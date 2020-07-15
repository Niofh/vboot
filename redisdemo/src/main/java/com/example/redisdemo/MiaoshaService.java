package com.example.redisdemo;

import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.redisdemo.config.rabbitmq.DirectRabbitConfig;
import com.example.redisdemo.config.rabbitmq.RabbitmqDeadOrder;
import com.example.redisdemo.dao.MiaoshaDao;
import com.example.redisdemo.dao.MiaoshaOrderDao;
import com.example.redisdemo.entity.Miaosha;
import com.example.redisdemo.entity.MiaoshaOrder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class MiaoshaService {
    public static final String KEY = "MIAOSHA_"; // 唯一秒杀产品id
    public static final String STOCK = "STOCK:"; // 库存数量

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 秒杀订单表DAO
     */
    @Autowired
    private MiaoshaOrderDao miaoshaOrderDao;

    @Autowired
    private MiaoshaDao miaoshaDao;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 秒杀接口
     *
     * @param userId
     * @param skuId
     */
    public void exec(String userId, String skuId) {
        RLock lock = redissonClient.getLock(KEY + skuId);
        try {
            //第一个参数10s=表示尝试获取分布式锁，并且最大的等待获取锁的时间为10s
            //第二个参数10s=表示上锁之后，10s内操作完毕将自动释放锁
            boolean tryLock = lock.tryLock(5, 10, TimeUnit.SECONDS);
            // 获取锁
            if (tryLock) {
                // 获取redis hash skuId库存数量
                int num = (int) redisTemplate.opsForValue().get(STOCK + skuId);
                if (num <= 0) {
                    log.info("商品已经抢完了！！！");
                    throw new RuntimeException("商品已经抢完了！！！");
                }

                // 减去redis缓存库存数量
                redisTemplate.opsForValue().decrement(STOCK + skuId, 1);
                HashMap<String, Object> objectObjectHashMap = new HashMap<>();
                objectObjectHashMap.put("userId", userId);
                objectObjectHashMap.put("skuId", skuId);
                // 异步下单
                rabbitTemplate.convertAndSend(DirectRabbitConfig.TEST_DIRECT_EXCHANGE, DirectRabbitConfig.TEST_DIRECT_ROUTING, JSONUtils.toJSONString(objectObjectHashMap));

            } else {
                log.info("【获取不到锁】人太多了，换种方式试试,websocket提示用户");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 秒杀商品操作
     */
    @Transactional(rollbackFor = {Exception.class})
    public void killSkuItem(String userId, String skuId) {
        // 判断用户是否秒杀过产品
        QueryWrapper<MiaoshaOrder> miaoshaOrderQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Miaosha> miaoshaQueryWrapper = new QueryWrapper<>();

        miaoshaOrderQueryWrapper.eq("user_id", userId).eq("sku_id", skuId);
        MiaoshaOrder miaoshaOrder = miaoshaOrderDao.selectOne(miaoshaOrderQueryWrapper);
        if (miaoshaOrder != null) {
            throw new RuntimeException("商品已经抢过了，不能再次抢购！！！");
        }
        // 判断库存是否充足
        miaoshaQueryWrapper.eq("sku_id", skuId).gt("stock", 0);
        Miaosha sku = miaoshaDao.selectOne(miaoshaQueryWrapper);
        if (sku == null) {
            throw new RuntimeException("商品已经抢完了！！！");
        }
        // 库存数量-1
        miaoshaQueryWrapper.clear();
        miaoshaQueryWrapper.eq("sku_id", skuId).gt("stock", 0);
        sku.setStock(sku.getStock() - 1);
        // 更新库存
        miaoshaDao.update(sku, miaoshaQueryWrapper);

        // 创建订单
        MiaoshaOrder miaoshaOrderNew = new MiaoshaOrder();
        miaoshaOrderNew.setSkuId(skuId);
        miaoshaOrderNew.setUserId(userId);
        miaoshaOrderNew.setPay(0);

        // 插入订单表
        int insert = miaoshaOrderDao.insert(miaoshaOrderNew);
        if (insert > 0) {
            // 发送异步邮箱消息
            this.sendDeadOrderMsg(miaoshaOrderNew);

        }


    }

    // 发送死信队列
    public void sendDeadOrderMsg(MiaoshaOrder miaoshaOrder) {
        CorrelationData correlationData = new CorrelationData(miaoshaOrder.getId());

        // 声明消息处理器 这个对消息进行处理 可以设置一些参数 对消息进行一些定制化处理 我们这里 来设置消息的编码 以及消息的过期时间
        // 因为在.net 以及其他版本过期时间不一致 这里的时间毫秒值 为字符串
        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            // 设置编码
            messageProperties.setContentEncoding("utf-8");
            // 设置过期时间60*1000毫秒,过期时间一分钟
            messageProperties.setExpiration("50000");
            return message;
        };

        // 加入死信队列判断是否超时支付，超时支付还原到仓库+1
        rabbitTemplate.convertAndSend(RabbitmqDeadOrder.ORDER_DEAD_EXCHANGE, RabbitmqDeadOrder.ORDER_FORWARD_ROUKINGKEY, miaoshaOrder, messagePostProcessor, correlationData);
    }

    // 将未支付订单返回到库存
    @Transactional(rollbackFor = {Exception.class})
    public void backStock(MiaoshaOrder miaoshaOrder) {
        MiaoshaOrder m = miaoshaOrderDao.selectById(miaoshaOrder);
        if (m != null) {
            int pay = m.getPay();
            if (pay == 0) {
                // 订单状态为-1
                m.setPay(-1);
                miaoshaOrderDao.updateById(m);
                // 未付款,加回库存
                miaoshaDao.addStock(miaoshaOrder.getSkuId(), 1);
            }
        }
    }


}

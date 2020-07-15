package com.example.redisdemo.rabbitmqReceiver;

import com.alibaba.druid.support.json.JSONUtils;
import com.example.redisdemo.MiaoshaService;
import com.example.redisdemo.config.rabbitmq.DirectRabbitConfig;
import com.example.redisdemo.config.rabbitmq.RabbitmqDeadOrder;
import com.example.redisdemo.config.rabbitmq.RabbitmqLimit;
import com.example.redisdemo.dao.MiaoshaOrderDao;
import com.example.redisdemo.entity.MiaoshaOrder;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
@Slf4j
public class DirectReceiver {

    @Autowired
    private MiaoshaService miaoshaService;

    /**
     * 排队限流
     */
    @RabbitListener(queues = RabbitmqLimit.LIMIT_QUEUE, containerFactory = "noAckMultiListenerContainer")
    @RabbitHandler
    public void process2(MiaoshaOrder miaoshaOrder, Channel channel, Message message) throws IOException {
        try {
            log.info("miaoshaOrder {}", miaoshaOrder);
            miaoshaService.exec(miaoshaOrder.getUserId(), miaoshaOrder.getSkuId());
        } catch (Exception e) { //这里写你的消费逻辑，如果出错，会被catch
            log.error("回滚订单错误：{} ", e.getMessage());
        }

    }

    /**
     * 处理订单消费者
     *
     * @param testMessage
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = DirectRabbitConfig.TEST_DIRECT_QUEUE, containerFactory = "singleListenerContainer")
    @RabbitHandler
    public void process(String testMessage, Channel channel, Message message) throws IOException {
        try {

            log.info("消费消息");
            log.info("DirectReceiver消费者收到消息  {}: ", JSONUtils.parse(testMessage));
            HashMap msg = (HashMap) JSONUtils.parse(testMessage);
            String userId = (String) msg.get("userId");
            String skuId = (String) msg.get("skuId");
            miaoshaService.killSkuItem(userId, skuId);

        } catch (Exception e) { //这里写你的消费逻辑，如果出错，会被catch
            log.error("下单失败：{}", e.getMessage());

            //这段代码表示，这次消息，我已经接受并消费掉了，不会再重复发送消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }

    }

    private MiaoshaOrderDao miaoshaOrderDao;

    /**
     * 订单超时处理
     */
    @RabbitListener(queues = RabbitmqDeadOrder.ORDER_DEAD_QUEUE, containerFactory = "singleListenerContainer")
    @RabbitHandler
    public void process3(MiaoshaOrder miaoshaOrder, Channel channel, Message message) throws IOException {
        try {

            log.info("超时订单：{}", miaoshaOrder);
            // 处理超时订单
            miaoshaService.backStock(miaoshaOrder);
        } catch (Exception e) { //这里写你的消费逻辑，如果出错，会被catch
            log.error("超时订单出错：{} ", e.getMessage());

            // 如果异常，会重复投递到队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }

    }
}
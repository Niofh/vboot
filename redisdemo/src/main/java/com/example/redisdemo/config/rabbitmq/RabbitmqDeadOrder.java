package com.example.redisdemo.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 死信队列说白 一个队列里设置时间，如果时间超时放在另外一个队列，监听这个队列来消费订单
 * 订单支付超时死信队列，如果超过30分钟还没有支付，将秒杀数量返回到库存
 */
@Configuration
public class RabbitmqDeadOrder {
    // 订单死信交换机
    public static final String ORDER_DEAD_EXCHANGE = "order.dead.exchange";
    public static final String ORDER_DEAD_QUEUE = "order.dead.queue";
    public static final String ORDER_DEAD_ROUKINGKEY = "order.dead.key";

    // 转发队列
    public static final String ORDER_FORWARD_QUEQU = "order.forward.queue";
    public static final String ORDER_FORWARD_ROUKINGKEY = "order.forward.key";


    // 订单转发队列,时间超时会转发 延迟ORDER_DEAD_QUEUE队列
    @Bean
    public Queue orderForWordQueue() {
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        HashMap<String, Object> args = new HashMap<>(2);
        // 声明死信交换机
        args.put("x-dead-letter-exchange", ORDER_DEAD_EXCHANGE);
        // 声明 死信路由键
        args.put("x-dead-letter-routing-key", ORDER_DEAD_ROUKINGKEY);

        return new Queue(ORDER_FORWARD_QUEQU, true, false, false, args);
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配键
    @Bean
    public Binding bindingOrderForWord() {
        return BindingBuilder.bind(orderForWordQueue()).to(orderDeadExchange()).with(ORDER_FORWARD_ROUKINGKEY);
    }

    @Bean
    public DirectExchange orderDeadExchange() {
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        return new DirectExchange(ORDER_DEAD_EXCHANGE, true, false);
    }


    @Bean
    public Queue orderDeadQueue() {
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        return new Queue(ORDER_DEAD_QUEUE, true);
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配键
    @Bean
    public Binding bindingOrderDead() {
        return BindingBuilder.bind(orderDeadQueue()).to(orderDeadExchange()).with(ORDER_DEAD_ROUKINGKEY);
    }


}

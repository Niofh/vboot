package com.example.redisdemo.config.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * 并发限流专用 创建队列和交换机
 */
@Configuration
@Slf4j
public class RabbitmqLimit {

    public static final String LIMIT_QUEUE = "limit.queue";
    public static final String LIMIT_EXCHANGE = "limit.exchange";
    public static final String LIMIT_ROUTERKEY = "limit.routingkey";

    @Bean
    public Queue executeLimitQueue() {
        Map<String, Object> argsMap = new HashMap<>();
        //限制channel中队列同一时刻通过的消息数量
        argsMap.put("x-max-length", 10);
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        return new Queue(LIMIT_QUEUE, true, false, false, argsMap);
    }

    @Bean
    public TopicExchange executeLimitExchange() {
        return new TopicExchange(LIMIT_EXCHANGE, true, false);
    }

    @Bean
    public Binding executeLimitBinding() {
        return BindingBuilder.bind(executeLimitQueue()).to(executeLimitExchange()).with(LIMIT_ROUTERKEY);
    }
}

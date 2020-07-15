package com.example.redisdemo.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class DirectRabbitConfig {
    /**
     * 交换机名称
     */
    public static final String TEST_DIRECT_EXCHANGE = "TestDirectExchange";

    /**
     * 队列名称
     */
    public static final String TEST_DIRECT_QUEUE = "TestDirectQueue";

    /**
     * 匹配路由
     */
    public static final String TEST_DIRECT_ROUTING = "TestDirectRouting";

    /**
     * 直连(Direct)交换机
     */
    @Bean
    public DirectExchange testDirectExchange() {
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        return new DirectExchange(TEST_DIRECT_EXCHANGE, true, false);
    }

    /**
     * 创建直连队列
     *
     * @return
     */
    @Bean
    public Queue testDirectQueue() {
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        return new Queue(TEST_DIRECT_QUEUE, true);
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
    @Bean
    public Binding bindingDirect() {
        return BindingBuilder.bind(testDirectQueue()).to(testDirectExchange()).with(TEST_DIRECT_ROUTING);
    }


}

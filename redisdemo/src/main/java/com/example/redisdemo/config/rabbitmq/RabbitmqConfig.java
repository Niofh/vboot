package com.example.redisdemo.config.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitmqConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    //单一消费者,这个为了处理秒杀订单，防止超卖和防止回滚入库时候错误
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        factory.setPrefetchCount(1);  // 每次只处理一条消息
        return factory;
    }

    // 多个消费者,并且用于秒杀，无ack模式(排队模式，可以丢失数据)
    @Bean(name = "noAckMultiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factoryConfigurer.configure(factory, connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());

        // spring-rabbit消费过程解析及AcknowledgeMode选择 https://blog.csdn.net/weixin_38380858/article/details/84963944
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);  // RabbitMQ默认是自动确认，无ack模式，会丢失消息

        //  https://www.cnblogs.com/qts-hope/p/11242559.html
        factory.setConcurrentConsumers(5);    // # 最小的消费者数量
        factory.setMaxConcurrentConsumers(15);
        factory.setPrefetchCount(10); // 指定一个请求能处理多少个消息，如果有事务的话，必须大于等于transaction数量.
        return factory;
    }

    /**
     * 定制生产者配置
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        //确认消息已发送到交换机(Exchange)  //消息确认机制
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("ConfirmCallback:     " + "本次消息的唯一标识是:" + correlationData);
                System.out.println("ConfirmCallback:     " + "确认情况：" + ack);
                if(!ack){
                    System.out.println("消息拒绝接收的原因是:" + cause);
                }else{
                    System.out.println("消息发送成功");
                }
            }
        });

        // 确认消息已发送到队列(Queue)   //有关消息被退回来的具体描述消息
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("ReturnCallback:     " + "退回来消息：" + message);
                System.out.println("ReturnCallback:     " + "退回来回应码：" + replyCode);
                System.out.println("ReturnCallback:     " + "退回来回应信息：" + replyText);
                System.out.println("ReturnCallback:     " + "退回来交换机：" + exchange);
                System.out.println("ReturnCallback:     " + "退回来路由键：" + routingKey);
            }
        });
        return rabbitTemplate;
    }
}

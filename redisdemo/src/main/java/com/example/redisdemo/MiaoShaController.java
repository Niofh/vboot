package com.example.redisdemo;
import com.example.redisdemo.config.rabbitmq.RabbitmqLimit;
import com.example.redisdemo.entity.MiaoshaOrder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/miaosha")
public class MiaoShaController {

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/exec")
    public void exec(String userId, String skuId) {

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setSkuId(skuId);
        miaoshaOrder.setUserId(userId);

        // 异步排队下单
        rabbitTemplate.convertAndSend(RabbitmqLimit.LIMIT_EXCHANGE, RabbitmqLimit.LIMIT_ROUTERKEY, miaoshaOrder);
    }
}

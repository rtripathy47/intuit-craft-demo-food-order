package com.food.order.system.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String DISPATCH_QUEUE = "dispatchQueue";

    @Bean
    public Queue dispatchQueue() {
        return new Queue(DISPATCH_QUEUE, true);
    }
}

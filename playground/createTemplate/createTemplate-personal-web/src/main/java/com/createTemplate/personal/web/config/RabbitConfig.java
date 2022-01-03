///*
//* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
//*
//* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
//*/
//package com.createTemplate.personal.web.config;
//import org.quartz.Scheduler;
//import org.springframework.amqp.core.AcknowledgeMode;
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.beans.factory.config.ConfigurableBeanFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Scope;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//
//import com.rabbitmq.client.Channel;  
//  
///**  
// * Qmqp Rabbitmq  
// *   
// * http://docs.spring.io/spring-amqp/docs/1.4.5.RELEASE/reference/html/  
// *   
// * @author lkl  
// * @version $Id: AmqpConfig.java, v 0.1 2015年11月01日 下午2:05:37 lkl Exp $  
// */  
//  
//@Configuration  
//public class RabbitConfig {  
//    @Value("${spring.rabbitmq.host}")
//    private String host;
//    @Value("${spring.rabbitmq.port}")
//    private Integer port;
//    @Value("${spring.rabbitmq.username}")
//    private String username;
//    @Value("${spring.rabbitmq.password}")
//    private String password;
//    
//    public static final String EXCHANGE   = "spring-boot-exchange";  
//    public static final String ROUTINGKEY = "spring-boot-routingKey";  
//    @Bean  
//    public ConnectionFactory connectionFactory() {  
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();  
//        //connectionFactory.setAddresses(host+":"+port);
//        connectionFactory.setHost(host);
//        connectionFactory.setPort(port);
//        connectionFactory.setUsername(username);  
//        connectionFactory.setPassword(password);  
//        connectionFactory.setVirtualHost("/");  
//        connectionFactory.setPublisherConfirms(true); //必须要设置  
//        return connectionFactory;  
//    }  
//   
// 
//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean() {
//        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
//        return schedulerFactoryBean;
//    }
// 
//    @Bean
//    public Scheduler scheduler() {
//        return schedulerFactoryBean().getScheduler();
//    }
//
//    
//    @Bean  
//    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)  
//    //必须是prototype类型  
//    public RabbitTemplate rabbitTemplate() {  
//        RabbitTemplate template = new RabbitTemplate(connectionFactory());  
//        return template;  
//    }  
//  
//    /**  
//     * 针对消费者配置  
//     * 1. 设置交换机类型  
//     * 2. 将队列绑定到交换机  
//     *   
//     *   
//        FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念  
//        HeadersExchange ：通过添加属性key-value匹配  
//        DirectExchange:按照routingkey分发到指定队列  
//        TopicExchange:多关键字匹配  
//     */  
//    @Bean  
//    public DirectExchange defaultExchange() {  
//        return new DirectExchange(EXCHANGE);  
//    }  
//  
//    @Bean  
//    public Queue queue() {  
//        return new Queue("spring-boot-queue", true); //队列持久  
//  
//    }  
//  
//    @Bean  
//    public Queue scheduleQueue() {  
//        return new Queue("schedule", true); //队列持久  
//    }  
//    
//    @Bean  
//    public Binding binding() {  
//        return BindingBuilder.bind(queue()).to(defaultExchange()).with(RabbitConfig.ROUTINGKEY);  
//    }  
//  
//    @Bean  
//    public SimpleMessageListenerContainer messageContainer() {  
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());  
//        container.setQueues(queue());  
//        container.setExposeListenerChannel(true);  
//        container.setMaxConcurrentConsumers(1);  
//        container.setConcurrentConsumers(1);  
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认  
//        container.setMessageListener(new ChannelAwareMessageListener() {  
//            @Override  
//            public void onMessage(Message message, Channel channel) throws Exception {  
//                byte[] body = message.getBody();  
//                System.out.println("receive msg : " + new String(body));  
//                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费  
//            }  
//        });  
//        return container;  
//    }  
//  
//    
//    @Bean
//    public Queue queueMessages() {
//        return new Queue("topic.checkIssue");
//    }
//    
//    
//} 
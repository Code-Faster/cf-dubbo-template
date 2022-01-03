///*
//* Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
//*
//* 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的 
//*/
//package com.createTemplate.personal.web.rabbitMq.sender;
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TopicSender {
//
//    @Autowired
//    private AmqpTemplate rabbitTemplate;
//
//    public void send() {
//        String msg1 = "I am topic.mesaage msg======";
//        System.out.println("sender1 : " + msg1);
//        this.rabbitTemplate.convertAndSend("exchange", "topic.message", msg1);
//        
//        String msg2 = "I am topic.mesaages msg########";
//        System.out.println("sender2 : " + msg2);
//        this.rabbitTemplate.convertAndSend("exchange", "topic.messages", msg2);
//    }
//
//}
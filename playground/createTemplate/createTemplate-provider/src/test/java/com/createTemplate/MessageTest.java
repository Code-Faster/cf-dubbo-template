package com.createTemplate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.createTemplate.api.common.dubbo.service.MessageService;
import com.createTemplate.api.core.doubbo.service.PersonService;
import com.createTemplate.model.core.vo.TPersonVO;
import com.createTemplate.provider.Application;



/**
 * 单元测试
 *
 * @author libiqi
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)

public class MessageTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MessageService messageService;

    @Test
    public void sendMessage() {
        TPersonVO newTPersonVO = new TPersonVO();
        newTPersonVO.setPhoneNum("15074919215");
        try {
            messageService.sendMessage(15074919215L, "123456");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.error("\r\ntest1------" + JSON.toJSONString("cg"));
    }


}

package com.createTemplate;

import com.createTemplate.api.base.dubbo.service.RoleService;
import com.createTemplate.api.core.doubbo.service.PersonService;
import com.createTemplate.model.core.pojo.TPerson;
import com.createTemplate.provider.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * 单元测试
 *
 * @author libiqi
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)

public class PersonTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    PersonService personService;

    @Test
    public void getMenu() {
        TPerson byId = personService.findById(1L);
        logger.error("\r\ntest1------" + byId.toString());
    }

}

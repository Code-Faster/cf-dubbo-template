package com.createTemplate.provider.mail;

import com.alibaba.dubbo.config.annotation.Service;
import com.createTemplate.api.mail.SendEmailService;
import com.createTemplate.provider.base.dao.impl.MybatisDaoImpl;

/**
 * 发送邮件service实现类
 * 
 * @author: 
 * @date: 2018年8月3日 上午10:55:38
 * @version V1.0
 */
@Service(version = "1.0.0", retries = -1, timeout = 6000)
public class SendEmailServiceImpl extends MybatisDaoImpl implements SendEmailService {

    /**
     * 发送查看了联系方式邮件
     * 
     * @param email
     * @param dbCompany
     */
//    @Async
//    public void sendCommunicateEmail(String recievePersonId, SendPosition dbSendPosition) {
//
//        PositionInfoService positionInfoService = (PositionInfoService) this.applicationContext.getBean(
//                "positionInfoService");
//        CompanyService companyService = (CompanyService) this.applicationContext.getBean("companyService");
//        PositionInfo dbPositionInfo = positionInfoService.findById(dbSendPosition.getPosition_id());
//        CompanyInfoVo dbCompanyInfoVo = companyService.getCompanyById(dbPositionInfo.getCompany_id());
//        PersonService personService = (PersonService) this.applicationContext.getBean("personService");
//        // Person recievePerson = personService.findById(recievePersonId);
//
//        // /**查询消息模板*/
//        // NoticeMouldDaoImpl noticeMouldDao = new NoticeMouldDaoImpl();
//        // noticeMouldDao.findDefaultByPersonIdMouldType(sessionPersonVo.getId(),
//        // mouldType)
//
//        MailService mailService = (MailService) this.applicationContext.getBean("mailService");
//        Map<String, String> map = new HashMap<String, String>();
//        // map.put("websiteUrl", QichengConfig.get("qichengInternet"));
//        // System.out.println(QichengConfig.get("logoUrl"));
//        // map.put("logoUrl", QichengConfig.get("logoUrl"));
//        // map.put("websiteName", "骄傲网");
//        // map.put("serviceEmail", QichengConfig.get("serviceEmail"));
//        // map.put("serviceTel", QichengConfig.get("serviceTel"));
//        // map.put("serviceQQ", QichengConfig.get("serviceQQ"));
//        // map.put("companyUrl", dbCompany.getCompanyUrl());
//        // map.put("companyName", dbCompany.getCompanyName());
//        // map.put("postUrl", this.getPositionUrl(dbSendPosition.getId()));
//        // map.put("postName", dbPositionInfo.getPositionName());
//        // map.put("resumeStatus", "通过筛选，待沟通");
//        // map.put("feedback", "您的简历已经通过筛选，三个工作日内企业将与您进行沟通，非常感谢您对我们的支持，请耐心等待！");
//        // map.put("employeeName", recievePerson.getRealName());
//        //// map.put("refuseContent", "决绝内容");
//        // String subject = dbCompany.getCompanyName() + "-" +
//        // dbPositionInfo.getPositionName() + "反馈通知.通过筛选，待沟通";
//        // try {
//        // mailService.sendMimeMessage(recievePerson.getEmail(), subject,
//        // TemplateUtil.merge("pending_communication", map), new HashMap());
//        // } catch (Exception e) {
//        // // TODO Auto-generated catch block
//        // e.printStackTrace();
//        // }
//    }

}

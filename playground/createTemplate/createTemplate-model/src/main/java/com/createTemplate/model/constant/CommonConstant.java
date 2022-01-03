/*
 * Copyright (c) 2017-2020 SHENZHEN ZHONGTUIKEJI SCIENCE AND TECHNOLOGY DEVELOP CO., LTD. All rights reserved.
 *
 * 注意：本内容仅限于深圳市科瑞特网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.createTemplate.model.constant;

import java.util.HashMap;
import java.util.Map;

public class CommonConstant {

    /**
     * TokenProcessor 类参数
     */
    public static final String TRANSACTION_TOKEN_KEY = "org.apache.struts.action.TOKEN";

    public static final String TOKEN_KEY = "com.createTemplate.token";

    public static final String SESSION_TYPE_WECHAT = "createTemplate";

    public static final String SESSION_TYPE_ADMIN = "admin";

    /**
     * 使用状态
     */
    public static final int STATUS_USED = 0;

    /**
     * 禁用状态
     */
    public static final int STATUS_UNUSE = 1;

    public final class TaskType {
        /**
         * 任务类型  0 积分；
         */
        public static final int TYPE_POINT = 0;
        /**
         * 任务类型  1 奖金
         */
        public static final int TYPE_BONUS = 1;
    }

    public final class TaskFlag {
        /**
         * 单次
         */
        public final static int TASKFLAG_ONE = 0;

        /**
         * 多次
         */
        public final static int TASKFLAG_MUILT = 1;

    }


    public final class buttonEName {

        /**
         * 查询
         */
        public final static String queryButton = "queryButton";

        /**
         * 新增
         */
        public final static String addButton = "addButton";

        /**
         * 修改
         */
        public final static String editButton = "editButton";

        /**
         * 删除
         */
        public final static String deleteButton = "deleteButton";

        /**
         * 重置密码
         */
        public final static String resetPwdButton = "resetPwdButton";

        /**
         * 激活
         */
        public final static String enableButton = "enableButton";

        /**
         * 禁用
         */
        public final static String disableButton = "disableButton";

        /**
         * 查看
         */
        public final static String viewButton = "viewButton";

        /**
         * 禁用激活
         */
        public final static String disableOrEnableButton = "disableOrEnableButton";

        /**
         * 导出
         */
        public final static String expButton = "expButton";

        /**
         * 导出
         */
        public final static String exportExcelButton = "exportExcelButton";

        /**
         * 一审按钮
         */
        public final static String firstAuditButton = "firstAuditButton";

        /**
         * 二审按钮
         */
        public final static String secondAuditButton = "secondAuditButton";

        /**
         * 三审按钮
         */
        public final static String thirdAuditButton = "thirdAuditButton";

    }

    public final class menuEName {

        /**
         * 管理员管理
         */
        public final static String glygl = "UserManage";

        /**
         * 角色管理
         */
        public final static String jsgl = "JurisdictionManage";

        /**
         * 用户管理
         */
        public final static String yhgl = "yhgl";

        /**
         * 超级管理员管理
         */
        public final static String cjglygl = "cjglygl";

        /**
         * 数据字典管理
         */
        public final static String sjzdgl = "sjzdgl";

        /**
         * 普通用户管理
         */
        public final static String ptyhgl = "ptyhgl";

        /**
         * 系统配置管理
         */
        public final static String xtpzgl = "xtpzgl";

        /**
         * 菜单管理
         */
        public final static String cdgl = "MenuManage";

        /**
         * 按钮管理
         */
        public final static String angl = "angl";

        /**
         * 系统配置类型管理
         */
        public final static String xtpzlxgl = "xtpzlxgl";

        /**
         * 省市区管理
         */
        public final static String ssqgl = "ssqgl";

        /**
         * 交易明细
         */
        public final static String jymx = "jymx";
    }

    public final class OperType {

        public final static int SAVE_OR_UPDATE = 0;

        public final static int DELETE = 1;
    }

    public final class Order {
        /**
         * 0 未支付
         */
        public final static int ORDER_NOTPAY = 0;
        /**
         * 1 已支付
         */
        public final static int ORDER_PAY = 1;
    }

    /**
     * 支付渠道
     */
    public final class channel {
        /**
         * 0 账户支付；；
         */
        public final static int CHANNEL_ACCOUNT = 0;
        /**
         * 1 微信支付；
         */
        public final static int CHANNEL_WECHAT = 1;
        /**
         * 2 支付宝支付
         */
        public final static int CHANNEL_ALIPAY = 2;
        /**
         * 3 银联支付
         */
        public final static int CHANNEL_UNIONPAY = 3;
    }

    public final class EnumType {

        /*** 行业领域 */
        public final static String ENUMTYPE_COMPANYBUSINESSTYPE = "companyBusinessType";
    }


    /**
     * 序列号
     *
     * @author aijian
     */
    public class Sequence {

        /**
         * 充值流水号
         */
        public final static String rechargeSerialNo = "rechargeSerialNo";
        /**
         * 支付订单号
         */
        public final static String payOrderNo = "payOrderNo";
        /**
         * 退款批次号
         */
        public final static String refundBatchNo = "refundBatchNo";

    }

    /**
     * @return
     * @Author libiqi
     * @Description 支付回调来源
     * @Date 11:36 上午 2019/11/1
     * @Param
     **/
    public static class PayFrom {

        /**
         * 充值
         */
        public final static Integer UNDER_RECHARGE = 0;

    }
}

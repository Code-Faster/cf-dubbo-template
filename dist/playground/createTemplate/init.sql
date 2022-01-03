
INSERT INTO `T_USER` (`id`,`user_name`,`real_name`,`user_password`,`input_date`,`update_date`,`status`,`role_id`) VALUES (1,'admin','系统管理员','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,0,1);

INSERT INTO `T_ROLE` (`id`,`role_name`,`remark`,`status`) VALUES (1,'超级管理员','超级管理员权限',0);

INSERT INTO `T_USERS_ROLE` (`id`,`cust_id`,`user_name`,`role_id`,`status`,`input_date`,`update_date`) VALUES (1,1,'admin',1,'1',NULL,NULL);

INSERT INTO `T_MENU` (`id`,`menu_cname`,`menu_ename`,`parent_id`,`level`,`icon_cls`,`url`,`sort`,`status`) VALUES (1,'首页','dashboard',NULL,'1','dashboard','/dashboard',0,0);
INSERT INTO `T_MENU` (`id`,`menu_cname`,`menu_ename`,`parent_id`,`level`,`icon_cls`,`url`,`sort`,`status`) VALUES (2,'基础管理','basic',NULL,'1','table','/basic',1,0);
INSERT INTO `T_MENU` (`id`,`menu_cname`,`menu_ename`,`parent_id`,`level`,`icon_cls`,`url`,`sort`,`status`) VALUES (3,'配置管理','config',NULL,'1','SettingOutlined','/config',2,0);
INSERT INTO `T_MENU` (`id`,`menu_cname`,`menu_ename`,`parent_id`,`level`,`icon_cls`,`url`,`sort`,`status`) VALUES (4,'系统监控','monitor',NULL,'1','FundProjectionScreenOutlined','/monitor',3,0);
INSERT INTO `T_MENU` (`id`,`menu_cname`,`menu_ename`,`parent_id`,`level`,`icon_cls`,`url`,`sort`,`status`) VALUES (5,'业务管理','business',NULL,'1','GoldOutlined','/business',4,0);
INSERT INTO `T_MENU` (`id`,`menu_cname`,`menu_ename`,`parent_id`,`level`,`icon_cls`,`url`,`sort`,`status`) VALUES (6,'报表管理','statistical',NULL,'1','FundOutlined','/statistical',5,1);

INSERT INTO `T_MENU` (`id`,`menu_cname`,`menu_ename`,`parent_id`,`level`,`icon_cls`,`url`,`sort`,`status`) VALUES (7,'工作台','dashboard-workplace',1,'2','','/dashboard/workplace',0,0);

INSERT INTO `T_MENU` (`id`,`menu_cname`,`menu_ename`,`parent_id`,`level`,`icon_cls`,`url`,`sort`,`status`) VALUES (8,'管理员管理','basic-user-list',2,'2','','/basic/user-list',0,0);
INSERT INTO `T_MENU` (`id`,`menu_cname`,`menu_ename`,`parent_id`,`level`,`icon_cls`,`url`,`sort`,`status`) VALUES (9,'菜单管理','basic-menu-list',2,'2','','/basic/menu-list',1,0);
INSERT INTO `T_MENU` (`id`,`menu_cname`,`menu_ename`,`parent_id`,`level`,`icon_cls`,`url`,`sort`,`status`) VALUES (10,'权限管理','basic-role-list',2,'2','','/basic/role-list',2,0);

INSERT INTO `T_MENU` (`id`,`menu_cname`,`menu_ename`,`parent_id`,`level`,`icon_cls`,`url`,`sort`,`status`) VALUES (11,'数据字典管理','config-enum-list',3,'2','','/config/enum-list',0,0);
INSERT INTO `T_MENU` (`id`,`menu_cname`,`menu_ename`,`parent_id`,`level`,`icon_cls`,`url`,`sort`,`status`) VALUES (12,'数据字典类型管理','config-enum-type-list',3,'2','','/config/enum-type-list',1,0);

INSERT INTO `T_MENU` (`id`,`menu_cname`,`menu_ename`,`parent_id`,`level`,`icon_cls`,`url`,`sort`,`status`) VALUES (13,'用户管理','business-person-list',5,'2','','/business/person-list',0,0);

INSERT INTO `T_MENU` (`id`,`menu_cname`,`menu_ename`,`parent_id`,`level`,`icon_cls`,`url`,`sort`,`status`) VALUES (14,'在线用户','monitor-person-list',12,'2','','/monitor/person-list',0,0);
INSERT INTO `T_MENU` (`id`,`menu_cname`,`menu_ename`,`parent_id`,`level`,`icon_cls`,`url`,`sort`,`status`) VALUES (15,'服务监控','monitor-server-list',5,'2','','/monitor/server-list',1,0);

INSERT INTO `T_MENU` (`id`,`menu_cname`,`menu_ename`,`parent_id`,`level`,`icon_cls`,`url`,`sort`,`status`) VALUES (16,'用户报表','statistical-person-list',6,'2','','/statistical/person-list',0,0);


INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (1,'basic-user-list','addButton','添加按钮',8,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (2,'basic-user-list','deleteButton','删除按钮',8,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (3,'basic-user-list','editButton','编辑按钮',8,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (4,'basic-user-list','queryButton','查询按钮',8,0);

INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (5,'basic-menu-list','addButton','添加按钮',9,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (6,'basic-menu-list','deleteButton','删除按钮',9,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (7,'basic-menu-list','editButton','编辑按钮',9,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (8,'basic-menu-list','queryButton','查询按钮',9,0);

INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (9,'basic-role-list','addButton','添加按钮',10,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (10,'basic-role-list','deleteButton','删除按钮',10,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (11,'basic-role-list','editButton','编辑按钮',10,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (12,'basic-role-list','queryButton','查询按钮',10,0);

INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (13,'config-enum-list','addButton','添加按钮',11,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (14,'config-enum-list','deleteButton','删除按钮',11,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (15,'config-enum-list','editButton','编辑按钮',11,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (16,'config-enum-list','queryButton','查询按钮',11,0);

INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (17,'config-enum-type-list','addButton','新增按钮',12,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (19,'config-enum-type-list','deleteButton','删除按钮',12,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (18,'config-enum-type-list','editButton','修改按钮',12,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (20,'config-enum-type-list','queryButton','查询按钮',12,0);

INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (21,'business-person-list','addButton','添加按钮',13,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (22,'business-person-list','deleteButton','删除按钮',13,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (23,'business-person-list','editButton','编辑按钮',13,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (24,'business-person-list','queryButton','查询按钮',13,0);

INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (25,'monitor-person-list','queryButton','查询按钮',14,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (26,'monitor-server-list','queryButton','查询按钮',15,0);
INSERT INTO `T_BUTTON` (`id`,`menu_ename`,`button_ename`,`button_cname`,`menu_id`,`status`) VALUES (27,'statistical-person-list','queryButton','查询按钮',16,0);



INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,1,'dashboard',NULL);
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,2,'basic',NULL);
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,3,'config',NULL);
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,4,'monitor',NULL);
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,5,'business',NULL);
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,6,'statistical',NULL);

INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,7,'dashboard-workplace',NULL);
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,8,'basic-user-list',NULL);
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,9,'basic-menu-list',NULL);
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,10,'basic-role-list',NULL);
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,11,'config-enum-list',NULL);
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,12,'config-enum-type-list',NULL);
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,13,'business-person-list',NULL);
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,14,'monitor-person-list',NULL);
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,15,'monitor-server-list',NULL);
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,16,'statistical-person-list',NULL);

INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,8,'basic-user-list','addButton,deleteButton,editButton,queryButton');
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,9,'basic-menu-list','addButton,deleteButton,editButton,queryButton');
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,10,'basic-role-list','addButton,deleteButton,editButton,queryButton');
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,11,'config-enum-list','addButton,deleteButton,editButton,queryButton');
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,12,'config-enum-type-list','addButton,editButton,deleteButton,queryButton');
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,13,'business-person-list','addButton,deleteButton,editButton,queryButton');

INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,14,'monitor-person-list','queryButton');
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,15,'monitor-server-list','queryButton');
INSERT INTO `T_ROLE_MENU` (`role_id`,`menu_id`,`menu_ename`,`button_ename`) VALUES (1,16,'statistical-person-list','queryButton');




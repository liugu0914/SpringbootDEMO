/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50728
Source Host           : localhost:3306
Source Database       : lyc

Target Server Type    : MYSQL
Target Server Version : 50728
File Encoding         : 65001

Date: 2019-12-12 15:48:05
*/

create database lyc;
use lyc;

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user
(
    id       varchar(32) NOT NULL COMMENT 'id',
    username varchar(100)  DEFAULT NULL COMMENT '昵称',
    email    varchar(100)  DEFAULT NULL COMMENT 'email',
    account  varchar(100)  DEFAULT NULL COMMENT '账号',
    password varchar(100)  DEFAULT NULL COMMENT '密码',
    imgurl   varchar(1000) DEFAULT NULL COMMENT '头像',
    type     varchar(100)  DEFAULT NULL COMMENT '登陆方式。 local github gitee',
    enable   varchar(10)   DEFAULT NULL COMMENT '是否可用 0否1是',
    ctime    datetime      DEFAULT NULL COMMENT '创建时间',
    updtime  datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='用户表';


DROP TABLE IF EXISTS t_user_grant;
CREATE TABLE t_user_grant
(
    id        varchar(32) NOT NULL,
    userid    varchar(32)   DEFAULT NULL COMMENT 'userid',
    granttype varchar(255)  DEFAULT NULL COMMENT '类型 github gitee',
    onlyid    varchar(255)  DEFAULT NULL COMMENT '唯一id',
    token     varchar(255)  DEFAULT NULL COMMENT 'token',
    imgurl    varchar(1000) DEFAULT NULL COMMENT '第三方头像',
    nickname  varchar(255)  DEFAULT NULL COMMENT '昵称',
    ctime     datetime      DEFAULT NULL COMMENT '创建时间',
    updtime   datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='授权登陆表';


DROP TABLE IF EXISTS t_oauth_code;
CREATE TABLE t_oauth_code
(
    id        varchar(32)  NOT NULL COMMENT 'id',
    client_id varchar(50)  NOT NULL COMMENT '客户端id',
    code      varchar(200) NOT NULL COMMENT '授权码',
    user_id   varchar(50)  NOT NULL COMMENT '用户id',
    status    varchar(10) DEFAULT NULL COMMENT '状态',
    ctime     datetime    DEFAULT NULL COMMENT '创建时间',
    updtime   datetime    DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='授权码';


DROP TABLE IF EXISTS t_user_agent;
CREATE TABLE t_user_agent
(
    id        varchar(32) NOT NULL,
    userid    varchar(32)  DEFAULT NULL COMMENT '用户id',
    ip        varchar(100) DEFAULT NULL COMMENT 'ip地址',
    useragent varchar(500) DEFAULT NULL COMMENT '浏览器总信息',
    browser   varchar(100) DEFAULT NULL COMMENT '浏览器名称',
    version   varchar(100) DEFAULT NULL COMMENT '浏览器版本',
    system    varchar(100) DEFAULT NULL COMMENT '系统名称',
    mac       varchar(100) DEFAULT NULL COMMENT 'mac地址',
    now       varchar(10)  DEFAULT NULL COMMENT '是否是最新登录情况0否 1是',
    ctime     datetime     DEFAULT NULL,
    updtime   datetime     DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户地址';


drop table if exists t_role;

/*==============================================================*/
/* Table: t_role                                                */
/*==============================================================*/
create table t_role
(
    id                   varchar(32) not null comment 'id',
    name                 varchar(100) comment '角色',
    remark               varchar(1000) comment '角色说明',
    enable               varchar(10) comment '是否启用',
    weight               varchar(10) comment '角色权重',
    ctime                datetime comment '创建时间',
    updtime              datetime comment '更新时间',
    primary key (id)
);
alter table t_role comment '角色t_role';

drop table if exists t_permission;

/*==============================================================*/
/* Table: t_permission                                          */
/*==============================================================*/
create table t_permission
(
    id                   varchar(32) not null comment 'id',
    name                 varchar(100) comment '权限名称',
    type                 varchar(10) comment '权限类型',
    enable               varchar(10) comment '是否可用',
    ctime                datetime comment '创建时间',
    updtime              datetime comment '更新时间',
    primary key (id)
);
alter table t_permission comment '权限t_permission';

drop table if exists t_user_role;

/*==============================================================*/
/* Table: t_user_role                                           */
/*==============================================================*/
create table t_user_role
(
    id                   varchar(32) not null comment 'id',
    userid               varchar(32) comment '用户id',
    roleid               varchar(32) comment '角色id',
    ctime                datetime comment '创建时间',
    updtime              datetime comment '更新时间',
    primary key (id)
);

alter table t_user_role comment '用户对应角色表t_user_role';

drop table if exists t_role_permission;

/*==============================================================*/
/* Table: t_role_permission                                     */
/*==============================================================*/
create table t_role_permission
(
    id                   varchar(32) not null comment 'id',
    permissionid         varchar(32) comment '权限id',
    roleid               varchar(32) comment '角色id',
    ctime                datetime comment '创建时间',
    updtime              datetime comment '更新时间',
    primary key (id)
);

alter table t_role_permission comment '角色对应权限表t_role_permission';


drop table if exists t_menu;

/*==============================================================*/
/* Table: t_menu                                                */
/*==============================================================*/
create table t_menu
(
    id                   varchar(32) not null comment 'id',
    name                 varchar(100) comment '名称',
    icon                 varchar(100) comment '图标',
    url                  varchar(500) comment '地址',
    enable               varchar(10) comment '是否可用',
    parent               varchar(10) comment '是否为父节点 0否 1是',
    level                int comment '菜单级别',
    superid              varchar(32) comment '上级菜单',
    ordernum             int comment '菜单顺序',
    appid                varchar(32) comment '应用id',
    ctime                datetime comment '创建时间',
    updtime              datetime comment '更新时间',
    primary key (id)
);

alter table t_menu comment '菜单t_menu';

drop table if exists t_app;

/*==============================================================*/
/* Table: t_app                                                 */
/*==============================================================*/
create table t_app
(
    id                   varchar(32) not null comment 'id',
    name                 varchar(100) comment '应用名称',
    shortname            varchar(30) comment '应用标识',
    des                  varchar(255) comment '应用描述',
    enable               varchar(10) comment '是否启用',
    ctime                datetime comment '创建时间',
    updtime              datetime comment '更新时间',
    primary key (id)
);

alter table t_app comment '应用表t_app';

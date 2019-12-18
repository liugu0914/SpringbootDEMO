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

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `id`       varchar(32) NOT NULL COMMENT 'id',
    `username` varchar(100)  DEFAULT NULL COMMENT '昵称',
    `email`    varchar(100)  DEFAULT NULL COMMENT 'email',
    `account`  varchar(100)  DEFAULT NULL COMMENT '账号',
    `password` varchar(100)  DEFAULT NULL COMMENT '密码',
    `imgurl`   varchar(1000) DEFAULT NULL COMMENT '头像',
    `type`     varchar(100)  DEFAULT NULL COMMENT '登陆方式。 local github gitee',
    `enable`   varchar(10)   DEFAULT NULL COMMENT '是否可用 0否1是',
    `ctime`    datetime      DEFAULT NULL COMMENT '创建时间',
    `updtime`  datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='用户表';


DROP TABLE IF EXISTS `t_user_grant`;
CREATE TABLE `t_user_grant`
(
    `id`        varchar(32) NOT NULL,
    `userid`    varchar(32)   DEFAULT NULL COMMENT 'userid',
    `granttype` varchar(255)  DEFAULT NULL COMMENT '类型 github gitee',
    `onlyid`    varchar(255)  DEFAULT NULL COMMENT '唯一id',
    `token`     varchar(255)  DEFAULT NULL COMMENT 'token',
    `imgurl`    varchar(1000) DEFAULT NULL COMMENT '第三方头像',
    `nickname`  varchar(255)  DEFAULT NULL COMMENT '昵称',
    `ctime`     datetime      DEFAULT NULL COMMENT '创建时间',
    `updtime`   datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='授权登陆表';


DROP TABLE IF EXISTS `t_oauth_code`;
CREATE TABLE `t_oauth_code`
(
    `id`        varchar(32)  NOT NULL COMMENT 'id',
    `client_id` varchar(50)  NOT NULL COMMENT '客户端id',
    `code`      varchar(200) NOT NULL COMMENT '授权码',
    `user_id`   varchar(50)  NOT NULL COMMENT '用户id',
    `status`    varchar(10) DEFAULT NULL COMMENT '状态',
    `ctime`     datetime    DEFAULT NULL COMMENT '创建时间',
    `updtime`   datetime    DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='授权码';


DROP TABLE IF EXISTS `t_user_agent`;
CREATE TABLE `t_user_agent`
(
    `id`        varchar(32) NOT NULL,
    `userid`    varchar(32)  DEFAULT NULL COMMENT '用户id',
    `ip`        varchar(100) DEFAULT NULL COMMENT 'ip地址',
    `useragent` varchar(500) DEFAULT NULL COMMENT '浏览器总信息',
    `browser`   varchar(100) DEFAULT NULL COMMENT '浏览器名称',
    `version`   varchar(100) DEFAULT NULL COMMENT '浏览器版本',
    `system`    varchar(100) DEFAULT NULL COMMENT '系统名称',
    `mac`       varchar(100) DEFAULT NULL COMMENT 'mac地址',
    `now`       varchar(10)  DEFAULT NULL COMMENT '是否是最新登录情况0否 1是',
    `ctime`     datetime     DEFAULT NULL,
    `updtime`   datetime     DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户地址';
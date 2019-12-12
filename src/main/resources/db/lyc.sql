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

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` varchar(32) NOT NULL,
  `username` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `account` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `updtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `t_user_grant`;
CREATE TABLE `t_user_grant` (
  `id` varchar(32) NOT NULL,
  `userid` varchar(32) DEFAULT NULL,
  `granttype` varchar(255) DEFAULT NULL,
  `onlyid` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `imgurl` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `updtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
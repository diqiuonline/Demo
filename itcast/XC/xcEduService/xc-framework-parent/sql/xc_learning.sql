/*
Navicat MySQL Data Transfer

Source Server         : xcMySQL
Source Server Version : 80017
Source Host           : 192.168.2.101:3306
Source Database       : xc_learning

Target Server Type    : MYSQL
Target Server Version : 80017
File Encoding         : 65001

Date: 2019-09-04 21:56:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for xc_learning_course
-- ----------------------------
DROP TABLE IF EXISTS `xc_learning_course`;
CREATE TABLE `xc_learning_course` (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `course_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程id',
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `valid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '有效性',
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '选课状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `xc_learning_list_unique` (`course_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of xc_learning_course
-- ----------------------------
INSERT INTO `xc_learning_course` VALUES ('402882836afe986b016afe9ec6770001', '4028e581617f945f01617f9dabc40000', '49', null, null, null, '501001');

-- ----------------------------
-- Table structure for xc_task_his
-- ----------------------------
DROP TABLE IF EXISTS `xc_task_his`;
CREATE TABLE `xc_task_his` (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务id',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `task_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '任务类型',
  `mq_exchange` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '交换机名称',
  `mq_routingkey` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'routingkey',
  `request_body` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '任务请求的内容',
  `version` int(10) DEFAULT '0' COMMENT '乐观锁版本号',
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '任务状态',
  `errormsg` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of xc_task_his
-- ----------------------------
INSERT INTO `xc_task_his` VALUES ('10', '2018-04-04 22:58:20', '2018-04-04 22:58:54', '2018-04-04 22:58:55', 'add_choosecourse', 'ex_learning_addchoosecourse', 'addchoosecourse', '{\"userId\":\"49\",\"courseId\":\"4028e581617f945f01617f9dabc40000\"}', null, null, null);

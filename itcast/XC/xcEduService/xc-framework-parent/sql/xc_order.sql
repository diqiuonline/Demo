/*
Navicat MySQL Data Transfer

Source Server         : xcMySQL
Source Server Version : 80017
Source Host           : 192.168.2.101:3306
Source Database       : xc_order

Target Server Type    : MYSQL
Target Server Version : 80017
File Encoding         : 65001

Date: 2019-09-04 21:56:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for xc_orders
-- ----------------------------
DROP TABLE IF EXISTS `xc_orders`;
CREATE TABLE `xc_orders` (
  `order_number` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `initial_price` float(8,2) DEFAULT NULL COMMENT '定价',
  `price` float(8,2) DEFAULT NULL COMMENT '交易价',
  `start_time` datetime NOT NULL COMMENT '起始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '交易状态',
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户id',
  `details` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '订单明细',
  PRIMARY KEY (`order_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of xc_orders
-- ----------------------------
INSERT INTO `xc_orders` VALUES ('299036931059486720', '0.01', '0.01', '2018-04-05 12:26:00', '2018-04-05 14:26:00', '401001', '49', '[{\"itemId\":\"4028e581617f945f01617f9dabc40000\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"299036931059486720\"}]');
INSERT INTO `xc_orders` VALUES ('299118286820741120', '0.01', '0.01', '2018-04-05 17:49:17', '2018-04-05 19:49:17', '401001', '49', '[{\"itemId\":\"4028e58161bd22e60161bd23672a0001\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"299118286820741120\"}]');
INSERT INTO `xc_orders` VALUES ('299118455888941056', '0.01', '0.01', '2018-04-05 17:49:57', '2018-04-05 19:49:57', '401001', '49', '[{\"itemId\":\"4028e58161bd22e60161bd23672a0001\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"299118455888941056\"}]');
INSERT INTO `xc_orders` VALUES ('299144273360982016', '0.01', '0.01', '2018-04-05 19:32:33', '2018-04-05 21:32:33', '401002', '49', '[{\"itemId\":\"4028e58161bcf7f40161bcf8b77c0000\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"299144273360982016\"}]');
INSERT INTO `xc_orders` VALUES ('299202627802370048', '0.01', '0.01', '2018-04-05 23:24:26', '2018-04-06 01:24:26', '401001', '49', '[{\"itemId\":\"4028e58161bd22e60161bd23672a0001\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"299202627802370048\"}]');
INSERT INTO `xc_orders` VALUES ('299226261577142272', '0.01', '0.01', '2018-04-06 00:58:50', '2018-04-06 02:58:50', '401001', '49', '[{\"itemId\":\"4028e58161bd3b380161bd3bcd2f0000\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"299226261577142272\"}]');
INSERT INTO `xc_orders` VALUES ('299226499146715136', '0.01', '0.01', '2018-04-06 00:59:17', '2018-04-06 02:59:17', '401001', '49', '[{\"itemId\":\"4028e58161bd3b380161bd3bcd2f0000\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"299226499146715136\"}]');
INSERT INTO `xc_orders` VALUES ('317320500991102976', '0.01', '0.01', '2018-05-25 23:18:23', '2018-05-26 01:18:23', '401001', '49', '[{\"itemId\":\"4028e58161bd22e60161bd23672a0001\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"317320500991102976\"}]');
INSERT INTO `xc_orders` VALUES ('317320549372399616', '0.01', '0.01', '2018-05-25 23:18:35', '2018-05-26 01:18:35', '401001', '49', '[{\"itemId\":\"4028e58161bd22e60161bd23672a0001\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"317320549372399616\"}]');
INSERT INTO `xc_orders` VALUES ('317326221119983616', '0.01', '0.01', '2018-05-25 23:41:07', '2018-05-26 01:41:07', '401001', '49', '[{\"itemId\":\"402885816243d2dd016243f24c030002\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"317326221119983616\"}]');
INSERT INTO `xc_orders` VALUES ('317532756458737664', '0.01', '0.01', '2018-05-26 13:21:49', '2018-05-26 15:21:49', '401001', '49', '[{\"itemId\":\"4028e58161bd22e60161bd23672a0001\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"317532756458737664\"}]');
INSERT INTO `xc_orders` VALUES ('317532808245809152', '0.01', '0.01', '2018-05-26 13:22:01', '2018-05-26 15:22:01', '401001', '49', '[{\"itemId\":\"4028e58161bd22e60161bd23672a0001\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"317532808245809152\"}]');
INSERT INTO `xc_orders` VALUES ('317532890600968192', '0.01', '0.01', '2018-05-26 13:22:21', '2018-05-26 15:22:21', '401001', '49', '[{\"itemId\":\"4028e58161bd22e60161bd23672a0001\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"317532890600968192\"}]');
INSERT INTO `xc_orders` VALUES ('317587489890373632', '99.00', '99.00', '2018-05-26 16:59:18', '2018-05-26 18:59:18', '401001', '49', '[{\"itemId\":\"402885816243d2dd016243f24c030002\",\"itemNum\":1,\"itemPrice\":99.0,\"orderNumber\":\"317587489890373632\"}]');
INSERT INTO `xc_orders` VALUES ('317590240250695680', '99.00', '99.00', '2018-05-26 17:10:14', '2018-05-26 19:10:14', '401001', '49', '[{\"itemId\":\"402885816243d2dd016243f24c030002\",\"itemNum\":1,\"itemPrice\":99.0,\"orderNumber\":\"317590240250695680\"}]');
INSERT INTO `xc_orders` VALUES ('317600970689613824', '99.00', '99.00', '2018-05-26 17:52:52', '2018-05-26 19:52:52', '401001', '49', '[{\"itemId\":\"402885816243d2dd016243f24c030002\",\"itemNum\":1,\"itemPrice\":99.0,\"orderNumber\":\"317600970689613824\"}]');
INSERT INTO `xc_orders` VALUES ('317651443140399104', '99.00', '99.00', '2018-05-26 21:13:26', '2018-05-26 23:13:26', '401001', '49', '[{\"itemId\":\"402885816243d2dd016243f24c030002\",\"itemNum\":1,\"itemPrice\":99.0,\"orderNumber\":\"317651443140399104\"}]');
INSERT INTO `xc_orders` VALUES ('317651457044516864', '1.01', '1.01', '2018-05-26 21:13:29', '2018-05-26 23:13:29', '401001', '49', '[{\"itemId\":\"402885816243d2dd016243f24c030002\",\"itemNum\":1,\"itemPrice\":99.0,\"orderNumber\":\"317651457044516864\"}]');
INSERT INTO `xc_orders` VALUES ('318344201396162560', '0.01', '0.01', '2018-05-28 19:06:12', '2018-05-28 21:06:12', '401002', '49', '[{\"itemId\":\"402885816243d2dd016243f24c030002\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"318344201396162560\"}]');
INSERT INTO `xc_orders` VALUES ('319855888196571136', '0.01', '0.01', '2018-06-01 23:13:07', '2018-06-02 01:13:07', '401002', '49', '[{\"endTime\":1585709437000,\"itemId\":\"4028e58161bd22e60161bd23672a0001\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"319855888196571136\",\"startTime\":1522551031000}]');
INSERT INTO `xc_orders` VALUES ('319867386222481408', '0.01', '0.01', '2018-06-01 23:58:48', '2018-06-02 01:58:48', '401001', '49', '[{\"endTime\":1585709437000,\"itemId\":\"4028e58161bd22e60161bd23672a0001\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"319867386222481408\",\"startTime\":1522551031000,\"valid\":\"204001\"}]');
INSERT INTO `xc_orders` VALUES ('319867403872112640', '0.01', '0.01', '2018-06-01 23:58:52', '2018-06-02 01:58:52', '401002', '49', '[{\"endTime\":1585709437000,\"itemId\":\"4028e58161bd22e60161bd23672a0001\",\"itemNum\":1,\"itemPrice\":0.01,\"orderNumber\":\"319867403872112640\",\"startTime\":1522551031000,\"valid\":\"204001\"}]');

-- ----------------------------
-- Table structure for xc_orders_detail
-- ----------------------------
DROP TABLE IF EXISTS `xc_orders_detail`;
CREATE TABLE `xc_orders_detail` (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `order_number` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `item_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品id',
  `item_num` int(8) NOT NULL COMMENT '商品数量',
  `item_price` float(8,2) NOT NULL COMMENT '金额',
  `valid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程有效性',
  `start_time` datetime NOT NULL COMMENT '课程开始时间',
  `end_time` datetime NOT NULL COMMENT '课程结束时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `xc_orders_detail_unique` (`order_number`,`item_id`) USING BTREE,
  CONSTRAINT `xc_orders_detail_ibfk_1` FOREIGN KEY (`order_number`) REFERENCES `xc_orders` (`order_number`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of xc_orders_detail
-- ----------------------------

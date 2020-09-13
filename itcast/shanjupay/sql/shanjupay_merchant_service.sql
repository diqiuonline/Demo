/*
 Navicat Premium Data Transfer

 Source Server         : 110Mysql
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : 192.168.2.110:3306
 Source Schema         : shanjupay_merchant_service

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 13/09/2020 20:28:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app`  (
  `ID` bigint(0) NOT NULL,
  `APP_ID` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `APP_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商店名称',
  `MERCHANT_ID` bigint(0) NULL DEFAULT NULL COMMENT '所属商户',
  `PUBLIC_KEY` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用公钥(RSAWithSHA256)',
  `NOTIFY_URL` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权回调地址',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `APP_ID`(`APP_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of app
-- ----------------------------
INSERT INTO `app` VALUES (1279823565268086785, '18ecba1d-f32e-4c07-9bf0-847cc468a7ca', 'xx电商网站', 1272782812025139201, 'string', 'string');
INSERT INTO `app` VALUES (1280160673589493761, '2b0d1fa2-633e-4790-a9ba-3e1e81b64eb3', 'xx电商网站2', 1272782812025139201, 'string', 'string');
INSERT INTO `app` VALUES (1284423411790725122, '00c63954-289f-4691-b645-9b1329037325', 'test110', 1284422494894899201, '110', '');
INSERT INTO `app` VALUES (1305046178433445889, '28e19c9e-99d2-4dc3-bbb6-78b62c65dbd3', '1', 1284422494894899201, '', '');
INSERT INTO `app` VALUES (1305050189530247169, 'c7be1ad8-ba3d-4a77-9852-6b8766765da2', 'test199', 1305049061220528129, 'test199', '');

-- ----------------------------
-- Table structure for merchant
-- ----------------------------
DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant`  (
  `ID` bigint(0) NOT NULL COMMENT '主键',
  `MERCHANT_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户名称',
  `MERCHANT_NO` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业编号',
  `MERCHANT_ADDRESS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业地址',
  `MERCHANT_TYPE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户类型',
  `BUSINESS_LICENSES_IMG` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '营业执照（企业证明）',
  `ID_CARD_FRONT_IMG` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '法人身份证正面照片',
  `ID_CARD_AFTER_IMG` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '法人身份证反面照片',
  `USERNAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人姓名',
  `MOBILE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人手机号(关联统一账号)',
  `CONTACTS_ADDRESS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人地址',
  `AUDIT_STATUS` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核状态 0-未申请,1-已申请待审核,2-审核通过,3-审核拒绝',
  `TENANT_ID` bigint(0) NULL DEFAULT NULL COMMENT '租户ID,关联统一用户',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of merchant
-- ----------------------------
INSERT INTO `merchant` VALUES (1272782812025139201, '学生餐厅', '32321321312', '郑州梧桐创业大厦', '餐饮', '6272d44a-19e4-44a7-a714-58ffc7da8e45e.png', '6272d44a-19e4-44a7-a714-58ffc7da8e45e.png', '6272d44a-19e4-44a7-a714-58ffc7da8e45e.png', '张先生', '18681955953', '郑州梧桐街', '2', NULL);
INSERT INTO `merchant` VALUES (1282730846938910722, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'test01', '18681955954', NULL, '0', 2);
INSERT INTO `merchant` VALUES (1282731972694298625, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'test02', '18681955955', NULL, '0', 4);
INSERT INTO `merchant` VALUES (1282734253372264449, 'test04企业名称', '235345234', '河南郑州', '教育', 'http://qdnrq0li1.bkt.clouddn.com/8e0478be-2a1a-49cf-9b8c-baecd0f18953g.jpg', 'http://qdnrq0li1.bkt.clouddn.com/d1697f17-f857-4750-94f1-bfbcf78dd43bw.jpg', 'http://qdnrq0li1.bkt.clouddn.com/06e65dc3-614e-43b0-8ffa-776592a158a8d.jpg', 'test04', '18681955956', '郑州高新区', '2', 6);
INSERT INTO `merchant` VALUES (1284414665614114817, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'test04', '18681955957', NULL, '0', 7);
INSERT INTO `merchant` VALUES (1284422494894899201, '110企业名称', '32534265436', '陕西西安', '科学研究', 'http://qdnrq0li1.bkt.clouddn.com/f6093cf3-2d28-4ac2-9927-c758795f556ff.jpg', 'http://qdnrq0li1.bkt.clouddn.com/68ca90f7-ed06-4be2-9773-bd5db8603f2a).jpg', 'http://qdnrq0li1.bkt.clouddn.com/42ae3de4-1095-48e3-a792-ee6767814128d.jpg', 'test110', '18681955110', '西安高兴去', '2', 8);
INSERT INTO `merchant` VALUES (1305049061220528129, 'test199', 'test199', 'test199', '教育', '', '', '', 'test199', '18683753245', NULL, '2', 9);

-- ----------------------------
-- Table structure for staff
-- ----------------------------
DROP TABLE IF EXISTS `staff`;
CREATE TABLE `staff`  (
  `ID` bigint(0) NOT NULL COMMENT '主键',
  `MERCHANT_ID` bigint(0) NULL DEFAULT NULL COMMENT '商户ID',
  `FULL_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `POSITION` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职位',
  `USERNAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名(关联统一用户)',
  `MOBILE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号(关联统一用户)',
  `STORE_ID` bigint(0) NULL DEFAULT NULL COMMENT '员工所属门店',
  `LAST_LOGIN_TIME` datetime(0) NULL DEFAULT NULL COMMENT '最后一次登录时间',
  `STAFF_STATUS` bit(1) NULL DEFAULT NULL COMMENT '0表示禁用，1表示启用',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of staff
-- ----------------------------
INSERT INTO `staff` VALUES (1282730847295426561, 1282730846938910722, NULL, NULL, 'test01', '18681955954', 1282730847014408193, NULL, NULL);
INSERT INTO `staff` VALUES (1282731972874653698, 1282731972694298625, NULL, NULL, 'test02', '18681955955', 1282731972753018882, NULL, NULL);
INSERT INTO `staff` VALUES (1282734253460344833, 1282734253372264449, NULL, NULL, 'test03', '18681955956', 1282734253389041665, NULL, NULL);
INSERT INTO `staff` VALUES (1284414666163568641, 1284414665614114817, NULL, NULL, 'test04', '18681955957', 1284414665836412930, NULL, b'1');
INSERT INTO `staff` VALUES (1284422494987173890, 1284422494894899201, NULL, NULL, 'test110', '18681955110', 1284422494915870722, NULL, b'1');
INSERT INTO `staff` VALUES (1305049061593821186, 1305049061220528129, NULL, NULL, 'test199', '18683753245', 1305049061262471169, NULL, b'1');

-- ----------------------------
-- Table structure for store
-- ----------------------------
DROP TABLE IF EXISTS `store`;
CREATE TABLE `store`  (
  `ID` bigint(0) NOT NULL,
  `STORE_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '门店名称',
  `STORE_NUMBER` bigint(0) NULL DEFAULT NULL COMMENT '门店编号',
  `MERCHANT_ID` bigint(0) NULL DEFAULT NULL COMMENT '所属商户',
  `PARENT_ID` bigint(0) NULL DEFAULT NULL COMMENT '父门店',
  `STORE_STATUS` bit(1) NULL DEFAULT NULL COMMENT '0表示禁用，1表示启用',
  `STORE_ADDRESS` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '门店地址',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of store
-- ----------------------------
INSERT INTO `store` VALUES (1282730847014408193, '根门店 ', NULL, 1282730846938910722, NULL, b'1', NULL);
INSERT INTO `store` VALUES (1282731972753018882, '根门店 ', NULL, 1282731972694298625, NULL, b'1', NULL);
INSERT INTO `store` VALUES (1282734253389041665, '根门店 ', NULL, 1282734253372264449, NULL, b'1', NULL);
INSERT INTO `store` VALUES (1284414665836412930, '根门店 ', NULL, 1284414665614114817, NULL, b'1', NULL);
INSERT INTO `store` VALUES (1284422494915870722, '根门店 ', NULL, 1284422494894899201, NULL, b'1', NULL);
INSERT INTO `store` VALUES (1305049061262471169, '根门店 ', NULL, 1305049061220528129, NULL, b'1', NULL);

-- ----------------------------
-- Table structure for store_staff
-- ----------------------------
DROP TABLE IF EXISTS `store_staff`;
CREATE TABLE `store_staff`  (
  `ID` bigint(0) NOT NULL,
  `STORE_ID` bigint(0) NULL DEFAULT NULL COMMENT '门店标识',
  `STAFF_ID` bigint(0) NULL DEFAULT NULL COMMENT '员工标识',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of store_staff
-- ----------------------------
INSERT INTO `store_staff` VALUES (1282730847316398082, 1282730847014408193, 1282730847295426561);
INSERT INTO `store_staff` VALUES (1282731972895625218, 1282731972753018882, 1282731972874653698);
INSERT INTO `store_staff` VALUES (1282734253468733442, 1282734253389041665, 1282734253460344833);
INSERT INTO `store_staff` VALUES (1284414666180345858, 1284414665836412930, 1284414666163568641);
INSERT INTO `store_staff` VALUES (1284422495012339713, 1284422494915870722, 1284422494987173890);
INSERT INTO `store_staff` VALUES (1305049061627375618, 1305049061262471169, 1305049061593821186);

SET FOREIGN_KEY_CHECKS = 1;

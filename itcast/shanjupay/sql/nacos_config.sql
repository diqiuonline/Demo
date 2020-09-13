/*
 Navicat Premium Data Transfer

 Source Server         : 110Mysql
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : 192.168.2.110:3306
 Source Schema         : nacos_config

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 13/09/2020 20:28:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (2, 'spring-boot-http.yaml', 'COMMON_GROUP', '#HTTP格式配置\r\nspring:\r\n  http:\r\n    encoding:\r\n      charset: UTF-8\r\n      force: true\r\n      enabled: true\r\n  messages:\r\n    encoding: UTF-8\r\n#tomcat头信息(用户ip和访问协议)及访问路径配置\r\nserver:\r\n  tomcat:\r\n    remote_ip_header: x-forwarded-for\r\n    protocol_header: x-forwarded-proto\r\n  servlet:\r\n    context-path: /\r\n  use-forward-headers: true\r\n#服务监控与管理配置，运维相关\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: refresh,health,info,env', '59190adf613d59d2e935e0e726e09e38', '2020-06-15 00:42:12', '2020-06-15 03:06:26', NULL, '192.168.2.3', '', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (5, 'merchant-application.yaml', 'SHANJUPAY_GROUP', '#覆盖访问路径\r\nserver:\r\n  servlet:\r\n    context-path: /merchant\r\n#启用Swagger\r\nswagger:\r\n  enable: true\r\nsms:\r\n  url: \"http://localhost:56085/sailing\"\r\n  effectiveTime: 6000\r\n\r\noss:\r\n  qiniu:\r\n    url: \"http://qdnrq0li1.bkt.clouddn.com/\"\r\n    accessKey: \"42FjT8A6B_nU_uN4Pxg1EB7IgB3aT9zTeIMQz7Pc\"\r\n    secretKey: \"kGlpVQ1SfCl0jFNGiuSI-DdQTX4ywnrky-u9RAMb\"\r\n    bucket: \"diqiuonline\"\r\nshanjupay:\r\n  c2b:\r\n    subject: \"%s商品\"\r\n    body: \"向%s付款\"', 'aedd97a81c4b5de8cd4ca24103765e21', '2020-06-15 00:44:43', '2020-08-22 06:38:50', NULL, '192.168.2.3', '', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (6, 'merchant-service.yaml', 'SHANJUPAY_GROUP', '# 覆盖spring‐boot‐http.yaml的项目\r\nserver:\r\n  servlet:\r\n    context-path: /merchant-service\r\n# 覆盖spring‐boot‐starter‐druid.yaml的项目\r\nspring:\r\n  datasource:\r\n    druid:\r\n      url: jdbc:mysql://192.168.2.110:3306/shanjupay_merchant_service?serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true&characterEncoding=utf-8\r\n      username: root\r\n      password: 123456\r\n# 覆盖spring‐boot‐mybatis‐plus.yaml的项目\r\nmybatis-plus:\r\n  typeAliasesPackage: com.dhcc.shanjupay.merchant.entity\r\n  mapper-locations: classpath:com/dhcc/shanjupay/*/mapper/*.xml', 'c21750ef6011c0728153894c5e427f57', '2020-06-15 00:46:14', '2020-06-15 03:06:55', NULL, '192.168.2.3', '', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (7, 'spring-boot-starter-druid.yaml', 'COMMON_GROUP', 'spring:\r\n  datasource:\r\n    type: com.alibaba.druid.pool.DruidDataSource\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    url: jdbc:mysql://localhost:3306/oauth_useUnicode=true\r\n    username: root\r\n    password: yourpassword\r\n    druid:\r\n      initial-size: 5\r\n      min-idle: 5\r\n      max-active: 20\r\n      max-wait: 60000\r\n      time-between-eviction-runs-millis: 60000\r\n      min-evictable-idle-time-millis: 300000\r\n      validation-query: SELECT 1 FROM DUAL\r\n      test-while-idle: true\r\n      test-on-borrow: true\r\n      test-on-return: false\r\n      pool-prepared-statements: true\r\n      max-pool-prepared-statement-per-connection-size: 20\r\n      filter:\r\n        stat:\r\n          slow-sql-millis: 1\r\n          log-slow-sql: true\r\n      filters: config,stat,wall,log4j2\r\n      web-stat-filter:\r\n        enabled: true\r\n        url-pattern: /*\r\n        exclusions: \"*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\"\r\n        session-stat-enable: false\r\n        session-stat-max-count: 1000\r\n        principal-cookie-name: admin\r\n        principal-session-name: admin\r\n        profile-enable: true\r\n      stat-view-servlet:\r\n        enabled: true\r\n        url-pattern: /druid/*\r\n        allow: 127.0.0.1,192.168.163.1\r\n        deny: 192.168.1.73\r\n        reset-enable: false\r\n        login-password: admin\r\n        login-username: admin\r\n      aop-patterns: com.shanjupay.*.service.*', '83328ce3c68087b837ea3d6a421cb004', '2020-06-15 00:49:33', '2020-06-15 03:08:45', NULL, '192.168.2.3', '', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (9, 'spring-boot-mybatis-plus.yaml', 'COMMON_GROUP', 'mybatis-plus:\r\n  configuration:\r\n    cache-enabled: false\r\n    map-underscore-to-camel-case: true\r\n  global-config:\r\n    id-type: 0\r\n    field-strategy: 0\r\n    db-column-underline: true\r\n    refresh-mapper: true\r\n  typeAliasesPackage: com.dhcc.shanjupay.user.entity\r\n  mapper-locations: classpath:com/dhcc/shanjupay/*/mapper/*.xml', '343babd246351d22f86fc8304ff03e7a', '2020-06-15 01:04:04', '2020-06-15 03:09:10', NULL, '192.168.2.3', '', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (25, 'transaction-service.yaml', 'SHANJUPAY_GROUP', '# 覆盖spring-boot-http.yaml的项目\r\nserver: \r\n  servlet:\r\n    context-path: /transaction\r\n# 覆盖spring-boot-starter-druid.yaml的项目\r\nspring: \r\n  datasource: \r\n    druid:\r\n      url: jdbc:mysql://192.168.2.110:3306/shanjupay_transaction?serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true&characterEncoding=utf-8\r\n      username: root\r\n      password: 123456\r\n# 覆盖spring-boot-mybatis-plus.yaml的项目\r\nmybatis-plus:\r\n  typeAliasesPackage: com.dhcc.shanjupay.transaction.entity \r\n  mapper-locations: classpath:com/dhcc/shanjupay/*/mapper/*.xml\r\n#支付入口\r\nshanjupay:\r\n  payurl: \"http://5m2793.natappfree.cc/transaction/pay-entry/\"\r\nweixin:\r\n  oauth2RequestUrl: \"https://open.weixin.qq.com/connect/oauth2/authorize\"\r\n  oauth2CodeReturnUrl: \"http://hqkay2.natappfree.cc/transaction/wx-oauth-code-return\"\r\n  oauth2Token: \"https://api.weixin.qq.com/sns/oauth2/access_token\"', '5f6c87126505f9b1fc8f4d79ab6522ce', '2020-07-09 03:14:57', '2020-09-13 13:35:20', NULL, '192.168.2.3', '', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (26, 'spring-boot-redis.yaml', 'COMMON_GROUP', 'spring:\r\n  redis:\r\n    # Redis数据库索引（默认为0 ）\r\n    database: 0\r\n    host: 192.168.2.110\r\n    port: 6379\r\n    #连接超时时间（毫秒）\r\n    timeout: 1000ms\r\n    #password: 123456\r\n    lettuce:\r\n      pool:\r\n        #连接池中的最大空闲连接\r\n        max-idle: 8\r\n        #连接池中的最小空闲连接\r\n        min-idle: 0\r\n        #连接池最大连接数（使用负值表示没有限制） \r\n        max-active: 8\r\n        #连接池最大阻塞等待时间（使用负值表示没有限制） \r\n        max-wait: 1000ms\r\n      shutdown-timeout: 1000ms', '91196a1543f9655cbf53c11e15305c9b', '2020-07-12 08:07:21', '2020-07-12 09:15:58', NULL, '192.168.2.3', '', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (29, 'gateway-service.yaml', 'SHANJUPAY_GROUP', '#路由规则\r\nzuul:\r\n  retryable: true\r\n  add-host-header: true\r\n  ignoredServices: \"*\"\r\n  sensitiveHeaders: \"*\"\r\n  routes:\r\n    operation-application:\r\n      path: /operation/**\r\n      stripPrefix: false\r\n    merchant-application:\r\n      path: /merchant/**\r\n      stripPrefix: false\r\n    uaa-service: \r\n      path: /uaa/**\r\n      stripPrefix: false\r\n    transaction-service: \r\n      path: /transaction/**\r\n      stripPrefix: false\r\nfeign:\r\n  hystrix:\r\n    enabled: true\r\n  compression:\r\n    request:\r\n      enabled: true # 配置请求GZIP压缩\r\n      mime-types: [\"text/xml\",\"application/xml\",\"application/json\"] # 配置压缩支持的MIME TYPE\r\n      min-request-size: 2048 # 配置压缩数据大小的下限\r\n    response:\r\n      enabled: true # 配置响应GZIP压缩\r\n\r\nhystrix:\r\n  command:\r\n    default:\r\n      execution:\r\n        isolation:\r\n          thread:\r\n            timeoutInMilliseconds: 93000  # 设置熔断超时时间  default 1000\r\n        timeout:\r\n          enabled: true # 打开超时熔断功能 default true\r\n\r\nribbon:\r\n  nacos:\r\n    enabled: true # 不知道是否生效\r\n  ConnectTimeout: 3000 # 设置连接超时时间 default 2000\r\n  ReadTimeout: 20000    # 设置读取超时时间  default 5000\r\n  OkToRetryOnAllOperations: false # 对所有操作请求都进行重试  default false\r\n  MaxAutoRetriesNextServer: 1    # 切换实例的重试次数  default 1\r\n  MaxAutoRetries: 1     # 对当前实例的重试次数 default 0', 'acbb9abf3f922217e4524956f14bbe69', '2020-07-14 05:59:50', '2020-08-22 23:31:26', NULL, '192.168.2.3', '', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (30, 'uaa-service.yaml', 'SHANJUPAY_GROUP', '#	覆盖spring-boot-http,yaml的项目 \r\nserver:\r\n  servlet:\r\n    context-path: /uaa\r\n#	覆盖 spring-boot-starter-druid.yaml 的项目\r\nspring:\r\n  datasource:\r\n    druid:\r\n      url: jdbc:mysql://192.168.2.110:3306/shanjupay_uaa?serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true&characterEncoding=utf-8 \r\n      username: root\r\n      password: 123456', 'f39a377c64227efd21e6a88da3658a1c', '2020-07-14 06:06:59', '2020-07-14 06:09:27', NULL, '192.168.2.3', '', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (32, 'user-service.yaml', 'SHANJUPAY_GROUP', '#	覆盖 spring-boot-http.yaml 的项目 \r\nserver:\r\n  servlet:\r\n    context-path: /user\r\n#	覆盖spring-boot-starter-druid .yaml的项目 spring:\r\nspring:\r\n  datasource:\r\n    druid:\r\n      url: jdbc:mysql://192.168.2.110:3306/shanjupay_user?serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true&characterEncoding=utf-8\r\n      username: root\r\n      password: 123456\r\n#	覆盖 spring-boot-mybatis-plus.yaml 的项目 \r\n# 覆盖spring‐boot‐mybatis‐plus.yaml的项目\r\nmybatis-plus:\r\n  typeAliasesPackage: com.dhcc.shanjupay.merchant.entity\r\n  mapper-locations: classpath:com/dhcc/shanjupay/*/mapper/*.xml\r\nsms :\r\n  url: \"http://localhost:56085/sailing\" \r\n  effectiveTime: 6000', 'a7eee9559ee5f694acb1a4f21c514ddf', '2020-07-14 06:10:46', '2020-07-14 06:10:46', NULL, '192.168.2.3', '', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (33, 'jwt.yaml', 'COMMON_GROUP', 'siging-key: shanju123', 'dbf1ac8e8c13511ae324487b3de124b8', '2020-07-14 06:21:40', '2020-07-14 06:21:40', NULL, '192.168.2.3', '', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (38, 'spring-boot-freemarker.yaml', 'COMMON_GROUP', 'spring:\r\n  freemarker:\r\n    charset: UTF-8\r\n    request-context-attribute: rc\r\n    content-type: text/html\r\n    suffix: .html\r\n    enabled: true\r\n  resources:\r\n    add-mappings: false\r\n  mvc:\r\n    throw-exception-if-no-handler-found: true', '3a1e090cffd1cd6b217236753c1c476c', '2020-08-22 23:15:52', '2020-08-23 06:55:50', NULL, '192.168.2.3', '', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (52, 'payment-agent-service.yaml', 'SHANJUPAY_GROUP', 'server:\r\n  servlet:\r\n    context-path: /payment-receiver', '4d57357e4c8100eded2ce9da7f85b0e0', '2020-08-23 20:04:38', '2020-08-23 20:04:38', NULL, '192.168.2.3', '', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (55, 'spring-boot-starter-rocketmq.yaml', 'COMMON_GROUP', 'rocketmq:\r\n  nameServer: 192.168.2.110:9876\r\n  producer:\r\n    group: PID_PAY_PRODUCER\r\ntestf: 234\r\n      ', '3d1f033988ee2b2cc13e3e9315507ab8', '2020-09-12 20:39:19', '2020-09-12 23:08:57', NULL, '192.168.2.3', '', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5', '', '', '', 'yaml', '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '增加租户字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_beta' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_tag' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint(0) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(0) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id`, `tag_name`, `tag_type`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_tag_relation' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint(0) UNSIGNED NOT NULL,
  `nid` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE,
  INDEX `idx_did`(`data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '多租户改造' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (25, 38, 'transaction-service.yaml', 'SHANJUPAY_GROUP', '', '# 覆盖spring-boot-http.yaml的项目\r\nserver: \r\n  servlet:\r\n    context-path: /transaction\r\n# 覆盖spring-boot-starter-druid.yaml的项目\r\nspring: \r\n  datasource: \r\n    druid:\r\n      url: jdbc:mysql://192.168.2.110:3306/shanjupay_transaction?serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true&characterEncoding=utf-8\r\n      username: root\r\n      password: 123456\r\n# 覆盖spring-boot-mybatis-plus.yaml的项目\r\nmybatis-plus:\r\n  typeAliasesPackage: com.dhcc.shanjupay.transaction.entity \r\n  mapper-locations: classpath:com/dhcc/shanjupay/*/mapper/*.xml', '6774a46fb6bb078a0fb49b140fb7b1c1', '2020-08-18 15:53:55', '2020-08-18 23:53:55', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (5, 39, 'merchant-application.yaml', 'SHANJUPAY_GROUP', '', '#覆盖访问路径\r\nserver:\r\n  servlet:\r\n    context-path: /merchant\r\n#启用Swagger\r\nswagger:\r\n  enable: true\r\nsms:\r\n  url: \"http://localhost:56085/sailing\"\r\n  effectiveTime: 6000\r\n\r\noss:\r\n  qiniu:\r\n    url: \"http://qdnrq0li1.bkt.clouddn.com/\"\r\n    accessKey: \"42FjT8A6B_nU_uN4Pxg1EB7IgB3aT9zTeIMQz7Pc\"\r\n    secretKey: \"kGlpVQ1SfCl0jFNGiuSI-DdQTX4ywnrky-u9RAMb\"\r\n    bucket: \"diqiuonline\"', '64e7c0c3a6523207640b818e7ece4a10', '2020-08-18 15:59:29', '2020-08-18 23:59:30', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (5, 40, 'merchant-application.yaml', 'SHANJUPAY_GROUP', '', '#覆盖访问路径\r\nserver:\r\n  servlet:\r\n    context-path: /merchant\r\n#启用Swagger\r\nswagger:\r\n  enable: true\r\nsms:\r\n  url: \"http://localhost:56085/sailing\"\r\n  effectiveTime: 6000\r\n\r\noss:\r\n  qiniu:\r\n    url: \"http://qdnrq0li1.bkt.clouddn.com/\"\r\n    accessKey: \"42FjT8A6B_nU_uN4Pxg1EB7IgB3aT9zTeIMQz7Pc\"\r\n    secretKey: \"kGlpVQ1SfCl0jFNGiuSI-DdQTX4ywnrky-u9RAMb\"\r\n    bucket: \"diqiuonline\"\r\nshanjupay:\r\n  c2b:\r\n    subiect: \"%s商品\"\r\n    body: \"向%s付款\"', '733989be88ad0aa4de3eebd56029be8b', '2020-08-21 22:38:50', '2020-08-22 06:38:50', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (0, 41, 'spring-boot-freemarker.yaml', 'COMMON_GROUP', '', '#freemarker基本配置\r\nspring:\r\n  freemarker:\r\n    charset: UTF-8 \r\n    request-context-attribute: rc \r\n    content-type: text/html \r\n    suffix: .html \r\n    enabled: true\r\n  resources:\r\n    add-mappings: false #关闭工程中默认的资源处理 \r\n  mvc:\r\n    throw-exception-if-no-handler-found : true #出现错误时直接抛出异常', 'f44a6887e2074c65afbde197e2b76c1b', '2020-08-22 15:15:52', '2020-08-22 23:15:52', NULL, '192.168.2.3', 'I', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (29, 42, 'gateway-service.yaml', 'SHANJUPAY_GROUP', '', '#路由规则\r\nzuul:\r\n  retryable: true\r\n  add-host-header: true\r\n  ignoredServices: \"*\"\r\n  sensitiveHeaders: \"*\"\r\n  routes:\r\n    operation-application:\r\n      path: /operation/**\r\n      stripPrefix: false\r\n    merchant-application:\r\n      path: /merchant/**\r\n      stripPrefix: false\r\n    uaa-service: \r\n      path: /uaa/**\r\n      stripPrefix: false\r\n\r\nfeign:\r\n  hystrix:\r\n    enabled: true\r\n  compression:\r\n    request:\r\n      enabled: true # 配置请求GZIP压缩\r\n      mime-types: [\"text/xml\",\"application/xml\",\"application/json\"] # 配置压缩支持的MIME TYPE\r\n      min-request-size: 2048 # 配置压缩数据大小的下限\r\n    response:\r\n      enabled: true # 配置响应GZIP压缩\r\n\r\nhystrix:\r\n  command:\r\n    default:\r\n      execution:\r\n        isolation:\r\n          thread:\r\n            timeoutInMilliseconds: 93000  # 设置熔断超时时间  default 1000\r\n        timeout:\r\n          enabled: true # 打开超时熔断功能 default true\r\n\r\nribbon:\r\n  nacos:\r\n    enabled: true # 不知道是否生效\r\n  ConnectTimeout: 3000 # 设置连接超时时间 default 2000\r\n  ReadTimeout: 20000    # 设置读取超时时间  default 5000\r\n  OkToRetryOnAllOperations: false # 对所有操作请求都进行重试  default false\r\n  MaxAutoRetriesNextServer: 1    # 切换实例的重试次数  default 1\r\n  MaxAutoRetries: 1     # 对当前实例的重试次数 default 0', '36becc60dcc0d752986a63525eb61fce', '2020-08-22 15:31:25', '2020-08-22 23:31:26', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (38, 43, 'spring-boot-freemarker.yaml', 'COMMON_GROUP', '', '#freemarker基本配置\r\nspring:\r\n  freemarker:\r\n    charset: UTF-8 \r\n    request-context-attribute: rc \r\n    content-type: text/html \r\n    suffix: .html \r\n    enabled: true\r\n  resources:\r\n    add-mappings: false #关闭工程中默认的资源处理 \r\n  mvc:\r\n    throw-exception-if-no-handler-found : true #出现错误时直接抛出异常', 'f44a6887e2074c65afbde197e2b76c1b', '2020-08-22 22:03:16', '2020-08-23 06:03:16', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (38, 44, 'spring-boot-freemarker.yaml', 'COMMON_GROUP', '', '#freemarker基本配置\r\nspring:\r\n  freemarker:\r\n    charset: UTF-8\r\n    request-context-attribute: rc\r\n    content-type: text/html\r\n    suffix: .html\r\n    enabled: true\r\n  resources:\r\n   #  关闭工程中默认的资源处理 \r\n    add-mappings: false\r\n  mvc:\r\n    #出现错误时直接抛出异常\r\n    throw-exception-if-no-handler-found: true', '7593691bf7f517fe4a224d8200f370f3', '2020-08-22 22:07:51', '2020-08-23 06:07:51', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (38, 45, 'spring-boot-freemarker.yaml', 'COMMON_GROUP', '', '#freemarker基本配置\r\nspring:\r\n  freemarker:\r\n    charset: UTF-8\r\n    request-context-attribute: rc\r\n    content-type: text/html\r\n    suffix: .html\r\n    enabled: true\r\n  resources:\r\n   #  关闭工程中默认的资源处理 \r\n    add-mappings: false\r\n  mvc:\r\n    #出现错误时直接抛出异常\r\n    throw-exception-if-no-handler-found: true', '7593691bf7f517fe4a224d8200f370f3', '2020-08-22 22:28:54', '2020-08-23 06:28:54', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (25, 46, 'transaction-service.yaml', 'SHANJUPAY_GROUP', '', '# 覆盖spring-boot-http.yaml的项目\r\nserver: \r\n  servlet:\r\n    context-path: /transaction\r\n# 覆盖spring-boot-starter-druid.yaml的项目\r\nspring: \r\n  datasource: \r\n    druid:\r\n      url: jdbc:mysql://192.168.2.110:3306/shanjupay_transaction?serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true&characterEncoding=utf-8\r\n      username: root\r\n      password: 123456\r\n# 覆盖spring-boot-mybatis-plus.yaml的项目\r\nmybatis-plus:\r\n  typeAliasesPackage: com.dhcc.shanjupay.transaction.entity \r\n  mapper-locations: classpath:com/dhcc/shanjupay/*/mapper/*.xml\r\n#支付入口\r\nshanjupay:\r\n  payurl: \"http://127.0.0.1:56010/transaction/pay-entry/\"', '10a3c8a650d691ab4c9667960cb12ff1', '2020-08-22 22:35:19', '2020-08-23 06:35:20', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (25, 47, 'transaction-service.yaml', 'SHANJUPAY_GROUP', '', '# 覆盖spring-boot-http.yaml的项目\r\nserver: \r\n  servlet:\r\n    context-path: /\r\n# 覆盖spring-boot-starter-druid.yaml的项目\r\nspring: \r\n  datasource: \r\n    druid:\r\n      url: jdbc:mysql://192.168.2.110:3306/shanjupay_transaction?serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true&characterEncoding=utf-8\r\n      username: root\r\n      password: 123456\r\n# 覆盖spring-boot-mybatis-plus.yaml的项目\r\nmybatis-plus:\r\n  typeAliasesPackage: com.dhcc.shanjupay.transaction.entity \r\n  mapper-locations: classpath:com/dhcc/shanjupay/*/mapper/*.xml\r\n#支付入口\r\nshanjupay:\r\n  payurl: \"http://127.0.0.1:56010/transaction/pay-entry/\"', '085a4f90bb8ce0b18bdbc653550a928b', '2020-08-22 22:35:38', '2020-08-23 06:35:39', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (25, 48, 'transaction-service.yaml', 'SHANJUPAY_GROUP', '', '# 覆盖spring-boot-http.yaml的项目\r\nserver: \r\n  servlet:\r\n    context-path: /transaction\r\n# 覆盖spring-boot-starter-druid.yaml的项目\r\nspring: \r\n  datasource: \r\n    druid:\r\n      url: jdbc:mysql://192.168.2.110:3306/shanjupay_transaction?serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true&characterEncoding=utf-8\r\n      username: root\r\n      password: 123456\r\n# 覆盖spring-boot-mybatis-plus.yaml的项目\r\nmybatis-plus:\r\n  typeAliasesPackage: com.dhcc.shanjupay.transaction.entity \r\n  mapper-locations: classpath:com/dhcc/shanjupay/*/mapper/*.xml\r\n#支付入口\r\nshanjupay:\r\n  payurl: \"http://127.0.0.1:56010/transaction/pay-entry/\"', '10a3c8a650d691ab4c9667960cb12ff1', '2020-08-22 22:37:18', '2020-08-23 06:37:19', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (25, 49, 'transaction-service.yaml', 'SHANJUPAY_GROUP', '', '# 覆盖spring-boot-http.yaml的项目\r\nserver: \r\n  servlet:\r\n    context-path: /\r\n# 覆盖spring-boot-starter-druid.yaml的项目\r\nspring: \r\n  datasource: \r\n    druid:\r\n      url: jdbc:mysql://192.168.2.110:3306/shanjupay_transaction?serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true&characterEncoding=utf-8\r\n      username: root\r\n      password: 123456\r\n# 覆盖spring-boot-mybatis-plus.yaml的项目\r\nmybatis-plus:\r\n  typeAliasesPackage: com.dhcc.shanjupay.transaction.entity \r\n  mapper-locations: classpath:com/dhcc/shanjupay/*/mapper/*.xml\r\n#支付入口\r\nshanjupay:\r\n  payurl: \"http://127.0.0.1:56010/transaction/pay-entry/\"', '085a4f90bb8ce0b18bdbc653550a928b', '2020-08-22 22:44:17', '2020-08-23 06:44:17', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (25, 50, 'transaction-service.yaml', 'SHANJUPAY_GROUP', '', '# 覆盖spring-boot-http.yaml的项目\r\nserver: \r\n  servlet:\r\n    context-path: /transaction\r\n# 覆盖spring-boot-starter-druid.yaml的项目\r\nspring: \r\n  datasource: \r\n    druid:\r\n      url: jdbc:mysql://192.168.2.110:3306/shanjupay_transaction?serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true&characterEncoding=utf-8\r\n      username: root\r\n      password: 123456\r\n# 覆盖spring-boot-mybatis-plus.yaml的项目\r\nmybatis-plus:\r\n  typeAliasesPackage: com.dhcc.shanjupay.transaction.entity \r\n  mapper-locations: classpath:com/dhcc/shanjupay/*/mapper/*.xml', '6774a46fb6bb078a0fb49b140fb7b1c1', '2020-08-22 22:44:41', '2020-08-23 06:44:41', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (25, 51, 'transaction-service.yaml', 'SHANJUPAY_GROUP', '', '# 覆盖spring-boot-http.yaml的项目\r\nserver: \r\n  servlet:\r\n    context-path: /transaction\r\n# 覆盖spring-boot-starter-druid.yaml的项目\r\nspring: \r\n  datasource: \r\n    druid:\r\n      url: jdbc:mysql://192.168.2.110:3306/shanjupay_transaction?serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true&characterEncoding=utf-8\r\n      username: root\r\n      password: 123456\r\n# 覆盖spring-boot-mybatis-plus.yaml的项目\r\nmybatis-plus:\r\n  typeAliasesPackage: com.dhcc.shanjupay.transaction.entity \r\n  mapper-locations: classpath:com/dhcc/shanjupay/*/mapper/*.xml', '6774a46fb6bb078a0fb49b140fb7b1c1', '2020-08-22 22:46:10', '2020-08-23 06:46:10', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (25, 52, 'transaction-service.yaml', 'SHANJUPAY_GROUP', '', '# 覆盖spring-boot-http.yaml的项目\r\nserver: \r\n  servlet:\r\n    context-path: /transaction\r\n# 覆盖spring-boot-starter-druid.yaml的项目\r\nspring: \r\n  datasource: \r\n    druid:\r\n      url: jdbc:mysql://192.168.2.110:3306/shanjupay_transaction?serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true&characterEncoding=utf-8\r\n      username: root\r\n      password: 123456\r\n# 覆盖spring-boot-mybatis-plus.yaml的项目\r\nmybatis-plus:\r\n  typeAliasesPackage: com.dhcc.shanjupay.transaction.entity \r\n  mapper-locations: classpath:com/dhcc/shanjupay/*/mapper/*.xml\r\n#支付入口\r\nshanjupay:\r\n  payurl: \"http://127.0.0.1:56010/transaction/pay-entry/\"', '10a3c8a650d691ab4c9667960cb12ff1', '2020-08-22 22:46:49', '2020-08-23 06:46:50', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (38, 53, 'spring-boot-freemarker.yaml', 'COMMON_GROUP', '', 'spring:\r\n  freemarker:\r\n    charset: UTF-8\r\n    request-context-attribute: rc\r\n    content-type: text/html\r\n    suffix: .html\r\n    enabled: true\r\n  resources:\r\n    add-mappings: false\r\n  mvc:\r\n    throw-exception-if-no-handler-found: true', '3a1e090cffd1cd6b217236753c1c476c', '2020-08-22 22:52:03', '2020-08-23 06:52:04', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (38, 54, 'spring-boot-freemarker.yaml', 'COMMON_GROUP', '', 'spring:\r\n  freemarker:\r\n    charset: UTF-8\r\n    request-context-attribute: rc\r\n    content-type: text/html\r\n    suffix: .html\r\n    enabled: true\r\n  resources:\r\n    add-mappings: true\r\n  mvc:\r\n    throw-exception-if-no-handler-found: true', 'f9acbeca10735bb88454b1ef7233ac80', '2020-08-22 22:55:50', '2020-08-23 06:55:50', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (0, 55, 'payment-agent-service.yaml', 'SHANJUPAY_GROUP', '', 'server:\r\n  servlet:\r\n    context-path: /payment-receiver', '4d57357e4c8100eded2ce9da7f85b0e0', '2020-08-23 12:04:38', '2020-08-23 20:04:38', NULL, '192.168.2.3', 'I', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (25, 56, 'transaction-service.yaml', 'SHANJUPAY_GROUP', '', '# 覆盖spring-boot-http.yaml的项目\r\nserver: \r\n  servlet:\r\n    context-path: /transaction\r\n# 覆盖spring-boot-starter-druid.yaml的项目\r\nspring: \r\n  datasource: \r\n    druid:\r\n      url: jdbc:mysql://192.168.2.110:3306/shanjupay_transaction?serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true&characterEncoding=utf-8\r\n      username: root\r\n      password: 123456\r\n# 覆盖spring-boot-mybatis-plus.yaml的项目\r\nmybatis-plus:\r\n  typeAliasesPackage: com.dhcc.shanjupay.transaction.entity \r\n  mapper-locations: classpath:com/dhcc/shanjupay/*/mapper/*.xml\r\n#支付入口\r\nshanjupay:\r\n  payurl: \"http://127.0.0.1:56010/transaction/pay-entry/\"', '10a3c8a650d691ab4c9667960cb12ff1', '2020-08-23 15:27:18', '2020-08-23 23:27:18', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (25, 57, 'transaction-service.yaml', 'SHANJUPAY_GROUP', '', '# 覆盖spring-boot-http.yaml的项目\r\nserver: \r\n  servlet:\r\n    context-path: /transaction\r\n# 覆盖spring-boot-starter-druid.yaml的项目\r\nspring: \r\n  datasource: \r\n    druid:\r\n      url: jdbc:mysql://192.168.2.110:3306/shanjupay_transaction?serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true&characterEncoding=utf-8\r\n      username: root\r\n      password: 123456\r\n# 覆盖spring-boot-mybatis-plus.yaml的项目\r\nmybatis-plus:\r\n  typeAliasesPackage: com.dhcc.shanjupay.transaction.entity \r\n  mapper-locations: classpath:com/dhcc/shanjupay/*/mapper/*.xml\r\n#支付入口\r\nshanjupay:\r\n  payurl: \"http://192.168.1.5:56010/transaction/pay-entry/\"', 'ed59a8aaa7375048f66e9a7a37ed5c4a', '2020-09-12 11:39:19', '2020-09-12 19:39:18', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (0, 58, 'spring-boot-starter-rocketmq.yaml', 'COMMON_GROUP', '', 'rocketmq:\r\n  baneServer: 192.168.2.110:9876\r\n  producer:\r\n    group: PID_PAY_PRODUCER', '37acc9afe79029d5a2b8fbaddc866b8a', '2020-09-12 12:39:18', '2020-09-12 20:39:19', NULL, '192.168.2.3', 'I', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (55, 59, 'spring-boot-starter-rocketmq.yaml', 'COMMON_GROUP', '', 'rocketmq:\r\n  baneServer: 192.168.2.110:9876\r\n  producer:\r\n    group: PID_PAY_PRODUCER', '37acc9afe79029d5a2b8fbaddc866b8a', '2020-09-12 14:46:34', '2020-09-12 22:46:35', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (55, 60, 'spring-boot-starter-rocketmq.yaml', 'COMMON_GROUP', '', 'rocketmq:\r\n  baneServer: 192.168.2.110:9876\r\n  producer:\r\n    group: PID_PAY_PRODUCER\r\ntestf: 234\r\n      ', '254716a3f259387201c7a22372777ec6', '2020-09-12 15:08:56', '2020-09-12 23:08:57', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (25, 61, 'transaction-service.yaml', 'SHANJUPAY_GROUP', '', '# 覆盖spring-boot-http.yaml的项目\r\nserver: \r\n  servlet:\r\n    context-path: /transaction\r\n# 覆盖spring-boot-starter-druid.yaml的项目\r\nspring: \r\n  datasource: \r\n    druid:\r\n      url: jdbc:mysql://192.168.2.110:3306/shanjupay_transaction?serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true&characterEncoding=utf-8\r\n      username: root\r\n      password: 123456\r\n# 覆盖spring-boot-mybatis-plus.yaml的项目\r\nmybatis-plus:\r\n  typeAliasesPackage: com.dhcc.shanjupay.transaction.entity \r\n  mapper-locations: classpath:com/dhcc/shanjupay/*/mapper/*.xml\r\n#支付入口\r\nshanjupay:\r\n  payurl: \"http://iecwwx.natappfree.cc/transaction/pay-entry/\"', 'b271e7c9d7689cbb8497632b0b1447bc', '2020-09-13 04:40:59', '2020-09-13 12:41:00', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (25, 62, 'transaction-service.yaml', 'SHANJUPAY_GROUP', '', '# 覆盖spring-boot-http.yaml的项目\r\nserver: \r\n  servlet:\r\n    context-path: /transaction\r\n# 覆盖spring-boot-starter-druid.yaml的项目\r\nspring: \r\n  datasource: \r\n    druid:\r\n      url: jdbc:mysql://192.168.2.110:3306/shanjupay_transaction?serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true&characterEncoding=utf-8\r\n      username: root\r\n      password: 123456\r\n# 覆盖spring-boot-mybatis-plus.yaml的项目\r\nmybatis-plus:\r\n  typeAliasesPackage: com.dhcc.shanjupay.transaction.entity \r\n  mapper-locations: classpath:com/dhcc/shanjupay/*/mapper/*.xml\r\n#支付入口\r\nshanjupay:\r\n  payurl: \"http://5m2793.natappfree.cc/transaction/pay-entry/\"', '088d836db29d1a96c3994c2cb06361a4', '2020-09-13 04:59:16', '2020-09-13 12:59:16', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');
INSERT INTO `his_config_info` VALUES (25, 63, 'transaction-service.yaml', 'SHANJUPAY_GROUP', '', '# 覆盖spring-boot-http.yaml的项目\r\nserver: \r\n  servlet:\r\n    context-path: /transaction\r\n# 覆盖spring-boot-starter-druid.yaml的项目\r\nspring: \r\n  datasource: \r\n    druid:\r\n      url: jdbc:mysql://192.168.2.110:3306/shanjupay_transaction?serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true&characterEncoding=utf-8\r\n      username: root\r\n      password: 123456\r\n# 覆盖spring-boot-mybatis-plus.yaml的项目\r\nmybatis-plus:\r\n  typeAliasesPackage: com.dhcc.shanjupay.transaction.entity \r\n  mapper-locations: classpath:com/dhcc/shanjupay/*/mapper/*.xml\r\n#支付入口\r\nshanjupay:\r\n  payurl: \"http://5m2793.natappfree.cc/transaction/pay-entry/\"\r\nweixin:\r\n  oauth2RequestUrl: \"https://open.weixin.qq.com/connect/oauth2/authorize\"\r\n  oauth2CodeReturnUrl: \"http://hqkay2.natappfree.cc/transaction/wx-oauth-code-return\"\r\n  oauth2Token: \"https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code\"', '4a368b6bb4dcaaaffe89745c081f7eb6', '2020-09-13 05:35:19', '2020-09-13 13:35:20', NULL, '192.168.2.3', 'U', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `resource` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  UNIQUE INDEX `idx_user_role`(`username`, `role`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '租户容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp`, `tenant_id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'tenant_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
INSERT INTO `tenant_info` VALUES (1, '1', '0dfeeb8b-0c9e-4c7a-bcdf-c3e6656a22a5', 'dev', '开发环境', 'nacos', 1592152609621, 1592152609621);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;

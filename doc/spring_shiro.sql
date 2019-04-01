/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost:3306
 Source Schema         : spring_shiro

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 01/04/2019 23:02:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_permission_init
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission_init`;
CREATE TABLE `sys_permission_init`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接地址',
  `permission_init` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '需要具备的权限',
  `sort` int(50) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission_init
-- ----------------------------
INSERT INTO `sys_permission_init` VALUES ('1', '/static/**', 'anon', 1);
INSERT INTO `sys_permission_init` VALUES ('2', '/ajaxLogin', 'anon', 2);
INSERT INTO `sys_permission_init` VALUES ('3', '/logout', 'logout', 3);
INSERT INTO `sys_permission_init` VALUES ('4', '/add', 'perms[权限添加]', 4);
INSERT INTO `sys_permission_init` VALUES ('5', '/**', 'authc', 5);

-- ----------------------------
-- Table structure for u_permission
-- ----------------------------
DROP TABLE IF EXISTS `u_permission`;
CREATE TABLE `u_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'url地址',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'url描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of u_permission
-- ----------------------------
INSERT INTO `u_permission` VALUES (21, 'user:adds', '添加');
INSERT INTO `u_permission` VALUES (22, 'user:update', '更新');
INSERT INTO `u_permission` VALUES (23, NULL, NULL);

-- ----------------------------
-- Table structure for u_role
-- ----------------------------
DROP TABLE IF EXISTS `u_role`;
CREATE TABLE `u_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for u_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `u_role_permission`;
CREATE TABLE `u_role_permission`  (
  `rid` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `pid` bigint(20) NULL DEFAULT NULL COMMENT '权限ID'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for u_user
-- ----------------------------
DROP TABLE IF EXISTS `u_user`;
CREATE TABLE `u_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `email` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱|登录帐号',
  `pswd` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `status` bigint(1) NULL DEFAULT 1 COMMENT '1:有效，0:禁止登录',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of u_user
-- ----------------------------
INSERT INTO `u_user` VALUES (15, 'soap', NULL, 'soap', '2019-03-31 19:46:54', '2019-04-01 23:00:47', 1);

-- ----------------------------
-- Table structure for u_user_role
-- ----------------------------
DROP TABLE IF EXISTS `u_user_role`;
CREATE TABLE `u_user_role`  (
  `uid` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `rid` bigint(20) NULL DEFAULT NULL COMMENT '角色ID'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

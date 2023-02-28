/*
 Navicat Premium Data Transfer

 Source Server         : default
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : localhost:3306
 Source Schema         : im-system

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 24/02/2023 14:33:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for im_user_detail
-- ----------------------------
DROP TABLE IF EXISTS `im_user_detail`;
CREATE TABLE `im_user_detail`  (
  `user_id` int(30) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户唯一标识符',
  `app_id` int(10) UNSIGNED NOT NULL COMMENT '上层应用唯一标识符',
  `user_nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户昵称',
  `user_gender` int(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户性别: 0 表示保密，1 表示男性，2表示女性',
  `user_age` int(5) UNSIGNED NULL DEFAULT 1 COMMENT '用户年龄',
  `user_birthday` bigint(30) UNSIGNED NULL DEFAULT NULL COMMENT '用户生日: 采用时间戳存储',
  `user_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户所在地',
  `user_self_signature` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '这个人很懒，什么都没有填写~' COMMENT '用户个性签名',
  `user_friendship_allow_type` tinyint(1) UNSIGNED NULL DEFAULT 1 COMMENT '用户添加好友的方式：0 表示允许任何添加、1表示需要验证后添加',
  `user_avatar_address` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像地址',
  `user_is_forbid_add` int(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户是否可以添加好友: 0表示可以添加，1表示不可以添加（管理员控制）',
  `user_is_forbid` int(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户是否被禁用：0 表示没有被禁用，1表示被禁用',
  `user_is_del` int(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户是否已经被删除: 0 表示没有删除、1表示已经删除',
  `user_type` int(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '用户类型: 0 表示系统管理员，1表示普通用户',
  `user_extra` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户资料扩展字段：采用 JSON 格式存储',
  PRIMARY KEY (`user_id`, `app_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

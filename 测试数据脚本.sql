/*
Navicat MySQL Data Transfer

Source Server         : 虚拟机40
Source Server Version : 80032
Source Host           : 192.168.0.40:3306
Source Database       : edu_system_xueden

Target Server Type    : MYSQL
Target Server Version : 80032
File Encoding         : 65001

Date: 2024-01-21 20:47:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime(6) NOT NULL,
  `remarks` mediumtext COLLATE utf8mb4_general_ci,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `isp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统角色信息表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '1', '2022-12-08 19:12:03.000000', '系统管理员', '1', '2022-12-08 19:11:59.000000', 'ROLE_ADMIN', '系统管理员', null, null, null, null, '');
INSERT INTO `sys_role` VALUES ('2', '1', '2022-12-08 19:12:15.000000', '普通用户', '1', '2022-12-08 19:12:30.000000', 'ROLE_USER', '普通用户', null, null, null, null, '');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime(6) NOT NULL,
  `remarks` mediumtext COLLATE utf8mb4_general_ci,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `realname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` int DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `user_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  `area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `isp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4dm5kxn3potpfgdigehw7pdyu` (`role_id`) USING BTREE,
  CONSTRAINT `sys_user_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统用户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', '1', '2022-12-02 12:55:05.000000', '我是一名管理员', '1', '2023-09-03 08:16:33.211500', 'E10ADC3949BA59ABBE56E057F20F883E', '系统管理员', '男', '1', 'admin', 'xueden2023@163.com', 'c66809ef-b6e6-43b9-a685-91915b57e3d6.jpg', '1', null, null, null, null, '');
INSERT INTO `sys_user` VALUES ('2', '1', '2022-12-05 13:06:48.040000', '韩立红颜知己，名义上的妾侍。落云宗女修，负责管理药园，韩立入落云宗修炼，归其管辖，被家族逼婚，一直推脱不肯完婚。后韩立结婴后，慕沛灵对外谎称已被韩立收为妾侍，韩立为突破瓶颈，假戏真唱收其为妾侍，改修颠凤培元功，后为突破元婴期未果身陨。', '1', '2022-12-06 03:36:51.824000', 'E10ADC3949BA59ABBE56E057F20F883E', '慕沛灵', '女', '1', 'mupeiling', 'mupeiling@qq.com', null, '2', null, null, null, null, '');
INSERT INTO `sys_user` VALUES ('3', '1', '2022-12-05 13:09:44.441000', '韩立红颜知己。又名雪玲，本是灵界银月妖狼玲珑仙子两魂之一。', '1', '2022-12-06 03:30:05.735000', 'E10ADC3949BA59ABBE56E057F20F883E', '银月', '女', '1', 'yinyue', 'yinyue@qq.com', null, '2', null, null, null, null, '');
INSERT INTO `sys_user` VALUES ('4', '1', '2022-12-05 13:10:42.589000', '这是一个凡人修仙者', '1', '2022-12-05 13:10:42.589000', 'E10ADC3949BA59ABBE56E057F20F883E', '韩立', '男', '1', 'hanli', '22@qq.com', null, '2', null, null, null, null, '');
INSERT INTO `sys_user` VALUES ('5', '1', '2022-12-05 13:14:47.104000', '韩立红颜知己。真名汪凝，原乱星海妙音门门主之女，韩立平生所遇第一美女、红颜知己。', '1', '2022-12-06 03:37:28.681000', 'E10ADC3949BA59ABBE56E057F20F883E', '紫灵', '女', '1', 'zilin', '33@qq.com', null, '2', null, null, null, null, '');
INSERT INTO `sys_user` VALUES ('6', '1', '2022-12-05 13:36:24.775000', '这是一个备注', '1', '2022-12-05 13:36:24.776000', 'E10ADC3949BA59ABBE56E057F20F883E', '厉飞雨', '男', '1', 'lifeiyu', '22@qq.com', null, '2', null, null, null, null, '');
INSERT INTO `sys_user` VALUES ('7', '1', '2022-12-05 13:38:54.366000', '韩立红颜知己。魁星岛附近的修仙小门派女修，对师姐妍丽重情重义，为已成鬼魂的师姐不死重生付出极大代价，与师姐修炼阴阳轮回诀被未知通道吸入灵界，后被姜老怪/青元子收为徒。韩立于其帮衬良多，对韩立情素暗生。', '1', '2022-12-05 13:38:54.366000', 'E10ADC3949BA59ABBE56E057F20F883E', '元瑶', '女', '1', 'yuanyao', '33@qq.com', null, '2', null, null, null, null, '');
INSERT INTO `sys_user` VALUES ('10', '1', '2022-12-06 01:13:56.953000', '韩立道侣。掩月宗女修，本命法宝朱雀环，修炼素女轮回功 。', '1', '2022-12-06 01:13:56.953000', 'E10ADC3949BA59ABBE56E057F20F883E', '南宫婉', '男', '1', 'nangongwan', '333@qq.com', null, '2', null, null, null, null, '');
INSERT INTO `sys_user` VALUES ('11', '1', '2022-12-06 03:46:19.410000', '韩立黄枫谷师姐。经过一些事与非后，爱上韩立，曾经向韩立告白，被一心求大道的韩立拒绝，为等韩立未婚不嫁，道消身死。后韩立在灵界碰到与巧倩长相极其相似之人，讶然不已。', '1', '2022-12-06 03:46:19.410000', 'E10ADC3949BA59ABBE56E057F20F883E', '陈巧倩', '女', '1', 'chenqiaoqian', 'chenqiaoqian@qq.com', null, '2', null, null, null, null, '');
INSERT INTO `sys_user` VALUES ('12', '1', '2022-12-06 03:47:02.119000', '韩立旧识。为人高傲，为了驻颜修炼化春决，喜欢男人为她争风吃醋，其师傅撮合她和韩立双修，两人相互不对眼，红拂派其和韩立一起参加燕家夺宝大会。', '1', '2022-12-06 03:47:02.119000', 'E10ADC3949BA59ABBE56E057F20F883E', '董萱儿', '女', '1', 'dongxuaner', 'dongxuaner@qq.com', null, '2', null, null, null, null, '');
INSERT INTO `sys_user` VALUES ('13', '1', '2022-12-06 03:47:40.257000', '韩立旧识。御灵宗女修，亲切可人，早年曾在太南小会上卖给韩立金竺符笔，多年后和柳眉等奉命追查至木灵婴下落，被韩立发现，韩立追上她时，发现是多年前旧识只将其打晕。', '1', '2022-12-06 03:47:40.258000', 'E10ADC3949BA59ABBE56E057F20F883E', '菡云芝', '女', '1', 'hanyunzhi', 'hanyunzhi@qq.com', null, '2', null, null, null, null, '');
INSERT INTO `sys_user` VALUES ('14', '1', '2022-12-06 03:48:40.648000', '韩立旧识文樯之女。妙音门女修，外出执行危险任务，父女被韩立所救。韩立助其脱离妙音门文思月打算以身相许，奈何韩立不解风情，将她安置在一个荒岛上自行离去。', '1', '2022-12-06 03:48:40.648000', 'E10ADC3949BA59ABBE56E057F20F883E', '文思月', '男', '1', 'wensiyue', 'wensiyue@qq.com', null, '2', null, null, null, null, '');

-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.25-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出  表 os-whrs.alone_app_version 结构
CREATE TABLE IF NOT EXISTS `alone_app_version` (
  `version` varchar(32) NOT NULL DEFAULT '',
  `update_time` varchar(32) DEFAULT NULL,
  `update_user` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.alone_app_version 的数据：~25 rows (大约)
/*!40000 ALTER TABLE `alone_app_version` DISABLE KEYS */;
INSERT INTO `alone_app_version` (`version`, `update_time`, `update_user`) VALUES
	('v0.2.1', '2025-04-08 12:16:40', 'USERNAME'),
	('v0.2.2', '2025-04-16 16:07:42', 'USERNAME'),
	('v0.2.3', '2025-04-27 18:43:40', 'USERNAME'),
	('v0.2.4', '2025-05-06 11:48:42', 'USERNAME'),
	('v0.2.6', '2025-05-12 14:46:41', 'USERNAME'),
	('v0.2.7', '2025-05-26 10:50:53', 'USERNAME'),
	('v0.2.8', '2025-06-05 15:02:49', 'USERNAME'),
	('v0.3.0', '2025-06-25 15:28:49', 'USERNAME'),
	('v0.3.1', '2025-07-01 16:31:39', 'USERNAME'),
	('v0.3.3', '2025-07-09 09:39:45', 'USERNAME'),
	('v0.3.4', '2025-07-15 10:18:08', 'USERNAME'),
	('v0.3.5', '2025-07-23 17:58:44', 'USERNAME'),
	('v0.3.6', '2025-07-28 10:08:35', 'USERNAME'),
	('v0.3.7', '2025-08-16 09:05:19', 'USERNAME'),
	('v0.3.8', '2025-08-22 15:08:00', 'USERNAME'),
	('v0.3.9', '2025-08-27 09:26:30', 'USERNAME'),
	('v0.4.0', '2025-08-30 16:12:32', 'USERNAME'),
	('v0.4.2', '2025-09-06 17:12:03', 'USERNAME'),
	('v0.4.3', '2025-09-17 16:09:49', 'USERNAME'),
	('v0.4.4', '2025-09-20 19:38:11', 'USERNAME'),
	('v0.4.5', '2025-09-30 20:00:42', 'USERNAME'),
	('v0.4.6', '2025-10-23 16:38:04', 'USERNAME'),
	('v0.4.7', '2025-10-28 21:12:46', 'USERNAME'),
	('v0.4.8', '2025-11-03 22:06:47', 'USERNAME'),
	('v0.4.9', '2025-11-17 14:10:24', 'USERNAME');
/*!40000 ALTER TABLE `alone_app_version` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_auth_actions 结构
CREATE TABLE IF NOT EXISTS `farm2_auth_actions` (
  `ID` varchar(32) NOT NULL,
  `CTIME` varchar(14) NOT NULL,
  `ETIME` varchar(14) NOT NULL,
  `EUSERKEY` varchar(32) NOT NULL,
  `CUSERKEY` varchar(32) NOT NULL,
  `STATE` varchar(2) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  `NAME` varchar(256) NOT NULL,
  `SORTCODE` int(11) NOT NULL,
  `PARENTID` varchar(32) NOT NULL,
  `TREECODE` varchar(512) NOT NULL,
  `TYPE` varchar(1) NOT NULL,
  `ACTIONS` varchar(512) DEFAULT NULL,
  `DOMAIN` varchar(64) NOT NULL,
  `MENUKEY` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_auth_actions 的数据：~30 rows (大约)
/*!40000 ALTER TABLE `farm2_auth_actions` DISABLE KEYS */;
INSERT INTO `farm2_auth_actions` (`ID`, `CTIME`, `ETIME`, `EUSERKEY`, `CUSERKEY`, `STATE`, `NOTE`, `NAME`, `SORTCODE`, `PARENTID`, `TREECODE`, `TYPE`, `ACTIONS`, `DOMAIN`, `MENUKEY`) VALUES
	('019b6d2774f67a63a5ee33516ee7bd9a', '20251230104741', '20251230104741', 'sysadmin', 'sysadmin', '1', '', '编辑器测试', 1, '0ea9eb9190514cfaa56f0b8b80606853', '0ea9eb9190514cfaa56f0b8b80606853019b6d2774f67a63a5ee33516ee7bd9a', '1', '', 'FRAME', '/system/editor'),
	('019b8c9385cd7850a5ac95f5c8dec143', '20260105131356', '20260105131439', 'sysadmin', 'sysadmin', '1', '', '职级管理', 2, '138d4bf184e341cbb931ad0e9495ca83', '138d4bf184e341cbb931ad0e9495ca83019b8c9385cd7850a5ac95f5c8dec143', '1', 'localorg*', 'FRAME', '/system/authgrade'),
	('019b924e98bf780f9201a1ef7c73a9da', '20260106155623', '20260106155623', 'sysadmin', 'sysadmin', '1', '', '考勤管理', 3, 'FRAME', '019b924e98bf780f9201a1ef7c73a9da', '1', '', 'FRAME', ''),
	('019b924f17e07a42b69c4a2edbf0ccbf', '20260106155655', '20260106155705', 'sysadmin', 'sysadmin', '1', '', '考勤结果', 1, '019b924e98bf780f9201a1ef7c73a9da', '019b924e98bf780f9201a1ef7c73a9da019b924f17e07a42b69c4a2edbf0ccbf', '1', 'attendancesummary*', 'FRAME', '/system/attendancesummary'),
	('019b9637b66870199b256dfe4fc9c0ed', '20260107100952', '20260107100952', 'sysadmin', 'sysadmin', '1', '', '薪酬社保', 4, 'FRAME', '019b9637b66870199b256dfe4fc9c0ed', '1', '', 'FRAME', ''),
	('019b96467bc67ad1be23b3abddac81f6', '20260107102600', '20260107102600', 'sysadmin', 'sysadmin', '1', '', '薪酬模板', 1, '019b9637b66870199b256dfe4fc9c0ed', '019b9637b66870199b256dfe4fc9c0ed019b96467bc67ad1be23b3abddac81f6', '1', 'salarytemplate*', 'FRAME', '/system/salarytemplate'),
	('019b9ba23f727609a33b7f27f66e37d1', '20260108112420', '20260108112420', 'sysadmin', 'sysadmin', '1', '', '薪酬管理', 2, '019b9637b66870199b256dfe4fc9c0ed', '019b9637b66870199b256dfe4fc9c0ed019b9ba23f727609a33b7f27f66e37d1', '1', 'salaryuser*', 'FRAME', '/system/salaryuser'),
	('05186c1a67624c5ca0c8b9a1129e6709', '20250125182011', '20250125182011', 'sysadmin', 'sysadmin', '1', '', '扩展文件', 2, '33f1e3c2e4eb475fbe822bf8f82ef65f', '33f1e3c2e4eb475fbe822bf8f82ef65f05186c1a67624c5ca0c8b9a1129e6709', '1', 'wdapextendfile*', 'FRAME', '/system/wdapextendfile'),
	('0ea9eb9190514cfaa56f0b8b80606853', '20250119224013', '20260107101022', 'sysadmin', 'sysadmin', '0', '测试性功能', 'DEMO', 10, 'FRAME', '0ea9eb9190514cfaa56f0b8b80606853', '1', '', 'FRAME', ''),
	('138d4bf184e341cbb931ad0e9495ca83', '20250106115838', '20250116170610', 'sysadmin', 'sysadmin', '1', '', '用户机构', 1, 'FRAME', '138d4bf184e341cbb931ad0e9495ca83', '1', '', 'FRAME', ''),
	('15c45eff1f35459e9174542e8fb11b3d', '20250106120553', '20250106120553', 'sysadmin', 'sysadmin', '1', '', '事件日志', 2, '4dfa4eabc6cd4ba1b24919c2f4e20309', '4dfa4eabc6cd4ba1b24919c2f4e2030915c45eff1f35459e9174542e8fb11b3d', '1', 'eventlog*', 'FRAME', '/system/eventlog'),
	('2e7bbf6352324bc0af361b5da13dfea4', '20250423103504', '20250611184623', 'sysadmin', 'sysadmin', '1', '', '用户消息', 2, '4c3acc97093d439bbbf77a6c0ccec7c1', '4c3acc97093d439bbbf77a6c0ccec7c12e7bbf6352324bc0af361b5da13dfea4', '1', 'usermessage*,localuser.query', 'FRAME', '/system/usermessage'),
	('33f1e3c2e4eb475fbe822bf8f82ef65f', '20250121201627', '20250906092419', 'sysadmin', 'sysadmin', '1', '', '文件转换', 10, 'FRAME', '33f1e3c2e4eb475fbe822bf8f82ef65f', '1', '', 'FRAME', ''),
	('3efd5ef7e75d494eb8a54c62fb29a4a9', '20250119224125', '20250119225136', 'sysadmin', 'sysadmin', '1', '', '流程图', 1, '0ea9eb9190514cfaa56f0b8b80606853', '0ea9eb9190514cfaa56f0b8b806068533efd5ef7e75d494eb8a54c62fb29a4a9', '1', '', 'FRAME', '/system/flow'),
	('464772dbafe242cab73be04626ff71a1', '20250106115838', '20250611154653', 'sysadmin', 'sysadmin', '1', '', '在线用户', 1, '4dfa4eabc6cd4ba1b24919c2f4e20309', '4dfa4eabc6cd4ba1b24919c2f4e20309464772dbafe242cab73be04626ff71a1', '1', 'onlineuser*', 'FRAME', '/system/onlineuser'),
	('4c3acc97093d439bbbf77a6c0ccec7c1', '20250106120149', '20250106124935', 'sysadmin', 'sysadmin', '1', '', '系统设置', 2, 'FRAME', '4c3acc97093d439bbbf77a6c0ccec7c1', '1', '', 'FRAME', NULL),
	('4cd79b21341245be966911b78505e2a1', '20250108144752', '20250110101934', 'sysadmin', 'sysadmin', '1', '', '数据字典', 4, '4c3acc97093d439bbbf77a6c0ccec7c1', '4c3acc97093d439bbbf77a6c0ccec7c14cd79b21341245be966911b78505e2a1', '1', 'dicentity*', 'FRAME', '/system/dicentity'),
	('4dfa4eabc6cd4ba1b24919c2f4e20309', '20250116170600', '20250520102320', 'sysadmin', 'sysadmin', '1', '', '运维管理', 9, 'FRAME', '4dfa4eabc6cd4ba1b24919c2f4e20309', '1', '', 'FRAME', ''),
	('52d6c03b4b6a4485be451d89a8e500ee', '20250106120553', '20260105131207', 'sysadmin', 'sysadmin', '1', '', '菜单设置', 7, '4c3acc97093d439bbbf77a6c0ccec7c1', '4c3acc97093d439bbbf77a6c0ccec7c152d6c03b4b6a4485be451d89a8e500ee', '1', 'actions*', 'FRAME', '/system/actions'),
	('554792df02a74154bcda7a00ca6dfbb6', '20250226210942', '20250226210959', 'sysadmin', 'sysadmin', '1', '', 'soffice状态', 5, '33f1e3c2e4eb475fbe822bf8f82ef65f', '33f1e3c2e4eb475fbe822bf8f82ef65f554792df02a74154bcda7a00ca6dfbb6', '1', 'soffice*', 'FRAME', '/system/soffice'),
	('5c36b854c0e74509beaecc9cef29e1ac', '20250121201750', '20250124103438', 'sysadmin', 'sysadmin', '1', '', '转换流程', 3, '33f1e3c2e4eb475fbe822bf8f82ef65f', '33f1e3c2e4eb475fbe822bf8f82ef65f5c36b854c0e74509beaecc9cef29e1ac', '1', 'wdapFlow*', 'FRAME', '/system/wdapflow'),
	('6709583b63ce4209b9b3a14e663dc3f5', '20250113151731', '20250321094239', 'sysadmin', 'sysadmin', '1', '', '附件管理', 1, '4dfa4eabc6cd4ba1b24919c2f4e20309', '4dfa4eabc6cd4ba1b24919c2f4e203096709583b63ce4209b9b3a14e663dc3f5', '1', 'resourceFile*,wdapTask.retask', 'FRAME', '/system/resourcefile'),
	('881b7952835b40b586347ca3786d9186', '20250108144752', '20260105095323', 'sysadmin', 'sysadmin', '1', '', '岗位管理', 2, '138d4bf184e341cbb931ad0e9495ca83', '138d4bf184e341cbb931ad0e9495ca83881b7952835b40b586347ca3786d9186', '1', 'post*', 'FRAME', '/system/post'),
	('8dcd3397cb7f487ca9b3b49f79312849', '20250119233053', '20250119233053', 'sysadmin', 'sysadmin', '1', '', 'echart图表', 1, '0ea9eb9190514cfaa56f0b8b80606853', '0ea9eb9190514cfaa56f0b8b806068538dcd3397cb7f487ca9b3b49f79312849', '1', '', 'FRAME', '/system/echart'),
	('8ddb8b38381f4fdc9a554dcff40b25b8', '20250118192953', '20250118192953', 'sysadmin', 'sysadmin', '1', '', '系统缓存', 3, '4dfa4eabc6cd4ba1b24919c2f4e20309', '4dfa4eabc6cd4ba1b24919c2f4e203098ddb8b38381f4fdc9a554dcff40b25b8', '1', 'caches*', 'FRAME', '/system/caches'),
	('94df945bf0ea4fbfa5a429f58c3f4c0a', '20250106124350', '20260105131154', 'sysadmin', 'sysadmin', '1', '', '机构管理', 1, '138d4bf184e341cbb931ad0e9495ca83', '138d4bf184e341cbb931ad0e9495ca8394df945bf0ea4fbfa5a429f58c3f4c0a', '1', 'localorg*', 'FRAME', '/system/localorg'),
	('bdb8c7583ac0446fbeebb86df9e47253', '20250124103354', '20250125182040', 'sysadmin', 'sysadmin', '1', '', '文件转换器', 4, '33f1e3c2e4eb475fbe822bf8f82ef65f', '33f1e3c2e4eb475fbe822bf8f82ef65fbdb8c7583ac0446fbeebb86df9e47253', '1', 'wdapconvertor*', 'FRAME', '/system/wdapconvertor'),
	('debf90dec7a542ca8e500f43fca67a8e', '20250106120553', '20260104223210', 'sysadmin', 'sysadmin', '1', '', '用户管理', 3, '138d4bf184e341cbb931ad0e9495ca83', '138d4bf184e341cbb931ad0e9495ca83debf90dec7a542ca8e500f43fca67a8e', '1', 'localuser*,localOrg.query', 'FRAME', '/system/localuser'),
	('dee84b67541f4fdfa721066f64cf5661', '20250108144752', '20250110101929', 'sysadmin', 'sysadmin', '1', '', '参数设置', 3, '4c3acc97093d439bbbf77a6c0ccec7c1', '4c3acc97093d439bbbf77a6c0ccec7c1dee84b67541f4fdfa721066f64cf5661', '1', 'parameter*', 'FRAME', '/system/parameter'),
	('fba37b2c9e884f81954f07f1c665b16e', '20250125094137', '20250125094228', 'sysadmin', 'sysadmin', '1', '', '转换任务', 1, '33f1e3c2e4eb475fbe822bf8f82ef65f', '33f1e3c2e4eb475fbe822bf8f82ef65ffba37b2c9e884f81954f07f1c665b16e', '1', 'wdaptask*', 'FRAME', '/system/wdaptask');
/*!40000 ALTER TABLE `farm2_auth_actions` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_auth_dicentity 结构
CREATE TABLE IF NOT EXISTS `farm2_auth_dicentity` (
  `ID` varchar(32) NOT NULL,
  `CTIME` varchar(14) NOT NULL,
  `ETIME` varchar(14) NOT NULL,
  `EUSERKEY` varchar(32) NOT NULL,
  `CUSERKEY` varchar(32) NOT NULL,
  `STATE` varchar(2) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  `NAME` varchar(256) NOT NULL,
  `KEYID` varchar(256) DEFAULT NULL,
  `TYPE` varchar(2) NOT NULL COMMENT '1序列',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_auth_dicentity 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `farm2_auth_dicentity` DISABLE KEYS */;
INSERT INTO `farm2_auth_dicentity` (`ID`, `CTIME`, `ETIME`, `EUSERKEY`, `CUSERKEY`, `STATE`, `NOTE`, `NAME`, `KEYID`, `TYPE`) VALUES
	('019b8d3471e7719589a05526fb96d75b', '20260105160943', '20260105160943', 'sysadmin', 'sysadmin', '1', '', '职级序列', 'TRACK_TYPES', '1'),
	('2715ef6df7b148a2abf58fdb725c5bbb', '20250107174044', '20250107174044', 'sysadmin', 'sysadmin', '1', '', '状态', 'STATE', '1'),
	('b9d937cd669a41999002d7827b6d4a21', '20250107174055', '20250107174055', 'sysadmin', 'sysadmin', '1', '', '类型', 'TYPE', '1');
/*!40000 ALTER TABLE `farm2_auth_dicentity` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_auth_dictype 结构
CREATE TABLE IF NOT EXISTS `farm2_auth_dictype` (
  `ID` varchar(32) NOT NULL,
  `CTIME` varchar(14) NOT NULL,
  `ETIME` varchar(14) NOT NULL,
  `EUSERKEY` varchar(32) NOT NULL,
  `CUSERKEY` varchar(32) NOT NULL,
  `STATE` varchar(2) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  `NAME` varchar(256) NOT NULL,
  `KEYID` varchar(256) DEFAULT NULL,
  `SORTCODE` int(11) NOT NULL,
  `ENTITYID` varchar(32) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_auth_dictype 的数据：~8 rows (大约)
/*!40000 ALTER TABLE `farm2_auth_dictype` DISABLE KEYS */;
INSERT INTO `farm2_auth_dictype` (`ID`, `CTIME`, `ETIME`, `EUSERKEY`, `CUSERKEY`, `STATE`, `NOTE`, `NAME`, `KEYID`, `SORTCODE`, `ENTITYID`) VALUES
	('019b8d35df6473209d6bb9153abdf23e', '20260105161116', '20260105161116', 'sysadmin', 'sysadmin', '1', '', '管理序列', 'MT', 1, '019b8d3471e7719589a05526fb96d75b'),
	('019b8d35ffc7783bb68c880294076217', '20260105161124', '20260105161205', 'sysadmin', 'sysadmin', '1', '', '专业/技术序列', 'TT', 2, '019b8d3471e7719589a05526fb96d75b'),
	('019b8d36297978ecaf53ee914e588330', '20260105161135', '20260105161202', 'sysadmin', 'sysadmin', '1', '', '业务/销售序列', 'SB', 3, '019b8d3471e7719589a05526fb96d75b'),
	('019b8d36789a710081486922bb31f214', '20260105161155', '20260105161155', 'sysadmin', 'sysadmin', '1', '', '职能支持序列', 'FT', 4, '019b8d3471e7719589a05526fb96d75b'),
	('3febe41171a34420a53f8551c3998f0f', '20250107163600', '20250107163707', 'sysadmin', 'sysadmin', '1', '', '禁用', '2', 1, '2715ef6df7b148a2abf58fdb725c5bbb'),
	('9c96be3631304a64a030505a105c9980', '20250107163600', '20250107163707', 'sysadmin', 'sysadmin', '1', '', '可用', '1', 1, '2715ef6df7b148a2abf58fdb725c5bbb'),
	('9cf4608c1f1545d9a4b1833b2b5224b6', '20250107163600', '20250107163707', 'sysadmin', 'sysadmin', '1', '', '文档', '2', 1, 'b9d937cd669a41999002d7827b6d4a21'),
	('e5345d8d5d244cd8b1aace0963350b01', '20250107163600', '20250107163707', 'sysadmin', 'sysadmin', '1', '', '图片', '1', 1, 'b9d937cd669a41999002d7827b6d4a21');
/*!40000 ALTER TABLE `farm2_auth_dictype` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_auth_eventlog 结构
CREATE TABLE IF NOT EXISTS `farm2_auth_eventlog` (
  `ID` varchar(32) NOT NULL,
  `CTIME` varchar(14) NOT NULL,
  `ACTIONTYPE` varchar(128) NOT NULL,
  `OBJTYPE` varchar(128) NOT NULL,
  `OBJID` varchar(128) DEFAULT NULL,
  `USERIP` varchar(32) DEFAULT NULL,
  `USERKEY` varchar(64) DEFAULT NULL,
  `NOTE` varchar(256) DEFAULT NULL,
  `OUSERKEY` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_auth_eventlog 的数据：~21 rows (大约)
/*!40000 ALTER TABLE `farm2_auth_eventlog` DISABLE KEYS */;
INSERT INTO `farm2_auth_eventlog` (`ID`, `CTIME`, `ACTIONTYPE`, `OBJTYPE`, `OBJID`, `USERIP`, `USERKEY`, `NOTE`, `OUSERKEY`) VALUES
	('019ba11809d1734aae591a4f22721386', '20260109125105', 'LOGIN', 'USER', 'sysadmin', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba13d963d748ca0ab59d8af4df667', '20260109133206', 'LOGIN', 'USER', 'sysadmin', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba1436750714887647138d130ce52', '20260109133827', 'LOGOUT', 'USER', 'sysadmin', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba143a5a2714089dd5e2637eab1c8', '20260109133843', 'LOGIN', 'USER', 'sysadmin', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba14711cd77539e84ae1c741feed7', '20260109134228', 'ADD', 'USER', '019ba14711cb776fa25942dbca41d922', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba148111b7d51ae406d85cf8d332e', '20260109134333', 'UPDATE', 'USER', '019ba14711cb776fa25942dbca41d922', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba14811217ed6b469083419474221', '20260109134333', 'SUBMIT_FILE', 'FILE', '019ba147f40d7694947a21bbada94a28', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba14811257e9f8d813cc437da6b54', '20260109134333', 'CANCEL_FILE', 'FILE', '', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba14811277e8389e8ec8b03c36f4b', '20260109134333', 'CANCEL_FILE', 'FILE', '', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba14811287e76b19a3ed6bfa22ccb', '20260109134333', 'CANCEL_FILE', 'FILE', '', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba14811297e68ba62db52c4aa59b3', '20260109134333', 'CANCEL_FILE', 'FILE', '', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba1481cf3749e9520cb2812aac8c7', '20260109134336', 'EXTEND_FILE', 'FILE', '019ba147f40d7694947a21bbada94a28', NULL, NULL, '', NULL),
	('019ba1484c8e773d9340dabab45072eb', '20260109134348', 'UPDATE', 'USER', '019ba14711cb776fa25942dbca41d922', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba1484c94779280ac1b00d1812151', '20260109134348', 'CANCEL_FILE', 'FILE', '', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba1484c967ab99cecaf2b07607d24', '20260109134348', 'CANCEL_FILE', 'FILE', '', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba1484c977aaebd1f1a84c836a740', '20260109134348', 'CANCEL_FILE', 'FILE', '', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba1484c987a9eb0ffb9e6425ac003', '20260109134348', 'CANCEL_FILE', 'FILE', '', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba1492db07261abd109116bf1f082', '20260109134446', 'CANCEL_FILE', 'FILE', '', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba1492db272888d23f5255f438952', '20260109134446', 'CANCEL_FILE', 'FILE', '', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba1492db37283a56b0e8524a28a5d', '20260109134446', 'CANCEL_FILE', 'FILE', '', '127.0.0.1', 'sysadmin', '', NULL),
	('019ba1492db572699f27c00540ddc41f', '20260109134446', 'CANCEL_FILE', 'FILE', '', '127.0.0.1', 'sysadmin', '', NULL);
/*!40000 ALTER TABLE `farm2_auth_eventlog` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_auth_family 结构
CREATE TABLE IF NOT EXISTS `farm2_auth_family` (
  `ID` varchar(32) NOT NULL,
  `KEYID` varchar(32) DEFAULT NULL,
  `NAME` varchar(256) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_auth_family 的数据：~4 rows (大约)
/*!40000 ALTER TABLE `farm2_auth_family` DISABLE KEYS */;
INSERT INTO `farm2_auth_family` (`ID`, `KEYID`, `NAME`, `NOTE`) VALUES
	('019b8c1791007c85831449bf70107041', 'TECH', '技术研发', ''),
	('019b8c17dad876f192015e0465f4ead2', 'SALES', '市场营销', ''),
	('019b8c180fa47a379853aca0406dcb79', 'HR', '人力资源', ''),
	('019b8c18418b7277a05c544e980b639f', 'CW', '财务', '');
/*!40000 ALTER TABLE `farm2_auth_family` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_auth_grade 结构
CREATE TABLE IF NOT EXISTS `farm2_auth_grade` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(256) NOT NULL,
  `KEYID` varchar(256) DEFAULT NULL,
  `TRACKTYPE` varchar(32) DEFAULT NULL COMMENT 'MANAGER管理序列 / PROFESSIONAL专业序列',
  `SORTCODE` int(11) NOT NULL,
  `MINSALARY` int(11) DEFAULT NULL,
  `MAXSALARY` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_auth_grade 的数据：~26 rows (大约)
/*!40000 ALTER TABLE `farm2_auth_grade` DISABLE KEYS */;
INSERT INTO `farm2_auth_grade` (`ID`, `NAME`, `KEYID`, `TRACKTYPE`, `SORTCODE`, `MINSALARY`, `MAXSALARY`) VALUES
	('019b8d3ce7bb702a9e2296374ef42ede', '主管', 'P1', 'MT', 1, 1, 1),
	('019b8d3ce7be7002a9fa3bbdb04c129c', '经理', 'P2', 'MT', 2, 1, 1),
	('019b8d3ce7bf7fe6a4eb7c670a8a2344', '高级经理', 'P3', 'MT', 3, 1, 1),
	('019b8d3ce7c07fd9913e8daccfa4b70c', '总监', 'P4', 'MT', 4, 1, 1),
	('019b8d3ce7c27fbdabd527f58528220c', '副总经理', 'P5', 'MT', 5, 1, 1),
	('019b8d3ce7c47fa096c0aa0e8dd3a3cf', '总经理', 'P6', 'MT', 6, 1, 1),
	('019b8d3ce7ca7f4f85aa051322651637', ' 副总裁', 'P7', 'MT', 7, 1, 1),
	('019b8d3ce7cc7e1ca1cfc5c2ade9a461', 'CEO', 'P8', 'MT', 8, 1, 1),
	('019b8d487be17d5db04aea8b5ca6a645', '初级工程师', 'T1', 'TT', 1, 1, 1),
	('019b8d487be37d3fa75e0f4b90b7e624', '工程师', 'T2', 'TT', 2, 1, 1),
	('019b8d487be57d229b0365ddd37197b5', '高级工程师', 'T3', 'TT', 3, 1, 1),
	('019b8d487be67d16b6e4b4d5da71cde7', '技术专家', 'T4', 'TT', 4, 1, 1),
	('019b8d487be87cfaa8ba98e9219a433d', '高级专家', 'T5', 'TT', 5, 1, 1),
	('019b8d487bea7cde91dcf3f83dd275ac', '资深专家', 'T6', 'TT', 6, 1, 1),
	('019b8d487beb7cd1aa9f17a7e23b6aa4', '架构师', 'T7', 'TT', 7, 1, 1),
	('019b8d4b01af726a86b041bcccbf6653', '销售专员', 'B1', 'SB', 1, 1, 1),
	('019b8d4b01b172bc8ad1eaf9cf9b7155', '高级销售', 'B2', 'SB', 2, 1, 1),
	('019b8d4b01b3722eb3007c27767e9c17', '销售主管', 'B3', 'SB', 3, 1, 1),
	('019b8d4b01b4718f8542f97aa4227d5d', '区域经理', 'B4', 'SB', 4, 1, 1),
	('019b8d4b01b575c4999f113b6547860e', '大区总监', 'B5', 'SB', 5, 1, 1),
	('019b8d4b01b676c3b2fbe9461018ed48', '销售副总裁', 'B6', 'SB', 6, 1, 1),
	('019b8d4c35e778a3a3eec98215d6bf2f', '专员', 'F1', 'FT', 1, 1, 1),
	('019b8d4c35e778a3a3ef3131db7396ea', '主管', 'F2', 'FT', 2, 1, 1),
	('019b8d4c35e778a3a3f0344f2df6d4d1', '经理', 'F3', 'FT', 3, 1, 1),
	('019b8d4c35e778a3a3f1833cbc54a658', '高级经理', 'F4', 'FT', 4, 1, 1),
	('019b8d4c35e778a3a3f2485e1d2380bc', '总监', 'F5', 'FT', 5, 1, 1);
/*!40000 ALTER TABLE `farm2_auth_grade` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_auth_parameter 结构
CREATE TABLE IF NOT EXISTS `farm2_auth_parameter` (
  `ID` varchar(32) NOT NULL,
  `ETIME` varchar(14) NOT NULL,
  `EUSERKEY` varchar(32) NOT NULL,
  `STATE` varchar(2) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  `NAME` varchar(256) NOT NULL,
  `PKEY` varchar(64) NOT NULL,
  `PVALUE` varchar(2048) NOT NULL,
  `DESCRIBES` varchar(2048) DEFAULT NULL,
  `GNAME` varchar(64) DEFAULT NULL,
  `GKEY` varchar(64) DEFAULT NULL,
  `VTYPE` varchar(1) NOT NULL COMMENT ' 文本：1 ,枚举：2,布尔',
  `VERSION` varchar(64) DEFAULT NULL,
  `USERABLE` varchar(1) NOT NULL COMMENT '0否，1是',
  `SOURCETYPE` varchar(1) NOT NULL COMMENT '1XML，2自定义',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_auth_parameter 的数据：~34 rows (大约)
/*!40000 ALTER TABLE `farm2_auth_parameter` DISABLE KEYS */;
INSERT INTO `farm2_auth_parameter` (`ID`, `ETIME`, `EUSERKEY`, `STATE`, `NOTE`, `NAME`, `PKEY`, `PVALUE`, `DESCRIBES`, `GNAME`, `GKEY`, `VTYPE`, `VERSION`, `USERABLE`, `SOURCETYPE`) VALUES
	('019b8934a5f17838a5bb211f53857291', '20260109124943', ':SYSTEM', '1', NULL, '系统默认密码', 'farm2.config.password.default', '111111', '参数描述', '安全', 'safe', '1', '0.0.1', '0', '1'),
	('019b8934a5f877e0a29292f2d7ae0ab2', '20260109124943', ':SYSTEM', '1', NULL, '图形验证码', 'farm2.config.imgcode.limit', '5', '当用户密码验证失败几次后出现验证码0为总是出现验证码，-1为不出现验证码，1为失败一次后出现验证码', '安全', 'safe', '3', '0.0.1', '0', '1'),
	('019b8934a5fb77b7be4107247d7a9bfb', '20260109124943', ':SYSTEM', '1', NULL, '验证失败锁定登录名', 'farm2.config.login.lock', '10', '当用户密码验证失败几次后锁定账户', '安全', 'safe', '3', '0.0.1', '0', '1'),
	('019b8934a5fd779b945754daa7853f94', '20260109124943', ':SYSTEM', '1', NULL, '附件存放目录', 'farm2.config.file.dir', 'DEFAULT:D:\\skcFiles\\files', '设置附件存放目录，格式KEY:dirpath,格式KEY:dirpath,第一组为读写目录，其他组为只读目录（多组目录时必须保留DEFAULT目录）', '附件', 'file', '1', '0.0.1', '0', '1'),
	('019b8934a5ff7823a589b49a8d9dc86a', '20260109124943', ':SYSTEM', '1', NULL, '附件扩展目录', 'farm2.config.file.ex.dir', 'DEFAULT:D:\\skcFiles\\wdap', '设置附件扩展目录，存放预览文件缩略图等，格式KEY:dirpath,格式KEY:dirpath,第一组为读写目录，其他组为只读目录', '附件', 'file', '1', '0.0.1', '0', '1'),
	('019b8934a601780d95d7c13d23deb71c', '20260109124943', ':SYSTEM', '1', NULL, '导出文件临时目录', 'farm2.config.file.export.dir', 'D:\\skcFiles\\export', '设置导出文件临时目录', '附件', 'file', '1', '0.0.1', '0', '1'),
	('019b8934a60277ff859655760d9475a3', '20260109124943', ':SYSTEM', '1', NULL, '允许上传的附件大小', 'farm2.config.file.length.max', '501048576', '单位b,1m=1048576', '附件', 'file', '3', '0.0.1', '0', '1'),
	('019b8934a60477e3a490000070cf24b1', '20260109124943', ':SYSTEM', '1', NULL, '允许上传的图片大小', 'farm2.config.img.length.max', '10485760', '单位b,1m=1048576', '附件', 'file', '3', '0.4.4', '0', '1'),
	('019b8934a60677c88cb18acd2d2ce553', '20260109124943', ':SYSTEM', '1', NULL, '允许上传的媒体大小', 'farm2.config.media.length.max', '524288000', '单位b,1m=1048576', '附件', 'file', '3', '0.4.4', '0', '1'),
	('019b8934a60877d4a440c5d616f620a5', '20260109124943', ':SYSTEM', '1', NULL, '允许上传的附件类型', 'farm2.config.file.exnames', 'png,jpg,jpeg,zip,doc,docx,xls,xlsx,pdf,ppt,pptx,web,rar,txt,flv,mp3,mp4,dcr,gif', '如：png,jpg,jpeg,zip,doc,docx,xls,xlsx,pdf,ppt,pptx,web,rar,txt,flv,mp3,mp4,dcr,gif', '附件', 'file', '1', '0.0.1', '0', '1'),
	('019b8934a60a77b981fd3c090cd9ba25', '20260109124943', ':SYSTEM', '1', NULL, '允许上传的图片类型', 'farm2.config.img.exnames', 'png,jpg,jpeg,gif', '如：png,jpg,jpeg,gif', '附件', 'file', '1', '0.0.1', '0', '1'),
	('019b8934a60c779d8ed8e2beca62c913', '20260109124943', ':SYSTEM', '1', NULL, '允许上传的媒体类型', 'farm2.config.media.exnames', 'mp4,mp3', '如：mp4,mp3', '附件', 'file', '1', '0.0.1', '0', '1'),
	('019b8934a60e7783981bfc50be420cbf', '20260109124943', ':SYSTEM', '1', NULL, '浏览器图片缓存', 'farm2.config.img.browser.cache.able', 'false', '是否开启浏览器缓存', '附件', 'file', '2', '0.0.1', '0', '1'),
	('019b8934a6107767b21956e7995f80af', '20260109124943', ':SYSTEM', '1', NULL, 'linux环境soffice启动CMD命令', 'farm2.config.openoffice.start.linux.cmd', 'nohup "$(readlink -f "$(which soffice)")" -headless -accept="socket,host=127.0.0.1,port=8100;urp;"\n                -nofirststartwizard &', '支持openoffice或libreoffice,也可写死路径启动，如：nohup /opt/libreoffice7.4/program/soffice\n                -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard &', 'Office预览', 'soffice', '1', '0.0.1', '0', '1'),
	('019b8934a612774cb0c36fe1c4158fb1', '20260109124943', ':SYSTEM', '1', NULL, 'windows环境soffice启动CMD命令', 'farm2.config.openoffice.start.windows.cmd', 'C:\\Program Files\\LibreOffice\\program\\soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;"\n                -nofirststartwizard', '支持openoffice或libreoffice', 'Office预览', 'soffice', '1', '0.0.1', '0', '1'),
	('019b8934a6157722bf86167a83405ff4', '20260109124943', ':SYSTEM', '1', NULL, 'linux环境soffice关闭CMD命令', 'farm2.config.openoffice.kill.linux.cmd', 'ps -ef | grep soffice | grep -v grep | awk \'{print $2}\' | xargs kill -9', '支持openoffice或libreoffice', 'Office预览', 'soffice', '1', '0.0.1', '0', '1'),
	('019b8934a61876fa8b17eab00f067d09', '20260109124943', ':SYSTEM', '1', NULL, 'windows环境soffice关闭CMD命令', 'farm2.config.openoffice.kill.windows.cmd', 'taskkill /F /T /IM soffice.exe', '支持openoffice或libreoffice', 'Office预览', 'soffice', '1', '0.0.1', '0', '1'),
	('019b8934a61976eb807ebc52a1a8c551', '20260109124943', ':SYSTEM', '1', NULL, '服务地址', 'farm2.config.openoffice.host', '127.0.0.1', '服务地址,支持openoffice或libreoffice', 'Office预览', 'soffice', '1', '0.0.1', '0', '1'),
	('019b8934a61b76d0998841646fb41e3c', '20260109124943', ':SYSTEM', '1', NULL, '服務端口', 'farm2.config..openoffice.port', '8100', '服務端口,支持openoffice或libreoffice', 'Office预览', 'soffice', '1', '0.0.1', '0', '1'),
	('019b8934a61e76a79716e03e6e8b03c2', '20260109124943', ':SYSTEM', '1', NULL, '命令行編碼', 'config.server.os.cmd.encode', 'gb2312', '操作系統命令行支持的編碼', 'Office预览', 'soffice', '1', '0.0.1', '0', '1'),
	('019b8934a620768cbb40d22755d8abb9', '20260109124943', ':SYSTEM', '1', NULL, '事件日志保留多少天', 'farm2.config.data.log.event.size', '60', '事件日志保留多少天，单位天', '数据量', 'datasize', '3', '0.2.2', '0', '1'),
	('019b8934a62276718a31b0a076d58729', '20260109124943', ':SYSTEM', '1', NULL, '系统标题', 'farm2.config.sys.title.head', 'WHRS人力资源', '如：WHRS人力资源', '系统标识符', 'titles', '1', '0.0.1', '0', '1'),
	('019b8934a6237927a1bbb15196988979', '20260109124943', ':SYSTEM', '1', NULL, '系统页脚', 'farm2.config.sys.title.foot', '人力资源管理系统', '如：人力资源管理系统-备案号等', '系统标识符', 'titles', '1', '0.0.1', '0', '1'),
	('019b8934a62479248d87261e2a87d884', '20260109124943', ':SYSTEM', '1', NULL, '帮助页面地址', 'farm2.config.sys.help.url', 'http://www.wcpdoc.com/webspecial/home/Pub2c9ff22692f269bd0195219f89ac69aa.html', '系统帮助页面地址URL,为NONE时不展示', '系统标识符', 'titles', '1', '0.0.1', '0', '1'),
	('019b8934a62778f8a7c3f5460deaf645', '20260109124943', ':SYSTEM', '1', NULL, '登录页公告内容', 'farm2.config.sys.tip.login.html', 'NONE', '在登录页面展示的公告内容,为NONE时不展示,支持html不可超过2000个字符', '系统标识符', 'titles', '4', '0.2.8', '0', '1'),
	('019b8934a62878eda66936cf8cacec77', '20260109124943', ':SYSTEM', '1', NULL, '系统首页地址URL', 'farm2.config.sys.home.url', '/system', '默认的首页地址，如/info或/home', '系统标识符', 'titles', '4', '0.4.4', '0', '1'),
	('019b8934a62a78d0aef5e026938a8f96', '20260109124943', ':SYSTEM', '1', NULL, '是否必须登录后访问', 'farm2.config.permission.login.need', 'false', '为true时允许用户不登录进行前台操作，为false时默认进入登录页面', '权限设置', 'permissions', '2', '0.1.9', '0', '1'),
	('019b8934a62b78c2a95e3db1c13a6604', '20260109124943', ':SYSTEM', '1', NULL, '默认菜单权限', 'farm2.config.permission.default.menus', 'NONE', '用户登录后默认添加的前台菜单权限，添加菜单关键字多个用逗号分隔,无默认菜单时添加NONE,常用：CREAT_KNOW', '权限设置', 'permissions', '1', '0.1.9', '0', '1'),
	('019b8934a62d78a8a0928b8c528128be', '20260109124943', ':SYSTEM', '1', NULL, '附件转换任务默认转换超时时间', 'farm2.config.wdap.task.timeout.default', '60', '默认超时时间，wdap附件转换任务中，当一个转换器执行时间超过该值时，抛出异常结束转换，单位（秒）', 'wdap系统', 'wdap', '3', '0.2.2', '0', '1'),
	('019b893a1bb27b7d898840cbc5e98540', '20260109124943', ':SYSTEM', '1', NULL, '索引文件存放目录', 'farm2.config.lucene.index.dir', 'D:\\skcFiles\\index', '设置索引文件存放目录', '全文检索', 'lucene', '1', '0.0.1', '0', '1'),
	('019b92e10c287a16b9c1834d46714255', '20260109124943', ':SYSTEM', '1', NULL, '上午上班时间', 'farm2.config.attendance.s.s.time', '0800', '4位时间，前两位是小时，后两位是分钟,填写0表示无需打卡', '考勤', 'attendance', '1', '0.0.1', '0', '1'),
	('019b92e10c2d7b76bb10ccb1a7ce7d4e', '20260109124943', ':SYSTEM', '1', NULL, '上午下班时间', 'farm2.config.attendance.s.x.time', '1200', '4位时间，前两位是小时，后两位是分钟,填写0表示无需打卡', '考勤', 'attendance', '1', '0.0.1', '0', '1'),
	('019b92e10c2d7b76bb11c70d3fd80243', '20260109124943', ':SYSTEM', '1', NULL, '下午上班时间', 'farm2.config.attendance.x.s.time', '1400', '4位时间，前两位是小时，后两位是分钟,填写0表示无需打卡', '考勤', 'attendance', '1', '0.0.1', '0', '1'),
	('019b92e10c2d7b76bb12917507adf84b', '20260109124943', ':SYSTEM', '1', NULL, '下午下班时间', 'farm2.config.attendance.x.x.time', '1800', '4位时间，前两位是小时，后两位是分钟,填写0表示无需打卡', '考勤', 'attendance', '1', '0.0.1', '0', '1');
/*!40000 ALTER TABLE `farm2_auth_parameter` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_auth_post 结构
CREATE TABLE IF NOT EXISTS `farm2_auth_post` (
  `ID` varchar(32) NOT NULL,
  `CTIME` varchar(14) NOT NULL,
  `ETIME` varchar(14) NOT NULL,
  `EUSERKEY` varchar(32) NOT NULL,
  `CUSERKEY` varchar(32) NOT NULL,
  `STATE` varchar(2) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  `NAME` varchar(256) NOT NULL,
  `KEYID` varchar(256) DEFAULT NULL,
  `FAMILYID` varchar(32) DEFAULT NULL,
  `GRADEID` varchar(32) DEFAULT NULL,
  `TRACKTYPE` varchar(32) DEFAULT NULL,
  `MAXUNUM` int(11) DEFAULT NULL,
  `ORGID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_auth_post 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `farm2_auth_post` DISABLE KEYS */;
INSERT INTO `farm2_auth_post` (`ID`, `CTIME`, `ETIME`, `EUSERKEY`, `CUSERKEY`, `STATE`, `NOTE`, `NAME`, `KEYID`, `FAMILYID`, `GRADEID`, `TRACKTYPE`, `MAXUNUM`, `ORGID`) VALUES
	('019b8d8f0bf57bfdab6554b5a719cd6f', '20260105110144', '20260105174259', 'sysadmin', 'sysadmin', '1', '', 'HR专员_山西', 'HREM_SHANXI', '019b8c180fa47a379853aca0406dcb79', '019b8d4c35e778a3a3eec98215d6bf2f', 'FT', 5, '019b8d8d6cc67da5a1208346c040854b'),
	('019b915f186e7151b27642bcb1319b6f', '20260106113447', '20260106113447', 'sysadmin', 'sysadmin', '1', '', '研发经理_山西', 'DEVLD', '019b8c1791007c85831449bf70107041', '019b8d3ce7bf7fe6a4eb7c670a8a2344', 'MT', 1, '019b8d8d2b2a781faae0081025372fba'),
	('019ba1463bc87394bcb6db906cebd47c', '20260109134133', '20260109134133', 'sysadmin', 'sysadmin', '1', '', '销售部经理', 'POST_XSJL', '019b8c17dad876f192015e0465f4ead2', '019b8d4b01b3722eb3007c27767e9c17', 'SB', 1, '019ba1445e3b78e5bbd7328df126bdd8');
/*!40000 ALTER TABLE `farm2_auth_post` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_auth_postaction 结构
CREATE TABLE IF NOT EXISTS `farm2_auth_postaction` (
  `ID` varchar(32) NOT NULL,
  `ACTIONSID` varchar(32) NOT NULL,
  `POSTID` varchar(32) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_auth_postaction 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `farm2_auth_postaction` DISABLE KEYS */;
INSERT INTO `farm2_auth_postaction` (`ID`, `ACTIONSID`, `POSTID`) VALUES
	('019b9de9af377d30bb4733212b072fdb', '019b9637b66870199b256dfe4fc9c0ed', '019b915f186e7151b27642bcb1319b6f'),
	('019b9de9af377d30bb48037ae9679c34', '019b96467bc67ad1be23b3abddac81f6', '019b915f186e7151b27642bcb1319b6f'),
	('019b9de9af377d30bb493764c80f1c21', '019b9ba23f727609a33b7f27f66e37d1', '019b915f186e7151b27642bcb1319b6f');
/*!40000 ALTER TABLE `farm2_auth_postaction` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_auth_post_ptype 结构
CREATE TABLE IF NOT EXISTS `farm2_auth_post_ptype` (
  `ID` varchar(32) NOT NULL,
  `POSTID` varchar(32) NOT NULL,
  `PTYPEID` varchar(32) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_auth_post_ptype 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `farm2_auth_post_ptype` DISABLE KEYS */;
INSERT INTO `farm2_auth_post_ptype` (`ID`, `POSTID`, `PTYPEID`) VALUES
	('019b8c247175773fadb31b6cdc1de6f6', '019b8c1a7b75762782e303189295c554', '019b8c21c94d7a0e9ec1cad77e2b13e4'),
	('019b8d90909a72d4a0be3d1cb8f958e8', '019b8d8f0bf57bfdab6554b5a719cd6f', '019b8c21c94d7a0e9ec1cad77e2b13e4');
/*!40000 ALTER TABLE `farm2_auth_post_ptype` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_auth_ptype 结构
CREATE TABLE IF NOT EXISTS `farm2_auth_ptype` (
  `ID` varchar(32) NOT NULL,
  `CTIME` varchar(14) NOT NULL,
  `ETIME` varchar(14) NOT NULL,
  `EUSERKEY` varchar(32) NOT NULL,
  `CUSERKEY` varchar(32) NOT NULL,
  `STATE` varchar(2) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  `NAME` varchar(256) NOT NULL,
  `SORTCODE` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_auth_ptype 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `farm2_auth_ptype` DISABLE KEYS */;
INSERT INTO `farm2_auth_ptype` (`ID`, `CTIME`, `ETIME`, `EUSERKEY`, `CUSERKEY`, `STATE`, `NOTE`, `NAME`, `SORTCODE`) VALUES
	('019b8c21abd9764f8e1f0a573518fc88', '20260105110935', '20260105110935', 'sysadmin', 'sysadmin', '1', '', '权限岗位', 1),
	('019b8c21c94d7a0e9ec1cad77e2b13e4', '20260105110943', '20260105175016', 'sysadmin', 'sysadmin', '1', '', '山西', 1);
/*!40000 ALTER TABLE `farm2_auth_ptype` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_auth_userpost 结构
CREATE TABLE IF NOT EXISTS `farm2_auth_userpost` (
  `ID` varchar(32) NOT NULL,
  `USERKEY` varchar(64) NOT NULL,
  `POSTID` varchar(32) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_auth_userpost 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `farm2_auth_userpost` DISABLE KEYS */;
INSERT INTO `farm2_auth_userpost` (`ID`, `USERKEY`, `POSTID`) VALUES
	('019b91eff64e7b17b12261c661882046', 'testadmin', '019b8d8f0bf57bfdab6554b5a719cd6f'),
	('019b924ffa9a797398a5e4f0c80b8a89', 'sysadmin', '019b8d8f0bf57bfdab6554b5a719cd6f'),
	('019b939c897c78beb79d6444c605cf64', 'zhangwuji', '019b8d8f0bf57bfdab6554b5a719cd6f'),
	('019b9de9cc51791e8673003dafb6e414', 'zhaomin', '019b915f186e7151b27642bcb1319b6f'),
	('019ba1484c8a77759de5f9335fbc4200', 'yangdingtian', '019ba1463bc87394bcb6db906cebd47c');
/*!40000 ALTER TABLE `farm2_auth_userpost` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_llm_client 结构
CREATE TABLE IF NOT EXISTS `farm2_llm_client` (
  `ID` varchar(32) NOT NULL,
  `STATE` varchar(2) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  `ETIME` varchar(14) NOT NULL,
  `NAME` varchar(256) NOT NULL,
  `IMPLCLASS` varchar(512) NOT NULL,
  `APIKEY` varchar(512) DEFAULT NULL,
  `BASEURL` varchar(512) NOT NULL,
  `MODELKEY` varchar(512) NOT NULL,
  `TOKENSIZE` int(11) NOT NULL,
  `PROXYIP` varchar(64) DEFAULT NULL,
  `PROXYPORT` int(11) DEFAULT NULL,
  `FUNCKEY` varchar(64) DEFAULT NULL,
  `MODELLEVEL` int(11) DEFAULT NULL,
  `DIMENSIONS` int(11) DEFAULT NULL,
  `ENFORMAT` varchar(64) DEFAULT NULL,
  `APPKEYS` varchar(512) NOT NULL,
  `TITLE` varchar(512) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_llm_client 的数据：~9 rows (大约)
/*!40000 ALTER TABLE `farm2_llm_client` DISABLE KEYS */;
INSERT INTO `farm2_llm_client` (`ID`, `STATE`, `NOTE`, `ETIME`, `NAME`, `IMPLCLASS`, `APIKEY`, `BASEURL`, `MODELKEY`, `TOKENSIZE`, `PROXYIP`, `PROXYPORT`, `FUNCKEY`, `MODELLEVEL`, `DIMENSIONS`, `ENFORMAT`, `APPKEYS`, `TITLE`) VALUES
	('11aed1aede23460080548df9d43506b4', '0', 'ollama-向量模型', '20250219111855', 'Aliyun_Embedding_SDK', 'com.farm2.llm.client.impl.Farm2EmbedOllamaClientImpl', '', 'http://192.168.0.107:11434/api/embed', 'nomic-embed-text', 1, '', 1, 'EMB', 1, 768, 'float', '', 'ollama-向量模型'),
	('239144d08a4c451490577576920486c3', '0', 'deepseek-reasoner，deepseek-chat', '20251023235314', 'SDQS_DEEPSEEK_SDK', 'com.farm2.llm.client.impl.Farm2LlmDeepseekClientImpl', 'sk-e3101a74817040d392a9d299a5232daa', 'https://api.deepseek.com/v1/chat/completions', 'deepseek-chat', 5000, '', 1, 'TALK', NULL, NULL, '', 'SEARCH_ANALYSIS,CREATE_ASSISTANT,QUESTION_ANSWERING,FORM_COMPLETION,KNOWTYPE_CHOOSE,QUOTE_SEARCH_JSON,QUOTE_SUMMARY,CLIENT_TEST', '深度求索DeepSeek'),
	('340afd029d1b4b6382c57ff9f9d2aa5e', '0', '阿里云-对话模型', '20251221180210', 'PlexPt_ChatGPT_SDK', 'com.farm2.llm.client.impl.Farm2LlmPlexPtClientImpl', 'sk-d4155784964c42339284e1677701f940', 'https://dashscope.aliyuncs.com/compatible-mode/', 'qwen-plus', 8500, '', 0, 'TALK', 1, NULL, NULL, '', '阿里云-对话模型'),
	('81ef7415721c4baa9d079e3e15586786', '0', 'DeepSeek-对话模型', '20250219111908', 'PlexPt_ChatGPT_SDK', 'com.farm2.llm.client.impl.Farm2LlmPlexPtClientImpl', '', 'https://api.deepseek.com/', 'deepseek-reasoner', 3500, '', 1, 'TALK', NULL, NULL, '', '', 'DeepSeek-对话模型'),
	('9a81646e61a04e398d0d49ddf7d504bd', '0', 'ollama-对话模型', '20250219111903', 'PlexPt_ChatGPT_SDK', 'com.farm2.llm.client.impl.Farm2LlmPlexPtClientImpl', '', 'http://192.168.0.107:11434/', 'deepseek-r1:32b', 5000, '', 1, 'TALK', NULL, NULL, '', '', 'ollama-对话模型'),
	('9b58b95c717c467b834d9a9fb90fe34f', '0', '阿里云-对话模型-客户测试', '20250813194510', 'ALIYUN_DASHSCOPE_SDK', 'com.farm2.llm.client.impl.Farm2LlmDashscopeClientImpl', 'sk-97fa8a1aeb454e69bfbdc49ad2163c24', 'https://dashscope.aliyuncs.com/compatible-mode/', 'qwen-turbo', 3504, '', 1, 'TALK', 1, NULL, NULL, 'KNOWTYPE_CHOOSE,QUOTE_SEARCH_JSON,QUOTE_SUMMARY,CLIENT_TEST,FORM_COMPLETION,QUESTION_ANSWERING,CREATE_ASSISTANT,SEARCH_ANALYSIS', '阿里云-对话模型-客户测试'),
	('a125218248ec417baf2e349f684ece30', '0', '功能演示-对话模型', '20250619144540', 'demo_Talk_SDK', 'com.farm2.llm.client.impl.Farm2LlmDemoClientImpl', '', '', '', 1, '', 1, 'TALK', NULL, NULL, '', '', '功能演示-对话模型'),
	('eeb899244ac546f5bc38fc067ff5d5e4', '0', '阿里云-向量模型', '20250430073732', 'PlexPt_ChatGPT_SDK', 'com.farm2.llm.client.impl.Farm2EmbedAliyunClientImpl', 'sk-d4155784964c42339284e1677701f940', 'https://dashscope.aliyuncs.com/compatible-mode/v1/embeddings', 'text-embedding-v3', 500, '', 1, 'EMB', 1, 1024, 'float', '', '阿里云-向量模型'),
	('f59bafdff0e5400ea43083481bed52a5', '1', '', '20251221180209', 'ALIYUN_DASHSCOPE_SDK', 'com.farm2.llm.client.impl.Farm2LlmDashscopeClientImpl', 'sk-d4155784964c42339284e1677701f940', 'deault', 'qwen-turbo', 3502, '', 1, 'TALK', NULL, NULL, '', '', '阿里云-Dashscope-对话');
/*!40000 ALTER TABLE `farm2_llm_client` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_llm_client_post 结构
CREATE TABLE IF NOT EXISTS `farm2_llm_client_post` (
  `ID` varchar(32) NOT NULL,
  `POSTKEY` varchar(32) NOT NULL,
  `POSTNAME` varchar(32) NOT NULL,
  `CLIENTID` varchar(32) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_llm_client_post 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `farm2_llm_client_post` DISABLE KEYS */;
INSERT INTO `farm2_llm_client_post` (`ID`, `POSTKEY`, `POSTNAME`, `CLIENTID`) VALUES
	('74bed1bec74241d6b1cd78670e6c35de', 'AICLIENT', 'AI模型', '340afd029d1b4b6382c57ff9f9d2aa5e');
/*!40000 ALTER TABLE `farm2_llm_client_post` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_local_org 结构
CREATE TABLE IF NOT EXISTS `farm2_local_org` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(64) NOT NULL,
  `CTIME` varchar(14) NOT NULL,
  `ETIME` varchar(14) NOT NULL,
  `EUSERKEY` varchar(32) NOT NULL,
  `CUSERKEY` varchar(32) NOT NULL,
  `STATE` varchar(2) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  `SORTCODE` int(11) NOT NULL,
  `PARENTID` varchar(32) NOT NULL,
  `TREECODE` varchar(512) NOT NULL,
  `CODE` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_local_org 的数据：~7 rows (大约)
/*!40000 ALTER TABLE `farm2_local_org` DISABLE KEYS */;
INSERT INTO `farm2_local_org` (`ID`, `NAME`, `CTIME`, `ETIME`, `EUSERKEY`, `CUSERKEY`, `STATE`, `NOTE`, `SORTCODE`, `PARENTID`, `TREECODE`, `CODE`) VALUES
	('019b102ca6f37c208b44286b23a62dc4', '研发部', '20251212092840', '20251212101346', 'sysadmin', 'sysadmin', '1', '', 1, 'NONE', '019b102ca6f37c208b44286b23a62dc4', 'DEPARD'),
	('019b104e5f777383b4c3ae6a29c147ce', '市场部', '20251212100530', '20251212100530', 'sysadmin', 'sysadmin', '1', '', 2, 'NONE', '019b104e5f777383b4c3ae6a29c147ce', 'SCPART'),
	('019b10543d1776698ad0976e7867256c', '测试部', '20251212101154', '20251212101154', 'sysadmin', 'sysadmin', '1', '', 3, 'NONE', '019b10543d1776698ad0976e7867256c', 'tpart'),
	('019b8d8d2b2a781faae0081025372fba', '山西分公司', '20260105174637', '20260105174637', 'sysadmin', 'sysadmin', '1', '', 4, 'NONE', '019b8d8d2b2a781faae0081025372fba', 'SHANXIORG'),
	('019b8d8d6cc67da5a1208346c040854b', '人力资源', '20260105174654', '20260105174654', 'sysadmin', 'sysadmin', '1', '', 1, '019b8d8d2b2a781faae0081025372fba', '019b8d8d2b2a781faae0081025372fba019b8d8d6cc67da5a1208346c040854b', 'HRORG'),
	('019ba1445e3b78e5bbd7328df126bdd8', '光明顶', '20260109133931', '20260109133931', 'sysadmin', 'sysadmin', '1', '', 1, 'NONE', '019ba1445e3b78e5bbd7328df126bdd8', 'GMD'),
	('019ba144a7247846ba6bc3d64ad22142', '研发部', '20260109133949', '20260109133949', 'sysadmin', 'sysadmin', '1', '', 1, '019ba1445e3b78e5bbd7328df126bdd8', '019ba1445e3b78e5bbd7328df126bdd8019ba144a7247846ba6bc3d64ad22142', 'YFB');
/*!40000 ALTER TABLE `farm2_local_org` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_local_user 结构
CREATE TABLE IF NOT EXISTS `farm2_local_user` (
  `ID` varchar(32) NOT NULL,
  `CTIME` varchar(14) NOT NULL,
  `ETIME` varchar(14) NOT NULL,
  `CUSERKEY` varchar(32) NOT NULL,
  `EUSERKEY` varchar(32) NOT NULL,
  `PASSWORD` varchar(256) NOT NULL,
  `ENCODE` varchar(32) NOT NULL,
  `TYPE` varchar(32) NOT NULL COMMENT '1:系统用户3超级用户9接口用户',
  `LOGINNAME` varchar(32) NOT NULL,
  `NAME` varchar(32) NOT NULL,
  `LOGINTIME` varchar(14) DEFAULT NULL,
  `STATE` varchar(2) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  `ORGID` varchar(32) DEFAULT NULL,
  `PHOTOID` varchar(32) NOT NULL,
  `GRADEID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_local_user 的数据：~10 rows (大约)
/*!40000 ALTER TABLE `farm2_local_user` DISABLE KEYS */;
INSERT INTO `farm2_local_user` (`ID`, `CTIME`, `ETIME`, `CUSERKEY`, `EUSERKEY`, `PASSWORD`, `ENCODE`, `TYPE`, `LOGINNAME`, `NAME`, `LOGINTIME`, `STATE`, `NOTE`, `ORGID`, `PHOTOID`, `GRADEID`) VALUES
	('019ac84583377b8485f2c7f6d0fa037d', '20251128102310', '20251212101234', 'sysadmin', 'sysadmin', 'cY9Snd46eI7b2a86b045ba9b101bc253e4d9fc0b841f7aef539036131021abef991f13b0ca', 'FARM2', '1', 'guest', 'guest', '20260109001609', '1', '', '019b10543d1776698ad0976e7867256c', '11', NULL),
	('019b91a3c9a8765bb9f6d1eba24eaf22', '20260106124948', '20260106220108', 'sysadmin', 'sysadmin', 'G0ibfwkJtP06d0ef06ca8684bedc47f8c4a87e6f5903d6c1f4e6210ce252be29f038ae0306', 'FARM2', '1', 'zhangwuji', '张无忌', NULL, '1', '', '019b8d8d6cc67da5a1208346c040854b', '11', '019b8d4c35e778a3a3eec98215d6bf2f'),
	('019b939c204171aeb1c4fa44f3bfe51c', '20260106220041', '20260106220104', 'sysadmin', 'sysadmin', '5TCM5qwIZ3fd9e4bd94b7ecb57b45aa7c51e9aa27d6742dc3d6dfc84595e1902273b2fa6f8', 'FARM2', '1', 'zhaomin', '赵敏', '20260109001544', '1', '', '019b10543d1776698ad0976e7867256c', '11', '019b8d4b01af726a86b041bcccbf6653'),
	('019b939c5f0d7bbea5c9c766b3feab34', '20260106220057', '20260106220110', 'sysadmin', 'sysadmin', '5fosiijFMT8754a93492c781f7975bf4f0a0fbe041a916e2e0cae234b0cf883ead417608b8', 'FARM2', '1', 'dhwl', '大海无量', NULL, '1', '', '019b104e5f777383b4c3ae6a29c147ce', '11', '019b8d4b01b676c3b2fbe9461018ed48'),
	('019b939d3cea7662aa15b7a41a3e63f6', '20260106220154', '20260106220158', 'sysadmin', 'sysadmin', 'KIWFRN73UW443071083ceda3c12797a5477a9ffe984f2b4f40fdceab9734a62e187f9bdcbf', 'FARM2', '1', 'HDF', '海大富', '20260108221744', '1', '', '019b102ca6f37c208b44286b23a62dc4', '11', '019b8d487be37d3fa75e0f4b90b7e624'),
	('019b9de3028c7e39b4b5b8abb750d50b', '20260108215418', '20260108215418', 'sysadmin', 'sysadmin', 'Z753WbelsC33c8dd11397b69fc2afcfecac804e4b8c91b6eab87d38a3fb731641338bc695f', 'FARM2', '1', 'asdf', 'asdf', NULL, '2', '', '019b102ca6f37c208b44286b23a62dc4', '11', '019b8d4b01af726a86b041bcccbf6653'),
	('019ba14711cb776fa25942dbca41d922', '20260109134228', '20260109134348', 'sysadmin', 'sysadmin', 'whEWdi4eB120a189e7ddfd41dae54ab1f79bdd77e4d9889489f2049277e4c3773ca8c0b50e', 'FARM2', '1', 'yangdingtian', '阳顶天', NULL, '2', '', '019ba1445e3b78e5bbd7328df126bdd8', '11', '019b8d4b01b3722eb3007c27767e9c17'),
	('561afc75c51d4dc482d1cb491cf1148d', '20250101010101', '20260106141317', 'sysadmin', 'sysadmin', '3dXk8gQnHNd4095f5c83618b66e9f8e862185ce746556a2f869ebb0be1f696af16149b2462', 'FARM2', '1', 'systest', '测试用户', '20260109095357', '2', '', '019b104e5f777383b4c3ae6a29c147ce', '11', '019b8d4b01af726a86b041bcccbf6653'),
	('9f1318305af64ecb9b26197112d2f7c8', '20250526135442', '20260106171910', 'sysadmin', 'sysadmin', 'YT7F12a94gd93b993bd6eef3c27be648fa851744996acf5555eea863d66f75b2bd2ae07da1', 'FARM2', '3', 'testadmin', '演示管理员', '20250526135853', '3', '', '019b8d8d6cc67da5a1208346c040854b', '11', '019b8d4c35e778a3a3eec98215d6bf2f'),
	('sysadmin-id', '20250101010101', '20260106155753', 'sysadmin', 'sysadmin', 'tt7Jwyh9zV9d0cbfe9f020f1c5923ff34ffb174a32e85572f40e1e4019abd93e62f6bc7ea5', 'FARM2', '3', 'sysadmin', '最帅管理员', '20260109133843', '2', NULL, '019b102ca6f37c208b44286b23a62dc4', '10', '019b8d4b01af726a86b041bcccbf6653');
/*!40000 ALTER TABLE `farm2_local_user` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_local_user_info 结构
CREATE TABLE IF NOT EXISTS `farm2_local_user_info` (
  `ID` varchar(32) NOT NULL,
  `SEX` varchar(32) DEFAULT NULL,
  `BIRTHDATE` varchar(32) DEFAULT NULL,
  `PHONECODE` varchar(32) DEFAULT NULL,
  `EMPTIME` varchar(32) DEFAULT NULL,
  `EMAIL` varchar(32) DEFAULT NULL,
  `IDCODE` varchar(32) DEFAULT NULL,
  `CITYCODE` varchar(32) DEFAULT NULL,
  `PAN` varchar(32) DEFAULT NULL,
  `CONTRACTFID` varchar(32) DEFAULT NULL COMMENT 'Contract',
  `EQFID` varchar(32) DEFAULT NULL,
  `DEGFID` varchar(32) DEFAULT NULL,
  `MERFID` varchar(32) DEFAULT NULL,
  `ICARDFID` varchar(32) DEFAULT NULL,
  `PROCESS` int(11) NOT NULL,
  `USERKEY` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_local_user_info 的数据：~11 rows (大约)
/*!40000 ALTER TABLE `farm2_local_user_info` DISABLE KEYS */;
INSERT INTO `farm2_local_user_info` (`ID`, `SEX`, `BIRTHDATE`, `PHONECODE`, `EMPTIME`, `EMAIL`, `IDCODE`, `CITYCODE`, `PAN`, `CONTRACTFID`, `EQFID`, `DEGFID`, `MERFID`, `ICARDFID`, `PROCESS`, `USERKEY`) VALUES
	('019b8e51957f7417a04e6fb651e5ce36', 'M', '20260101', '13593183783', '20260106', 'mcc@126.com', '142202198507150112', '140000,140500', '1111111111111111', '019b8ee88fdb71a5aacc369fdf2bef68', '019b8ed8d3567528800f17faeb9a9016', '019b8edf48507fd1a9b52c62973a9f24', '019b8edf524d74938a936b8ee95c9837', '019b8edf5dff74fa9d5bb48b4413ce5a', 100, 'testadmin'),
	('019b8e819c0b769eafd047effce7b1bc', 'M', '20260115', '13111050937', '20260106', '123@126.com', '142202199008120118', '140000,140200', '1322232123221211', '019ba0756bcc7aa79ffa7019c2f406ac', '', '', '', '', 69, 'systest'),
	('019b8e819c0b769eafd11b5db854a3ed', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'systest'),
	('019b8e819c0b769eafd237b45c3eb93e', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'systest'),
	('019b8e8698b07f67a7c3f60cb813f999', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '', '', '', '', 0, 'sysadmin'),
	('019b8e869e6c7ae7a76e70bf2a793c1f', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'guest'),
	('019b91abebd970f4a008236cb98078bc', NULL, NULL, NULL, '20260106', '122@123.com', '111', NULL, '11', '', '', '', '019b91d8e91d7abda094c60605cec533', '', 38, 'zhangwuji'),
	('019b939c74c6701d817cbebca4338717', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '', '', '', '', 0, 'zhaomin'),
	('019b939c8d1a7395a972b8818c8624e7', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '', '', '', '', 0, 'dhwl'),
	('019b939d4ab37da1a0328294fcef7ec5', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', '', '', '', '', 0, 'HDF'),
	('019ba1473b477dee8e2ed104bf34625e', 'M', '20000215', NULL, '20260109', 'ydt@126.com', NULL, '', NULL, '019ba147f40d7694947a21bbada94a28', '', '', '', '', 38, 'yangdingtian');
/*!40000 ALTER TABLE `farm2_local_user_info` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_resource_file 结构
CREATE TABLE IF NOT EXISTS `farm2_resource_file` (
  `ID` varchar(32) NOT NULL,
  `CTIME` varchar(14) NOT NULL,
  `CUSERKEY` varchar(32) NOT NULL,
  `STATE` varchar(2) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  `EXNAME` varchar(32) NOT NULL,
  `RELATIVEPATH` varchar(1024) NOT NULL,
  `FILENAME` varchar(128) NOT NULL,
  `TITLE` varchar(256) NOT NULL,
  `FILESIZE` int(11) NOT NULL,
  `RESOURCEKEY` varchar(32) NOT NULL COMMENT '对应BASE目录',
  `APPID` varchar(128) DEFAULT NULL COMMENT '对应外部ID',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_resource_file 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `farm2_resource_file` DISABLE KEYS */;
INSERT INTO `farm2_resource_file` (`ID`, `CTIME`, `CUSERKEY`, `STATE`, `NOTE`, `EXNAME`, `RELATIVEPATH`, `FILENAME`, `TITLE`, `FILESIZE`, `RESOURCEKEY`, `APPID`) VALUES
	('019ba147f40d7694947a21bbada94a28', '20260109134325', 'sysadmin', '1', 'SYSTEM', 'ppt', '2026\\01\\09\\13', '019ba147f40d76949479673c609b9202.farm.file', 'Excel函数应用.ppt', 183808, 'DEFAULT', NULL);
/*!40000 ALTER TABLE `farm2_resource_file` ENABLE KEYS */;

-- 导出  表 os-whrs.farm2_resource_file_registe 结构
CREATE TABLE IF NOT EXISTS `farm2_resource_file_registe` (
  `ID` varchar(32) NOT NULL,
  `CTIME` varchar(14) NOT NULL,
  `APPID` varchar(32) NOT NULL,
  `TYPE` varchar(32) NOT NULL,
  `FILEID` varchar(32) NOT NULL,
  `NOTE` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.farm2_resource_file_registe 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `farm2_resource_file_registe` DISABLE KEYS */;
INSERT INTO `farm2_resource_file_registe` (`ID`, `CTIME`, `APPID`, `TYPE`, `FILEID`, `NOTE`) VALUES
	('019ba1492dab7a828390ed178a46b550', '20260109134446', 'yangdingtian', 'USER', '019ba147f40d7694947a21bbada94a28', NULL);
/*!40000 ALTER TABLE `farm2_resource_file_registe` ENABLE KEYS */;

-- 导出  表 os-whrs.user_message 结构
CREATE TABLE IF NOT EXISTS `user_message` (
  `ID` varchar(32) NOT NULL,
  `CTIME` varchar(14) NOT NULL,
  `CUSERKEY` varchar(32) NOT NULL,
  `STATE` varchar(2) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  `READUSERKEY` varchar(32) NOT NULL,
  `CONTENT` varchar(1024) NOT NULL,
  `TITLE` varchar(256) NOT NULL,
  `READSTATE` varchar(2) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.user_message 的数据：~8 rows (大约)
/*!40000 ALTER TABLE `user_message` DISABLE KEYS */;
INSERT INTO `user_message` (`ID`, `CTIME`, `CUSERKEY`, `STATE`, `NOTE`, `READUSERKEY`, `CONTENT`, `TITLE`, `READSTATE`) VALUES
	('019a99676b9378cbae00bb86f49dfef7', '20251119075803', 'SYS', '1', NULL, 'sysadmin', '您有一个新任务已经开始：wcp漏洞修复', '新的待办任务', '1'),
	('019b4a86dcb67c488be0f7305b64d915', '20251223172510', 'sysadmin', '1', '', 'sysadmin', 'ceasdf ', 'asdf', '1'),
	('019b5d8ad4a97f91948fb6a606f66baf', '20251227100218', 'sysadmin', '1', '', 'sysadmin', '撒地方', '阿斯蒂芬', '1'),
	('23230acf413c4ff6a8b19c1b8b638d60', '20250526135911', 'SYS', '1', NULL, 'systest', '知识已通过审核[AUDIT_TASK]', '"探索未来企业知识管理新方式-知识版本"审核通过', '1'),
	('3f0bc218e7044b338d1b85642f9c5dbe', '20250808154318', 'SYS', '1', NULL, 'sysadmin', '您有一个新的审核任务"测试审核标题生成-知识版本"[AUDIT_TASK]', '新的审核任务', '1'),
	('5e070245703148558681fa2eadfff220', '20250526135839', 'SYS', '1', NULL, 'testadmin', '您有一个新的审核任务"探索未来企业知识管理新方式-知识版本"[AUDIT_TASK]', '新的审核任务', '1'),
	('9203f122c3b54391a1030af19da3b80e', '20250808154318', 'SYS', '1', NULL, 'testadmin', '您有一个新的审核任务"测试审核标题生成-知识版本"[AUDIT_TASK]', '新的审核任务', '0'),
	('f8ea3734491c422a85a75cb8fb9f654c', '20250526135839', 'SYS', '1', NULL, 'sysadmin', '您有一个新的审核任务"探索未来企业知识管理新方式-知识版本"[AUDIT_TASK]', '新的审核任务', '1');
/*!40000 ALTER TABLE `user_message` ENABLE KEYS */;

-- 导出  表 os-whrs.wdap2_convertor 结构
CREATE TABLE IF NOT EXISTS `wdap2_convertor` (
  `ID` varchar(32) NOT NULL,
  `CLASSKEY` varchar(512) NOT NULL,
  `SFILEMODEL` varchar(32) NOT NULL COMMENT '系统附件，图片集预览，PDF预览，图片预览',
  `TFILEMODEL` varchar(32) NOT NULL,
  `TITLE` varchar(64) NOT NULL,
  `STATE` varchar(2) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  `PARAMS` text,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.wdap2_convertor 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `wdap2_convertor` DISABLE KEYS */;
INSERT INTO `wdap2_convertor` (`ID`, `CLASSKEY`, `SFILEMODEL`, `TFILEMODEL`, `TITLE`, `STATE`, `NOTE`, `PARAMS`) VALUES
	('07823d14393341ff9704bde50a5a66c3', 'org.farm2.wdap.convertor.impl.OpenOfficeConvertor', 'RESOURCE_FILE', 'NONE_FILE', 'OpenOffice文件转HTML', '1', '', '[{"field":"key","note":"将文件拷贝到目标模型:,文本文件:TEXT_FILE,缩略图文件夹:THUMBNAIL_FILE,Mp3音频文件:AUDIO_MP3_FILE,原始文件:RESOURCE_FILE,未知文件:NONE_FILE,Mp4视频文件:VIDEO_MP4_FILE,HTML文件:HTML_FILE,图片册文件夹:IMGS_FILE,pdf文件:PDF_FILE","title":"目标文件类型","value":"HTML_FILE"}]'),
	('19faf5f6fef24fa0a281cb0fd81119ff', 'org.farm2.wdap.convertor.impl.PdfToImgConvertor', 'RESOURCE_FILE', 'IMGS_FILE', 'PDF转图片', '1', '', '[{"field":"dpi","note":"每英寸内包含的点数,打印所需的PDF文件通常需要较高DPI（如300 DPI或更高）而用于屏幕显示的PDF文件，72 DPI或96 DPI往往就足够了","title":"转换DPI","value":"96"},{"field":"img-max-width","note":"图片册左侧缩略图大小，默认150","title":"缩略图宽度","value":"150"},{"field":"img-min-width","note":"图片册右侧详情图大小，默认1000","title":"详情图宽度","value":"1000"}]'),
	('3151a415f6c6489da1fe3e561d163d8b', 'org.farm2.wdap.convertor.impl.HtmlToTextConvertor', 'HTML_FILE', 'TEXT_FILE', 'Html转文本(html模型)', '1', '', '[]'),
	('45c0c5d0a24641dc9671dd9698f7c261', 'org.farm2.wdap.convertor.impl.PdfToTextConvertor', 'RESOURCE_FILE', 'TEXT_FILE', 'PDF(原始)转文本', '1', '', '[]'),
	('4a0b44df7f4644489e6de36966ce847b', 'org.farm2.wdap.convertor.impl.OpenOfficeConvertor', 'RESOURCE_FILE', 'NONE_FILE', 'OpenOffice文件转PDF', '1', '', '[{"field":"key","note":"将文件拷贝到目标模型:,文本文件:TEXT_FILE,缩略图文件夹:THUMBNAIL_FILE,Mp3音频文件:AUDIO_MP3_FILE,原始文件:RESOURCE_FILE,未知文件:NONE_FILE,Mp4视频文件:VIDEO_MP4_FILE,图片册文件夹:IMGS_FILE,pdf文件:PDF_FILE","title":"目标文件类型","value":"PDF_FILE"}]'),
	('5a9c027028794bcf9af9758fc1bcfae9', 'org.farm2.wdap.convertor.impl.PdfConvertor', 'RESOURCE_FILE', 'PDF_FILE', 'PDF复制预览', '1', '', NULL),
	('62a20675cd19416a8d2d32929eac032e', 'org.farm2.wdap.convertor.impl.CopyToFileModelConvertor', 'RESOURCE_FILE', 'NONE_FILE', '拷贝文件到HTML模型', '1', '', '[{"field":"key","note":"将文件拷贝到目标模型:,文本文件:TEXT_FILE,缩略图文件夹:THUMBNAIL_FILE,Mp3音频文件:AUDIO_MP3_FILE,原始文件:RESOURCE_FILE,未知文件:NONE_FILE,Mp4视频文件:VIDEO_MP4_FILE,HTML文件:HTML_FILE,图片册文件夹:IMGS_FILE,pdf文件:PDF_FILE","title":"目标文件类型","value":"HTML_FILE"}]'),
	('86ea6cce55ef4cc8bff12a56c3acc7a2', 'org.farm2.wdap.convertor.impl.ZipToTextConvertor', 'RESOURCE_FILE', 'TEXT_FILE', 'Zip转目录结构文本', '1', '', '[{"field":"ENCODE","note":"GBK或UTF-8","title":"编码","value":"gbk"}]'),
	('9c7c4c9be691445b909e75b9720dc1bb', 'org.farm2.wdap.convertor.impl.CopyToFileModelConvertor', 'RESOURCE_FILE', 'NONE_FILE', '拷贝文件到MP4模型', '1', '', '[{"field":"key","note":"将文件拷贝到目标模型:,文本文件:TEXT_FILE,缩略图文件夹:THUMBNAIL_FILE,原始文件:RESOURCE_FILE,未知文件:NONE_FILE,Mp4视频文件:VIDEO_MP4_FILE,图片册文件夹:IMGS_FILE,pdf文件:PDF_FILE","title":"目标文件类型","value":"VIDEO_MP4_FILE"}]'),
	('b8f536badfda4d3cb8f1d18636548a62', 'org.farm2.wdap.convertor.impl.TextSegmentConvertor', 'TEXT_FILE', 'SEGMENT_FILE', '文本分段', '1', '', '[{"field":"size","note":"每段最大长度100到300之间","title":"段落长度","value":"100"}]'),
	('bcdd5116065a4090bc99713aae04a1db', 'org.farm2.wdap.convertor.impl.PdfToTextConvertor', 'PDF_FILE', 'TEXT_FILE', 'PDF(模型)转文本', '1', '', '[]'),
	('bfea31f696cd4152a5350c9db0dcfbdc', 'org.farm2.wdap.convertor.impl.ImgThumbnailConvertor', 'RESOURCE_FILE', 'THUMBNAIL_FILE', '生成缩略图', '1', '', NULL),
	('c0b22b6b137940f79f597bc43d73ff52', 'org.farm2.wdap.convertor.impl.CopyToFileModelConvertor', 'RESOURCE_FILE', 'NONE_FILE', '拷贝文件到Mp3模型', '1', '', '[{"field":"key","note":"将文件拷贝到目标模型:,文本文件:TEXT_FILE,缩略图文件夹:THUMBNAIL_FILE,Mp3音频文件:AUDIO_MP3_FILE,原始文件:RESOURCE_FILE,未知文件:NONE_FILE,Mp4视频文件:VIDEO_MP4_FILE,图片册文件夹:IMGS_FILE,pdf文件:PDF_FILE","title":"目标文件类型","value":"AUDIO_MP3_FILE"}]'),
	('df5a9178578f4c93b98108520b80af35', 'org.farm2.wdap.convertor.impl.VideoToMp4Convertor', 'RESOURCE_FILE', 'VIDEO_MP4_FILE', '视频文件转换为Mp4', '1', '', '[{"field":"audio-codec","note":"可以选择音频编解码器，例如AAC","title":"音频编解码器","value":"AAC"},{"field":"video-codec","note":"设置视频编解码器为libx264（即H.264）","title":"视频编解码器","value":"libx264"},{"field":"video-bit-rate","note":"设置比特率，这里是2.5 Mbps","title":"视频比特率","value":"2500000"},{"field":"video-frame-rate","note":"设置帧率，这里是30 fps","title":"帧率","value":"30"}]'),
	('e10db2c914184d6a9479cd8d818df48e', 'org.farm2.wdap.convertor.impl.PdfToImgConvertor', 'PDF_FILE', 'IMGS_FILE', 'PDF转图片', '1', '', '[{"field":"dpi","note":"每英寸内包含的点数,打印所需的PDF文件通常需要较高DPI（如300 DPI或更高）而用于屏幕显示的PDF文件，72 DPI或96 DPI往往就足够了","title":"转换DPI","value":"96"},{"field":"img-max-width","note":"图片册左侧缩略图大小，默认150","title":"缩略图宽度","value":"150"},{"field":"img-min-width","note":"图片册右侧详情图大小，默认1000","title":"详情图宽度","value":"1000"}]'),
	('f4d6c71b9dd04f558c816a4ef2def619', 'org.farm2.wdap.convertor.impl.TextEncodeConvertor', 'RESOURCE_FILE', 'TEXT_FILE', '编码文本文件', '1', '', NULL);
/*!40000 ALTER TABLE `wdap2_convertor` ENABLE KEYS */;

-- 导出  表 os-whrs.wdap2_extendfile 结构
CREATE TABLE IF NOT EXISTS `wdap2_extendfile` (
  `ID` varchar(32) NOT NULL,
  `TASKID` varchar(32) DEFAULT NULL,
  `FILEID` varchar(32) NOT NULL,
  `FILEMODEL` varchar(32) NOT NULL COMMENT '原文件，图片组，缩略图，PDf等',
  `VIEWIS` varchar(1) NOT NULL COMMENT '1可以0禁用',
  `STATE` varchar(2) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  `RESOURCEKEY` varchar(32) NOT NULL,
  `SERVERID` varchar(32) NOT NULL COMMENT '方便集群部署',
  `CTIME` varchar(16) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.wdap2_extendfile 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `wdap2_extendfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `wdap2_extendfile` ENABLE KEYS */;

-- 导出  表 os-whrs.wdap2_flow 结构
CREATE TABLE IF NOT EXISTS `wdap2_flow` (
  `ID` varchar(32) NOT NULL,
  `CTIME` varchar(14) NOT NULL,
  `ETIME` varchar(14) NOT NULL,
  `EUSERKEY` varchar(32) NOT NULL,
  `CUSERKEY` varchar(32) NOT NULL,
  `STATE` varchar(2) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  `NAME` varchar(256) NOT NULL,
  `MODELKEYS` varchar(512) NOT NULL,
  `SIZEMIN` int(11) NOT NULL,
  `SIZEMAX` int(11) NOT NULL,
  `EXNAME` varchar(512) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.wdap2_flow 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `wdap2_flow` DISABLE KEYS */;
INSERT INTO `wdap2_flow` (`ID`, `CTIME`, `ETIME`, `EUSERKEY`, `CUSERKEY`, `STATE`, `NOTE`, `NAME`, `MODELKEYS`, `SIZEMIN`, `SIZEMAX`, `EXNAME`) VALUES
	('14f6f1c79dfd4fa99ba31ca8ec68e79c', '20250128223545', '20250130230945', 'sysadmin', 'sysadmin', '1', '', 'Office文件转换-Pdf', '', 1, 100000000, 'doc,docx,ppt,pptx'),
	('1542dd09278b4a5e97e8bf4fbd074c42', '20250130131131', '20250130152528', 'sysadmin', 'sysadmin', '1', '', 'Mp4文件转Mp4', '', 1, 508807312, 'mp4'),
	('33fe7edcea544910abbcbd17d0aeb955', '20250208223307', '20250208223307', 'sysadmin', 'sysadmin', '1', '', 'Html文件转Html', '', 1, 20971521, 'html'),
	('4cb439730d154336a9ca453729d6f0cd', '20250128170428', '20250210184023', 'sysadmin', 'sysadmin', '1', '', '压缩文件解析目录结构', '', 1, 1100000000, 'zip'),
	('61a6862fe1d84f8383c93c8bbdf95d96', '20250130152516', '20250130152516', 'sysadmin', 'sysadmin', '1', '', 'Mp3文件转Mp3', '', 1, 100000000, 'mp3'),
	('7561be29bfc24e3cb52b0f2b1e3440ef', '20250126210916', '20250126210916', 'sysadmin', 'sysadmin', '1', '', 'PDF文件转图片册', '', 1, 105088769, 'pdf'),
	('7f99b68bfe8241dab2dbe5b0bf97cb11', '20250126104742', '20250126191706', 'sysadmin', 'sysadmin', '1', '', '编码文本文件', '', 1, 10562816, 'txt,java'),
	('a5fca86e033c41c1a4f43ee4aab9724f', '20250124170518', '20250125162243', 'sysadmin', 'sysadmin', '1', '', '预览图生成', '', 1, 838860900, 'png,jpg,jpeg'),
	('afbd297cebf8409498e9c1ec7fe7d973', '20250130230504', '20250201114942', 'sysadmin', 'sysadmin', '1', '', 'Office文件转换-HTML', '', 1, 11048577, 'xls,xlsx');
/*!40000 ALTER TABLE `wdap2_flow` ENABLE KEYS */;

-- 导出  表 os-whrs.wdap2_flow_link 结构
CREATE TABLE IF NOT EXISTS `wdap2_flow_link` (
  `ID` varchar(32) NOT NULL,
  `PCONTENT` varchar(128) DEFAULT NULL,
  `SNODEID` varchar(32) NOT NULL,
  `TNODEID` varchar(32) NOT NULL,
  `FLOWID` varchar(32) NOT NULL,
  `EXPRESSION` varchar(1024) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.wdap2_flow_link 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `wdap2_flow_link` DISABLE KEYS */;
INSERT INTO `wdap2_flow_link` (`ID`, `PCONTENT`, `SNODEID`, `TNODEID`, `FLOWID`, `EXPRESSION`) VALUES
	('020978b9e32945bb96a82f59584fa8db', NULL, '010fc25aa1c14516887baee156a54713', 'd81ffc2d0a104c83a67eeab3a852543d', '33fe7edcea544910abbcbd17d0aeb955', 'true'),
	('09e2e42387fc4e48a0aa7b8d220733b1', NULL, '6386b31974064720a6a4a8fcf60ba6b8', 'END', '33fe7edcea544910abbcbd17d0aeb955', 'true'),
	('0a198305b0cc492cba06b05800239d77', NULL, 'START', '6889dc453ef9431fa1d1777e0f617627', 'bd036db8b992463fb76c73417316e885', 'true'),
	('12d70f2492154d4580d2d25c56a53bba', NULL, '88ca1bc27ea64aa28345ddbb1156a4c6', 'START', 'e4271d38f719424e9b15ff43ac4a4750', 'true'),
	('13478c4e0ba34f4faba4cb3728a59882', NULL, '98897feb1ed841aba97c6b48eeff3f9c', '11b58a62910e4a48a6ed16c1b1766cc2', '7561be29bfc24e3cb52b0f2b1e3440ef', 'true'),
	('161c75cf916740cf849b64f6cd3d5d54', NULL, '9cf3e18991cc44d79a4c7d31f9c8fdcf', '89d82e35b35446d585c211d2a7395ff2', 'afbd297cebf8409498e9c1ec7fe7d973', 'true'),
	('2b35e17f21734336acdde3bd657c51dd', NULL, '0f741bfe511e470d9252f09504167bda', 'e1d7951a6a804dae9015a2e985d6676f', '7561be29bfc24e3cb52b0f2b1e3440ef', 'true'),
	('30b30b046b424f2792456a4956364c78', NULL, '7af710e7b896446889d165a8c59a7851', 'END', '7f99b68bfe8241dab2dbe5b0bf97cb11', 'true'),
	('33375e2e9300444f9fa8b6251e4cdead', NULL, 'afe5bb3dd4cb41478d5e34095f92eaed', '8c1fb926a2f94e5086fe58705fe8c814', '6075a80f909f4d09909f62d26e57723c', 'true'),
	('34e43fcd8c084f9c8a8b4095bd88b73a', NULL, 'START', '69e1e06f92c546a3b1c03c3da23db6a5', '6075a80f909f4d09909f62d26e57723c', 'true'),
	('39dbff26351a47538dfbafdf75fd780f', NULL, '2d7159bf58e7449286b3b30b24d67a71', 'END', 'e4271d38f719424e9b15ff43ac4a4750', 'true'),
	('3c0ce26984ea4c15955f4e11b6d2aeaf', NULL, 'e1d7951a6a804dae9015a2e985d6676f', 'END', '7561be29bfc24e3cb52b0f2b1e3440ef', 'true'),
	('41cafb6e74184c598a5ab10b4ebe4b5f', NULL, '046a8883f0944fd388b0f88d5aba9210', 'END', 'e4271d38f719424e9b15ff43ac4a4750', 'true'),
	('45114ba4a4214e3eba2cd721f81ada23', NULL, 'START', '8c1fb926a2f94e5086fe58705fe8c814', '1993fbe88a5a4d1c975627c630c589a7', 'true'),
	('49e3afa41d2b447eafee40d12f2d150e', NULL, 'START', 'a40afb2e479145ce8277046cd86d411e', 'a5fca86e033c41c1a4f43ee4aab9724f', 'true'),
	('4ef7c5491f174ea99d15a19a0eae232c', NULL, '11b58a62910e4a48a6ed16c1b1766cc2', '0f741bfe511e470d9252f09504167bda', '7561be29bfc24e3cb52b0f2b1e3440ef', 'true'),
	('520dbc53e0f6446dae5e1f0985bffa75', NULL, '8c1fb926a2f94e5086fe58705fe8c814', 'a1cfe72663304f0bb7bb3409375419ed', '1993fbe88a5a4d1c975627c630c589a7', 'true'),
	('55da6a8fd9694bb090ee6cd142f59ec3', NULL, 'START', '010fc25aa1c14516887baee156a54713', '33fe7edcea544910abbcbd17d0aeb955', 'true'),
	('5bca7d4a6ce64b4da0d82331a469108d', NULL, 'a1cfe72663304f0bb7bb3409375419ed', 'END', '1993fbe88a5a4d1c975627c630c589a7', 'true'),
	('613d6ebbaa5b404195414fd1484e78cd', NULL, 'START', 'e185755ff1d04a629eb6ce70e3a00938', '1542dd09278b4a5e97e8bf4fbd074c42', 'true'),
	('62ba40650ce0434a807a9ae59f229b2d', NULL, 'START', '24015910007d4e46b4a59a33c8bd2852', '15b523f6074e463dad324a2b3b54ed67', 'true'),
	('66c3453cd45845ec89e40f2f13d39a5d', NULL, '7317c0d60e7b4442bcec829637b17ca0', '6e12958ed5af437981e49a634ffe2421', '14f6f1c79dfd4fa99ba31ca8ec68e79c', 'true'),
	('682a13fa4c1a4dd2b2edd0804217e452', NULL, 'a40afb2e479145ce8277046cd86d411e', 'END', 'a5fca86e033c41c1a4f43ee4aab9724f', 'true'),
	('6edeb93b51dc49a1986fb7a14c058f84', NULL, '719cfa57dcb1455ba7a3bad9839bfb51', '9cf3e18991cc44d79a4c7d31f9c8fdcf', 'afbd297cebf8409498e9c1ec7fe7d973', 'true'),
	('70dff879634645be9652f90e9e4bf8ce', NULL, '4c0be8d4d34d4d838bf2dd973fd64ad1', 'END', '14f6f1c79dfd4fa99ba31ca8ec68e79c', 'true'),
	('712d7db2448a4da29c0747a78f8242e6', NULL, 'START', '719cfa57dcb1455ba7a3bad9839bfb51', 'afbd297cebf8409498e9c1ec7fe7d973', 'true'),
	('7333016963044defb55e3440fb776155', NULL, 'START', '2c13727f769d4e64a2b7c8d9330f6c40', '61a6862fe1d84f8383c93c8bbdf95d96', 'true'),
	('761dc5df3edc4601904bd0e781c460da', NULL, '24015910007d4e46b4a59a33c8bd2852', '69e1e06f92c546a3b1c03c3da23db6a5', '15b523f6074e463dad324a2b3b54ed67', 'true'),
	('7661a5d4a6ca418b9e7e3e3be3e3fa9e', NULL, 'START', 'cc78c725fc3342219840cfaa57b5a152', '4cb439730d154336a9ca453729d6f0cd', 'true'),
	('7c61664b404e433eb34084d750c0aec2', NULL, '2c13727f769d4e64a2b7c8d9330f6c40', 'END', '61a6862fe1d84f8383c93c8bbdf95d96', 'true'),
	('84b71c3d57d74ff68c05103aee7526ff', NULL, '69e1e06f92c546a3b1c03c3da23db6a5', 'END', '6075a80f909f4d09909f62d26e57723c', 'true'),
	('8c4f9d4c6205401fb1376716b30dcc50', NULL, 'START', 'afe5bb3dd4cb41478d5e34095f92eaed', '6075a80f909f4d09909f62d26e57723c', 'true'),
	('8da7e6a915f64fc6b12ee59768aefff9', NULL, 'START', '2d7159bf58e7449286b3b30b24d67a71', 'e4271d38f719424e9b15ff43ac4a4750', 'true'),
	('8fb3a90ed1f64f3b859fdae7d87088c2', NULL, '5ff2a6ca4252451b8ce223a890c48ca3', 'END', 'e4271d38f719424e9b15ff43ac4a4750', 'true'),
	('98c473d8c3f049809cae0c753c5fed00', NULL, 'cc78c725fc3342219840cfaa57b5a152', 'END', '4cb439730d154336a9ca453729d6f0cd', 'true'),
	('ab7f9a9ca3774efcb2e9328d825df5db', NULL, 'END', '5ff2a6ca4252451b8ce223a890c48ca3', 'e4271d38f719424e9b15ff43ac4a4750', 'true'),
	('ad6cd38bc3c748d7a59692740dbbaeb2', NULL, 'd81ffc2d0a104c83a67eeab3a852543d', '6386b31974064720a6a4a8fcf60ba6b8', '33fe7edcea544910abbcbd17d0aeb955', 'true'),
	('ad77600b375f4016896b990d4cf57f4f', NULL, 'START', '98897feb1ed841aba97c6b48eeff3f9c', '7561be29bfc24e3cb52b0f2b1e3440ef', 'true'),
	('b8f90a42f74b4971b48e3959ed48f849', NULL, '89d82e35b35446d585c211d2a7395ff2', 'END', 'afbd297cebf8409498e9c1ec7fe7d973', 'true'),
	('c275d44fd5434909ab3da14e3684da06', NULL, '1d83e4aa64a647aea62439a922f7237c', 'END', '1993fbe88a5a4d1c975627c630c589a7', 'true'),
	('c569e624e09f4c20822504d4bfa2b25c', NULL, '3a8969ea18b843ddab709818a2e7a988', '7317c0d60e7b4442bcec829637b17ca0', '14f6f1c79dfd4fa99ba31ca8ec68e79c', 'true'),
	('cb7ec008b7744f43b3a212aa0df4dd3a', NULL, 'START', '3a8969ea18b843ddab709818a2e7a988', '14f6f1c79dfd4fa99ba31ca8ec68e79c', 'true'),
	('d4cf91aa104c4e3ea7f566c648aa307d', NULL, '6e12958ed5af437981e49a634ffe2421', '4c0be8d4d34d4d838bf2dd973fd64ad1', '14f6f1c79dfd4fa99ba31ca8ec68e79c', 'true'),
	('d7359d3dd4d14fe8a880a789707e7648', NULL, 'START', '7af710e7b896446889d165a8c59a7851', '7f99b68bfe8241dab2dbe5b0bf97cb11', 'true'),
	('d743a5e779804718b6225fb13779261d', NULL, 'e185755ff1d04a629eb6ce70e3a00938', 'END', '1542dd09278b4a5e97e8bf4fbd074c42', 'true'),
	('e02ea2c5bd2a40a0b2b7458f4ef53a76', NULL, '6889dc453ef9431fa1d1777e0f617627', 'END', 'bd036db8b992463fb76c73417316e885', 'true'),
	('e53e14d5733447c3a66c1e8ed8ad3577', NULL, '8c1fb926a2f94e5086fe58705fe8c814', '1d83e4aa64a647aea62439a922f7237c', '1993fbe88a5a4d1c975627c630c589a7', 'true');
/*!40000 ALTER TABLE `wdap2_flow_link` ENABLE KEYS */;

-- 导出  表 os-whrs.wdap2_flow_node 结构
CREATE TABLE IF NOT EXISTS `wdap2_flow_node` (
  `ID` varchar(32) NOT NULL,
  `PCONTENT` varchar(128) DEFAULT NULL,
  `MODEL` varchar(128) NOT NULL,
  `RID` varchar(32) DEFAULT NULL,
  `FLOWID` varchar(32) NOT NULL,
  `TIMEOUT` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.wdap2_flow_node 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `wdap2_flow_node` DISABLE KEYS */;
INSERT INTO `wdap2_flow_node` (`ID`, `PCONTENT`, `MODEL`, `RID`, `FLOWID`, `TIMEOUT`) VALUES
	('010fc25aa1c14516887baee156a54713', NULL, 'convertor', '62a20675cd19416a8d2d32929eac032e', '33fe7edcea544910abbcbd17d0aeb955', NULL),
	('046a8883f0944fd388b0f88d5aba9210', NULL, 'none', NULL, 'e4271d38f719424e9b15ff43ac4a4750', NULL),
	('0f741bfe511e470d9252f09504167bda', NULL, 'convertor', '45c0c5d0a24641dc9671dd9698f7c261', '7561be29bfc24e3cb52b0f2b1e3440ef', NULL),
	('11b58a62910e4a48a6ed16c1b1766cc2', NULL, 'convertor', '19faf5f6fef24fa0a281cb0fd81119ff', '7561be29bfc24e3cb52b0f2b1e3440ef', NULL),
	('1d83e4aa64a647aea62439a922f7237c', NULL, 'none', NULL, '1993fbe88a5a4d1c975627c630c589a7', NULL),
	('24015910007d4e46b4a59a33c8bd2852', NULL, 'none', NULL, '15b523f6074e463dad324a2b3b54ed67', NULL),
	('2c13727f769d4e64a2b7c8d9330f6c40', NULL, 'convertor', 'c0b22b6b137940f79f597bc43d73ff52', '61a6862fe1d84f8383c93c8bbdf95d96', NULL),
	('2d7159bf58e7449286b3b30b24d67a71', NULL, 'none', NULL, 'e4271d38f719424e9b15ff43ac4a4750', NULL),
	('3a8969ea18b843ddab709818a2e7a988', NULL, 'convertor', '4a0b44df7f4644489e6de36966ce847b', '14f6f1c79dfd4fa99ba31ca8ec68e79c', NULL),
	('4c0be8d4d34d4d838bf2dd973fd64ad1', NULL, 'convertor', 'b8f536badfda4d3cb8f1d18636548a62', '14f6f1c79dfd4fa99ba31ca8ec68e79c', NULL),
	('5ff2a6ca4252451b8ce223a890c48ca3', NULL, 'none', NULL, 'e4271d38f719424e9b15ff43ac4a4750', NULL),
	('6386b31974064720a6a4a8fcf60ba6b8', NULL, 'convertor', 'b8f536badfda4d3cb8f1d18636548a62', '33fe7edcea544910abbcbd17d0aeb955', NULL),
	('6889dc453ef9431fa1d1777e0f617627', NULL, 'none', NULL, 'bd036db8b992463fb76c73417316e885', NULL),
	('69e1e06f92c546a3b1c03c3da23db6a5', NULL, 'none', NULL, '6075a80f909f4d09909f62d26e57723c', NULL),
	('6e12958ed5af437981e49a634ffe2421', NULL, 'convertor', 'bcdd5116065a4090bc99713aae04a1db', '14f6f1c79dfd4fa99ba31ca8ec68e79c', NULL),
	('719cfa57dcb1455ba7a3bad9839bfb51', NULL, 'convertor', '07823d14393341ff9704bde50a5a66c3', 'afbd297cebf8409498e9c1ec7fe7d973', NULL),
	('7317c0d60e7b4442bcec829637b17ca0', NULL, 'convertor', 'e10db2c914184d6a9479cd8d818df48e', '14f6f1c79dfd4fa99ba31ca8ec68e79c', NULL),
	('7af710e7b896446889d165a8c59a7851', NULL, 'convertor', 'f4d6c71b9dd04f558c816a4ef2def619', '7f99b68bfe8241dab2dbe5b0bf97cb11', NULL),
	('88ca1bc27ea64aa28345ddbb1156a4c6', NULL, 'none', NULL, 'e4271d38f719424e9b15ff43ac4a4750', NULL),
	('89d82e35b35446d585c211d2a7395ff2', NULL, 'convertor', 'b8f536badfda4d3cb8f1d18636548a62', 'afbd297cebf8409498e9c1ec7fe7d973', NULL),
	('8c1fb926a2f94e5086fe58705fe8c814', NULL, 'none', NULL, '1993fbe88a5a4d1c975627c630c589a7', NULL),
	('98897feb1ed841aba97c6b48eeff3f9c', NULL, 'convertor', '5a9c027028794bcf9af9758fc1bcfae9', '7561be29bfc24e3cb52b0f2b1e3440ef', NULL),
	('9cf3e18991cc44d79a4c7d31f9c8fdcf', NULL, 'convertor', '3151a415f6c6489da1fe3e561d163d8b', 'afbd297cebf8409498e9c1ec7fe7d973', NULL),
	('a1cfe72663304f0bb7bb3409375419ed', NULL, 'none', NULL, '1993fbe88a5a4d1c975627c630c589a7', NULL),
	('a40afb2e479145ce8277046cd86d411e', NULL, 'convertor', 'bfea31f696cd4152a5350c9db0dcfbdc', 'a5fca86e033c41c1a4f43ee4aab9724f', NULL),
	('afe5bb3dd4cb41478d5e34095f92eaed', NULL, 'none', NULL, '6075a80f909f4d09909f62d26e57723c', NULL),
	('cc78c725fc3342219840cfaa57b5a152', NULL, 'convertor', '86ea6cce55ef4cc8bff12a56c3acc7a2', '4cb439730d154336a9ca453729d6f0cd', NULL),
	('d81ffc2d0a104c83a67eeab3a852543d', NULL, 'convertor', '3151a415f6c6489da1fe3e561d163d8b', '33fe7edcea544910abbcbd17d0aeb955', NULL),
	('e185755ff1d04a629eb6ce70e3a00938', NULL, 'convertor', '9c7c4c9be691445b909e75b9720dc1bb', '1542dd09278b4a5e97e8bf4fbd074c42', NULL),
	('e1d7951a6a804dae9015a2e985d6676f', NULL, 'convertor', 'b8f536badfda4d3cb8f1d18636548a62', '7561be29bfc24e3cb52b0f2b1e3440ef', NULL);
/*!40000 ALTER TABLE `wdap2_flow_node` ENABLE KEYS */;

-- 导出  表 os-whrs.wdap2_task 结构
CREATE TABLE IF NOT EXISTS `wdap2_task` (
  `ID` varchar(32) NOT NULL,
  `FILEID` varchar(32) NOT NULL,
  `PSTATE` varchar(2) NOT NULL COMMENT '等待，转换中，完成，失败',
  `LOGJSON` text NOT NULL,
  `ETIME` varchar(14) NOT NULL,
  `CTIME` varchar(14) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  `SERVERID` varchar(32) NOT NULL COMMENT '方便集群部署',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.wdap2_task 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `wdap2_task` DISABLE KEYS */;
INSERT INTO `wdap2_task` (`ID`, `FILEID`, `PSTATE`, `LOGJSON`, `ETIME`, `CTIME`, `NOTE`, `SERVERID`) VALUES
	('019ba14811237eb9b81b2d6e02137097', '019ba147f40d7694947a21bbada94a28', '2', '[\n	"INFO:2026-01-09 13:43:36/全部转换完成"\n]', '20260109134336', '20260109134333', NULL, 'farm2-01');
/*!40000 ALTER TABLE `wdap2_task` ENABLE KEYS */;

-- 导出  表 os-whrs.whrs_attendance_summary 结构
CREATE TABLE IF NOT EXISTS `whrs_attendance_summary` (
  `ID` varchar(32) NOT NULL,
  `CTIME` varchar(14) NOT NULL,
  `USERKEY` varchar(64) DEFAULT NULL,
  `ATTENDANCETIME` varchar(14) NOT NULL,
  `WORKHOURS` float NOT NULL,
  `LATEM` int(11) DEFAULT NULL,
  `EARLYM` int(11) DEFAULT NULL,
  `OVERTIMEM` int(11) DEFAULT NULL,
  `ABSENTIS` varchar(1) DEFAULT NULL,
  `EXCEPTIONTYPE` varchar(32) DEFAULT NULL,
  `STATE` varchar(32) DEFAULT NULL,
  `EXEMPTNOTE` varchar(256) DEFAULT NULL,
  `WORKING` varchar(1) DEFAULT NULL,
  `BACKUP` varchar(1) DEFAULT NULL,
  `SSTIME` varchar(14) DEFAULT NULL,
  `SXTIME` varchar(14) DEFAULT NULL,
  `XSTIME` varchar(14) DEFAULT NULL,
  `XXTIME` varchar(14) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.whrs_attendance_summary 的数据：~13 rows (大约)
/*!40000 ALTER TABLE `whrs_attendance_summary` DISABLE KEYS */;
INSERT INTO `whrs_attendance_summary` (`ID`, `CTIME`, `USERKEY`, `ATTENDANCETIME`, `WORKHOURS`, `LATEM`, `EARLYM`, `OVERTIMEM`, `ABSENTIS`, `EXCEPTIONTYPE`, `STATE`, `EXEMPTNOTE`, `WORKING`, `BACKUP`, `SSTIME`, `SXTIME`, `XSTIME`, `XXTIME`) VALUES
	('019b92f6e00d7438b820c362b9e7bc39', '20260106190011', 'testadmin', '20260106', 0, 0, 0, NULL, '1', '0', '1', NULL, NULL, NULL, '20260106000000', '20260106121704', '20260106133000', '20260106190011'),
	('019b930462457abbba54c38712cfa47f', '20260106191456', 'testadmin', '20260105', 0, -1, -1, NULL, '1', '3', '1', NULL, NULL, NULL, '20260105000400', '20260105120400', NULL, NULL),
	('019b93183f25721da03fec955a9e251b', '20260106193638', 'testadmin', '20260103', 0, -1, -2, NULL, '1', '3', '1', NULL, NULL, NULL, '20260103000000', NULL, NULL, NULL),
	('019b933cf4f3706eb226e51d4b58663c', '20260106201644', 'testadmin', '20260104', 0, -1, -2, NULL, '1', '3', '1', NULL, NULL, NULL, '20260104000000', NULL, NULL, NULL),
	('019b93668b2e79a992d246343cd94900', '20260106210209', 'testadmin', '20260101', 0, -1, -1, NULL, '1', '3', '1', NULL, NULL, NULL, '20260101070000', '20260101121500', NULL, NULL),
	('019b9368479870a0864624576a83a493', '20260106210403', 'systest', '20260103', 0, 30, 0, NULL, '1', '1', '1', NULL, NULL, NULL, '20260103083000', '20260103130000', '20260103131500', '20260103181500'),
	('019b9369812571658bad4404128edbd6', '20260106210523', 'systest', '20260104', 0, 60, 30, NULL, '1', '12', '1', NULL, NULL, NULL, '20260104083000', '20260104114500', '20260104143000', '20260104174500'),
	('019b937ef6da7b56851642806a0e5683', '20260106212850', 'systest', '20260102', 0, -2, -2, NULL, '0', '3', '2', '阿斯顿', NULL, NULL, NULL, NULL, NULL, NULL),
	('019b938ca3027628aba721458f5a4684', '20260106214346', 'sysadmin', '20260101', 0, -1, -2, NULL, '1', '4', '2', '阿斯顿', NULL, NULL, '20260101073000', NULL, NULL, NULL),
	('019b9d5979e37a01ad36ac8524e307a0', '20260108192405', 'zhangwuji', '20260101', 0, NULL, NULL, NULL, NULL, '0', '2', '得到', NULL, NULL, NULL, NULL, NULL, NULL),
	('019b9df532e4727a8ea481faa9dadeea', '20260108221410', 'sysadmin', '20260108', 0, -2, -1, NULL, '1', '3', '0', NULL, NULL, NULL, NULL, NULL, NULL, '20260108221713'),
	('019b9df7e66474e091c898a6316465ab', '20260108221707', 'zhangwuji', '20260102', 0, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, '20260102073000', NULL, NULL, NULL),
	('019b9df895c473dea9344eb69f0501f0', '20260108221752', 'HDF', '20260108', 0, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, '20260108221752');
/*!40000 ALTER TABLE `whrs_attendance_summary` ENABLE KEYS */;

-- 导出  表 os-whrs.whrs_salary_template 结构
CREATE TABLE IF NOT EXISTS `whrs_salary_template` (
  `ID` varchar(32) NOT NULL,
  `CUSER` varchar(32) DEFAULT NULL,
  `CTIME` varchar(14) NOT NULL,
  `STATE` varchar(2) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  `NAME` varchar(256) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.whrs_salary_template 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `whrs_salary_template` DISABLE KEYS */;
INSERT INTO `whrs_salary_template` (`ID`, `CUSER`, `CTIME`, `STATE`, `NOTE`, `NAME`) VALUES
	('019b964f15fc78738f9083572c7d5a75', 'sysadmin', '20260107103524', '1', '实发工资 = 应发工资 - 社保个人部分 - 公积金个人部分 - 个人所得税 - 其他扣款', '简化计算模板');
/*!40000 ALTER TABLE `whrs_salary_template` ENABLE KEYS */;

-- 导出  表 os-whrs.whrs_salary_template_formula 结构
CREATE TABLE IF NOT EXISTS `whrs_salary_template_formula` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(256) DEFAULT NULL,
  `RULEVAL` varchar(512) DEFAULT NULL,
  `VALNAME` varchar(32) DEFAULT NULL,
  `VALCODE` varchar(32) DEFAULT NULL,
  `TEMPLATEID` varchar(32) DEFAULT NULL,
  `SHOWMODEL` varchar(1) DEFAULT NULL,
  `SORTCODE` int(11) DEFAULT NULL,
  `STEPCODE` int(11) DEFAULT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.whrs_salary_template_formula 的数据：~8 rows (大约)
/*!40000 ALTER TABLE `whrs_salary_template_formula` DISABLE KEYS */;
INSERT INTO `whrs_salary_template_formula` (`ID`, `NAME`, `RULEVAL`, `VALNAME`, `VALCODE`, `TEMPLATEID`, `SHOWMODEL`, `SORTCODE`, `STEPCODE`, `NOTE`) VALUES
	('019b9727f52b7bdab24e79d75bbfd041', '', 'JBGZ_SR+JXGZ_SR+JJ_SR+JT_SR+JBF_SR+QQJ_SR', '', 'GROSS_SALARY_SYS', '019b964f15fc78738f9083572c7d5a75', '', 1, 1, '应得收入=收集员工当月所有项'),
	('019b9781fcfd7a16854eaf6767756f39', '', 'LAST_YEAR_M_SALA>SB_MAX?SB_MAX:(LAST_YEAR_M_SALA<SB_MIN?SBMIN:LAST_YEAR_M_SALA)', '', 'LAST_YEAR_M_SALA', '019b964f15fc78738f9083572c7d5a75', '', 1, 2, '上年度社保工资-调整（若低于当地下限，按 下限；若高于上限，按 上限）'),
	('019b9785d8487767892c096c17c1aecd', '', 'LAST_YEAR_M_SALA*YLBX_GR+LAST_YEAR_M_SALA*YILIAOBX_GR+LAST_YEAR_M_SALA*SYBX_GR', '', 'SB_GRJF', '019b964f15fc78738f9083572c7d5a75', '', 1, 3, '社保个人缴费部分'),
	('019b978a4ae97e4c90cb35ee6615f913', '', 'GJJ_BL*LAST_YEAR_M_SALA', '', 'GJJ_GR', '019b964f15fc78738f9083572c7d5a75', '', 1, 4, '计算住房公积金个人部分'),
	('019b978ebd7d7b539c5b140f38b7df2a', '', 'GROSS_SALARY_SYS-GJJ_GR-SB_GRJF-ZXFJKC-GSQZD', '', 'YNSSDE', '019b964f15fc78738f9083572c7d5a75', '', 1, 5, '应纳税所得额 = \n    应发工资 \n  − 社保个人部分 \n  − 公积金个人部分 \n  − 起征点（5,000 元/月） \n  − 专项附加扣除'),
	('019b9796a02d72668daa94ccc4e90e21', '', 'YNSSDE <= 0 ? 0 :\nYNSSDE <= 3000 ? YNSSDE * 0.03 :\nYNSSDE <= 12000 ? YNSSDE * 0.10 - 210 :\nYNSSDE <= 25000 ? YNSSDE * 0.20 - 1410 :\nYNSSDE <= 35000 ? YNSSDE * 0.25 - 2660 :\nYNSSDE <= 55000 ? YNSSDE * 0.30 - 4410 :\nYNSSDE <= 80000 ? YNSSDE * 0.35 - 7160 :\nYNSSDE * 0.45 - 15160', '', 'INCOME_TAX_SYS', '019b964f15fc78738f9083572c7d5a75', '', 1, 6, '个税'),
	('019b979d332c71f1807d4961abe4d462', '', 'INCOME_TAX_SYS+SB_GRJF+GJJ_GR-QTKK_DD', '', 'KOUKUAN', '019b964f15fc78738f9083572c7d5a75', '', 1, 7, '总扣款 = 社保个人 + 公积金个人 + 个税 + 其他扣款'),
	('019b979f3812776a85fcc6fe79c890a0', '', 'GROSS_SALARY_SYS-KOUKUAN', '', 'NET_SALARY_SYS', '019b964f15fc78738f9083572c7d5a75', '', 1, 8, '实发工资 = 应发工资 − 总扣款');
/*!40000 ALTER TABLE `whrs_salary_template_formula` ENABLE KEYS */;

-- 导出  表 os-whrs.whrs_salary_template_item 结构
CREATE TABLE IF NOT EXISTS `whrs_salary_template_item` (
  `ID` varchar(32) NOT NULL,
  `NOTE` varchar(512) DEFAULT NULL,
  `NAME` varchar(256) NOT NULL,
  `DEFAULTVAL` decimal(18,2) NOT NULL,
  `KEYCODE` varchar(32) NOT NULL,
  `COMPONENTTYPE` varchar(32) DEFAULT NULL COMMENT '收入 / 扣款 / 税费 / 补贴',
  `SOURCEMODEL` varchar(32) DEFAULT NULL COMMENT 'INPUT：输入',
  `USEROVER` varchar(1) NOT NULL,
  `SORTCODE` int(11) NOT NULL,
  `TEMPLATEID` varchar(32) NOT NULL,
  `SHOWMODEL` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.whrs_salary_template_item 的数据：~25 rows (大约)
/*!40000 ALTER TABLE `whrs_salary_template_item` DISABLE KEYS */;
INSERT INTO `whrs_salary_template_item` (`ID`, `NOTE`, `NAME`, `DEFAULTVAL`, `KEYCODE`, `COMPONENTTYPE`, `SOURCEMODEL`, `USEROVER`, `SORTCODE`, `TEMPLATEID`, `SHOWMODEL`) VALUES
	('019b967f94d078dcac7f055df7de84d8', '', '基本工资(高级)', 3600.00, 'BASE1', '收入', '', '1', 1, '019b967890d67ae5a2b8024c5b317587', ''),
	('019b968e6694718aa3c16f7f48d87b78', '', '绩效工资', 1200.00, 'BASE2', '收入', '', '0', 2, '019b967890d67ae5a2b8024c5b317587', ''),
	('019b974143f2750fb898c67d9ebd2298', '', '基本工资', 3500.00, 'JBGZ_SR', '收入', '', '1', 1, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b97417d17799bb4a3b262fb279d6e', '', '绩效工资', 1500.00, 'JXGZ_SR', '收入', '', '1', 2, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b9741baca7e6898bdaf2a899ff305', '', '奖金', 0.00, 'JJ_SR', '收入', '', '2', 3, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b97420fa07dd49e2f1d73e55b89c5', '', '津贴补贴', 800.00, 'JT_SR', '收入', '', '1', 4, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b974292c27e558afc4eb0666c9603', '', '加班费', 0.00, 'JBF_SR', '收入', '', '2', 5, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b9743042d7d6cbb6bd6702e7abcca', '', '全勤奖', 0.00, 'QQJ_SR', '收入', '', '2', 6, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b9774db717a62953a45d799b47f4f', '', '应得收入', 0.00, 'GROSS_SALARY_SYS', '其他', '', '0', 1, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b977724b479be88d1246e21fc3212', '', '上年度平均月工资', 3500.00, 'LAST_YEAR_M_SALA', '其他', '', '1', 1, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b977f74a97345998ac07036e1faf5', '', '最低缴费基数（社保）', 3000.00, 'SB_MIN', '社保', '', '0', 1, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b977fc1ea7161b3ffb47830632c66', '', '最高缴费基数（社保）', 10000.00, 'SB_MAX', '社保', '', '0', 1, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b97834bfd72afb71926fb1f950215', '', '养老保险-个人比例', 0.08, 'YLBX_GR', '社保', '', '0', 1, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b97839e54768fbc90177e4ab74651', '', '医疗保险-个人比例', 0.02, 'YILIAOBX_GR', '社保', '', '0', 1, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b97840e1e7865a6fcfef3abff02dc', '', '失业保险-个人比例', 0.05, 'SYBX_GR', '社保', '', '0', 1, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b9784ab987f499f11249870cae5a7', '', '社保个人缴费', 0.00, 'SB_GRJF', '社保', '', '0', 1, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b9786a3e5785bb24d75cf74419488', '', '公积金个人缴费', 0.00, 'GJJ_GR', '社保', '', '0', 1, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b97892aec7fc7ab6105a3c2a019a6', '', '公积金缴费比例', 0.12, 'GJJ_BL', '社保', '', '0', 1, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b978c08177a6c977b50d1bd81090c', '', '应纳税所得额', 8000.00, 'YNSSDE', '税费', '', '3', 1, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b978d58d07febbe4e762ad96eafbf', '', '专项附加扣除', 0.00, 'ZXFJKC', '税费', '', '2', 1, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b978e21de715297b26314b2dee35f', '', '个税起征点', 5000.00, 'GSQZD', '税费', '', '0', 1, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b979672ba7cd78b9535a2352e7be3', '', '个人所得税', 5000.00, 'INCOME_TAX_SYS', '税费', '', '0', 1, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b979b2002720bb20da2233c5fafb9', '', '汇总扣款', 0.00, 'KOUKUAN', '其他', '', '3', 1, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b979c5f56783aad70afd075d46e34', '', '其他扣款（考勤|罚款等）', 0.00, 'QTKK_DD', '扣款', '', '0', 1, '019b964f15fc78738f9083572c7d5a75', ''),
	('019b979e9ea975f2a69c002aeed7e6f0', '', '实发工资', 0.00, 'NET_SALARY_SYS', '其他', '', '3', 1, '019b964f15fc78738f9083572c7d5a75', '');
/*!40000 ALTER TABLE `whrs_salary_template_item` ENABLE KEYS */;

-- 导出  表 os-whrs.whrs_salary_user 结构
CREATE TABLE IF NOT EXISTS `whrs_salary_user` (
  `ID` varchar(32) NOT NULL,
  `USERKEY` varchar(32) NOT NULL,
  `USERNAME` varchar(32) NOT NULL,
  `NOTE` varchar(256) DEFAULT NULL,
  `STATE` varchar(2) NOT NULL,
  `TEMPLATEID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.whrs_salary_user 的数据：~9 rows (大约)
/*!40000 ALTER TABLE `whrs_salary_user` DISABLE KEYS */;
INSERT INTO `whrs_salary_user` (`ID`, `USERKEY`, `USERNAME`, `NOTE`, `STATE`, `TEMPLATEID`) VALUES
	('019b9bc14601781b8be558b3f8695740', 'zhaomin', '赵敏', NULL, '1', '019b964f15fc78738f9083572c7d5a75'),
	('019b9bc2343d7d6996bcdd4e9033d79c', 'zhangwuji', '张无忌', '111', '1', '019b964f15fc78738f9083572c7d5a75'),
	('019b9c3ab9e47526b0d8ed401171e861', 'testadmin', '演示管理员', NULL, '1', '019b964f15fc78738f9083572c7d5a75'),
	('019b9c3ab9ea7cc5b9d07af7c2ddafe1', 'systest', '测试用户', NULL, '1', NULL),
	('019b9c3ab9fb76c88dd9a9408a1bcb3f', 'sysadmin', '最帅管理员', NULL, '1', NULL),
	('019b9c3aba1c77d39a0b7f7b75dae6fd', 'HDF', '海大富', NULL, '1', NULL),
	('019b9c3aba2177c5965e610143d4cd83', 'guest', 'guest', NULL, '1', NULL),
	('019b9c3aba367036ae2d2e66187651fe', 'dhwl', '大海无量', NULL, '1', NULL),
	('019b9de491b97b73aababec5c2f1a763', 'asdf', 'asdf', NULL, '1', NULL);
/*!40000 ALTER TABLE `whrs_salary_user` ENABLE KEYS */;

-- 导出  表 os-whrs.whrs_salary_user_base 结构
CREATE TABLE IF NOT EXISTS `whrs_salary_user_base` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(256) NOT NULL,
  `KEYCODE` varchar(32) NOT NULL,
  `VAL` decimal(18,2) NOT NULL,
  `USEROVER` varchar(1) NOT NULL COMMENT '1手动更新，2月更新，0不更新',
  `SHOWMODEL` varchar(1) NOT NULL,
  `USERKEY` varchar(32) NOT NULL,
  `USERNAME` varchar(32) NOT NULL,
  `SALARYTIME` varchar(8) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.whrs_salary_user_base 的数据：~32 rows (大约)
/*!40000 ALTER TABLE `whrs_salary_user_base` DISABLE KEYS */;
INSERT INTO `whrs_salary_user_base` (`ID`, `NAME`, `KEYCODE`, `VAL`, `USEROVER`, `SHOWMODEL`, `USERKEY`, `USERNAME`, `SALARYTIME`) VALUES
	('019b9cab967e7991b03165233125bffb', '基本工资', 'JBGZ_SR', 3500.00, '1', '1', 'zhangwuji', 'zhangwuji', '202601'),
	('019b9cab967f7b43a816c15e82a903db', '绩效工资', 'JXGZ_SR', 1500.00, '1', '1', 'zhangwuji', 'zhangwuji', '202601'),
	('019b9cab967f7b43a817d1ec16a2cccb', '奖金', 'JJ_SR', 0.00, '2', '1', 'zhangwuji', 'zhangwuji', '202601'),
	('019b9cab967f7b43a818af2c6c094759', '津贴补贴', 'JT_SR', 800.00, '1', '1', 'zhangwuji', 'zhangwuji', '202601'),
	('019b9cab967f7b43a819f75f6c07b713', '加班费', 'JBF_SR', 0.00, '2', '1', 'zhangwuji', 'zhangwuji', '202601'),
	('019b9cab967f7b43a81aa7acc4c66eb3', '全勤奖', 'QQJ_SR', 0.00, '2', '1', 'zhangwuji', 'zhangwuji', '202601'),
	('019b9cab96847b4ab397357602c300ea', '上年度平均月工资', 'LAST_YEAR_M_SALA', 3500.00, '1', '1', 'zhangwuji', 'zhangwuji', '202601'),
	('019b9cab96847b4ab3980cfe0274f8bf', '专项附加扣除', 'ZXFJKC', 0.00, '2', '1', 'zhangwuji', 'zhangwuji', '202601'),
	('019b9cafc2087ef9a33804f31c0b3267', '基本工资', 'JBGZ_SR', 35000.00, '1', '1', 'zhaomin', 'zhaomin', '202601'),
	('019b9cafc20b7ed087c7eea363f5317d', '绩效工资', 'JXGZ_SR', 1500.00, '1', '1', 'zhaomin', 'zhaomin', '202601'),
	('019b9cafc20c7ec2a152d07a730d4491', '奖金', 'JJ_SR', 0.00, '2', '1', 'zhaomin', 'zhaomin', '202601'),
	('019b9cafc20e7ea6aa4f1a49e55d2f87', '津贴补贴', 'JT_SR', 800.00, '1', '1', 'zhaomin', 'zhaomin', '202601'),
	('019b9cafc20f7e9983bd8cebfbe73e2d', '加班费', 'JBF_SR', 0.00, '2', '1', 'zhaomin', 'zhaomin', '202601'),
	('019b9cafc2117e7e9a4268d0cfcc2d75', '全勤奖', 'QQJ_SR', 0.00, '2', '1', 'zhaomin', 'zhaomin', '202601'),
	('019b9cafc2127e6fb0dceadc5c52142a', '上年度平均月工资', 'LAST_YEAR_M_SALA', 3500.00, '1', '1', 'zhaomin', 'zhaomin', '202601'),
	('019b9cafc2137e6882b2bd8da03d167a', '专项附加扣除', 'ZXFJKC', 0.00, '2', '1', 'zhaomin', 'zhaomin', '202601'),
	('019b9d0236c673e7a16f9eb54762c1bb', '基本工资', 'JBGZ_SR', 35000.00, '1', '1', 'zhaomin', 'zhaomin', '202512'),
	('019b9d0236c673e7a17089f41bed0743', '绩效工资', 'JXGZ_SR', 1500.00, '1', '1', 'zhaomin', 'zhaomin', '202512'),
	('019b9d0236c673e7a171df11a9657a45', '奖金', 'JJ_SR', 5000.00, '2', '1', 'zhaomin', 'zhaomin', '202512'),
	('019b9d0236c673e7a1723be4086cc0d6', '津贴补贴', 'JT_SR', 800.00, '1', '1', 'zhaomin', 'zhaomin', '202512'),
	('019b9d0236c673e7a17384c95abb1e85', '加班费', 'JBF_SR', 0.00, '2', '1', 'zhaomin', 'zhaomin', '202512'),
	('019b9d0236c673e7a174c9aafe331f3f', '全勤奖', 'QQJ_SR', 0.00, '2', '1', 'zhaomin', 'zhaomin', '202512'),
	('019b9d0236c673e7a1751d155cd6817f', '上年度平均月工资', 'LAST_YEAR_M_SALA', 3500.00, '1', '1', 'zhaomin', 'zhaomin', '202512'),
	('019b9d0236c673e7a17675b89990a818', '专项附加扣除', 'ZXFJKC', 0.00, '2', '1', 'zhaomin', 'zhaomin', '202512'),
	('019b9d0ff9297356955a1d7eacd8f025', '基本工资', 'JBGZ_SR', 3500.00, '1', '1', 'testadmin', 'testadmin', '202601'),
	('019b9d0ff92b733aa3462730d5cd115f', '绩效工资', 'JXGZ_SR', 1500.00, '1', '1', 'testadmin', 'testadmin', '202601'),
	('019b9d0ff92c732d83625db8710c6faf', '奖金', 'JJ_SR', 0.00, '2', '1', 'testadmin', 'testadmin', '202601'),
	('019b9d0ff92d731fad72e925583d95a3', '津贴补贴', 'JT_SR', 800.00, '1', '1', 'testadmin', 'testadmin', '202601'),
	('019b9d0ff92e73118b6a0dbd284f4c85', '加班费', 'JBF_SR', 0.00, '2', '1', 'testadmin', 'testadmin', '202601'),
	('019b9d0ff92f73048f92f74d326d1fba', '全勤奖', 'QQJ_SR', 0.00, '2', '1', 'testadmin', 'testadmin', '202601'),
	('019b9d0ff93072f8be8072e6cd7bad0d', '上年度平均月工资', 'LAST_YEAR_M_SALA', 3500.00, '1', '1', 'testadmin', 'testadmin', '202601'),
	('019b9d0ff93272dba2ce2ec0330685c7', '专项附加扣除', 'ZXFJKC', 0.00, '2', '1', 'testadmin', 'testadmin', '202601');
/*!40000 ALTER TABLE `whrs_salary_user_base` ENABLE KEYS */;

-- 导出  表 os-whrs.whrs_salary_user_item 结构
CREATE TABLE IF NOT EXISTS `whrs_salary_user_item` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(256) NOT NULL,
  `KEYCODE` varchar(32) NOT NULL,
  `CTIME` varchar(14) NOT NULL,
  `VAL` decimal(18,2) NOT NULL,
  `SHOWMODEL` varchar(1) NOT NULL,
  `SALARYTIME` varchar(8) NOT NULL,
  `USERKEY` varchar(32) NOT NULL,
  `USERNAME` varchar(32) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.whrs_salary_user_item 的数据：~92 rows (大约)
/*!40000 ALTER TABLE `whrs_salary_user_item` DISABLE KEYS */;
INSERT INTO `whrs_salary_user_item` (`ID`, `NAME`, `KEYCODE`, `CTIME`, `VAL`, `SHOWMODEL`, `SALARYTIME`, `USERKEY`, `USERNAME`) VALUES
	('019b9cf4057f75c0a55be74a394a9bbe', '个税起征点', 'GSQZD', '20260108173316', 5000.00, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4057f75c0a55cba3031d4cfbf', '最高缴费基数（社保）', 'SB_MAX', '20260108173316', 10000.00, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4057f75c0a55d7882cc4fa200', '全勤奖', 'QQJ_SR', '20260108173316', 0.00, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4058e7fd09085b11c263c2b07', '养老保险-个人比例', 'YLBX_GR', '20260108173316', 0.08, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4058e7fd09086caa9518e1062', '应纳税所得额', 'YNSSDE', '20260108173316', 31355.00, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4058e7fd090871b1164591f6a', '其他扣款（考勤|罚款等）', 'QTKK_DD', '20260108173316', 0.00, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4058e7fd09088de1717952129', '奖金', 'JJ_SR', '20260108173316', 0.00, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4058e7fd090899cf22d7f0117', '基本工资', 'JBGZ_SR', '20260108173316', 35000.00, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4058e7fd0908af689d93ce368', '个人所得税', 'INCOME_TAX_SYS', '20260108173316', 5178.75, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4058e7fd0908bf4ed0e91f9e4', '应得收入', 'GROSS_SALARY_SYS', '20260108173316', 37300.00, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4058e7fd0908c1e0d7c882276', '津贴补贴', 'JT_SR', '20260108173316', 800.00, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4058e7fd0908d860d7286f603', '医疗保险-个人比例', 'YILIAOBX_GR', '20260108173316', 0.02, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4058e7fd0908e274c0b11cfae', '实发工资', 'NET_SALARY_SYS', '20260108173316', 31176.25, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4058e7fd0908f0d200a9294d3', '最低缴费基数（社保）', 'SB_MIN', '20260108173316', 3000.00, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4058e7fd090904c96d010513f', '上年度平均月工资', 'LAST_YEAR_M_SALA', '20260108173316', 3500.00, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4058e7fd090919abb97a1ee06', '社保个人缴费', 'SB_GRJF', '20260108173316', 525.00, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4059e7dffab80e0804fa01719', '绩效工资', 'JXGZ_SR', '20260108173316', 1500.00, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4059e7dffab81d6daf2bd3544', '专项附加扣除', 'ZXFJKC', '20260108173316', 0.00, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4059e7dffab82cc06c80656eb', '加班费', 'JBF_SR', '20260108173316', 0.00, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4059e7dffab8391c1a8809581', '汇总扣款', 'KOUKUAN', '20260108173316', 6123.75, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4059e7dffab8462e130fe6971', '失业保险-个人比例', 'SYBX_GR', '20260108173316', 0.05, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4059e7dffab85948f7140aafc', '公积金个人缴费', 'GJJ_GR', '20260108173316', 420.00, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cf4059e7dffab86f26381df09e0', '公积金缴费比例', 'GJJ_BL', '20260108173316', 0.12, '1', '202601', 'zhaomin', 'zhaomin'),
	('019b9cff0a9672a3a9ba7acc46131550', '个税起征点', 'GSQZD', '20260108174518', 5000.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0a9672a3a9bba0907450b7ec', '最高缴费基数（社保）', 'SB_MAX', '20260108174518', 10000.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0a9672a3a9bc190ce3e73146', '全勤奖', 'QQJ_SR', '20260108174518', 0.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0a9672a3a9bd30fd6a2d5c53', '养老保险-个人比例', 'YLBX_GR', '20260108174518', 0.08, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0a9672a3a9be76f04d60df34', '应纳税所得额', 'YNSSDE', '20260108174518', -145.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0a9672a3a9bfa1c34a7ad326', '其他扣款（考勤|罚款等）', 'QTKK_DD', '20260108174518', 0.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0a9672a3a9c01a125fe9682f', '奖金', 'JJ_SR', '20260108174518', 0.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0aa57c9b9dc88a0d69c45f59', '基本工资', 'JBGZ_SR', '20260108174518', 3500.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0aa57c9b9dc920a83deae1d5', '个人所得税', 'INCOME_TAX_SYS', '20260108174518', 0.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0aa57c9b9dca72822f61417d', '应得收入', 'GROSS_SALARY_SYS', '20260108174518', 5800.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0aa57c9b9dcb463581846d6c', '津贴补贴', 'JT_SR', '20260108174518', 800.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0aa57c9b9dcc3b74102f32cb', '医疗保险-个人比例', 'YILIAOBX_GR', '20260108174518', 0.02, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0aa57c9b9dcde1b1826d66a9', '实发工资', 'NET_SALARY_SYS', '20260108174518', 4855.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0aa57c9b9dce4f894eb5b836', '最低缴费基数（社保）', 'SB_MIN', '20260108174518', 3000.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0aa57c9b9dcf2ee6342389be', '上年度平均月工资', 'LAST_YEAR_M_SALA', '20260108174518', 3500.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0aa57c9b9dd0c901de276b83', '社保个人缴费', 'SB_GRJF', '20260108174518', 525.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0aa57c9b9dd1f776f448475e', '绩效工资', 'JXGZ_SR', '20260108174518', 1500.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0aa57c9b9dd2927b07f6a3c7', '专项附加扣除', 'ZXFJKC', '20260108174518', 0.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0aa57c9b9dd357dd90931666', '加班费', 'JBF_SR', '20260108174518', 0.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0aa57c9b9dd41aa7d2e27769', '汇总扣款', 'KOUKUAN', '20260108174518', 945.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0aa57c9b9dd555d994cd5fa7', '失业保险-个人比例', 'SYBX_GR', '20260108174518', 0.05, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0aa57c9b9dd6abe563334d0b', '公积金个人缴费', 'GJJ_GR', '20260108174518', 420.00, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9cff0aa57c9b9dd79fe81101d911', '公积金缴费比例', 'GJJ_BL', '20260108174518', 0.12, '1', '202601', 'zhangwuji', 'zhangwuji'),
	('019b9d027a95758d98d56eb528c59692', '个税起征点', 'GSQZD', '20260108174904', 5000.00, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027a95758d98d615be0642e47f', '最高缴费基数（社保）', 'SB_MAX', '20260108174904', 10000.00, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027a95758d98d7f251eff980e1', '全勤奖', 'QQJ_SR', '20260108174904', 0.00, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027a95758d98d8b466d4f9f4b3', '养老保险-个人比例', 'YLBX_GR', '20260108174904', 0.08, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027a95758d98d94c9fc6e94e50', '应纳税所得额', 'YNSSDE', '20260108174904', 36355.00, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027a95758d98da62625324ed11', '其他扣款（考勤|罚款等）', 'QTKK_DD', '20260108174904', 0.00, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027a95758d98dbc7886250c832', '奖金', 'JJ_SR', '20260108174904', 5000.00, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027a95758d98dc9852797b3948', '基本工资', 'JBGZ_SR', '20260108174904', 35000.00, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027a95758d98dd003fb43209b6', '个人所得税', 'INCOME_TAX_SYS', '20260108174904', 6496.50, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027a95758d98dec387b2637cf5', '应得收入', 'GROSS_SALARY_SYS', '20260108174904', 42300.00, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027a95758d98df25dd87bad899', '津贴补贴', 'JT_SR', '20260108174904', 800.00, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027a95758d98e0658ecb464137', '医疗保险-个人比例', 'YILIAOBX_GR', '20260108174904', 0.02, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027aa47f84adc1a8fc5c891658', '实发工资', 'NET_SALARY_SYS', '20260108174904', 34858.50, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027aa777bfb18b692c56bec9ef', '最低缴费基数（社保）', 'SB_MIN', '20260108174904', 3000.00, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027aa777bfb18c0a19a7645448', '上年度平均月工资', 'LAST_YEAR_M_SALA', '20260108174904', 3500.00, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027aa777bfb18dbf5b632378fa', '社保个人缴费', 'SB_GRJF', '20260108174904', 525.00, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027aa777bfb18e86066e1fbcac', '绩效工资', 'JXGZ_SR', '20260108174904', 1500.00, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027aa777bfb18fb65bf884319b', '专项附加扣除', 'ZXFJKC', '20260108174904', 0.00, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027aa777bfb1903838854b049e', '加班费', 'JBF_SR', '20260108174904', 0.00, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027aa777bfb19136d59bbab182', '汇总扣款', 'KOUKUAN', '20260108174904', 7441.50, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027aa777bfb192606926546015', '失业保险-个人比例', 'SYBX_GR', '20260108174904', 0.05, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027aa777bfb1937a355394da8d', '公积金个人缴费', 'GJJ_GR', '20260108174904', 420.00, '1', '202512', 'zhaomin', 'zhaomin'),
	('019b9d027ab771bab465a01d18143128', '公积金缴费比例', 'GJJ_BL', '20260108174904', 0.12, '1', '202512', 'zhaomin', 'zhaomin'),
	('019ba1185f45731e98db9f657945bfcf', '个税起征点', 'GSQZD', '20260109125127', 5000.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f4673109bbde2e775f7d8af', '最高缴费基数（社保）', 'SB_MAX', '20260109125127', 10000.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f4773029e8ed017ec79c3ca', '全勤奖', 'QQJ_SR', '20260109125127', 0.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f4872f58d452cb6f3446cf0', '养老保险-个人比例', 'YLBX_GR', '20260109125127', 0.08, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f4972e8b8ce1a91e83ae1ad', '应纳税所得额', 'YNSSDE', '20260109125127', -145.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f4a72da8b97598328e53985', '其他扣款（考勤|罚款等）', 'QTKK_DD', '20260109125127', 0.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f4b7ae1a9081daf54638a9b', '奖金', 'JJ_SR', '20260109125127', 0.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f4c7b1086e501abb1350d38', '基本工资', 'JBGZ_SR', '20260109125127', 3500.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f4d7b098994404bbf5c364d', '个人所得税', 'INCOME_TAX_SYS', '20260109125127', 0.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f4e7afa98f1975cd525c96b', '应得收入', 'GROSS_SALARY_SYS', '20260109125127', 5800.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f4f7aeda95ec9739f1fc83a', '津贴补贴', 'JT_SR', '20260109125127', 800.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f4f7aeda95fe7fa573ec15c', '医疗保险-个人比例', 'YILIAOBX_GR', '20260109125127', 0.02, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f507adf8ecad0e70c950617', '实发工资', 'NET_SALARY_SYS', '20260109125127', 4855.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f517ad2a850e53cdce6d531', '最低缴费基数（社保）', 'SB_MIN', '20260109125127', 3000.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f527ac5bd0cef327ed27ea4', '上年度平均月工资', 'LAST_YEAR_M_SALA', '20260109125127', 3500.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f537ab5a4224f5df2ef9d35', '社保个人缴费', 'SB_GRJF', '20260109125127', 525.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f547aa983a6dac4150fdb9d', '绩效工资', 'JXGZ_SR', '20260109125127', 1500.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f557a9ab0ba9d6ed14fd854', '专项附加扣除', 'ZXFJKC', '20260109125127', 0.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f577027b54716dcc4ac17f4', '加班费', 'JBF_SR', '20260109125127', 0.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f5870199404393d3d4dc3a3', '汇总扣款', 'KOUKUAN', '20260109125127', 945.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f59700b86f0ee2fe3fad2cc', '失业保险-个人比例', 'SYBX_GR', '20260109125127', 0.05, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f597ffe86f1e793fe3bdc11', '公积金个人缴费', 'GJJ_GR', '20260109125127', 420.00, '1', '202601', 'testadmin', 'testadmin'),
	('019ba1185f597ffe86f2203534357907', '公积金缴费比例', 'GJJ_BL', '20260109125127', 0.12, '1', '202601', 'testadmin', 'testadmin');
/*!40000 ALTER TABLE `whrs_salary_user_item` ENABLE KEYS */;

-- 导出  表 os-whrs.whrs_salary_user_unit 结构
CREATE TABLE IF NOT EXISTS `whrs_salary_user_unit` (
  `ID` varchar(32) NOT NULL,
  `SALARYTIME` varchar(8) NOT NULL,
  `USERKEY` varchar(32) NOT NULL,
  `USERNAME` varchar(32) NOT NULL,
  `CTIME` varchar(14) NOT NULL,
  `NOTE` varchar(256) DEFAULT NULL,
  `STATE` varchar(2) NOT NULL COMMENT '0待计算 / 1已计算 ',
  `GROSSPAY` decimal(18,2) NOT NULL COMMENT '应发工资Gross Pay',
  `NETPAY` decimal(18,2) NOT NULL COMMENT '实发工资Net Pay',
  `TAXAMOUNT` decimal(18,2) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  os-whrs.whrs_salary_user_unit 的数据：~26 rows (大约)
/*!40000 ALTER TABLE `whrs_salary_user_unit` DISABLE KEYS */;
INSERT INTO `whrs_salary_user_unit` (`ID`, `SALARYTIME`, `USERKEY`, `USERNAME`, `CTIME`, `NOTE`, `STATE`, `GROSSPAY`, `NETPAY`, `TAXAMOUNT`) VALUES
	('019b9c6def5e71388d358e855d8d08c3', '202601', 'zhaomin', 'zhaomin', '20260108173316', NULL, '1', 37300.00, 31176.25, 5178.75),
	('019b9c6def6671c2bd82af2c4093a89f', '202601', 'zhangwuji', 'zhangwuji', '20260108174518', NULL, '1', 5800.00, 4855.00, 0.00),
	('019b9c6def6b7a7da0459b95bcfe4a4d', '202601', 'testadmin', 'testadmin', '20260109125127', NULL, '1', 5800.00, 4855.00, 0.00),
	('019b9c6def717a1ba9b0fe1b0aa661da', '202601', 'systest', 'systest', '20260108150649', NULL, '0', 0.00, 0.00, 0.00),
	('019b9c6def7679d88078f970833d2ab7', '202601', 'sysadmin', 'sysadmin', '20260108150649', NULL, '0', 0.00, 0.00, 0.00),
	('019b9c6def7a79a1bd17cdb58ee51554', '202601', 'HDF', 'HDF', '20260108150649', NULL, '0', 0.00, 0.00, 0.00),
	('019b9c6def7e79698cca2ee6f11aaf17', '202601', 'guest', 'guest', '20260108150649', NULL, '0', 0.00, 0.00, 0.00),
	('019b9c6def81793fa98f67924304191a', '202601', 'dhwl', 'dhwl', '20260108150649', NULL, '0', 0.00, 0.00, 0.00),
	('019b9d020d7a7441978f699525ea40ee', '202602', 'zhaomin', 'zhaomin', '20260108174836', NULL, '0', 0.00, 0.00, 0.00),
	('019b9d020d7a74419790a8ab902359db', '202602', 'zhangwuji', 'zhangwuji', '20260108174836', NULL, '0', 0.00, 0.00, 0.00),
	('019b9d020d7a7441979116615199f8a8', '202602', 'testadmin', 'testadmin', '20260108174836', NULL, '0', 0.00, 0.00, 0.00),
	('019b9d020d7a744197929b45ee1a570a', '202602', 'systest', 'systest', '20260108174836', NULL, '0', 0.00, 0.00, 0.00),
	('019b9d020d7a7441979397f12003bd8c', '202602', 'sysadmin', 'sysadmin', '20260108174836', NULL, '0', 0.00, 0.00, 0.00),
	('019b9d020d7a7441979480dc46ef916c', '202602', 'HDF', 'HDF', '20260108174836', NULL, '0', 0.00, 0.00, 0.00),
	('019b9d020d7a744197956146b058b1c3', '202602', 'guest', 'guest', '20260108174836', NULL, '0', 0.00, 0.00, 0.00),
	('019b9d020d7a74419796c5338783b94e', '202602', 'dhwl', 'dhwl', '20260108174836', NULL, '0', 0.00, 0.00, 0.00),
	('019b9d0226067cf2bce1691b604ec75d', '202512', 'zhaomin', 'zhaomin', '20260108174904', NULL, '1', 42300.00, 34858.50, 6496.50),
	('019b9d0226067cf2bce242907d828931', '202512', 'zhangwuji', 'zhangwuji', '20260108174842', NULL, '0', 0.00, 0.00, 0.00),
	('019b9d02261671188d5500c6a24ff5a0', '202512', 'testadmin', 'testadmin', '20260108174842', NULL, '0', 0.00, 0.00, 0.00),
	('019b9d02261672148d5668dc4c7750fc', '202512', 'systest', 'systest', '20260108174842', NULL, '0', 0.00, 0.00, 0.00),
	('019b9d02261672148d57cdcc07c101ed', '202512', 'sysadmin', 'sysadmin', '20260108174842', NULL, '0', 0.00, 0.00, 0.00),
	('019b9d0226257c0f85ab298aa60b6790', '202512', 'HDF', 'HDF', '20260108174842', NULL, '0', 0.00, 0.00, 0.00),
	('019b9d0226257c0f85ac35a9aea16389', '202512', 'guest', 'guest', '20260108174842', NULL, '0', 0.00, 0.00, 0.00),
	('019b9d0226257c0f85ad23e6f300dfc3', '202512', 'dhwl', 'dhwl', '20260108174842', NULL, '0', 0.00, 0.00, 0.00),
	('019b9de491b97b73aabb74011cc4a9e4', '202601', 'asdf', 'asdf', '20260108215601', NULL, '0', 0.00, 0.00, 0.00),
	('019b9e50cf15752782a84a2bcc003492', '202512', 'asdf', 'asdf', '20260108235414', NULL, '0', 0.00, 0.00, 0.00);
/*!40000 ALTER TABLE `whrs_salary_user_unit` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

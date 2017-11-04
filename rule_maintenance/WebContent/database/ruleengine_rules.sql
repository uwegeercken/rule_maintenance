-- MySQL dump 10.15  Distrib 10.0.23-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: ruleengine_rules
-- ------------------------------------------------------
-- Server version	10.0.23-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activity_log`
--
DROP TABLE IF EXISTS `activity_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_log` (
  `id` int(19) NOT NULL AUTO_INCREMENT,
  `activity_date` datetime DEFAULT NULL,
  `user_id` int(10) unsigned NOT NULL DEFAULT '0',
  `message` varchar(255) DEFAULT NULL,
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_last_update` (`last_update`)
) ENGINE=MyISAM CHARSET=utf8;

--
-- Table structure for table `action`
--

DROP TABLE IF EXISTS `action`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `action` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `classname` varchar(255) NOT NULL,
  `methodname` varchar(255) DEFAULT NULL,
  `methoddisplayname` varchar(255) DEFAULT NULL,
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `action`
--

LOCK TABLES `action` WRITE;
/*!40000 ALTER TABLE `action` DISABLE KEYS */;
INSERT INTO `action` VALUES (1,'Set a value to another value','com.datamelt.rules.core.action.StringAction','setValue','set value (string)','2016-04-20 19:26:10'),
(2,'Add a value to the beginning of a value','com.datamelt.rules.core.action.StringAction','prependValue','prepend','2016-04-20 19:26:10'),
(3,'Add a value to the end of a value','com.datamelt.rules.core.action.StringAction','appendValue','append','2016-04-20 19:26:10'),
(4,'Concatenate two values','com.datamelt.rules.core.action.StringAction','concatValues','concat','2016-04-20 19:26:10'),
(5,'Add leading spaces to a value','com.datamelt.rules.core.action.StringAction','addLeadingSpaces','add leading spaces','2016-04-20 19:26:10'),
(6,'Add leading zeros to a value','com.datamelt.rules.core.action.StringAction','addLeadingZeros','add leading zeros','2016-04-20 19:26:10'),
(7,'Set a value to its lowercase representation','com.datamelt.rules.core.action.StringAction','lowerCaseValue','lowercase','2016-04-20 19:26:10'),
(8,'Set a value to its uppercase representation','com.datamelt.rules.core.action.StringAction','upperCaseValue','uppercase','2016-04-20 19:26:10'),
(9,'Set a value to the substring of a value','com.datamelt.rules.core.action.StringAction','subStringValue','substring','2016-04-20 19:26:10'),
(10,'Remove (trim) all spaces from the beginning and the end of a value','com.datamelt.rules.core.action.StringAction','trimValue','trim','2016-04-20 19:26:10'),
(11,'Set a value to another value','com.datamelt.rules.core.action.MathAction','setValue','set value (number)','2016-04-20 19:30:14'),
(12,'Sum two values','com.datamelt.rules.core.action.MathAction','addValues','sum','2016-04-20 19:30:14'),
(13,'Devide two values','com.datamelt.rules.core.action.MathAction','devideValues','devide','2016-04-20 19:30:14'),
(14,'Multiply two values','com.datamelt.rules.core.action.MathAction','multiplyValues','multiply','2016-04-20 19:30:14'),
(15,'Subtract two values','com.datamelt.rules.core.action.MathAction','subtractValues','subtract','2016-04-23 15:25:55'),
(16,'Set a value to another value','com.datamelt.rules.core.action.DateAction','setValue','set value (date)','2016-04-20 19:30:14'),
(17,'Set a value to the square root of a value','com.datamelt.rules.core.action.MathAction','squareRootValue','square root','2016-04-20 19:30:14'),
(18,'Set a value to the square of a value','com.datamelt.rules.core.action.MathAction','squareValue','square','2016-04-20 19:30:14'),
(19,'Round a value','com.datamelt.rules.core.action.MathAction','roundValue','round','2016-04-20 19:30:14'),
(20,'Set a value to the cosinus of a value','com.datamelt.rules.core.action.MathAction','cosValue','cosinus','2016-04-20 19:30:14'),
(21,'Set a value to the cosinush of a value','com.datamelt.rules.core.action.MathAction','coshValue','cosinush','2016-04-20 19:30:14'),
(22,'Set a value to the acosinus of a value','com.datamelt.rules.core.action.MathAction','acosValue','acosinus','2016-04-20 19:30:14'),
(23,'Set a value to the sinus of a value','com.datamelt.rules.core.action.MathAction','sinValue','sinus','2016-04-20 19:30:14'),
(24,'Set a value to the sinush of a value','com.datamelt.rules.core.action.MathAction','sinhValue','sinush','2016-04-20 19:30:14'),
(25,'Set a value to the asinus of a value','com.datamelt.rules.core.action.MathAction','asinValue','asinus','2016-04-20 19:30:14'),
(26,'Set a value to the tangens of a value','com.datamelt.rules.core.action.MathAction','tanValue','tangens','2016-04-20 19:30:14'),
(27,'Set a value to the tangensh of a value','com.datamelt.rules.core.action.MathAction','tanhValue','tangensh','2016-04-20 19:30:14'),
(28,'Set a value to the atangens of a value','com.datamelt.rules.core.action.MathAction','atanValue','atangens','2016-04-20 19:30:14'),
(29,'Add a percentage of a value to the value','com.datamelt.rules.core.action.MathAction','addPercentageValue','add percentage','2016-04-20 19:30:14'),
(30,'Subtract a percentage of a value from the value','com.datamelt.rules.core.action.MathAction','subtractPercentageValue','subtract percentage','2016-04-20 19:30:14'),
(31,'Set a value to its absolute value','com.datamelt.rules.core.action.MathAction','absValue','absolute','2016-04-23 14:51:37'),
(32,'Set a value to today\'s date','com.datamelt.rules.core.action.DateAction','setTodayDate','set today date','2016-06-11 08:44:18'),
(33,'Set a value to the last day of the month','com.datamelt.rules.core.action.DateAction','setLastDayOfMonth','set last day of month','2016-06-11 08:44:18'),
(34,'Set a value to the first day of the month','com.datamelt.rules.core.action.DateAction','setFirstDayOfMonth','set first day of month','2016-06-11 11:17:18'),
(35,'Set a value to the mid day of the month (day 15)','com.datamelt.rules.core.action.DateAction','setMidDayOfMonth','set mid day of month','2016-06-11 11:30:40'),
(36,'Replace a value by providing a regular expression to search for and a replacement value','com.datamelt.rules.core.action.StringAction','replaceValue','replace value','2016-06-25 17:50:00'),
(37,'Add minutes to a date','com.datamelt.rules.core.action.DateAction','addMinutes','add minutes','2016-10-10 23:00:00'),
(38,'Remainder value (modulus)','com.datamelt.rules.core.action.MathAction','remainderValue','remainder value','2016-10-10 23:00:00'),
(39,'Random value','com.datamelt.rules.core.action.MathAction','randomValue','random value','2016-10-12 21:47:00'),
(40,'Add hours to a date','com.datamelt.rules.core.action.DateAction','addHours','add hours','2016-10-10 23:00:00'),
(41,'Add seconds to a date','com.datamelt.rules.core.action.DateAction','addSeconds','add seconds','2016-10-10 23:00:00'),
(42,'Add days to a date','com.datamelt.rules.core.action.DateAction','addDays','add days','2016-10-10 23:00:00'),
(43,'Subtract minutes from a date','com.datamelt.rules.core.action.DateAction','subtractMinutes','subtract minutes','2016-10-10 23:00:00'),
(44,'Subtract hours from a date','com.datamelt.rules.core.action.DateAction','subtractHours','subtract hours','2016-10-10 23:00:00'),
(45,'Subtract seconds from a date','com.datamelt.rules.core.action.DateAction','subtractSeconds','subtract seconds','2016-10-10 23:00:00'),
(46,'Subtract days from a date','com.datamelt.rules.core.action.DateAction','subtractDays','subtract days','2016-10-10 23:00:00'),
(47,'Replace value from mapping file','com.datamelt.rules.core.action.StringAction','replaceValueFromMap','replace value from map','2017-07-19 18:02:00');
/*!40000 ALTER TABLE `action` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `action_method`
--

DROP TABLE IF EXISTS `action_method`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `action_method` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `action_id` int(10) NOT NULL,
  `return_type` varchar(80) CHARACTER SET latin1 DEFAULT NULL,
  `method_types` varchar(80) CHARACTER SET latin1 DEFAULT NULL,
  `note` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `optional_type1` varchar(80) CHARACTER SET latin1 DEFAULT NULL,
  `optional_type1_explanation` varchar(255) DEFAULT NULL,
  `optional_type2` varchar(80) CHARACTER SET latin1 DEFAULT NULL,
  `optional_type2_explanation` varchar(255) DEFAULT NULL,
  `optional_type3` varchar(80) CHARACTER SET latin1 DEFAULT NULL,
  `optional_type3_explanation` varchar(255) DEFAULT NULL,
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_actionid` (`action_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `action_method`
--

LOCK TABLES `action_method` WRITE;
/*!40000 ALTER TABLE `action_method` DISABLE KEYS */;
INSERT INTO `action_method` VALUES (1,4,'String','String, double',NULL,'String','Seperator to be used',NULL,NULL,NULL,NULL,'2016-04-23 14:00:59'),
(2,5,'String','String, integer','integer: maximum length of the String',NULL,'number of spaces to add at the begining',NULL,NULL,NULL,NULL,'2016-04-23 14:02:28'),
(3,3,'String','String, String',NULL,'String','Seperator to be used',NULL,NULL,NULL,NULL,'2016-04-23 13:44:00'),
(4,3,'String','String, integer','','String','Seperator to be used',NULL,NULL,NULL,NULL,'2016-04-23 14:00:59'),
(5,3,'String','String, long',NULL,'String','Seperator to be used',NULL,NULL,NULL,NULL,'2016-04-23 14:00:59'),
(6,4,'String','String, float',NULL,'String','Seperator to be used',NULL,NULL,NULL,NULL,'2016-04-23 14:00:59'),
(7,4,'String','String, integer',NULL,'String','Seperator to be used',NULL,NULL,NULL,NULL,'2016-04-23 14:00:59'),
(8,4,'String','String, long',NULL,'String','Seperator to be used',NULL,NULL,NULL,NULL,'2016-04-23 14:00:59'),
(9,4,'String','String, String',NULL,'String','Seperator to be used',NULL,NULL,NULL,NULL,'2016-04-23 13:45:46'),
(10,7,'String','String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 13:56:52'),
(11,1,'String','String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:37:08'),
(12,11,'boolen','boolean',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:43:38'),
(13,11,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:43:38'),
(14,11,'float','float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:43:38'),
(15,11,'integer','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:43:38'),
(16,11,'long','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:43:38'),
(17,16,'Date','Date',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:47:21'),
(18,16,'Date','String',NULL,'String','Date format of the String',NULL,NULL,NULL,NULL,'2016-04-23 14:47:21'),
(19,22,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:49:36'),
(20,31,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:53:29'),
(21,31,'float','float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:53:29'),
(22,31,'integer','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:53:29'),
(23,31,'long','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:53:29'),
(24,6,'String','String, integer','integer: maximum length of the String',NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:55:49'),
(25,29,'double','double, double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(26,29,'double','double, float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(27,29,'double','double, integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(28,29,'double','integer, double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(29,29,'double','integer, float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(30,29,'double','integer, integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(31,29,'double','long, double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(32,29,'double','long, float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(33,29,'double','long, int',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(34,25,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:00:28'),
(35,28,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:03:14'),
(36,20,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:04:26'),
(37,21,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:04:58'),
(38,13,'double','double, double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:07:46'),
(39,13,'double','double, integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:07:46'),
(40,13,'float','float, float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:07:46'),
(41,13,'double','integer, integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:07:46'),
(42,13,'double','integer, long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:07:46'),
(43,13,'double','long, double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:07:46'),
(44,13,'double','long, integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:07:46'),
(45,13,'double','long, long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:07:46'),
(46,14,'double','double, double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:11:11'),
(47,14,'double','double, integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:11:11'),
(48,14,'float','float, float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:11:11'),
(49,14,'long','integer, integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:11:11'),
(50,14,'long','integer, long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:11:11'),
(51,14,'double','long, double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:11:11'),
(52,14,'long','long, integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:11:11'),
(53,14,'long','long, long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:11:11'),
(54,2,'String','String, String',NULL,'String','Seperator to be used',NULL,NULL,NULL,NULL,'2016-04-23 13:44:00'),
(55,2,'String','String, integer','','String','Seperator to be used',NULL,NULL,NULL,NULL,'2016-04-23 14:00:59'),
(56,2,'String','String, long',NULL,'String','Seperator to be used',NULL,NULL,NULL,NULL,'2016-04-23 14:00:59'),
(57,19,'long','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:14:44'),
(58,19,'int','float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:14:44'),
(59,23,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:15:51'),
(60,24,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:16:29'),
(61,18,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:18:12'),
(62,18,'double','float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:18:12'),
(63,18,'long','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:18:12'),
(64,18,'long','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:18:12'),
(65,17,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:20:20'),
(66,17,'double','float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:20:20'),
(67,17,'double','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:20:20'),
(68,17,'double','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:20:20'),
(69,9,'String','String, integer','integer: start of the substring','integer','end of the substring',NULL,NULL,NULL,NULL,'2016-04-23 15:23:15'),
(70,15,'long','Date, Date','',NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:29:49'),
(71,15,'double','double, double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:29:49'),
(72,15,'float','float, float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:29:49'),
(73,15,'long','long, long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:29:49'),
(74,15,'integer','integer, integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:29:49'),
(75,15,'long','integer, long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:29:49'),
(76,15,'long','long, integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:29:49'),
(77,30,'double','double, double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(78,30,'double','double, float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(79,30,'double','double, integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(80,30,'double','integer, double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(81,30,'double','integer, float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(82,30,'double','integer, integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(83,30,'double','long, double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(84,30,'double','long, float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(85,30,'double','long, int',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 14:59:02'),
(86,12,'double','double, double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:35:43'),
(87,12,'float','float, float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:35:43'),
(88,12,'integer','integer, integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:35:43'),
(89,12,'long','integer, long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:35:43'),
(90,12,'long','long, integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:35:43'),
(91,12,'long','long, long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:35:43'),
(92,26,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:36:26'),
(93,27,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:37:07'),
(94,10,'String','String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:37:45'),
(95,8,'String','String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-23 15:38:27'),
(96,32,'String','String',NULL,'integer','Number of offset days',NULL,NULL,NULL,NULL,'2016-06-11 08:54:50'),
(100,33,'Date','[empty]',NULL,'Date','Date for which to calculate the last day of the month',NULL,NULL,NULL,NULL,'2016-06-11 11:22:42'),
(99,33,'String','integer, integer',NULL,'String','Date format of the String',NULL,NULL,NULL,NULL,'2016-06-11 08:52:54'),
(102,34,'Date','[empty]','','Date','Date for which to calculate the first day of the month',NULL,NULL,NULL,NULL,'2016-06-11 11:24:35'),
(103,34,'String','integer, integer','','String','Date format of the String',NULL,NULL,NULL,NULL,'2016-06-11 11:24:35'),
(104,35,'Date','[empty]','','Date','Date for which to calculate the mid day of the month',NULL,NULL,NULL,NULL,'2016-06-11 11:32:09'),
(105,35,'String','integer, integer','','String','Date format of the String',NULL,NULL,NULL,NULL,'2016-06-11 11:32:09'),
(106,11,'BigDecimal','BigDecimal',null,null,null,NULL,NULL,NULL,NULL,'2016-06-11 11:32:09'),
(107,36,'String','String, String, String',null,null,null,NULL,NULL,NULL,NULL,'2016-06-11 11:32:09'),
(108,9,'String','String, String','second String is the String up to which the substring shall extend',null,null,NULL,NULL,NULL,NULL,'2016-06-11 11:32:09'),
(110,37,'Date','Date, long',null,null,null,NULL,NULL,NULL,NULL,'2016-10-10 23:00:00'),
(111,38,'long','long, long',null,null,null,NULL,NULL,NULL,NULL,'2016-10-10 23:00:00'),
(112,38,'long','long, integer',null,null,null,NULL,NULL,NULL,NULL,'2016-10-10 23:00:00'),
(113,38,'integer','integer, integer',null,null,null,NULL,NULL,NULL,NULL,'2016-10-10 23:00:00'),
(114,39,'integer','integer, integer',null,null,null,NULL,NULL,NULL,NULL,'2016-10-10 23:00:00'),
(115,40,'Date','Date, long',null,null,null,NULL,NULL,NULL,NULL,'2016-10-10 23:00:00'),
(116,41,'Date','Date, long',null,null,null,NULL,NULL,NULL,NULL,'2016-10-10 23:00:00'),
(117,42,'Date','Date, long',null,null,null,NULL,NULL,NULL,NULL,'2016-10-10 23:00:00'),
(118,43,'Date','Date, long',null,null,null,NULL,NULL,NULL,NULL,'2016-10-10 23:00:00'),
(119,44,'Date','Date, long',null,null,null,NULL,NULL,NULL,NULL,'2016-10-10 23:00:00'),
(120,45,'Date','Date, long',null,null,null,NULL,NULL,NULL,NULL,'2016-10-10 23:00:00'),
(121,46,'Date','Date, long',null,null,null,NULL,NULL,NULL,NULL,'2016-10-10 23:00:00'),
(122,4,'String','double, String',NULL,'String','Seperator to be used',NULL,NULL,NULL,NULL,'2016-04-23 14:00:59'),
(123,4,'String','integer, String',NULL,'String','Seperator to be used',NULL,NULL,NULL,NULL,'2016-04-23 14:00:59'),
(124,4,'String','float, String',NULL,'String','Seperator to be used',NULL,NULL,NULL,NULL,'2016-04-23 14:00:59'),
(125,4,'String','long, String',NULL,'String','Seperator to be used',NULL,NULL,NULL,NULL,'2016-04-23 14:00:59'),
(130,47,'String','String, String',NULL,null,null,NULL,NULL,NULL,NULL,'2016-04-23 14:00:59');
/*!40000 ALTER TABLE `action_method` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `check`
--

DROP TABLE IF EXISTS `check`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `check` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(80) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name_descriptive` varchar(255) DEFAULT NULL,
  `package` varchar(80) DEFAULT NULL,
  `class` varchar(80) DEFAULT NULL,
  `check_single_field` tinyint(1) DEFAULT NULL,
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `check`
--

LOCK TABLES `check` WRITE;
/*!40000 ALTER TABLE `check` DISABLE KEYS */;
INSERT INTO `check` VALUES (1,'Check Is Equal','Check for equality of values','is equal to','com.datamelt.rules.implementation','CheckIsEqual',0,'2014-11-27 22:03:01'),
(2,'Check Is Not Equal','Check if values are not equal','is not equal to','com.datamelt.rules.implementation','CheckIsNotEqual',0,'2014-11-27 20:35:35'),
(3,'Check Contains','Check if one String contains another String','contains','com.datamelt.rules.implementation','CheckContains',0,'2014-11-27 20:35:35'),
(4,'Check Not Contains','Check if one String does not contain another String','does not contain','com.datamelt.rules.implementation','CheckNotContains',0,'2014-11-27 20:35:35'),
(5,'Check Is Uppercase','Check if a String contains upper case characters only','is uppercase','com.datamelt.rules.implementation','CheckIsUppercase',1,'2014-11-27 20:35:35'),
(6,'Check Is Lowercase','Check if a String contains lower case characters only','is lowercase','com.datamelt.rules.implementation','CheckIsLowercase',1,'2014-11-27 20:35:35'),
(7,'Check Ends With','Check if a String ends with a certain String','ends with','com.datamelt.rules.implementation','CheckEndsWith',0,'2014-11-27 20:35:35'),
(8,'Check Not Ends With','Check if a String does not end with a certain String','does not end with','com.datamelt.rules.implementation','CheckNotEndsWith',0,'2014-11-27 20:35:35'),
(9,'Check Starts With','Check if a String starts with a certain String','starts with','com.datamelt.rules.implementation','CheckStartsWith',0,'2014-11-27 20:35:35'),
(10,'Check Not Starts With','Check if a String does not start with a certain String','does not start with','com.datamelt.rules.implementation','CheckNotStartsWith',0,'2014-11-27 20:35:35'),
(11,'Check Is Greater','Check if a numeric value is greater than the other one. In case of a String checks if the length of a String is greater than the other one','is greater than','com.datamelt.rules.implementation','CheckIsGreater',0,'2014-11-27 20:35:35'),
(12,'Check Is Smaller','Check if a numeric value is smaller than the other one. In case of a String checks if the length of a String is smaller than the other one','is smaller than','com.datamelt.rules.implementation','CheckIsSmaller',0,'2014-11-27 20:35:35'),
(13,'Check Is Greater Or Equal','Check if a numeric value is greater or equal than the other one. In case of a string checks if the length of a String is greater or equal than the other one','is greater or equal than','com.datamelt.rules.implementation','CheckIsGreaterOrEqual',0,'2014-11-27 20:35:35'),
(14,'Check Is Smaller Or Equal','Check if a numeric value is smaller or equal than the other one. In case of a string checks if the length of a String is smaller or equal than the other one','is smaller or equal than','com.datamelt.rules.implementation','CheckIsSmallerOrEqual',0,'2014-11-27 20:35:35'),
(15,'Check Is In List','Checks if a string is contained in a list of values. The list is represented by a string where the individual values are seperated by a comma','is in list','com.datamelt.rules.implementation','CheckIsInList',0,'2014-11-27 20:35:35'),
(16,'Check Is Not In List','Checks if a string is not contained in a list of values. The list is represented by a string where the individual values are seperated by a comma','is not in list','com.datamelt.rules.implementation','CheckIsNotInList',0,'2014-11-27 20:35:35'),
(17,'Check Is Between','Checks if a numeric value is between two given values','is between','com.datamelt.rules.implementation','CheckIsBetween',0,'2014-11-27 20:35:35'),
(18,'Check Is Not Between','Checks if a numeric value is not between two given values','is not between','com.datamelt.rules.implementation','CheckIsNotBetween',0,'2014-11-27 20:35:35'),
(19,'Check Is Numeric','Checks if a value is numeric where all characters are numbers','is numeric','com.datamelt.rules.implementation','CheckIsNumeric',1,'2014-11-27 20:35:35'),
(20,'Check Is Empty','Checks if a string value is empty, meaning of zero length','is empty','com.datamelt.rules.implementation','CheckIsEmpty',1,'2014-11-27 20:35:35'),
(21,'Check Is Not Empty','Checks if a string value is not empty, meaning it\'s length is greater than zero','is not empty','com.datamelt.rules.implementation','CheckIsNotEmpty',1,'2014-11-27 20:35:35'),
(22,'Check Is Null','Checks if a value is null','is null','com.datamelt.rules.implementation','CheckIsNull',1,'2014-11-27 20:35:35'),
(23,'Check Is Not Null','Checks if a value is not null','is not null','com.datamelt.rules.implementation','CheckIsNotNull',1,'2014-11-27 20:35:35'),
(24,'Check Length','Checks if a string has a defined length. In case of an integer, checks the number of digits the integer consists of','has length','com.datamelt.rules.implementation','CheckLength',0,'2014-11-27 20:35:35'),
(25,'Check Matches','Checks if a string matches another one, using a regular expression pattern','matches','com.datamelt.rules.implementation','CheckMatches',0,'2014-11-27 20:35:35'),
(26,'Check Not Matches','Checks if a string does not match another one, using a regular expression pattern','not matches','com.datamelt.rules.implementation','CheckNotMatches',0,'2014-11-27 20:35:35'),
(27,'Check Sounds Like','Checks if a string sounds like the other, using the soundex algorithm','sounds like','com.datamelt.rules.implementation','CheckSoundsLike',0,'2014-11-27 20:35:35'),
(28,'Check Not Sounds Like','Checks if a string does not sound like the other, using the soundex algorithm','not sounds like','com.datamelt.rules.implementation','CheckNotSoundsLike',0,'2014-11-27 22:04:10'),
(29,'Check Is Negative Number','Checks if a number is smaller than zero','is negativ number','com.datamelt.rules.implementation','CheckIsNegativeNumber',1,'2014-11-27 20:35:35'),
(30,'Check List Has Member','Checks if a comma seperated list of values contains a given member','has list member','com.datamelt.rules.implementation','CheckListHasMember',0,'2015-04-17 06:25:48'),
(31,'Check List Not Has Member','Checks if a comma seperated list of values not contains a given member','has not list member','com.datamelt.rules.implementation','CheckListNotHasMember',0,'2015-04-17 06:26:09'),
(32,'Check Is Prime','Checks if a number is a prime number','is prime','com.datamelt.rules.implementation','CheckIsPrime',1,'2014-11-27 20:35:35'),
(33,'Check Is Not Prime','Checks if a number is not a prime number','is not prime','com.datamelt.rules.implementation','CheckIsNotPrime',1,'2014-11-27 20:35:35'),
(34,'Check Is Even','Checks if a number is an even number','is even','com.datamelt.rules.implementation','CheckIsEven',1,'2014-11-27 20:35:35'),
(35,'Check Is Not Even','Checks if a number is not an even number','is not even','com.datamelt.rules.implementation','CheckIsNotEven',1,'2014-11-27 20:35:35'),
(36,'Check Distance Is Equal','Checks if the Levenshtein distance between two strings is equal to a given value','is equal to (Levenshtein distance)','com.datamelt.rules.implementation','CheckDistanceIsEqual',0,'2015-04-17 06:23:05'),
(37,'Check Distance Is Greater Or Equal','Checks if the Levenshtein distance between two strings is greater or equal to a given value','is greater than or equal to (Levenshtein distance)','com.datamelt.rules.implementation','CheckDistanceIsGreaterOrEqual',0,'2015-04-17 06:27:57'),
(38,'Check Distance Is Smaller Or Equal','Checks if the Levenshtein distance between two strings is smaller or equal to a given value','is smaller than or equal to (Levenshtein distance)','com.datamelt.rules.implementation','CheckDistanceIsSmallerOrEqual',0,'2015-04-17 06:28:13'),
(39,'Check Distance Is Smaller','Checks if the Levenshtein distance between two strings is smaller than a given value','is smaller than (Levenshtein distance)','com.datamelt.rules.implementation','CheckDistanceIsSmaller',0,'2015-04-17 06:21:53'),
(40,'Check Distance Is Greater','Checks if the Levenshtein distance between two strings is greater than a given value','is greater than (Levenshtein distance)','com.datamelt.rules.implementation','CheckDistanceIsGreater',0,'2015-04-17 06:20:47');
/*!40000 ALTER TABLE `check` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `check_method`
--

DROP TABLE IF EXISTS `check_method`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `check_method` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `check_id` int(10) NOT NULL,
  `compare` varchar(80) CHARACTER SET latin1 DEFAULT NULL,
  `compare_to` varchar(80) CHARACTER SET latin1 DEFAULT NULL,
  `note` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `parameter1` varchar(80) CHARACTER SET latin1 DEFAULT NULL,
  `parameter1_explanation` varchar(255) DEFAULT NULL,
  `parameter2` varchar(80) CHARACTER SET latin1 DEFAULT NULL,
  `parameter2_explanation` varchar(255) DEFAULT NULL,
  `parameter3` varchar(80) CHARACTER SET latin1 DEFAULT NULL,
  `parameter3_explanation` varchar(255) DEFAULT NULL,
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_checkid` (`check_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `check_method`
--

LOCK TABLES `check_method` WRITE;
/*!40000 ALTER TABLE `check_method` DISABLE KEYS */;
INSERT INTO `check_method` VALUES (1,1,'String','String','Without parameter Strings are compared case sensitive','boolean','Ignore case differences during comparison.',NULL,NULL,NULL,NULL,'2015-04-19 15:08:06'),
(2,1,'boolean','boolean',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-19 14:37:18'),
(3,1,'Date','Date',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-19 14:39:53'),
(6,1,'Date','String','Without parameter the String is converted to a Date using the format yyyy-MM-dd','String','Provide a date format different from the default.',NULL,NULL,NULL,NULL,'2015-04-19 15:06:04'),
(7,1,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-19 14:54:43'),
(8,1,'float','float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-19 14:55:07'),
(9,1,'integer','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-19 14:58:35'),
(10,1,'long','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-19 14:59:02'),
(11,1,'long','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-19 14:59:10'),
(12,1,'integer','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-19 14:59:18'),
(13,3,'String','String','The default is to compare the values case sensitive.','boolean','Ignore case differences during comparison.',NULL,NULL,NULL,NULL,'2015-04-20 18:06:19'),
(14,4,'String','String','The default is to compare the values case sensitive.','boolean','Ignore case differences during comparison.',NULL,NULL,NULL,NULL,'2015-04-20 18:07:27'),
(15,7,'String','String','The default is to compare the values case sensitive.','boolean','Ignore case differences during comparison.',NULL,NULL,NULL,NULL,'2015-04-20 18:08:56'),
(16,8,'String','String','The default is to compare the values case sensitive.','boolean','Ignore case differences during comparison.',NULL,NULL,NULL,NULL,'2015-04-20 18:09:05'),
(17,9,'String','String','The default is to compare the values case sensitive.','boolean','Ignore case differences during comparison.',NULL,NULL,NULL,NULL,'2015-04-20 18:09:54'),
(18,10,'String','String','The default is to compare the values case sensitive.','boolean','Ignore case differences during comparison.',NULL,NULL,NULL,NULL,'2015-04-20 18:10:05'),
(19,24,'String','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-20 18:12:53'),
(20,24,'String','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-20 18:13:43'),
(21,24,'integer','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-20 18:14:13'),
(22,24,'integer','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-20 18:14:19'),
(23,24,'long','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-20 18:14:32'),
(25,30,'String','String','First String is a comma seperated list of values','boolean','Ignore case differences during comparison.',NULL,NULL,NULL,NULL,'2015-04-20 18:19:58'),
(26,31,'String','String','First String is a comma seperated list of values','boolean','Ignore case differences during comparison.',NULL,NULL,NULL,NULL,'2015-04-20 18:20:27'),
(28,31,'String','long','First String is a comma seperated list of long values',NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-20 18:37:21'),
(29,30,'String','integer','First String is a comma seperated list of integer values',NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-20 18:37:41'),
(30,31,'String','integer','First String is a comma seperated list of integer values',NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-20 18:37:27'),
(31,30,'String','long','First String is a comma seperated list of long values',NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-20 18:37:44'),
(32,17,'double','double','The compare to value is the lower limit','double','This value defines the upper limit',NULL,NULL,NULL,NULL,'2016-04-16 20:33:46'),
(33,17,'double','String','String is comma seperated list of lower limit, upper limit',NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-21 08:56:56'),
(34,17,'float','float','The compare to value is the lower limit','float','This value defines the upper limit',NULL,NULL,NULL,NULL,'2016-04-16 20:41:51'),
(35,17,'float','String','String is comma seperated list of lower limit, upper limit',NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-21 08:58:31'),
(36,17,'integer','integer','The compare to value is the lower limit','integer','This value defines the upper limit',NULL,NULL,NULL,NULL,'2016-04-16 20:41:20'),
(37,17,'long','long','The compare to value is the lower limit','long','This value defines the upper limit',NULL,NULL,NULL,NULL,'2016-04-16 20:41:20'),
(38,17,'integer','String','String is comma seperated list of lower limit, upper limit',NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-21 09:01:03'),
(39,17,'long','String','String is comma seperated list of lower limit, upper limit',NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-21 09:01:50'),
(40,20,'String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-21 09:03:53'),
(41,2,'String','String','Without parameter Strings are compared case sensitive','boolean','Ignore case differences during comparison.',NULL,NULL,NULL,NULL,'2016-04-15 21:17:06'),
(42,2,'boolean','boolean',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-15 21:17:06'),
(43,2,'Date','Date',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-15 21:17:06'),
(44,2,'Date','String','Without parameter the String is converted to a Date using the format yyyy-MM-dd','String','Provide a date format different from the default.',NULL,NULL,NULL,NULL,'2016-04-15 21:17:06'),
(45,2,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-15 21:17:06'),
(46,2,'float','float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-15 21:17:06'),
(47,2,'integer','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-15 21:17:06'),
(48,2,'long','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-15 21:17:06'),
(49,2,'long','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-15 21:17:06'),
(50,2,'integer','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-15 21:17:06'),
(51,21,'String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-21 09:03:53'),
(52,11,'Date','Date',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 20:39:00'),
(53,11,'Date','String','Without parameter the String is converted to a Date using the format yyyy-MM-dd','String','Provide a date format different from the default.',NULL,NULL,NULL,NULL,'2016-04-15 21:49:16'),
(54,11,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-15 21:52:52'),
(55,11,'double','Integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-15 21:52:52'),
(56,11,'double','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-15 21:53:12'),
(57,11,'float','float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-15 21:56:24'),
(58,11,'float','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-15 21:56:24'),
(59,11,'integer','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-15 21:56:24'),
(60,11,'long','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-15 21:56:24'),
(61,11,'long','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-15 21:56:24'),
(62,11,'String','integer','Checks if the length of the String is greater than the integer value',NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:42:34'),
(63,11,'String','long','Checks if the length of the String is greater than the long value',NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:42:34'),
(64,11,'String','String','Compare to String values that contain dates','String','Provide the date format of the String values',NULL,NULL,NULL,NULL,'2016-04-16 16:40:39'),
(65,5,'String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:26:02'),
(66,6,'String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:27:04'),
(67,11,'double','float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:37:30'),
(68,11,'float','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:37:30'),
(69,11,'float','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:37:30'),
(70,12,'Date','Date',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 20:39:00'),
(71,12,'Date','String','Without parameter the String is converted to a Date using the format yyyy-MM-dd','String','Provide a date format different from the default.',NULL,NULL,NULL,NULL,'2016-04-16 16:54:11'),
(72,12,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:54:11'),
(73,12,'double','Integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:54:11'),
(74,12,'double','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:54:11'),
(75,12,'float','float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:54:11'),
(76,12,'float','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:54:11'),
(77,12,'integer','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:54:11'),
(78,12,'long','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:54:11'),
(79,12,'long','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:54:11'),
(80,12,'String','integer','Checks if the length of the String is smaller than the integer value',NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:55:19'),
(81,12,'String','long','Checks if the length of the String is smaller than the long value',NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:56:49'),
(82,12,'String','String','Compare to String values that contain dates','String','Provide the date format of the String values',NULL,NULL,NULL,NULL,'2016-04-16 16:54:11'),
(83,12,'double','float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:54:11'),
(84,12,'float','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:54:11'),
(85,12,'float','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:54:11'),
(86,13,'Date','Date',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 20:39:00'),
(87,13,'Date','String','Without parameter the String is converted to a Date using the format yyyy-MM-dd','String','Provide a date format different from the default.',NULL,NULL,NULL,NULL,'2016-04-16 16:55:59'),
(88,13,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:55:59'),
(89,13,'double','Integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:55:59'),
(90,13,'double','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:55:59'),
(91,13,'float','float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:55:59'),
(92,13,'float','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:55:59'),
(93,13,'integer','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:55:59'),
(94,13,'long','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:55:59'),
(95,13,'long','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:55:59'),
(96,13,'String','integer','Checks if the length of the String is greater than or equal to the integer value',NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:55:59'),
(97,13,'String','long','Checks if the length of the String is greater than or equal to the long value',NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:55:59'),
(98,13,'String','String','Compare to String values that contain dates','String','Provide the date format of the String values',NULL,NULL,NULL,NULL,'2016-04-16 16:55:59'),
(99,13,'double','float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:55:59'),
(100,13,'float','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:55:59'),
(101,13,'float','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:55:59'),
(102,14,'Date','Date',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 20:39:00'),
(103,14,'Date','String','Without parameter the String is converted to a Date using the format yyyy-MM-dd','String','Provide a date format different from the default.',NULL,NULL,NULL,NULL,'2016-04-16 16:57:42'),
(104,14,'double','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:57:42'),
(105,14,'double','Integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:57:42'),
(106,14,'double','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:57:42'),
(107,14,'float','float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:57:42'),
(108,14,'float','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:57:42'),
(109,14,'integer','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:57:42'),
(110,14,'long','integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:57:42'),
(111,14,'long','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:57:42'),
(112,14,'String','integer','Checks if the length of the String is smaller than or equal to the integer value',NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:57:42'),
(113,14,'String','long','Checks if the length of the String is smaller than or equal to the long value',NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:57:42'),
(114,14,'String','String','Compare to String values that contain dates','String','Provide the date format of the String values',NULL,NULL,NULL,NULL,'2016-04-16 16:57:42'),
(115,14,'double','float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:57:42'),
(116,14,'float','long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:57:42'),
(117,14,'float','double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 16:57:42'),
(118,15,'integer','String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 20:39:00'),
(119,15,'long','String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 20:39:00'),
(120,15,'String','String','Without parameter Strings are compared case sensitive','boolean','Define if the comparison ignores case differences',NULL,NULL,NULL,NULL,'2016-04-16 17:02:47'),
(121,16,'integer','String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 20:39:00'),
(122,16,'long','String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 20:39:00'),
(123,16,'String','String','Without parameter Strings are compared case sensitive','boolean','Define if the comparison ignores case differences',NULL,NULL,NULL,NULL,'2016-04-16 17:02:47'),
(124,18,'double','double','The compare to value is the lower limit','double','This value defines the upper limit',NULL,NULL,NULL,NULL,'2016-04-16 20:38:08'),
(125,18,'double','String','String is comma seperated list of lower limit, upper limit',NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-21 08:56:56'),
(126,18,'float','float','The compare to value is the lower limit','float','This value defines the upper limit',NULL,NULL,NULL,NULL,'2016-04-16 20:38:08'),
(127,18,'float','String','String is comma seperated list of lower limit, upper limit',NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-21 08:58:31'),
(128,18,'integer','integer','The compare to value is the lower limit','integer','This value defines the upper limit',NULL,NULL,NULL,NULL,'2016-04-16 20:38:08'),
(129,18,'long','long','The compare to value is the lower limit','long','This value defines the upper limit',NULL,NULL,NULL,NULL,'2016-04-16 20:38:08'),
(130,18,'integer','String','String is comma seperated list of lower limit, upper limit',NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-21 09:01:03'),
(131,18,'long','String','String is comma seperated list of lower limit, upper limit',NULL,NULL,NULL,NULL,NULL,NULL,'2015-04-21 09:01:50'),
(132,19,'String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 17:12:58'),
(133,29,'double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 17:15:45'),
(134,29,'float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 17:15:45'),
(135,29,'long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 17:15:45'),
(136,29,'integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 17:15:45'),
(138,22,'Date',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 17:54:12'),
(139,22,'double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 17:54:12'),
(140,22,'float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 17:54:12'),
(142,22,'integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 17:54:12'),
(143,22,'long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 17:54:12'),
(144,22,'String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 17:55:48'),
(145,23,'Date',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 17:54:12'),
(146,23,'double',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 17:54:12'),
(147,23,'float',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 17:54:12'),
(148,23,'integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 17:54:12'),
(149,23,'long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 17:54:12'),
(150,23,'String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 17:55:48'),
(151,25,'String','String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 18:53:31'),
(152,26,'String','String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 18:54:35'),
(153,27,'String','String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 18:56:49'),
(154,28,'String','String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 18:56:49'),
(155,32,'integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 19:02:00'),
(156,33,'integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 19:01:20'),
(157,34,'integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 19:03:55'),
(158,34,'long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 19:03:55'),
(159,35,'integer',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 19:03:55'),
(160,35,'long',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-04-16 19:03:55'),
(161,36,'String','String',NULL,'integer','The value for the distance between the two strings to be evaluated',NULL,NULL,NULL,NULL,'2016-04-16 20:39:00'),
(162,37,'String','String',NULL,'integer','The value for the distance between the two strings to be evaluated',NULL,NULL,NULL,NULL,'2016-04-16 20:39:00'),
(163,38,'String','String',NULL,'integer','The value for the distance between the two strings to be evaluated',NULL,NULL,NULL,NULL,'2016-04-16 20:27:19'),
(164,39,'String','String',NULL,'integer','The value for the distance between the two strings to be evaluated',NULL,NULL,NULL,NULL,'2016-04-16 20:27:19'),
(165,40,'String','String',NULL,'integer','The value for the distance between the two strings to be evaluated',NULL,NULL,NULL,NULL,'2016-04-16 20:27:19'),
(166,1,'Date','Date','Without parameter the String is converted to a Date using the format yyyy-MM-dd','String','Provide a date format different from the default.',NULL,NULL,NULL,NULL,'2016-07-05 21:50:00'),
(167,1,'boolean','String',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2017-05-25 14:50:00'),
(168,1,'String','boolean',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2017-05-25 14:50:00');
/*!40000 ALTER TABLE `check_method` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL DEFAULT '',
  `description` varchar(255) NOT NULL DEFAULT '',
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` VALUES (1,'Admin','Admin Group','2014-04-08 14:35:19');
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupuser`
--

DROP TABLE IF EXISTS `groupuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupuser` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL DEFAULT '0',
  `groups_id` int(10) unsigned NOT NULL DEFAULT '0',
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_groupuser` (`user_id`,`groups_id`),
  KEY `idx_user` (`user_id`),
  KEY `idx_group` (`groups_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupuser`
--

LOCK TABLES `groupuser` WRITE;
/*!40000 ALTER TABLE `groupuser` DISABLE KEYS */;
INSERT INTO `groupuser` VALUES (1,1,1,'2015-12-04 06:11:46');
/*!40000 ALTER TABLE `groupuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL,
  `type_id` int(10) unsigned NOT NULL,
  `user_id` int(10) DEFAULT NULL,
  `parent_1` int(10) DEFAULT NULL,
  `parent_2` int(10) DEFAULT NULL,
  `parent_3` int(10) DEFAULT NULL,
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type_typeid_userid` (`type`,`type_id`,`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history`
--

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(80) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `export_filename` varchar(255) DEFAULT NULL,
  `is_private` tinyint(1) DEFAULT '0',
  `object_classname` varchar(80) DEFAULT NULL,
  `object_method_getter` varchar(80) DEFAULT NULL,
  `object_method_setter` varchar(80) DEFAULT NULL,
  `database_hostname` varchar(80) DEFAULT NULL,
  `database_name` varchar(80) DEFAULT NULL,
  `database_tablename` varchar(80) DEFAULT NULL,
  `database_userid` varchar(80) DEFAULT NULL,
  `database_user_password` varchar(80) DEFAULT NULL,
  `last_update_user_id` int(10) DEFAULT NULL,
  `owner_user_id` int(10) DEFAULT NULL,
  `group_id` int(10) DEFAULT NULL,
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

--
-- Table structure for table `reference_fields`
--

DROP TABLE IF EXISTS `reference_fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reference_fields` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL,
  `name` varchar(80) DEFAULT NULL,
  `name_descriptive` varchar(80) NOT NULL,
  `description` varchar(120) DEFAULT NULL,
  `java_type_id` int(1) NOT NULL,
  `last_update_user_id` int(10) DEFAULT NULL,
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`project_id`,`name`),
  KEY `idx_project_id` (`project_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rule`
--

DROP TABLE IF EXISTS `rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rule` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `rulesubgroup_id` int(10) NOT NULL DEFAULT '0',
  `check_id` int(10) DEFAULT NULL,
  `last_update_user_id` int(10) DEFAULT NULL,
  `name` varchar(80) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `object1_parametertype_id` int(10) DEFAULT NULL,
  `object1_parameter` varchar(255) DEFAULT NULL,
  `object1_type_id` int(10) DEFAULT NULL,
  `object2_parametertype_id` int(10) DEFAULT NULL,
  `object2_parameter` varchar(255) DEFAULT NULL,
  `object2_type_id` int(10) DEFAULT NULL,
  `expectedvalue` varchar(255) DEFAULT NULL,
  `expectedvalue_type_id` int(10) DEFAULT NULL,
  `additional_parameter` varchar(255) DEFAULT NULL,
  `additional_parameter_type_id` int(10) DEFAULT NULL,
  `message_passed` varchar(255) NOT NULL,
  `message_failed` varchar(255) NOT NULL,
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_rulesubgroup_id` (`rulesubgroup_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule`
--

--
-- Table structure for table `rulegroup`
--

DROP TABLE IF EXISTS `rulegroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rulegroup` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` int(10) unsigned NOT NULL DEFAULT '0',
  `last_update_user_id` int(10) DEFAULT NULL,
  `name` varchar(80) NOT NULL,
  `description` varchar(255) NOT NULL,
  `valid_from` date NOT NULL,
  `valid_until` date NOT NULL,
  `dependent_rulegroup_id` int(10) unsigned DEFAULT NULL,
  `dependent_rulegroup_execute_if` varchar(6) DEFAULT NULL,
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_project_id` (`project_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rulegroup`
--

--
-- Table structure for table `rulegroupaction`
--

DROP TABLE IF EXISTS `rulegroupaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rulegroupaction` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `rulegroup_id` int(10) NOT NULL DEFAULT '0',
  `last_update_user_id` int(10) DEFAULT NULL,
  `action_id` int(10) NOT NULL,
  `name` varchar(80) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `object1_parametertype_id` int(10) DEFAULT NULL,
  `object1_parameter` varchar(255) DEFAULT NULL,
  `object1_type_id` int(10) DEFAULT NULL,
  `object2_parametertype_id` int(10) DEFAULT NULL,
  `object2_parameter` varchar(255) DEFAULT NULL,
  `object2_type_id` int(10) DEFAULT NULL,
  `object3_parametertype_id` int(10) DEFAULT NULL,
  `object3_parameter` varchar(255) DEFAULT NULL,
  `object3_type_id` int(10) DEFAULT NULL,
  `parameter1` varchar(255) DEFAULT NULL,
  `parameter1_type_id` int(10) DEFAULT NULL,
  `parameter2` varchar(255) DEFAULT NULL,
  `parameter2_type_id` int(10) DEFAULT NULL,
  `parameter3` varchar(255) DEFAULT NULL,
  `parameter3_type_id` int(10) DEFAULT NULL,
  `execute_if` varchar(20) DEFAULT NULL,
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_rulegroup_id` (`rulegroup_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rulegroupaction`
--

--
-- Table structure for table `rulesubgroup`
--

DROP TABLE IF EXISTS `rulesubgroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rulesubgroup` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `rulegroup_id` int(10) unsigned NOT NULL DEFAULT '0',
  `last_update_user_id` int(10) DEFAULT NULL,
  `name` varchar(80) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `intergroupoperator` enum('and','or') NOT NULL,
  `ruleoperator` enum('and','or') NOT NULL,
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_rulegroup_id` (`rulegroup_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rulesubgroup`
--

--
-- Table structure for table `types`
--

DROP TABLE IF EXISTS `types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `types` (
  `id` int(19) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `types`
--

LOCK TABLES `types` WRITE;
/*!40000 ALTER TABLE `types` DISABLE KEYS */;
INSERT INTO `types` VALUES (1,'string','2014-04-13 15:28:35'),
(2,'integer','2014-04-13 15:28:40'),
(3,'float','2014-04-13 15:28:47'),
(4,'double','2014-04-13 15:28:51'),
(5,'boolean','2014-04-13 15:28:57'),
(6,'long','2014-04-17 22:39:47'),
(7,'bigdecimal','2014-04-23 21:17:04'),
(8,'date','2014-05-15 19:28:37');
/*!40000 ALTER TABLE `types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userid` varchar(25) NOT NULL,
  `name` varchar(80) NOT NULL DEFAULT '',
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(80) DEFAULT NULL,
  `generated_code` VARCHAR(40) NULL DEFAULT NULL,
  `lastlogin` datetime DEFAULT NULL,
  `deactivated` tinyint(1) unsigned DEFAULT '0',
  `deactivated_date` date DEFAULT '0000-00-00',
  `password_update_date` date DEFAULT '0000-00-00',
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_userid` (`userid`),
  KEY `idx_deactivated` (`deactivated`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','admin user','*4ACFE3202A5FF5CF467898FC58AAB1D615029441',NULL,NULL,'2000-01-01 00:00:00',0,NULL,'2000-01-01','2016-03-24 20:34:24');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-24 22:14:50

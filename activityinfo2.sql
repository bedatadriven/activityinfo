-- MySQL dump 10.13  Distrib 5.7.30, for Linux (x86_64)
--
-- Host: localhost    Database: activityinfo2
-- ------------------------------------------------------
-- Server version	5.7.30-0ubuntu0.16.04.1

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
-- Table structure for table `DATABASECHANGELOG`
--

DROP TABLE IF EXISTS `DATABASECHANGELOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DATABASECHANGELOG` (
  `ID` varchar(63) NOT NULL,
  `AUTHOR` varchar(63) NOT NULL,
  `FILENAME` varchar(200) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`,`AUTHOR`,`FILENAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATABASECHANGELOG`
--

LOCK TABLES `DATABASECHANGELOG` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOG` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOG` VALUES ('001','alex','org/activityinfo/database/changelog/db.changelog-001.xml','2020-05-14 09:40:20',2,'EXECUTED','3:8f697965f0d0b78466455fbd6be72542','Drop Column (x10)','',NULL,'2.0.2-bdd'),('001-2','alex','org/activityinfo/database/changelog/db.changelog-001.xml','2020-05-14 09:40:20',3,'EXECUTED','3:f891f65f1727e70e6a4034cde5f1116f','Drop Not-Null Constraint','Not sure why this was incorrectly marked as not-null, but it should be nullable.',NULL,'2.0.2-bdd'),('001-3','alex','org/activityinfo/database/changelog/db.changelog-001.xml','2020-05-14 09:40:21',4,'EXECUTED','3:4f2155c3ddadbc51653bbfa1f2273ebe','Drop Column','Not used.',NULL,'2.0.2-bdd'),('001-4','alex','org/activityinfo/database/changelog/db.changelog-001.xml','2020-05-14 09:40:21',5,'EXECUTED','3:fb1333004dfa117cc38ab8d5c231069e','Drop Column (x4)','',NULL,'2.0.2-bdd'),('002-01','abid','org/activityinfo/database/changelog/db.changelog-002.xml','2020-05-14 09:40:21',6,'EXECUTED','3:e661bc29349b92e74c7d1f105a5a5ea8','Add Column','',NULL,'2.0.2-bdd'),('002-02','abid','org/activityinfo/database/changelog/db.changelog-002.xml','2020-05-14 09:40:21',7,'EXECUTED','3:ec2b5b9059fb391fa44264d7a5f775dd','Create Table (x2)','',NULL,'2.0.2-bdd'),('002-03','alex','org/activityinfo/database/changelog/db.changelog-002.xml','2020-05-14 09:40:21',8,'EXECUTED','3:1e4d8654d510887fdbf5e4c27e86b867','Update Data, Add Not-Null Constraint, Add Default Value','',NULL,'2.0.2-bdd'),('002-04','abid','org/activityinfo/database/changelog/db.changelog-002.xml','2020-05-14 09:40:21',9,'EXECUTED','3:107801c92b429ccf838b21d78e67181c','Create Table','',NULL,'2.0.2-bdd'),('003-01','umad','org/activityinfo/database/changelog/db.changelog-003.xml','2020-05-14 09:40:21',10,'EXECUTED','3:fe77f726ebab2c617cfee6668c2f1589','Create Table','',NULL,'2.0.2-bdd'),('003-02','umad','org/activityinfo/database/changelog/db.changelog-003.xml','2020-05-14 09:40:21',11,'EXECUTED','3:03407e0c1f3ce2d36bafc5af261a7c6a','Modify data type, Add Column (x2)','',NULL,'2.0.2-bdd'),('003-03','umad','org/activityinfo/database/changelog/db.changelog-003.xml','2020-05-14 09:40:21',12,'EXECUTED','3:1bcb39b23147692527be935007067dc9','Modify data type','',NULL,'2.0.2-bdd'),('003-04','umad','org/activityinfo/database/changelog/db.changelog-003.xml','2020-05-14 09:40:21',13,'EXECUTED','3:05cbc647e9500f2ef86e503e917bd237','Add Column','',NULL,'2.0.2-bdd'),('003-05','umad','org/activityinfo/database/changelog/db.changelog-003.xml','2020-05-14 09:40:21',14,'EXECUTED','3:48e0dc623e2a2fe9ac2ea91ccad6e273','Drop Column','',NULL,'2.0.2-bdd'),('003-06','umad','org/activityinfo/database/changelog/db.changelog-003.xml','2020-05-14 09:40:22',15,'EXECUTED','3:cb385b4afd7dd305cfce0ac393cc645f','Add Column','',NULL,'2.0.2-bdd'),('004-01','alex','org/activityinfo/database/changelog/db.changelog-004-timestamps.xml','2020-05-14 09:40:22',16,'EXECUTED','3:08fdbafdaf6502792bd86491756d9e1c','Add Column, Custom SQL','',NULL,'2.0.2-bdd'),('004-02','alex','org/activityinfo/database/changelog/db.changelog-004-timestamps.xml','2020-05-14 09:40:22',17,'EXECUTED','3:e0a96e0be9276ef5a472d04c3457f755','Add Column, Custom SQL','',NULL,'2.0.2-bdd'),('005-01','alex','org/activityinfo/database/changelog/db.changelog-005-subscriptions.xml','2020-05-14 09:40:22',18,'EXECUTED','3:9adb725122ff79af2733ac52047dc355','Rename Column, Add Column','',NULL,'2.0.2-bdd'),('006-01','alex','org/activityinfo/database/changelog/db.changelog-006-report-vis.xml','2020-05-14 09:40:22',19,'EXECUTED','3:41dba44d626020121561cf8cac8ce272','Create Table','',NULL,'2.0.2-bdd'),('006-02','alex','org/activityinfo/database/changelog/db.changelog-006-report-vis.xml','2020-05-14 09:40:22',20,'EXECUTED','3:9ed74446ca5da3a03930af4566ab669b','Add Column','',NULL,'2.0.2-bdd'),('006-03','alex','org/activityinfo/database/changelog/db.changelog-006-report-vis.xml','2020-05-14 09:40:22',21,'EXECUTED','3:861774626d94405938941f1d433837a1','Drop Column (x2)','',NULL,'2.0.2-bdd'),('007-01','alex','org/activityinfo/database/changelog/db.changelog-007-fieldcleanup.xml','2020-05-14 09:40:22',22,'EXECUTED','3:2e19c6a73c5ceb204b23892104c4ab48','Drop Column (x2), Add Unique Constraint','',NULL,'2.0.2-bdd'),('008-01-changed','alex','org/activityinfo/database/changelog/db.changelog-008-charsets.xml','2020-05-14 09:40:24',23,'EXECUTED','3:33c66a7ffe637694f6d6ce60d309a6fe','Modify data type (x2), Custom SQL','Shortens string keys because MySQL reserves 4-bytes for each character\n			when using utfmb4 and imposes a maximum key length of 1000.',NULL,'2.0.2-bdd'),('009-01','alex','org/activityinfo/database/changelog/db.changelog-009-schema-updates.xml','2020-05-14 09:40:24',24,'EXECUTED','3:422d66bde030d65a7bf879f3094eb987','Add Column (x2), Add Default Value (x2), Custom SQL','We\'re using the timeEdited field as a rough versioning mechanism to detect\n			when users need to synchronize, so we want pretty good resolution, and \n			we don\'t need MySQL mucking around with the timestamps',NULL,'2.0.2-bdd'),('010-01','alex','org/activityinfo/database/changelog/db.changelog-010-indicator.xml','2020-05-14 09:40:24',25,'EXECUTED','3:3537b62e59d0a73eb5eddd6358d6213a','Modify data type','Increase the length of allowable indicator names',NULL,'2.0.2-bdd'),('011-01','alex','org/activityinfo/database/changelog/db.changelog-011-polygons.xml','2020-05-14 09:40:24',26,'EXECUTED','3:f761c5d1038ff95906ea9b22782f0873','Add Column','Increase the length of allowable indicator names',NULL,'2.0.2-bdd'),('011-02','alex','org/activityinfo/database/changelog/db.changelog-011-polygons.xml','2020-05-14 09:40:24',27,'EXECUTED','3:f286ba875bd6f27dedb49ce594d344c2','Drop Column','Cleans up disused field',NULL,'2.0.2-bdd'),('012-01','jasper','org/activityinfo/database/changelog/db.changelog-012-email-notifications.xml','2020-05-14 09:40:24',28,'EXECUTED','3:908fbdff278d02b7987f1001c455c703','Add Column','',NULL,'2.0.2-bdd'),('013-01','jasper','org/activityinfo/database/changelog/db.changelog-013-site-history.xml','2020-05-14 09:40:24',29,'EXECUTED','3:e4da8bd097bae99b04317dec4d2d01ce','Create Table','Creates the sitehistory table, storing sitedata as json',NULL,'2.0.2-bdd'),('013-02','jasper','org/activityinfo/database/changelog/db.changelog-013-site-history.xml','2020-05-14 09:40:24',30,'EXECUTED','3:03fc747c1d50381690edf0bece2b5ade','Modify data type','Alters the sitehistory table, changing the json column to varchar(5000)',NULL,'2.0.2-bdd'),('013-03','alex','org/activityinfo/database/changelog/db.changelog-013-site-history.xml','2020-05-14 09:40:24',31,'EXECUTED','3:9f85d8f710b843aec3b2035327430803','Modify data type','Alters the sitehistory table, changing the json column back to longtext following\n			fix in rebar library',NULL,'2.0.2-bdd'),('014-01','alex','org/activityinfo/database/changelog/db.changelog-014-refine.xml','2020-05-14 09:40:25',32,'EXECUTED','3:fd70bd00055e6bac2111f3e2f7d9abe6','Create Table','Creates an index table for admin entities',NULL,'2.0.2-bdd'),('015-02','alex','org/activityinfo/database/changelog/db.changelog-015-geoadmin.xml','2020-05-14 09:40:25',33,'EXECUTED','3:b0a500bbd4b06d1a558be365fe764b7d','Custom SQL','Updates locationtype table to be autoincrement. We have to first remove a \n      record with id=0 that was somehow added in the past. There is actually data \n      bound to it out there, so we can drop it completely, but we can remove it long\n      eno...',NULL,'2.0.2-bdd'),('015-03','alex','org/activityinfo/database/changelog/db.changelog-015-geoadmin.xml','2020-05-14 09:40:25',34,'EXECUTED','3:da401a86b0212cd3efbb8e414e56916c','Add Column','',NULL,'2.0.2-bdd'),('015-04','alex','org/activityinfo/database/changelog/db.changelog-015-geoadmin.xml','2020-05-14 09:40:25',35,'EXECUTED','3:b8ad32334a83faa75efceed4831f1d2e','Create Table','',NULL,'2.0.2-bdd'),('015-05','alex','org/activityinfo/database/changelog/db.changelog-015-geoadmin.xml','2020-05-14 09:40:25',36,'EXECUTED','3:aa0bd2e0628fefd0bc360b95f65ae206','Add Column (x2)','',NULL,'2.0.2-bdd'),('015-06','alex','org/activityinfo/database/changelog/db.changelog-015-geoadmin.xml','2020-05-14 09:40:25',37,'EXECUTED','3:8c1de2079bf0fbfe8fbdfed3e243bb35','Add Column (x5)','',NULL,'2.0.2-bdd'),('015-07','alex','org/activityinfo/database/changelog/db.changelog-015-geoadmin.xml','2020-05-14 09:40:25',38,'EXECUTED','3:0b9e3ed044545cc305fdd97eddd4d53e','Add Foreign Key Constraint','',NULL,'2.0.2-bdd'),('015-08','alex','org/activityinfo/database/changelog/db.changelog-015-geoadmin.xml','2020-05-14 09:40:25',39,'EXECUTED','3:02e813502881a5cb1cec42c98af77c17','Add Column','',NULL,'2.0.2-bdd'),('016-01','jasper','org/activityinfo/database/changelog/db.changelog-016-issues.xml','2020-05-14 09:40:25',40,'EXECUTED','3:72b2c98f8c3e86224f75c3ec6377850b','Add Column','#47050153 - Adds \'mandatory\' flag to the indicator table.',NULL,'2.0.2-bdd'),('016-02','jasper','org/activityinfo/database/changelog/db.changelog-016-issues.xml','2020-05-14 09:40:25',41,'EXECUTED','3:6563e3101c33fcc9d8546cb1c0ed382a','Add Column','#47050153 - Adds \'mandatory\' flag to the attributegroup table.',NULL,'2.0.2-bdd'),('017-01','jasper','org/activityinfo/database/changelog/db.changelog-017-opensignup.xml','2020-05-14 09:40:26',42,'EXECUTED','3:023b69d74029d3f5a1631462b4982508','Add Column (x2), Drop Column (x2)','#46186875 Open signup - Cleaning up userlogin table',NULL,'2.0.2-bdd'),('017-02','jasper','org/activityinfo/database/changelog/db.changelog-017-opensignup.xml','2020-05-14 09:40:26',43,'EXECUTED','3:45d44c9d8b622df593c8262a8bd4ad28','Add Column','[#49384031] Record inviting user in UserLogin',NULL,'2.0.2-bdd'),('017-03','alex','org/activityinfo/database/changelog/db.changelog-017-opensignup.xml','2020-05-14 09:40:26',44,'EXECUTED','3:d1f44e30a4fbb9f8510e2e986db0ce77','Add Column','Record account creation date',NULL,'2.0.2-bdd'),('018-01','alex','org/activityinfo/database/changelog/db.changelog-018-import.xml','2020-05-14 09:40:26',45,'EXECUTED','3:a21d5834f6f20c5a18a67207bddb5d11','Drop Unique Constraint','Batch import',NULL,'2.0.2-bdd'),('019-01','jasper','org/activityinfo/database/changelog/db.changelog-019-domains.xml','2020-05-14 09:40:26',46,'EXECUTED','3:bec9b62823154802d134f8e014d8b095','Create Table','Creates the domain table',NULL,'2.0.2-bdd'),('019-02','jasper','org/activityinfo/database/changelog/db.changelog-019-domains.xml','2020-05-14 09:40:26',47,'EXECUTED','3:4484452b847c21f820d2a41bbb6b1cc8','Custom SQL, Add Foreign Key Constraint','',NULL,'2.0.2-bdd'),('019-04','alex','org/activityinfo/database/changelog/db.changelog-019-domains.xml','2020-05-14 09:40:26',48,'EXECUTED','3:db6538083d69d9fbd8796c47d186ac1c','Add Column','',NULL,'2.0.2-bdd'),('019-05','alex','org/activityinfo/database/changelog/db.changelog-019-domains.xml','2020-05-14 09:40:26',49,'EXECUTED','3:3fbfee675b572939a2e5286da025ed18','Add Column','',NULL,'2.0.2-bdd'),('019-06','alex','org/activityinfo/database/changelog/db.changelog-019-domains.xml','2020-05-14 09:40:26',50,'EXECUTED','3:cb91f6186587e23803db4eccf26fd76c','Drop Not-Null Constraint','Precursor to dropping the column',NULL,'2.0.2-bdd'),('019-07','alex','org/activityinfo/database/changelog/db.changelog-019-domains.xml','2020-05-14 09:40:26',51,'EXECUTED','3:b30eee629500bf7adcf647f7c639363c','Add Column','',NULL,'2.0.2-bdd'),('initial-20111102','alex (generated)','org/activityinfo/database/changelog/db.changelog-000-initial.xml','2020-05-14 09:40:20',1,'EXECUTED','3:d114cf0b8219d6e803dbd4da4433a532','Create Table (x27), Add Primary Key (x6), Add Foreign Key Constraint, Create Index (x47)','',NULL,'2.0.2-bdd');
/*!40000 ALTER TABLE `DATABASECHANGELOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATABASECHANGELOGLOCK`
--

DROP TABLE IF EXISTS `DATABASECHANGELOGLOCK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DATABASECHANGELOGLOCK` (
  `ID` int(11) NOT NULL,
  `LOCKED` tinyint(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATABASECHANGELOGLOCK`
--

LOCK TABLES `DATABASECHANGELOGLOCK` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOGLOCK` VALUES (1,0,NULL,NULL);
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity` (
  `ActivityId` int(11) NOT NULL AUTO_INCREMENT,
  `AllowEdit` bit(1) NOT NULL,
  `category` varchar(255) DEFAULT NULL,
  `dateDeleted` datetime DEFAULT NULL,
  `mapIcon` varchar(255) DEFAULT NULL,
  `Name` varchar(45) NOT NULL,
  `ReportingFrequency` int(11) NOT NULL,
  `SortOrder` int(11) NOT NULL,
  `DatabaseId` int(11) NOT NULL,
  `LocationTypeId` int(11) NOT NULL,
  `published` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ActivityId`),
  KEY `FKA126572F494BD9E` (`DatabaseId`),
  KEY `FKA126572F8C0165BB` (`LocationTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
INSERT INTO `activity` VALUES (1,_binary '\0',NULL,NULL,NULL,'School rehabilitation',0,1,1,6,0);
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `adminentity`
--

DROP TABLE IF EXISTS `adminentity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adminentity` (
  `AdminEntityId` int(11) NOT NULL AUTO_INCREMENT,
  `x1` double DEFAULT NULL,
  `x2` double DEFAULT NULL,
  `y1` double DEFAULT NULL,
  `y2` double DEFAULT NULL,
  `Code` varchar(15) DEFAULT NULL,
  `Name` varchar(50) NOT NULL,
  `Soundex` varchar(50) DEFAULT NULL,
  `AdminLevelId` int(11) NOT NULL,
  `AdminEntityParentId` int(11) DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `geometry` geometry DEFAULT NULL,
  PRIMARY KEY (`AdminEntityId`),
  KEY `FK2E3083F227F5CAC7` (`AdminLevelId`),
  KEY `FK2E3083F2FF2BADA7` (`AdminEntityParentId`),
  CONSTRAINT `fk_entity_parent` FOREIGN KEY (`AdminEntityParentId`) REFERENCES `adminentity` (`AdminEntityId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adminentity`
--

LOCK TABLES `adminentity` WRITE;
/*!40000 ALTER TABLE `adminentity` DISABLE KEYS */;
/*!40000 ALTER TABLE `adminentity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `adminentity_soundex`
--

DROP TABLE IF EXISTS `adminentity_soundex`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adminentity_soundex` (
  `id` int(11) NOT NULL,
  `levelid` int(11) NOT NULL,
  `soundex` varchar(255) NOT NULL,
  `score` double NOT NULL,
  KEY `fk_adminentity_soundex_adminlevel` (`levelid`),
  KEY `fk_adminentity_soundex_adminentity` (`id`),
  CONSTRAINT `fk_adminentity_soundex_adminentity` FOREIGN KEY (`id`) REFERENCES `adminentity` (`AdminEntityId`),
  CONSTRAINT `fk_adminentity_soundex_adminlevel` FOREIGN KEY (`levelid`) REFERENCES `adminlevel` (`AdminLevelId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adminentity_soundex`
--

LOCK TABLES `adminentity_soundex` WRITE;
/*!40000 ALTER TABLE `adminentity_soundex` DISABLE KEYS */;
/*!40000 ALTER TABLE `adminentity_soundex` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `adminlevel`
--

DROP TABLE IF EXISTS `adminlevel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adminlevel` (
  `AdminLevelId` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(30) NOT NULL,
  `CountryId` int(11) NOT NULL,
  `ParentId` int(11) DEFAULT NULL,
  `polygons` tinyint(1) NOT NULL DEFAULT '1',
  `version` int(11) NOT NULL DEFAULT '1',
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`AdminLevelId`),
  KEY `FK9EC33D95B6676E25` (`CountryId`),
  KEY `FK9EC33D95E01B109C` (`ParentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adminlevel`
--

LOCK TABLES `adminlevel` WRITE;
/*!40000 ALTER TABLE `adminlevel` DISABLE KEYS */;
/*!40000 ALTER TABLE `adminlevel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `adminlevelversion`
--

DROP TABLE IF EXISTS `adminlevelversion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adminlevelversion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `adminlevelid` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  `timecreated` bigint(20) NOT NULL DEFAULT '0',
  `json` longtext,
  `sourceMetadata` longtext,
  `sourceFilename` longtext,
  `sourceUrl` longtext,
  `sourceHash` char(32) DEFAULT NULL,
  `message` longtext,
  PRIMARY KEY (`id`),
  KEY `fk_adminlevelversion` (`adminlevelid`),
  KEY `fk_adminlevelversion_userlogin` (`userid`),
  CONSTRAINT `fk_adminlevelversion` FOREIGN KEY (`adminlevelid`) REFERENCES `adminlevel` (`AdminLevelId`),
  CONSTRAINT `fk_adminlevelversion_userlogin` FOREIGN KEY (`userid`) REFERENCES `userlogin` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adminlevelversion`
--

LOCK TABLES `adminlevelversion` WRITE;
/*!40000 ALTER TABLE `adminlevelversion` DISABLE KEYS */;
/*!40000 ALTER TABLE `adminlevelversion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attribute`
--

DROP TABLE IF EXISTS `attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute` (
  `AttributeId` int(11) NOT NULL AUTO_INCREMENT,
  `dateDeleted` datetime DEFAULT NULL,
  `Name` varchar(50) NOT NULL,
  `SortOrder` int(11) NOT NULL,
  `AttributeGroupId` int(11) NOT NULL,
  PRIMARY KEY (`AttributeId`),
  KEY `FK7839CA7CDA7C5E3` (`AttributeGroupId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attribute`
--

LOCK TABLES `attribute` WRITE;
/*!40000 ALTER TABLE `attribute` DISABLE KEYS */;
INSERT INTO `attribute` VALUES (1,NULL,'USAID',0,1),(2,NULL,'ECHO',0,1);
/*!40000 ALTER TABLE `attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attributegroup`
--

DROP TABLE IF EXISTS `attributegroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attributegroup` (
  `AttributeGroupId` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(50) DEFAULT NULL,
  `dateDeleted` datetime DEFAULT NULL,
  `multipleAllowed` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sortOrder` int(11) NOT NULL,
  `mandatory` bit(1) NOT NULL,
  PRIMARY KEY (`AttributeGroupId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attributegroup`
--

LOCK TABLES `attributegroup` WRITE;
/*!40000 ALTER TABLE `attributegroup` DISABLE KEYS */;
INSERT INTO `attributegroup` VALUES (1,NULL,NULL,_binary '\0','Donor',0,_binary '\0');
/*!40000 ALTER TABLE `attributegroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attributegroupinactivity`
--

DROP TABLE IF EXISTS `attributegroupinactivity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attributegroupinactivity` (
  `AttributeGroupId` int(11) NOT NULL,
  `ActivityId` int(11) NOT NULL,
  PRIMARY KEY (`ActivityId`,`AttributeGroupId`),
  KEY `FKDD8C951780BF17DB` (`ActivityId`),
  KEY `FKDD8C9517DA7C5E3` (`AttributeGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attributegroupinactivity`
--

LOCK TABLES `attributegroupinactivity` WRITE;
/*!40000 ALTER TABLE `attributegroupinactivity` DISABLE KEYS */;
INSERT INTO `attributegroupinactivity` VALUES (1,1);
/*!40000 ALTER TABLE `attributegroupinactivity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attributevalue`
--

DROP TABLE IF EXISTS `attributevalue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attributevalue` (
  `AttributeId` int(11) NOT NULL,
  `SiteId` int(11) NOT NULL,
  `Value` bit(1) DEFAULT NULL,
  PRIMARY KEY (`AttributeId`,`SiteId`),
  KEY `FK4ED7045544C2434B` (`SiteId`),
  KEY `FK4ED70455AFED0B31` (`AttributeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attributevalue`
--

LOCK TABLES `attributevalue` WRITE;
/*!40000 ALTER TABLE `attributevalue` DISABLE KEYS */;
INSERT INTO `attributevalue` VALUES (1,1023366582,_binary '\0'),(1,1771303120,_binary '\0'),(2,1023366582,_binary ''),(2,1771303120,_binary '');
/*!40000 ALTER TABLE `attributevalue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authentication`
--

DROP TABLE IF EXISTS `authentication`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authentication` (
  `AuthToken` varchar(32) NOT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `dateLastActive` datetime DEFAULT NULL,
  `UserId` int(11) NOT NULL,
  PRIMARY KEY (`AuthToken`),
  KEY `FKDDEEAE9848B34B53` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authentication`
--

LOCK TABLES `authentication` WRITE;
/*!40000 ALTER TABLE `authentication` DISABLE KEYS */;
INSERT INTO `authentication` VALUES ('5d4887a36ca9f1fe3e37d0470b3db31b','2020-05-14 09:53:31','2020-05-14 09:53:31',1),('be0291d74947511d18c34936ba9a8f82','2020-05-14 09:53:08','2020-05-14 09:53:08',1);
/*!40000 ALTER TABLE `authentication` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `basemap`
--

DROP TABLE IF EXISTS `basemap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `basemap` (
  `id` varchar(50) NOT NULL,
  `copyright` longtext,
  `maxZoom` int(11) NOT NULL,
  `minZoom` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `tileUrlPattern` longtext,
  `thumbnailUrl` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `basemap`
--

LOCK TABLES `basemap` WRITE;
/*!40000 ALTER TABLE `basemap` DISABLE KEYS */;
/*!40000 ALTER TABLE `basemap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country` (
  `CountryId` int(11) NOT NULL AUTO_INCREMENT,
  `x1` double NOT NULL,
  `x2` double NOT NULL,
  `y1` double NOT NULL,
  `y2` double NOT NULL,
  `ISO2` varchar(2) DEFAULT NULL,
  `Name` varchar(50) NOT NULL,
  PRIMARY KEY (`CountryId`)
) ENGINE=InnoDB AUTO_INCREMENT=495 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country`
--

LOCK TABLES `country` WRITE;
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
INSERT INTO `country` VALUES (1,12.18794184,31.306,-13.45599996,5.386098154,NULL,'RDC'),(2,-74.7,-70,17.3,20.4,NULL,'Haïti'),(249,-180,180,-90,180,'AF','Afghanistan'),(250,-180,180,-90,180,'AX','Åland Islands'),(251,-180,180,-90,180,'AL','Albania'),(252,-180,180,-90,180,'DZ','Algeria'),(253,-180,180,-90,180,'AS','American Samoa'),(254,-180,180,-90,180,'AD','Andorra'),(255,-180,180,-90,180,'AO','Angola'),(256,-180,180,-90,180,'AI','Anguilla'),(257,-180,180,-90,180,'AQ','Antarctica'),(258,-180,180,-90,180,'AG','Antigua and Barbuda'),(259,-180,180,-90,180,'AR','Argentina'),(260,-180,180,-90,180,'AM','Armenia'),(261,-180,180,-90,180,'AW','Aruba'),(262,-180,180,-90,180,'AU','Australia'),(263,-180,180,-90,180,'AT','Austria'),(264,-180,180,-90,180,'AZ','Azerbaijan'),(265,-180,180,-90,180,'BS','Bahamas'),(266,-180,180,-90,180,'BH','Bahrain'),(267,-180,180,-90,180,'BD','Bangladesh'),(268,-180,180,-90,180,'BB','Barbados'),(269,-180,180,-90,180,'BY','Belarus'),(270,-180,180,-90,180,'BE','Belgium'),(271,-180,180,-90,180,'BZ','Belize'),(272,-180,180,-90,180,'BJ','Benin'),(273,-180,180,-90,180,'BM','Bermuda'),(274,-180,180,-90,180,'BT','Bhutan'),(275,-180,180,-90,180,'BO','Bolivia'),(276,-180,180,-90,180,'BA','Bosnia And Herzegovina'),(277,-180,180,-90,180,'BW','Botswana'),(278,-180,180,-90,180,'BV','Bouvet Island'),(279,-180,180,-90,180,'BR','Brazil'),(280,-180,180,-90,180,'IO','British Indian Ocean Territory'),(281,-180,180,-90,180,'B','Brunei Darussalam'),(282,-180,180,-90,180,'BG','Bulgaria'),(283,-180,180,-90,180,'BF','Burkina Faso'),(284,-180,180,-90,180,'BI','Burundi'),(285,-180,180,-90,180,'KH','Cambodia'),(286,-180,180,-90,180,'CM','Cameroon'),(287,-180,180,-90,180,'CA','Canada'),(288,-180,180,-90,180,'CV','Cape Verde'),(289,-180,180,-90,180,'KY','Cayman Islands'),(290,-180,180,-90,180,'CF','Central African Republic'),(291,-180,180,-90,180,'TD','Chad'),(292,-180,180,-90,180,'CL','Chile'),(293,-180,180,-90,180,'C','China'),(294,-180,180,-90,180,'CX','Christmas Island'),(295,-180,180,-90,180,'CC','Cocos (Keeling) Islands'),(296,-180,180,-90,180,'CO','Colombia'),(297,-180,180,-90,180,'KM','Comoros'),(298,-180,180,-90,180,'CG','Congo'),(300,-180,180,-90,180,'CK','Cook Islands'),(301,-180,180,-90,180,'CR','Costa Rica'),(302,-180,180,-90,180,'CI','Côte D\'Ivoire'),(303,-180,180,-90,180,'HR','Croatia'),(304,-180,180,-90,180,'CU','Cuba'),(305,-180,180,-90,180,'CY','Cyprus'),(306,-180,180,-90,180,'CZ','Czech Republic'),(307,-180,180,-90,180,'DK','Denmark'),(308,-180,180,-90,180,'DJ','Djibouti'),(309,-180,180,-90,180,'DM','Dominica'),(310,-180,180,-90,180,'DO','Dominican Republic'),(311,-180,180,-90,180,'EC','Ecuador'),(312,-180,180,-90,180,'EG','Egypt'),(313,-180,180,-90,180,'SV','El Salvador'),(314,-180,180,-90,180,'GQ','Equatorial Guinea'),(315,-180,180,-90,180,'ER','Eritrea'),(316,-180,180,-90,180,'EE','Estonia'),(317,-180,180,-90,180,'ET','Ethiopia'),(318,-180,180,-90,180,'FK','Falkland Islands (Malvinas)'),(319,-180,180,-90,180,'FO','Faroe Islands'),(320,-180,180,-90,180,'FJ','Fiji'),(321,-180,180,-90,180,'FI','Finland'),(322,-180,180,-90,180,'FR','France'),(323,-180,180,-90,180,'GF','French Guiana'),(324,-180,180,-90,180,'PF','French Polynesia'),(325,-180,180,-90,180,'TF','French Southern Territories'),(326,-180,180,-90,180,'GA','Gabo'),(327,-180,180,-90,180,'GM','Gambia'),(328,-180,180,-90,180,'GE','Georgia'),(329,-180,180,-90,180,'DE','Germany'),(330,-180,180,-90,180,'GH','Ghana'),(331,-180,180,-90,180,'GI','Gibraltar'),(332,-180,180,-90,180,'GR','Greece'),(333,-180,180,-90,180,'GL','Greenland'),(334,-180,180,-90,180,'GD','Grenada'),(335,-180,180,-90,180,'GP','Guadeloupe'),(336,-180,180,-90,180,'GU','Guam'),(337,-180,180,-90,180,'GT','Guatemala'),(338,-180,180,-90,180,'GG','Guernsey'),(339,-180,180,-90,180,'G','Guinea'),(340,-180,180,-90,180,'GW','Guinea-Bissau'),(341,-180,180,-90,180,'GY','Guyana'),(343,-180,180,-90,180,'HM','Heard Island and Mcdonald Islands'),(344,-180,180,-90,180,'VA','Vatican City'),(345,-180,180,-90,180,'H','Honduras'),(346,-180,180,-90,180,'HK','Hong Kong'),(347,-180,180,-90,180,'HU','Hungary'),(348,-180,180,-90,180,'IS','Iceland'),(349,-180,180,-90,180,'I','India'),(350,-180,180,-90,180,'ID','Indonesia'),(351,-180,180,-90,180,'IR','Iran'),(352,-180,180,-90,180,'IQ','Iraq'),(353,-180,180,-90,180,'IE','Ireland'),(354,-180,180,-90,180,'IM','Isle of Man'),(355,-180,180,-90,180,'IL','Israel'),(356,-180,180,-90,180,'IT','Italy'),(357,-180,180,-90,180,'JM','Jamaica'),(358,-180,180,-90,180,'JP','Japan'),(359,-180,180,-90,180,'JE','Jersey'),(360,-180,180,-90,180,'JO','Jordan'),(361,-180,180,-90,180,'KZ','Kazakhstan'),(362,-180,180,-90,180,'KE','Kenya'),(363,-180,180,-90,180,'KI','Kiribati'),(364,-180,180,-90,180,'KP','Democratic People\'s Republic of Korea'),(365,-180,180,-90,180,'KR','Republic of Korea'),(366,-180,180,-90,180,'KW','Kuwait'),(367,-180,180,-90,180,'KG','Kyrgyzstan'),(368,-180,180,-90,180,'LA','Laos'),(369,-180,180,-90,180,'LV','Latvia'),(370,-180,180,-90,180,'LB','Lebanon'),(371,-180,180,-90,180,'LS','Lesotho'),(372,-180,180,-90,180,'LR','Liberia'),(373,-180,180,-90,180,'LY','Libyan Arab Jamahiriya'),(374,-180,180,-90,180,'LI','Liechtenstein'),(375,-180,180,-90,180,'LT','Lithuania'),(376,-180,180,-90,180,'LU','Luxembourg'),(377,-180,180,-90,180,'MO','Macao'),(378,-180,180,-90,180,'MK','Macedonia, FYRO'),(379,-180,180,-90,180,'MG','Madagascar'),(380,-180,180,-90,180,'MW','Malawi'),(381,-180,180,-90,180,'MY','Malaysia'),(382,-180,180,-90,180,'MV','Maldives'),(383,-180,180,-90,180,'ML','Mali'),(384,-180,180,-90,180,'MT','Malta'),(385,-180,180,-90,180,'MH','Marshall Islands'),(386,-180,180,-90,180,'MQ','Martinique'),(387,-180,180,-90,180,'MR','Mauritania'),(388,-180,180,-90,180,'MU','Mauritius'),(389,-180,180,-90,180,'YT','Mayotte'),(390,-180,180,-90,180,'MX','Mexico'),(391,-180,180,-90,180,'FM','Micronesia'),(392,-180,180,-90,180,'MD','Moldova'),(393,-180,180,-90,180,'MC','Monaco'),(394,-180,180,-90,180,'M','Mongolia'),(395,-180,180,-90,180,'ME','Montenegro'),(396,-180,180,-90,180,'MS','Montserrat'),(397,-180,180,-90,180,'MA','Morocco'),(398,-180,180,-90,180,'MZ','Mozambique'),(399,-180,180,-90,180,'MM','Myanmar'),(400,-180,180,-90,180,'NA','Namibia'),(401,-180,180,-90,180,'NR','Nauru'),(402,-180,180,-90,180,'NP','Nepal'),(403,-180,180,-90,180,'NL','Netherlands'),(404,-180,180,-90,180,'A','Netherlands Antilles'),(405,-180,180,-90,180,'NC','New Caledonia'),(406,-180,180,-90,180,'NZ','New Zealand'),(407,-180,180,-90,180,'NI','Nicaragua'),(408,-180,180,-90,180,'NE','Niger'),(409,-180,180,-90,180,'NG','Nigeria'),(410,-180,180,-90,180,'NU','Niue'),(411,-180,180,-90,180,'NF','Norfolk Island'),(412,-180,180,-90,180,'MP','Northern Mariana Islands'),(413,-180,180,-90,180,'NO','Norway'),(414,-180,180,-90,180,'OM','Oman'),(415,-180,180,-90,180,'PK','Pakistan'),(416,-180,180,-90,180,'PW','Palau'),(417,-180,180,-90,180,'PS','Palestinian Territory, Occupied'),(418,-180,180,-90,180,'PA','Panama'),(419,-180,180,-90,180,'PG','Papua New Guinea'),(420,-180,180,-90,180,'PY','Paraguay'),(421,-180,180,-90,180,'PE','Peru'),(422,-180,180,-90,180,'PH','Philippines'),(423,-180,180,-90,180,'P','Pitcair'),(424,-180,180,-90,180,'PL','Poland'),(425,-180,180,-90,180,'PT','Portugal'),(426,-180,180,-90,180,'PR','Puerto Rico'),(427,-180,180,-90,180,'QA','Qatar'),(428,-180,180,-90,180,'RE','Réunion'),(429,-180,180,-90,180,'RO','Romania'),(430,-180,180,-90,180,'RU','Russian Federation'),(431,-180,180,-90,180,'RW','Rwanda'),(432,-180,180,-90,180,'BL','Saint Barthélemy'),(433,-180,180,-90,180,'SH','Saint Helena'),(434,-180,180,-90,180,'K','Saint Kitts And Nevis'),(435,-180,180,-90,180,'LC','Saint Lucia'),(436,-180,180,-90,180,'MF','Saint Martin'),(437,-180,180,-90,180,'PM','Saint Pierre And Miquelon'),(438,-180,180,-90,180,'VC','Saint Vincent And The Grenadines'),(439,-180,180,-90,180,'WS','Samoa'),(440,-180,180,-90,180,'SM','San Marino'),(441,-180,180,-90,180,'ST','Sao Tome And Principe'),(442,-180,180,-90,180,'SA','Saudi Arabia'),(443,-180,180,-90,180,'S','Senegal'),(444,-180,180,-90,180,'RS','Serbia'),(445,-180,180,-90,180,'SC','Seychelles'),(446,-180,180,-90,180,'SL','Sierra Leone'),(447,-180,180,-90,180,'SG','Singapore'),(448,-180,180,-90,180,'SK','Slovakia'),(449,-180,180,-90,180,'SI','Slovenia'),(450,-180,180,-90,180,'SB','Solomon Islands'),(451,-180,180,-90,180,'SO','Somalia'),(452,-180,180,-90,180,'ZA','South Africa'),(453,-180,180,-90,180,'GS','South Georgia And The South Sandwich Islands'),(454,-180,180,-90,180,'ES','Spain'),(455,-180,180,-90,180,'LK','Sri Lanka'),(456,-180,180,-90,180,'SD','Sudan'),(457,-180,180,-90,180,'SR','Suriname'),(458,-180,180,-90,180,'SJ','Svalbard And Jan Maye'),(459,-180,180,-90,180,'SZ','Swaziland'),(460,-180,180,-90,180,'SE','Sweden'),(461,-180,180,-90,180,'CH','Switzerland'),(462,-180,180,-90,180,'SY','Syrian Arab Republic'),(463,-180,180,-90,180,'TW','Taiwan, Province Of China'),(464,-180,180,-90,180,'TJ','Tajikistan'),(465,-180,180,-90,180,'TZ','Tanzania, United Republic Of'),(466,-180,180,-90,180,'TH','Thailand'),(467,-180,180,-90,180,'TL','Timor-Leste'),(468,-180,180,-90,180,'TG','Togo'),(469,-180,180,-90,180,'TK','Tokelau'),(470,-180,180,-90,180,'TO','Tonga'),(471,-180,180,-90,180,'TT','Trinidad And Tobago'),(472,-180,180,-90,180,'T','Tunisia'),(473,-180,180,-90,180,'TR','Turkey'),(474,-180,180,-90,180,'TM','Turkmenistan'),(475,-180,180,-90,180,'TC','Turks And Caicos Islands'),(476,-180,180,-90,180,'TV','Tuvalu'),(477,-180,180,-90,180,'UG','Uganda'),(478,-180,180,-90,180,'UA','Ukraine'),(479,-180,180,-90,180,'AE','United Arab Emirates'),(480,-180,180,-90,180,'GB','United Kingdom'),(481,-180,180,-90,180,'US','United States'),(482,-180,180,-90,180,'UM','United States Minor Outlying Islands'),(483,-180,180,-90,180,'UY','Uruguay'),(484,-180,180,-90,180,'UZ','Uzbekistan'),(485,-180,180,-90,180,'VU','Vanuatu'),(486,-180,180,-90,180,'VE','Venezuela, Bolivarian Republic Of'),(487,-180,180,-90,180,'V','Viet Nam'),(488,-180,180,-90,180,'VG','Virgin Islands, British'),(489,-180,180,-90,180,'VI','Virgin Islands, U.S.'),(490,-180,180,-90,180,'WF','Wallis And Futuna'),(491,-180,180,-90,180,'EH','Western Sahara'),(492,-180,180,-90,180,'YE','Yemen'),(493,-180,180,-90,180,'ZM','Zambia'),(494,-180,180,-90,180,'ZW','Zimbabwe');
/*!40000 ALTER TABLE `country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `domain`
--

DROP TABLE IF EXISTS `domain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `domain` (
  `host` varchar(100) NOT NULL,
  `title` varchar(100) NOT NULL,
  `bucket` varchar(100) DEFAULT NULL,
  `scaffolding` longtext,
  `homePageBody` longtext,
  `signUpAllowed` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`host`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `domain`
--

LOCK TABLES `domain` WRITE;
/*!40000 ALTER TABLE `domain` DISABLE KEYS */;
/*!40000 ALTER TABLE `domain` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `indicator`
--

DROP TABLE IF EXISTS `indicator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `indicator` (
  `IndicatorId` int(11) NOT NULL AUTO_INCREMENT,
  `Aggregation` int(11) NOT NULL,
  `Category` varchar(50) DEFAULT NULL,
  `dateDeleted` datetime DEFAULT NULL,
  `description` longtext,
  `ListHeader` varchar(30) DEFAULT NULL,
  `name` longtext,
  `SortOrder` int(11) NOT NULL,
  `Units` varchar(15) NOT NULL,
  `ActivityId` int(11) NOT NULL,
  `mandatory` bit(1) NOT NULL,
  PRIMARY KEY (`IndicatorId`),
  KEY `FK4D01DDEF80BF17DB` (`ActivityId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `indicator`
--

LOCK TABLES `indicator` WRITE;
/*!40000 ALTER TABLE `indicator` DISABLE KEYS */;
INSERT INTO `indicator` VALUES (1,0,NULL,NULL,NULL,'Children','Number of children',0,'children',1,_binary '\0');
/*!40000 ALTER TABLE `indicator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `indicatorlink`
--

DROP TABLE IF EXISTS `indicatorlink`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `indicatorlink` (
  `SourceIndicatorId` int(11) NOT NULL,
  `DestinationIndicatorId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `indicatorlink`
--

LOCK TABLES `indicatorlink` WRITE;
/*!40000 ALTER TABLE `indicatorlink` DISABLE KEYS */;
/*!40000 ALTER TABLE `indicatorlink` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `indicatorvalue`
--

DROP TABLE IF EXISTS `indicatorvalue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `indicatorvalue` (
  `IndicatorId` int(11) NOT NULL,
  `ReportingPeriodId` int(11) NOT NULL,
  `Value` double NOT NULL,
  PRIMARY KEY (`IndicatorId`,`ReportingPeriodId`),
  KEY `FK676020C247C62157` (`IndicatorId`),
  KEY `FK676020C284811DB7` (`ReportingPeriodId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `indicatorvalue`
--

LOCK TABLES `indicatorvalue` WRITE;
/*!40000 ALTER TABLE `indicatorvalue` DISABLE KEYS */;
INSERT INTO `indicatorvalue` VALUES (1,1022404710,66),(1,1771880243,55);
/*!40000 ALTER TABLE `indicatorvalue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location` (
  `LocationID` int(11) NOT NULL,
  `Axe` varchar(50) DEFAULT NULL,
  `LocationGuid` varchar(36) DEFAULT NULL,
  `Name` varchar(50) NOT NULL,
  `X` double DEFAULT NULL,
  `Y` double DEFAULT NULL,
  `LocationTypeID` int(11) NOT NULL,
  `timeEdited` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`LocationID`),
  KEY `FK752A03D58C0165BB` (`LocationTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
INSERT INTO `location` VALUES (820188514,NULL,NULL,'Lyon',4.822997222222222,45.77518611111111,6,1589443186866);
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locationadminlink`
--

DROP TABLE IF EXISTS `locationadminlink`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `locationadminlink` (
  `AdminEntityId` int(11) NOT NULL,
  `LocationId` int(11) NOT NULL,
  PRIMARY KEY (`LocationId`,`AdminEntityId`),
  KEY `FK50408394368DDFA7` (`LocationId`),
  KEY `FK50408394CD1204FD` (`AdminEntityId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locationadminlink`
--

LOCK TABLES `locationadminlink` WRITE;
/*!40000 ALTER TABLE `locationadminlink` DISABLE KEYS */;
/*!40000 ALTER TABLE `locationadminlink` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locationtype`
--

DROP TABLE IF EXISTS `locationtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `locationtype` (
  `locationtypeid` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  `Reuse` bit(1) NOT NULL,
  `BoundAdminLevelId` int(11) DEFAULT NULL,
  `CountryId` int(11) NOT NULL,
  PRIMARY KEY (`locationtypeid`),
  KEY `FK65214AF20FEB745` (`BoundAdminLevelId`),
  KEY `FK65214AFB6676E25` (`CountryId`)
) ENGINE=InnoDB AUTO_INCREMENT=257 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locationtype`
--

LOCK TABLES `locationtype` WRITE;
/*!40000 ALTER TABLE `locationtype` DISABLE KEYS */;
INSERT INTO `locationtype` VALUES (1,'Zone de Sante',_binary '\0',7,1),(2,'Village',_binary '\0',NULL,1),(3,'Village',_binary '\0',NULL,2),(4,'Village',_binary '\0',NULL,249),(5,'Village',_binary '\0',NULL,250),(6,'Village',_binary '\0',NULL,251),(7,'Village',_binary '\0',NULL,252),(8,'Village',_binary '\0',NULL,253),(9,'Village',_binary '\0',NULL,254),(10,'Village',_binary '\0',NULL,255),(11,'Village',_binary '\0',NULL,256),(12,'Village',_binary '\0',NULL,257),(13,'Village',_binary '\0',NULL,258),(14,'Village',_binary '\0',NULL,259),(15,'Village',_binary '\0',NULL,260),(16,'Village',_binary '\0',NULL,261),(17,'Village',_binary '\0',NULL,262),(18,'Village',_binary '\0',NULL,263),(19,'Village',_binary '\0',NULL,264),(20,'Village',_binary '\0',NULL,265),(21,'Village',_binary '\0',NULL,266),(22,'Village',_binary '\0',NULL,267),(23,'Village',_binary '\0',NULL,268),(24,'Village',_binary '\0',NULL,269),(25,'Village',_binary '\0',NULL,270),(26,'Village',_binary '\0',NULL,271),(27,'Village',_binary '\0',NULL,272),(28,'Village',_binary '\0',NULL,273),(29,'Village',_binary '\0',NULL,274),(30,'Village',_binary '\0',NULL,275),(31,'Village',_binary '\0',NULL,276),(32,'Village',_binary '\0',NULL,277),(33,'Village',_binary '\0',NULL,278),(34,'Village',_binary '\0',NULL,279),(35,'Village',_binary '\0',NULL,280),(36,'Village',_binary '\0',NULL,281),(37,'Village',_binary '\0',NULL,282),(38,'Village',_binary '\0',NULL,283),(39,'Village',_binary '\0',NULL,284),(40,'Village',_binary '\0',NULL,285),(41,'Village',_binary '\0',NULL,286),(42,'Village',_binary '\0',NULL,287),(43,'Village',_binary '\0',NULL,288),(44,'Village',_binary '\0',NULL,289),(45,'Village',_binary '\0',NULL,290),(46,'Village',_binary '\0',NULL,291),(47,'Village',_binary '\0',NULL,292),(48,'Village',_binary '\0',NULL,293),(49,'Village',_binary '\0',NULL,294),(50,'Village',_binary '\0',NULL,295),(51,'Village',_binary '\0',NULL,296),(52,'Village',_binary '\0',NULL,297),(53,'Village',_binary '\0',NULL,298),(54,'Village',_binary '\0',NULL,300),(55,'Village',_binary '\0',NULL,301),(56,'Village',_binary '\0',NULL,302),(57,'Village',_binary '\0',NULL,303),(58,'Village',_binary '\0',NULL,304),(59,'Village',_binary '\0',NULL,305),(60,'Village',_binary '\0',NULL,306),(61,'Village',_binary '\0',NULL,307),(62,'Village',_binary '\0',NULL,308),(63,'Village',_binary '\0',NULL,309),(64,'Village',_binary '\0',NULL,310),(65,'Village',_binary '\0',NULL,311),(66,'Village',_binary '\0',NULL,312),(67,'Village',_binary '\0',NULL,313),(68,'Village',_binary '\0',NULL,314),(69,'Village',_binary '\0',NULL,315),(70,'Village',_binary '\0',NULL,316),(71,'Village',_binary '\0',NULL,317),(72,'Village',_binary '\0',NULL,318),(73,'Village',_binary '\0',NULL,319),(74,'Village',_binary '\0',NULL,320),(75,'Village',_binary '\0',NULL,321),(76,'Village',_binary '\0',NULL,322),(77,'Village',_binary '\0',NULL,323),(78,'Village',_binary '\0',NULL,324),(79,'Village',_binary '\0',NULL,325),(80,'Village',_binary '\0',NULL,326),(81,'Village',_binary '\0',NULL,327),(82,'Village',_binary '\0',NULL,328),(83,'Village',_binary '\0',NULL,329),(84,'Village',_binary '\0',NULL,330),(85,'Village',_binary '\0',NULL,331),(86,'Village',_binary '\0',NULL,332),(87,'Village',_binary '\0',NULL,333),(88,'Village',_binary '\0',NULL,334),(89,'Village',_binary '\0',NULL,335),(90,'Village',_binary '\0',NULL,336),(91,'Village',_binary '\0',NULL,337),(92,'Village',_binary '\0',NULL,338),(93,'Village',_binary '\0',NULL,339),(94,'Village',_binary '\0',NULL,340),(95,'Village',_binary '\0',NULL,341),(96,'Village',_binary '\0',NULL,343),(97,'Village',_binary '\0',NULL,344),(98,'Village',_binary '\0',NULL,345),(99,'Village',_binary '\0',NULL,346),(100,'Village',_binary '\0',NULL,347),(101,'Village',_binary '\0',NULL,348),(102,'Village',_binary '\0',NULL,349),(103,'Village',_binary '\0',NULL,350),(104,'Village',_binary '\0',NULL,351),(105,'Village',_binary '\0',NULL,352),(106,'Village',_binary '\0',NULL,353),(107,'Village',_binary '\0',NULL,354),(108,'Village',_binary '\0',NULL,355),(109,'Village',_binary '\0',NULL,356),(110,'Village',_binary '\0',NULL,357),(111,'Village',_binary '\0',NULL,358),(112,'Village',_binary '\0',NULL,359),(113,'Village',_binary '\0',NULL,360),(114,'Village',_binary '\0',NULL,361),(115,'Village',_binary '\0',NULL,362),(116,'Village',_binary '\0',NULL,363),(117,'Village',_binary '\0',NULL,364),(118,'Village',_binary '\0',NULL,365),(119,'Village',_binary '\0',NULL,366),(120,'Village',_binary '\0',NULL,367),(121,'Village',_binary '\0',NULL,368),(122,'Village',_binary '\0',NULL,369),(123,'Village',_binary '\0',NULL,370),(124,'Village',_binary '\0',NULL,371),(125,'Village',_binary '\0',NULL,372),(126,'Village',_binary '\0',NULL,373),(127,'Village',_binary '\0',NULL,374),(128,'Village',_binary '\0',NULL,375),(129,'Village',_binary '\0',NULL,376),(130,'Village',_binary '\0',NULL,377),(131,'Village',_binary '\0',NULL,378),(132,'Village',_binary '\0',NULL,379),(133,'Village',_binary '\0',NULL,380),(134,'Village',_binary '\0',NULL,381),(135,'Village',_binary '\0',NULL,382),(136,'Village',_binary '\0',NULL,383),(137,'Village',_binary '\0',NULL,384),(138,'Village',_binary '\0',NULL,385),(139,'Village',_binary '\0',NULL,386),(140,'Village',_binary '\0',NULL,387),(141,'Village',_binary '\0',NULL,388),(142,'Village',_binary '\0',NULL,389),(143,'Village',_binary '\0',NULL,390),(144,'Village',_binary '\0',NULL,391),(145,'Village',_binary '\0',NULL,392),(146,'Village',_binary '\0',NULL,393),(147,'Village',_binary '\0',NULL,394),(148,'Village',_binary '\0',NULL,395),(149,'Village',_binary '\0',NULL,396),(150,'Village',_binary '\0',NULL,397),(151,'Village',_binary '\0',NULL,398),(152,'Village',_binary '\0',NULL,399),(153,'Village',_binary '\0',NULL,400),(154,'Village',_binary '\0',NULL,401),(155,'Village',_binary '\0',NULL,402),(156,'Village',_binary '\0',NULL,403),(157,'Village',_binary '\0',NULL,404),(158,'Village',_binary '\0',NULL,405),(159,'Village',_binary '\0',NULL,406),(160,'Village',_binary '\0',NULL,407),(161,'Village',_binary '\0',NULL,408),(162,'Village',_binary '\0',NULL,409),(163,'Village',_binary '\0',NULL,410),(164,'Village',_binary '\0',NULL,411),(165,'Village',_binary '\0',NULL,412),(166,'Village',_binary '\0',NULL,413),(167,'Village',_binary '\0',NULL,414),(168,'Village',_binary '\0',NULL,415),(169,'Village',_binary '\0',NULL,416),(170,'Village',_binary '\0',NULL,417),(171,'Village',_binary '\0',NULL,418),(172,'Village',_binary '\0',NULL,419),(173,'Village',_binary '\0',NULL,420),(174,'Village',_binary '\0',NULL,421),(175,'Village',_binary '\0',NULL,422),(176,'Village',_binary '\0',NULL,423),(177,'Village',_binary '\0',NULL,424),(178,'Village',_binary '\0',NULL,425),(179,'Village',_binary '\0',NULL,426),(180,'Village',_binary '\0',NULL,427),(181,'Village',_binary '\0',NULL,428),(182,'Village',_binary '\0',NULL,429),(183,'Village',_binary '\0',NULL,430),(184,'Village',_binary '\0',NULL,431),(185,'Village',_binary '\0',NULL,432),(186,'Village',_binary '\0',NULL,433),(187,'Village',_binary '\0',NULL,434),(188,'Village',_binary '\0',NULL,435),(189,'Village',_binary '\0',NULL,436),(190,'Village',_binary '\0',NULL,437),(191,'Village',_binary '\0',NULL,438),(192,'Village',_binary '\0',NULL,439),(193,'Village',_binary '\0',NULL,440),(194,'Village',_binary '\0',NULL,441),(195,'Village',_binary '\0',NULL,442),(196,'Village',_binary '\0',NULL,443),(197,'Village',_binary '\0',NULL,444),(198,'Village',_binary '\0',NULL,445),(199,'Village',_binary '\0',NULL,446),(200,'Village',_binary '\0',NULL,447),(201,'Village',_binary '\0',NULL,448),(202,'Village',_binary '\0',NULL,449),(203,'Village',_binary '\0',NULL,450),(204,'Village',_binary '\0',NULL,451),(205,'Village',_binary '\0',NULL,452),(206,'Village',_binary '\0',NULL,453),(207,'Village',_binary '\0',NULL,454),(208,'Village',_binary '\0',NULL,455),(209,'Village',_binary '\0',NULL,456),(210,'Village',_binary '\0',NULL,457),(211,'Village',_binary '\0',NULL,458),(212,'Village',_binary '\0',NULL,459),(213,'Village',_binary '\0',NULL,460),(214,'Village',_binary '\0',NULL,461),(215,'Village',_binary '\0',NULL,462),(216,'Village',_binary '\0',NULL,463),(217,'Village',_binary '\0',NULL,464),(218,'Village',_binary '\0',NULL,465),(219,'Village',_binary '\0',NULL,466),(220,'Village',_binary '\0',NULL,467),(221,'Village',_binary '\0',NULL,468),(222,'Village',_binary '\0',NULL,469),(223,'Village',_binary '\0',NULL,470),(224,'Village',_binary '\0',NULL,471),(225,'Village',_binary '\0',NULL,472),(226,'Village',_binary '\0',NULL,473),(227,'Village',_binary '\0',NULL,474),(228,'Village',_binary '\0',NULL,475),(229,'Village',_binary '\0',NULL,476),(230,'Village',_binary '\0',NULL,477),(231,'Village',_binary '\0',NULL,478),(232,'Village',_binary '\0',NULL,479),(233,'Village',_binary '\0',NULL,480),(234,'Village',_binary '\0',NULL,481),(235,'Village',_binary '\0',NULL,482),(236,'Village',_binary '\0',NULL,483),(237,'Village',_binary '\0',NULL,484),(238,'Village',_binary '\0',NULL,485),(239,'Village',_binary '\0',NULL,486),(240,'Village',_binary '\0',NULL,487),(241,'Village',_binary '\0',NULL,488),(242,'Village',_binary '\0',NULL,489),(243,'Village',_binary '\0',NULL,490),(244,'Village',_binary '\0',NULL,491),(245,'Village',_binary '\0',NULL,492),(246,'Village',_binary '\0',NULL,493),(247,'Village',_binary '\0',NULL,494);
/*!40000 ALTER TABLE `locationtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lockedperiod`
--

DROP TABLE IF EXISTS `lockedperiod`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lockedperiod` (
  `LockedPeriodId` int(11) NOT NULL AUTO_INCREMENT,
  `FromDate` datetime NOT NULL,
  `Name` varchar(255) NOT NULL,
  `ToDate` datetime NOT NULL,
  `UserDatabaseId` int(11) DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `ActivityId` int(11) DEFAULT NULL,
  `ProjectId` int(11) DEFAULT NULL,
  PRIMARY KEY (`LockedPeriodId`),
  KEY `FK7CF5F98BE4EA806B` (`ProjectId`),
  CONSTRAINT `FK7CF5F98BE4EA806B` FOREIGN KEY (`ProjectId`) REFERENCES `project` (`ProjectId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lockedperiod`
--

LOCK TABLES `lockedperiod` WRITE;
/*!40000 ALTER TABLE `lockedperiod` DISABLE KEYS */;
/*!40000 ALTER TABLE `lockedperiod` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partner`
--

DROP TABLE IF EXISTS `partner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `partner` (
  `PartnerId` int(11) NOT NULL AUTO_INCREMENT,
  `FullName` varchar(64) DEFAULT NULL,
  `Name` varchar(16) NOT NULL,
  `location_LocationID` int(11) DEFAULT NULL,
  `id_org_unit_model` int(11) DEFAULT NULL,
  `organization_id_organization` int(11) DEFAULT NULL,
  PRIMARY KEY (`PartnerId`),
  KEY `FK33F574A8350D2271` (`location_LocationID`),
  KEY `FK33F574A84BA27D70` (`id_org_unit_model`),
  KEY `FK33F574A8CF94C360` (`organization_id_organization`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partner`
--

LOCK TABLES `partner` WRITE;
/*!40000 ALTER TABLE `partner` DISABLE KEYS */;
INSERT INTO `partner` VALUES (1,NULL,'NRC',NULL,NULL,NULL);
/*!40000 ALTER TABLE `partner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partnerindatabase`
--

DROP TABLE IF EXISTS `partnerindatabase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `partnerindatabase` (
  `PartnerId` int(11) NOT NULL,
  `DatabaseId` int(11) NOT NULL,
  PRIMARY KEY (`DatabaseId`,`PartnerId`),
  KEY `FKA9A62C88494BD9E` (`DatabaseId`),
  KEY `FKA9A62C8879D901C9` (`PartnerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partnerindatabase`
--

LOCK TABLES `partnerindatabase` WRITE;
/*!40000 ALTER TABLE `partnerindatabase` DISABLE KEYS */;
INSERT INTO `partnerindatabase` VALUES (1,1);
/*!40000 ALTER TABLE `partnerindatabase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `ProjectId` int(11) NOT NULL AUTO_INCREMENT,
  `dateDeleted` datetime DEFAULT NULL,
  `description` longtext,
  `name` varchar(30) NOT NULL,
  `DatabaseId` int(11) NOT NULL,
  PRIMARY KEY (`ProjectId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectreportmodel`
--

DROP TABLE IF EXISTS `projectreportmodel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectreportmodel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectreportmodel`
--

LOCK TABLES `projectreportmodel` WRITE;
/*!40000 ALTER TABLE `projectreportmodel` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectreportmodel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reportingperiod`
--

DROP TABLE IF EXISTS `reportingperiod`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reportingperiod` (
  `ReportingPeriodId` int(11) NOT NULL,
  `comments` longtext,
  `Date1` date NOT NULL,
  `Date2` date NOT NULL,
  `DateCreated` datetime NOT NULL,
  `dateDeleted` datetime DEFAULT NULL,
  `DateEdited` datetime NOT NULL,
  `SiteId` int(11) NOT NULL,
  PRIMARY KEY (`ReportingPeriodId`),
  KEY `FKDCFE056F44C2434B` (`SiteId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reportingperiod`
--

LOCK TABLES `reportingperiod` WRITE;
/*!40000 ALTER TABLE `reportingperiod` DISABLE KEYS */;
INSERT INTO `reportingperiod` VALUES (1022404710,NULL,'2020-05-14','2020-05-22','2020-05-14 10:00:13',NULL,'2020-05-14 10:00:13',1023366582),(1771880243,NULL,'2020-05-14','2020-05-15','2020-05-14 09:59:47',NULL,'2020-05-14 09:59:47',1771303120);
/*!40000 ALTER TABLE `reportingperiod` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reportsubscription`
--

DROP TABLE IF EXISTS `reportsubscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reportsubscription` (
  `reportid` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `subscribed` bit(1) NOT NULL,
  `invitingUserId` int(11) DEFAULT NULL,
  `dashboard` tinyint(1) DEFAULT '0',
  `emaildelivery` varchar(25) DEFAULT 'NONE',
  `emailday` int(11) DEFAULT '1',
  PRIMARY KEY (`reportid`,`userId`),
  KEY `FK35F790911741F030` (`reportid`),
  KEY `FK35F7909148B34B53` (`userId`),
  KEY `FK35F7909173633C59` (`invitingUserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reportsubscription`
--

LOCK TABLES `reportsubscription` WRITE;
/*!40000 ALTER TABLE `reportsubscription` DISABLE KEYS */;
/*!40000 ALTER TABLE `reportsubscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reporttemplate`
--

DROP TABLE IF EXISTS `reporttemplate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reporttemplate` (
  `ReportTemplateId` int(11) NOT NULL AUTO_INCREMENT,
  `dateDeleted` datetime DEFAULT NULL,
  `description` longtext,
  `title` varchar(255) DEFAULT NULL,
  `visibility` int(11) DEFAULT NULL,
  `xml` longtext NOT NULL,
  `DatabaseId` int(11) DEFAULT NULL,
  `OwnerUserId` int(11) NOT NULL,
  `json` longtext,
  PRIMARY KEY (`ReportTemplateId`),
  KEY `FKC69DDEE494BD9E` (`DatabaseId`),
  KEY `FKC69DDEEA5C52BC6` (`OwnerUserId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reporttemplate`
--

LOCK TABLES `reporttemplate` WRITE;
/*!40000 ALTER TABLE `reporttemplate` DISABLE KEYS */;
INSERT INTO `reporttemplate` VALUES (1,NULL,NULL,NULL,1,'<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><report><filter><dateRange/></filter><elements><pivotTable><filter><dateRange/></filter><columns><dimension type=\"date\" dateUnit=\"year\"/><dimension type=\"date\" dateUnit=\"month\"/></columns><rows><dimension type=\"Partner\"/></rows></pivotTable></elements><id>0</id></report>',NULL,1,NULL);
/*!40000 ALTER TABLE `reporttemplate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reportvisibility`
--

DROP TABLE IF EXISTS `reportvisibility`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reportvisibility` (
  `reportid` int(11) DEFAULT NULL,
  `databaseid` int(11) DEFAULT NULL,
  `defaultDashboard` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reportvisibility`
--

LOCK TABLES `reportvisibility` WRITE;
/*!40000 ALTER TABLE `reportvisibility` DISABLE KEYS */;
/*!40000 ALTER TABLE `reportvisibility` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site`
--

DROP TABLE IF EXISTS `site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site` (
  `SiteId` int(11) NOT NULL,
  `comments` longtext,
  `Date1` date DEFAULT NULL,
  `Date2` date DEFAULT NULL,
  `DateCreated` datetime NOT NULL,
  `dateDeleted` datetime DEFAULT NULL,
  `DateEdited` datetime NOT NULL,
  `DateSynchronized` datetime DEFAULT NULL,
  `SiteGuid` varchar(36) DEFAULT NULL,
  `ActivityId` int(11) NOT NULL,
  `LocationId` int(11) NOT NULL,
  `PartnerId` int(11) NOT NULL,
  `ProjectId` int(11) DEFAULT NULL,
  `timeEdited` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`SiteId`),
  KEY `FK275367368DDFA7` (`LocationId`),
  KEY `FK27536779D901C9` (`PartnerId`),
  KEY `FK27536780BF17DB` (`ActivityId`),
  KEY `FK275367E4EA806B` (`ProjectId`),
  KEY `FK275367F87ABA8F` (`ProjectId`),
  KEY `deleted` (`dateDeleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site`
--

LOCK TABLES `site` WRITE;
/*!40000 ALTER TABLE `site` DISABLE KEYS */;
INSERT INTO `site` VALUES (1023366582,NULL,'2020-05-14','2020-05-22','2020-05-14 10:00:13',NULL,'2020-05-14 10:00:13',NULL,NULL,1,820188514,1,NULL,1589443212856),(1771303120,NULL,'2020-05-14','2020-05-15','2020-05-14 09:59:47',NULL,'2020-05-14 09:59:47',NULL,NULL,1,820188514,1,NULL,1589443186902);
/*!40000 ALTER TABLE `site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `siteattachment`
--

DROP TABLE IF EXISTS `siteattachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `siteattachment` (
  `blobid` varchar(50) NOT NULL,
  `siteid` int(11) NOT NULL,
  `filename` varchar(255) NOT NULL,
  `uploadedby` int(11) DEFAULT NULL,
  `blobsize` double DEFAULT NULL,
  `contenttype` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`blobid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `siteattachment`
--

LOCK TABLES `siteattachment` WRITE;
/*!40000 ALTER TABLE `siteattachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `siteattachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sitehistory`
--

DROP TABLE IF EXISTS `sitehistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sitehistory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `siteid` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  `timecreated` bigint(20) NOT NULL DEFAULT '0',
  `initial` bit(1) NOT NULL DEFAULT b'0',
  `json` longtext,
  PRIMARY KEY (`id`),
  KEY `fk_sitehistory_userlogin` (`userid`),
  KEY `fk_sitehistory_site` (`siteid`),
  CONSTRAINT `fk_sitehistory_site` FOREIGN KEY (`siteid`) REFERENCES `site` (`SiteId`),
  CONSTRAINT `fk_sitehistory_userlogin` FOREIGN KEY (`userid`) REFERENCES `userlogin` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sitehistory`
--

LOCK TABLES `sitehistory` WRITE;
/*!40000 ALTER TABLE `sitehistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `sitehistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `target`
--

DROP TABLE IF EXISTS `target`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `target` (
  `targetId` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `Date1` datetime NOT NULL,
  `Date2` datetime NOT NULL,
  `ProjectId` int(11) DEFAULT NULL,
  `PartnerId` int(11) DEFAULT NULL,
  `AdminEntityId` int(11) DEFAULT NULL,
  `DatabaseId` int(11) NOT NULL,
  PRIMARY KEY (`targetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `target`
--

LOCK TABLES `target` WRITE;
/*!40000 ALTER TABLE `target` DISABLE KEYS */;
/*!40000 ALTER TABLE `target` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `targetvalue`
--

DROP TABLE IF EXISTS `targetvalue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `targetvalue` (
  `TargetId` int(11) NOT NULL,
  `IndicatorId` int(11) NOT NULL,
  `Value` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `targetvalue`
--

LOCK TABLES `targetvalue` WRITE;
/*!40000 ALTER TABLE `targetvalue` DISABLE KEYS */;
/*!40000 ALTER TABLE `targetvalue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userdatabase`
--

DROP TABLE IF EXISTS `userdatabase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userdatabase` (
  `DatabaseId` int(11) NOT NULL AUTO_INCREMENT,
  `dateDeleted` datetime DEFAULT NULL,
  `FullName` varchar(50) DEFAULT NULL,
  `lastSchemaUpdate` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `Name` varchar(16) NOT NULL,
  `StartDate` date DEFAULT NULL,
  `CountryId` int(11) NOT NULL,
  `OwnerUserId` int(11) NOT NULL,
  `version` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`DatabaseId`),
  KEY `FK46AEBA86A5C52BC6` (`OwnerUserId`),
  KEY `FK46AEBA86B6676E25` (`CountryId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userdatabase`
--

LOCK TABLES `userdatabase` WRITE;
/*!40000 ALTER TABLE `userdatabase` DISABLE KEYS */;
INSERT INTO `userdatabase` VALUES (1,NULL,NULL,'1970-01-01 00:00:00','PEAR',NULL,251,1,1589443144530);
/*!40000 ALTER TABLE `userdatabase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userlogin`
--

DROP TABLE IF EXISTS `userlogin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userlogin` (
  `UserId` int(11) NOT NULL AUTO_INCREMENT,
  `changePasswordKey` varchar(34) DEFAULT NULL,
  `dateChangePasswordKeyIssued` datetime DEFAULT NULL,
  `Email` varchar(75) NOT NULL,
  `Password` varchar(150) DEFAULT NULL,
  `Locale` varchar(10) NOT NULL,
  `Name` varchar(50) NOT NULL,
  `emailnotification` tinyint(1) DEFAULT '0',
  `organization` varchar(100) DEFAULT NULL,
  `jobtitle` varchar(100) DEFAULT NULL,
  `invitedBy` int(11) DEFAULT NULL,
  `dateCreated` date NOT NULL DEFAULT '1970-01-01',
  PRIMARY KEY (`UserId`),
  UNIQUE KEY `Email` (`Email`),
  KEY `fk_userlogin_invitedby` (`invitedBy`),
  CONSTRAINT `fk_userlogin_invitedby` FOREIGN KEY (`invitedBy`) REFERENCES `userlogin` (`UserId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userlogin`
--

LOCK TABLES `userlogin` WRITE;
/*!40000 ALTER TABLE `userlogin` DISABLE KEYS */;
INSERT INTO `userlogin` VALUES (1,NULL,NULL,'test@example.com','$2a$10$Xj0niXV4in.qZEJvvqeHj.SbLd9GkYvg3BQCvpamtc7BImksDL2Li','en','Admin',0,NULL,NULL,NULL,'1970-01-01');
/*!40000 ALTER TABLE `userlogin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userpermission`
--

DROP TABLE IF EXISTS `userpermission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userpermission` (
  `UserPermissionId` int(11) NOT NULL AUTO_INCREMENT,
  `AllowDesign` bit(1) NOT NULL,
  `AllowEdit` bit(1) NOT NULL,
  `AllowEditAll` bit(1) NOT NULL,
  `allowManageAllUsers` bit(1) NOT NULL,
  `allowManageUsers` bit(1) NOT NULL,
  `AllowView` bit(1) NOT NULL,
  `AllowViewAll` bit(1) NOT NULL,
  `lastSchemaUpdate` datetime DEFAULT '1970-01-01 00:00:00',
  `DatabaseId` int(11) NOT NULL,
  `PartnerId` int(11) NOT NULL,
  `UserId` int(11) NOT NULL,
  `version` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`UserPermissionId`),
  KEY `FKD265581A48B34B53` (`UserId`),
  KEY `FKD265581A494BD9E` (`DatabaseId`),
  KEY `FKD265581A79D901C9` (`PartnerId`),
  CONSTRAINT `fk_userpermission_userlogin` FOREIGN KEY (`UserId`) REFERENCES `userlogin` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userpermission`
--

LOCK TABLES `userpermission` WRITE;
/*!40000 ALTER TABLE `userpermission` DISABLE KEYS */;
/*!40000 ALTER TABLE `userpermission` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-14 10:01:27

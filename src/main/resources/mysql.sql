-- MySQL dump 10.11
--
-- Host: localhost    Database: fps
-- ------------------------------------------------------
-- Server version       5.0.84

/*
Creating fps database

root@debian64:~# mysql -p
Enter password:

mysql> CREATE DATABASE fps CHARACTER SET utf8 COLLATE utf8_general_ci;
Query OK, 1 row affected (0.00 sec)

mysql> use mysql
mysql> select host, user from user;

                                 192.168.56.10 - we connect from that machine
mysql> GRANT ALL ON *.* to root@'192.168.56.10' IDENTIFIED BY 'password';
Query OK, 0 rows affected (0.00 sec)

mysql> FLUSH PRIVILEGES;
Query OK, 0 rows affected (0.00 sec)
*/

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
-- Table structure for table `SLIPLINES`
--

DROP TABLE IF EXISTS `SLIPLINES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SLIPLINES` (
  `ID` int(11) NOT NULL auto_increment,
  `FKID_SLIP` int(11) NOT NULL,
  `NAME` varchar(40) NOT NULL,
  `AMOUNT` double NOT NULL,
  `PRICE` double NOT NULL,
  `TAXRATE` char(5) NOT NULL,
  `DISCOUNT` double NOT NULL default '0',
  `DISCOUNT_TYPE` int(11) NOT NULL default '0',
  PRIMARY KEY  (`ID`),
  KEY `FK_SLIPLINES` (`FKID_SLIP`),
  CONSTRAINT `FK_SLIPLINES` FOREIGN KEY (`FKID_SLIP`) REFERENCES `SLIPS` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=325 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SLIPPAYMENTS`
--

DROP TABLE IF EXISTS `SLIPPAYMENTS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SLIPPAYMENTS` (
  `ID` int(11) NOT NULL auto_increment,
  `FKID_SLIP` int(11) NOT NULL,
  `AMOUNT` double NOT NULL,
  `NAME` varchar(16) default NULL,
  `FORM` char(10) NOT NULL,
  PRIMARY KEY  (`ID`),
  KEY `FK_SLIPPAYMENTS` (`FKID_SLIP`),
  CONSTRAINT `FK_SLIPPAYMENTS` FOREIGN KEY (`FKID_SLIP`) REFERENCES `SLIPS` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=217 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SLIPS`
--

DROP TABLE IF EXISTS `SLIPS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SLIPS` (
  `ID` int(11) NOT NULL auto_increment,
  `CASHBOXLOGO` char(8) NOT NULL,
  `EXTERNALREFERENCE` char(16) NOT NULL,
  `CASHIERNAME` varchar(32) NOT NULL default 'Operator',
  `CREATED_TS` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `PRINTED_TS` timestamp NULL default NULL,
  `STAGE` int(11) NOT NULL default '0',
  `ERROR` varchar(120) NOT NULL default 'OK',
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `EXTERNALREFERENCE` (`EXTERNALREFERENCE`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-01-21 20:16:33






DROP TABLE IF EXISTS `INVOICES`;
CREATE TABLE `INVOICES` (
  `ID` int(11) NOT NULL auto_increment,
  `CASHBOXLOGO` char(8) NOT NULL,
  `EXTERNALREFERENCE` char(16) NOT NULL,
  `HEADER` varchar(32) NOT NULL default 'Naglowek Sprzedawcy',
  `NIP` varchar(15) NOT NULL default '813-188-60-14',
  `PMTDUE` varchar(32) NOT NULL default 'nigdy',
  `PMTTYPE` varchar(32) NOT NULL default 'przelew',
  `CASHIERNAME` varchar(32) NOT NULL default 'Operator',
  `CREATED_TS` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `PRINTED_TS` timestamp NULL default NULL,
  `STAGE` int(11) NOT NULL default '0',
  `ERROR` varchar(120) NOT NULL default 'OK',
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `EXTERNALREFERENCE` (`EXTERNALREFERENCE`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `INVOICELINES`;
CREATE TABLE `INVOICELINES` (
  `ID` int(11) NOT NULL auto_increment,
  `FKID_INVOICE` int(11) NOT NULL,
  `NAME` varchar(40) NOT NULL,
  `AMOUNT` double NOT NULL,
  `PRICE` double NOT NULL,
  `TAXRATE` char(5) NOT NULL,
  `DISCOUNT` double NOT NULL default '0',
  `DISCOUNT_TYPE` int(11) NOT NULL default '0',
  PRIMARY KEY  (`ID`),
  KEY `FK_INVOICELINES` (`FKID_INVOICE`),
  CONSTRAINT `FK_INVOICELINES` FOREIGN KEY (`FKID_INVOICE`) REFERENCES `INVOICES` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=325 DEFAULT CHARSET=latin1;

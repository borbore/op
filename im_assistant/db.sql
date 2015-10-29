/*
SQLyog Ultimate v11.42 (64 bit)
MySQL - 5.6.20-enterprise-commercial-advanced : Database - suneee
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`suneee` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `suneee`;

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `username` char(40) COLLATE utf8_bin NOT NULL,
  `password` char(41) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `t_user` */

insert  into `t_user`(`userid`,`username`,`password`) values (3,'王五','123456'),(4,'添加第二个','123456'),(8,'test','123456'),(9,'test','123456'),(10,'test','123456'),(11,'test','123456'),(12,'test','123456'),(13,'test','123456'),(14,'test','123456'),(15,'test','123456'),(16,'test','123456'),(17,'test','123456'),(18,'test','123456'),(19,'test','123456'),(20,'test','123456'),(21,'test','123456'),(23,'test','123456'),(24,'test','123456'),(25,'test','123456'),(26,'test','123456'),(27,'test','123456'),(28,'test','123456');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.5.29 : Database - java_image_server
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`java_image_server` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `java_image_server`;

/*Table structure for table `image_table` */

DROP TABLE IF EXISTS `image_table`;

CREATE TABLE `image_table` (
  `imageId` int(11) NOT NULL AUTO_INCREMENT,
  `imageName` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `size` int(11) DEFAULT NULL,
  `uploadTime` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `contentType` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `path` varchar(1024) CHARACTER SET latin1 DEFAULT NULL,
  `md5` varchar(1024) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`imageId`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Data for the table `image_table` */

insert  into `image_table`(`imageId`,`imageName`,`size`,`uploadTime`,`contentType`,`path`,`md5`) values (2,'7.jpg',380239,'20200518','image/jpeg','./image/2efe8e0e2ea944f1a7f950990aaf44e3','2efe8e0e2ea944f1a7f950990aaf44e3'),(3,'120.png',9789,'20200518','image/png','./image/cb839c822d1d8a0f1aba873452398426','cb839c822d1d8a0f1aba873452398426'),(4,'55.jpg',24348,'20200518','image/jpeg','./image/484a8e17fa51cb20bb702a8abe4964e9','484a8e17fa51cb20bb702a8abe4964e9'),(5,'99.png',55747,'20200518','image/png','./image/f94ce79f67c69e5f97730fa6d98af3f2','f94ce79f67c69e5f97730fa6d98af3f2'),(6,'66.jpg',58891,'20200518','image/jpeg','./image/d0d9c183d822a04754d187b862bc75a7','d0d9c183d822a04754d187b862bc75a7'),(7,'124.png',472107,'20200518','image/png','./image/6d5e484d600d154181b73aaadaedbc70','6d5e484d600d154181b73aaadaedbc70'),(8,'11.jpg',502343,'20200518','image/jpeg','./image/66f8d92173849be5baf0e4ec34dd4fd7','66f8d92173849be5baf0e4ec34dd4fd7'),(11,'33.png',296056,'20200518','image/png','./image/b71a2738bc4065a52c426f8e09816b7b','b71a2738bc4065a52c426f8e09816b7b'),(13,'127.png',273093,'20200518','image/png','./image/6c19bd020adfa6ec531869aff332f12c','6c19bd020adfa6ec531869aff332f12c'),(19,'44.png',43097,'20200518','image/png','./image/c7d3c5389c35f9e5b4ad783fdc92ae90','c7d3c5389c35f9e5b4ad783fdc92ae90'),(22,'D:\\Scripts\\ImageTest\\44.png',43097,'20200518','image/png','./image/c7d3c5389c35f9e5b4ad783fdc92ae90','c7d3c5389c35f9e5b4ad783fdc92ae90'),(23,'D:\\Scripts\\ImageTest\\44.png',43097,'20200518','image/png','./image/c7d3c5389c35f9e5b4ad783fdc92ae90','c7d3c5389c35f9e5b4ad783fdc92ae90'),(24,'D:\\Scripts\\ImageTest\\44.png',43097,'20200518','image/png','./image/c7d3c5389c35f9e5b4ad783fdc92ae90','c7d3c5389c35f9e5b4ad783fdc92ae90'),(25,'D:\\Scripts\\ImageTest\\44.png',43097,'20200518','image/png','./image/c7d3c5389c35f9e5b4ad783fdc92ae90','c7d3c5389c35f9e5b4ad783fdc92ae90'),(26,'D:\\Scripts\\ImageTest\\44.png',43097,'20200518','image/png','./image/c7d3c5389c35f9e5b4ad783fdc92ae90','c7d3c5389c35f9e5b4ad783fdc92ae90'),(27,'D:\\Scripts\\ImageTest\\44.png',43097,'20200518','image/png','./image/c7d3c5389c35f9e5b4ad783fdc92ae90','c7d3c5389c35f9e5b4ad783fdc92ae90'),(28,'D:\\Scripts\\ImageTest\\44.png',43097,'20200518','image/png','./image/c7d3c5389c35f9e5b4ad783fdc92ae90','c7d3c5389c35f9e5b4ad783fdc92ae90'),(29,'D:\\Scripts\\ImageTest\\44.png',43097,'20200518','image/png','./image/c7d3c5389c35f9e5b4ad783fdc92ae90','c7d3c5389c35f9e5b4ad783fdc92ae90'),(30,'D:\\Scripts\\ImageTest\\44.png',43097,'20200518','image/png','./image/c7d3c5389c35f9e5b4ad783fdc92ae90','c7d3c5389c35f9e5b4ad783fdc92ae90'),(31,'D:\\Scripts\\ImageTest\\44.png',43097,'20200518','image/png','./image/c7d3c5389c35f9e5b4ad783fdc92ae90','c7d3c5389c35f9e5b4ad783fdc92ae90'),(32,'D:\\Scripts\\ImageTest\\44.png',43097,'20200518','image/png','./image/c7d3c5389c35f9e5b4ad783fdc92ae90','c7d3c5389c35f9e5b4ad783fdc92ae90'),(33,'D:\\Scripts\\ImageTest\\44.png',43097,'20200518','image/png','./image/c7d3c5389c35f9e5b4ad783fdc92ae90','c7d3c5389c35f9e5b4ad783fdc92ae90');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.39 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for medipluse
CREATE DATABASE IF NOT EXISTS `medipluse` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `medipluse`;

-- Dumping structure for table medipluse.brand
CREATE TABLE IF NOT EXISTS `brand` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table medipluse.brand: ~13 rows (approximately)
INSERT INTO `brand` (`id`, `name`) VALUES
	(1, 'Pfizer'),
	(2, 'Johnson & Johnson'),
	(3, 'Pfizer'),
	(4, 'Johnson & Johnson'),
	(5, 'Novartis'),
	(6, 'GlaxoSmithKline'),
	(7, 'AstraZeneca'),
	(8, 'Bayer'),
	(9, 'Sanofi'),
	(10, 'Roche'),
	(11, 'Merck & Co.'),
	(12, 'AbbVie'),
	(13, '000000'),
	(14, 'hfdhf'),
	(15, 'new'),
	(16, 'hetjhtehejt'),
	(17, 'khbjhhj');

-- Dumping structure for table medipluse.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table medipluse.category: ~8 rows (approximately)
INSERT INTO `category` (`id`, `name`) VALUES
	(1, 'Medicine'),
	(2, 'edical Equipment'),
	(3, 'Analgesics'),
	(4, 'Antibiotics'),
	(5, 'Antipyretics'),
	(6, 'Antiseptics'),
	(7, 'Vitamins & Supplements'),
	(10, 'ooooo'),
	(11, 'hgchtch');

-- Dumping structure for table medipluse.company
CREATE TABLE IF NOT EXISTS `company` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `hotline` varchar(45) NOT NULL,
  `email` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table medipluse.company: ~9 rows (approximately)
INSERT INTO `company` (`id`, `name`, `hotline`, `email`) VALUES
	(1, 'Cipla Pharmaceuticals', '0112345678', 'info@cipla.com'),
	(2, 'Sun Pharma', '0118765432', 'imfp@sunpharma.com'),
	(3, 'Hemas Pharmaceuticals', '0113456789', 'info@hemas.com'),
	(4, 'Tokyo Cement Company', '0119876543', 'info@tokyo.com '),
	(5, 'Swiss Biogenics', '0114567890', 'info@swiss.com '),
	(8, 'vsdfgvggdfgdy', '0552055277', 'avc@gmail.com'),
	(10, 'sgrg', '0712654117', 'wryrwy@gmail.com'),
	(11, 'newwrey4ry', '0712675663', 'kjbgfefj@gmail.com'),
	(12, 'jbrhihgrehgfdhgfd', '0117734157', 'jgbrfjkgb@gmail.com');

-- Dumping structure for table medipluse.employee
CREATE TABLE IF NOT EXISTS `employee` (
  `email` varchar(250) NOT NULL,
  `password` varchar(45) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `nic` varchar(45) NOT NULL,
  `mobile` varchar(10) NOT NULL,
  `date_registered` date NOT NULL,
  `gender_id` int NOT NULL,
  `employee_type_id` int NOT NULL,
  PRIMARY KEY (`email`),
  KEY `fk_employee_gender1_idx` (`gender_id`),
  KEY `fk_employee_employee_type1_idx` (`employee_type_id`),
  CONSTRAINT `fk_employee_employee_type1` FOREIGN KEY (`employee_type_id`) REFERENCES `employee_type` (`id`),
  CONSTRAINT `fk_employee_gender1` FOREIGN KEY (`gender_id`) REFERENCES `gender` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table medipluse.employee: ~2 rows (approximately)
INSERT INTO `employee` (`email`, `password`, `first_name`, `last_name`, `nic`, `mobile`, `date_registered`, `gender_id`, `employee_type_id`) VALUES
	('cashier@gmail.com', '456@Medipluse', 'Pramod', 'Nawarathna', '200235188909', '0779214118', '2025-01-24', 1, 2),
	('manager@gamil.com', '123@Mediplues', 'Lakshitha', 'Madumal', '200330800692', '0712654117', '2025-01-24', 1, 1);

-- Dumping structure for table medipluse.employee_type
CREATE TABLE IF NOT EXISTS `employee_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table medipluse.employee_type: ~2 rows (approximately)
INSERT INTO `employee_type` (`id`, `name`) VALUES
	(1, 'Pharmacist'),
	(2, 'Cashier');

-- Dumping structure for table medipluse.gender
CREATE TABLE IF NOT EXISTS `gender` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table medipluse.gender: ~2 rows (approximately)
INSERT INTO `gender` (`id`, `name`) VALUES
	(1, 'Male'),
	(2, 'Female');

-- Dumping structure for table medipluse.grn
CREATE TABLE IF NOT EXISTS `grn` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `suppler_mobile` varchar(10) NOT NULL,
  `added_date` datetime NOT NULL,
  `paid_amount` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_grn_suppler1_idx` (`suppler_mobile`),
  CONSTRAINT `fk_grn_suppler1` FOREIGN KEY (`suppler_mobile`) REFERENCES `suppler` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=1737469875402 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table medipluse.grn: ~55 rows (approximately)
INSERT INTO `grn` (`id`, `suppler_mobile`, `added_date`, `paid_amount`) VALUES
	(1, '0765425119', '2024-09-15 08:47:59', 20000),
	(2, '0705682449', '2024-09-15 08:48:24', 58000),
	(3, '0705682449', '2024-09-17 15:50:25', 59000),
	(1726761887594, '0763987330', '2024-09-19 00:00:00', 2000),
	(1726761985476, '0765425119', '2024-09-19 00:00:00', 2000),
	(1726764565163, '0765425119', '2024-09-19 00:00:00', 5000),
	(1726764644557, '0765425119', '2024-09-19 00:00:00', 9500),
	(1726765017029, '0765425119', '2024-09-19 00:00:00', 5000),
	(1726765145114, '0765425119', '2024-09-19 00:00:00', 5000),
	(1726765252123, '0765425119', '2024-09-19 00:00:00', 3781),
	(1726766456014, '0765425119', '2024-09-19 00:00:00', 60000),
	(1726848481255, '0712651116', '2024-09-20 00:00:00', 4000),
	(1726848757425, '0712651116', '2024-09-20 00:00:00', 742644),
	(1726848886690, '0712651116', '2024-09-20 00:00:00', 500),
	(1726849042604, '0763987330', '2024-09-20 00:00:00', 3600),
	(1726849429985, '0779275592', '2024-09-20 00:00:00', 5400),
	(1726849815024, '0705682449', '2024-09-20 00:00:00', 3500),
	(1726849948531, '0779275592', '2024-09-20 00:00:00', 1080),
	(1726851250564, '0765425119', '2024-09-20 00:00:00', 0),
	(1736540675468, '0765425119', '2025-01-11 00:00:00', 1000),
	(1736541003902, '0779275592', '2025-01-11 00:00:00', 15200),
	(1736541130506, '0705682449', '2025-01-11 00:00:00', 172000),
	(1736569237752, '0763987330', '2025-01-11 00:00:00', 1500),
	(1736591320412, '0705682449', '2025-01-11 00:00:00', 1000),
	(1736591430201, '0763987330', '2025-01-11 00:00:00', 1000),
	(1736750765571, '0712651116', '2025-01-13 00:00:00', 1000),
	(1736750906168, '0705682449', '2025-01-13 00:00:00', 0),
	(1736751353907, '0789098776', '2025-01-13 00:00:00', 100),
	(1736751390737, '0718900990', '2025-01-13 00:00:00', 120),
	(1736754121203, '0718900990', '2025-01-13 00:00:00', 2200),
	(1736762975024, '0763987330', '2025-01-13 00:00:00', 100),
	(1736763002592, '0705682449', '2025-01-13 00:00:00', 10100),
	(1736764389134, '0787890990', '2025-01-13 00:00:00', 10000),
	(1736764548156, '0705682449', '2025-01-13 00:00:00', 0),
	(1736764584582, '0763987330', '2025-01-13 00:00:00', 0),
	(1736765049368, '0765425119', '2025-01-13 00:00:00', 100),
	(1736765105373, '0705682449', '2025-01-13 00:00:00', 500),
	(1736773056270, '0712651116', '2025-01-13 00:00:00', 100),
	(1736773825253, '0789098776', '2025-01-13 00:00:00', 100),
	(1736830769495, '0787890990', '2025-01-14 00:00:00', 100),
	(1736831119038, '0787890990', '2025-01-14 00:00:00', 15000),
	(1736831632068, '0763987330', '2025-01-14 00:00:00', 100),
	(1736831648093, '0712657890', '2025-01-14 00:00:00', 100),
	(1736832901728, '0789098776', '2025-01-14 00:00:00', 900),
	(1736832946579, '0787890990', '2025-01-14 00:00:00', 100),
	(1736833243891, '0789098776', '2025-01-14 00:00:00', 750),
	(1736834070828, '0789098776', '2025-01-14 00:00:00', 3100),
	(1736834167336, '0705682449', '2025-01-14 00:00:00', 100),
	(1736834184458, '0712651116', '2025-01-14 00:00:00', 1100),
	(1736834208466, '0787890990', '2025-01-14 00:00:00', 160000),
	(1736834809280, '0787890990', '2025-01-14 00:00:00', 18500),
	(1736835033716, '0789098776', '2025-01-14 00:00:00', 5500),
	(1736835378537, '0787890990', '2025-01-14 00:00:00', 20900),
	(1736947167968, '0787890990', '2025-01-15 00:00:00', 2000),
	(1737469875401, '0765425119', '2025-01-21 00:00:00', 100);

-- Dumping structure for table medipluse.grn_item
CREATE TABLE IF NOT EXISTS `grn_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `qty` int NOT NULL,
  `buying_price` double NOT NULL,
  `grn_id` bigint NOT NULL,
  `stock_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_grn_item_grn1_idx` (`grn_id`),
  KEY `fk_grn_item_stock1_idx` (`stock_id`),
  CONSTRAINT `fk_grn_item_grn1` FOREIGN KEY (`grn_id`) REFERENCES `grn` (`id`),
  CONSTRAINT `fk_grn_item_stock1` FOREIGN KEY (`stock_id`) REFERENCES `stock` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table medipluse.grn_item: ~55 rows (approximately)
INSERT INTO `grn_item` (`id`, `qty`, `buying_price`, `grn_id`, `stock_id`) VALUES
	(1, 100, 40, 1, 1),
	(2, 60, 1000, 1726766456014, 13),
	(3, 8, 500, 1726848481255, 14),
	(4, 98, 7578, 1726848757425, 15),
	(5, 5, 100, 1726848886690, 16),
	(6, 9, 400, 1726849042604, 17),
	(7, 9, 600, 1726849429985, 18),
	(8, 5, 700, 1726849815024, 19),
	(9, 9, 120, 1726849948531, 20),
	(10, 9, 100, 1726851250564, 21),
	(11, 10, 100, 1736540675468, 22),
	(12, 100, 152, 1736541003902, 23),
	(13, 100, 152, 1736541130506, 23),
	(14, 10, 200, 1736541130506, 24),
	(15, 5, 200, 1736569237752, 25),
	(16, 10, 100, 1736591320412, 29),
	(17, 10, 100, 1736591430201, 30),
	(18, 10, 100, 1736750765571, 31),
	(19, 10, 100, 1736750906168, 31),
	(20, 54, 100, 1736750906168, 32),
	(21, 10, 100, 1736750906168, 33),
	(22, 10, 10, 1736750906168, 34),
	(23, 10, 10, 1736751353907, 35),
	(24, 10, 10, 1736751390737, 36),
	(25, 10, 10, 1736754121203, 37),
	(26, 110, 10, 1736754121203, 38),
	(27, 100, 10, 1736754121203, 39),
	(28, 10, 10, 1736762975024, 40),
	(29, 100, 100, 1736763002592, 41),
	(30, 10, 10, 1736763002592, 40),
	(31, 10, 1000, 1736764389134, 42),
	(32, 10, 10, 1736765049368, 43),
	(33, 10, 50, 1736765105373, 44),
	(34, 10, 10, 1736773056270, 45),
	(35, 10, 10, 1736773825253, 46),
	(36, 10, 10, 1736830769495, 47),
	(37, 100, 150, 1736831119038, 48),
	(38, 10, 10, 1736831632068, 49),
	(39, 10, 10, 1736831648093, 50),
	(40, 90, 10, 1736832901728, 51),
	(41, 10, 10, 1736832946579, 52),
	(42, 20, 15, 1736833243891, 53),
	(43, 210, 10, 1736834070828, 54),
	(44, 10, 10, 1736834167336, 51),
	(45, 110, 10, 1736834184458, 55),
	(46, 1100, 100, 1736834208466, 56),
	(47, 110, 150, 1736834809280, 57),
	(48, 10, 200, 1736834809280, 58),
	(49, 40, 100, 1736835033716, 55),
	(50, 10, 150, 1736835033716, 59),
	(51, 205, 100, 1736835378537, 60),
	(52, 40, 10, 1736835378537, 61),
	(53, 10, 100, 1736947167968, 62),
	(54, 10, 100, 1736947167968, 63),
	(55, 10, 10, 1737469875401, 64);

-- Dumping structure for table medipluse.invoice
CREATE TABLE IF NOT EXISTS `invoice` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `employee_email` varchar(250) NOT NULL,
  `payment_method_id` int NOT NULL,
  `total_amount` double NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_invoice_employee1_idx` (`employee_email`),
  KEY `fk_invoice_payment_method1_idx` (`payment_method_id`),
  CONSTRAINT `fk_invoice_employee1` FOREIGN KEY (`employee_email`) REFERENCES `employee` (`email`),
  CONSTRAINT `fk_invoice_payment_method1` FOREIGN KEY (`payment_method_id`) REFERENCES `payment_method` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1737469953677 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table medipluse.invoice: ~13 rows (approximately)
INSERT INTO `invoice` (`id`, `employee_email`, `payment_method_id`, `total_amount`, `date`) VALUES
	(1, 'cashier@gmail.com', 1, 1000, '2025-01-24 21:07:40');

-- Dumping structure for table medipluse.invoice_item
CREATE TABLE IF NOT EXISTS `invoice_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `invoice_id` bigint NOT NULL,
  `stock_id` int NOT NULL,
  `qty` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_invoice_item_invoice1_idx` (`invoice_id`),
  KEY `fk_invoice_item_stock1_idx` (`stock_id`),
  CONSTRAINT `fk_invoice_item_invoice1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`),
  CONSTRAINT `fk_invoice_item_stock1` FOREIGN KEY (`stock_id`) REFERENCES `stock` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table medipluse.invoice_item: ~12 rows (approximately)

-- Dumping structure for table medipluse.payment_method
CREATE TABLE IF NOT EXISTS `payment_method` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table medipluse.payment_method: ~2 rows (approximately)
INSERT INTO `payment_method` (`id`, `name`) VALUES
	(1, 'Cash'),
	(2, 'Card');

-- Dumping structure for table medipluse.product
CREATE TABLE IF NOT EXISTS `product` (
  `id` varchar(150) NOT NULL,
  `name` varchar(45) NOT NULL,
  `brand_id` int NOT NULL,
  `category_id` int NOT NULL,
  `datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_brand1_idx` (`brand_id`),
  KEY `fk_product_category1_idx` (`category_id`),
  CONSTRAINT `fk_product_brand1` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`),
  CONSTRAINT `fk_product_category1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table medipluse.product: ~22 rows (approximately)
INSERT INTO `product` (`id`, `name`, `brand_id`, `category_id`, `datetime`) VALUES
	('1', 'Panadol', 3, 3, '2025-01-11 11:02:17'),
	('10', 'Zinc Tablets', 10, 5, '2025-01-11 11:02:20'),
	('11', 'new', 4, 2, '2025-01-11 11:02:21'),
	('12', 'edfgedg', 4, 4, NULL),
	('13', 'News', 14, 10, NULL),
	('2', 'Amoxicillin', 2, 2, NULL),
	('3', 'Brufen', 3, 1, NULL),
	('4', 'Paracetamol', 4, 3, NULL),
	('43-HJ', 'efgde', 4, 5, NULL),
	('4543', 'plooooo', 3, 3, NULL),
	('45635', 'mmmmmm', 3, 3, '2025-01-11 11:50:16'),
	('5', 'Dettol', 5, 4, NULL),
	('6', 'Aspirin', 6, 1, NULL),
	('65467', 'plooooo', 5, 5, NULL),
	('7', 'C Vitamin Tablets', 7, 5, NULL),
	('8', 'Risperidone', 8, 2, NULL),
	('9', 'Ibuprofen', 9, 1, NULL),
	('MEDI-1736577068433', 'jhtrgfjrt', 4, 3, '2025-01-11 12:01:48'),
	('MEDI-1736577137597', 'mmmmmmmmmmmmmm', 4, 6, '2025-01-11 12:02:26'),
	('MEDI-1736577204479', 'ooooo', 4, 3, '2025-01-11 12:03:35'),
	('MEDI-1736577297045', 'qqqqqqq', 4, 5, '2025-01-11 12:05:12'),
	('MEDI-1736577519566', 'jnfgjfg', 4, 3, '2025-01-11 12:08:47'),
	('MEDI-1736591583930', 'fgjfgjfgj', 3, 5, '2025-01-11 16:06:12'),
	('MEDI-1736743030500', 'newnew', 4, 6, '2025-01-13 10:08:03');

-- Dumping structure for table medipluse.stock
CREATE TABLE IF NOT EXISTS `stock` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` varchar(150) NOT NULL,
  `date_added` date NOT NULL,
  `selling_price` double NOT NULL,
  `qty` double NOT NULL,
  `mfd` date NOT NULL,
  `exp` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_stock_product1_idx` (`product_id`),
  CONSTRAINT `fk_stock_product1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb3;

-- Dumping data for table medipluse.stock: ~57 rows (approximately)
INSERT INTO `stock` (`id`, `product_id`, `date_added`, `selling_price`, `qty`, `mfd`, `exp`) VALUES
	(1, '1', '2024-01-01', 50, 0, '2023-12-01', '2025-01-01'),
	(2, '2', '2024-01-02', 120, 200, '2023-11-15', '2025-06-01'),
	(3, '3', '2024-01-03', 70, 150, '2023-12-10', '2025-03-01'),
	(4, '4', '2024-01-04', 40, 250, '2023-12-05', '2024-12-01'),
	(5, '5', '2024-01-05', 150, 50, '2023-11-01', '2025-09-01'),
	(6, '6', '2024-01-06', 35, 300, '2023-10-15', '2025-01-01'),
	(7, '7', '2024-01-07', 200, 100, '2023-11-20', '2025-07-01'),
	(8, '9', '2024-01-09', 90, 120, '2023-11-05', '2025-05-01'),
	(9, '8', '2024-01-08', 250, 80, '2023-10-30', '2025-08-01'),
	(10, '10', '2024-01-10', 180, 0, '2023-12-01', '2025-06-01'),
	(11, '1', '2024-09-19', 60, 20, '2024-09-01', '2025-09-19'),
	(12, '2', '2024-09-19', 588, 19, '2024-09-01', '2024-09-30'),
	(13, '9', '2024-09-19', 1500, 60, '2024-09-01', '2024-09-30'),
	(14, '1', '2024-09-20', 600, 8, '2024-09-01', '2024-09-30'),
	(15, '10', '2024-09-20', 97798, 88, '2024-09-02', '2024-09-29'),
	(16, '1', '2024-09-20', 200, 5, '2024-09-01', '2024-09-30'),
	(17, '11', '2024-09-20', 500, 9, '2024-09-01', '2024-09-23'),
	(18, '12', '2024-09-20', 800, 9, '2024-09-01', '2024-09-30'),
	(19, '3', '2024-09-20', 780, 5, '2024-09-01', '2024-09-30'),
	(20, '8', '2024-09-20', 140, 9, '2024-09-01', '2024-09-30'),
	(21, '13', '2024-09-20', 120, 9, '2024-09-01', '2024-09-30'),
	(22, '10', '2025-01-11', 120, 10, '2025-01-04', '2025-01-21'),
	(23, '12', '2025-01-11', 160, 200, '2025-01-04', '2025-01-21'),
	(24, '3', '2025-01-11', 500, 10, '2025-01-04', '2025-01-17'),
	(25, '9', '2025-01-11', 500, 5, '2025-01-04', '2025-01-28'),
	(26, '4543', '2025-01-11', 66, 7766, '2025-01-11', '2025-01-11'),
	(27, '7', '2025-01-11', 66, 565, '2025-01-11', '2025-01-11'),
	(28, '4', '2025-01-11', 1000, 1000, '2025-01-11', '2025-01-11'),
	(29, '11', '2025-01-11', 120, 10, '2025-01-04', '2025-01-30'),
	(30, '11', '2025-01-11', 150, 10, '2025-01-02', '2025-01-23'),
	(31, '1', '2025-01-13', 1000, 0, '2025-01-01', '2025-01-31'),
	(32, '13', '2025-01-13', 1000, 54, '2025-01-11', '2025-01-30'),
	(33, '43-HJ', '2025-01-13', 1000, 10, '2025-01-04', '2025-01-28'),
	(34, '10', '2025-01-13', 100, 10, '2025-01-01', '2025-07-22'),
	(35, 'MEDI-1736577204479', '2025-01-13', 100, 10, '2025-01-04', '2026-09-18'),
	(36, 'MEDI-1736577297045', '2025-01-13', 100, 10, '2025-01-04', '2027-01-22'),
	(37, '12', '2025-01-13', 100, 10, '2025-01-04', '2026-01-31'),
	(38, '43-HJ', '2025-01-13', 100, 110, '2025-01-04', '2026-01-31'),
	(39, '6', '2025-01-13', 100, 100, '2025-01-04', '2026-01-31'),
	(40, 'MEDI-1736577137597', '2025-01-13', 100, 20, '2025-01-04', '2025-01-29'),
	(41, '11', '2025-01-13', 1000, 100, '2025-01-04', '2025-01-31'),
	(42, '4543', '2025-01-13', 100000, 10, '2025-01-04', '2025-01-29'),
	(43, '1', '2025-01-13', 100, 10, '2025-01-11', '2025-01-30'),
	(44, '11', '2025-01-13', 100, 10, '2025-01-04', '2025-01-31'),
	(45, '13', '2025-01-13', 150, 10, '2025-01-04', '2025-01-31'),
	(46, '43-HJ', '2025-01-13', 150, 10, '2025-01-04', '2025-01-27'),
	(47, '13', '2025-01-14', 15, 10, '2025-01-04', '2025-01-31'),
	(48, '13', '2025-01-14', 200, 100, '2024-01-06', '2026-01-24'),
	(49, '1', '2025-01-14', 15, 10, '2025-01-01', '2025-01-31'),
	(50, '9', '2025-01-14', 15, 10, '2025-01-01', '2025-01-31'),
	(51, '2', '2025-01-14', 15, 100, '2025-01-04', '2025-01-31'),
	(52, '11', '2025-01-14', 15, 10, '2025-01-11', '2025-01-29'),
	(53, '12', '2025-01-14', 50, 20, '2025-01-04', '2025-01-30'),
	(54, '1', '2025-01-14', 15, 180, '2025-01-04', '2025-01-31'),
	(55, '1', '2025-01-14', 150, 140, '2025-01-04', '2025-01-31'),
	(56, '1', '2025-01-14', 1000, 1100, '2025-01-04', '2025-01-23'),
	(57, '1', '2025-01-14', 180, 110, '2025-01-01', '2025-01-31'),
	(58, '10', '2025-01-14', 520, 10, '2025-01-04', '2025-01-30'),
	(59, '13', '2025-01-14', 250, 10, '2025-01-04', '2025-01-31'),
	(60, '1', '2025-01-14', 1000, 205, '2025-01-04', '2025-01-31'),
	(61, '10', '2025-01-14', 15, 40, '2025-01-04', '2025-01-30'),
	(62, '8', '2025-01-15', 150, 10, '2025-01-01', '2025-01-31'),
	(63, '9', '2025-01-15', 150, 10, '2025-01-01', '2025-01-31'),
	(64, '12', '2025-01-21', 15, 10, '2025-01-04', '2025-01-31');

-- Dumping structure for table medipluse.suppler
CREATE TABLE IF NOT EXISTS `suppler` (
  `mobile` varchar(10) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `email` varchar(250) NOT NULL,
  `company_id` int NOT NULL,
  PRIMARY KEY (`mobile`),
  KEY `fk_suppler_company_idx` (`company_id`),
  CONSTRAINT `fk_suppler_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Dumping data for table medipluse.suppler: ~9 rows (approximately)
INSERT INTO `suppler` (`mobile`, `first_name`, `last_name`, `email`, `company_id`) VALUES
	('0705682449', 'Madura', 'Prasad', 'madura@gmail.com', 5),
	('0712651116', 'kasun', 'perera', 'kasun@gmail.com', 1),
	('0712657890', 'pakoo', 'fdhdfh', 'gdfhdf@gmail.com', 10),
	('0718900990', 'maduuu', 'yaasf', 'fete@gmail.com', 1),
	('0763987330', 'Rahana', 'Perera', 'rohana@gmail.com', 4),
	('0765425119', 'Laky', 'jaya', 'lakyjaya@gmail.com', 2),
	('0779275592', 'Nuwan', 'Gamage', 'nuesn@gmail.com', 3),
	('0787890990', '27242regvr456u56u', 'g34h34yh34g34wrtytr', 'g34h34hierohpgvgre@gmail.com', 11),
	('0789098776', 'fgbkdghkreghdf', 'erghrehgerh', 'iggreukgbrk@gmail.com', 3);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

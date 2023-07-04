SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

DROP TABLE IF EXISTS `jobworkmode`;

CREATE TABLE `jobworkmode` (
  `id` bigint(11) NOT NULL,
  `title` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `jobworkmode` (`id`,`title`) VALUES
(1,"Work From Office"),
(2,"Work From Home"),
(3,"Hybrid Work"),
(4,"Partime Work");

ALTER TABLE `jobworkmode`
  ADD PRIMARY KEY (`id`);
  
ALTER TABLE `jobworkmode`
  MODIFY `id` bigint(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6600;
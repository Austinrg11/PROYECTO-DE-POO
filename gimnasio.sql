-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 05-02-2026 a las 18:48:28
-- Versión del servidor: 9.1.0
-- Versión de PHP: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `gimnasio`
--
CREATE DATABASE IF NOT EXISTS `gimnasio` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `gimnasio`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asistencia`
--

DROP TABLE IF EXISTS `asistencia`;
CREATE TABLE IF NOT EXISTS `asistencia` (
  `IDASISTENCIA` int NOT NULL,
  `IDCLIENTE` int DEFAULT NULL,
  `FECHA` date NOT NULL,
  `HORAENTRADA` time NOT NULL,
  `HORASALIDA` time DEFAULT NULL,
  PRIMARY KEY (`IDASISTENCIA`),
  KEY `fk_asistencia_cliente` (`IDCLIENTE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `asistencia`
--

INSERT INTO `asistencia` (`IDASISTENCIA`, `IDCLIENTE`, `FECHA`, `HORAENTRADA`, `HORASALIDA`) VALUES
(1, 2, '2025-11-21', '10:00:00', '14:00:00'),
(2, 3, '2026-01-16', '08:00:00', '22:00:00'),
(3, 1, '2025-10-11', '13:00:00', '18:00:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

DROP TABLE IF EXISTS `cliente`;
CREATE TABLE IF NOT EXISTS `cliente` (
  `IDCLIENTE` int NOT NULL,
  `NOMBRES` varchar(50) NOT NULL,
  `APELLIDOS` varchar(50) NOT NULL,
  `CELULAR` varchar(15) DEFAULT NULL,
  `DIRECCION` varchar(20) DEFAULT NULL,
  `CORREO` varchar(25) DEFAULT NULL,
  `FECHAREGISTRO` date NOT NULL,
  PRIMARY KEY (`IDCLIENTE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`IDCLIENTE`, `NOMBRES`, `APELLIDOS`, `CELULAR`, `DIRECCION`, `CORREO`, `FECHAREGISTRO`) VALUES
(1, 'JUAN', 'LOPEZ TORRES', '161456', 'PORTO', 'SFD@FDSFDS', '2025-10-11'),
(2, 'carlos', 'perez', '111111', 'PORTO', 'SFDSDFSD@FDSFDS', '2025-07-12'),
(3, 'alberto', 'torres', '0134646', 'orquideaz', 'fsdfsd@gmail.com', '2026-01-16');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente_plan`
--

DROP TABLE IF EXISTS `cliente_plan`;
CREATE TABLE IF NOT EXISTS `cliente_plan` (
  `IDCLIENTEPLAN` int NOT NULL,
  `IDCLIENTE` int NOT NULL,
  `IDPLAN` int NOT NULL,
  `FECHAINICIO` date NOT NULL,
  `FECHAFIN` date NOT NULL,
  `ESTADO` varchar(20) NOT NULL,
  PRIMARY KEY (`IDCLIENTEPLAN`),
  KEY `fk_cliente_plan_plan` (`IDPLAN`),
  KEY `fk_cliente_plan_cliente` (`IDCLIENTE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `cliente_plan`
--

INSERT INTO `cliente_plan` (`IDCLIENTEPLAN`, `IDCLIENTE`, `IDPLAN`, `FECHAINICIO`, `FECHAFIN`, `ESTADO`) VALUES
(1, 2, 2, '2025-11-06', '2025-11-22', 'INACTIVO'),
(2, 1, 1, '2025-05-05', '2025-11-29', 'ACTIVO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `entrenador`
--

DROP TABLE IF EXISTS `entrenador`;
CREATE TABLE IF NOT EXISTS `entrenador` (
  `IDENTRENADOR` int NOT NULL,
  `NOMBRES` varchar(50) NOT NULL,
  `ESPECIALIDAD` varchar(50) NOT NULL,
  `CELULAR` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`IDENTRENADOR`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `entrenador`
--

INSERT INTO `entrenador` (`IDENTRENADOR`, `NOMBRES`, `ESPECIALIDAD`, `CELULAR`) VALUES
(1, 'juan', 'pesas', '15665'),
(2, 'roberto', 'cardio', '445253'),
(3, 'andres', 'trainer', '0661031');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `equipo`
--

DROP TABLE IF EXISTS `equipo`;
CREATE TABLE IF NOT EXISTS `equipo` (
  `IDEQUIPO` int NOT NULL,
  `IDENTRENADOR` int DEFAULT NULL,
  `NOMBREEQUIPO` varchar(50) NOT NULL,
  `TIPO` varchar(50) DEFAULT NULL,
  `ESTADO` varchar(50) DEFAULT NULL,
  `DESCRIPCION` text,
  PRIMARY KEY (`IDEQUIPO`),
  KEY `fk_equipo_entrenador` (`IDENTRENADOR`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `equipo`
--

INSERT INTO `equipo` (`IDEQUIPO`, `IDENTRENADOR`, `NOMBREEQUIPO`, `TIPO`, `ESTADO`, `DESCRIPCION`) VALUES
(1, 1, 'tigres', 'pesas', 'ACTIVO', 'solo hombres'),
(2, 2, 'cardio', 'fuerza', 'ACTIVO', 'mejorar fisico'),
(3, 3, 'LEONES', 'pesas', 'INACTIVO', 'solo mayores de 60');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `plan`
--

DROP TABLE IF EXISTS `plan`;
CREATE TABLE IF NOT EXISTS `plan` (
  `IDPLAN` int NOT NULL,
  `NOMBREPLAN` varchar(50) NOT NULL,
  `COSTO` decimal(5,2) NOT NULL,
  `DURACION` int NOT NULL,
  `DESCRIPCION` text,
  PRIMARY KEY (`IDPLAN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `plan`
--

INSERT INTO `plan` (`IDPLAN`, `NOMBREPLAN`, `COSTO`, `DURACION`, `DESCRIPCION`) VALUES
(1, 'gold', 50.00, 365, 'pago efectivo'),
(2, 'bronce', 20.00, 30, 'descuento 10%');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `uso_equipo`
--

DROP TABLE IF EXISTS `uso_equipo`;
CREATE TABLE IF NOT EXISTS `uso_equipo` (
  `IDUSO` int NOT NULL,
  `IDCLIENTE` int NOT NULL,
  `IDEQUIPO` int NOT NULL,
  `FECHA` date NOT NULL,
  `TIEMPO` int NOT NULL,
  PRIMARY KEY (`IDUSO`),
  KEY `fk_uso_equipo` (`IDEQUIPO`),
  KEY `fk_uso_cliente` (`IDCLIENTE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `uso_equipo`
--

INSERT INTO `uso_equipo` (`IDUSO`, `IDCLIENTE`, `IDEQUIPO`, `FECHA`, `TIEMPO`) VALUES
(1, 2, 1, '2025-11-08', 90),
(2, 1, 2, '2025-11-20', 120),
(3, 3, 3, '2023-07-08', 120);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

DROP TABLE IF EXISTS `usuario`;
CREATE TABLE IF NOT EXISTS `usuario` (
  `IDUSUARIO` int NOT NULL AUTO_INCREMENT,
  `USUARIO` varchar(50) DEFAULT NULL,
  `PASS` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`IDUSUARIO`),
  UNIQUE KEY `Usuario` (`USUARIO`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`IDUSUARIO`, `USUARIO`, `PASS`) VALUES
(1, 'admin', '1234');

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cliente_plan`
--
ALTER TABLE `cliente_plan`
  ADD CONSTRAINT `fk_cliente_plan_cliente` FOREIGN KEY (`IDCLIENTE`) REFERENCES `cliente` (`IDCLIENTE`);

--
-- Filtros para la tabla `uso_equipo`
--
ALTER TABLE `uso_equipo`
  ADD CONSTRAINT `fk_uso_cliente` FOREIGN KEY (`IDCLIENTE`) REFERENCES `cliente` (`IDCLIENTE`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

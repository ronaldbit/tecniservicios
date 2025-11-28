-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 22-10-2025 a las 04:57:58
-- Versión del servidor: 11.8.3-MariaDB-log
-- Versión de PHP: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `u266581275_tecniservicios`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `afectaciones`
--

CREATE TABLE `afectaciones` (
  `id_afectacion` bigint(20) NOT NULL,
  `codigo` varchar(2) NOT NULL,
  `descripcion` varchar(120) NOT NULL,
  `grava_igv` tinyint(1) NOT NULL,
  `es_gratuita` tinyint(1) NOT NULL DEFAULT 0,
  `es_exportacion` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `afectaciones`
--

INSERT INTO `afectaciones` (`id_afectacion`, `codigo`, `descripcion`, `grava_igv`, `es_gratuita`, `es_exportacion`) VALUES
(1, '10', 'Gravado - Operación onerosa', 1, 0, 0),
(2, '20', 'Exonerado - Operación onerosa', 0, 0, 0),
(3, '30', 'Inafecto - Operación onerosa', 0, 0, 0),
(4, '40', 'Exportación', 0, 0, 1),
(5, '31', 'Inafecto - Retiro por bonificación (Gratuita)', 0, 1, 0);v

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `almacenes`
--

CREATE TABLE `almacenes` (
  `id_almacen` bigint(20) NOT NULL,
  `id_sucursal` bigint(20) NOT NULL,
  `nombre` varchar(120) NOT NULL,
  `activo` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `almacenes`
--

INSERT INTO `almacenes` (`id_almacen`, `id_sucursal`, `nombre`, `activo`) VALUES
(1, 1, 'Almacén Principal', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cajas`
--

CREATE TABLE `cajas` (
  `id_caja` bigint(20) NOT NULL,
  `id_sucursal` bigint(20) NOT NULL,
  `codigo` varchar(30) NOT NULL,
  `id_estado_caja` bigint(20) NOT NULL,
  `saldo_inicial` decimal(14,2) DEFAULT 0.00,
  `id_abierto_por` bigint(20) DEFAULT NULL,
  `abierto_en` datetime DEFAULT NULL,
  `id_cerrado_por` bigint(20) DEFAULT NULL,
  `cerrado_en` datetime DEFAULT NULL,
  `saldo_cierre` decimal(14,2) DEFAULT NULL,
  `observacion` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `caja_movimientos`
--

CREATE TABLE `caja_movimientos` (
  `id_movimiento_caja` bigint(20) NOT NULL,
  `id_caja` bigint(20) NOT NULL,
  `fecha` datetime NOT NULL,
  `id_tipo_mov_caja` bigint(20) NOT NULL,
  `id_origen_mov_caja` bigint(20) NOT NULL,
  `referencia_id` bigint(20) DEFAULT NULL,
  `concepto` varchar(200) DEFAULT NULL,
  `monto` decimal(14,2) NOT NULL,
  `id_usuario` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `canales_pedido`
--

CREATE TABLE `canales_pedido` (
  `id_canal_pedido` bigint(20) NOT NULL,
  `codigo` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `canales_pedido`
--

INSERT INTO `canales_pedido` (`id_canal_pedido`, `codigo`) VALUES
(1, 'ONLINE');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `canales_venta`
--

CREATE TABLE `canales_venta` (
  `id_canal_venta` bigint(20) NOT NULL,
  `codigo` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `canales_venta`
--

INSERT INTO `canales_venta` (`id_canal_venta`, `codigo`) VALUES
(2, 'ONLINE'),
(1, 'POS');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias`
--

CREATE TABLE `categorias` (
  `id_categoria` bigint(20) NOT NULL,
  `nombre` varchar(120) NOT NULL,
  `activo` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `categorias`
--

INSERT INTO `categorias` (`id_categoria`, `nombre`, `activo`) VALUES
(1, 'Aceite Motor', 1),
(2, 'Filtro Aceite', 1),
(3, 'Refrigerante', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id_cliente` bigint(20) NOT NULL,
  `id_persona` bigint(20) DEFAULT NULL,
  `categoria` varchar(50) DEFAULT NULL,
  `linea_credito` decimal(12,2) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id_cliente`, `id_persona`, `categoria`, `linea_credito`, `activo`) VALUES
(1, NULL, NULL, NULL, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cobros`
--

CREATE TABLE `cobros` (
  `id_cobro` bigint(20) NOT NULL,
  `id_cuenta_cobrar` bigint(20) NOT NULL,
  `fecha` datetime NOT NULL,
  `monto` decimal(14,2) NOT NULL,
  `id_metodo_pago` bigint(20) DEFAULT NULL,
  `id_caja` bigint(20) DEFAULT NULL,
  `id_usuario` bigint(20) DEFAULT NULL,
  `referencia` varchar(120) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `compras`
--

CREATE TABLE `compras` (
  `id_compra` bigint(20) NOT NULL,
  `fecha` datetime NOT NULL,
  `id_proveedor` bigint(20) NOT NULL,
  `id_usuario` bigint(20) DEFAULT NULL,
  `id_almacen` bigint(20) NOT NULL,
  `id_estado_compra` bigint(20) NOT NULL,
  `moneda` char(3) DEFAULT 'PEN',
  `tipo_cambio` decimal(10,4) DEFAULT 1.0000,
  `subtotal` decimal(14,2) DEFAULT 0.00,
  `impuesto_monto` decimal(14,2) DEFAULT 0.00,
  `total` decimal(14,2) DEFAULT 0.00,
  `observacion` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `compra_items`
--

CREATE TABLE `compra_items` (
  `id_item_compra` bigint(20) NOT NULL,
  `id_compra` bigint(20) NOT NULL,
  `id_producto` bigint(20) NOT NULL,
  `cantidad` decimal(14,3) NOT NULL,
  `precio_unit` decimal(12,2) NOT NULL,
  `descuento` decimal(12,2) DEFAULT 0.00,
  `impuesto_pct` decimal(5,2) DEFAULT NULL,
  `impuesto_monto` decimal(12,2) DEFAULT 0.00,
  `total_linea` decimal(14,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Disparadores `compra_items`
--
DELIMITER $$
CREATE TRIGGER `trg_compra_item_ai` AFTER INSERT ON `compra_items` FOR EACH ROW BEGIN
  DECLARE v_estado VARCHAR(20);
  DECLARE v_id_alm BIGINT;

  SELECT ec.codigo, c.id_almacen
    INTO v_estado, v_id_alm
  FROM compras c
  JOIN estado_compras ec ON ec.id_estado_compra = c.id_estado_compra
  WHERE c.id_compra = NEW.id_compra;

  IF v_estado = 'RECIBIDO' THEN
    INSERT INTO inventario_movimientos
      (fecha,id_almacen,id_producto,id_tipo_mov_inventario,id_motivo_mov_inventario,cantidad,costo_unit,id_ref_mov_inventario,referencia_id)
    VALUES
      (NOW(), v_id_alm, NEW.id_producto,
       (SELECT id_tipo_mov_inventario FROM tipo_mov_inventarios WHERE codigo='IN'),
       (SELECT id_motivo_mov_inventario FROM motivo_mov_inventarios WHERE codigo='COMPRA'),
       NEW.cantidad, NEW.precio_unit,
       (SELECT id_ref_mov_inventario FROM ref_mov_inventarios WHERE codigo='COMPRA'),
       NEW.id_compra);

    INSERT INTO stock (id_almacen,id_producto,cantidad)
      VALUES (v_id_alm, NEW.id_producto, NEW.cantidad)
    ON DUPLICATE KEY UPDATE cantidad = cantidad + NEW.cantidad;
  END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `condiciones_pago`
--

CREATE TABLE `condiciones_pago` (
  `id_condicion_pago` bigint(20) NOT NULL,
  `codigo` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `condiciones_pago`
--

INSERT INTO `condiciones_pago` (`id_condicion_pago`, `codigo`) VALUES
(1, 'CONTADO'),
(2, 'CREDITO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cuentas_cobrar`
--

CREATE TABLE `cuentas_cobrar` (
  `id_cuenta_cobrar` bigint(20) NOT NULL,
  `id_venta` bigint(20) NOT NULL,
  `id_cliente` bigint(20) NOT NULL,
  `fecha_emision` date NOT NULL,
  `fecha_venc` date DEFAULT NULL,
  `monto_total` decimal(14,2) NOT NULL,
  `saldo` decimal(14,2) NOT NULL,
  `moneda` char(3) DEFAULT 'PEN',
  `id_estado_cxc` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empresas`
--

CREATE TABLE `empresas` (
  `id_empresa` bigint(20) NOT NULL,
  `ruc` varchar(20) NOT NULL,
  `razon_social` varchar(200) NOT NULL,
  `nombre_comercial` varchar(200) DEFAULT NULL,
  `direccion` varchar(300) DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `email` varchar(120) DEFAULT NULL,
  `igv_por_defecto` decimal(5,2) DEFAULT 18.00,
  `moneda_base` char(3) DEFAULT 'PEN',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `empresas`
--

INSERT INTO `empresas` (`id_empresa`, `ruc`, `razon_social`, `nombre_comercial`, `direccion`, `telefono`, `email`, `igv_por_defecto`, `moneda_base`, `created_at`, `updated_at`) VALUES
(1, '20123456789', 'Aceites Perú S.A.C.', 'Aceites Perú', NULL, NULL, NULL, 18.00, 'PEN', '2025-09-30 14:09:13', '2025-09-30 14:09:13');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estado_cajas`
--

CREATE TABLE `estado_cajas` (
  `id_estado_caja` bigint(20) NOT NULL,
  `codigo` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `estado_cajas`
--

INSERT INTO `estado_cajas` (`id_estado_caja`, `codigo`) VALUES
(1, 'ABIERTA'),
(2, 'CERRADA');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estado_compras`
--

CREATE TABLE `estado_compras` (
  `id_estado_compra` bigint(20) NOT NULL,
  `codigo` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `estado_compras`
--

INSERT INTO `estado_compras` (`id_estado_compra`, `codigo`) VALUES
(1, 'BORRADOR'),
(4, 'CANCELADO'),
(2, 'ORDENADO'),
(3, 'RECIBIDO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estado_cxc`
--

CREATE TABLE `estado_cxc` (
  `id_estado_cxc` bigint(20) NOT NULL,
  `codigo` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `estado_cxc`
--

INSERT INTO `estado_cxc` (`id_estado_cxc`, `codigo`) VALUES
(3, 'PAGADO'),
(2, 'PARCIAL'),
(1, 'PENDIENTE'),
(4, 'VENCIDO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estado_pedidos`
--

CREATE TABLE `estado_pedidos` (
  `id_estado_pedido` bigint(20) NOT NULL,
  `codigo` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `estado_pedidos`
--

INSERT INTO `estado_pedidos` (`id_estado_pedido`, `codigo`) VALUES
(6, 'CANCELADO'),
(7, 'DEVUELTO'),
(5, 'ENTREGADO'),
(4, 'ENVIADO'),
(2, 'PAGADO'),
(1, 'PENDIENTE_PAGO'),
(3, 'POR_DESPACHAR');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estado_sunat`
--

CREATE TABLE `estado_sunat` (
  `id_estado_sunat` bigint(20) NOT NULL,
  `codigo` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `estado_sunat`
--

INSERT INTO `estado_sunat` (`id_estado_sunat`, `codigo`) VALUES
(2, 'ACEPTADO'),
(6, 'BAJA_ACEPTADA'),
(5, 'BAJA_PENDIENTE'),
(7, 'BAJA_RECHAZADA'),
(3, 'OBSERVADO'),
(1, 'PENDIENTE'),
(4, 'RECHAZADO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estado_ventas`
--

CREATE TABLE `estado_ventas` (
  `id_estado_venta` bigint(20) NOT NULL,
  `codigo` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `estado_ventas`
--

INSERT INTO `estado_ventas` (`id_estado_venta`, `codigo`) VALUES
(4, 'ANULADO'),
(1, 'COTIZACION'),
(5, 'DEVUELTO'),
(3, 'EMITIDO'),
(2, 'PEDIDO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `impuestos`
--

CREATE TABLE `impuestos` (
  `id_impuesto` bigint(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `tipo` varchar(15) NOT NULL,
  `tasa` decimal(5,2) NOT NULL,
  `activo` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `impuestos`
--

INSERT INTO `impuestos` (`id_impuesto`, `nombre`, `tipo`, `tasa`, `activo`) VALUES
(1, 'IGV 18%', 'AFECTO', 18.00, 1),
(2, 'Exonerado', 'EXONERADO', 0.00, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inventario_movimientos`
--

CREATE TABLE `inventario_movimientos` (
  `id_movimiento_inventario` bigint(20) NOT NULL,
  `fecha` datetime NOT NULL,
  `id_almacen` bigint(20) NOT NULL,
  `id_producto` bigint(20) NOT NULL,
  `id_tipo_mov_inventario` bigint(20) NOT NULL,
  `id_motivo_mov_inventario` bigint(20) NOT NULL,
  `cantidad` decimal(14,3) NOT NULL,
  `costo_unit` decimal(12,4) DEFAULT 0.0000,
  `id_ref_mov_inventario` bigint(20) DEFAULT NULL,
  `referencia_id` bigint(20) DEFAULT NULL,
  `observacion` varchar(300) DEFAULT NULL,
  `id_usuario` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `marcas`
--

CREATE TABLE `marcas` (
  `id_marca` bigint(20) NOT NULL,
  `nombre` varchar(120) NOT NULL,
  `activo` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `marcas`
--

INSERT INTO `marcas` (`id_marca`, `nombre`, `activo`) VALUES
(1, 'Castrol', 1),
(2, 'Mobil', 1),
(3, 'Shell', 1),
(4, 'TotalEnergies', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `metodos_pago`
--

CREATE TABLE `metodos_pago` (
  `id_metodo_pago` bigint(20) NOT NULL,
  `nombre` varchar(80) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `metodos_pago`
--

INSERT INTO `metodos_pago` (`id_metodo_pago`, `nombre`) VALUES
(1, 'Efectivo'),
(2, 'Tarjeta'),
(3, 'Transferencia'),
(4, 'Yape/Plin');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `motivo_mov_inventarios`
--

CREATE TABLE `motivo_mov_inventarios` (
  `id_motivo_mov_inventario` bigint(20) NOT NULL,
  `codigo` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `motivo_mov_inventarios`
--

INSERT INTO `motivo_mov_inventarios` (`id_motivo_mov_inventario`, `codigo`) VALUES
(3, 'AJUSTE'),
(1, 'COMPRA'),
(6, 'DEVOLUCION_COMPRA'),
(7, 'DEVOLUCION_VENTA'),
(4, 'TRASLADO_ENT'),
(5, 'TRASLADO_SAL'),
(2, 'VENTA');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `origen_mov_cajas`
--

CREATE TABLE `origen_mov_cajas` (
  `id_origen_mov_caja` bigint(20) NOT NULL,
  `codigo` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `origen_mov_cajas`
--

INSERT INTO `origen_mov_cajas` (`id_origen_mov_caja`, `codigo`) VALUES
(5, 'AJUSTE'),
(4, 'CAJA_CHICA'),
(2, 'COBRO'),
(6, 'OTRO'),
(3, 'PAGO'),
(1, 'VENTA');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedidos`
--

CREATE TABLE `pedidos` (
  `id_pedido` bigint(20) NOT NULL,
  `fecha` datetime NOT NULL,
  `id_cliente` bigint(20) NOT NULL,
  `id_estado_pedido` bigint(20) NOT NULL,
  `id_canal_pedido` bigint(20) NOT NULL,
  `moneda` char(3) DEFAULT 'PEN',
  `tipo_cambio` decimal(10,4) DEFAULT 1.0000,
  `subtotal` decimal(14,2) DEFAULT 0.00,
  `impuesto_monto` decimal(14,2) DEFAULT 0.00,
  `total` decimal(14,2) DEFAULT 0.00,
  `direccion_envio` varchar(300) DEFAULT NULL,
  `metodo_pago` varchar(100) DEFAULT NULL,
  `transaccion_ref` varchar(120) DEFAULT NULL,
  `observacion` varchar(300) DEFAULT NULL,
  `id_usuario` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedido_items`
--

CREATE TABLE `pedido_items` (
  `id_item_pedido` bigint(20) NOT NULL,
  `id_pedido` bigint(20) NOT NULL,
  `id_producto` bigint(20) NOT NULL,
  `cantidad` decimal(14,3) NOT NULL,
  `precio_unit` decimal(12,2) NOT NULL,
  `impuesto_pct` decimal(5,2) DEFAULT NULL,
  `impuesto_monto` decimal(12,2) DEFAULT 0.00,
  `total_linea` decimal(14,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `permisos`
--

CREATE TABLE `permisos` (
  `id_permiso` bigint(20) NOT NULL,
  `codigo` varchar(120) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `permisos`
--

INSERT INTO `permisos` (`id_permiso`, `codigo`, `descripcion`) VALUES
(1, 'ventas.ver', 'Ver ventas'),
(2, 'ventas.crear', 'Crear ventas'),
(3, 'caja.cerrar', 'Cerrar caja');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `personas`
--

CREATE TABLE `personas` (
  `id_persona` bigint(20) NOT NULL,
  `id_tipo_documento` bigint(20) NOT NULL,
  `nro_doc` varchar(20) NOT NULL,
  `tipo_persona` varchar(10) NOT NULL,
  `nombres` varchar(120) DEFAULT NULL,
  `apellidos` varchar(120) DEFAULT NULL,
  `razon_social` varchar(200) DEFAULT NULL,
  `email` varchar(120) DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `direccion` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `personas`
--

INSERT INTO `personas` (`id_persona`, `id_tipo_documento`, `nro_doc`, `tipo_persona`, `nombres`, `apellidos`, `razon_social`, `email`, `telefono`, `direccion`) VALUES
(1, 1, '25455666', 'NATURAL', 'ronald', 'ramos', NULL, 'ronald.ramos.malca.30@gmail.com', '2122', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id_producto` bigint(20) NOT NULL,
  `codigo` varchar(60) NOT NULL,
  `nombre` varchar(200) NOT NULL,
  `id_marca` bigint(20) DEFAULT NULL,
  `id_categoria` bigint(20) DEFAULT NULL,
  `id_tipo_producto` bigint(20) NOT NULL,
  `unidad` varchar(20) DEFAULT 'UND',
  `costo` decimal(12,2) NOT NULL DEFAULT 0.00,
  `precio` decimal(12,2) NOT NULL DEFAULT 0.00,
  `precio_online` decimal(12,2) DEFAULT NULL,
  `publicar_online` tinyint(1) DEFAULT 0,
  `id_impuesto` bigint(20) DEFAULT NULL,
  `id_afectacion` bigint(20) DEFAULT NULL,
  `stock_minimo` decimal(12,2) DEFAULT 0.00,
  `activo` tinyint(1) DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `imagenes` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`imagenes`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id_producto`, `codigo`, `nombre`, `id_marca`, `id_categoria`, `id_tipo_producto`, `unidad`, `costo`, `precio`, `precio_online`, `publicar_online`, `id_impuesto`, `id_afectacion`, `stock_minimo`, `activo`, `created_at`, `updated_at`, `imagenes`) VALUES
(1, 'ACE-MOB-5W30', 'Aceite 5W-30 Mobil 1 4L', 2, 1, 1, 'GAL', 75.00, 120.00, NULL, 1, 1, NULL, 2.00, 1, '2025-09-30 14:09:14', '2025-09-30 14:09:14', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proveedores`
--

CREATE TABLE `proveedores` (
  `id_proveedor` bigint(20) NOT NULL,
  `ruc` varchar(20) DEFAULT NULL,
  `razon_social` varchar(200) NOT NULL,
  `direccion` varchar(300) DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `email` varchar(120) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `proveedores`
--

INSERT INTO `proveedores` (`id_proveedor`, `ruc`, `razon_social`, `direccion`, `telefono`, `email`, `activo`) VALUES
(1, NULL, 'Proveedor Demo', NULL, NULL, NULL, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ref_mov_inventarios`
--

CREATE TABLE `ref_mov_inventarios` (
  `id_ref_mov_inventario` bigint(20) NOT NULL,
  `codigo` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `ref_mov_inventarios`
--

INSERT INTO `ref_mov_inventarios` (`id_ref_mov_inventario`, `codigo`) VALUES
(3, 'AJUSTE'),
(1, 'COMPRA'),
(5, 'OTRO'),
(4, 'PEDIDO'),
(2, 'VENTA');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id_rol` bigint(20) NOT NULL,
  `nombre` varchar(80) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id_rol`, `nombre`, `descripcion`) VALUES
(1, 'ADMIN', NULL),
(2, 'CAJERO', NULL),
(3, 'ALMACENERO', NULL),
(4, 'VENDEDOR', NULL),
(5, 'CLIENTE', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol_permiso`
--

CREATE TABLE `rol_permiso` (
  `id_rol` bigint(20) NOT NULL,
  `id_permiso` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `series`
--

CREATE TABLE `series` (
  `id_serie` bigint(20) NOT NULL,
  `id_sucursal` bigint(20) NOT NULL,
  `id_tipo_comprobante` bigint(20) NOT NULL,
  `serie` varchar(8) NOT NULL,
  `correlativo_actual` int(11) NOT NULL DEFAULT 0,
  `activo` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `series`
--

INSERT INTO `series` (`id_serie`, `id_sucursal`, `id_tipo_comprobante`, `serie`, `correlativo_actual`, `activo`) VALUES
(1, 1, 1, 'F001', 0, 1),
(2, 1, 2, 'B001', 0, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `stock`
--

CREATE TABLE `stock` (
  `id_almacen` bigint(20) NOT NULL,
  `id_producto` bigint(20) NOT NULL,
  `cantidad` decimal(14,3) NOT NULL DEFAULT 0.000
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `stock`
--

INSERT INTO `stock` (`id_almacen`, `id_producto`, `cantidad`) VALUES
(1, 1, 20.000);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sucursales`
--

CREATE TABLE `sucursales` (
  `id_sucursal` bigint(20) NOT NULL,
  `id_empresa` bigint(20) NOT NULL,
  `nombre` varchar(120) NOT NULL,
  `direccion` varchar(300) DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `sucursales`
--

INSERT INTO `sucursales` (`id_sucursal`, `id_empresa`, `nombre`, `direccion`, `telefono`, `activo`, `created_at`, `updated_at`) VALUES
(1, 1, 'Chiclayo Central', NULL, NULL, 1, '2025-09-30 14:09:13', '2025-09-30 14:09:13');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipos_comprobante`
--

CREATE TABLE `tipos_comprobante` (
  `id_tipo_comprobante` bigint(20) NOT NULL,
  `codigo` varchar(10) NOT NULL,
  `nombre` varchar(120) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `tipos_comprobante`
--

INSERT INTO `tipos_comprobante` (`id_tipo_comprobante`, `codigo`, `nombre`) VALUES
(1, 'FAC', 'Factura'),
(2, 'BOL', 'Boleta'),
(3, 'NC', 'Nota de Crédito');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipos_documento`
--

CREATE TABLE `tipos_documento` (
  `id_tipo_documento` bigint(20) NOT NULL,
  `codigo` varchar(5) NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `tipos_documento`
--

INSERT INTO `tipos_documento` (`id_tipo_documento`, `codigo`, `activo`) VALUES
(1, 'DNI', 1),
(2, 'RUC', 1),
(3, 'CE', 0),
(4, 'PAS', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_mov_cajas`
--

CREATE TABLE `tipo_mov_cajas` (
  `id_tipo_mov_caja` bigint(20) NOT NULL,
  `codigo` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `tipo_mov_cajas`
--

INSERT INTO `tipo_mov_cajas` (`id_tipo_mov_caja`, `codigo`) VALUES
(2, 'EGRESO'),
(1, 'INGRESO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_mov_inventarios`
--

CREATE TABLE `tipo_mov_inventarios` (
  `id_tipo_mov_inventario` bigint(20) NOT NULL,
  `codigo` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `tipo_mov_inventarios`
--

INSERT INTO `tipo_mov_inventarios` (`id_tipo_mov_inventario`, `codigo`) VALUES
(3, 'AJUSTE'),
(1, 'IN'),
(2, 'OUT');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_productos`
--

CREATE TABLE `tipo_productos` (
  `id_tipo_producto` bigint(20) NOT NULL,
  `codigo` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `tipo_productos`
--

INSERT INTO `tipo_productos` (`id_tipo_producto`, `codigo`) VALUES
(1, 'PRODUCTO'),
(2, 'SERVICIO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id_usuario` bigint(20) NOT NULL,
  `id_persona` bigint(20) DEFAULT NULL,
  `id_sucursal` bigint(20) DEFAULT NULL,
  `nombre_usuario` varchar(100) DEFAULT NULL,
  `hash_pass` varchar(255) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `id_persona`, `id_sucursal`, `nombre_usuario`, `hash_pass`, `activo`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 'admin', '$2a$10$ZRvXec/SkP2IZs4AS977HueFkxxWq7VjELS4IlahLmfcwUFkgtrj6', 1, '2025-09-30 14:09:13', '2025-10-17 03:49:50');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_rol`
--

CREATE TABLE `usuario_rol` (
  `id_usuario` bigint(20) NOT NULL,
  `id_rol` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `usuario_rol`
--

INSERT INTO `usuario_rol` (`id_usuario`, `id_rol`) VALUES
(1, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ventas`
--

CREATE TABLE `ventas` (
  `id_venta` bigint(20) NOT NULL,
  `fecha` datetime NOT NULL,
  `id_sucursal` bigint(20) NOT NULL,
  `id_almacen` bigint(20) NOT NULL,
  `id_cliente` bigint(20) DEFAULT NULL,
  `id_usuario` bigint(20) NOT NULL,
  `id_canal_venta` bigint(20) NOT NULL,
  `id_estado_venta` bigint(20) DEFAULT NULL,
  `id_estado_sunat` bigint(20) DEFAULT NULL,
  `id_afectacion` bigint(20) NOT NULL,
  `id_condicion_pago` bigint(20) NOT NULL,
  `dias_credito` int(11) DEFAULT NULL,
  `moneda` char(3) DEFAULT 'PEN',
  `tipo_cambio` decimal(10,4) DEFAULT 1.0000,
  `subtotal` decimal(14,2) DEFAULT 0.00,
  `impuesto_monto` decimal(14,2) DEFAULT 0.00,
  `total` decimal(14,2) DEFAULT 0.00,
  `id_tipo_comprobante` bigint(20) DEFAULT NULL,
  `id_serie` bigint(20) DEFAULT NULL,
  `numero` int(11) DEFAULT NULL,
  `id_pedido` bigint(20) DEFAULT NULL,
  `observacion` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `venta_items`
--

CREATE TABLE `venta_items` (
  `id_item_venta` bigint(20) NOT NULL,
  `id_venta` bigint(20) NOT NULL,
  `id_producto` bigint(20) NOT NULL,
  `id_marca` bigint(20) DEFAULT NULL,
  `cantidad` decimal(14,3) NOT NULL,
  `valor_unit` decimal(12,6) DEFAULT NULL,
  `precio_unit` decimal(12,6) DEFAULT NULL,
  `descuento` decimal(12,2) DEFAULT 0.00,
  `igv_pct` decimal(5,2) DEFAULT 18.00,
  `igv_monto` decimal(12,2) DEFAULT 0.00,
  `id_afectacion` bigint(20) NOT NULL,
  `total_linea` decimal(14,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Disparadores `venta_items`
--
DELIMITER $$
CREATE TRIGGER `trg_venta_item_ai` AFTER INSERT ON `venta_items` FOR EACH ROW BEGIN
  DECLARE v_estado VARCHAR(20);
  DECLARE v_id_alm BIGINT;

  SELECT ev.codigo, v.id_almacen
    INTO v_estado, v_id_alm
  FROM ventas v
  LEFT JOIN estado_ventas ev ON ev.id_estado_venta = v.id_estado_venta
  WHERE v.id_venta = NEW.id_venta;

  IF v_estado = 'EMITIDO' THEN
    INSERT INTO inventario_movimientos
      (fecha,id_almacen,id_producto,id_tipo_mov_inventario,id_motivo_mov_inventario,cantidad,costo_unit,id_ref_mov_inventario,referencia_id)
    VALUES
      (NOW(), v_id_alm, NEW.id_producto,
       (SELECT id_tipo_mov_inventario FROM tipo_mov_inventarios WHERE codigo='OUT'),
       (SELECT id_motivo_mov_inventario FROM motivo_mov_inventarios WHERE codigo='VENTA'),
       NEW.cantidad, 0.0,
       (SELECT id_ref_mov_inventario FROM ref_mov_inventarios WHERE codigo='VENTA'),
       NEW.id_venta);

    INSERT INTO stock (id_almacen,id_producto,cantidad)
      VALUES (v_id_alm, NEW.id_producto, 0 - NEW.cantidad)
    ON DUPLICATE KEY UPDATE cantidad = cantidad - NEW.cantidad;
  END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `v_comprobantes_pendientes`
--

CREATE TABLE `v_comprobantes_pendientes` (
  `cxc_id` bigint(20) DEFAULT NULL,
  `venta_id` bigint(20) DEFAULT NULL,
  `tipo` varchar(10) DEFAULT NULL,
  `serie` varchar(8) DEFAULT NULL,
  `numero` int(11) DEFAULT NULL,
  `fecha` datetime DEFAULT NULL,
  `fecha_venc` date DEFAULT NULL,
  `monto_total` decimal(14,2) DEFAULT NULL,
  `saldo` decimal(14,2) DEFAULT NULL,
  `estado` varchar(20) DEFAULT NULL,
  `id_cliente` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `v_estado_cxc`
--

CREATE TABLE `v_estado_cxc` (
  `cliente_id` bigint(20) DEFAULT NULL,
  `cliente` varchar(241) DEFAULT NULL,
  `total_emitido` decimal(36,2) DEFAULT NULL,
  `saldo_pendiente` decimal(36,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `v_kardex`
--

CREATE TABLE `v_kardex` (
  `fecha` datetime DEFAULT NULL,
  `almacen` varchar(120) DEFAULT NULL,
  `codigo` varchar(60) DEFAULT NULL,
  `producto` varchar(200) DEFAULT NULL,
  `tipo` varchar(10) DEFAULT NULL,
  `motivo` varchar(30) DEFAULT NULL,
  `cantidad` decimal(14,3) DEFAULT NULL,
  `costo_unit` decimal(12,4) DEFAULT NULL,
  `referencia_tipo` varchar(20) DEFAULT NULL,
  `referencia_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `v_mov_por_caja`
--

CREATE TABLE `v_mov_por_caja` (
  `id_sucursal` bigint(20) DEFAULT NULL,
  `codigo_caja` varchar(30) DEFAULT NULL,
  `id` bigint(20) DEFAULT NULL,
  `id_caja` bigint(20) DEFAULT NULL,
  `fecha` datetime DEFAULT NULL,
  `tipo` varchar(20) DEFAULT NULL,
  `origen` varchar(20) DEFAULT NULL,
  `referencia_id` bigint(20) DEFAULT NULL,
  `concepto` varchar(200) DEFAULT NULL,
  `monto` decimal(14,2) DEFAULT NULL,
  `id_usuario` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `v_mov_por_producto`
--

CREATE TABLE `v_mov_por_producto` (
  `fecha` datetime DEFAULT NULL,
  `id_almacen` bigint(20) DEFAULT NULL,
  `codigo` varchar(60) DEFAULT NULL,
  `nombre` varchar(200) DEFAULT NULL,
  `tipo` varchar(20) DEFAULT NULL,
  `mov_tipo` varchar(10) DEFAULT NULL,
  `motivo` varchar(30) DEFAULT NULL,
  `cantidad` decimal(14,3) DEFAULT NULL,
  `costo_unit` decimal(12,4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `v_resumen_por_producto`
--

CREATE TABLE `v_resumen_por_producto` (
  `codigo` varchar(60) DEFAULT NULL,
  `nombre` varchar(200) DEFAULT NULL,
  `tipo` varchar(20) DEFAULT NULL,
  `ingreso` decimal(36,3) DEFAULT NULL,
  `salida` decimal(36,3) DEFAULT NULL,
  `saldo_mov` decimal(37,3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `v_ventas_detalle`
--

CREATE TABLE `v_ventas_detalle` (
  `venta_id` bigint(20) DEFAULT NULL,
  `fecha` datetime DEFAULT NULL,
  `canal` varchar(20) DEFAULT NULL,
  `estado` varchar(20) DEFAULT NULL,
  `total` decimal(14,2) DEFAULT NULL,
  `id_sucursal` bigint(20) DEFAULT NULL,
  `id_usuario` bigint(20) DEFAULT NULL,
  `vendedor` varchar(100) DEFAULT NULL,
  `id_cliente` bigint(20) DEFAULT NULL,
  `cliente` varchar(241) DEFAULT NULL,
  `id_producto` bigint(20) DEFAULT NULL,
  `codigo` varchar(60) DEFAULT NULL,
  `producto` varchar(200) DEFAULT NULL,
  `marca` varchar(120) DEFAULT NULL,
  `cantidad` decimal(14,3) DEFAULT NULL,
  `precio_unit` decimal(12,6) DEFAULT NULL,
  `total_linea` decimal(14,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `v_ventas_por_marca`
--

CREATE TABLE `v_ventas_por_marca` (
  `fecha` date DEFAULT NULL,
  `marca` varchar(120) DEFAULT NULL,
  `venta_total` decimal(36,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `afectaciones`
--
ALTER TABLE `afectaciones`
  ADD PRIMARY KEY (`id_afectacion`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `almacenes`
--
ALTER TABLE `almacenes`
  ADD PRIMARY KEY (`id_almacen`),
  ADD KEY `id_sucursal` (`id_sucursal`);

--
-- Indices de la tabla `cajas`
--
ALTER TABLE `cajas`
  ADD PRIMARY KEY (`id_caja`),
  ADD UNIQUE KEY `uk_caja` (`id_sucursal`,`codigo`),
  ADD KEY `id_abierto_por` (`id_abierto_por`),
  ADD KEY `id_cerrado_por` (`id_cerrado_por`),
  ADD KEY `fk_caja_estado` (`id_estado_caja`);

--
-- Indices de la tabla `caja_movimientos`
--
ALTER TABLE `caja_movimientos`
  ADD PRIMARY KEY (`id_movimiento_caja`),
  ADD KEY `id_caja` (`id_caja`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `fecha` (`fecha`),
  ADD KEY `id_tipo_mov_caja` (`id_tipo_mov_caja`),
  ADD KEY `fk_cm_origen` (`id_origen_mov_caja`);

--
-- Indices de la tabla `canales_pedido`
--
ALTER TABLE `canales_pedido`
  ADD PRIMARY KEY (`id_canal_pedido`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `canales_venta`
--
ALTER TABLE `canales_venta`
  ADD PRIMARY KEY (`id_canal_venta`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `categorias`
--
ALTER TABLE `categorias`
  ADD PRIMARY KEY (`id_categoria`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id_cliente`),
  ADD KEY `fk_cliente_persona` (`id_persona`);

--
-- Indices de la tabla `cobros`
--
ALTER TABLE `cobros`
  ADD PRIMARY KEY (`id_cobro`),
  ADD KEY `id_cuenta_cobrar` (`id_cuenta_cobrar`),
  ADD KEY `id_metodo_pago` (`id_metodo_pago`),
  ADD KEY `id_caja` (`id_caja`),
  ADD KEY `id_usuario` (`id_usuario`);

--
-- Indices de la tabla `compras`
--
ALTER TABLE `compras`
  ADD PRIMARY KEY (`id_compra`),
  ADD KEY `id_proveedor` (`id_proveedor`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `id_almacen` (`id_almacen`),
  ADD KEY `fecha` (`fecha`),
  ADD KEY `id_estado_compra` (`id_estado_compra`);

--
-- Indices de la tabla `compra_items`
--
ALTER TABLE `compra_items`
  ADD PRIMARY KEY (`id_item_compra`),
  ADD KEY `id_compra` (`id_compra`),
  ADD KEY `id_producto` (`id_producto`);

--
-- Indices de la tabla `condiciones_pago`
--
ALTER TABLE `condiciones_pago`
  ADD PRIMARY KEY (`id_condicion_pago`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `cuentas_cobrar`
--
ALTER TABLE `cuentas_cobrar`
  ADD PRIMARY KEY (`id_cuenta_cobrar`),
  ADD KEY `id_venta` (`id_venta`),
  ADD KEY `id_cliente` (`id_cliente`),
  ADD KEY `id_estado_cxc` (`id_estado_cxc`);

--
-- Indices de la tabla `empresas`
--
ALTER TABLE `empresas`
  ADD PRIMARY KEY (`id_empresa`),
  ADD UNIQUE KEY `ruc` (`ruc`);

--
-- Indices de la tabla `estado_cajas`
--
ALTER TABLE `estado_cajas`
  ADD PRIMARY KEY (`id_estado_caja`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `estado_compras`
--
ALTER TABLE `estado_compras`
  ADD PRIMARY KEY (`id_estado_compra`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `estado_cxc`
--
ALTER TABLE `estado_cxc`
  ADD PRIMARY KEY (`id_estado_cxc`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `estado_pedidos`
--
ALTER TABLE `estado_pedidos`
  ADD PRIMARY KEY (`id_estado_pedido`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `estado_sunat`
--
ALTER TABLE `estado_sunat`
  ADD PRIMARY KEY (`id_estado_sunat`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `estado_ventas`
--
ALTER TABLE `estado_ventas`
  ADD PRIMARY KEY (`id_estado_venta`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `impuestos`
--
ALTER TABLE `impuestos`
  ADD PRIMARY KEY (`id_impuesto`);

--
-- Indices de la tabla `inventario_movimientos`
--
ALTER TABLE `inventario_movimientos`
  ADD PRIMARY KEY (`id_movimiento_inventario`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `fecha` (`fecha`),
  ADD KEY `id_producto` (`id_producto`),
  ADD KEY `id_almacen` (`id_almacen`,`id_producto`),
  ADD KEY `fk_im_tipo` (`id_tipo_mov_inventario`),
  ADD KEY `fk_im_motivo` (`id_motivo_mov_inventario`),
  ADD KEY `fk_im_ref` (`id_ref_mov_inventario`);

--
-- Indices de la tabla `marcas`
--
ALTER TABLE `marcas`
  ADD PRIMARY KEY (`id_marca`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `metodos_pago`
--
ALTER TABLE `metodos_pago`
  ADD PRIMARY KEY (`id_metodo_pago`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `motivo_mov_inventarios`
--
ALTER TABLE `motivo_mov_inventarios`
  ADD PRIMARY KEY (`id_motivo_mov_inventario`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `origen_mov_cajas`
--
ALTER TABLE `origen_mov_cajas`
  ADD PRIMARY KEY (`id_origen_mov_caja`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD PRIMARY KEY (`id_pedido`),
  ADD KEY `id_cliente` (`id_cliente`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `fecha` (`fecha`),
  ADD KEY `id_estado_pedido` (`id_estado_pedido`),
  ADD KEY `fk_pedido_canal` (`id_canal_pedido`);

--
-- Indices de la tabla `pedido_items`
--
ALTER TABLE `pedido_items`
  ADD PRIMARY KEY (`id_item_pedido`),
  ADD KEY `id_pedido` (`id_pedido`),
  ADD KEY `id_producto` (`id_producto`);

--
-- Indices de la tabla `permisos`
--
ALTER TABLE `permisos`
  ADD PRIMARY KEY (`id_permiso`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `personas`
--
ALTER TABLE `personas`
  ADD PRIMARY KEY (`id_persona`),
  ADD UNIQUE KEY `uk_persona_doc` (`id_tipo_documento`,`nro_doc`),
  ADD UNIQUE KEY `uk_persona_email` (`email`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id_producto`),
  ADD UNIQUE KEY `codigo` (`codigo`),
  ADD KEY `id_marca` (`id_marca`),
  ADD KEY `id_categoria` (`id_categoria`),
  ADD KEY `id_impuesto` (`id_impuesto`),
  ADD KEY `id_tipo_producto` (`id_tipo_producto`),
  ADD KEY `id_afectacion` (`id_afectacion`);

--
-- Indices de la tabla `proveedores`
--
ALTER TABLE `proveedores`
  ADD PRIMARY KEY (`id_proveedor`),
  ADD UNIQUE KEY `ruc` (`ruc`);

--
-- Indices de la tabla `ref_mov_inventarios`
--
ALTER TABLE `ref_mov_inventarios`
  ADD PRIMARY KEY (`id_ref_mov_inventario`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id_rol`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `rol_permiso`
--
ALTER TABLE `rol_permiso`
  ADD PRIMARY KEY (`id_rol`,`id_permiso`),
  ADD KEY `fk_rp_permiso` (`id_permiso`);

--
-- Indices de la tabla `series`
--
ALTER TABLE `series`
  ADD PRIMARY KEY (`id_serie`),
  ADD UNIQUE KEY `uk_serie` (`id_sucursal`,`id_tipo_comprobante`,`serie`),
  ADD KEY `id_tipo_comprobante` (`id_tipo_comprobante`);

--
-- Indices de la tabla `stock`
--
ALTER TABLE `stock`
  ADD PRIMARY KEY (`id_almacen`,`id_producto`),
  ADD KEY `id_producto` (`id_producto`);

--
-- Indices de la tabla `sucursales`
--
ALTER TABLE `sucursales`
  ADD PRIMARY KEY (`id_sucursal`),
  ADD KEY `id_empresa` (`id_empresa`);

--
-- Indices de la tabla `tipos_comprobante`
--
ALTER TABLE `tipos_comprobante`
  ADD PRIMARY KEY (`id_tipo_comprobante`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `tipos_documento`
--
ALTER TABLE `tipos_documento`
  ADD PRIMARY KEY (`id_tipo_documento`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `tipo_mov_cajas`
--
ALTER TABLE `tipo_mov_cajas`
  ADD PRIMARY KEY (`id_tipo_mov_caja`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `tipo_mov_inventarios`
--
ALTER TABLE `tipo_mov_inventarios`
  ADD PRIMARY KEY (`id_tipo_mov_inventario`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `tipo_productos`
--
ALTER TABLE `tipo_productos`
  ADD PRIMARY KEY (`id_tipo_producto`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuario`),
  ADD KEY `id_sucursal` (`id_sucursal`),
  ADD KEY `fk_usuario_persona` (`id_persona`);

--
-- Indices de la tabla `usuario_rol`
--
ALTER TABLE `usuario_rol`
  ADD PRIMARY KEY (`id_usuario`,`id_rol`),
  ADD KEY `fk_ur_rol` (`id_rol`);

--
-- Indices de la tabla `ventas`
--
ALTER TABLE `ventas`
  ADD PRIMARY KEY (`id_venta`),
  ADD UNIQUE KEY `uk_comprobante` (`id_tipo_comprobante`,`id_serie`,`numero`),
  ADD KEY `id_sucursal` (`id_sucursal`),
  ADD KEY `id_almacen` (`id_almacen`),
  ADD KEY `id_cliente` (`id_cliente`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `id_serie` (`id_serie`),
  ADD KEY `id_pedido` (`id_pedido`),
  ADD KEY `fecha` (`fecha`),
  ADD KEY `id_estado_venta` (`id_estado_venta`),
  ADD KEY `id_canal_venta` (`id_canal_venta`),
  ADD KEY `fk_venta_afect` (`id_afectacion`),
  ADD KEY `fk_venta_condicion` (`id_condicion_pago`),
  ADD KEY `idx_ventas_sunat` (`id_estado_sunat`),
  ADD KEY `idx_ventas_estado` (`id_estado_venta`),
  ADD KEY `idx_ventas_canal_cond` (`id_canal_venta`,`id_condicion_pago`);

--
-- Indices de la tabla `venta_items`
--
ALTER TABLE `venta_items`
  ADD PRIMARY KEY (`id_item_venta`),
  ADD KEY `id_venta` (`id_venta`),
  ADD KEY `id_producto` (`id_producto`),
  ADD KEY `id_marca` (`id_marca`),
  ADD KEY `id_afectacion` (`id_afectacion`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `afectaciones`
--
ALTER TABLE `afectaciones`
  MODIFY `id_afectacion` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `almacenes`
--
ALTER TABLE `almacenes`
  MODIFY `id_almacen` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `cajas`
--
ALTER TABLE `cajas`
  MODIFY `id_caja` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `caja_movimientos`
--
ALTER TABLE `caja_movimientos`
  MODIFY `id_movimiento_caja` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `canales_pedido`
--
ALTER TABLE `canales_pedido`
  MODIFY `id_canal_pedido` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `canales_venta`
--
ALTER TABLE `canales_venta`
  MODIFY `id_canal_venta` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `categorias`
--
ALTER TABLE `categorias`
  MODIFY `id_categoria` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id_cliente` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `cobros`
--
ALTER TABLE `cobros`
  MODIFY `id_cobro` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `compras`
--
ALTER TABLE `compras`
  MODIFY `id_compra` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `compra_items`
--
ALTER TABLE `compra_items`
  MODIFY `id_item_compra` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `condiciones_pago`
--
ALTER TABLE `condiciones_pago`
  MODIFY `id_condicion_pago` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `cuentas_cobrar`
--
ALTER TABLE `cuentas_cobrar`
  MODIFY `id_cuenta_cobrar` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `empresas`
--
ALTER TABLE `empresas`
  MODIFY `id_empresa` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `estado_cajas`
--
ALTER TABLE `estado_cajas`
  MODIFY `id_estado_caja` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `estado_compras`
--
ALTER TABLE `estado_compras`
  MODIFY `id_estado_compra` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `estado_cxc`
--
ALTER TABLE `estado_cxc`
  MODIFY `id_estado_cxc` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `estado_pedidos`
--
ALTER TABLE `estado_pedidos`
  MODIFY `id_estado_pedido` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `estado_sunat`
--
ALTER TABLE `estado_sunat`
  MODIFY `id_estado_sunat` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `estado_ventas`
--
ALTER TABLE `estado_ventas`
  MODIFY `id_estado_venta` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `impuestos`
--
ALTER TABLE `impuestos`
  MODIFY `id_impuesto` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `inventario_movimientos`
--
ALTER TABLE `inventario_movimientos`
  MODIFY `id_movimiento_inventario` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `marcas`
--
ALTER TABLE `marcas`
  MODIFY `id_marca` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `metodos_pago`
--
ALTER TABLE `metodos_pago`
  MODIFY `id_metodo_pago` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `motivo_mov_inventarios`
--
ALTER TABLE `motivo_mov_inventarios`
  MODIFY `id_motivo_mov_inventario` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `origen_mov_cajas`
--
ALTER TABLE `origen_mov_cajas`
  MODIFY `id_origen_mov_caja` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  MODIFY `id_pedido` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `pedido_items`
--
ALTER TABLE `pedido_items`
  MODIFY `id_item_pedido` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `permisos`
--
ALTER TABLE `permisos`
  MODIFY `id_permiso` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `personas`
--
ALTER TABLE `personas`
  MODIFY `id_persona` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id_producto` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `proveedores`
--
ALTER TABLE `proveedores`
  MODIFY `id_proveedor` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `ref_mov_inventarios`
--
ALTER TABLE `ref_mov_inventarios`
  MODIFY `id_ref_mov_inventario` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id_rol` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `series`
--
ALTER TABLE `series`
  MODIFY `id_serie` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `sucursales`
--
ALTER TABLE `sucursales`
  MODIFY `id_sucursal` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `tipos_comprobante`
--
ALTER TABLE `tipos_comprobante`
  MODIFY `id_tipo_comprobante` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `tipos_documento`
--
ALTER TABLE `tipos_documento`
  MODIFY `id_tipo_documento` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `tipo_mov_cajas`
--
ALTER TABLE `tipo_mov_cajas`
  MODIFY `id_tipo_mov_caja` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `tipo_mov_inventarios`
--
ALTER TABLE `tipo_mov_inventarios`
  MODIFY `id_tipo_mov_inventario` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `tipo_productos`
--
ALTER TABLE `tipo_productos`
  MODIFY `id_tipo_producto` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id_usuario` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `ventas`
--
ALTER TABLE `ventas`
  MODIFY `id_venta` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `venta_items`
--
ALTER TABLE `venta_items`
  MODIFY `id_item_venta` bigint(20) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

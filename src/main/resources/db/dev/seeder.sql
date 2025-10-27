INSERT INTO `rol` (`id`, `nombre`, `descripcion`) VALUES
(1, 'ADMIN', 'Administrador del sistema'),
(2, 'CAJERO', 'Encargado de caja'),
(3, 'ALMACENERO', 'Encargado de almacén'),
(4, 'VENDEDOR', 'Encargado de ventas'),
(5, 'CLIENTE', 'Cliente del sistema');

INSERT INTO `tipo_documento` (`id`, `codigo`, `estado`) VALUES
(1, 'DNI', 2),
(2, 'RUC', 2),
(3, 'CE', 1),
(4, 'PAS', 1);

INSERT INTO `empresa` (`id`, `ruc`, `razon_social`, `nombre_comercial`, `direccion`, `telefono`, `email`, `igv_por_defecto`, `moneda_base`, `fecha_creacion`, `fecha_actualizacion`) VALUES
(1, '20123456789', 'Aceites Perú S.A.C.', 'Aceites Perú', 'Calle Falsa 123', '999999999', 'contacto@aceitesperu.com', 18.00, 'PEN', '2025-09-30 14:09:13', '2025-09-30 14:09:13');

INSERT INTO `sucursal` (`id`, `empresa_id`, `nombre`, `direccion`, `telefono`, `estado`, `fecha_creacion`, `fecha_actualizacion`) VALUES
(1, 1, 'Chiclayo Central', 'Av. José Balta 123', '999888777', 2, '2025-09-30 14:09:13', '2025-09-30 14:09:13');
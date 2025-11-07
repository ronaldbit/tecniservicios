INSERT INTO `rol` (`estado`,`id`, `nombre`, `descripcion`)
VALUES
    (1, 1, 'ADMIN', 'Administrador del sistema'),
    (1, 2, 'CAJERO', 'Encargado de caja'),
    (1, 3, 'ALMACENERO', 'Encargado de almacén'),
    (1, 4, 'VENDEDOR', 'Encargado de ventas'),
    (1, 5, 'CLIENTE', 'Cliente del sistema');


INSERT INTO `tipo_documento` (`id`, `codigo`, `estado`)
VALUES
    (1, 'DNI', 2),
    (2, 'RUC', 2),
    (3, 'CE', 1),
    (4, 'PAS', 1);

INSERT INTO `empresa` (`id`, `ruc`, `razon_social`, `nombre_comercial`, `direccion`, `telefono`, `email`, `igv_por_defecto`, `moneda_base`, `fecha_creacion`, `fecha_actualizacion`)
VALUES
    (1, '20123456789', 'Aceites Perú S.A.C.', 'Aceites Perú', 'Calle Falsa 123', '999999999', 'contacto@aceitesperu.com', 18.00, 'PEN', '2025-09-30 14:09:13', '2025-09-30 14:09:13');

INSERT INTO `sucursal` (`id`, `empresa_id`, `nombre`, `direccion`, `telefono`, `estado`, `fecha_creacion`, `fecha_actualizacion`)
VALUES
    (1, 1, 'Chiclayo Central', 'Av. José Balta 123', '999888777', 2, '2025-09-30 14:09:13', '2025-09-30 14:09:13');

INSERT INTO persona 
(`id`, `tipo_documento_id`, `numero_documento`, `tipo_persona`, 
 `nombres`, `apellidos`, `razon_social`, `email`, `telefono`, 
 `direccion`, `estado`, `email_verificado`)
VALUES
(1, 1, '12345678', 'NATURAL', 'Admin', 'Sistema', 'Ing', 
 'admin@tecniservicios.com', '900000001', 'Av. Principal 123', 
 true, true);

INSERT INTO `usuario` (`id`, `persona_id`, `sucursal_id`, `nombre_usuario`, `password`, `estado`, `fecha_creacion`, `fecha_actualizacion`)
VALUES
    (1, 1, 1, 'admin', '$2a$10$uJi4EvQibXmEJ1R.tXqKtukPbJ8TnrESYBFf9VMEfMBVpun.6OR.6', 2, '2025-09-30 14:09:13', '2025-09-30 14:09:13');

INSERT INTO `usuario_rol` (`usuario_id`, `rol_id`)
VALUES
    (1, 1);
    (1, 1);

INSERT INTO `permiso` (`id`,`nombre`, `path`, `rol_id`)
VALUES
    (1, 'Navegacion', '/dashboard/**', 1),
    (2, 'Ventas', '/dashboard/ventas/**', 1),
    (3, 'Logistica', '/dashboard/logistica/**', 1),
    (4, 'Reportes', '/dashboard/reportes/**', 1),
    (5, 'Ajustes', '/dashboard/ajustes/**', 1);

INSERT INTO categorias (nombre, descripcion, estado) VALUES
('Motor', 'Aceites y lubricantes para el motor del vehículo.', 2),
('Transmisión', 'Fluidos para cajas de cambio manuales y automáticas.', 2),
('Hidráulico', 'Fluidos para sistemas hidráulicos y direcciones asistidas.', 2),
('Frenos', 'Líquidos de frenos para todo tipo de vehículos.', 2),
('Refrigerantes', 'Anticongelantes y refrigerantes para el sistema de enfriamiento.', 2),
('Grasas', 'Grasas para rodamientos, chasis y otros componentes.', 2),
('Filtros', 'Categoría para todo tipo de filtros (obsoleto).', 1);

INSERT INTO marcas (nombre, descripcion, estado) VALUES
('Castrol', 'Líder en aceites de motor sintéticos y convencionales.', 2),
('Mobil', 'Amplia gama de lubricantes de alto rendimiento.', 2),
('Shell', 'Conocido por sus líneas Helix (motor) y Rimula (diésel).', 2),
('Valvoline', 'Aceites de motor, fluidos de transmisión y refrigerantes.', 2),
('Liqui Moly', 'Aditivos y aceites de motor de alta calidad (Origen Alemán).', 2),
('TotalEnergies', 'Línea Quartz para vehículos ligeros.', 2),
('Repsol', 'Lubricantes y productos de competición.', 2),
('Motul', 'Especialistas en lubricantes para motocicletas y autos.', 2);
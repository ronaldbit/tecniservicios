INSERT INTO `rol` (`id`, `nombre`, `descripcion`)
VALUES
    (1, 'ADMIN', 'Administrador del sistema'),
    (2, 'CAJERO', 'Encargado de caja'),
    (3, 'ALMACENERO', 'Encargado de almacén'),
    (4, 'VENDEDOR', 'Encargado de ventas'),
    (5, 'CLIENTE', 'Cliente del sistema');


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

INSERT INTO `persona` (`id`, `tipo_documento_id`, `numero_documento`, `tipo_persona`, `nombres`, `apellidos`, `razon_social`, `email`, `telefono`, `direccion`)
VALUES
    (1, 1, '12345678', 'NATURAL', 'Admin', 'Sistema', 'Ing', 'admin@tecniservicios.com', '900000001', 'Av. Principal 123');

INSERT INTO `usuario` (`id`, `persona_id`, `sucursal_id`, `nombre_usuario`, `password`, `estado`, `fecha_creacion`, `fecha_actualizacion`)
VALUES
    (1, 1, 1, 'admin', '$2a$10$uJi4EvQibXmEJ1R.tXqKtukPbJ8TnrESYBFf9VMEfMBVpun.6OR.6', 1, '2025-09-30 14:09:13', '2025-09-30 14:09:13');

INSERT INTO `usuario_rol` (`usuario_id`, `rol_id`)
VALUES
    (1, 1);
    (1, 1);

INSERT INTO `permiso` (`id`,`nombre`, `path`, `rol_id`)
VALUES
    (1, 'Dashboard', '/dashboard/**', 1),
    (2, 'Ventas', '/dashboard/ventas/**', 1),
    (3, 'Logistica', '/dashboard/logistica/**', 1),
    (4, 'Reportes', '/dashboard/reportes/**', 1),
    (5, 'Configuracion', '/dashboard/configuracion/**', 1);

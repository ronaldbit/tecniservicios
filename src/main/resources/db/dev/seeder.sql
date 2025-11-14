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
('Aceites de Motor', 'Lubricantes sintéticos, semisintéticos y minerales para motores gasolina y diésel.', 2),
('Aceite de Transmisión', 'Lubricantes para cajas automáticas, CVT y mecánicas.', 2),
('Refrigerantes', 'Anticongelantes y líquidos refrigerantes para el sistema de enfriamiento.', 2),
('Líquido de Frenos', 'Fluido DOT 3, DOT 4 y DOT 5.1 para sistemas de frenos y embragues.', 2),
('Hidráulicos', 'Aceites hidráulicos para maquinaria y dirección asistida.', 2),
('Grasas', 'Grasas multiuso, litio, grafito y alta temperatura.', 2),
('Aditivos', 'Aditivos para combustible, motor y sistema de aceite.', 2);

INSERT INTO marcas (nombre, descripcion, estado) VALUES
('Castrol', 'Aceites de motor y transmisión de alto rendimiento.', 2),
('Mobil', 'Lubricantes premium para motores y equipos industriales.', 2),
('Shell', 'Conocido por su línea Helix, Rimula y Spirax.', 2),
('Valvoline', 'Lubricantes y aditivos de gran reconocimiento mundial.', 2),
('Liqui Moly', 'Marca alemana premium con aditivos y aceites sintéticos.', 2),
('TotalEnergies', 'Línea Quartz recomendada para vehículos modernos.', 2),
('Repsol', 'Lubricantes españoles usados en autos y motocicletas.', 2),
('Motul', 'Especialistas en motores de alto rendimiento y motos.', 2);


INSERT INTO proveedor (ruc, razon_social, direccion, telefono, email, estado) VALUES
('20145987641', 'Lubricantes del Norte S.A.C.', 'Av. Leguía 1040, Chiclayo', '074-234567', 'ventas@lubnorte.pe', 2),
('20678452311', 'Distribuidora Automotriz Andina E.I.R.L.', 'Jr. Amazonas 650, Cajamarca', '076-208450', 'contacto@andaauto.pe', 2),
('20457896541', 'Comercial Técnica Lambayeque S.R.L.', 'Calle Puno 345, Lambayeque', '074-206789', 'info@ctlambayeque.pe', 2),
('20587965431', 'Importadora El Motor S.A.C.', 'Av. Sánchez Cerro 1120, Piura', '073-257890', 'ventas@importmotorsac.pe', 2),
('20123459871', 'Grupo Industrial LubriCar S.A.', 'Av. América Sur 330, Trujillo', '044-308650', 'contacto@lubricar.pe', 2);

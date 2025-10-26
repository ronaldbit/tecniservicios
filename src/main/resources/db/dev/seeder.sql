INSERT INTO usuario_rol (id, nombre) VALUES
    (1, 'ADMIN'),
    (2, 'USER');

INSERT INTO permiso (id, path, rol_id) VALUES
    (1, '/admin/*', 1),
    (2, '/user/*', 2);

INSERT INTO persona (id, dni, nombre, apellido, direccion, deleted) VALUES
    (1, '12312312', 'Admin', 'User', 'Calle Admin, 1', false),
    (2, '87654321', 'Regular', 'User', 'Calle User, 2', false);

INSERT INTO usuario (id, persona_id, correo, password, rol_id, deleted) VALUES
    (1, 1, 'admin@example.com', '$2a$10$4dcFoKzzGN9CBdE5lTyK0.gG64YXStld5uaRNfJlKBYISjq2iFosq', 1, false),
    (2, 2, 'user@example.com', '$2a$10$4dcFoKzzGN9CBdE5lTyK0.gG64YXStld5uaRNfJlKBYISjq2iFosq', 2, false);
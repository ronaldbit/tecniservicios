INSERT INTO usuario_rol (id, nombre) VALUES
    (1, 'ADMIN'),
    (2, 'USER');

INSERT INTO permiso (id, path, rol_id) VALUES
    (1, '/admin/*', 1),
    (2, '/user/*', 2);
-- Crear usuarios
INSERT INTO usuario (nombre, correo_electronico, contrasena) VALUES
                                                                 ('Usuario Básico', 'user@foro.com', '$2a$10$DOWds0DFD0VYZPbCzUlSPuImKnbbE5VRj1nLj5s3BBwNoE3r/kDOi'),
                                                                 ('Moderador', 'mod@foro.com', '$2a$10$WLIOnz9VEpiYHpiQsZKx2urIFjXeYbkkWgUnppik9Z.Hd/OltHpbW'),
                                                                 ('Administrador', 'admin@foro.com', '$2a$10$VUbvA5MwqUPqR8UAnwh1BeRkzHnM/nbuyKjIRZVmbKkMDUo3IUt8e');

-- Asignar perfiles: (usuario_id, perfil_id)
INSERT INTO usuario_perfil (usuario_id, perfil_id) VALUES
                                                       (1, 1), -- Usuario básico -> ROLE_USER
                                                       (2, 2), -- Moderador -> ROLE_MODERATOR
                                                       (3, 3); -- Administrador -> ROLE_ADMIN

CREATE TABLE usuario (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nombre VARCHAR(255) NOT NULL,
                         correo_electronico VARCHAR(255) NOT NULL UNIQUE,
                         contrasena VARCHAR(255) NOT NULL
);

CREATE TABLE perfil (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE usuario_perfil (
                                usuario_id BIGINT NOT NULL,
                                perfil_id  BIGINT NOT NULL,
                                PRIMARY KEY (usuario_id, perfil_id),
                                CONSTRAINT fk_usuario_perfil_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
                                CONSTRAINT fk_usuario_perfil_perfil FOREIGN KEY (perfil_id)  REFERENCES perfil(id)
);

CREATE TABLE curso (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       nombre VARCHAR(255) NOT NULL,
                       categoria VARCHAR(100)
);

CREATE TABLE topico (
                        id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                        titulo          VARCHAR(255) NOT NULL,
                        mensaje         TEXT,
                        fecha_creacion  DATETIME(6) NOT NULL,
                        status          VARCHAR(50),
                        autor_id        BIGINT NOT NULL,
                        curso_id        BIGINT,
                        CONSTRAINT fk_topico_autor FOREIGN KEY (autor_id) REFERENCES usuario(id),
                        CONSTRAINT fk_topico_curso FOREIGN KEY (curso_id) REFERENCES curso(id)
);

CREATE TABLE respuesta (
                           id             BIGINT AUTO_INCREMENT PRIMARY KEY,
                           mensaje        TEXT      NOT NULL,
                           fecha_creacion DATETIME(6) NOT NULL,
                           solucion       BOOLEAN   NOT NULL DEFAULT FALSE,
                           topico_id      BIGINT    NOT NULL,
                           autor_id       BIGINT    NOT NULL,
                           CONSTRAINT fk_respuesta_topico FOREIGN KEY (topico_id) REFERENCES topico(id),
                           CONSTRAINT fk_respuesta_autor  FOREIGN KEY (autor_id)  REFERENCES usuario(id)
);

CREATE DATABASE IF NOT EXISTS talento_academico;
USE talento_academico;

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120),
    email VARCHAR(120),
    password VARCHAR(120),
    rol VARCHAR(30),
    activo BOOLEAN
);

CREATE TABLE IF NOT EXISTS areas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120),
    descripcion VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS ofertas_laborales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150),
    descripcion VARCHAR(800),
    vacantes INT,
    area_id BIGINT,
    activa BOOLEAN,
    FOREIGN KEY (area_id) REFERENCES areas(id)
);

CREATE TABLE IF NOT EXISTS postulantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120),
    email VARCHAR(120),
    telefono VARCHAR(40),
    cv VARCHAR(255),
    estado VARCHAR(30),
    oferta_id BIGINT,
    puntaje INT,
    aprobado BOOLEAN,
    FOREIGN KEY (oferta_id) REFERENCES ofertas_laborales(id)
);

CREATE TABLE IF NOT EXISTS preguntas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    enunciado VARCHAR(500),
    opcion_correcta VARCHAR(120),
    oferta_id BIGINT,
    FOREIGN KEY (oferta_id) REFERENCES ofertas_laborales(id)
);

CREATE TABLE IF NOT EXISTS entrevistas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_entrevista VARCHAR(30),
    fecha DATE,
    hora TIME,
    lugar VARCHAR(150),
    resultado VARCHAR(120),
    observacion VARCHAR(700),
    postulante_id BIGINT,
    FOREIGN KEY (postulante_id) REFERENCES postulantes(id)
);

INSERT INTO usuarios(nombre, email, password, rol, activo)
VALUES ('Administrador Academico', 'admin@talento.edu', '123456', 'ADMIN', true);

INSERT INTO areas(nombre, descripcion)
VALUES ('Tecnologia', 'Desarrollo y soporte de sistemas'),
       ('Administracion', 'Gestion documentaria y administrativa');

INSERT INTO ofertas_laborales(titulo, descripcion, vacantes, area_id, activa)
VALUES ('Practicante de Desarrollo Java', 'Apoyo en aplicaciones web con Spring Boot.', 2, 1, true);

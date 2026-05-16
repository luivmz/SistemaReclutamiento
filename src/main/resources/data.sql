INSERT INTO usuarios (id, nombre, email, password, telefono, rol, activo) VALUES
(1, 'Administrador Academico', 'admin@talento.edu', '123456', '999000111', 'ADMIN', true),
(2, 'Postulante Demo', 'postulante@talento.edu', '123456', '999222333', 'POSTULANTE', true);

INSERT INTO areas (id, nombre, descripcion) VALUES
(1, 'Tecnologia', 'Desarrollo y soporte de sistemas'),
(2, 'Administracion', 'Gestion documentaria y administrativa'),
(3, 'Recursos Humanos', 'Procesos de seleccion y gestion del talento');

INSERT INTO ofertas_laborales (id, titulo, descripcion, vacantes, area_id, activa) VALUES
(1, 'Practicante de Desarrollo Java', 'Apoyo en mantenimiento de aplicaciones web con Spring Boot, JSP y JPA.', 2, 1, true),
(2, 'Asistente Administrativo', 'Apoyo en registro documentario, reportes y coordinacion interna.', 1, 2, true),
(3, 'Practicante de Seleccion', 'Apoyo en filtro curricular, entrevistas y seguimiento de postulantes.', 1, 3, true);

INSERT INTO postulantes (id, nombre, email, telefono, experiencia, habilidades, cv, fecha_postulacion, estado, oferta_id, usuario_id, puntaje, aprobado, observacion) VALUES
(1, 'Postulante Demo', 'postulante@talento.edu', '999222333', 'Cursos basicos de Java', 'Java, HTML, trabajo en equipo', 'cv_demo.pdf', DATE '2026-05-15', 'POSTULADO', 1, 2, 0, false, 'Postulacion registrada.'),
(2, 'Postulante Demo', 'postulante@talento.edu', '999222333', 'Apoyo administrativo academico', 'Excel, organizacion, comunicacion', 'cv_demo.pdf', DATE '2026-05-14', 'APROBADO', 2, 2, 85, true, 'Proceso aprobado.');

INSERT INTO preguntas (id, enunciado, opcion_correcta) VALUES
(1, 'Que anotacion se usa para declarar una entidad JPA?', '@Entity'),
(2, 'Que repositorio base ofrece operaciones CRUD en Spring Data JPA?', 'JpaRepository'),
(3, 'Que herramienta permite ejecutar consultas sobre H2 desde el navegador?', 'H2 Console');

INSERT INTO preguntas_ofertas (pregunta_id, oferta_id) VALUES
(1, 1),
(1, 3),
(2, 1),
(2, 3),
(3, 1),
(3, 2),
(3, 3);

INSERT INTO entrevistas (id, tipo_entrevista, fecha, hora, lugar, resultado, observacion, postulante_id) VALUES
(1, 'NORMAL', DATE '2026-05-20', TIME '09:00:00', 'Aula 201', 'Pendiente', 'Entrevista inicial programada.', 1),
(2, 'PSICOLOGICA', DATE '2026-05-21', TIME '10:30:00', 'Aula 202', 'Apto', 'Buen desempeno en evaluacion.', 2);

ALTER TABLE usuarios ALTER COLUMN id RESTART WITH 3;
ALTER TABLE areas ALTER COLUMN id RESTART WITH 4;
ALTER TABLE ofertas_laborales ALTER COLUMN id RESTART WITH 4;
ALTER TABLE postulantes ALTER COLUMN id RESTART WITH 3;
ALTER TABLE preguntas ALTER COLUMN id RESTART WITH 4;
ALTER TABLE entrevistas ALTER COLUMN id RESTART WITH 3;

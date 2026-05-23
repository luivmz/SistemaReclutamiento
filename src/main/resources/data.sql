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

INSERT INTO postulantes (id, nombre, email, telefono, experiencia, habilidades, cv, fecha_postulacion, estado, oferta_id, usuario_id, aprobado, observacion) VALUES
(1, 'Postulante Demo', 'postulante@talento.edu', '999222333', 'Cursos basicos de Java', 'Java, HTML, trabajo en equipo', 'cv_demo.pdf', DATE '2026-05-15', 'POSTULADO', 1, 2, false, 'Postulacion registrada.'),
(2, 'Postulante Demo', 'postulante@talento.edu', '999222333', 'Apoyo administrativo academico', 'Excel, organizacion, comunicacion', 'cv_demo.pdf', DATE '2026-05-14', 'APROBADO', 2, 2, true, 'Proceso aprobado por entrevista.');

INSERT INTO entrevistas (id, tipo_entrevista, fecha, hora, lugar, modalidad, resultado, observacion, estado_entrevista, postulante_id) VALUES
(1, 'NORMAL', DATE '2026-05-20', TIME '09:00:00', 'Aula 201', 'PRESENCIAL', 'PENDIENTE', 'Entrevista inicial programada.', 'PROGRAMADA', 1),
(2, 'PSICOLOGICA', DATE '2026-05-21', TIME '10:30:00', 'Aula 202', 'PRESENCIAL', 'APROBADO', 'Buen desempeno en evaluacion psicologica.', 'REALIZADA', 2);

ALTER TABLE usuarios ALTER COLUMN id RESTART WITH 3;
ALTER TABLE areas ALTER COLUMN id RESTART WITH 4;
ALTER TABLE ofertas_laborales ALTER COLUMN id RESTART WITH 4;
ALTER TABLE postulantes ALTER COLUMN id RESTART WITH 3;
ALTER TABLE entrevistas ALTER COLUMN id RESTART WITH 3;

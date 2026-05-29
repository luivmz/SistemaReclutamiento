INSERT INTO usuarios (id, nombre, email, password, telefono, rol, activo) VALUES
(1, 'Administrador Colegio', 'coordinador@colegio.edu.pe', '123456', '999000111', 'ADMIN', true),
(2, 'Mariana Torres', 'docente.matematica@demo.com', '123456', '999111001', 'POSTULANTE', true),
(3, 'Luis Ramirez', 'docente.comunicacion@demo.com', '123456', '999111002', 'POSTULANTE', true),
(4, 'Camila Herrera', 'psicologia.escolar@demo.com', '123456', '999111003', 'POSTULANTE', true),
(5, 'Rosa Delgado', 'auxiliar.educacion@demo.com', '123456', '999111004', 'POSTULANTE', true);

INSERT INTO areas (id, nombre, descripcion) VALUES
(1, 'Direccion Academica', 'Planificacion curricular, seguimiento pedagogico y gestion docente del colegio'),
(2, 'Nivel Primaria', 'Acompanamiento academico y formativo de estudiantes de primaria'),
(3, 'Nivel Secundaria', 'Gestion pedagogica, tutoria y convivencia en secundaria'),
(4, 'Psicopedagogia', 'Acompanamiento socioemocional, orientacion familiar y convivencia escolar');

INSERT INTO ofertas_laborales (id, titulo, descripcion, vacantes, area_id, activa) VALUES
(1, 'Docente de Matematica', 'Planificacion de sesiones, evaluacion formativa, tutoria y acompanamiento academico de estudiantes de secundaria.', 2, 3, true),
(2, 'Docente de Comunicacion', 'Desarrollo de comprension lectora, produccion de textos, trabajo con familias y seguimiento academico de estudiantes.', 1, 2, true),
(3, 'Auxiliar de Educacion Inicial', 'Apoyo en rutinas escolares, cuidado de estudiantes pequenos y comunicacion permanente con familias.', 1, 2, true),
(4, 'Psicologo Escolar', 'Acompanamiento socioemocional, orientacion a padres, tutorias y apoyo a la convivencia escolar.', 2, 4, true),
(5, 'Asistente Administrativo Escolar', 'Gestion de matriculas, atencion a familias y organizacion documentaria del colegio.', 2, 1, true),
(6, 'Coordinador Academico', 'Seguimiento de planificacion curricular, acompanamiento pedagogico a docentes y desarrollo academico de estudiantes.', 1, 1, true);

INSERT INTO postulantes (id, nombre, email, telefono, experiencia, habilidades, cv, fecha_postulacion, estado, oferta_id, usuario_id, aprobado, observacion) VALUES
(1, 'Mariana Torres', 'docente.matematica@demo.com', '999111001', 'Tres anos dictando matematica en secundaria y preparando sesiones por competencias', 'Manejo de aula, evaluacion formativa, planificacion curricular, uso de recursos didacticos', 'cv_mariana_torres_matematica.pdf', DATE '2026-05-10', 'POSTULADO', 1, 2, false, 'Postulacion registrada para revision de experiencia docente.'),
(2, 'Luis Ramirez', 'docente.comunicacion@demo.com', '999111002', 'Experiencia en comprension lectora y produccion de textos con estudiantes de primaria', 'Comunicacion con estudiantes, trabajo con familias, rubricas, acompanamiento lector', 'cv_luis_ramirez_comunicacion.pdf', DATE '2026-05-11', 'POSTULADO', 2, 3, false, 'Pendiente de revision por coordinacion de primaria.'),
(3, 'Rosa Delgado', 'auxiliar.educacion@demo.com', '999111004', 'Apoyo en aula inicial, rutinas de cuidado y acompanamiento de estudiantes pequenos', 'Empatia, orden en aula, comunicacion con padres, apoyo a docentes', 'cv_rosa_delgado_auxiliar.pdf', DATE '2026-05-12', 'POSTULADO', 3, 5, false, 'Perfil en revision para apoyo en educacion inicial.'),
(4, 'Camila Herrera', 'psicologia.escolar@demo.com', '999111003', 'Acompanamiento psicopedagogico y orientacion a estudiantes con necesidades de apoyo', 'Escucha activa, orientacion familiar, convivencia escolar, informes psicopedagogicos', 'cv_camila_herrera_psicologia.pdf', DATE '2026-05-13', 'EN_ENTREVISTA', 4, 4, false, 'Entrevista psicopedagogica programada.'),
(5, 'Mariana Torres', 'docente.matematica@demo.com', '999111001', 'Experiencia en coordinacion de proyectos academicos y acompanamiento docente', 'Planificacion curricular, liderazgo pedagogico, evaluacion por competencias, trabajo con familias', 'cv_mariana_torres_coordinacion.pdf', DATE '2026-05-14', 'EN_ENTREVISTA', 6, 2, false, 'Evaluacion con coordinacion academica pendiente.'),
(6, 'Luis Ramirez', 'docente.comunicacion@demo.com', '999111002', 'Trabajo administrativo escolar en secretaria academica y atencion a familias', 'Organizacion documentaria, trato con padres, registro de matriculas, Excel', 'cv_luis_ramirez_administrativo.pdf', DATE '2026-05-15', 'APROBADO', 5, 3, true, 'Postulante aprobado para apoyo administrativo escolar.'),
(7, 'Camila Herrera', 'psicologia.escolar@demo.com', '999111003', 'Experiencia en talleres de convivencia y tutoria para estudiantes de secundaria', 'Tutoria escolar, mediacion, comunicacion asertiva, trabajo con familias', 'cv_camila_herrera_tutoria.pdf', DATE '2026-05-16', 'RECHAZADO', 4, 4, false, 'Requiere fortalecer estrategias de intervencion en convivencia escolar.'),
(8, 'Rosa Delgado', 'auxiliar.educacion@demo.com', '999111004', 'Apoyo docente en primaria y seguimiento de tareas con estudiantes', 'Manejo de grupo, paciencia, comunicacion con familias, apoyo pedagogico', 'cv_rosa_delgado_primaria.pdf', DATE '2026-05-17', 'APROBADO', 2, 5, true, 'Aprobada por buen desempeno en entrevista con coordinacion primaria.');

INSERT INTO entrevistas (id, tipo_entrevista, fecha, hora, lugar, modalidad, observacion, estado_entrevista, postulante_id) VALUES
(1, 'NORMAL', DATE '2026-05-20', TIME '09:00:00', 'Sala de Direccion', 'PRESENCIAL', 'Entrevista docente para revisar manejo de aula y planificacion de sesiones.', 'PROGRAMADA', 4),
(2, 'PSICOLOGICA', DATE '2026-05-20', TIME '10:30:00', 'Oficina de Psicopedagogia', 'PRESENCIAL', 'Evaluacion psicologica pendiente para validar habilidades socioemocionales.', 'PROGRAMADA', 5),
(3, 'NORMAL', DATE '2026-05-21', TIME '11:00:00', 'Sala de Coordinacion Academica', 'PRESENCIAL', 'Buen manejo de atencion a padres y organizacion documentaria escolar.', 'REALIZADA', 6),
(4, 'PSICOLOGICA', DATE '2026-05-21', TIME '15:00:00', 'Aula de Entrevistas', 'PRESENCIAL', 'Se revisaron estrategias de convivencia y acompanamiento a estudiantes.', 'REALIZADA', 7),
(5, 'NORMAL', DATE '2026-05-22', TIME '08:30:00', 'Entrevista Virtual', 'VIRTUAL', 'Entrevista cancelada por reprogramacion de direccion academica.', 'CANCELADA', 8);

INSERT INTO resultados_entrevista (id, entrevista_id, resultado, puntaje, observacion, recomendacion, fecha_registro, registrado_por) VALUES
(1, 3, 'APROBADO', 92, 'Demuestra buen trato con padres de familia y orden en procesos escolares.', 'Continuar con incorporacion al area administrativa escolar.', TIMESTAMP '2026-05-21 12:00:00', 'Administrador Colegio'),
(2, 4, 'DESAPROBADO', 55, 'Requiere fortalecer estrategias de convivencia escolar y manejo de casos tutoriales.', 'Cerrar postulacion y sugerir capacitacion en intervencion escolar.', TIMESTAMP '2026-05-21 16:00:00', 'Administrador Colegio'),
(3, 2, 'PENDIENTE', NULL, 'Pendiente de evaluacion psicologica y entrevista con coordinacion academica.', 'Esperar informe de psicopedagogia antes de emitir decision final.', TIMESTAMP '2026-05-20 11:00:00', 'Administrador Colegio');

INSERT INTO historial_postulantes (id, postulante_id, estado_anterior, estado_nuevo, fecha_cambio, observacion, registrado_por) VALUES
(1, 4, 'POSTULADO', 'EN_ENTREVISTA', TIMESTAMP '2026-05-18 09:00:00', 'Postulante derivada a entrevista psicopedagogica.', 'Administrador Colegio'),
(2, 5, 'POSTULADO', 'EN_ENTREVISTA', TIMESTAMP '2026-05-18 10:00:00', 'Postulante citada a evaluacion psicologica y coordinacion academica.', 'Administrador Colegio'),
(3, 6, 'POSTULADO', 'EN_ENTREVISTA', TIMESTAMP '2026-05-19 09:30:00', 'Avanza a entrevista con direccion academica por perfil administrativo escolar.', 'Administrador Colegio'),
(4, 6, 'EN_ENTREVISTA', 'APROBADO', TIMESTAMP '2026-05-21 12:10:00', 'Aprobado por coordinacion academica para apoyo administrativo escolar.', 'Administrador Colegio'),
(5, 7, 'POSTULADO', 'EN_ENTREVISTA', TIMESTAMP '2026-05-19 10:30:00', 'Avanza a entrevista psicologica por perfil de psicopedagogia.', 'Administrador Colegio'),
(6, 7, 'EN_ENTREVISTA', 'RECHAZADO', TIMESTAMP '2026-05-21 16:10:00', 'Rechazado por no cumplir el perfil esperado para convivencia escolar.', 'Administrador Colegio'),
(7, 8, 'POSTULADO', 'EN_ENTREVISTA', TIMESTAMP '2026-05-20 08:30:00', 'Derivada a entrevista con coordinacion de primaria.', 'Administrador Colegio'),
(8, 8, 'EN_ENTREVISTA', 'APROBADO', TIMESTAMP '2026-05-22 09:00:00', 'Aprobada por buen manejo de grupo y comunicacion con familias.', 'Administrador Colegio');

ALTER TABLE usuarios ALTER COLUMN id RESTART WITH 6;
ALTER TABLE areas ALTER COLUMN id RESTART WITH 5;
ALTER TABLE ofertas_laborales ALTER COLUMN id RESTART WITH 7;
ALTER TABLE postulantes ALTER COLUMN id RESTART WITH 9;
ALTER TABLE entrevistas ALTER COLUMN id RESTART WITH 6;
ALTER TABLE resultados_entrevista ALTER COLUMN id RESTART WITH 4;
ALTER TABLE historial_postulantes ALTER COLUMN id RESTART WITH 9;

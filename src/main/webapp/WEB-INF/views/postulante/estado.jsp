<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Estado</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Estado de postulacion</h2>
    <section class="card">
        <h3>${postulacion.ofertaTitulo}</h3>
        <p><strong>Area:</strong> ${postulacion.areaNombre}</p>
        <p><strong>Estado actual:</strong> ${postulacion.estado}</p>
        <p><strong>Puntaje:</strong> ${postulacion.puntaje}</p>
        <p><strong>Resultado:</strong> ${postulacion.aprobado}</p>
        <p><strong>Observacion:</strong> ${postulacion.observacion}</p>
    </section>
    <a class="btn" href="/postulante/mis-postulaciones">Volver</a>
</main>
</body>
</html>

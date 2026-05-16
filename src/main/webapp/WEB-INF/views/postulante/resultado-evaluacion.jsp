<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Resultado</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <section class="card">
        <h2>Resultado de evaluacion</h2>
        <p><strong>Oferta:</strong> ${postulacion.ofertaTitulo}</p>
        <p><strong>Puntaje:</strong> ${puntaje}</p>
        <p><strong>Estado:</strong> ${postulacion.estado}</p>
        <p>${postulacion.observacion}</p>
        <a class="btn" href="/postulante/mis-postulaciones">Ver mis postulaciones</a>
    </section>
</main>
</body>
</html>

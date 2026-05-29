<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Detalle de oferta docente</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <section class="card">
        <h2>${oferta.titulo}</h2>
        <p>${oferta.descripcion}</p>
        <p><strong>Area academica:</strong> ${oferta.areaNombre}</p>
        <p><strong>Vacantes:</strong> ${oferta.vacantes}</p>
    </section>
    <section class="card">
        <h2>Postulacion</h2>
        <p>Para postular debes iniciar sesion con una cuenta de postulante docente.</p>
        <a class="btn" href="/ofertas/${oferta.id}/postular">Postular a esta oferta</a>
    </section>
</main>
<%@ include file="../includes/footer.jsp" %>
</body>
</html>

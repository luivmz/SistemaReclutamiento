<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Detalle oferta</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <section class="card">
        <h2>${oferta.titulo}</h2>
        <p>${oferta.descripcion}</p>
        <p><strong>Area:</strong> ${oferta.areaNombre}</p>
        <p><strong>Vacantes:</strong> ${oferta.vacantes}</p>
    </section>
    <h2>Formulario de postulacion</h2>
    <form method="post" action="/postular" class="card">
        <input type="hidden" name="ofertaId" value="${oferta.id}">
        <label>Nombre</label><input name="nombre" required>
        <label>Email</label><input type="email" name="email" required>
        <label>Telefono</label><input name="telefono">
        <label>CV</label><input name="cv" placeholder="Nombre del archivo o enlace">
        <button type="submit">Enviar postulacion</button>
    </form>
</main>
</body>
</html>

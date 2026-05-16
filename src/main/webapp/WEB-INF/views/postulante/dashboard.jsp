<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Dashboard postulante</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Panel del postulante</h2>
    <p>Bienvenido, ${nombre}</p>
    <div class="grid">
        <section class="card"><h3>Postulaciones</h3><p>${total}</p></section>
        <section class="card"><h3>En evaluacion</h3><p>${evaluacion}</p></section>
        <section class="card"><h3>Aprobadas</h3><p>${aprobadas}</p></section>
        <section class="card"><h3>Rechazadas</h3><p>${rechazadas}</p></section>
    </div>
    <div class="grid">
        <a class="btn" href="/ofertas">Ver ofertas disponibles</a>
        <a class="btn" href="/postulante/mis-postulaciones">Mis postulaciones</a>
        <a class="btn" href="/postulante/historial">Estado de mis postulaciones</a>
        <a class="btn" href="/postulante/mis-postulaciones">Mis evaluaciones</a>
        <a class="btn secundario" href="/logout">Cerrar sesion</a>
    </div>
</main>
</body>
</html>

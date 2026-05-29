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
        <section class="card"><h3>Mis Postulaciones</h3><p>${total}</p></section>
        <section class="card"><h3>Entrevistas Programadas</h3><p>${enEntrevista}</p></section>
        <section class="card"><h3>Aprobadas</h3><p>${aprobadas}</p></section>
        <section class="card"><h3>Rechazadas</h3><p>${rechazadas}</p></section>
    </div>
    <div class="grid">
        <a class="btn" href="/ofertas">Ver ofertas docentes</a>
        <a class="btn" href="/postulante/mis-postulaciones">Mis Postulaciones</a>
        <a class="btn" href="/postulante/historial">Estado del Proceso</a>
        <a class="btn secundario" href="/logout">Cerrar sesion</a>
    </div>
</main>
<%@ include file="../includes/footer.jsp" %>
</body>
</html>

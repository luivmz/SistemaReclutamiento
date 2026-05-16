<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Dashboard</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<header>
    <h1>Administracion - Talento Academico</h1>
    <nav><a href="/admin/usuarios">Usuarios</a><a href="/admin/areas">Areas</a><a href="/admin/ofertas">Ofertas</a><a href="/admin/postulantes">Postulantes</a><a href="/admin/entrevistas">Entrevistas</a><a href="/admin/preguntas">Preguntas</a><a href="/logout">Salir</a></nav>
</header>
<main>
    <h2>Resumen</h2>
    <div class="grid">
        <section class="card"><h3>Usuarios</h3><p>${usuarios}</p></section>
        <section class="card"><h3>Ofertas</h3><p>${ofertas}</p></section>
        <section class="card"><h3>Activos</h3><p>${activos}</p></section>
        <section class="card"><h3>Historial</h3><p>${historial}</p></section>
    </div>
</main>
</body>
</html>

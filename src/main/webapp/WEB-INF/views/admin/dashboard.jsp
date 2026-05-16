<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Dashboard</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<div class="admin-layout">
    <%@ include file="../includes/admin-sidebar.jsp" %>
<main class="admin-content">
    <h2 class="page-title">Resumen</h2>
    <div class="grid">
        <section class="card"><h3>Usuarios</h3><p>${usuarios}</p></section>
        <section class="card"><h3>Ofertas</h3><p>${ofertas}</p></section>
        <section class="card"><h3>Activos</h3><p>${activos}</p></section>
        <section class="card"><h3>Historial</h3><p>${historial}</p></section>
    </div>
    <h2>Opciones</h2>
    <div class="grid">
        <a class="btn" href="/admin/usuarios">Gestionar usuarios</a>
        <a class="btn" href="/admin/areas">Gestionar areas</a>
        <a class="btn" href="/admin/ofertas">Gestionar ofertas</a>
        <a class="btn" href="/admin/preguntas">Gestionar preguntas</a>
        <a class="btn" href="/admin/postulantes">Ver postulantes activos</a>
        <a class="btn" href="/admin/postulantes">Ver historial de postulantes</a>
        <a class="btn" href="/admin/entrevistas">Ver entrevistas</a>
        <a class="btn secundario" href="/admin/dashboard">Ver metricas</a>
    </div>
</main>
</div>
</body>
</html>

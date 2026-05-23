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
        <section class="card"><h3>Entrevistas</h3><p>${entrevistas}</p></section>
        <section class="card"><h3>Programadas</h3><p>${entrevistasProgramadas}</p></section>
        <section class="card"><h3>Aprobados</h3><p>${aprobados}</p></section>
        <section class="card"><h3>Rechazados</h3><p>${rechazados}</p></section>
        <section class="card"><h3>Historial</h3><p>${historial}</p></section>
    </div>
    <h2>Opciones</h2>
    <div class="grid">
        <a class="btn" href="/admin/usuarios">Gestionar usuarios</a>
        <a class="btn" href="/admin/areas">Gestionar areas</a>
        <a class="btn" href="/admin/ofertas">Gestionar ofertas</a>
        <a class="btn" href="/admin/postulantes">Ver postulantes activos</a>
        <a class="btn" href="/admin/entrevistas">Gestionar entrevistas</a>
        <a class="btn" href="/admin/resultados-entrevista">Resultados de entrevistas</a>
        <a class="btn" href="/admin/historial">Ver historial</a>
    </div>
</main>
</div>
</body>
</html>

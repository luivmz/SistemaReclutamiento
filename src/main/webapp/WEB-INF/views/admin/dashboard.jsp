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
</main>
</div>
</body>
</html>

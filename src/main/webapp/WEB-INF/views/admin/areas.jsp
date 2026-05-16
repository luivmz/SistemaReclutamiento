<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Areas</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<div class="admin-layout">
    <%@ include file="../includes/admin-sidebar.jsp" %>
<main class="admin-content">
    <h2 class="page-title">Areas</h2>
    <form method="post" action="/admin/areas" class="card">
        <label>Nombre</label><input name="nombre" required>
        <label>Descripcion</label><textarea name="descripcion"></textarea>
        <button type="submit">Guardar</button>
    </form>
    <table>
        <tr><th>Nombre</th><th>Descripcion</th></tr>
        <c:forEach var="a" items="${areas}">
            <tr><td>${a.nombre}</td><td>${a.descripcion}</td></tr>
        </c:forEach>
    </table>
</main>
</div>
</body>
</html>

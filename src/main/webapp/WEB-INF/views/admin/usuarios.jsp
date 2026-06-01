<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Usuarios</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<div class="admin-layout">
    <%@ include file="../includes/admin-sidebar.jsp" %>
<main class="admin-content">
    <h2 class="page-title">Usuarios</h2>
    <form method="post" action="/admin/usuarios" class="card">
        <input type="hidden" name="id" value="${usuario.id}">
        <c:if test="${not empty error}"><p class="error">${error}</p></c:if>
        <label>Nombre</label><input name="nombre" value="${usuario.nombre}" minlength="2" maxlength="120" required>
        <label>Email</label><input type="email" name="email" value="${usuario.email}" maxlength="120" required>
        <label>Telefono</label><input name="telefono" value="${usuario.telefono}" maxlength="40">
        <label>Password</label><input name="password" value="${usuario.password}" required>
        <label>Rol</label>
        <select name="rol">
            <option value="ADMIN" <c:if test="${usuario.rol == 'ADMIN'}">selected</c:if>>ADMIN</option>
            <option value="POSTULANTE" <c:if test="${usuario.rol == 'POSTULANTE'}">selected</c:if>>POSTULANTE</option>
        </select>
        <label>Activo</label>
        <select name="activo">
            <option value="true" <c:if test="${usuario.activo != false}">selected</c:if>>Si</option>
            <option value="false" <c:if test="${usuario.activo == false}">selected</c:if>>No</option>
        </select>
        <button type="submit">Guardar</button>
        <a class="btn secundario" href="/admin/usuarios">Nuevo</a>
    </form>
    <table>
        <tr><th>Nombre</th><th>Email</th><th>Telefono</th><th>Rol</th><th>Activo</th><th>Acciones</th></tr>
        <c:forEach var="u" items="${usuarios}">
            <tr>
                <td>${u.nombre}</td><td>${u.email}</td><td>${u.telefono}</td><td>${u.rol}</td><td>${u.activo}</td>
                <td>
                    <a class="btn" href="/admin/usuarios/editar/${u.id}">Editar</a>
                    <a class="btn secundario" href="/admin/usuarios/desactivar/${u.id}">Desactivar</a>
                    <a class="btn" href="/admin/usuarios/activar/${u.id}">Activar</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</main>
</div>
</body>
</html>

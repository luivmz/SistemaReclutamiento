<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Areas Academicas</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<div class="admin-layout">
    <%@ include file="../includes/admin-sidebar.jsp" %>
<main class="admin-content">
    <h2 class="page-title">Areas Academicas</h2>
    <form method="post" action="/admin/areas" class="card">
        <input type="hidden" name="id" value="${area.id}">
        <c:if test="${not empty error}"><p class="error">${error}</p></c:if>
        <label>Nombre</label><input name="nombre" value="${area.nombre}" minlength="2" maxlength="120" required>
        <label>Descripcion</label><textarea name="descripcion" maxlength="255">${area.descripcion}</textarea>
        <label>Activa</label>
        <select name="activa">
            <option value="true" <c:if test="${area.activa != false}">selected</c:if>>Si</option>
            <option value="false" <c:if test="${area.activa == false}">selected</c:if>>No</option>
        </select>
        <button type="submit">Guardar</button>
        <a class="btn secundario" href="/admin/areas">Nuevo</a>
    </form>
    <table>
        <tr><th>Nombre</th><th>Descripcion</th><th>Activa</th><th>Acciones</th></tr>
        <c:forEach var="a" items="${areas}">
            <tr>
                <td>${a.nombre}</td>
                <td>${a.descripcion}</td>
                <td>${a.activa}</td>
                <td>
                    <a class="btn" href="/admin/areas/editar/${a.id}">Editar</a>
                    <a class="btn secundario" href="/admin/areas/eliminar/${a.id}">Desactivar</a>
                    <a class="btn" href="/admin/areas/activar/${a.id}">Activar</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</main>
</div>
</body>
</html>

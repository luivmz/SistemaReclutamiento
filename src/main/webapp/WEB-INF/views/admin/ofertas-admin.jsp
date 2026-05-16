<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Ofertas admin</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<div class="admin-layout">
    <%@ include file="../includes/admin-sidebar.jsp" %>
<main class="admin-content">
    <h2 class="page-title">Ofertas</h2>
    <form method="post" action="/admin/ofertas" class="card">
        <input type="hidden" name="id" value="${oferta.id}">
        <label>Titulo</label><input name="titulo" value="${oferta.titulo}" required>
        <label>Descripcion</label><textarea name="descripcion">${oferta.descripcion}</textarea>
        <label>Vacantes</label><input type="number" name="vacantes" value="${oferta.vacantes == null ? 1 : oferta.vacantes}">
        <label>Area</label>
        <select name="areaId">
            <c:forEach var="area" items="${areas}">
                <option value="${area.id}">${area.nombre}</option>
            </c:forEach>
        </select>
        <label>Activa</label><select name="activa"><option value="true">Si</option><option value="false">No</option></select>
        <button type="submit">Guardar</button>
        <a class="btn secundario" href="/admin/ofertas">Nuevo</a>
    </form>
    <table>
        <tr><th>Titulo</th><th>Area</th><th>Vacantes</th><th>Activa</th><th>Acciones</th></tr>
        <c:forEach var="o" items="${ofertas}">
            <tr>
                <td>${o.titulo}</td><td>${o.areaNombre}</td><td>${o.vacantes}</td><td>${o.activa}</td>
                <td>
                    <a class="btn" href="/admin/ofertas/editar/${o.id}">Editar</a>
                    <a class="btn secundario" href="/admin/ofertas/eliminar/${o.id}">Desactivar</a>
                    <a class="btn" href="/admin/ofertas/activar/${o.id}">Activar</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</main>
</div>
</body>
</html>

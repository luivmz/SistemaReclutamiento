<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Ofertas Docentes</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<div class="admin-layout">
    <%@ include file="../includes/admin-sidebar.jsp" %>
<main class="admin-content">
    <h2 class="page-title">Ofertas Docentes</h2>
    <form method="post" action="/admin/ofertas" class="card">
        <input type="hidden" name="id" value="${oferta.id}">
        <c:if test="${not empty error}"><p class="error">${error}</p></c:if>
        <label>Titulo</label><input name="titulo" value="${oferta.titulo}" minlength="2" maxlength="120" required>
        <label>Descripcion</label><textarea name="descripcion" maxlength="800" required>${oferta.descripcion}</textarea>
        <label>Vacantes</label><input type="number" name="vacantes" min="0" value="${oferta.vacantes == null ? 1 : oferta.vacantes}" required>
        <label>Area academica</label>
        <select name="areaId" required>
            <c:forEach var="area" items="${areas}">
                <option value="${area.id}" <c:if test="${area.id == oferta.areaId}">selected</c:if>>${area.nombre}</option>
            </c:forEach>
        </select>
        <label>Activa</label>
        <select name="activa">
            <option value="true" <c:if test="${oferta.activa != false}">selected</c:if>>Si</option>
            <option value="false" <c:if test="${oferta.activa == false}">selected</c:if>>No</option>
        </select>
        <button type="submit">Guardar</button>
        <a class="btn secundario" href="/admin/ofertas">Nuevo</a>
    </form>
    <table>
        <tr><th>Titulo</th><th>Area academica</th><th>Vacantes</th><th>Activa</th><th>Acciones</th></tr>
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

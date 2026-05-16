<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Preguntas</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<div class="admin-layout">
    <%@ include file="../includes/admin-sidebar.jsp" %>
<main class="admin-content">
    <h2 class="page-title">Preguntas</h2>
    <form method="post" action="/admin/preguntas" class="card">
        <input type="hidden" name="id" value="${pregunta.id}">
        <label>Ofertas laborales</label>
        <select name="ofertaIds" multiple size="4">
            <c:forEach var="o" items="${ofertas}">
                <option value="${o.id}">${o.titulo}</option>
            </c:forEach>
        </select>
        <label>Enunciado</label><textarea name="enunciado">${pregunta.enunciado}</textarea>
        <label>Opcion correcta</label><input name="opcionCorrecta" value="${pregunta.opcionCorrecta}">
        <button type="submit">Guardar</button>
        <a class="btn secundario" href="/admin/preguntas">Nuevo</a>
    </form>
    <table>
        <tr><th>Ofertas</th><th>Pregunta</th><th>Correcta</th><th>Acciones</th></tr>
        <c:forEach var="p" items="${preguntas}">
            <tr>
                <td>${p.ofertasTitulos}</td><td>${p.enunciado}</td><td>${p.opcionCorrecta}</td>
                <td>
                    <a class="btn" href="/admin/preguntas/editar/${p.id}">Editar</a>
                    <a class="btn secundario" href="/admin/preguntas/eliminar/${p.id}">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</main>
</div>
</body>
</html>

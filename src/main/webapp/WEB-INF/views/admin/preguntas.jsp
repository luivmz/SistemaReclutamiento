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
        <label>Oferta</label>
        <select name="ofertaId">
            <c:forEach var="o" items="${ofertas}">
                <option value="${o.id}">${o.titulo}</option>
            </c:forEach>
        </select>
        <label>Enunciado</label><textarea name="enunciado"></textarea>
        <label>Opcion correcta</label><input name="opcionCorrecta">
        <button type="submit">Guardar</button>
    </form>
    <table>
        <tr><th>Oferta</th><th>Pregunta</th><th>Correcta</th></tr>
        <c:forEach var="p" items="${preguntas}">
            <tr><td>${p.ofertaTitulo}</td><td>${p.enunciado}</td><td>${p.opcionCorrecta}</td></tr>
        </c:forEach>
    </table>
</main>
</div>
</body>
</html>

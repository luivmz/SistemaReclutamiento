<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Registrar resultado de entrevista</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<div class="admin-layout">
    <%@ include file="../includes/admin-sidebar.jsp" %>
<main class="admin-content">
    <h2 class="page-title">
        <c:choose>
            <c:when test="${resultado.id == null}">Registrar resultado</c:when>
            <c:otherwise>Editar resultado</c:otherwise>
        </c:choose>
    </h2>
    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>
    <form method="post" action="/admin/resultados-entrevista/guardar" class="card">
        <input type="hidden" name="id" value="${resultado.id}">
        <input type="hidden" name="entrevistaId" value="${resultado.entrevista.id}">

        <div class="card">
            <p><strong>Entrevista:</strong> ${resultado.entrevista.tipoEntrevista} - ${resultado.entrevista.fecha} ${resultado.entrevista.hora}</p>
            <p><strong>Postulante:</strong> ${resultado.entrevista.postulante.nombre}</p>
            <p><strong>Oferta:</strong> ${resultado.entrevista.postulante.oferta.titulo}</p>
        </div>

        <label>Resultado</label>
        <select name="resultado" required>
            <option value="PENDIENTE" <c:if test="${resultado.resultado == 'PENDIENTE'}">selected</c:if>>PENDIENTE</option>
            <option value="APROBADO" <c:if test="${resultado.resultado == 'APROBADO'}">selected</c:if>>APROBADO</option>
            <option value="DESAPROBADO" <c:if test="${resultado.resultado == 'DESAPROBADO'}">selected</c:if>>DESAPROBADO</option>
        </select>

        <label>Puntaje (0-100, opcional)</label>
        <input type="number" name="puntaje" min="0" max="100" value="${resultado.puntaje}">
        <small>El puntaje debe estar entre 0 y 100. Puede quedar vacio si el resultado esta pendiente.</small>

        <label>Observacion</label>
        <textarea name="observacion" rows="3" maxlength="700">${resultado.observacion}</textarea>

        <label>Recomendacion</label>
        <textarea name="recomendacion" rows="3" maxlength="700">${resultado.recomendacion}</textarea>

        <button type="submit">Guardar resultado</button>
        <a class="btn secundario" href="/admin/entrevistas">Volver a entrevistas</a>
    </form>
</main>
</div>
</body>
</html>

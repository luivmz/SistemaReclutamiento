<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Entrevistas</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<div class="admin-layout">
    <%@ include file="../includes/admin-sidebar.jsp" %>
<main class="admin-content">
    <h2 class="page-title">Entrevistas</h2>
    <form method="post" action="/admin/entrevistas" class="card">
        <input type="hidden" name="id" value="${entrevista.id}">
        <c:if test="${not empty error}"><p class="error">${error}</p></c:if>

        <label>Tipo</label>
        <select name="tipoEntrevista">
            <option value="NORMAL" <c:if test="${entrevista.tipoEntrevista == 'NORMAL'}">selected</c:if>>NORMAL</option>
            <option value="PSICOLOGICA" <c:if test="${entrevista.tipoEntrevista == 'PSICOLOGICA'}">selected</c:if>>PSICOLOGICA</option>
        </select>

        <label>Postulante</label>
        <select name="postulanteId" required>
            <c:forEach var="p" items="${postulantes}">
                <option value="${p.id}" <c:if test="${p.id == entrevista.postulanteId}">selected</c:if>>${p.nombre} - ${p.ofertaTitulo}</option>
            </c:forEach>
        </select>

        <label>Fecha</label><input type="date" name="fecha" value="${entrevista.fecha}" required>
        <label>Hora</label><input type="time" name="hora" value="${entrevista.hora}" required>
        <label>Lugar</label><input name="lugar" value="${entrevista.lugar}" maxlength="150" required>

        <label>Modalidad</label>
        <select name="modalidad">
            <option value="PRESENCIAL" <c:if test="${entrevista.modalidad == 'PRESENCIAL'}">selected</c:if>>PRESENCIAL</option>
            <option value="VIRTUAL" <c:if test="${entrevista.modalidad == 'VIRTUAL'}">selected</c:if>>VIRTUAL</option>
        </select>

        <label>Estado de la entrevista</label>
        <select name="estadoEntrevista">
            <option value="PROGRAMADA" <c:if test="${entrevista.estadoEntrevista == 'PROGRAMADA'}">selected</c:if>>PROGRAMADA</option>
            <option value="REALIZADA" <c:if test="${entrevista.estadoEntrevista == 'REALIZADA'}">selected</c:if>>REALIZADA</option>
            <option value="CANCELADA" <c:if test="${entrevista.estadoEntrevista == 'CANCELADA'}">selected</c:if>>CANCELADA</option>
        </select>

        <label>Observacion</label><textarea name="observacion" maxlength="700">${entrevista.observacion}</textarea>
        <button type="submit">Guardar</button>
        <a class="btn secundario" href="/admin/entrevistas">Nuevo</a>
    </form>

    <table>
        <tr>
            <th>Tipo</th>
            <th>Postulante</th>
            <th>Fecha</th>
            <th>Hora</th>
            <th>Modalidad</th>
            <th>Estado</th>
            <th>Resultado</th>
            <th>Acciones</th>
        </tr>
        <c:forEach var="e" items="${entrevistas}">
            <tr>
                <td>${e.tipoEntrevista}</td>
                <td>${e.postulanteNombre}</td>
                <td>${e.fecha}</td>
                <td>${e.hora}</td>
                <td>${e.modalidad}</td>
                <td>${e.estadoEntrevista}</td>
                <td>${e.resultadoEntrevistaValor}</td>
                <td>
                    <a class="btn" href="/admin/entrevistas/editar/${e.id}">Editar</a>
                    <c:choose>
                        <c:when test="${e.resultadoEntrevistaId == null}">
                            <a class="btn" href="/admin/resultados-entrevista/nuevo/${e.id}">Registrar resultado</a>
                        </c:when>
                        <c:otherwise>
                            <a class="btn" href="/admin/resultados-entrevista/editar/${e.resultadoEntrevistaId}">Editar resultado</a>
                        </c:otherwise>
                    </c:choose>
                    <a class="btn secundario" href="/admin/entrevistas/eliminar/${e.id}">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</main>
</div>
</body>
</html>

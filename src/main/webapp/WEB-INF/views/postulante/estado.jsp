<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Estado</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Estado del Proceso</h2>
    <c:if test="${not empty error}"><p class="error">${error}</p></c:if>
    <section class="card">
        <h3>${postulacion.ofertaTitulo}</h3>
        <p><strong>Area academica:</strong> ${postulacion.areaNombre}</p>
        <p><strong>Estado actual:</strong> ${postulacion.estado}</p>
        <p><strong>Resultado:</strong> ${postulacion.aprobado}</p>
        <p><strong>Observacion:</strong> ${postulacion.observacion}</p>
        <c:if test="${postulacion.estado == 'POSTULADO' || postulacion.estado == 'EN_ENTREVISTA'}">
            <form method="post" action="/postulante/postulaciones/${postulacion.id}/cancelar" onsubmit="return confirm('Deseas cancelar esta postulacion?');">
                <button type="submit" class="btn secundario">Cancelar postulacion</button>
            </form>
        </c:if>
    </section>

    <h3>Entrevistas Programadas</h3>
    <c:choose>
        <c:when test="${empty entrevistas}">
            <p>Aun no tienes entrevistas programadas.</p>
        </c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th>Tipo</th>
                    <th>Fecha</th>
                    <th>Hora</th>
                    <th>Lugar</th>
                    <th>Modalidad</th>
                    <th>Estado</th>
                    <th>Resultado</th>
                </tr>
                <c:forEach var="e" items="${entrevistas}">
                    <tr>
                        <td>${e.tipoEntrevista}</td>
                        <td>${e.fecha}</td>
                        <td>${e.hora}</td>
                        <td>${e.lugar}</td>
                        <td>${e.modalidad}</td>
                        <td>${e.estadoEntrevista}</td>
                        <td>${e.resultadoEntrevistaValor}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
    <a class="btn" href="/postulante/mis-postulaciones">Volver</a>
</main>
<%@ include file="../includes/footer.jsp" %>
</body>
</html>

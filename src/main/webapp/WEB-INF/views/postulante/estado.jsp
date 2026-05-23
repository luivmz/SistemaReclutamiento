<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Estado</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Estado de postulacion</h2>
    <section class="card">
        <h3>${postulacion.ofertaTitulo}</h3>
        <p><strong>Area:</strong> ${postulacion.areaNombre}</p>
        <p><strong>Estado actual:</strong> ${postulacion.estado}</p>
        <p><strong>Resultado:</strong> ${postulacion.aprobado}</p>
        <p><strong>Observacion:</strong> ${postulacion.observacion}</p>
    </section>

    <h3>Entrevistas asignadas</h3>
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
</body>
</html>

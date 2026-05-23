<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Historial</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<div class="admin-layout">
    <%@ include file="../includes/admin-sidebar.jsp" %>
<main class="admin-content">
    <h2 class="page-title">Historial de postulantes</h2>
    <table>
        <tr><th>Nombre</th><th>Oferta</th><th>Area</th><th>Estado final</th><th>Aprobado</th><th>Observacion</th></tr>
        <c:forEach var="p" items="${historial}">
            <tr>
                <td>${p.nombre}</td>
                <td>${p.ofertaTitulo}</td>
                <td>${p.areaNombre}</td>
                <td>${p.estado}</td>
                <td>${p.aprobado}</td>
                <td>${p.observacion}</td>
            </tr>
        </c:forEach>
    </table>
</main>
</div>
</body>
</html>

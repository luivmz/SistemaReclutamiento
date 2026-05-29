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
    <h2 class="page-title">Historial de movimientos</h2>
    <table>
        <tr>
            <th>Postulante</th>
            <th>Estado Anterior</th>
            <th>Estado Nuevo</th>
            <th>Fecha</th>
            <th>Observacion</th>
            <th>Registrado Por</th>
        </tr>
        <c:forEach var="h" items="${historial}">
            <tr>
                <td>${h.postulante.nombre}</td>
                <td>${h.estadoAnterior}</td>
                <td>${h.estadoNuevo}</td>
                <td>${h.fechaCambio}</td>
                <td>${h.observacion}</td>
                <td>${h.registradoPor}</td>
            </tr>
        </c:forEach>
    </table>
</main>
</div>
</body>
</html>

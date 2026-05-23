<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Mis postulaciones</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Mis postulaciones</h2>
    <table>
        <tr><th>Oferta</th><th>Area</th><th>Fecha</th><th>Estado</th><th>Aprobado</th><th>Accion</th></tr>
        <c:forEach var="p" items="${postulaciones}">
            <tr>
                <td>${p.ofertaTitulo}</td>
                <td>${p.areaNombre}</td>
                <td>${p.fechaPostulacion}</td>
                <td>${p.estado}</td>
                <td>${p.aprobado}</td>
                <td>
                    <a class="btn" href="/postulante/postulaciones/${p.id}/estado">Ver estado</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</main>
</body>
</html>

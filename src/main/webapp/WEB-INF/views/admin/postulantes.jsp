<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Postulantes</title><link rel="stylesheet" href="/resources/css/style.css"><script src="/resources/js/app.js"></script></head>
<body>
<header><h1>Postulantes</h1><nav><a href="/admin/dashboard">Dashboard</a></nav></header>
<main>
    <h2>Postulantes activos</h2>
    <table>
        <tr><th>Nombre</th><th>Email</th><th>Oferta</th><th>Puntaje</th><th>Acciones</th></tr>
        <c:forEach var="p" items="${activos}">
            <tr>
                <td>${p.nombre}</td><td>${p.email}</td><td>${p.ofertaTitulo}</td><td>${p.puntaje}</td>
                <td>
                    <a class="btn" href="/admin/postulantes/aprobar/${p.id}">Aprobar</a>
                    <a class="btn secundario" href="/admin/postulantes/rechazar/${p.id}">Rechazar</a>
                    <a class="btn secundario" href="/admin/postulantes/finalizar/${p.id}">Finalizar</a>
                </td>
            </tr>
        </c:forEach>
    </table>
    <h2>Historial</h2>
    <table>
        <tr><th>Nombre</th><th>Oferta</th><th>Estado</th><th>Aprobado</th></tr>
        <c:forEach var="p" items="${historial}">
            <tr><td>${p.nombre}</td><td>${p.ofertaTitulo}</td><td>${p.estado}</td><td>${p.aprobado}</td></tr>
        </c:forEach>
    </table>
</main>
</body>
</html>

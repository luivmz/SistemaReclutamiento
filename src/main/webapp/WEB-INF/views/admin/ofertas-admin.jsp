<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Ofertas admin</title><link rel="stylesheet" href="/resources/css/style.css"><script src="/resources/js/app.js"></script></head>
<body>
<header><h1>Ofertas</h1><nav><a href="/admin/dashboard">Dashboard</a></nav></header>
<main>
    <form method="post" action="/admin/ofertas" class="card">
        <label>Titulo</label><input name="titulo" required>
        <label>Descripcion</label><textarea name="descripcion"></textarea>
        <label>Vacantes</label><input type="number" name="vacantes" value="1">
        <label>Area</label>
        <select name="areaId">
            <c:forEach var="area" items="${areas}">
                <option value="${area.id}">${area.nombre}</option>
            </c:forEach>
        </select>
        <label>Activa</label><select name="activa"><option value="true">Si</option><option value="false">No</option></select>
        <button type="submit">Guardar</button>
    </form>
    <table>
        <tr><th>Titulo</th><th>Area</th><th>Vacantes</th><th>Activa</th><th>Accion</th></tr>
        <c:forEach var="o" items="${ofertas}">
            <tr>
                <td>${o.titulo}</td><td>${o.areaNombre}</td><td>${o.vacantes}</td><td>${o.activa}</td>
                <td><a class="btn secundario" href="/admin/ofertas/eliminar/${o.id}" onclick="return confirmarAccion('Eliminar oferta?')">Eliminar</a></td>
            </tr>
        </c:forEach>
    </table>
</main>
</body>
</html>

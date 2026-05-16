<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Usuarios</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<header><h1>Usuarios</h1><nav><a href="/admin/dashboard">Dashboard</a></nav></header>
<main>
    <form method="post" action="/admin/usuarios" class="card">
        <label>Nombre</label><input name="nombre" required>
        <label>Email</label><input type="email" name="email" required>
        <label>Password</label><input name="password" required>
        <label>Rol</label><select name="rol"><option>ADMIN</option><option>POSTULANTE</option></select>
        <input type="hidden" name="activo" value="true">
        <button type="submit">Guardar</button>
    </form>
    <table>
        <tr><th>Nombre</th><th>Email</th><th>Rol</th><th>Activo</th></tr>
        <c:forEach var="u" items="${usuarios}">
            <tr><td>${u.nombre}</td><td>${u.email}</td><td>${u.rol}</td><td>${u.activo}</td></tr>
        </c:forEach>
    </table>
</main>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Registro</title>
    <link rel="stylesheet" href="/resources/css/style.css">
</head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Registro de postulante</h2>
    <form method="post" action="/registro" class="card">
        <label>Nombre</label><input name="nombre" required>
        <label>Email</label><input type="email" name="email" required>
        <label>Telefono</label><input name="telefono" required>
        <label>Password</label><input type="password" name="password" required>
        <button type="submit">Registrar</button>
    </form>
</main>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Registro de postulante</title>
    <link rel="stylesheet" href="/resources/css/style.css">
</head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Registro de postulante docente</h2>
    <form method="post" action="/registro" class="card">
        <label>Nombre</label><input name="nombre" required>
        <label>Email</label><input type="email" name="email" required>
        <label>Telefono</label><input name="telefono" required>
        <label>Password</label><input type="password" name="password" required>
        <button type="submit">Registrar</button>
    </form>
</main>
<%@ include file="../includes/footer.jsp" %>
</body>
</html>

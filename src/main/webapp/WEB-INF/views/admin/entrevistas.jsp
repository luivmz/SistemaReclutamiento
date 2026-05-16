<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Entrevistas</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<header><h1>Entrevistas</h1><nav><a href="/admin/dashboard">Dashboard</a></nav></header>
<main>
    <form method="post" action="/admin/entrevistas" class="card">
        <label>Tipo</label><select name="tipoEntrevista"><option>NORMAL</option><option>PSICOLOGICA</option></select>
        <label>Postulante</label>
        <select name="postulanteId">
            <c:forEach var="p" items="${postulantes}">
                <option value="${p.id}">${p.nombre} - ${p.ofertaTitulo}</option>
            </c:forEach>
        </select>
        <label>Fecha</label><input type="date" name="fecha">
        <label>Hora</label><input type="time" name="hora">
        <label>Lugar</label><input name="lugar">
        <label>Resultado</label><input name="resultado">
        <label>Observacion</label><textarea name="observacion"></textarea>
        <button type="submit">Guardar</button>
    </form>
    <table>
        <tr><th>Tipo</th><th>Postulante</th><th>Fecha</th><th>Hora</th><th>Resultado</th></tr>
        <c:forEach var="e" items="${entrevistas}">
            <tr><td>${e.tipoEntrevista}</td><td>${e.postulanteNombre}</td><td>${e.fecha}</td><td>${e.hora}</td><td>${e.resultado}</td></tr>
        </c:forEach>
    </table>
</main>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Evaluacion</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Evaluacion academica</h2>
    <p>Oferta: ${postulacion.ofertaTitulo}</p>
    <form method="post" action="/postulante/evaluacion/${postulacion.id}" class="card">
        <c:forEach var="pregunta" items="${preguntas}">
            <label>${pregunta.enunciado}</label>
            <input name="respuesta_${pregunta.id}" required>
        </c:forEach>
        <button type="submit">Finalizar evaluacion</button>
    </form>
</main>
</body>
</html>

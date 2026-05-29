<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Areas Academicas</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Areas academicas del Colegio Andino</h2>
    <div class="grid">
        <c:forEach var="area" items="${areas}">
            <section class="card">
                <h3>${area.nombre}</h3>
                <p>${area.descripcion}</p>
            </section>
        </c:forEach>
    </div>
</main>
<%@ include file="../includes/footer.jsp" %>
</body>
</html>

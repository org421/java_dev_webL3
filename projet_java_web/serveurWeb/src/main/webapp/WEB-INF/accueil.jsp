<%@ page import="servlet.serveurweb.Document" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Title</title>
    <%@ include file="bootstrap.jsp" %>
    <link rel="stylesheet" href="../css/accueil.css">
    <style>
        div {
            margin-bottom: 10px;
        }
        span {
            font-weight: bold;
        }
    </style>

</head>
<body>
<!-- <script type="text/javascript" src="../js_login.js"></script> -->
<%@ include file="header.jsp" %>

<!-- <%@ include file="monCalendrier.jsp" %> -->


<div class="container mt-5">
    <h1 class="p-5">Document(s) récent(s) :</h1>
    <c:if test="${not empty message}">
        <div class="alert alert-info text-center">${message}</div>
    </c:if>

    <div class="row">
        <c:forEach var="doc" items="${sessionScope.documents}">
            <div class="col-md-4 mb-3">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">${doc.nom}</h5>
                        <a href="${pageContext.request.contextPath}/Document?id=${doc.id}" class="btn btn-primary">Voir</a>

                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <section class="p-2">
        <div class="container">
            <div class="row d-flex justify-content-center">
                <div class="col-sm-3">
                    <div class="card" style="width: 18rem;">
                        <div class="card-body">
                            <h5 class="card-title">Nouveau document</h5>
                            <h6 class="card-subtitle mb-2 text-muted">Créer un nouveau document</h6>
                            <form action="Document" method="post">
                                <button type="submit" class="btn btn-success">Créer</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<script type="text/javascript"><%@include file="../js/calendar.js"%></script>
</body>
</html>

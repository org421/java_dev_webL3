<%@ page import="servlet.serveurweb.Document" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Tous les Documents</title>
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
<%@ include file="header.jsp" %>

<div class="container mt-5">
    <h1 class="p-5">Tous les Documents :</h1>
    <c:if test="${not empty message}">
        <div class="alert alert-info text-center">${message}</div>
    </c:if>

    <div class="row">
        <c:forEach var="doc" items="${sessionScope.allDocuments}">
            <div class="col-md-4 mb-3">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">${doc.nom}</h5>
                        <form action="${pageContext.request.contextPath}/MyDoc" method="post" style="display:inline;">
                            <input type="hidden" name="docId" value="${doc.id}">
                            <button type="submit" name="action" value="voir" class="btn btn-primary">Voir</button>
                            <button type="submit" name="action" value="supprimer" class="btn btn-danger">Supprimer</button>
                        </form>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<script type="text/javascript"><%@ include file="../js/calendar.js" %></script>
</body>
</html>

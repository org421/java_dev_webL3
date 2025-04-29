<%@ page import="servlet.serveurweb.Document" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Document</title>
    <%@ include file="bootstrap.jsp" %>
    <link rel="stylesheet" href="../css/accueil.css">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

    <script type="text/javascript"><%@include file="../js/calendar.js"%></script>
    <script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.11/index.global.min.js'></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var calendarEl = document.getElementById('calendar');
            var calendar = new FullCalendar.Calendar(calendarEl, {
                initialView: 'dayGridMonth'
            });
            calendar.render();
        });
    </script>

</head>
<!--<body onload="chargerDonneeCalendrier('${role}')"> -->
<body onload="chargerUser('${user.id}', '${user.pseudo}', '${role}')">

<%@ include file="header.jsp" %>
<form>
    <div class="w-75">
        <!-- Supposons que 'doc' est un attribut de requête contenant un document, sinon ajustez selon vos besoins -->
        <input type="text" id="nomDocument" name="nomDocument" class="form-control m-2 w-75 " value="<%= ((Document)request.getAttribute("doc")).getNom() %>" required autofocus>
    </div>
</form>

<c:if test="${role=='owner'}">
<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#inviteModal">
    Inviter des personnes
</button>
</c:if>

<div id='calendar' style="margin: 10px;"></div>

<div class="modal fade" id="inviteModal" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modalLabel">Inviter des personnes</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <% if (request.getAttribute("message") != null) { %>
                <div class="alert <%= request.getAttribute("messageStyle") %> text-center" role="alert">
                    <%= request.getAttribute("message") %>
                </div>
                <% } %>
                <form id="inviteForm" method="post" action="inviteUser?id=<%= request.getParameter("id") %>">
                    <div class="form-group">
                        <label for="inviteEmail">Email de l'invité:</label>
                        <input type="email" class="form-control" id="inviteEmail" name="inviteEmail" required>
                    </div>
                    <div class="form-group">
                        <label for="inviteRole">Rôle:</label>
                        <select class="form-control" id="inviteRole" name="inviteRole">
                            <option value="editor">Éditeur</option>
                            <option value="viewer">Visualisateur</option>
                        </select>
                    </div>
                    <div style="text-align: center; padding-top: 20px;">
                        <button type="submit" class="btn btn-primary">Envoyer l'invitation</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="chat.jsp" %>
<script type="text/javascript" src="./js/document.js"></script>
<script type="text/javascript" src="./js/chat.js"></script>
</body>
</html>
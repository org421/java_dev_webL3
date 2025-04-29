<%@ page import="servlet.serveurweb.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Mon Profil</title>
    <%@ include file="./bootstrap.jsp" %>

</head>
<body>

<%@ include file="./header.jsp" %>
<div class="container mt-5">
    <h1 class="h3 mb-3 font-weight-normal">Votre profil</h1>

    <% if (request.getAttribute("message") != null) {
        String message = (String) request.getAttribute("message");
        String alertClass = "alert-success";
        if ("Ancien mot de passe incorrect.".equals(message)) {
            alertClass = "alert-danger error-message"; // Utilisation de Bootstrap pour les erreurs et de la classe personnalisée pour la couleur rouge
        }
    %>
    <div class="alert <%= alertClass %>" role="alert">
        <%= message %>
    </div>
    <% } %>

    <%
        User user = (User) session.getAttribute("user");
    %>
    <p>Pseudo : <%= user.getPseudo() %></p>
    <p>Email : <%= user.getEmail() %></p>
    <form action="Profile" method="post" class="form-signin">
        <input type="hidden" name="action" value="updatePassword">
        <label for="oldPassword" class="sr-only">Ancien mot de passe</label>
        <input type="password" id="oldPassword" name="oldPassword" class="form-control mb-2" placeholder="Ancien mot de passe" required>
        <label for="password" class="sr-only">Nouveau mot de passe</label>
        <input type="password" id="password" name="password" class="form-control mb-2" placeholder="Nouveau mot de passe" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Mettre à jour</button>
    </form>
</div>
</body>
</html>

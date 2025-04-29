<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <%@ include file="../bootstrap.jsp" %>
    <script type="text/javascript"><%@include file="../../js/admin.js"%></script>

</head>
<body>

<%@ include file="../header.jsp" %>
<div class="d-flex justify-content-center mt-5">
    <% if (session.getAttribute("loginError") != null) { %>
    <div class="alert alert-danger" role="alert">
        <%= session.getAttribute("loginError") %>
    </div>
    <% session.removeAttribute("loginError"); %>
    <% } %>

    <form id="login" method="post" style="display: block">
        <h1 class="h3 m-2 font-weight-normal align-self-center">Login:</h1>

        <input type="hidden" name="action" value="login">
        <label for="loginEmail" class="sr-only">Email address</label>
        <input type="email" id="loginEmail" name="loginEmail" class="form-control m-2" placeholder="Email address" required autofocus>
        <label for="loginPassword" class="sr-only">Password</label>
        <input type="password" id="loginPassword" name="loginPassword" class="form-control m-2" placeholder="Password" required>
        <button class="btn btn-secondary btn-block m-2" type="submit">Connexion</button>
        <p class="btn btn-secondary btn-block m-2" onclick="switchLog()">Inscription</p>
    </form>
    <form id="inscription" method="post" style="display: none">
        <h1 class="h3 m-2 font-weight-normal align-self-center">Créer un compte:</h1>
        <input type="hidden" name="action" value="inscription">
        <label for="inscriptionPseudo" class="sr-only">Pseudo</label>
        <input type="text" id="inscriptionPseudo" name="inscriptionPseudo" class="form-control m-2" placeholder="Pseudo" required autofocus>
        <label for="inscriptionEmail" class="sr-only">Email address</label>
        <input type="email" id="inscriptionEmail" name="inscriptionEmail" class="form-control m-2" placeholder="Email address" required autofocus>
        <label for="inscriptionPassword" class="sr-only">Password</label>
        <input type="password" id="inscriptionPassword" name="inscriptionPassword" class="form-control m-2" placeholder="Password" required>
        <p class="btn btn-secondary btn-block m-2" onclick="switchLog()">Connexion</p>
        <button class="btn btn-secondary btn-block m-2" type="submit">Inscription</button>
    </form>
</div>
</body>

</html>


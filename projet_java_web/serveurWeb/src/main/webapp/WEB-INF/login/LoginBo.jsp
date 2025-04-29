<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>LoginBO</title>
    <%@ include file="../bootstrap.jsp" %>
</head>
<body>
<%@ include file="../header.jsp" %>
    <div class="d-flex justify-content-center mt-5">


        <form action="LoginBO" method="post">
            <h1 class="h3 m-2 font-weight-normal align-self-center">Login Dashboard:</h1>

            <label for="inputEmail" class="sr-only">Email address</label>
            <input type="email" name="email" id="inputEmail" class="form-control m-2" placeholder="Email address" required autofocus>
            <label for="inputPassword" class="sr-only">Password</label>
            <input type="password" name="password" id="inputPassword" class="form-control m-2" placeholder="Password" required>
            <button class="btn btn-secondary btn-block m-2" type="submit">Connexion</button>
        </form>

    </div>
</body>
</html>

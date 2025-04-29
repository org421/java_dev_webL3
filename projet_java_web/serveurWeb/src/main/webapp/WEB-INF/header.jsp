<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/serveurWeb_war_exploded">
        <img src="<%= request.getContextPath() %>/ressource/logo.jpeg" style="height: 80px; width: 80px" alt="Logo de l'application">
    </a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <% if (session.getAttribute("user") != null) { %>
            <li class="nav-item">
                <form action="Logout" method="post" style="margin-bottom: 0;">
                    <button type="submit" class="nav-link btn btn-link" style="margin: 0">Deconnexion</button>
                </form>
            </li>
            <% } else { %>
            <li class="nav-item">
                <a class="nav-link" href="Login">Connexion<span class="sr-only">(current)</span></a>
            </li>
            <% } %>
            <li class="nav-item">
                <a class="nav-link" href="MyDoc">Tout mes document</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="Profile">Mon Profil</a>
            </li>
        </ul>
    </div>
</nav>
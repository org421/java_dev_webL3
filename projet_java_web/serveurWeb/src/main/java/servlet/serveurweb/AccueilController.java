package servlet.serveurweb;

import java.io.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

public class AccueilController extends HttpServlet {
    private String message;
    private static final String URL = "jdbc:mariadb://localhost:3306/dw2_projet_web";
    private static final String USERNAME = "webuser";
    private static final String PASSWORD = "multipass";
    private Connection connection;

    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            request.setAttribute("message", "Pas de documents disponibles car vous n'êtes pas connecté.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/accueil.jsp");
            dispatcher.forward(request, response);
            return;
        }

        User user = (User) session.getAttribute("user");
        List<Document> documents = new ArrayList<>();
        String query = "SELECT d.id, d.nom FROM document d "
                + "INNER JOIN lien_document_user l ON d.id = l.id_document "
                + "WHERE l.id_user = ? "
                + "ORDER BY l.derniere_connexion DESC "
                + "LIMIT 3";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int docId = resultSet.getInt("id");
                String docNom = resultSet.getString("nom");
                Document document = new Document(docId, docNom);
                documents.add(document);
                System.out.println("Document ajouté - ID: " + docId + ", Nom: " + docNom);
            }

            System.out.println(documents.size());

            request.setAttribute("documents", documents);
            session.setAttribute("documents", documents);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/accueil.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'exécution de la requête SQL.");
            response.sendRedirect(request.getContextPath() + "/Login");
        }
    }





    public void destroy() {
    }
}
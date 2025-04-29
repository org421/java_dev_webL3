package servlet.serveurweb;


import java.io.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.sql.*;
import java.time.LocalDate;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;


@WebServlet("/Document")
public class DocumentController extends HttpServlet {
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

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Verification.est_connecter(request) == false){
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }
        System.out.println("user connecter");
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user);
        try {
            String query = "INSERT INTO document (nom, id_user_proprio) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, "Nouveau document");
            statement.setString(2, String.valueOf(user.getId()));

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                String role = "owner";
                ResultSet generatedKeys = statement.getGeneratedKeys();

                generatedKeys.next();
                System.out.println("Nouveau document créer !");
                query = "INSERT INTO lien_document_user (id_document,id_user,role,derniere_connexion) VALUES (?, ?, ?, ?)";
                statement = connection.prepareStatement(query);
                statement.setInt(1, generatedKeys.getInt(1));
                statement.setInt(2, user.getId());
                statement.setString(3, role);
                statement.setDate(4, Date.valueOf(LocalDate.now()));
                rowsInserted = statement.executeUpdate();
                response.sendRedirect(request.getContextPath() + "/Document?id="+generatedKeys.getInt(1));
                return;

            } else {
                System.out.println("Erreur à la création !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/AccueilController");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        if(!Verification.est_connecter(request) || request.getParameter("id") == null){
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }
        String idDocument = request.getParameter("id");
        User user = (User) request.getSession().getAttribute("user");
        try {
            String role = isOwner(user.getId(), Integer.parseInt(idDocument), connection);
            request.setAttribute("role", role);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        String query = "SELECT * FROM lien_document_user WHERE id_document = ? AND id_user = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(idDocument));
            statement.setInt(2, user.getId());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next() == false){
                //pas de lien user - document pour cette id_document
                response.sendRedirect(request.getContextPath() + "/Login");
            }
            //fin des verif début du code de la page :)
            query = "SELECT * FROM document WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(idDocument));
            resultSet = statement.executeQuery();
            resultSet.next();
            Document doc = new Document(resultSet.getInt("id"),resultSet.getString("nom"));
            System.out.println(doc.getNom());
            request.setAttribute("doc",doc);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/document.jsp");
            dispatcher.include(request, response);
        } catch (SQLException e) {
            //erreur sur le requête sql
            response.sendRedirect(request.getContextPath() + "/Login");
            throw new RuntimeException(e);
        }
    }

    private String isOwner(int userId, int documentId, Connection conn) throws SQLException {
        String query = "SELECT role FROM lien_document_user WHERE id_document = ? AND id_user = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, documentId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return "NULL";
    }

    public void destroy() {
    }
}
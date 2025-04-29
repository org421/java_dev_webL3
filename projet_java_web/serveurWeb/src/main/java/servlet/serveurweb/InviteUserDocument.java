package servlet.serveurweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/inviteUser")
public class InviteUserDocument extends HttpServlet {

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




    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("test");
        String userEmail = request.getParameter("inviteEmail");
        String userRole = request.getParameter("inviteRole");
        int documentId = getCurrentDocumentId(request);
        String message;
        String messageStyle;
        System.out.println(" Invite ce mec :" +userEmail + userRole);
        try {
            if (userExists(userEmail, connection)) {
                int userId = getUserId(userEmail, connection);
                linkUserToDocument(userId, documentId, userRole, connection);
                System.out.println("Utilisateur lié avec succès");
                message = "Utilisateur invité avec succès.";
                messageStyle = "alert-success";
            } else {
                System.out.println("L'utilisateur n'existe pas");
                message = "L'utilisateur n'existe pas. / Vous ne pousez pas inviter.";
                messageStyle = "alert-danger";
            }
        } catch (SQLException ex) {
            System.out.println("Erreur de connexion : " + ex.getMessage());
            message = "Erreur de base de données : " + ex.getMessage();
            messageStyle = "alert-danger";
        }

        request.setAttribute("message", message);
        request.setAttribute("messageStyle", messageStyle);
        response.sendRedirect(request.getContextPath() + "/Document?id="+documentId);
/*
        request.getRequestDispatcher("").forward(request, response);
*/
    }


    private int getCurrentDocumentId(HttpServletRequest request) throws ServletException {
        String documentIdStr = request.getParameter("id");
        if (documentIdStr == null || documentIdStr.isEmpty()) {
            throw new ServletException("ID du document manquant dans la requête.");
        }
        return Integer.parseInt(documentIdStr);
    }



    private boolean userExists(String email, Connection conn) throws SQLException {
        String query = "SELECT COUNT(*) FROM app_user WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }


    private int getUserId(String email, Connection conn) throws SQLException {
        String query = "SELECT id FROM app_user WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        throw new SQLException("Utilisateur non trouvé");
    }


    private void linkUserToDocument(int userId, int documentId, String role, Connection conn) throws SQLException {
        String query = "INSERT INTO lien_document_user (id_document, id_user, role) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, documentId);
            stmt.setInt(2, userId);
            stmt.setString(3, role);
            stmt.executeUpdate();
        }
    }




    public void destroy() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

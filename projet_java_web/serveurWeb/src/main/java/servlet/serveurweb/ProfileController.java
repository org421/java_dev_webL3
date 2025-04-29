package servlet.serveurweb;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.*;

@WebServlet("/Profile")
public class ProfileController extends HttpServlet {

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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/Login");
        } else {
            request.getRequestDispatcher("/WEB-INF/Profile.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("password");

            String query = "SELECT mdp FROM app_user WHERE pseudo = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, user.getPseudo());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("mdp");
                    if (Hashmdp.checkPassword(oldPassword, storedPassword)) {
                        String updateQuery = "UPDATE app_user SET mdp = ? WHERE pseudo = ?";
                        try {
                            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                            updateStatement.setString(1, Hashmdp.hashPassword(newPassword));
                            updateStatement.setString(2, user.getPseudo());
                            updateStatement.executeUpdate();
                            request.setAttribute("message", "Mot de passe mis à jour avec succès !");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        request.setAttribute("message", "Ancien mot de passe incorrect.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("message", "Erreur de base de données lors de la vérification du mot de passe.");
            }
            request.getRequestDispatcher("/WEB-INF/Profile.jsp").forward(request, response);
        } else {
            response.sendRedirect("Login.jsp");
        }
    }
}
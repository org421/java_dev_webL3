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

@WebServlet("/Admin")
public class DashboardController extends HttpServlet {

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

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/LoginBO");
            return;
        }

        UserBo user = (UserBo) session.getAttribute("userBO");
        if (user == null) {
            System.out.println("Utilisateur non connecté ");
            response.sendRedirect(request.getContextPath() + "/LoginBO");
            return;
        }

        if (user.isAdmin()) {
            System.out.println("Admin connecté " + user.getUsername());
            List<User> users = getAllUsers();
            request.setAttribute("users", users);
            System.out.println("taille user :" + users.size());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/admin.jsp");
            dispatcher.forward(request, response);
        } else {
            System.out.println("Accès refusé pour l'utilisateur : " + user.getUsername());
            response.sendRedirect(request.getContextPath() + "/LoginBO");
        }
    }

    private List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, pseudo, email, is_valide FROM app_user";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User(resultSet.getInt("id"), resultSet.getString("pseudo"),resultSet.getString("email"), resultSet.getBoolean("is_valide")
                );
                System.out.println("User: " + user.getId() + ", " + user.getPseudo() + ", " + user.getEmail());

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            deleteUser(userId);
        }
        doGet(request, response);
    }

    private void deleteUser(int userId) {
        String query = "DELETE FROM app_user WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

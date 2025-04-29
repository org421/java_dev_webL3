package servlet.serveurweb;

import java.io.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.sql.*;


@WebServlet("/Login")
public class LoginController extends HttpServlet {

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

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String action = request.getParameter("action");
        if ("login".equals(action)) {
            String email = request.getParameter("loginEmail");
            String password = request.getParameter("loginPassword");
            String hashmdp = Hashmdp.hashPassword(password);
            try {
                String query = "SELECT * FROM app_user WHERE email = ? AND mdp = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, hashmdp);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String pseudo = resultSet.getString("pseudo");
                    String userEmail = resultSet.getString("email");
                    boolean isValid = resultSet.getBoolean("is_valide");

                    User user = new User(userId, pseudo, userEmail, isValid);

                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    session.setAttribute("loggedIn", true);

                    System.out.println("Connexion réussie");
                    response.sendRedirect(request.getContextPath() + "/AccueilController");
                } else {
                    HttpSession session = request.getSession();
                    session.setAttribute("loginError", "Compte inexistant");
                    System.out.println("Connexion échouée");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if ("inscription".equals(action)) {
            String pseudo = request.getParameter("inscriptionPseudo");
            String email = request.getParameter("inscriptionEmail");
            String password = request.getParameter("inscriptionPassword");
            String hashmdp = Hashmdp.hashPassword(password);
            try {
                String query = "INSERT INTO app_user (pseudo, email, mdp) VALUES (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, pseudo);
                statement.setString(2, email);
                statement.setString(3, hashmdp);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Inscription réussie !");
                    HttpSession session = request.getSession();

                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        session.setAttribute("userId", userId);
                        User userSession = new User(userId,pseudo,email,false);
                        session.setAttribute("user",userSession);
                        System.out.println("ID utilisateur généré : " + userId);
                        response.sendRedirect(request.getContextPath() + "/AccueilController");
                    }
                } else {
                    System.out.println("Échec de l'inscription !");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //response.sendRedirect(request.getContextPath() + "/AccueilController");
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        Object log = session.getAttribute("user");
        if(log != null){
            response.sendRedirect(request.getContextPath() + "/AccueilController");
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login/Login.jsp");
        dispatcher.include(request, response);
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
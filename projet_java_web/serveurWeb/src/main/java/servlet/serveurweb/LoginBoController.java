package servlet.serveurweb;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet("/LoginBO")
public class LoginBoController extends HttpServlet {

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
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String hashedPassword = Hashmdp.hashPassword(password);
        try {
            String query = "SELECT * FROM app_user_bo WHERE email = ? AND mdp = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, hashedPassword);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("userBO", new UserBo(email, password));
                System.out.println("Connexion admin réussie");
                response.sendRedirect(request.getContextPath() + "/Admin");

            } else {
                HttpSession session = request.getSession();
                session.setAttribute("loginError", "Email ou mot de passe incorrect.");
                System.out.println("Connexion échouée");
                System.out.println(email + " " + password);
                doGet(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login/LoginBo.jsp");
        dispatcher.forward(request, response);
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

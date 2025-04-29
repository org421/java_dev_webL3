package servlet.serveurweb;

import jakarta.servlet.RequestDispatcher;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/MyDoc")
public class AllDocument extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String URL = "jdbc:mariadb://localhost:3306/dw2_projet_web";
    private static final String USERNAME = "webuser";
    private static final String PASSWORD = "multipass";
    private Connection connection;

    public void init() throws ServletException {
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
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/MyDoc.jsp");
            dispatcher.forward(request, response);
            return;
        }

        User user = (User) session.getAttribute("user");
        List<Document> allDocuments = new ArrayList<>();
        String query = "SELECT d.id, d.nom FROM document d "
                + "INNER JOIN lien_document_user l ON d.id = l.id_document "
                + "WHERE l.id_user = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int docId = resultSet.getInt("id");
                String docNom = resultSet.getString("nom");
                Document document = new Document(docId, docNom);
                allDocuments.add(document);
            }

            request.setAttribute("allDocuments", allDocuments);
            session.setAttribute("allDocuments", allDocuments);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/MyDoc.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/Login");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        String action = request.getParameter("action");
        int docId = Integer.parseInt(request.getParameter("docId"));
        User user = (User) session.getAttribute("user");

        if ("supprimer".equals(action)) {
            String query = "DELETE FROM lien_document_user WHERE id_document = ? AND id_user = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, docId);
                statement.setInt(2, user.getId());
                statement.executeUpdate();

                response.sendRedirect(request.getContextPath() + "/MyDoc");
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/MyDoc?error=deleteFailed");
            }
        } else if ("voir".equals(action)) {
            response.sendRedirect(request.getContextPath() + "/Document?id=" + docId);
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

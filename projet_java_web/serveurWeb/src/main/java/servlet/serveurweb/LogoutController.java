package servlet.serveurweb;

import java.io.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.sql.*;


@WebServlet("/Logout")
public class LogoutController extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Récupérer la session existante, false pour ne pas en créer une nouvelle si elle n'existe pas
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.removeAttribute("user");

            session.removeAttribute("userBO");

            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/AccueilController");
    }

}
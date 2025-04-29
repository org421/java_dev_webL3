package servlet.serveurweb;

import java.io.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebServlet("/RecuperationEvenement")
public class AjaxRecuperationDate extends HttpServlet {

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
        System.out.println("Requete ajax!");
        String idDocument = request.getParameter("id");
        String dateDebut = request.getParameter("dateDebut");
        String dateFin = request.getParameter("dateFin");
        System.out.println(idDocument);
        System.out.println(dateDebut);
        System.out.println(dateFin);
        String query = "SELECT * FROM evenement_document WHERE id_document = ? AND date_debut BETWEEN ? AND ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date parsedDateDebut = inputFormat.parse(dateDebut);
            Date parsedDateFin = inputFormat.parse(dateFin);

            String formattedDateDebut = outputFormat.format(parsedDateDebut);
            String formattedDateFin = outputFormat.format(parsedDateFin);


            statement.setInt(1, Integer.parseInt(idDocument));
            statement.setString(2, formattedDateDebut);
            statement.setString(3, formattedDateFin);

            ResultSet resultSet = statement.executeQuery();
            List<Map<String, String>> evenements = new ArrayList<>();

            while(resultSet.next() != false){
                Map<String, String> evenement = new HashMap<>();

                evenement.put("nom", resultSet.getString("nom"));
                evenement.put("description", resultSet.getString("contenu"));
                evenement.put("date_debut", resultSet.getString("date_debut"));
                evenement.put("date_fin", resultSet.getString("date_fin"));
                evenement.put("id", resultSet.getString("id"));
                System.out.println(evenement);
                evenements.add(evenement);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String evenementsJson = objectMapper.writeValueAsString(evenements);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(evenementsJson);
            out.flush();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


    }
}

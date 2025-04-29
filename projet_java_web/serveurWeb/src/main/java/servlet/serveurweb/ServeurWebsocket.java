package servlet.serveurweb;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.format.DateTimeFormatter;


@ServerEndpoint("/WebsocketDocument")
public class ServeurWebsocket {

    private static final Map<String, SessionWebSocketInfo> sessions = new ConcurrentHashMap<>();

    private static final String URL = "jdbc:mariadb://localhost:3306/dw2_projet_web";
    private static final String USERNAME = "webuser";
    private static final String PASSWORD = "multipass";

    private Connection connection;


    @OnOpen
    public void onOpen(Session session) {
        int idDocument = -1;
        String queryString = session.getQueryString();
        if (queryString != null) {
            String[] params = queryString.split("=");
            if (params.length == 2 && "idDocument".equals(params[0])) {
                idDocument = Integer.parseInt(params[1]);
            }
        }

        System.out.println("Nouvelle connexion WebSocket ouverte pour le document: " + idDocument);

        if (idDocument == -1) {
            System.err.println("Identifiant du document est null.");
            return;
        }

        SessionWebSocketInfo sessionInfo = new SessionWebSocketInfo(session,Integer.valueOf(idDocument));
        sessions.put(session.getId(),sessionInfo);
    }
    @OnMessage
    public void onMessage(String message, Session session) throws JsonProcessingException, SQLException {
        sessions.forEach((key, userSession) -> {
            System.out.println(userSession.getSession().getId());
        });

        System.out.println(sessions);
        System.out.println("websocket");

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        System.out.println("websocket2");
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("websocket3");
        Map<String, String> evenementMap = objectMapper.readValue(message, Map.class);
        System.out.println("websocket5");
        System.out.println(evenementMap.get("type"));
        if(evenementMap.get("type").equals("message")) {
            System.out.println("type message !");
        }
        if(evenementMap.get("type").equals("evenement")) {
            System.out.println("type evenement !");
            String query = "INSERT INTO evenement_document (id_document, date_debut, date_fin,nom,contenu) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, Integer.parseInt(evenementMap.get("idDocument")));

            String heureDebutStr = evenementMap.get("heureDebut");
            LocalDateTime heureDebut = LocalDateTime.parse(heureDebutStr, DateTimeFormatter.ISO_DATE_TIME);
            Timestamp heureDebutTimestamp = Timestamp.valueOf(heureDebut);
            statement.setTimestamp(2, heureDebutTimestamp);

            String dateFinStr = evenementMap.get("dateFin");
            LocalDateTime dateFin = LocalDateTime.parse(dateFinStr, DateTimeFormatter.ISO_DATE_TIME);
            Timestamp dateFinTimestamp = Timestamp.valueOf(dateFin);
            statement.setTimestamp(3, dateFinTimestamp);
            statement.setString(4, evenementMap.get("nom"));
            statement.setString(5, evenementMap.get("description"));

            int rowsInserted = statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            int idEvent = generatedKeys.getInt(1);
            evenementMap.put("id", String.valueOf(idEvent));
            message = objectMapper.writeValueAsString(evenementMap);

        }
        if(evenementMap.get("type").equals("suppr")){
            System.out.println(evenementMap.get("idEvent"));
            System.out.println("type suppr");
            String querydel = "DELETE FROM evenement_document WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(querydel);
            statement.setInt(1, Integer.parseInt(evenementMap.get("idEvent")));
            statement.executeUpdate();
        }
        if(evenementMap.get("type").equals("editNom")) {
            System.out.println("type change nom");
            String queryUpdate = "UPDATE document SET nom = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(queryUpdate);
            statement.setString(1, evenementMap.get("nom"));
            statement.setInt(2, Integer.parseInt(evenementMap.get("idDocument")));
            statement.executeUpdate();
        }

        System.out.println("Message reçu du client " + session.getId() + ": " + message);
        SessionWebSocketInfo sessionInfo = sessions.get(session.getId());
        String finalMessage = message;
        sessions.forEach((key, userSession) -> {
            if(userSession.getIdDocument() == sessionInfo.getIdDocument() ){
                userSession.getSession().getAsyncRemote().sendText(finalMessage);
            }
        });
    }

    @OnError
    public void onError(Throwable throwable) {
        System.err.println("Erreur sur la connexion WebSocket: ");
        throwable.printStackTrace();
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Connexion WebSocket fermée: " + session.getId());
        sessions.computeIfPresent(session.getId(), (sessionId, sessionInfo) -> {
            if (sessionInfo.getSession().equals(session)) {
                return null;
            } else {
                return sessionInfo;
            }
        });
    }


}

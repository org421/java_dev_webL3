package servlet.serveurweb;

import jakarta.websocket.Session;

public class SessionWebSocketInfo {
    private Session session;
    private int idDocument;

    public SessionWebSocketInfo(Session session, int idDocument) {
        this.session = session;
        this.idDocument = idDocument;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public int getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(int idDocument) {
        this.idDocument = idDocument;
    }
}

package servlet.serveurweb;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String pseudo;
    private String email;
    private Boolean is_valide;


    public int getId() {
        return id;
    }

    public Boolean getIs_valide() {
        return is_valide;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) { this.id = id; }
    public User(int id, String pseudo, String email, Boolean is_valide) {
        this.id = id;
        this.pseudo = pseudo;
        this.email = email;
        this.is_valide = is_valide;
    }
}

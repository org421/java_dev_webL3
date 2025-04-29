package servlet.serveurweb;

public class Document {
    private int id;
    private String nom;


    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Document(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }
}

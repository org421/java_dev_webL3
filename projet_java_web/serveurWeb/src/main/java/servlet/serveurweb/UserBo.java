package servlet.serveurweb;

public class UserBo {
    private String username;
    private String password;
    private boolean isAdmin;

    public UserBo(String username, String password) {
        this.username = username;
        this.password = password;
        this.isAdmin = true; // Puisque c'est spécifique aux admins
    }

    // Getters et setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}

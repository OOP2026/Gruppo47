package model;

abstract public class Utente {
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String login;

    public Utente(String nome, String cognome, String email, String password, String login){
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.login = login;
    }

    // --- GET E SET ---

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean login(String login, String password) {
        return ( login.equals(this.login) && password.equals(this.password));
    }
}

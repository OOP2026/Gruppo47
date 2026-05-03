package Classi;

public class Studente extends Utente{
    private String matricola;
    public Studente(String login, String nome, String cognome, String mail, String password) {
        super(nome, cognome, mail, password, login);
        this.matricola = matricola;
    }

    //Get e Set
    public String getMatricola() {
        return matricola;
    }
    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    //Metodi
    public void visualizzaOrario(){

    }
}

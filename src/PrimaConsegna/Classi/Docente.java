package Classi;

import java.util.ArrayList;
import java.util.List;

public class Docente extends Utente{
    private List<Insegnamento> insegnamenti;

    public Docente(String nome, String cognome, String email, String password, String login){
        super(nome, cognome, email, password, login);
        this.insegnamenti = new ArrayList<>();
    }

    //Metodi
    public void visualizzaProprioOrario(){

    }

    public void richiediSpostamento(){

    }

    public void aggiungiInsegnamento(Insegnamento insegnamento){
        this.insegnamenti.add(insegnamento);
    }
}

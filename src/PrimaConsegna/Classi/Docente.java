package Classi;

import java.util.ArrayList;
import java.util.List;

public class Docente extends Utente{
    private List<Insegnamento> insegnamenti;
    private Orario orarioGenerale;

    public Docente(String nome, String cognome, String email, String password, String login, Orario orario){
        super(nome, cognome, email, password, login);
        this.insegnamenti = new ArrayList<>();
        this.orarioGenerale = orario;
    }

    //Metodi
    public void visualizzaOrario(){
        System.out.println("\n---ORARIO DOCENTE: "+this.getCognome()+" ---");
        if(orarioGenerale == null){
            System.out.println("Errore: orario non esistente");
            return;
        }
        List<Lezione> mieLezioni = orarioGenerale.getLezioniPerDocente(this);
        if(mieLezioni.isEmpty()){
            System.out.println("Non ci sono lezioni a tuo nome");
        } else{
            for(Lezione l : mieLezioni){
                System.out.println(l.getGiorno() + " [" + l.getOraInizio() + "-" + l.getOraFine() + "] " +
                        l.getInsegnamento().getNome() + " - Aula: " + l.getAula().getNome());
            }
        }
    }

    public void richiediSpostamento(){

    }

    public void aggiungiInsegnamento(Insegnamento insegnamento){
        this.insegnamenti.add(insegnamento);
    }
}

package Classi;

import java.util.List;

public class Studente extends Utente {
    private String matricola;
    private Orario orarioGenerale;
    private AnnoCorso annoS;

    public Studente(String nome, String cognome, String mail, String password, String login,AnnoCorso annoS, String matricola, Orario orario) {
        super(nome, cognome, mail, password, login);
        this.matricola = matricola;
        this.orarioGenerale = orario;
        this.annoS = annoS;
    }

    //Get e Set
    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public AnnoCorso getAnnoS() {
        return annoS;
    }

    public void setAnnoS(AnnoCorso annoS) {
        this.annoS = annoS;
    }

    //Metodi
    public void visualizzaOrarioCorso() {
        System.out.println("\n--- ORARIO STUDENTE (" + this.annoS + " ANNO) ---");

        if (orarioGenerale == null) {
            System.out.println("Errore: Sistema orario non collegato.");
            return;
        }

        // Recuperiamo solo le lezioni del nostro anno usando il metodo in Orario
        List<Lezione> lezioniMioAnno = orarioGenerale.getLezioniPerAnno(this.annoS);

        if (lezioniMioAnno.isEmpty()) {
            System.out.println("Non ci sono lezioni programmate per il " + this.annoS + " anno.");
        } else {
            for (Lezione l : lezioniMioAnno) {
                // Stampiamo i dettagli: Giorno [Inizio-Fine] Materia (Prof. Cognome)
                System.out.println(l.getGiorno() + " [" + l.getOraInizio() + "-" + l.getOraFine() + "] " +
                        l.getInsegnamento().getNome() +
                        " (Aula: " + l.getAula().getNome() +
                        ", Prof: " + l.getInsegnamento().getDocenteTitolare().getCognome() + ")");
            }
        }
    }
}
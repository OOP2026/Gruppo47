package model;

import java.util.List;

/**
 * Rappresenta uno studente iscritto all'università.
 * <p>
 * Estende la classe {@link Utente} aggiungendo le informazioni specifiche dello studente,
 * come il numero di matricola e l'anno di corso frequentato. Permette inoltre
 * la visualizzazione dell'orario delle lezioni filtrato in base al proprio anno.
 * </p>
 *
 * @see Utente
 */
public class Studente extends Utente {
    private String matricola;
    private Orario orarioGenerale;
    private AnnoCorso annoS;

    /**
     * Costruisce un nuovo studente con i dati anagrafici, le credenziali e le informazioni accademiche.
     *
     * @param nome     Il nome dello studente.
     * @param cognome  Il cognome dello studente.
     * @param mail     L'indirizzo email (es. istituzionale).
     * @param password La password per l'accesso.
     * @param login    Lo username per l'autenticazione.
     * @param annoS    L'anno di corso a cui lo studente è attualmente iscritto.
     * @param matricola Il codice univoco identificativo dello studente.
     * @param orario   Il sistema di gestione dell'orario generale dell'università.
     */
    public Studente(String nome, String cognome, String mail, String password, String login, AnnoCorso annoS, String matricola, Orario orario) {
        super(nome, cognome, mail, password, login);
        this.matricola = matricola;
        this.orarioGenerale = orario;
        this.annoS = annoS;
    }

    // --- GET E SET ---

    /**
     * Restituisce il numero di matricola dello studente.
     *
     * @return La matricola dello studente.
     */
    public String getMatricola() {
        return matricola;
    }

    /**
     * Imposta o modifica il numero di matricola dello studente.
     *
     * @param matricola La nuova matricola da assegnare.
     */
    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    /**
     * Restituisce l'anno di corso frequentato attualmente dallo studente.
     *
     * @return L'anno di corso (es. primo, secondo, ecc.).
     */
    public AnnoCorso getAnnoS() {
        return annoS;
    }

    /**
     * Imposta o aggiorna l'anno di corso dello studente.
     *
     * @param annoS Il nuovo anno di corso.
     */
    public void setAnnoS(AnnoCorso annoS) {
        this.annoS = annoS;
    }

    // --- METODI ---

    /**
     * Stampa a schermo l'orario delle lezioni specifico per l'anno di corso dello studente.
     * <p>
     * Il metodo interroga l'oggetto {@link Orario} generale per estrapolare unicamente
     * le lezioni destinate all'anno di corso corrente ({@code annoS}). Se il sistema
     * orario non è collegato o se non ci sono lezioni programmate, stampa un messaggio di avviso.
     * </p>
     */
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
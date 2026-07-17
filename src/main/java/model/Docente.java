package model;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;

/**
 * Rappresenta un docente all'interno del sistema universitario.
 * <p>
 * Estende la classe {@link Utente} e gestisce la lista degli insegnamenti a lui assegnati.
 * Fornisce funzionalità per visualizzare il proprio orario personale e per
 * inoltrare richieste di spostamento per le proprie lezioni.
 * </p>
 *
 * @see Utente
 */
public class Docente extends Utente {
    private List<Insegnamento> insegnamenti;
    private Orario orarioGenerale;

    /**
     * Costruisce un nuovo docente inizializzando i suoi dati anagrafici e il collegamento all'orario.
     * La lista degli insegnamenti viene inizializzata vuota.
     *
     * @param nome     Il nome del docente.
     * @param cognome  Il cognome del docente.
     * @param email    L'indirizzo email (es. istituzionale).
     * @param password La password per l'accesso.
     * @param login    Lo username per l'autenticazione.
     * @param orario   Il sistema di gestione dell'orario generale dell'università.
     */
    public Docente(String nome, String cognome, String email, String password, String login, Orario orario){
        super(nome, cognome, email, password, login);
        this.insegnamenti = new ArrayList<>();
        this.orarioGenerale = orario;
    }

    // --- METODI ---

    /**
     * Stampa a schermo l'orario settimanale contenente esclusivamente le lezioni di questo docente.
     * <p>
     * Il metodo interroga il sistema orario generale filtrando le lezioni in cui
     * il docente risulta essere il titolare dell'insegnamento.
     * </p>
     */
    public void visualizzaOrario(){
        System.out.println("\n---ORARIO DOCENTE: " + this.getCognome() + " ---");

        if(orarioGenerale == null){
            System.out.println("Errore: orario non esistente");
            return;
        }

        List<Lezione> mieLezioni = orarioGenerale.getLezioniPerDocente(this);

        if(mieLezioni.isEmpty()){
            System.out.println("Non ci sono lezioni a tuo nome");
        } else {
            for(Lezione l : mieLezioni){
                System.out.println(l.getGiorno() + " [" + l.getOraInizio() + "-" + l.getOraFine() + "] " +
                        l.getInsegnamento().getNome() + " - Aula: " + l.getAula().getNome());
            }
        }
    }

    /**
     * Crea e inoltra al sistema una richiesta formale per spostare una lezione esistente.
     * <p>
     * La richiesta viene generata con lo stato iniziale di {@code in_attesa} e viene
     * delegata all'oggetto {@link Orario} per la successiva approvazione da parte del responsabile.
     * </p>
     *
     * @param lezione     La lezione specifica che si desidera riprogrammare.
     * @param nuovoGiorno Il nuovo giorno della settimana proposto.
     * @param nuovoInizio Il nuovo orario di inizio proposto.
     * @param nuovaFine   Il nuovo orario di fine proposto.
     */
    public void richiediSpostamento(Lezione lezione, GiornoSettimana nuovoGiorno, LocalTime nuovoInizio, LocalTime nuovaFine){
        RichiestaSpostamento richiesta = new RichiestaSpostamento(nuovoGiorno, nuovoInizio, nuovaFine, StatoRichiesta.in_attesa, lezione);
        orarioGenerale.aggiungiRichiesta(richiesta);
        System.out.println("Richiesta di spostamento inviata per: " + lezione.getInsegnamento().getNome());
    }

    /**
     * Assegna un nuovo insegnamento alla lista delle materie tenute da questo docente.
     *
     * @param insegnamento L'oggetto che rappresenta l'insegnamento da aggiungere.
     */
    public void aggiungiInsegnamento(Insegnamento insegnamento){
        this.insegnamenti.add(insegnamento);
    }
}
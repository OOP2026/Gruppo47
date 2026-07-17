package model;
import java.time.LocalTime;

/**
 * Rappresenta una singola sessione didattica schedulata nell'orario.
 * <p>
 * Definisce l'intersezione tra un {@link Insegnamento}, un'{@link Aula}
 * e le coordinate temporali (giorno e fascia oraria) in cui l'evento ha luogo.
 * </p>
 */
public class Lezione {
    private GiornoSettimana giorno;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private Insegnamento insegnamento;
    private Aula aula;

    /**
     * Costruisce una nuova lezione programmata.
     *
     * @param giorno       Il giorno della settimana in cui si svolge la lezione.
     * @param oraInizio    L'orario esatto di inizio.
     * @param oraFine      L'orario esatto di fine.
     * @param insegnamento L'insegnamento (materia) trattato nella lezione.
     * @param aula         Lo spazio fisico/virtuale assegnato per lo svolgimento.
     */
    public Lezione(GiornoSettimana giorno, LocalTime oraInizio, LocalTime oraFine, Insegnamento insegnamento, Aula aula) {
        this.giorno = giorno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.insegnamento = insegnamento;
        this.aula = aula;
    }

    // --- GET E SET ---

    /**
     * Restituisce il giorno della settimana della lezione.
     *
     * @return Il giorno della settimana.
     */
    public GiornoSettimana getGiorno() {
        return giorno;
    }

    /**
     * Imposta il giorno della settimana per la lezione.
     *
     * @param giorno Il nuovo giorno della settimana.
     */
    public void setGiorno(GiornoSettimana giorno) {
        this.giorno = giorno;
    }

    /**
     * Restituisce l'orario di inizio della lezione.
     *
     * @return L'ora di inizio.
     */
    public LocalTime getOraInizio() {
        return oraInizio;
    }

    /**
     * Imposta l'orario di inizio della lezione.
     *
     * @param oraInizio La nuova ora di inizio.
     */
    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }

    /**
     * Restituisce l'orario di fine della lezione.
     *
     * @return L'ora di fine.
     */
    public LocalTime getOraFine() {
        return oraFine;
    }

    /**
     * Imposta l'orario di fine della lezione.
     *
     * @param oraFine La nuova ora di fine.
     */
    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
    }

    /**
     * Restituisce l'insegnamento associato alla lezione.
     *
     * @return L'insegnamento trattato.
     */
    public Insegnamento getInsegnamento() {
        return insegnamento;
    }

    /**
     * Imposta o modifica l'insegnamento della lezione.
     *
     * @param insegnamento Il nuovo insegnamento.
     */
    public void setInsegnamento(Insegnamento insegnamento) {
        this.insegnamento = insegnamento;
    }

    /**
     * Restituisce l'aula in cui si svolge la lezione.
     *
     * @return L'aula assegnata.
     */
    public Aula getAula() {
        return aula;
    }

    /**
     * Imposta o modifica l'aula per la lezione.
     *
     * @param aula La nuova aula.
     */
    public void setAula(Aula aula) {
        this.aula = aula;
    }
}
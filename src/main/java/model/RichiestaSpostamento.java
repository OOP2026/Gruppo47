package model;
import java.time.LocalTime;

/**
 * Rappresenta una richiesta formale per riprogrammare una {@link Lezione} esistente.
 * <p>
 * Mantiene traccia della lezione originale, delle nuove coordinate temporali proposte
 * e dello stato di avanzamento della richiesta (es. in attesa, approvata, rifiutata).
 * </p>
 */
public class RichiestaSpostamento {
    private GiornoSettimana giornoProposto;
    private LocalTime oraInizioProposta;
    private LocalTime oraFineProposta;
    private StatoRichiesta stato;
    private Lezione lezione;

    /**
     * Genera una nuova richiesta di spostamento.
     * <p>
     * Al momento della creazione, lo stato della richiesta viene forzato
     * in automatico a {@code StatoRichiesta.in_attesa}, indipendentemente
     * dal parametro passato nel costruttore.
     * </p>
     *
     * @param giornoProposto     Il nuovo giorno della settimana desiderato.
     * @param oraInizioProposta  La nuova ora di inizio proposta.
     * @param oraFineProposta    La nuova ora di fine proposta.
     * @param stato              Lo stato iniziale (sovrascritto internamente a "in attesa").
     * @param lezioneRiferimento La lezione originale che si desidera riprogrammare.
     */
    public RichiestaSpostamento(GiornoSettimana giornoProposto, LocalTime oraInizioProposta, LocalTime oraFineProposta, StatoRichiesta stato, Lezione lezioneRiferimento) {
        this.giornoProposto = giornoProposto;
        this.oraInizioProposta = oraInizioProposta;
        this.oraFineProposta = oraFineProposta;
        this.stato = StatoRichiesta.in_attesa;
        this.lezione = lezioneRiferimento;
    }

    // --- GET E SET ---

    /**
     * Restituisce il nuovo giorno proposto per la lezione.
     *
     * @return Il giorno della settimana proposto.
     */
    public GiornoSettimana getGiornoProposto() {
        return giornoProposto;
    }

    /**
     * Modifica il giorno proposto nella richiesta.
     *
     * @param giornoProposto Il nuovo giorno proposto.
     */
    public void setGiornoProposto(GiornoSettimana giornoProposto) {
        this.giornoProposto = giornoProposto;
    }

    /**
     * Restituisce l'orario di inizio proposto.
     *
     * @return La nuova ora di inizio.
     */
    public LocalTime getOraProposta() {
        return oraInizioProposta;
    }

    /**
     * Modifica l'orario di inizio proposto.
     *
     * @param oraProposta La nuova ora di inizio da salvare nella richiesta.
     */
    public void setOraProposta(LocalTime oraProposta) {
        this.oraInizioProposta = oraProposta;
    }

    /**
     * Restituisce l'orario di fine proposto.
     *
     * @return La nuova ora di fine.
     */
    public LocalTime getOraFineProposta() {
        return oraFineProposta;
    }

    /**
     * Modifica l'orario di fine proposto.
     *
     * @param oraFineProposta La nuova ora di fine.
     */
    public void setOraFineProposta(LocalTime oraFineProposta) {
        this.oraFineProposta = oraFineProposta;
    }

    /**
     * Restituisce lo stato attuale di avanzamento della richiesta.
     *
     * @return Lo stato (es. in attesa, approvata, rifiutata).
     */
    public StatoRichiesta getStato() {
        return stato;
    }

    /**
     * Aggiorna lo stato della richiesta (solitamente operato dal {@link Responsabile}).
     *
     * @param stato Il nuovo stato da assegnare.
     */
    public void setStato(StatoRichiesta stato) {
        this.stato = stato;
    }

    /**
     * Restituisce la lezione originale a cui questa richiesta fa riferimento.
     *
     * @return L'oggetto lezione originario.
     */
    public Lezione getLezione() {
        return lezione;
    }

    /**
     * Modifica la lezione a cui è associata la richiesta.
     *
     * @param lezione La nuova lezione di riferimento.
     */
    public void setLezione(Lezione lezione) {
        this.lezione = lezione;
    }
}
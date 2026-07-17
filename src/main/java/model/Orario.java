package model;
import java.util.List;
import java.util.ArrayList;

/**
 * Rappresenta il registro centrale del sistema, contenente tutte le lezioni
 * programmate e le richieste di spostamento pendenti.
 * <p>
 * Offre metodi per filtrare le lezioni (per studente o docente) e, soprattutto,
 * implementa le logiche per prevenire sovrapposizioni e conflitti durante
 * la schedulazione.
 * </p>
 */
public class Orario {
    private List<Lezione> lezioni;
    private List<RichiestaSpostamento> richiesteSpostamento = new ArrayList<>();

    /**
     * Inizializza un nuovo registro orario con liste vuote.
     */
    public Orario() {
        this.lezioni = new ArrayList<>();
    }

    // --- GET E SET ---

    /**
     * Restituisce la lista completa di tutte le lezioni programmate nel sistema.
     *
     * @return L'elenco di tutte le lezioni.
     */
    public List<Lezione> getLezioni() {
        return lezioni;
    }

    /**
     * Sostituisce l'intera lista delle lezioni programmate.
     *
     * @param lezioni La nuova lista di lezioni da registrare.
     */
    public void setLezioni(List<Lezione> lezioni) {
        this.lezioni = lezioni;
    }

    // --- METODI DI BUSINESS ---

    /**
     * Verifica se una nuova lezione entra in conflitto con le lezioni già presenti a sistema.
     * <p>
     * Vengono esaminati due casi specifici di sovrapposizione oraria nello stesso giorno:
     * </p>
     * <ul>
     *     <li><strong>Conflitto Aula:</strong> La stessa aula è occupata da un'altra lezione.</li>
     *     <li><strong>Conflitto Docente:</strong> Lo stesso docente è impegnato in un'altra lezione.</li>
     * </ul>
     *
     * @param nuova La lezione candidata all'inserimento.
     * @return {@code true} se viene rilevato almeno un conflitto, {@code false} se la lezione può essere inserita.
     */
    public boolean haConflitti(Lezione nuova) {
        for (Lezione esistente : lezioni) {
            // 1. Controllo stessa aula nello stesso giorno
            if (esistente.getGiorno().equals(nuova.getGiorno()) &&
                    esistente.getAula().equals(nuova.getAula())) {

                // Controllo sovrapposizione oraria con LocalTime
                if (nuova.getOraInizio().isBefore(esistente.getOraFine()) &&
                        nuova.getOraFine().isAfter(esistente.getOraInizio())) {
                    return true; // Conflitto Aula!
                }
            }

            // 2. Controllo stesso docente nello stesso giorno
            if (esistente.getGiorno().equals(nuova.getGiorno()) &&
                    esistente.getInsegnamento().getDocenteTitolare().equals(nuova.getInsegnamento().getDocenteTitolare())) {

                if (nuova.getOraInizio().isBefore(esistente.getOraFine()) &&
                        nuova.getOraFine().isAfter(esistente.getOraInizio())) {
                    return true; // Conflitto Docente!
                }
            }
        }
        return false;
    }

    /**
     * Aggiunge una nuova lezione al registro ufficiale dell'orario.
     *
     * @param l La lezione da inserire.
     */
    public void aggiungiLezione(Lezione l) {
        this.lezioni.add(l);
    }

    /**
     * Filtra e restituisce esclusivamente le lezioni destinate a un determinato anno accademico.
     * Utile per mostrare agli studenti solo l'orario di loro pertinenza.
     *
     * @param anno L'anno di corso per il quale filtrare le lezioni.
     * @return Una lista di lezioni previste per l'anno specificato.
     */
    public List<Lezione> getLezioniPerAnno(AnnoCorso anno){
        List<Lezione> filtrate = new ArrayList<>();
        for (Lezione l : lezioni) {
            if(l.getInsegnamento().getAnno().equals(anno)){
                filtrate.add(l);
            }
        }
        return filtrate;
    }

    /**
     * Filtra e restituisce esclusivamente le lezioni tenute da un determinato docente.
     * Utile per consentire ai docenti di visualizzare il proprio carico didattico.
     *
     * @param d Il docente per il quale filtrare le lezioni.
     * @return Una lista di lezioni in cui il docente figura come titolare.
     */
    public List<Lezione> getLezioniPerDocente(Docente d){
        List<Lezione> filtrate = new ArrayList<>();
        for(Lezione l : lezioni){
            if(l.getInsegnamento().getDocenteTitolare().equals(d)){
                filtrate.add(l);
            }
        }
        return filtrate;
    }

    /**
     * Registra una nuova richiesta formale di spostamento di una lezione.
     *
     * @param r La richiesta di spostamento da salvare a sistema.
     */
    public void aggiungiRichiesta(RichiestaSpostamento r){
        richiesteSpostamento.add(r);
    }

    /**
     * Restituisce l'elenco di tutte le richieste di spostamento presentate dai docenti.
     *
     * @return La lista delle richieste di spostamento.
     */
    public List<RichiestaSpostamento> getRichiesteSpostamento() {
        return richiesteSpostamento;
    }

    /**
     * Rimuove definitivamente una lezione dall'orario.
     *
     * @param l La lezione da eliminare.
     */
    public void rimuoviLezione(Lezione l) {
        this.lezioni.remove(l);
    }
}
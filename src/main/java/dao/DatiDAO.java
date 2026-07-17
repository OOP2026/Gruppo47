package dao;

import model.*;
import java.util.List;
import java.time.LocalTime;

/**
 * Interfaccia per l'accesso ai dati (Data Access Object) relativi alla struttura dell'orario.
 * <p>
 * Definisce il contratto per le operazioni di persistenza (salvataggio, caricamento, aggiornamento
 * e cancellazione) di Aule, Insegnamenti, Lezioni e Richieste di Spostamento.
 * Isola la logica di business (il Controller) dai dettagli implementativi del database
 * (es. query SQL), permettendo di cambiare tecnologia di storage senza modificare il resto del codice.
 * </p>
 */
public interface DatiDAO {

    // ── Gestione Aule ─────────────────────────────────────────────────────────

    /**
     * Salva una nuova aula all'interno del database.
     *
     * @param aula L'oggetto {@link Aula} da rendere persistente.
     */
    void salvaAula(Aula aula);

    /**
     * Recupera l'elenco completo di tutte le aule registrate nel sistema.
     *
     * @return Una lista contenente tutte le aule disponibili.
     */
    List<Aula> caricaAule();

    // ── Gestione Insegnamenti ─────────────────────────────────────────────────

    /**
     * Salva un nuovo insegnamento all'interno del database.
     *
     * @param ins L'oggetto {@link Insegnamento} da rendere persistente.
     */
    void salvaInsegnamento(Insegnamento ins);

    /**
     * Recupera tutti gli insegnamenti dal database e li collega ai rispettivi docenti titolari.
     *
     * @param docentiDisponibili La lista dei docenti già caricata in memoria, necessaria per
     *                           ripristinare l'associazione tra l'insegnamento e il suo titolare.
     * @return L'elenco di tutti gli insegnamenti.
     */
    List<Insegnamento> caricaInsegnamenti(List<Docente> docentiDisponibili);

    // ── Gestione Lezioni ──────────────────────────────────────────────────────

    /**
     * Salva una nuova lezione programmata all'interno del database.
     *
     * @param lezione L'oggetto {@link Lezione} da rendere persistente.
     */
    void salvaLezione(Lezione lezione);

    /**
     * Recupera l'elenco completo delle lezioni dal database, ricostruendo i riferimenti
     * alle entità collegate (insegnamenti e aule).
     *
     * @param insegnamenti La lista degli insegnamenti in memoria, usata per associare la lezione alla sua materia.
     * @param aule         La lista delle aule in memoria, usata per assegnare il luogo fisico alla lezione.
     * @return L'elenco di tutte le lezioni programmate.
     */
    List<Lezione> caricaLezioni(List<Insegnamento> insegnamenti, List<Aula> aule);

    // ── Gestione Richieste e Aggiornamenti ────────────────────────────────────

    /**
     * Salva una nuova richiesta di spostamento inviata da un docente.
     *
     * @param richiesta L'oggetto {@link RichiestaSpostamento} da registrare nel sistema.
     */
    void salvaRichiesta(RichiestaSpostamento richiesta);

    /**
     * Recupera tutte le richieste di spostamento dal database (sia in attesa che storicizzate).
     *
     * @param lezioni La lista delle lezioni attive in memoria, necessaria per collegare
     *                ogni richiesta alla lezione che si intende spostare.
     * @return L'elenco completo delle richieste.
     */
    List<RichiestaSpostamento> caricaRichieste(List<Lezione> lezioni);

    /**
     * Aggiorna lo stato di una richiesta esistente (es. da 'in_attesa' a 'approvata' o 'rifiutata').
     *
     * @param richiesta L'oggetto richiesta contenente il nuovo stato da persistere.
     */
    void aggiornaStatoRichiesta(RichiestaSpostamento richiesta);

    /**
     * Modifica in modo permanente le coordinate temporali di una lezione nel database
     * a seguito dell'approvazione di uno spostamento.
     *
     * @param vecchiaLezione La lezione preesistente da aggiornare.
     * @param nuovoGiorno    Il nuovo giorno della settimana assegnato.
     * @param nuovoInizio    Il nuovo orario di inizio.
     * @param nuovaFine      Il nuovo orario di fine.
     */
    void aggiornaLezioneDopoSpostamento(Lezione vecchiaLezione, GiornoSettimana nuovoGiorno, LocalTime nuovoInizio, LocalTime nuovaFine);

    // ── Eliminazione lezioni ──────────────────────────────────────────────────

    /**
     * Rimuove definitivamente una lezione dal database.
     *
     * @param lezione La lezione da eliminare.
     */
    void eliminaLezione(Lezione lezione);
}
package dao;
import model.Utente;
import model.Docente;
import java.util.List;

/**
 * Interfaccia per l'accesso ai dati (Data Access Object) relativi agli account utente.
 * <p>
 * Gestisce l'autenticazione (login) e il recupero delle anagrafiche legate agli attori
 * del sistema, garantendo che il resto dell'applicazione sia indipendente dalle logiche SQL.
 * </p>
 */
public interface UtenteDAO {

    /**
     * Verifica le credenziali di accesso interrogate al database.
     * <p>
     * Se l'autenticazione ha successo, il metodo costruisce e restituisce l'istanza corretta
     * della sottoclasse di appartenenza ({@link model.Studente}, {@link model.Docente}
     * o {@link model.Responsabile}) in base al ruolo dell'utente trovato nel DB.
     * </p>
     *
     * @param login    L'identificativo univoco (username) inserito dall'utente.
     * @param password La password inserita.
     * @return Un oggetto di tipo {@link Utente} se le credenziali sono valide; {@code null} se non corrispondono a nessun account.
     */
    Utente verificaLogin(String login, String password);

    /**
     * Recupera l'elenco completo di tutti i docenti registrati nel sistema.
     * <p>
     * Utilizzato principalmente in fase di avvio per popolare la memoria RAM e
     * per permettere l'associazione dei docenti ai rispettivi insegnamenti.
     * </p>
     *
     * @return Una lista di oggetti {@link Docente}.
     */
    List<Docente> caricaTuttiDocenti();
}
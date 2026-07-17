package model;

/**
 * Questa classe astratta rappresenta un utente generico all'interno del sistema di gestione dell'orario universitario.
 * <p>
 * In questa classe vengono definite le informazioni anagrafiche e le credenziali
 * di accesso degli utenti (Studenti, Docenti e Responsabili).
 * </p>
 *
 * @author Giovanni Miniero
 * @version 1.0
 */
abstract public class Utente {
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String login;

    /**
     * Costruisce un nuovo utente inizializzando i suoi dati anagrafici e di accesso.
     *
     * @param nome     Il nome di battesimo dell'utente.
     * @param cognome  Il cognome dell'utente.
     * @param email    L'indirizzo email istituzionale.
     * @param password La password per l'accesso.
     * @param login    Lo username per l'accesso.
     */
    public Utente(String nome, String cognome, String email, String password, String login){
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.login = login;
    }

    // --- GET E SET ---

    /**
     * Restituisce il nome dell'utente.
     *
     * @return Il nome dell'utente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta o modifica il nome dell'utente.
     *
     * @param nome Il nuovo nome da assegnare.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return Il cognome dell'utente.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta o modifica il cognome dell'utente.
     *
     * @param cognome Il nuovo cognome da assegnare.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce l'indirizzo email dell'utente.
     *
     * @return L'email dell'utente.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta o modifica l'indirizzo email dell'utente.
     *
     * @param email Il nuovo indirizzo email da assegnare.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Restituisce la password dell'utente.
     *
     * @return La password attuale.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Imposta o modifica la password dell'utente.
     *
     * @param password La nuova password da assegnare.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Restituisce lo username (login) dell'utente.
     *
     * @return L'identificativo di login.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Imposta o modifica lo username (login) dell'utente.
     *
     * @param login Il nuovo identificativo univoco da assegnare.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Verifica se le credenziali inserite corrispondono a quelle dell'utente.
     *
     * @param login    Lo username inserito in fase di autenticazione.
     * @param password La password inserita in fase di autenticazione.
     * @return {@code true} se sia il login che la password coincidono con i dati memorizzati,
     *         {@code false} altrimenti.
     */
    public boolean login(String login, String password) {
        return ( login.equals(this.login) && password.equals(this.password));
    }

    /**
     * Confronta questo utente con un altro oggetto per verificarne l'uguaglianza.
     * <p>
     * L'uguaglianza logica tra due utenti nel sistema è determinata unicamente
     * dal campo {@code login}, che funge da chiave primaria (identificatore univoco).
     * </p>
     *
     * @param obj L'oggetto con cui confrontare l'utente corrente.
     * @return {@code true} se gli oggetti rappresentano lo stesso utente (hanno lo stesso login),
     *         {@code false} altrimenti o se l'oggetto passato è nullo o di tipo diverso.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Utente altroUtente = (Utente) obj;
        return this.login.equals(altroUtente.login);
    }
}
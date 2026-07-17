package model;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;

/**
 * Rappresenta il responsabile della gestione dell'orario universitario.
 * <p>
 * Questa classe estende {@link Docente} (e di conseguenza {@link Utente}),
 * aggiungendo i privilegi di amministrazione. Il Responsabile ha il compito di:
 * </p>
 * <ul>
 *     <li>Definire le aule, i docenti e gli insegnamenti disponibili.</li>
 *     <li>Creare le lezioni assicurandosi che non ci siano sovrapposizioni.</li>
 *     <li>Rilevare eventuali conflitti nell'orario generale.</li>
 *     <li>Approvare o rifiutare le richieste di spostamento inviate dagli altri docenti.</li>
 * </ul>
 *
 * @see Docente
 */
public class Responsabile extends Docente {
    private List<Aula> elencoAule;
    private List<Insegnamento> elencoInsegnamenti;
    private List<Docente> elencoDocenti;
    private Orario orarioGenerale;

    /**
     * Costruisce un nuovo Responsabile, inizializzando le liste di gestione e l'orario generale.
     *
     * @param nome     Il nome del responsabile.
     * @param cognome  Il cognome del responsabile.
     * @param mail     L'indirizzo email.
     * @param password La password per l'accesso.
     * @param login    Lo username per l'autenticazione.
     * @param orario   Il sistema di gestione dell'orario generale dell'università.
     */
    public Responsabile(String nome, String cognome, String mail, String password, String login, Orario orario){
        super(nome, cognome, mail, password, login, orario);
        this.elencoAule = new ArrayList<>();
        this.elencoInsegnamenti = new ArrayList<>();
        this.elencoDocenti = new ArrayList<>();
        this.orarioGenerale = orario;
    }

    // --- GET E SET ---

    /**
     * Restituisce la lista di tutte le aule registrate nel sistema.
     *
     * @return Una lista di oggetti {@link Aula}.
     */
    public List<Aula> getelencoAule(){
        return elencoAule;
    }

    /**
     * Imposta o sostituisce l'elenco delle aule disponibili.
     *
     * @param elencoAule La nuova lista di aule.
     */
    public void setelencoAule(List<Aula> elencoAule){
        this.elencoAule = elencoAule;
    }

    /**
     * Restituisce la lista di tutti gli insegnamenti attivati.
     *
     * @return Una lista di oggetti {@link Insegnamento}.
     */
    public List<Insegnamento> getelencoInsegnamenti(){
        return elencoInsegnamenti;
    }

    /**
     * Imposta o sostituisce l'elenco degli insegnamenti.
     *
     * @param elencoInsegnamenti La nuova lista di insegnamenti.
     */
    public void setelencoInsegnamenti(List<Insegnamento> elencoInsegnamenti){
        this.elencoInsegnamenti = elencoInsegnamenti;
    }

    /**
     * Restituisce la lista di tutti i docenti registrati nel sistema.
     *
     * @return Una lista di oggetti {@link Docente}.
     */
    public List<Docente> getelencoDocenti(){
        return this.elencoDocenti;
    }

    /**
     * Aggiunge un nuovo docente alla lista dei docenti gestiti dal responsabile.
     *
     * @param d Il docente da aggiungere al sistema.
     */
    public void aggiungiDocente(Docente d){
        this.elencoDocenti.add(d);
    }


    // --- METODI DI GESTIONE ---

    /**
     * Crea un nuovo insegnamento e lo aggiunge al registro del sistema.
     *
     * @param nome     Il nome dell'insegnamento (es. "Programmazione Orientata agli Oggetti").
     * @param CFU      I Crediti Formativi Universitari assegnati alla materia.
     * @param anno     L'anno di corso in cui la materia viene erogata.
     * @param titolare Il docente responsabile dell'insegnamento.
     */
    public void definisciInsegnamento(String nome, int CFU, AnnoCorso anno, Docente titolare){
        Insegnamento nuovoInsegnamento = new Insegnamento(nome, CFU, anno, titolare);
        this.elencoInsegnamenti.add(nuovoInsegnamento);
        System.out.println("Insegnamento " + nome + " aggiunto con successo");
    }

    /**
     * Crea una nuova aula e la rende disponibile per l'assegnazione delle lezioni.
     *
     * @param nomeAula Il nome o codice identificativo dell'aula (es. "Aula Magna", "F1").
     */
    public void inserisciAula(String nomeAula){
        Aula nuovaAula = new Aula(nomeAula);
        this.elencoAule.add(nuovaAula);
    }

    /**
     * Programma una nuova lezione all'interno dell'orario generale.
     * <p>
     * Prima di confermare l'inserimento, il metodo verifica l'assenza di conflitti
     * interrogando il sistema orario. Se viene rilevata una sovrapposizione,
     * la lezione non viene inserita e viene stampato un messaggio di errore.
     * </p>
     *
     * @param ins    L'insegnamento oggetto della lezione.
     * @param g      Il giorno della settimana in cui si terrà la lezione.
     * @param inizio L'orario di inizio della lezione.
     * @param fine   L'orario di fine della lezione.
     * @param aula   L'aula in cui si svolgerà la lezione.
     */
    public void creaLezione(Insegnamento ins, GiornoSettimana g, LocalTime inizio, LocalTime fine, Aula aula){
        Lezione nuovaLezione = new Lezione(g, inizio, fine, ins, aula);

        if(!orarioGenerale.haConflitti(nuovaLezione)){
            orarioGenerale.aggiungiLezione(nuovaLezione);
            System.out.println("Lezione aggiunta correttamente");
        } else {
            System.out.println("Lezione non aggiunta");
        }
    }

    /**
     * Stampa a schermo l'elenco di tutte le richieste di spostamento pervenute al sistema.
     * Mostra per ogni richiesta la materia, la nuova proposta oraria e lo stato
     * di approvazione attuale.
     */
    public void visualizzaRichiestaSpostamento(){
        System.out.println("\n---ELENCO RICHIESTE SPOSTAMENTO---");
        for(RichiestaSpostamento r : orarioGenerale.getRichiesteSpostamento()){
            System.out.println("Lezione: " + r.getLezione().getInsegnamento().getNome() +
                    "| Proposta: " + r.getGiornoProposto() + " ore " + r.getOraProposta() +
                    "| Stato: " + r.getStato());
        }
    }

    /**
     * Esegue una scansione completa di tutte le lezioni presenti nell'orario per rilevare anomalie.
     * <p>
     * Il metodo segnala due tipi di conflitti:
     * </p>
     * <ul>
     *     <li><strong>Conflitto Aula:</strong> Due lezioni si svolgono contemporaneamente nella stessa aula.</li>
     *     <li><strong>Conflitto Docente:</strong> Lo stesso docente è assegnato a due lezioni che si sovrappongono.</li>
     * </ul>
     */
    public void visualizzaConflitti(){
        System.out.println("\n--- VERIFICA CONFLITTI ORARIO ---");
        boolean conflittiTrovati = false;
        List<Lezione> lezioni = orarioGenerale.getLezioni();

        for (int i = 0; i < lezioni.size(); i++) {
            for (int j = i + 1; j < lezioni.size(); j++) {
                Lezione l1 = lezioni.get(i);
                Lezione l2 = lezioni.get(j);

                if (l1.getGiorno().equals(l2.getGiorno())) {
                    boolean sovrapposizioneOraria = l1.getOraInizio().isBefore(l2.getOraFine()) &&
                            l1.getOraFine().isAfter(l2.getOraInizio());

                    if (sovrapposizioneOraria) {
                        if (l1.getAula().equals(l2.getAula())) {
                            System.out.println("CONFLITTO AULA [" + l1.getAula().getNome() + "]: " +
                                    l1.getInsegnamento().getNome() + " e " + l2.getInsegnamento().getNome());
                            conflittiTrovati = true;
                        }
                        if (l1.getInsegnamento().getDocenteTitolare().equals(l2.getInsegnamento().getDocenteTitolare())) {
                            System.out.println("CONFLITTO DOCENTE [" + l1.getInsegnamento().getDocenteTitolare().getCognome() + "]: " +
                                    l1.getInsegnamento().getNome() + " e " + l2.getInsegnamento().getNome());
                            conflittiTrovati = true;
                        }
                    }
                }
            }
        }
        if (!conflittiTrovati) System.out.println("Nessun conflitto rilevato.");
    }

    /**
     * Valuta e processa una richiesta di spostamento inviata da un docente.
     * <p>
     * Se il responsabile decide di approvare, il metodo esegue una simulazione:
     * calcola il nuovo orario di fine (mantenendo inalterata la durata originale della lezione),
     * rimuove virtualmente la lezione attuale per evitare falsi positivi su sé stessa e controlla
     * se la nuova collocazione genera conflitti. L'approvazione è finalizzata solo se l'esito è positivo.
     * </p>
     *
     * @param richiesta L'oggetto contenente i dettagli della proposta di spostamento.
     * @param approva   {@code true} per tentare l'approvazione e lo spostamento;
     *                  {@code false} per rifiutare la richiesta direttamente.
     */
    public void gestisciRichiestaSpostamento(RichiestaSpostamento richiesta, boolean approva){
        if (approva) {
            // Calcoliamo l'ora di fine basandoci sulla durata originale della lezione
            long durataMinuti = java.time.Duration.between(richiesta.getLezione().getOraInizio(), richiesta.getLezione().getOraFine()).toMinutes();
            LocalTime nuovaFine = richiesta.getOraProposta().plusMinutes(durataMinuti);

            // Creiamo una lezione temporanea per testare i conflitti
            Lezione ipotesi = new Lezione(richiesta.getGiornoProposto(), richiesta.getOraProposta(), nuovaFine,
                    richiesta.getLezione().getInsegnamento(), richiesta.getLezione().getAula());

            // Verifichiamo se lo spostamento è possibile (escludendo la lezione originale dal controllo)
            orarioGenerale.rimuoviLezione(richiesta.getLezione());

            if (!orarioGenerale.haConflitti(ipotesi)) {
                modificaOrario(richiesta.getLezione(), richiesta.getGiornoProposto(), richiesta.getOraProposta(), nuovaFine);
                richiesta.setStato(StatoRichiesta.approvata);
                System.out.println("Richiesta approvata e orario aggiornato.");
            } else {
                // Se c'è conflitto, rimettiamo la lezione originale e rifiutiamo o segnaliamo
                orarioGenerale.aggiungiLezione(richiesta.getLezione());
                System.out.println("Impossibile approvare: lo spostamento genera conflitti.");
            }
        } else {
            richiesta.setStato(StatoRichiesta.rifiutata);
            System.out.println("Richiesta rifiutata.");
        }
    }

    /**
     * Applica le modifiche a una lezione esistente, aggiornandone giorno e orario.
     * <p>
     * Se la lezione non è attualmente presente nell'orario generale (ad esempio
     * perché rimossa temporaneamente durante una verifica di conflitti), viene reinserita.
     * </p>
     *
     * @param lezione La lezione da modificare.
     * @param g       Il nuovo giorno in cui si terrà la lezione.
     * @param inizio  Il nuovo orario di inizio.
     * @param fine    Il nuovo orario di fine.
     */
    public void modificaOrario(Lezione lezione, GiornoSettimana g, LocalTime inizio, LocalTime fine) {
        lezione.setGiorno(g);
        lezione.setOraInizio(inizio);
        lezione.setOraFine(fine);
        // Se non era già presente (es. chiamata da gestisciRichiesta), la riaggiungiamo
        if (!orarioGenerale.getLezioni().contains(lezione)) {
            orarioGenerale.aggiungiLezione(lezione);
        }
    }
}
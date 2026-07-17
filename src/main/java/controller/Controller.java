package controller;

import model.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import dao.UtenteDAO;
import implementazioneDao.UtentePostgresDao;
import dao.DatiDAO;
import implementazioneDao.DatiPostgresDao;
import java.util.Comparator;

/**
 * Gestisce la logica di business dell'applicazione, fungendo da intermediario (Controller)
 * tra l'interfaccia grafica (Boundary) e i dati (Entity e Database).
 * <p>
 * Questa classe coordina tutte le operazioni principali del sistema:
 * </p>
 * <ul>
 *     <li>Gestione dell'orario e rilevamento dei conflitti.</li>
 *     <li>Creazione di lezioni, insegnamenti e aule, sincronizzandole sia in memoria (RAM) che nel Database.</li>
 *     <li>Gestione dell'invio e dell'approvazione delle richieste di spostamento.</li>
 * </ul>
 *
 * @see Orario
 * @see DatiDAO
 * @see UtenteDAO
 */
public class Controller {

	private Orario orario;
	private List<Docente> docenti = new ArrayList<>();
	private List<Aula> aule = new ArrayList<>();
	private List<Insegnamento> insegnamenti = new ArrayList<>();
	private UtenteDAO utenteDao;
	private DatiDAO datiDao;

	/**
	 * Inizializza il Controller collegandolo all'orario generale e istanziando i DAO per il database.
	 *
	 * @param orario L'istanza principale dell'{@link Orario} (modello dati in memoria).
	 */
	public Controller(Orario orario) {
		this.orario = orario;
		this.utenteDao = new UtentePostgresDao();
		this.datiDao = new DatiPostgresDao();
	}

	// ── Gestione Orario ───────────────────────────────────────────────────────

	/**
	 * Tenta di creare una nuova lezione e di salvarla nel sistema.
	 * <p>
	 * Il metodo valida i formati di input, verifica l'esistenza dell'insegnamento e dell'aula,
	 * controlla la congruenza degli orari e si assicura che non ci siano conflitti nell'orario.
	 * Se tutti i controlli vengono superati, la lezione viene salvata sia in RAM che nel DB.
	 * </p>
	 *
	 * @param nomeInsegnamento Il nome della materia.
	 * @param giornoStr        Il giorno della settimana sotto forma di stringa.
	 * @param oraInizioStr     L'orario di inizio nel formato HH:MM.
	 * @param oraFineStr       L'orario di fine nel formato HH:MM.
	 * @param nomeAula         Il nome dell'aula selezionata.
	 * @return {@code null} se l'operazione ha successo; altrimenti una stringa contenente il messaggio di errore.
	 */
	public String creaLezione(String nomeInsegnamento, String giornoStr,
							  String oraInizioStr, String oraFineStr, String nomeAula) {
		if (nomeInsegnamento == null || nomeInsegnamento.trim().isEmpty())
			return "Seleziona un insegnamento.";
		if (nomeAula == null || nomeAula.trim().isEmpty())
			return "Seleziona un'aula.";

		Insegnamento ins = trovaInsegnamento(nomeInsegnamento);
		if (ins == null) return "Insegnamento non trovato.";

		Aula aula = trovaAula(nomeAula);
		if (aula == null) return "Aula non trovata.";

		GiornoSettimana giorno;
		try {
			giorno = GiornoSettimana.valueOf(giornoStr);
		} catch (IllegalArgumentException e) {
			return "Giorno non valido.";
		}

		LocalTime inizio, fine;
		try {
			inizio = LocalTime.parse(oraInizioStr);
			fine   = LocalTime.parse(oraFineStr);
		} catch (Exception e) {
			return "Formato orario non valido (usa HH:MM).";
		}

		if (!fine.isAfter(inizio))
			return "L'ora di fine deve essere successiva all'ora di inizio.";

		Lezione nuova = new Lezione(giorno, inizio, fine, ins, aula);
		if (orario.haConflitti(nuova))
			return "Conflitto: aula occupata o docente già impegnato in quell'orario.";

		orario.aggiungiLezione(nuova);
		datiDao.salvaLezione(nuova);
		return null;
	}

	/**
	 * Aggiunge una nuova aula al sistema, verificando che non esista già un'aula con lo stesso nome.
	 *
	 * @param nomeAula Il nome dell'aula da creare.
	 * @return {@code null} in caso di successo, altrimenti il messaggio di errore.
	 */
	public String aggiungiAula(String nomeAula) {
		if (nomeAula == null || nomeAula.trim().isEmpty())
			return "Il nome dell'aula non può essere vuoto.";

		for (Aula a : this.aule) {
			if (a.getNome().equalsIgnoreCase(nomeAula)) {
				return "Aula già presente.";
			}
		}

		Aula nuovaAula = new Aula(nomeAula);
		this.aule.add(nuovaAula);
		datiDao.salvaAula(nuovaAula);
		return null;
	}

	/**
	 * Crea un nuovo insegnamento, validando i dati in ingresso, e lo salva nel database e in memoria.
	 *
	 * @param nome     Il nome dell'insegnamento.
	 * @param cfu      I Crediti Formativi Universitari (devono essere > 0).
	 * @param anno     L'anno di corso associato.
	 * @param titolare Il docente responsabile dell'insegnamento.
	 * @return {@code null} in caso di successo, altrimenti il messaggio di errore.
	 */
	public String aggiungiInsegnamento(String nome, int cfu, AnnoCorso anno, Docente titolare) {
		if (nome == null || nome.trim().isEmpty()) return "Il nome non può essere vuoto.";
		if (cfu <= 0)      return "I CFU devono essere positivi.";
		if (titolare == null) return "Seleziona un docente titolare.";

		Insegnamento nuovoInsegnamento = new Insegnamento(nome, cfu, anno, titolare);
		this.insegnamenti.add(nuovoInsegnamento);
		datiDao.salvaInsegnamento(nuovoInsegnamento);
		return null;
	}

	/**
	 * Restituisce tutte le lezioni attualmente presenti nell'orario generale.
	 *
	 * @return Una lista contenente tutte le lezioni programmate.
	 */
	public List<Lezione> getLezioni() {
		return orario.getLezioni();
	}

	/**
	 * Restituisce l'orario filtrato per uno specifico anno di corso.
	 *
	 * @param anno L'anno di corso da filtrare.
	 * @return La lista delle lezioni previste per quell'anno.
	 */
	public List<Lezione> getLezioniPerAnno(AnnoCorso anno) {
		return orario.getLezioniPerAnno(anno);
	}

	/**
	 * Restituisce le lezioni di cui un docente è titolare.
	 *
	 * @param docente Il docente per il quale cercare le lezioni.
	 * @return La lista delle lezioni tenute dal docente.
	 */
	public List<Lezione> getLezioniPerDocente(Docente docente) {
		return orario.getLezioniPerDocente(docente);
	}

	/**
	 * Metodo di convenienza. Richiama {@link #getLezioniPerDocente(Docente)}.
	 *
	 * @param docente Il docente da filtrare.
	 * @return La lista delle lezioni tenute dal docente.
	 */
	public List<Lezione> getLezioniDocente(Docente docente) {
		return orario.getLezioniPerDocente(docente);
	}

	// ── Gestione Richieste ────────────────────────────────────────────────────

	/**
	 * Valida e inoltra una nuova richiesta di spostamento per una determinata lezione.
	 * Controlla che non ci siano altre richieste in attesa per la medesima lezione prima dell'inserimento.
	 *
	 * @param lezione      La lezione originale da spostare.
	 * @param giornoStr    Il nuovo giorno proposto in formato stringa.
	 * @param oraInizioStr Il nuovo orario di inizio in formato HH:MM.
	 * @param oraFineStr   Il nuovo orario di fine in formato HH:MM.
	 * @return {@code null} se inviata con successo, altrimenti il messaggio di errore.
	 */
	public String inviaRichiesta(Lezione lezione, String giornoStr,
								 String oraInizioStr, String oraFineStr) {
		if (lezione == null) return "Seleziona una lezione da spostare.";

		GiornoSettimana giorno;
		try {
			giorno = GiornoSettimana.valueOf(giornoStr);
		} catch (IllegalArgumentException e) {
			return "Giorno non valido.";
		}

		LocalTime inizio, fine;
		try {
			inizio = LocalTime.parse(oraInizioStr);
			fine   = LocalTime.parse(oraFineStr);
		} catch (Exception e) {
			return "Formato orario non valido (usa HH:MM).";
		}

		if (!fine.isAfter(inizio))
			return "L'ora di fine deve essere successiva all'ora di inizio.";

		for (RichiestaSpostamento r : orario.getRichiesteSpostamento())
			if (r.getLezione().equals(lezione) && r.getStato() == StatoRichiesta.in_attesa)
				return "Esiste già una richiesta in attesa per questa lezione.";

		RichiestaSpostamento nuovaRichiesta = new RichiestaSpostamento(giorno, inizio, fine, StatoRichiesta.in_attesa, lezione);
		orario.aggiungiRichiesta(nuovaRichiesta);
		datiDao.salvaRichiesta(nuovaRichiesta);
		return null;
	}

	/**
	 * Valuta una richiesta di spostamento, approvandola o rifiutandola.
	 * <p>
	 * Se approvata, simula il nuovo orario per verificare l'assenza di conflitti. Se la simulazione
	 * ha esito positivo, aggiorna persistentemente la lezione nel Database e nella memoria, e segna la
	 * richiesta come {@link StatoRichiesta#approvata}. Altrimenti, annulla l'operazione e imposta
	 * lo stato in {@link StatoRichiesta#rifiutata}.
	 * </p>
	 *
	 * @param richiesta La richiesta pendente da elaborare.
	 * @param approva   {@code true} per approvare lo spostamento, {@code false} per respingerlo direttamente.
	 * @return Un messaggio testuale che descrive l'esito dell'operazione.
	 */
	public String gestisciRichiesta(RichiestaSpostamento richiesta, boolean approva) {
		if (richiesta == null) return "Nessuna richiesta selezionata.";
		if (richiesta.getStato() != StatoRichiesta.in_attesa)
			return "La richiesta è già stata elaborata (stato: " + richiesta.getStato() + ").";

		if (approva) {
			long durataMin = java.time.Duration.between(
					richiesta.getLezione().getOraInizio(),
					richiesta.getLezione().getOraFine()).toMinutes();
			LocalTime nuovaFine = richiesta.getOraProposta().plusMinutes(durataMin);

			Lezione ipotesi = new Lezione(
					richiesta.getGiornoProposto(),
					richiesta.getOraProposta(), nuovaFine,
					richiesta.getLezione().getInsegnamento(),
					richiesta.getLezione().getAula());

			orario.rimuoviLezione(richiesta.getLezione());

			if (!orario.haConflitti(ipotesi)) {
				// 1. AGGIORNA LA LEZIONE SUL DB
				datiDao.aggiornaLezioneDopoSpostamento(richiesta.getLezione(), richiesta.getGiornoProposto(), richiesta.getOraProposta(), nuovaFine);

				// 2. AGGIORNA LA LEZIONE IN RAM (Sostituisce il vecchio 'responsabile.modificaOrario')
				richiesta.getLezione().setGiorno(richiesta.getGiornoProposto());
				richiesta.getLezione().setOraInizio(richiesta.getOraProposta());
				richiesta.getLezione().setOraFine(nuovaFine);
				orario.aggiungiLezione(richiesta.getLezione());

				// 3. AGGIORNA LO STATO DELLA RICHIESTA
				richiesta.setStato(StatoRichiesta.approvata);
				datiDao.aggiornaStatoRichiesta(richiesta);

				return "Richiesta approvata: orario aggiornato.";
			} else {
				// Se c'è conflitto, rimettiamo la lezione dov'era prima
				orario.aggiungiLezione(richiesta.getLezione());
				richiesta.setStato(StatoRichiesta.rifiutata);
				datiDao.aggiornaStatoRichiesta(richiesta);
				return "Impossibile approvare: genera conflitti. Richiesta rifiutata.";
			}
		} else {
			richiesta.setStato(StatoRichiesta.rifiutata);
			datiDao.aggiornaStatoRichiesta(richiesta);
			return "Richiesta rifiutata.";
		}
	}

	/**
	 * Restituisce tutte le richieste di spostamento storicizzate e in attesa.
	 *
	 * @return La lista completa delle richieste di spostamento.
	 */
	public List<RichiestaSpostamento> getRichieste() {
		return orario.getRichiesteSpostamento();
	}

	// ── Visualizzazione ───────────────────────────────────────────────────────

	/**
	 * Recupera l'orario specifico per uno studente, basato sul suo anno di corso.
	 *
	 * @param studente Lo studente per il quale filtrare l'orario.
	 * @return La lista delle lezioni programmate per l'anno dello studente.
	 */
	public List<Lezione> getOrarioStudente(Studente studente) {
		if (studente == null) return java.util.Collections.emptyList();
		return orario.getLezioniPerAnno(studente.getAnnoS());
	}

	// ── Dati di supporto per le Boundary ─────────────────────────────────────

	/**
	 * Restituisce l'elenco delle aule salvate in RAM.
	 *
	 * @return La lista di tutte le aule disponibili.
	 */
	public List<Aula> getAule() {
		return this.aule;
	}

	/**
	 * Restituisce l'elenco degli insegnamenti salvati in RAM.
	 *
	 * @return La lista di tutti gli insegnamenti.
	 */
	public List<Insegnamento> getInsegnamenti() {
		return this.insegnamenti;
	}

	/**
	 * Restituisce l'elenco dei docenti salvati in RAM.
	 *
	 * @return La lista di tutti i docenti registrati.
	 */
	public List<Docente> getDocenti() {
		return this.docenti;
	}

	/**
	 * Restituisce l'elenco dei giorni della settimana validi per l'orario accademico.
	 *
	 * @return Array enumerato dei giorni della settimana.
	 */
	public GiornoSettimana[] getGiorni() {
		return GiornoSettimana.values();
	}

	/**
	 * Restituisce l'elenco degli anni di corso validi.
	 *
	 * @return Array enumerato degli anni di corso (es. I, II, III).
	 */
	public AnnoCorso[] getAnni() {
		return AnnoCorso.values();
	}

	// ── Helper privati / Operazioni di base ───────────────────────────────────

	/**
	 * Cerca un insegnamento in memoria in base al nome (ignorando maiuscole/minuscole).
	 *
	 * @param nome Il nome dell'insegnamento da cercare.
	 * @return L'oggetto {@link Insegnamento} se trovato, {@code null} altrimenti.
	 */
	public Insegnamento trovaInsegnamento(String nome) {
		for (Insegnamento i : this.insegnamenti) {
			if (i.getNome().equalsIgnoreCase(nome)) {
				return i;
			}
		}
		return null;
	}

	/**
	 * Cerca un'aula in memoria in base al nome (ignorando maiuscole/minuscole).
	 *
	 * @param nome Il nome dell'aula da cercare.
	 * @return L'oggetto {@link Aula} se trovato, {@code null} altrimenti.
	 */
	public Aula trovaAula(String nome) {
		for (Aula a : this.aule) {
			if (a.getNome().equalsIgnoreCase(nome)) {
				return a;
			}
		}
		return null;
	}

	/**
	 * Delega al Data Access Object l'autenticazione dell'utente tramite DB.
	 *
	 * @param username Lo username inserito dall'utente.
	 * @param password La password inserita dall'utente.
	 * @return Un oggetto di tipo {@link Utente} se l'autenticazione va a buon fine, {@code null} altrimenti.
	 */
	public Utente verificaLogin(String username, String password) {
		return utenteDao.verificaLogin(username, password);
	}

	/**
	 * Sincronizza lo stato in RAM del sistema con i dati presenti nel Database.
	 * <p>
	 * Questo metodo viene richiamato all'avvio dell'applicazione. Interroga i DAO per recuperare
	 * docenti, aule, insegnamenti, lezioni e richieste di spostamento storicizzate,
	 * popolando così il modello dati centrale ({@link Orario}).
	 * </p>
	 */
	public void caricaDatiAllAvvio() {
		this.docenti = utenteDao.caricaTuttiDocenti();
		this.aule = datiDao.caricaAule();
		this.insegnamenti = datiDao.caricaInsegnamenti(this.docenti);

		List<Lezione> lez = datiDao.caricaLezioni(this.insegnamenti, this.aule);
		orario.setLezioni(lez);

		List<RichiestaSpostamento> richieste = datiDao.caricaRichieste(lez);
		for (RichiestaSpostamento r : richieste) {
			orario.aggiungiRichiesta(r);
		}
	}

	/**
	 * Elimina definitivamente una lezione rimuovendola sia dalla RAM che dal Database.
	 *
	 * @param lezione La lezione da eliminare.
	 */
	public void eliminaLezione(Lezione lezione) {
		orario.rimuoviLezione(lezione);
		datiDao.eliminaLezione(lezione);
	}

	/**
	 * Ordina una lista di lezioni per agevolare la visualizzazione nell'interfaccia utente.
	 * <p>
	 * L'ordinamento gerarchico segue i seguenti criteri:
	 * </p>
	 * <ol>
	 *     <li>Per Anno di corso crescente.</li>
	 *     <li>Per Giorno della settimana consecutivo (es. lunedì, poi martedì).</li>
	 *     <li>Per Orario di Inizio crescente (dalla mattina al pomeriggio).</li>
	 * </ol>
	 *
	 * @param listaDaOrdinare La lista non ordinata di lezioni.
	 * @return Una nuova lista contenente le stesse lezioni ma riordinate.
	 */
	public List<Lezione> ordinaLezioni(List<Lezione> listaDaOrdinare) {
		List<Lezione> ordinate = new ArrayList<>(listaDaOrdinare);

		ordinate.sort(java.util.Comparator
				.comparing((Lezione l) -> l.getInsegnamento().getAnno())
				.thenComparing(Lezione::getGiorno)
				.thenComparing(Lezione::getOraInizio));

		return ordinate;
	}

	/**
	 * Recupera e ordina l'elenco delle lezioni assegnate a uno specifico docente.
	 *
	 * @param docenteLoggato Il docente di cui recuperare l'orario personale.
	 * @return La lista ordinata cronologicamente e raggruppata delle sue lezioni.
	 */
	public List<Lezione> getOrarioPersonaleDocente(Docente docenteLoggato) {
		List<Lezione> lezioniPersonali = new ArrayList<>();

		for (Lezione l : orario.getLezioni()) {
			if (l.getInsegnamento().getDocenteTitolare().equals(docenteLoggato)) {
				lezioniPersonali.add(l);
			}
		}
		return ordinaLezioni(lezioniPersonali);
	}
}
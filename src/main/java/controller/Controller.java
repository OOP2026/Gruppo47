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

public class Controller {

	private Orario orario;
	// VARIABILE 'responsabile' ELIMINATA DEFINITIVAMENTE DA QUI
	private List<Docente> docenti = new ArrayList<>();
	private List<Aula> aule = new ArrayList<>();
	private List<Insegnamento> insegnamenti = new ArrayList<>();
	private UtenteDAO utenteDao;
	private DatiDAO datiDao;

	public Controller(Orario orario) {
		this.orario = orario;
		this.utenteDao = new UtentePostgresDao();
		this.datiDao = new DatiPostgresDao();
	}

	// ── Gestione Orario ───────────────────────────────────────────────────────

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

	public String aggiungiInsegnamento(String nome, int cfu, AnnoCorso anno, Docente titolare) {
		if (nome == null || nome.trim().isEmpty()) return "Il nome non può essere vuoto.";
		if (cfu <= 0)      return "I CFU devono essere positivi.";
		if (titolare == null) return "Seleziona un docente titolare.";

		Insegnamento nuovoInsegnamento = new Insegnamento(nome, cfu, anno, titolare);
		this.insegnamenti.add(nuovoInsegnamento);
		datiDao.salvaInsegnamento(nuovoInsegnamento);
		return null;
	}

	public List<Lezione> getLezioni() {
		return orario.getLezioni();
	}

	public List<Lezione> getLezioniPerAnno(AnnoCorso anno) {
		return orario.getLezioniPerAnno(anno);
	}

	public List<Lezione> getLezioniPerDocente(Docente docente) {
		return orario.getLezioniPerDocente(docente);
	}

	public List<Lezione> getLezioniDocente(Docente docente) {
		return orario.getLezioniPerDocente(docente);
	}

	// ── Gestione Richieste ────────────────────────────────────────────────────

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

	public List<RichiestaSpostamento> getRichieste() {
		return orario.getRichiesteSpostamento();
	}

	// ── Visualizzazione ───────────────────────────────────────────────────────

	public List<Lezione> getOrarioStudente(Studente studente) {
		if (studente == null) return java.util.Collections.emptyList();
		return orario.getLezioniPerAnno(studente.getAnnoS());
	}

	// ── Dati di supporto per le Boundary ─────────────────────────────────────

	public List<Aula> getAule() {
		return this.aule;
	}

	public List<Insegnamento> getInsegnamenti() {
		return this.insegnamenti;
	}

	public List<Docente> getDocenti() {
		return this.docenti;
	}

	public GiornoSettimana[] getGiorni() {
		return GiornoSettimana.values();
	}

	public AnnoCorso[] getAnni() {
		return AnnoCorso.values();
	}

	// ── Helper privati ────────────────────────────────────────────────────────

	public Insegnamento trovaInsegnamento(String nome) {
		for (Insegnamento i : this.insegnamenti) {
			if (i.getNome().equalsIgnoreCase(nome)) {
				return i;
			}
		}
		return null;
	}

	public Aula trovaAula(String nome) {
		for (Aula a : this.aule) {
			if (a.getNome().equalsIgnoreCase(nome)) {
				return a;
			}
		}
		return null;
	}

	public Utente verificaLogin(String username, String password) {
		return utenteDao.verificaLogin(username, password);
	}

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

	public void eliminaLezione(Lezione lezione) {
		orario.rimuoviLezione(lezione);
		datiDao.eliminaLezione(lezione);
	}

	public List<Lezione> ordinaLezioni(List<Lezione> listaDaOrdinare) {
		List<Lezione> ordinate = new ArrayList<>(listaDaOrdinare);

		ordinate.sort(java.util.Comparator
				.comparing((Lezione l) -> l.getInsegnamento().getAnno())
				.thenComparing(Lezione::getGiorno)
				.thenComparing(Lezione::getOraInizio));

		return ordinate;
	}

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
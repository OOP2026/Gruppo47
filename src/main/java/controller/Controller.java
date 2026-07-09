package controller;

import model.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Controller {

	private Orario orario;
	private Responsabile responsabile;
	private List<Utente> utentiRegistrati;

	public Controller(Orario orario, Responsabile responsabile) {
		this.orario       = orario;
		this.responsabile = responsabile;
		this.utentiRegistrati = new ArrayList<Utente>();
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
		return null;
	}

	public String aggiungiAula(String nomeAula) {
		if (nomeAula == null || nomeAula.trim().isEmpty())
			return "Il nome dell'aula non può essere vuoto.";
		for (Aula a : responsabile.getelencoAule())
			if (a.getNome().equalsIgnoreCase(nomeAula))
				return "Aula già presente.";
		responsabile.inserisciAula(nomeAula);
		return null;
	}

	public String aggiungiInsegnamento(String nome, int cfu, AnnoCorso anno, Docente titolare) {
		if (nome == null || nome.trim().isEmpty()) return "Il nome non può essere vuoto.";
		if (cfu <= 0)      return "I CFU devono essere positivi.";
		if (titolare == null) return "Seleziona un docente titolare.";
		responsabile.definisciInsegnamento(nome, cfu, anno, titolare);
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

		orario.aggiungiRichiesta(new RichiestaSpostamento(giorno, inizio, fine, StatoRichiesta.in_attesa, lezione));
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
				responsabile.modificaOrario(richiesta.getLezione(),
						richiesta.getGiornoProposto(),
						richiesta.getOraProposta(), nuovaFine);
				richiesta.setStato(StatoRichiesta.approvata);
				return "Richiesta approvata: orario aggiornato.";
			} else {
				orario.aggiungiLezione(richiesta.getLezione());
				richiesta.setStato(StatoRichiesta.rifiutata);
				return "Impossibile approvare: genera conflitti. Richiesta rifiutata.";
			}
		} else {
			richiesta.setStato(StatoRichiesta.rifiutata);
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
		return responsabile.getelencoAule();
	}

	public List<Insegnamento> getInsegnamenti() {
		return responsabile.getelencoInsegnamenti();
	}

	public List<Docente> getDocenti() {
		return responsabile.getelencoDocenti();
	}

	public GiornoSettimana[] getGiorni() {
		return GiornoSettimana.values();
	}

	public AnnoCorso[] getAnni() {
		return AnnoCorso.values();
	}



	// ── Helper privati ────────────────────────────────────────────────────────

	private Insegnamento trovaInsegnamento(String nome) {
		for (Insegnamento i : responsabile.getelencoInsegnamenti())
			if (i.getNome().equals(nome)) return i;
		return null;
	}

	private Aula trovaAula(String nome) {
		for (Aula a : responsabile.getelencoAule())
			if (a.getNome().equals(nome)) return a;
		return null;
	}

	public void registraUtente(Utente u) {
		utentiRegistrati.add(u);
	}

	public Utente verificaLogin(String username, String password) {
		for (Utente u : utentiRegistrati) {
			if (u.login(username, password)) {
				return u;
			}
		}
		return null;
	}
}

package Controller;

import Classi.*;
import java.time.LocalTime;
import java.util.List;

public class GestioneOrarioController {

    private Orario orario;
    private Responsabile responsabile;

    public GestioneOrarioController(Orario orario, Responsabile responsabile) {
        this.orario = orario;
        this.responsabile = responsabile;
    }

    // ── USE CASE: Crea Lezione ───────────────────────────────────────────────
    
    public String creaLezione(String nomeInsegnamento, String giornoStr,
                              String oraInizioStr, String oraFineStr, String nomeAula) {
        // Validazione input
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
            return "Conflitto rilevato: aula occupata o docente già impegnato in quell'orario.";

        orario.aggiungiLezione(nuova);
        return null; // successo
    }

    // ── USE CASE: Visualizza Orario Completo ─────────────────────────────────

    public List<Lezione> getLezioni() {
        return orario.getLezioni();
    }

    // ── USE CASE: Visualizza Orario per Anno ─────────────────────────────────

    public List<Lezione> getLezioniPerAnno(AnnoCorso anno) {
        return orario.getLezioniPerAnno(anno);
    }

    // ── USE CASE: Visualizza Orario per Docente ──────────────────────────────

    public List<Lezione> getLezioniPerDocente(Docente docente) {
        return orario.getLezioniPerDocente(docente);
    }

    // ── USE CASE: Aggiungi Aula ───────────────────────────────────────────────

    public String aggiungiAula(String nomeAula) {
        if (nomeAula == null || nomeAula.trim().isEmpty())
            return "Il nome dell'aula non può essere vuoto.";
        for (Aula a : responsabile.getelencoAule())
            if (a.getNome().equalsIgnoreCase(nomeAula))
                return "Aula già presente.";
        responsabile.inserisciAula(nomeAula);
        return null;
    }

    // ── USE CASE: Aggiungi Insegnamento ───────────────────────────────────────

    public String aggiungiInsegnamento(String nome, int cfu, AnnoCorso anno, Docente titolare) {
        if (nome == null || nome.trim().isEmpty()) return "Il nome non può essere vuoto.";
        if (cfu <= 0) return "I CFU devono essere positivi.";
        if (titolare == null) return "Seleziona un docente titolare.";
        responsabile.definisciInsegnamento(nome, cfu, anno, titolare);
        return null;
    }

    // ── Dati di supporto per le Boundary ─────────────────────────────────────

    public List<Aula> getAule() {
        return responsabile.getelencoAule();
    }

    public List<Insegnamento> getInsegnamenti() {
        return responsabile.getelencoInsegnamenti();
    }

    public GiornoSettimana[] getGiorni() {
        return GiornoSettimana.values();
    }

    public AnnoCorso[] getAnni() {
        return AnnoCorso.values();
    }

    // ── Helper privati ────────────────────────────────────────────────────────

    private Insegnamento trovaInsegnamento(String nome) {
        return responsabile.getelencoInsegnamenti().stream()
                .filter(i -> i.getNome().equals(nome))
                .findFirst().orElse(null);
    }

    private Aula trovaAula(String nome) {
        return responsabile.getelencoAule().stream()
                .filter(a -> a.getNome().equals(nome))
                .findFirst().orElse(null);
    }
}
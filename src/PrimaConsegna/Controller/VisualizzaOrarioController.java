package Controller;

import Classi.*;
import java.util.List;

public class VisualizzaOrarioController {

    private Orario orario;

    public VisualizzaOrarioController(Orario orario) {
        this.orario = orario;
    }

    // ── USE CASE: Visualizza Orario Studente ──────────────────────────────────

    public List<Lezione> getOrarioStudente(Studente studente) {
        if (studente == null) return java.util.Collections.emptyList();
        return orario.getLezioniPerAnno(studente.getAnnoS());
    }

    // ── USE CASE: Visualizza Orario Docente ───────────────────────────────────

    public List<Lezione> getOrarioDocente(Docente docente) {
        if (docente == null) return java.util.Collections.emptyList();
        return orario.getLezioniPerDocente(docente);
    }

    public AnnoCorso[] getAnni() {
        return AnnoCorso.values();
    }
}
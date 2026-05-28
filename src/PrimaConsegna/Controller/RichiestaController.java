package Controller;

import Classi.*;
import java.time.LocalTime;
import java.util.List;

public class RichiestaController {

    private Orario orario;
    private Responsabile responsabile;

    public RichiestaController(Orario orario, Responsabile responsabile) {
        this.orario = orario;
        this.responsabile = responsabile;
    }

    // ── USE CASE: Invia Richiesta di Spostamento (Docente) ───────────────────


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

        // Controlla se esiste già una richiesta in attesa per la stessa lezione
        for (RichiestaSpostamento r : orario.getRichiesteSpostamento()) {
            if (r.getLezione().equals(lezione) && r.getStato() == StatoRichiesta.in_attesa)
                return "Esiste già una richiesta in attesa per questa lezione.";
        }

        RichiestaSpostamento richiesta = new RichiestaSpostamento(
                giorno, inizio, fine, StatoRichiesta.in_attesa, lezione);
        orario.aggiungiRichiesta(richiesta);
        return null; // successo
    }

    // ── USE CASE: Gestisci Richiesta (Responsabile) ──────────────────────────


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
                return "Impossibile approvare: lo spostamento genererebbe conflitti. Richiesta rifiutata automaticamente.";
            }
        } else {
            richiesta.setStato(StatoRichiesta.rifiutata);
            return "Richiesta rifiutata.";
        }
    }

    // ── Dati di supporto per le Boundary ─────────────────────────────────────

    public List<RichiestaSpostamento> getRichieste() {
        return orario.getRichiesteSpostamento();
    }

    public List<RichiestaSpostamento> getRichiesteInAttesa() {
        return orario.getRichiesteSpostamento().stream()
                .filter(r -> r.getStato() == StatoRichiesta.in_attesa)
                .collect(java.util.stream.Collectors.toList());
    }


    public List<Lezione> getLezioniDocente(Docente docente) {
        return orario.getLezioniPerDocente(docente);
    }

    public GiornoSettimana[] getGiorni() {
        return GiornoSettimana.values();
    }
}
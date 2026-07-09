package Test;

import javax.swing.*;
import java.time.LocalTime;
import model.*;
import controller.Controller;
import gui.*;

public class Main {

    public static void main(String[] args) {



        // ── Entity ────────────────────────────────────────────────────────────
        Orario orarioGenerale = new Orario();

        Docente docRossi   = new Docente("Mario",  "Rossi",   "rossi@uni.it",   "pass", "mrossi",   orarioGenerale);
        Docente docBianchi = new Docente("Laura",  "Bianchi", "bianchi@uni.it", "pass", "lbianchi", orarioGenerale);
        Responsabile resp  = new Responsabile("Carlo", "Verdi", "verdi@uni.it", "admin", "cverdi",  orarioGenerale);

        resp.inserisciAula("Aula A1");
        resp.inserisciAula("Aula B2");
        resp.inserisciAula("Lab Informatica");

        resp.definisciInsegnamento("Analisi Matematica",      9, AnnoCorso.I,   docRossi);
        resp.definisciInsegnamento("Programmazione I",        9, AnnoCorso.I,   docBianchi);
        resp.definisciInsegnamento("Ingegneria del Software", 6, AnnoCorso.II, docBianchi);
        resp.definisciInsegnamento("Basi di Dati",            6, AnnoCorso.II, docRossi);

        Insegnamento analisi  = resp.getelencoInsegnamenti().get(0);
        Insegnamento progI    = resp.getelencoInsegnamenti().get(1);
        Insegnamento ingSoft  = resp.getelencoInsegnamenti().get(2);
        Insegnamento basiDati = resp.getelencoInsegnamenti().get(3);

        Aula aulaA1  = resp.getelencoAule().get(0);
        Aula aulaB2  = resp.getelencoAule().get(1);
        Aula labInfo = resp.getelencoAule().get(2);

        resp.creaLezione(analisi,  GiornoSettimana.lunedi,    LocalTime.of(9,  0), LocalTime.of(11, 0), aulaA1);
        resp.creaLezione(progI,    GiornoSettimana.lunedi,    LocalTime.of(11, 0), LocalTime.of(13, 0), labInfo);
        resp.creaLezione(ingSoft,  GiornoSettimana.martedi,   LocalTime.of(14, 0), LocalTime.of(16, 0), aulaB2);
        resp.creaLezione(basiDati, GiornoSettimana.mercoledi, LocalTime.of(9,  0), LocalTime.of(11, 0), aulaA1);

        Studente studente = new Studente("Giulia", "Ferrari", "ferrari@stud.it",
                "pass", "gferrari", AnnoCorso.I, "123456", orarioGenerale);


        // NUOVO: Passiamo i docenti all'oggetto Responsabile
        resp.aggiungiDocente(docRossi);
        resp.aggiungiDocente(docBianchi);
        resp.aggiungiDocente(resp); // Anche il responsabile stesso è un docente

        // ── Control ───────────────────────────────────────────────────────────
        Controller controller = new Controller(orarioGenerale, resp);

        // ── Boundary ──────────────────────────────────────────────────────────
        // NOTA: Ho eliminato la vecchia lista 'docenti' che avevi qui perché ora è dentro 'resp'

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(controller, docBianchi, studente);
            frame.setVisible(true);
        });
    }
}
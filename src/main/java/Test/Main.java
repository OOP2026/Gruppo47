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

        // 1. CREAZIONE UTENTI
        Docente docRossi   = new Docente("Mario",  "Rossi",   "rossi@uni.it",   "pass", "mrossi",   orarioGenerale);
        Docente docBianchi = new Docente("Laura",  "Bianchi", "bianchi@uni.it", "pass", "lbianchi", orarioGenerale);
        Responsabile resp  = new Responsabile("Carlo", "Verdi", "verdi@uni.it", "admin", "cverdi",  orarioGenerale);
        Studente studente = new Studente("Giulia", "Ferrari", "ferrari@stud.it", "pass", "gferrari", AnnoCorso.I, "123456", orarioGenerale);

        // (Opzionale) Aggiungi un po' di dati di prova per non avere l'app vuota
        resp.inserisciAula("Aula A1");
        resp.inserisciAula("Aula B2");
        resp.inserisciAula("Lab Informatica");
        resp.definisciInsegnamento("Analisi Matematica", 9, AnnoCorso.I, docRossi);
        resp.definisciInsegnamento("Programmazione I", 9, AnnoCorso.I, docBianchi);

//        // Aggiungi docenti al responsabile
//        resp.aggiungiDocente(docRossi);
//        resp.aggiungiDocente(docBianchi);
//        resp.aggiungiDocente(resp);

        // ── Control ───────────────────────────────────────────────────────────
        Controller controller = new Controller(orarioGenerale, resp);
        controller.caricaDatiAllAvvio();

        // ── Boundary (Avvio Grafica) ──────────────────────────────────────────
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame(controller);
            login.setVisible(true);
        });
    }
}
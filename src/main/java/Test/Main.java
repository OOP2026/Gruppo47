package Test;

import javax.swing.*;
import java.time.LocalTime;
import model.*;
import controller.Controller;
import gui.*;

/**
 * Classe principale che avvia l'applicazione di gestione dell'orario universitario.
 * <p>
 * Questa classe funge da punto di ingresso (entry-point) del sistema e si occupa
 * di inizializzare i componenti fondamentali dell'architettura dell'applicazione.
 * Nello specifico, istanzia il modello dati centrale ({@link Orario}), il gestore
 * della logica ({@link Controller}) e avvia l'interfaccia grafica (GUI).
 * </p>
 */
public class Main {

    /**
     * Metodo principale che esegue l'applicazione.
     * <p>
     * Il metodo esegue sequenzialmente le seguenti operazioni di setup:
     * </p>
     * <ul>
     *     <li><strong>Entity:</strong> Crea l'istanza dell'orario generale vuoto.</li>
     *     <li><strong>Control:</strong> Inizializza il {@code Controller} passandogli l'orario e
     *         chiama il metodo per il caricamento dei dati di base all'avvio.</li>
     *     <li><strong>Boundary:</strong> Avvia la finestra di login ({@link LoginFrame}) in modo
     *         thread-safe utilizzando {@code SwingUtilities.invokeLater}.</li>
     * </ul>
     *
     * @param args Argomenti passati da riga di comando (attualmente non utilizzati dal sistema).
     */
    public static void main(String[] args) {

        // ── Entity ────────────────────────────────────────────────────────────
        Orario orarioGenerale = new Orario();

        // ── Control ───────────────────────────────────────────────────────────
        Controller controller = new Controller(orarioGenerale);
        controller.caricaDatiAllAvvio();

        // ── Boundary (Avvio Grafica) ──────────────────────────────────────────
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame(controller);
            login.setVisible(true);
        });
    }
}
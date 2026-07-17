package gui;

import javax.swing.*;
import controller.Controller;
import model.*;

/**
 * Pannello dell'interfaccia grafica (Boundary) dedicato alla creazione di una nuova {@link Lezione}.
 * <p>
 * Fornisce un form interattivo in cui l'utente può selezionare un insegnamento,
 * un'aula, il giorno della settimana e inserire gli orari di inizio e fine.
 * Si occupa di raccogliere questi input e delegare la logica di validazione e
 * salvataggio al {@link Controller}.
 * </p>
 */
public class CreaLezionePanel {

    // --- 1. COMPONENTI GESTITI DAL DESIGNER (.form) ---
    private JPanel mainPanel;
    private JComboBox<String> insegnamentoBox;
    private JComboBox<String> giornoBox;
    private JComboBox<String> aulaBox;
    private JTextField oraInizioField;
    private JTextField oraFineField;
    private JButton creaBtn;

    // --- 2. VARIABILI DI LOGICA ---
    private Controller controller;
    private MainFrame mainFrame;

    /**
     * Inizializza il pannello per la creazione delle lezioni e configura i listener degli eventi.
     * <p>
     * Al clic sul pulsante "Crea", il listener raccoglie i dati dai campi di testo
     * e dai menu a tendina, inoltrandoli al controller. Se l'operazione ha successo,
     * mostra un popup di conferma e reindirizza l'utente alla vista dell'orario.
     * </p>
     *
     * @param controller Il gestore della logica di business.
     * @param mainFrame  Il frame principale dell'applicazione, usato per la navigazione tra i pannelli.
     */
    public CreaLezionePanel(Controller controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame  = mainFrame;

        // Popolamento iniziale dei giorni (Tenuto)
        for (GiornoSettimana g : controller.getGiorni()) {
            giornoBox.addItem(g.name());
        }

        // Listener del bottone (Tenuto)
        creaBtn.addActionListener(e -> {
            String errore = controller.creaLezione(
                    (String) insegnamentoBox.getSelectedItem(),
                    (String) giornoBox.getSelectedItem(),
                    oraInizioField.getText().trim(),
                    oraFineField.getText().trim(),
                    (String) aulaBox.getSelectedItem()
            );
            if (errore != null) {
                MainFrame.showError(mainPanel, errore);
            } else {
                MainFrame.showSuccess(mainPanel, "Lezione creata con successo!");
                mainFrame.showCard(MainFrame.CARD_ORARIO);
            }
        });
    }

    // --- 3. METODI ---

    /**
     * Sincronizza i dati mostrati nei menu a tendina (ComboBox) con lo stato attuale del sistema.
     * <p>
     * Viene solitamente richiamato ogni volta che il pannello diventa visibile,
     * per garantire che l'utente veda sempre la lista aggiornata di aule e insegnamenti.
     * </p>
     */
    public void refresh() {
        insegnamentoBox.removeAllItems();
        for (Insegnamento i : controller.getInsegnamenti())
            insegnamentoBox.addItem(i.getNome());

        aulaBox.removeAllItems();
        for (Aula a : controller.getAule())
            aulaBox.addItem(a.getNome());
    }

    /**
     * Restituisce il contenitore grafico principale di questo pannello.
     *
     * @return Il {@link JPanel} radice configurato tramite il GUI designer.
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
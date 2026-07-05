package GUI;

import Controller.Controller;
import Classi.*;

import javax.swing.*;

public class CreaLezionePanel {

    // --- 1. COMPONENTI GESTITI DAL DESIGNER (.form) ---
    // (Usa questi esatti nomi per la proprietà "field name" nel designer)
    private JPanel mainPanel;
    private JComboBox<String> insegnamentoBox;
    private JComboBox<String> giornoBox;
    private JComboBox<String> aulaBox;
    private JTextField oraInizioField;
    private JTextField oraFineField;
    private JButton creaBtn; // Promosso a variabile di classe per il binding!

    // --- 2. VARIABILI DI LOGICA ---
    private Controller controller;
    private MainFrame mainFrame;

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
                // Nota: sostituito "this" con "mainPanel"
                MainFrame.showError(mainPanel, errore);
            } else {
                MainFrame.showSuccess(mainPanel, "Lezione creata con successo!");
                mainFrame.showCard(MainFrame.CARD_ORARIO);
            }
        });
    }

    // --- 3. METODI (Tenuti) ---

    public void refresh() {
        insegnamentoBox.removeAllItems();
        for (Insegnamento i : controller.getInsegnamenti())
            insegnamentoBox.addItem(i.getNome());

        aulaBox.removeAllItems();
        for (Aula a : controller.getAule())
            aulaBox.addItem(a.getNome());
    }

    // Metodo per recuperare la vista
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
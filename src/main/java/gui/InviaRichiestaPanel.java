package gui;

import javax.swing.*;
import java.util.List;
import controller.Controller;
import model.*;

public class InviaRichiestaPanel {

    // --- 1. COMPONENTI CREATI DAL DESIGNER ---
    // (Nel .form imposta il campo "field name" con questi esatti nomi)
    private JPanel mainPanel; // Il pannello base su cui trascini tutto
    private JComboBox<String> lezioneBox;
    private JComboBox<String> giornoBox;
    private JTextField oraInizioField;
    private JTextField oraFineField;
    private JButton inviaBtn;

    // --- 2. LE TUE VARIABILI LOGICHE ---
    private Controller controller;
    private Docente docenteCorrente;
    private MainFrame mainFrame;
    private List<Lezione> lezioniDocente;

    public InviaRichiestaPanel(Controller controller, Docente docenteCorrente, MainFrame mainFrame) {
        this.controller      = controller;
        this.docenteCorrente = docenteCorrente;
        this.mainFrame       = mainFrame;

        // Popolamento dinamico del giorno (intatto!)
        for (GiornoSettimana g : controller.getGiorni()) {
            giornoBox.addItem(g.name());
        }

        // Il tuo Listener (intatto!)
        inviaBtn.addActionListener(e -> {
            int idx = lezioneBox.getSelectedIndex();
            if (idx < 0 || lezioniDocente == null || idx >= lezioniDocente.size()) {
                // Nota: uso mainPanel invece di "this" per mostrare l'errore
                MainFrame.showError(mainPanel, "Seleziona una lezione.");
                return;
            }
            String errore = controller.inviaRichiesta(
                    lezioniDocente.get(idx),
                    (String) giornoBox.getSelectedItem(),
                    oraInizioField.getText().trim(),
                    oraFineField.getText().trim()
            );
            if (errore != null) MainFrame.showError(mainPanel, errore);
            else MainFrame.showSuccess(mainPanel, "Richiesta inviata.");
        });
    }

    // Il tuo metodo (intatto!)
    public void refresh() {
        lezioneBox.removeAllItems();
        lezioniDocente = controller.getLezioniDocente(docenteCorrente);
        for (Lezione l : lezioniDocente)
            lezioneBox.addItem(l.getGiorno() + " " + l.getOraInizio() + "-" + l.getOraFine()
                    + " | " + l.getInsegnamento().getNome());
    }

    // METODO NUOVO: Serve a restituire il pannello grafico a chi lo richiede (es. il MainFrame)
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
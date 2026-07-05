package GUI;

import Controller.Controller;
import Classi.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class GestisciRichiestaPanel {

    // --- 1. COMPONENTI GESTITI DAL DESIGNER (.form) ---
    private JPanel mainPanel;
    private JTable table;
    private JButton approvaBtn;
    private JButton rifiutaBtn;

    // --- 2. VARIABILI DI LOGICA ---
    private Controller controller;
    private MainFrame mainFrame;
    private DefaultTableModel tableModel;
    private List<RichiestaSpostamento> richiesteCorrente;

    private static final String[] COLONNE = {
            "Insegnamento", "Giorno orig.", "Orario orig.", "Giorno prop.", "Ora prop.", "Stato"
    };

    public GestisciRichiestaPanel(Controller controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame  = mainFrame;

        // --- INIZIALIZZAZIONE TABELLA ---
        // Ricreo il tuo modello personalizzato non modificabile
        tableModel = new DefaultTableModel(COLONNE, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        // Assegno il modello alla tabella che il designer ha disegnato per me
        table.setModel(tableModel);

        // --- LISTENERS ---
        approvaBtn.addActionListener(e -> onGestisci(true));
        rifiutaBtn.addActionListener(e -> onGestisci(false));
    }

    // --- 3. METODI ---
    private void onGestisci(boolean approva) {
        int row = table.getSelectedRow();
        if (row < 0) {
            MainFrame.showError(mainPanel, "Seleziona una richiesta."); // Aggiornato con mainPanel
            return;
        }
        String esito = controller.gestisciRichiesta(richiesteCorrente.get(row), approva);
        MainFrame.showSuccess(mainPanel, esito); // Aggiornato con mainPanel
        refresh();
    }

    public void refresh() {
        tableModel.setRowCount(0);
        richiesteCorrente = controller.getRichieste();
        for (RichiestaSpostamento r : richiesteCorrente) {
            tableModel.addRow(new Object[]{
                    r.getLezione().getInsegnamento().getNome(),
                    r.getLezione().getGiorno(),
                    r.getLezione().getOraInizio() + "-" + r.getLezione().getOraFine(),
                    r.getGiornoProposto(),
                    r.getOraProposta(),
                    r.getStato().name()
            });
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
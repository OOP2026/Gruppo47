package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import controller.Controller;
import model.*;

public class OrarioTablePanel {

    // --- 1. COMPONENTI GESTITI DAL DESIGNER (.form) ---
    private JPanel mainPanel;
    private JComboBox<String> filtroAnnoBox;
    private JTable table;

    // --- 2. VARIABILI DI LOGICA ---
    private Controller controller;
    private DefaultTableModel tableModel;

    private static final String[] COLONNE = {
            "Giorno", "Inizio", "Fine", "Insegnamento", "Anno", "Docente", "Aula"
    };

    public OrarioTablePanel(Controller controller) {
        this.controller = controller;

        // --- INIZIALIZZAZIONE FILTRO ANNO ---
        filtroAnnoBox.addItem("Tutti");
        for (AnnoCorso a : controller.getAnni()) {
            filtroAnnoBox.addItem(a.name());
        }

        // Aggiungo il listener per aggiornare la tabella quando cambio l'anno
        filtroAnnoBox.addActionListener(e -> refresh());

        // --- INIZIALIZZAZIONE TABELLA ---
        tableModel = new DefaultTableModel(COLONNE, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        // Assegno il modello alla tabella grafica
        table.setModel(tableModel);
    }

    // --- 3. METODO REFRESH ---
    public void refresh() {
        tableModel.setRowCount(0);
        String sel = (String) filtroAnnoBox.getSelectedItem();

        // Se non c'è nulla di selezionato (es. in fase di avvio), esco in modo sicuro
        if (sel == null) return;

        List<Lezione> lezioni = "Tutti".equals(sel)
                ? controller.getLezioni()
                : controller.getLezioniPerAnno(AnnoCorso.valueOf(sel));

        for (Lezione l : lezioni) {
            tableModel.addRow(new Object[]{
                    l.getGiorno(),
                    l.getOraInizio(),
                    l.getOraFine(),
                    l.getInsegnamento().getNome(),
                    l.getInsegnamento().getAnno(),
                    l.getInsegnamento().getDocenteTitolare().getCognome(),
                    l.getAula().getNome()
            });
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
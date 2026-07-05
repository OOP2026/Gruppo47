package GUI;

import Controller.Controller;
import Classi.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class StudenteOrarioPanel {

    // --- 1. COMPONENTI GESTITI DAL DESIGNER (.form) ---
    private JPanel mainPanel;
    private JTable table;

    // --- 2. VARIABILI DI LOGICA ---
    private Controller controller;
    private Studente studente;
    private DefaultTableModel tableModel;

    private static final String[] COLONNE = {
            "Giorno", "Inizio", "Fine", "Insegnamento", "Docente", "Aula"
    };

    public StudenteOrarioPanel(Controller controller, Studente studente) {
        this.controller = controller;
        this.studente   = studente;

        // --- INIZIALIZZAZIONE TABELLA ---
        tableModel = new DefaultTableModel(COLONNE, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        // Assegno il modello alla tabella che hai disegnato graficamente
        table.setModel(tableModel);
    }

    // --- 3. METODO REFRESH ---
    public void refresh() {
        tableModel.setRowCount(0);
        if (studente == null) return;

        List<Lezione> lezioni = controller.getOrarioStudente(studente);
        for (Lezione l : lezioni) {
            tableModel.addRow(new Object[]{
                    l.getGiorno(),
                    l.getOraInizio(),
                    l.getOraFine(),
                    l.getInsegnamento().getNome(),
                    l.getInsegnamento().getDocenteTitolare().getCognome(),
                    l.getAula().getNome()
            });
        }
    }

    // Metodo per recuperare la vista
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
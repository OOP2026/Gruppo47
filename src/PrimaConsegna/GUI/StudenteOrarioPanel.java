package GUI;
import Controller.Controller;


import Classi.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudenteOrarioPanel extends JPanel {

    private Controller controller;
    private Studente studente;
    private DefaultTableModel tableModel;

    private static final String[] COLONNE = {
            "Giorno", "Inizio", "Fine", "Insegnamento", "Docente", "Aula"
    };

    public StudenteOrarioPanel(Controller controller, Studente studente) {
        this.controller = controller;
        this.studente   = studente;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(COLONNE, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

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
}
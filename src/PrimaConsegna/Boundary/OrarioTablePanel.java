package Boundary;

import Controller.GestioneOrarioController;
import Classi.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrarioTablePanel extends JPanel {

    private GestioneOrarioController controller;
    private DefaultTableModel tableModel;
    private JComboBox<String> filtroAnnoBox;

    private static final String[] COLONNE = {
            "Giorno", "Inizio", "Fine", "Insegnamento", "Anno", "Docente", "Aula"
    };

    public OrarioTablePanel(GestioneOrarioController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // Filtro anno
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Filtra per anno:"));
        filtroAnnoBox = new JComboBox<>();
        filtroAnnoBox.addItem("Tutti");
        for (AnnoCorso a : controller.getAnni())
            filtroAnnoBox.addItem(a.name());
        filtroAnnoBox.addActionListener(e -> refresh());
        topPanel.add(filtroAnnoBox);
        add(topPanel, BorderLayout.NORTH);

        // Tabella
        tableModel = new DefaultTableModel(COLONNE, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void refresh() {
        tableModel.setRowCount(0);
        String sel = (String) filtroAnnoBox.getSelectedItem();
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
}
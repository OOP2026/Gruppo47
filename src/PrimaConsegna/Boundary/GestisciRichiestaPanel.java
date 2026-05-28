package Boundary;

import Controller.RichiestaController;
import Classi.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestisciRichiestaPanel extends JPanel {

    private RichiestaController controller;
    private MainFrame mainFrame;

    private JTable table;
    private DefaultTableModel tableModel;
    private List<RichiestaSpostamento> richiesteCorrente;

    private static final String[] COLONNE = {
            "Insegnamento", "Giorno orig.", "Orario orig.", "Giorno prop.", "Ora prop.", "Stato"
    };

    public GestisciRichiestaPanel(RichiestaController controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame  = mainFrame;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(COLONNE, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton approvaBtn = new JButton("Approva");
        JButton rifiutaBtn = new JButton("Rifiuta");
        bottomPanel.add(approvaBtn);
        bottomPanel.add(rifiutaBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        approvaBtn.addActionListener(e -> onGestisci(true));
        rifiutaBtn.addActionListener(e -> onGestisci(false));
    }

    private void onGestisci(boolean approva) {
        int row = table.getSelectedRow();
        if (row < 0) {
            MainFrame.showError(this, "Seleziona una richiesta.");
            return;
        }
        String esito = controller.gestisciRichiesta(richiesteCorrente.get(row), approva);
        MainFrame.showSuccess(this, esito);
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
}
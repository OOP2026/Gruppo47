package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
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
    private Utente utenteLoggato;
    private List<Lezione> lezioniAttuali;

    private static final String[] COLONNE = {
            "Giorno", "Inizio", "Fine", "Insegnamento", "Anno", "Docente", "Aula"
    };

    public OrarioTablePanel(Controller controller) {
        this.controller = controller;

        // --- INIZIALIZZAZIONE FILTRO A TENDINA ---
        filtroAnnoBox.addItem("Tutti");
        filtroAnnoBox.addItem("Il mio orario"); // <-- NUOVA OPZIONE PER TUTTI
        for (AnnoCorso a : controller.getAnni()) {
            filtroAnnoBox.addItem(a.name());
        }
        filtroAnnoBox.addActionListener(e -> refresh());

        // --- INIZIALIZZAZIONE TABELLA ---
        tableModel = new DefaultTableModel(COLONNE, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setModel(tableModel);

        // --- FUNZIONE ELIMINA (Solo per Responsabile) ---
        JPopupMenu popupElimina = new JPopupMenu();
        JMenuItem eliminaItem = new JMenuItem("Elimina Lezione");
        popupElimina.add(eliminaItem);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Mostra il menù contestuale solo se clicchi col destro e sei Responsabile
                if (SwingUtilities.isRightMouseButton(e) && utenteLoggato instanceof Responsabile) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        table.setRowSelectionInterval(row, row);
                        popupElimina.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        eliminaItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Lezione daEliminare = lezioniAttuali.get(row);
                controller.eliminaLezione(daEliminare);
                JOptionPane.showMessageDialog(mainPanel, "Lezione eliminata dal database!");
                refresh();
            }
        });
    }

    // Viene chiamato dal MainFrame quando si apre la schermata
    public void setUtenteLoggato(Utente u) {
        this.utenteLoggato = u;
        // Adesso la tendina è SEMPRE visibile, per qualsiasi ruolo!
        filtroAnnoBox.setVisible(true);

        // Impostiamo di default la vista generale (o personale) all'avvio
        if (u instanceof Docente) {
            filtroAnnoBox.setSelectedItem("Il mio orario");
        } else {
            filtroAnnoBox.setSelectedItem("Tutti");
        }

        refresh();
    }

    // --- 3. METODO REFRESH LOGICO ---
    public void refresh() {
        tableModel.setRowCount(0);
        String sel = (String) filtroAnnoBox.getSelectedItem();

        if (sel == null || utenteLoggato == null) return;

        List<Lezione> lezioniGrezze = new ArrayList<>();

        // 1. Scegliamo quali lezioni pescare in base a cosa c'è scritto nella tendina
        if ("Tutti".equals(sel)) {
            lezioniGrezze = controller.getLezioni();

        } else if ("Il mio orario".equals(sel)) {
            // Selezionato orario personale:
            // Visto che sia Docente sia Responsabile ereditano da Docente (o lo sono nel DB), usiamo il loro profilo
            if (utenteLoggato instanceof Docente) {
                lezioniGrezze = controller.getOrarioPersonaleDocente((Docente) utenteLoggato);
            } else {
                // Se è uno Studente, supponendo che abbia un anno assegnato, puoi inserire
                // qui la logica se hai un metodo tipo controller.getOrarioPersonaleStudente()
                // Altrimenti lo avvisiamo di usare il filtro per Anno:
                JOptionPane.showMessageDialog(mainPanel, "Usa i filtri dell'anno di corso per vedere il tuo orario.");
                filtroAnnoBox.setSelectedItem("Tutti");
                return;
            }

        } else {
            // Se non è né "Tutti" né "Il mio orario", allora è un Anno di corso
            lezioniGrezze = controller.getLezioniPerAnno(AnnoCorso.valueOf(sel));
        }

        // 2. Le ordiniamo perfettamente come abbiamo fatto prima
        lezioniAttuali = controller.ordinaLezioni(lezioniGrezze);

        // 3. Le stampiamo a video
        for (Lezione l : lezioniAttuali) {
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
package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import controller.Controller;
import model.*;

/**
 * Pannello dell'interfaccia grafica (Boundary) principale per la visualizzazione dell'orario.
 * <p>
 * Offre una tabella interattiva in cui le lezioni possono essere filtrate dinamicamente:
 * in base a un anno di corso specifico, visualizzando tutte le lezioni, o mostrando solo
 * l'orario personale (nel caso dei docenti). Inoltre, integra funzionalità di amministrazione
 * avanzate: se l'utente loggato è un {@link Responsabile}, permette l'eliminazione
 * delle lezioni tramite un menu contestuale (tasto destro del mouse).
 * </p>
 */
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

    /**
     * Inizializza il pannello dell'orario, popolando i filtri di ricerca e configurando
     * il modello della tabella (non modificabile dall'utente).
     * <p>
     * Associa inoltre un {@link java.awt.event.MouseListener} alla tabella per gestire
     * il menu a tendina (JPopupMenu) dedicato all'eliminazione delle lezioni.
     * </p>
     *
     * @param controller Il gestore della logica di business.
     */
    public OrarioTablePanel(Controller controller) {
        this.controller = controller;

        // --- INIZIALIZZAZIONE FILTRO A TENDINA ---
        filtroAnnoBox.addItem("Tutti");
        filtroAnnoBox.addItem("Il mio orario");
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

    /**
     * Associa l'utente attualmente autenticato a questa vista, adattando l'interfaccia
     * di conseguenza.
     * <p>
     * Modifica il filtro predefinito all'avvio in base al ruolo: i Docenti partono dalla vista
     * "Il mio orario", mentre gli altri partono dalla vista "Tutti".
     * </p>
     *
     * @param u L'utente che ha effettuato l'accesso al sistema.
     */
    public void setUtenteLoggato(Utente u) {
        this.utenteLoggato = u;
        filtroAnnoBox.setVisible(true);

        if (u instanceof Docente) {
            filtroAnnoBox.setSelectedItem("Il mio orario");
        } else {
            filtroAnnoBox.setSelectedItem("Tutti");
        }

        refresh();
    }

    /**
     * Sincronizza la tabella grafica con i dati in memoria, applicando il filtro attualmente
     * selezionato nel menu a tendina.
     * <p>
     * Prima di stampare le righe a video, la lista delle lezioni viene passata al
     * metodo {@link Controller#ordinaLezioni(List)} per garantire un ordine cronologico
     * corretto e leggibile.
     * </p>
     */
    public void refresh() {
        tableModel.setRowCount(0);
        String sel = (String) filtroAnnoBox.getSelectedItem();

        if (sel == null || utenteLoggato == null) return;

        List<Lezione> lezioniGrezze = new ArrayList<>();

        if ("Tutti".equals(sel)) {
            lezioniGrezze = controller.getLezioni();

        } else if ("Il mio orario".equals(sel)) {
            if (utenteLoggato instanceof Docente) {
                lezioniGrezze = controller.getOrarioPersonaleDocente((Docente) utenteLoggato);
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Usa i filtri dell'anno di corso per vedere il tuo orario.");
                filtroAnnoBox.setSelectedItem("Tutti");
                return;
            }

        } else {
            lezioniGrezze = controller.getLezioniPerAnno(AnnoCorso.valueOf(sel));
        }

        lezioniAttuali = controller.ordinaLezioni(lezioniGrezze);

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

    /**
     * Restituisce il contenitore grafico principale di questo pannello.
     *
     * @return Il {@link JPanel} radice.
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import controller.Controller;
import model.*;

/**
 * Pannello dell'interfaccia grafica (Boundary) dedicato alla visualizzazione dell'orario
 * lato {@link Studente}.
 * <p>
 * A differenza del pannello generale, questa schermata mostra esclusivamente
 * le lezioni pertinenti all'anno di corso ({@link AnnoCorso}) dello studente loggato,
 * nascondendo la colonna relativa all'anno (diventata superflua) per massimizzare la leggibilità.
 * </p>
 */
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

    /**
     * Inizializza il pannello dell'orario per lo studente, configurando la tabella
     * con intestazioni semplificate e rendendola di sola lettura.
     *
     * @param controller Il gestore della logica di business.
     * @param studente   Lo studente attualmente autenticato di cui estrarre l'orario.
     */
    public StudenteOrarioPanel(Controller controller, Studente studente) {
        this.controller = controller;
        this.studente   = studente;

        // --- INIZIALIZZAZIONE TABELLA ---
        tableModel = new DefaultTableModel(COLONNE, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setModel(tableModel);
    }

    /**
     * Sincronizza la tabella grafica recuperando dal sistema le sole lezioni previste
     * per l'anno accademico dello studente e popolando le righe.
     */
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

    /**
     * Restituisce il contenitore grafico principale di questo pannello.
     *
     * @return Il {@link JPanel} radice generato dal GUI designer.
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import controller.Controller;
import model.*;

/**
 * Pannello dell'interfaccia grafica (Boundary) dedicato alla gestione delle
 * {@link RichiestaSpostamento} in sospeso.
 * <p>
 * Mostra una tabella non modificabile con tutte le richieste registrate nel sistema.
 * Permette al Responsabile di selezionare una specifica richiesta e di approvarla
 * o rifiutarla tramite appositi pulsanti d'azione.
 * </p>
 */
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

    /**
     * Intestazioni delle colonne per la tabella delle richieste.
     */
    private static final String[] COLONNE = {
            "Insegnamento", "Giorno orig.", "Orario orig.", "Giorno prop.", "Ora prop.", "Stato"
    };

    /**
     * Inizializza il pannello impostando il modello personalizzato per la tabella
     * (impedendone la modifica manuale delle celle) e collegando i listener ai pulsanti.
     *
     * @param controller Il gestore della logica di business.
     * @param mainFrame  Il frame principale per gestire la navigazione e i popup di esito.
     */
    public GestisciRichiestaPanel(Controller controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame  = mainFrame;

        // --- INIZIALIZZAZIONE TABELLA ---
        tableModel = new DefaultTableModel(COLONNE, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setModel(tableModel);

        // --- LISTENERS ---
        approvaBtn.addActionListener(e -> onGestisci(true));
        rifiutaBtn.addActionListener(e -> onGestisci(false));
    }

    // --- 3. METODI ---

    /**
     * Metodo interno chiamato dai pulsanti di approvazione/rifiuto.
     * <p>
     * Recupera la riga selezionata nella tabella, estrae l'oggetto richiesta corrispondente
     * e inoltra la decisione al controller. Al termine mostra un messaggio di esito e
     * aggiorna la tabella.
     * </p>
     *
     * @param approva {@code true} se l'utente ha cliccato "Approva", {@code false} se ha cliccato "Rifiuta".
     */
    private void onGestisci(boolean approva) {
        int row = table.getSelectedRow();
        if (row < 0) {
            MainFrame.showError(mainPanel, "Seleziona una richiesta.");
            return;
        }
        String esito = controller.gestisciRichiesta(richiesteCorrente.get(row), approva);
        MainFrame.showSuccess(mainPanel, esito);
        refresh();
    }

    /**
     * Sincronizza la tabella grafica svuotandola e ripopolandola con i dati più recenti
     * forniti dal sistema.
     */
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

    /**
     * Restituisce il contenitore grafico principale di questo pannello.
     *
     * @return Il {@link JPanel} radice generato dal GUI designer.
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
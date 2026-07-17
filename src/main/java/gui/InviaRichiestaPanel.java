package gui;

import javax.swing.*;
import java.util.List;
import controller.Controller;
import model.*;

/**
 * Pannello dell'interfaccia grafica (Boundary) che permette a un {@link Docente}
 * di richiedere lo spostamento di una propria lezione.
 * <p>
 * Fornisce un modulo in cui il docente può selezionare una delle lezioni di cui è titolare
 * e proporre un nuovo giorno e una nuova fascia oraria. La richiesta verrà salvata in stato
 * di attesa affinché il Responsabile possa valutarla.
 * </p>
 */
public class InviaRichiestaPanel {

    // --- 1. COMPONENTI CREATI DAL DESIGNER ---
    private JPanel mainPanel;
    private JComboBox<String> lezioneBox;
    private JComboBox<String> giornoBox;
    private JTextField oraInizioField;
    private JTextField oraFineField;
    private JButton inviaBtn;

    // --- 2. LE TUE VARIABILI LOGICHE ---
    private Controller controller;
    private Docente docenteCorrente;
    private MainFrame mainFrame;
    private List<Lezione> lezioniDocente;

    /**
     * Inizializza il pannello per l'invio delle richieste e imposta l'azione del pulsante di invio.
     *
     * @param controller      Il gestore della logica di business.
     * @param docenteCorrente Il docente attualmente autenticato nel sistema.
     * @param mainFrame       Il frame principale, usato per agganciare i messaggi di popup.
     */
    public InviaRichiestaPanel(Controller controller, Docente docenteCorrente, MainFrame mainFrame) {
        this.controller      = controller;
        this.docenteCorrente = docenteCorrente;
        this.mainFrame       = mainFrame;

        // Popolamento dinamico del giorno (intatto!)
        for (GiornoSettimana g : controller.getGiorni()) {
            giornoBox.addItem(g.name());
        }

        // Il tuo Listener (intatto!)
        inviaBtn.addActionListener(e -> {
            int idx = lezioneBox.getSelectedIndex();
            if (idx < 0 || lezioniDocente == null || idx >= lezioniDocente.size()) {
                MainFrame.showError(mainPanel, "Seleziona una lezione.");
                return;
            }
            String errore = controller.inviaRichiesta(
                    lezioniDocente.get(idx),
                    (String) giornoBox.getSelectedItem(),
                    oraInizioField.getText().trim(),
                    oraFineField.getText().trim()
            );
            if (errore != null) MainFrame.showError(mainPanel, errore);
            else MainFrame.showSuccess(mainPanel, "Richiesta inviata.");
        });
    }

    // --- 3. METODI ---

    /**
     * Sincronizza il menu a tendina delle lezioni recuperando dal controller l'elenco aggiornato
     * degli impegni didattici assegnati al docente corrente.
     */
    public void refresh() {
        lezioneBox.removeAllItems();
        lezioniDocente = controller.getLezioniDocente(docenteCorrente);
        for (Lezione l : lezioniDocente)
            lezioneBox.addItem(l.getGiorno() + " " + l.getOraInizio() + "-" + l.getOraFine()
                    + " | " + l.getInsegnamento().getNome());
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
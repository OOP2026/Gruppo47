package GUI;

import Controller.Controller;
import Classi.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    // --- 1. COMPONENTI GESTITI DAL DESIGNER (.form) ---
    private JPanel mainPanel; // Il contenitore radice
    private JPanel contentPanel; // Il pannello vuoto al centro in cui ruoteremo le carte
    private JButton btnOrario;
    private JButton btnCreaLezione;
    private JButton btnRisorse;
    private JButton btnRichieste;
    private JButton btnInviaReq;
    private JButton btnStudente;

    // --- 2. LE TUE VARIABILI LOGICHE ---
    private CardLayout cardLayout;
    private OrarioTablePanel orarioTablePanel;
    private CreaLezionePanel creaLezionePanel;
    private InviaRichiestaPanel inviaRichiestaPanel;
    private GestisciRichiestaPanel gestisciRichiestePanel;
    private StudenteOrarioPanel studenteOrarioPanel;
    private GestioneRisorsePanel gestioneRisorsePanel;

    public static final String CARD_ORARIO       = "ORARIO";
    public static final String CARD_CREA_LEZIONE = "CREA_LEZIONE";
    public static final String CARD_INVIA_REQ    = "INVIA_REQ";
    public static final String CARD_GESTISCI_REQ = "GESTISCI_REQ";
    public static final String CARD_STUDENTE     = "STUDENTE";
    public static final String CARD_RISORSE      = "RISORSE";

    public MainFrame(Controller controller, Docente docenteCorrente, Studente studenteCorrente) {

        // --- CONFIGURAZIONE FINESTRA BASE ---
        setTitle("Gestione Orario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Imposto il pannello disegnato graficamente come contenuto della finestra!
        setContentPane(mainPanel);

        // --- CONFIGURAZIONE CARD LAYOUT ---
        // Applico il CardLayout al pannello centrale disegnato nel form
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        // --- INIZIALIZZAZIONE DEI SOTTO-PANNELLI ---
        orarioTablePanel       = new OrarioTablePanel(controller);
        creaLezionePanel       = new CreaLezionePanel(controller, this);
        inviaRichiestaPanel    = new InviaRichiestaPanel(controller, docenteCorrente, this);
        gestisciRichiestePanel = new GestisciRichiestaPanel(controller, this);
        studenteOrarioPanel    = new StudenteOrarioPanel(controller, studenteCorrente);
        gestioneRisorsePanel   = new GestioneRisorsePanel(controller, this);

        // Aggiungo i sotto-pannelli estraendo la loro vista grafica tramite getMainPanel()
        contentPanel.add(orarioTablePanel.getMainPanel(),       CARD_ORARIO);
        contentPanel.add(creaLezionePanel.getMainPanel(),        CARD_CREA_LEZIONE);
        contentPanel.add(inviaRichiestaPanel.getMainPanel(),     CARD_INVIA_REQ);
        contentPanel.add(gestisciRichiestePanel.getMainPanel(),  CARD_GESTISCI_REQ);
        contentPanel.add(studenteOrarioPanel.getMainPanel(),     CARD_STUDENTE);
        contentPanel.add(gestioneRisorsePanel.getMainPanel(),    CARD_RISORSE);

        // --- LISTENERS ---
        btnOrario.addActionListener(e      -> showCard(CARD_ORARIO));
        btnCreaLezione.addActionListener(e -> showCard(CARD_CREA_LEZIONE));
        btnRisorse.addActionListener(e     -> showCard(CARD_RISORSE));
        btnRichieste.addActionListener(e   -> showCard(CARD_GESTISCI_REQ));
        btnInviaReq.addActionListener(e    -> showCard(CARD_INVIA_REQ));
        btnStudente.addActionListener(e    -> showCard(CARD_STUDENTE));

        // Mostro la prima carta
        showCard(CARD_ORARIO);
    }

    // --- 3. I TUOI METODI DI UTILITA' INTATTI ---

    public void showCard(String cardName) {
        cardLayout.show(contentPanel, cardName);
        switch (cardName) {
            case CARD_ORARIO:        orarioTablePanel.refresh();        break;
            case CARD_GESTISCI_REQ:  gestisciRichiestePanel.refresh();  break;
            case CARD_INVIA_REQ:     inviaRichiestaPanel.refresh();     break;
            case CARD_CREA_LEZIONE:  creaLezionePanel.refresh();        break;
            case CARD_RISORSE:       gestioneRisorsePanel.refresh();    break;
            case CARD_STUDENTE:      studenteOrarioPanel.refresh();     break;
        }
    }

    public static void showError(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    public static void showSuccess(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "OK", JOptionPane.INFORMATION_MESSAGE);
    }
}
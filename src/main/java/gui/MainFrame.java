package gui;

import javax.swing.*;
import java.awt.*;
import controller.Controller;
import model.*;

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

    public MainFrame(Controller controller, Utente utenteLoggato) {

        // --- CONFIGURAZIONE FINESTRA BASE ---
        setTitle("Gestione Orario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Imposto il pannello disegnato graficamente come contenuto della finestra!
        setContentPane(mainPanel);

        // --- CONFIGURAZIONE CARD LAYOUT ---
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        // --- CORREZIONE: RICAVIAMO IL TIPO DI UTENTE ---
        Docente docenteCorrente = null;
        if (utenteLoggato instanceof Docente) {
            docenteCorrente = (Docente) utenteLoggato;
        }

        Studente studenteCorrente = null;
        if (utenteLoggato instanceof Studente) {
            studenteCorrente = (Studente) utenteLoggato;
        }

        // --- INIZIALIZZAZIONE DEI SOTTO-PANNELLI ---
        orarioTablePanel       = new OrarioTablePanel(controller);
        creaLezionePanel       = new CreaLezionePanel(controller, this);
        // Ora docenteCorrente e studenteCorrente esistono e hanno il valore corretto!
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

        // Imposta i permessi e mostra la carta corretta in base a chi si è loggato
        impostaPermessi(utenteLoggato);
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

    public void impostaPermessi(Utente u) {
        // 1. Prima nascondiamo tutto a tutti
        btnCreaLezione.setVisible(false);
        btnRisorse.setVisible(false);
        btnRichieste.setVisible(false);
        btnInviaReq.setVisible(false);
        btnStudente.setVisible(false);

        // 2. Poi diamo i poteri in base al ruolo
        if (u instanceof Responsabile) {
            btnCreaLezione.setVisible(true);
            btnRisorse.setVisible(true);
            btnRichieste.setVisible(true);
            btnInviaReq.setVisible(true);
            showCard(CARD_RISORSE); // Il prof parte vedendo le Risorse

        } else if (u instanceof Docente) {
            btnInviaReq.setVisible(true);
            showCard(CARD_ORARIO); // Il docente parte vedendo l'Orario generale

        } else if (u instanceof Studente) {
            btnStudente.setVisible(true);
            showCard(CARD_STUDENTE); // Lo studente parte vedendo il suo orario
        }
    }
}
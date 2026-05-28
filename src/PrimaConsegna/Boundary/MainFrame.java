package Boundary;

import Controller.*;
import Classi.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

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

    public MainFrame(GestioneOrarioController gestioneCtrl,
                     RichiestaController richiestaCtrl,
                     VisualizzaOrarioController visualizzaCtrl,
                     Docente docenteCorrente,
                     Studente studenteCorrente) {

        setTitle("Gestione Orario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Bottoni di navigazione in alto
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnOrario      = new JButton("Orario Generale");
        JButton btnCreaLezione = new JButton("Crea Lezione");
        JButton btnRisorse     = new JButton("Aule & Insegnamenti");
        JButton btnRichieste   = new JButton("Gestisci Richieste");
        JButton btnInviaReq    = new JButton("Invia Richiesta");
        JButton btnStudente    = new JButton("Orario Studente");

        navPanel.add(btnOrario);
        navPanel.add(btnCreaLezione);
        navPanel.add(btnRisorse);
        navPanel.add(btnRichieste);
        navPanel.add(btnInviaReq);
        navPanel.add(btnStudente);
        add(navPanel, BorderLayout.NORTH);

        // Area centrale
        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        orarioTablePanel       = new OrarioTablePanel(gestioneCtrl);
        creaLezionePanel       = new CreaLezionePanel(gestioneCtrl, this);
        inviaRichiestaPanel    = new InviaRichiestaPanel(richiestaCtrl, docenteCorrente, this);
        gestisciRichiestePanel = new GestisciRichiestaPanel(richiestaCtrl, this);
        studenteOrarioPanel    = new StudenteOrarioPanel(visualizzaCtrl, studenteCorrente);
        gestioneRisorsePanel   = new GestioneRisorsePanel(gestioneCtrl, this);

        contentPanel.add(orarioTablePanel,       CARD_ORARIO);
        contentPanel.add(creaLezionePanel,        CARD_CREA_LEZIONE);
        contentPanel.add(inviaRichiestaPanel,     CARD_INVIA_REQ);
        contentPanel.add(gestisciRichiestePanel,  CARD_GESTISCI_REQ);
        contentPanel.add(studenteOrarioPanel,     CARD_STUDENTE);
        contentPanel.add(gestioneRisorsePanel,    CARD_RISORSE);

        add(contentPanel, BorderLayout.CENTER);

        // Azioni bottoni navigazione
        btnOrario.addActionListener(e      -> showCard(CARD_ORARIO));
        btnCreaLezione.addActionListener(e -> showCard(CARD_CREA_LEZIONE));
        btnRisorse.addActionListener(e     -> showCard(CARD_RISORSE));
        btnRichieste.addActionListener(e   -> showCard(CARD_GESTISCI_REQ));
        btnInviaReq.addActionListener(e    -> showCard(CARD_INVIA_REQ));
        btnStudente.addActionListener(e    -> showCard(CARD_STUDENTE));

        showCard(CARD_ORARIO);
    }

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
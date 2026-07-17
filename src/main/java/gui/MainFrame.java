package gui;

import javax.swing.*;
import java.awt.*;
import controller.Controller;
import model.*;

/**
 * Finestra principale dell'applicazione (Boundary) che funge da contenitore (Hub) per tutte le altre viste.
 * <p>
 * Implementa un pattern di navigazione basato su {@link CardLayout}, permettendo di passare
 * dinamicamente da un pannello all'altro senza aprire nuove finestre. Gestisce inoltre le
 * autorizzazioni (Role-Based Access Control), mostrando o nascondendo le funzionalità in base
 * al tipo di {@link Utente} che ha effettuato l'accesso.
 * </p>
 */
public class MainFrame extends JFrame {

    // --- 1. COMPONENTI GESTITI DAL DESIGNER (.form) ---
    private JPanel mainPanel;
    private JPanel contentPanel;
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

    /** Identificativo testuale per il pannello dell'orario generale. */
    public static final String CARD_ORARIO       = "ORARIO";
    /** Identificativo testuale per il pannello di creazione lezioni. */
    public static final String CARD_CREA_LEZIONE = "CREA_LEZIONE";
    /** Identificativo testuale per il pannello di invio richieste. */
    public static final String CARD_INVIA_REQ    = "INVIA_REQ";
    /** Identificativo testuale per il pannello di gestione delle richieste in sospeso. */
    public static final String CARD_GESTISCI_REQ = "GESTISCI_REQ";
    /** Identificativo testuale per il pannello dell'orario specifico per lo studente. */
    public static final String CARD_STUDENTE     = "STUDENTE";
    /** Identificativo testuale per il pannello di gestione aule e insegnamenti. */
    public static final String CARD_RISORSE      = "RISORSE";

    /**
     * Inizializza la finestra principale, carica i pannelli figli nel CardLayout e
     * adegua l'interfaccia ai permessi dell'utente.
     *
     * @param controller    Il gestore centrale della logica di business.
     * @param utenteLoggato L'utente (Studente, Docente o Responsabile) che ha superato il login.
     */
    public MainFrame(Controller controller, Utente utenteLoggato) {

        // --- CONFIGURAZIONE FINESTRA BASE ---
        setTitle("Gestione Orario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);

        // --- CONFIGURAZIONE CARD LAYOUT ---
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        // --- RICAVIAMO IL TIPO DI UTENTE ---
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
        orarioTablePanel.setUtenteLoggato(utenteLoggato);
        creaLezionePanel       = new CreaLezionePanel(controller, this);
        inviaRichiestaPanel    = new InviaRichiestaPanel(controller, docenteCorrente, this);
        gestisciRichiestePanel = new GestisciRichiestaPanel(controller, this);
        studenteOrarioPanel    = new StudenteOrarioPanel(controller, studenteCorrente);
        gestioneRisorsePanel   = new GestioneRisorsePanel(controller, this);

        // Aggiungo i sotto-pannelli al CardLayout
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

        // Imposta i permessi
        impostaPermessi(utenteLoggato);
    }

    // --- 3. I TUOI METODI DI UTILITA' ---

    /**
     * Mostra uno specifico pannello all'interno della finestra principale,
     * nascondendo quello precedente e forzando l'aggiornamento dei dati (refresh)
     * del pannello appena attivato.
     *
     * @param cardName L'identificativo costante (es. {@link #CARD_ORARIO}) del pannello da mostrare.
     */
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

    /**
     * Metodo statico di utilità per visualizzare rapidamente un popup di errore
     * standardizzato in tutta l'applicazione.
     *
     * @param parent Il componente grafico padre su cui centrare il popup.
     * @param msg    Il messaggio di errore da mostrare all'utente.
     */
    public static void showError(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Metodo statico di utilità per visualizzare rapidamente un popup di successo
     * standardizzato in tutta l'applicazione.
     *
     * @param parent Il componente grafico padre su cui centrare il popup.
     * @param msg    Il messaggio di conferma da mostrare all'utente.
     */
    public static void showSuccess(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "OK", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Configura la visibilità dei pulsanti nel menu laterale e stabilisce la schermata
     * di partenza in base alla classe dell'utente (Studente, Docente o Responsabile).
     *
     * @param u L'utente attualmente autenticato le cui autorizzazioni devono essere verificate.
     */
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
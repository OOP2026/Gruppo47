package gui;

import javax.swing.*;
import java.util.List;
import controller.Controller;
import model.*;

/**
 * Pannello dell'interfaccia grafica (Boundary) per l'amministrazione delle risorse del sistema.
 * <p>
 * Permette agli utenti con privilegi elevati (come il {@link Responsabile}) di visualizzare,
 * creare e gestire le {@link Aula} e gli {@link Insegnamento}. La finestra è divisa in due sezioni
 * parallele con le rispettive liste e form di inserimento.
 * </p>
 */
public class GestioneRisorsePanel {

    // --- 1. COMPONENTI GESTITI DAL DESIGNER (.form) ---
    private JPanel mainPanel;
    private JList<String> aulaList;
    private JTextField nomeAulaField;
    private JButton aggiungiAulaBtn;
    private JList<String> insList;
    private JLabel JLabel1;
    private JTextField nomeInsField;
    private JLabel JLabel2;
    private JTextField cfuField;
    private JLabel JLabel3;
    private JComboBox<String> annoBox;
    private JLabel JLabel4;
    private JComboBox<String> docenteBox;
    private JButton aggiungiInsBtn;

    // --- 2. VARIABILI DI LOGICA ---
    private Controller controller;
    private MainFrame mainFrame;
    private DefaultListModel<String> aulaListModel;
    private DefaultListModel<String> insListModel;
    private List<Docente> docentiDisponibili;

    /**
     * Inizializza il pannello di gestione risorse, i modelli delle liste e i listener dei bottoni.
     *
     * @param controller Il gestore della logica di business.
     * @param mainFrame  Il frame principale per la gestione dei popup e della navigazione.
     */
    public GestioneRisorsePanel(Controller controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame = mainFrame;

        // --- INIZIALIZZAZIONE MODELLI LISTE ---
        aulaListModel = new DefaultListModel<>();
        aulaList.setModel(aulaListModel);

        insListModel = new DefaultListModel<>();
        insList.setModel(insListModel);

        for (AnnoCorso a : controller.getAnni()) {
            annoBox.addItem(a.name());
        }

        // --- LISTENERS ---
        aggiungiAulaBtn.addActionListener(e -> {
            String err = controller.aggiungiAula(nomeAulaField.getText().trim());
            if (err != null) MainFrame.showError(mainPanel, err);
            else {
                nomeAulaField.setText("");
                refresh();
            }
        });

        aggiungiInsBtn.addActionListener(e -> {
            int cfu;
            try {
                cfu = Integer.parseInt(cfuField.getText().trim());
            } catch (NumberFormatException ex) {
                MainFrame.showError(mainPanel, "CFU non valido.");
                return;
            }

            AnnoCorso anno = AnnoCorso.valueOf((String) annoBox.getSelectedItem());
            Docente titolare = null;
            int idx = docenteBox.getSelectedIndex();
            if (docentiDisponibili != null && idx >= 0 && idx < docentiDisponibili.size()) {
                titolare = docentiDisponibili.get(idx);
            }

            String err = controller.aggiungiInsegnamento(nomeInsField.getText().trim(), cfu, anno, titolare);
            if (err != null) MainFrame.showError(mainPanel, err);
            else {
                nomeInsField.setText("");
                cfuField.setText("6");
                refresh();
            }
        });
    }

    // --- 3. METODI ---

    /**
     * Aggiorna la lista dei docenti disponibili nel menu a tendina per l'assegnazione
     * di un nuovo insegnamento.
     *
     * @param docenti La lista aggiornata dei docenti registrati nel sistema.
     */
    public void setDocentiDisponibili(List<Docente> docenti) {
        this.docentiDisponibili = docenti;
        docenteBox.removeAllItems();
        for (Docente d : docenti)
            docenteBox.addItem(d.getNome() + " " + d.getCognome());
    }

    /**
     * Sincronizza le liste grafiche (aule e insegnamenti) con i dati attualmente
     * presenti in memoria (RAM) tramite il controller. Pulisce e ricrea i modelli delle liste.
     */
    public void refresh() {
        aulaListModel.clear();
        for (Aula a : controller.getAule())
            aulaListModel.addElement(a.getNome());

        insListModel.clear();
        for (Insegnamento i : controller.getInsegnamenti())
            insListModel.addElement(i.getNome() + " (" + i.getCFU() + " CFU, " + i.getAnno() + ")");

        docentiDisponibili = controller.getDocenti();
        docenteBox.removeAllItems();
        if (docentiDisponibili != null) {
            for (Docente d : docentiDisponibili) {
                docenteBox.addItem(d.getNome() + " " + d.getCognome());
            }
        }
    }

    /**
     * Restituisce il contenitore grafico principale di questo pannello.
     *
     * @return Il {@link JPanel} radice configurato tramite il GUI designer.
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
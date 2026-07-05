package GUI;

import Controller.Controller;
import Classi.*;

import javax.swing.*;
import java.util.List;

public class GestioneRisorsePanel {

    // --- 1. COMPONENTI GESTITI DAL DESIGNER (.form) ---
    private JPanel mainPanel;

    // Sezione Aule
    private JList<String> aulaList;
    private JTextField nomeAulaField;
    private JButton aggiungiAulaBtn;

    // Sezione Insegnamenti
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

    public GestioneRisorsePanel(Controller controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame  = mainFrame;

        // --- INIZIALIZZAZIONE MODELLI LISTE ---
        aulaListModel = new DefaultListModel<>();
        aulaList.setModel(aulaListModel); // Collego il modello alla lista grafica

        insListModel = new DefaultListModel<>();
        insList.setModel(insListModel); // Collego il modello alla lista grafica

        // Popolamento combo box anni
        for (AnnoCorso a : controller.getAnni()) {
            annoBox.addItem(a.name());
        }

        // --- LISTENERS ---
        // Listener aggiunta Aula
        aggiungiAulaBtn.addActionListener(e -> {
            String err = controller.aggiungiAula(nomeAulaField.getText().trim());
            if (err != null) MainFrame.showError(mainPanel, err); // Sostituito this
            else {
                nomeAulaField.setText("");
                refresh();
            }
        });

        // Listener aggiunta Insegnamento
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
    public void setDocentiDisponibili(List<Docente> docenti) {
        this.docentiDisponibili = docenti;
        docenteBox.removeAllItems();
        for (Docente d : docenti)
            docenteBox.addItem(d.getNome() + " " + d.getCognome());
    }

    public void refresh() {
        aulaListModel.clear();
        for (Aula a : controller.getAule())
            aulaListModel.addElement(a.getNome());

        insListModel.clear();
        for (Insegnamento i : controller.getInsegnamenti())
            insListModel.addElement(i.getNome() + " (" + i.getCFU() + " CFU, " + i.getAnno() + ")");
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
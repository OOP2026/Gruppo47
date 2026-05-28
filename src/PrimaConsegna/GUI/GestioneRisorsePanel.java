package GUI;
import Controller.Controller;


import Classi.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GestioneRisorsePanel extends JPanel {

    private Controller controller;
    private MainFrame mainFrame;

    private JTextField nomeAulaField;
    private DefaultListModel<String> aulaListModel;

    private JTextField nomeInsField;
    private JTextField cfuField;
    private JComboBox<String> annoBox;
    private JComboBox<String> docenteBox;
    private DefaultListModel<String> insListModel;

    private List<Docente> docentiDisponibili;

    public GestioneRisorsePanel(Controller controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame  = mainFrame;
        setLayout(new GridLayout(1, 2, 10, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildAulePanel());
        add(buildInsegnamentiPanel());
    }

    private JPanel buildAulePanel() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createTitledBorder("Aule"));

        aulaListModel = new DefaultListModel<>();
        p.add(new JScrollPane(new JList<>(aulaListModel)), BorderLayout.CENTER);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nomeAulaField = new JTextField(12);
        JButton addBtn = new JButton("Aggiungi");
        form.add(new JLabel("Nome:"));
        form.add(nomeAulaField);
        form.add(addBtn);
        p.add(form, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            String err = controller.aggiungiAula(nomeAulaField.getText().trim());
            if (err != null) MainFrame.showError(this, err);
            else { nomeAulaField.setText(""); refresh(); }
        });
        return p;
    }

    private JPanel buildInsegnamentiPanel() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createTitledBorder("Insegnamenti"));

        insListModel = new DefaultListModel<>();
        p.add(new JScrollPane(new JList<>(insListModel)), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(5, 2, 4, 4));
        nomeInsField = new JTextField();
        cfuField     = new JTextField("6");
        annoBox      = new JComboBox<>();
        docenteBox   = new JComboBox<>();
        for (AnnoCorso a : controller.getAnni())
            annoBox.addItem(a.name());

        form.add(new JLabel("Nome:"));    form.add(nomeInsField);
        form.add(new JLabel("CFU:"));     form.add(cfuField);
        form.add(new JLabel("Anno:"));    form.add(annoBox);
        form.add(new JLabel("Docente:")); form.add(docenteBox);

        JButton addBtn = new JButton("Aggiungi Insegnamento");
        form.add(new JLabel());
        form.add(addBtn);
        p.add(form, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            int cfu;
            try { cfu = Integer.parseInt(cfuField.getText().trim()); }
            catch (NumberFormatException ex) { MainFrame.showError(this, "CFU non valido."); return; }

            AnnoCorso anno = AnnoCorso.valueOf((String) annoBox.getSelectedItem());
            Docente titolare = null;
            int idx = docenteBox.getSelectedIndex();
            if (docentiDisponibili != null && idx >= 0 && idx < docentiDisponibili.size())
                titolare = docentiDisponibili.get(idx);

            String err = controller.aggiungiInsegnamento(nomeInsField.getText().trim(), cfu, anno, titolare);
            if (err != null) MainFrame.showError(this, err);
            else { nomeInsField.setText(""); cfuField.setText("6"); refresh(); }
        });
        return p;
    }

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
}
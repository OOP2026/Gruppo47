package GUI;
import Controller.Controller;


import Classi.*;

import javax.swing.*;
import java.awt.*;

public class CreaLezionePanel extends JPanel {

    private Controller controller;
    private MainFrame mainFrame;

    private JComboBox<String> insegnamentoBox;
    private JComboBox<String> giornoBox;
    private JComboBox<String> aulaBox;
    private JTextField oraInizioField;
    private JTextField oraFineField;

    public CreaLezionePanel(Controller controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame  = mainFrame;
        setLayout(new GridLayout(6, 2, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        insegnamentoBox = new JComboBox<>();
        giornoBox = new JComboBox<>();
        for (GiornoSettimana g : controller.getGiorni())
            giornoBox.addItem(g.name());
        aulaBox = new JComboBox<>();
        oraInizioField = new JTextField("09:00");
        oraFineField   = new JTextField("11:00");

        add(new JLabel("Insegnamento:"));  add(insegnamentoBox);
        add(new JLabel("Giorno:"));        add(giornoBox);
        add(new JLabel("Ora inizio:"));    add(oraInizioField);
        add(new JLabel("Ora fine:"));      add(oraFineField);
        add(new JLabel("Aula:"));          add(aulaBox);

        JButton creaBtn = new JButton("Crea Lezione");
        add(new JLabel());
        add(creaBtn);

        creaBtn.addActionListener(e -> {
            String errore = controller.creaLezione(
                    (String) insegnamentoBox.getSelectedItem(),
                    (String) giornoBox.getSelectedItem(),
                    oraInizioField.getText().trim(),
                    oraFineField.getText().trim(),
                    (String) aulaBox.getSelectedItem()
            );
            if (errore != null) {
                MainFrame.showError(this, errore);
            } else {
                MainFrame.showSuccess(this, "Lezione creata con successo!");
                mainFrame.showCard(MainFrame.CARD_ORARIO);
            }
        });
    }

    public void refresh() {
        insegnamentoBox.removeAllItems();
        for (Insegnamento i : controller.getInsegnamenti())
            insegnamentoBox.addItem(i.getNome());

        aulaBox.removeAllItems();
        for (Aula a : controller.getAule())
            aulaBox.addItem(a.getNome());
    }
}
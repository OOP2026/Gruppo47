package Boundary;

import Controller.RichiestaController;
import Classi.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InviaRichiestaPanel extends JPanel {

    private RichiestaController controller;
    private Docente docenteCorrente;
    private MainFrame mainFrame;

    private JComboBox<String> lezioneBox;
    private JComboBox<String> giornoBox;
    private JTextField oraInizioField;
    private JTextField oraFineField;

    private List<Lezione> lezioniDocente;

    public InviaRichiestaPanel(RichiestaController controller, Docente docenteCorrente, MainFrame mainFrame) {
        this.controller      = controller;
        this.docenteCorrente = docenteCorrente;
        this.mainFrame       = mainFrame;
        setLayout(new GridLayout(5, 2, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lezioneBox     = new JComboBox<>();
        giornoBox      = new JComboBox<>();
        for (GiornoSettimana g : controller.getGiorni())
            giornoBox.addItem(g.name());
        oraInizioField = new JTextField("09:00");
        oraFineField   = new JTextField("11:00");

        add(new JLabel("Lezione da spostare:")); add(lezioneBox);
        add(new JLabel("Nuovo giorno:"));        add(giornoBox);
        add(new JLabel("Nuova ora inizio:"));    add(oraInizioField);
        add(new JLabel("Nuova ora fine:"));      add(oraFineField);

        JButton inviaBtn = new JButton("Invia Richiesta");
        add(new JLabel());
        add(inviaBtn);

        inviaBtn.addActionListener(e -> {
            int idx = lezioneBox.getSelectedIndex();
            if (idx < 0 || lezioniDocente == null || idx >= lezioniDocente.size()) {
                MainFrame.showError(this, "Seleziona una lezione.");
                return;
            }
            String errore = controller.inviaRichiesta(
                    lezioniDocente.get(idx),
                    (String) giornoBox.getSelectedItem(),
                    oraInizioField.getText().trim(),
                    oraFineField.getText().trim()
            );
            if (errore != null) MainFrame.showError(this, errore);
            else MainFrame.showSuccess(this, "Richiesta inviata.");
        });
    }

    public void refresh() {
        lezioneBox.removeAllItems();
        lezioniDocente = controller.getLezioniDocente(docenteCorrente);
        for (Lezione l : lezioniDocente)
            lezioneBox.addItem(l.getGiorno() + " " + l.getOraInizio() + "-" + l.getOraFine()
                    + " | " + l.getInsegnamento().getNome());
    }
}
package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn;

    private Controller controller;

    public LoginFrame(Controller controller) {
        this.controller = controller;

        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Il metodo verificaLogin ti restituisce lo Studente, Docente o Responsabile
                // nascosto dietro al "velo" della classe astratta Utente
                Utente u = controller.verificaLogin(username, password);

                if (u != null) {
                    // Login corretto: chiudo questa finestra
                    dispose();
                    // Apro il MainFrame passandogli la variabile "u"
                    MainFrame main = new MainFrame(controller, u);
                    main.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Credenziali errate!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
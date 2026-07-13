package Test;

import javax.swing.*;
import java.time.LocalTime;
import model.*;
import controller.Controller;
import gui.*;

public class Main {

    public static void main(String[] args) {

        // ── Entity ────────────────────────────────────────────────────────────
        Orario orarioGenerale = new Orario();

        // ── Control ───────────────────────────────────────────────────────────
        Controller controller = new Controller(orarioGenerale);
        controller.caricaDatiAllAvvio();

        // ── Boundary (Avvio Grafica) ──────────────────────────────────────────
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame(controller);
            login.setVisible(true);
        });
    }
}
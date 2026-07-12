package implementazioneDao;

import dao.UtenteDAO;
import database_connection.ConnessioneDatabase;
import model.*;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtentePostgresDao implements UtenteDAO {

    private Connection connection;

    public UtentePostgresDao() {
        try {
            connection = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Utente verificaLogin(String login, String password) {
        Utente utenteTrovato = null;
        String sql = "SELECT * FROM \"Utente\" WHERE \"Login\" = ? AND \"Password\" = ?;";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, login);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("Nome");
                String cognome = rs.getString("Cognome");
                String email = rs.getString("Email");
                String ruolo = rs.getString("Ruolo");

                Orario orarioVuoto = new Orario();

                if (ruolo.equals("STUDENTE")) {
                    String matricola = rs.getString("Matricola");
                    AnnoCorso anno = AnnoCorso.valueOf(rs.getString("AnnoCorso"));
                    utenteTrovato = new Studente(nome, cognome, email, password, login, anno, matricola, orarioVuoto);
                } else if (ruolo.equals("DOCENTE")) {
                    utenteTrovato = new Docente(nome, cognome, email, password, login, orarioVuoto);
                } else if (ruolo.equals("RESPONSABILE")) {
                    utenteTrovato = new Responsabile(nome, cognome, email, password, login, orarioVuoto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenteTrovato;
    }

    @Override
    public List<Docente> caricaTuttiDocenti() {
        List<Docente> docenti = new java.util.ArrayList<>();
        // Cerchiamo sia DOCENTE sia RESPONSABILE, visto che il Responsabile può insegnare
        String sql = "SELECT * FROM \"Utente\" WHERE \"Ruolo\" = 'DOCENTE' OR \"Ruolo\" = 'RESPONSABILE'";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("Nome");
                String cognome = rs.getString("Cognome");
                String email = rs.getString("Email");
                String login = rs.getString("Login");
                String password = rs.getString("Password");
                String ruolo = rs.getString("Ruolo");

                Orario orarioVuoto = new Orario();

                if (ruolo.equals("DOCENTE")) {
                    docenti.add(new Docente(nome, cognome, email, password, login, orarioVuoto));
                } else if (ruolo.equals("RESPONSABILE")) {
                    docenti.add(new Responsabile(nome, cognome, email, password, login, orarioVuoto));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return docenti;
    }
}
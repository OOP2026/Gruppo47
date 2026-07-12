package dao;
import model.Utente;
import model.Docente;
import java.util.List;

public interface UtenteDAO {
    Utente verificaLogin(String login, String password);
    List<Docente> caricaTuttiDocenti(); // <-- NUOVO METODO
}
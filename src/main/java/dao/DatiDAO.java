package dao;

import model.*;
import java.util.List;
import java.time.LocalTime;

public interface DatiDAO {
    // Gestione Aule
    void salvaAula(Aula aula);
    List<Aula> caricaAule();

    // Gestione Insegnamenti
    void salvaInsegnamento(Insegnamento ins);
    List<Insegnamento> caricaInsegnamenti(List<Docente> docentiDisponibili);

    // Gestione Lezioni
    void salvaLezione(Lezione lezione);
    List<Lezione> caricaLezioni(List<Insegnamento> insegnamenti, List<Aula> aule);

    // Gestione Richieste e Aggiornamenti
    void salvaRichiesta(RichiestaSpostamento richiesta);
    List<RichiestaSpostamento> caricaRichieste(List<Lezione> lezioni);
    void aggiornaStatoRichiesta(RichiestaSpostamento richiesta);
    void aggiornaLezioneDopoSpostamento(Lezione vecchiaLezione, GiornoSettimana nuovoGiorno, LocalTime nuovoInizio, LocalTime nuovaFine);

    // Eliminazione lezioni
    void eliminaLezione(Lezione lezione);
}
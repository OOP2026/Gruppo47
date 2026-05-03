package Test;

import Classi.*;
import java.time.LocalTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. INIZIALIZZAZIONE SISTEMA
        Orario orarioGenerale = new Orario();

        // 2. CREAZIONE UTENTI (Studenti, Docenti, Responsabile)
        Responsabile resp = new Responsabile("Sara", "Zattera", "sara@univ.it", "pwd123", "saraz", orarioGenerale);
        Docente profNeri = new Docente("Marco", "Neri", "neri@univ.it", "pwd456", "marcon", orarioGenerale);
        Studente stud = new Studente("Mario", "Rossi", "mario@stud.it", "pwd789", "marior", AnnoCorso.I, "N8600123", orarioGenerale);

        System.out.println("--- TEST 1: Definizione Insegnamenti e Aule (Responsabile) ---");
        resp.inserisciAula("Aula A1"); //
        resp.inserisciAula("Aula B2");

        // Il responsabile definisce gli insegnamenti per l'anno accademico
        resp.definisciInsegnamento("Programmazione Java", 9, AnnoCorso.I, resp);
        resp.definisciInsegnamento("Basi di Dati", 6, AnnoCorso.II, profNeri);

        Aula a1 = resp.getelencoAule().get(0);
        Insegnamento java = resp.getelencoInsegnamenti().get(0);
        Insegnamento database = resp.getelencoInsegnamenti().get(1);

        System.out.println("\n--- TEST 2: Creazione Lezioni e Gestione Conflitti ---");
        // Creazione corretta
        resp.creaLezione(java, GiornoSettimana.lunedi, LocalTime.of(9, 0), LocalTime.of(11, 0), a1);

        // Tentativo di conflitto (stessa aula, stesso orario)
        System.out.print("Tentativo lezione sovrapposta in Aula A1: ");
        resp.creaLezione(database, GiornoSettimana.lunedi, LocalTime.of(10, 0), LocalTime.of(12, 0), a1);

        // Visualizzazione conflitti
        resp.visualizzaConflitti();

        System.out.println("\n--- TEST 3: Visualizzazione Orari (Studente e Docente) ---");
        // Lo studente vede solo il suo anno
        stud.visualizzaOrarioCorso();

        // Il docente (Neri) vede le sue lezioni
        // Aggiungiamo una lezione per il prof Neri
        resp.creaLezione(database, GiornoSettimana.martedi, LocalTime.of(14, 0), LocalTime.of(16, 0), a1);
        profNeri.visualizzaOrario();

        System.out.println("\n--- TEST 4: Richiesta Spostamento Lezione (Docente) ---");
        // Il docente Neri vuole spostare la sua lezione di Martedì a Mercoledì
        Lezione lezioneNeri = orarioGenerale.getLezioniPerDocente(profNeri).get(0);
        profNeri.richiediSpostamento(lezioneNeri, GiornoSettimana.mercoledi, LocalTime.of(11, 0), LocalTime.of(13, 0));

        // Il responsabile visualizza le richieste
        resp.visualizzaRichiestaSpostamento();

        System.out.println("\n--- TEST 5: Gestione Richiesta e Modifica Orario (Responsabile) ---");
        RichiestaSpostamento richiesta = orarioGenerale.getRichiesteSpostamento().get(0);

        // Il responsabile approva la richiesta
        // Questo internamente chiama modificaOrario()
        resp.gestisciRichiestaSpostamento(richiesta, true);

        System.out.println("\n--- RIEPILOGO FINALE ORARIO GENERALE ---");
        for (Lezione l : orarioGenerale.getLezioni()) {
            System.out.println(l.getGiorno() + " " + l.getOraInizio() + "-" + l.getOraFine() +
                    " | " + l.getInsegnamento().getNome() + " | Aula: " + l.getAula().getNome());
        }
    }
}
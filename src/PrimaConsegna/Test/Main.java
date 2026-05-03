package Test;
import Classi.Aula;
import Classi.Insegnamento;
import Classi.Lezione;
import Classi.Orario;
import Classi.Responsabile;
import Classi.AnnoCorso;
import Classi.GiornoSettimana;

import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        //Creazione oggetto Orario
        Orario orarioGenerale = new Orario();

        //Creazione Responsabile
        Responsabile resp = new Responsabile("Sara", "Zattera", "sarazattera8@gmail.com", "180226", "Sarina", orarioGenerale);

        //Test metodo inserisciAula
        resp.inserisciAula("A1");
        resp.inserisciAula("A2");

        //Verifica
        for (Aula a : resp.getelencoAule()) {
            System.out.println("-Nome aula: " + a.getNome());
        }
        //------------------------------------------------------------------------------------
        //Test metodo definisciInsegnamento
        resp.definisciInsegnamento("Algebra", 6, AnnoCorso.I, resp);

        //Verifica
        for (Insegnamento ins : resp.getelencoInsegnamenti()) {
            System.out.println("- Insegnamento: " + ins.getNome() + " (" + ins.getCFU() + " CFU)");
            System.out.println("  Titolare: " + ins.getDocenteTitolare().getCognome());
        }
        //-----------------------------------------------------------------------------------

        // --- TEST 1: Inserimento corretto ---
        Insegnamento ins1 = resp.getelencoInsegnamenti().get(0);
        Aula aula1 = resp.getelencoAule().get(0);

        System.out.println("Tentativo 1: Lunedì 09:00 - 11:00");
        resp.creaLezione(ins1, GiornoSettimana.lunedi, LocalTime.of(9, 0), LocalTime.of(11, 0), aula1);

        // --- TEST 2: Conflitto di Aula (stesso giorno, orario sovrapposto) ---
        // Proviamo a inserire un'altra lezione in Aula A1 dalle 10:00 alle 12:00
        System.out.println("\nTentativo 2 (Conflitto Aula): Lunedì 10:00 - 12:00");
        resp.creaLezione(ins1, GiornoSettimana.lunedi, LocalTime.of(10, 0), LocalTime.of(12, 0), aula1);

        // 4. VERIFICA FINALE
        // Stampiamo le lezioni presenti nell'oggetto Orario
        System.out.println("\n--- Riepilogo Orario Finale ---");
        for (Lezione l : orarioGenerale.getLezioni()) {
            System.out.println("- " + l.getInsegnamento().getNome() + " in " + l.getAula().getNome() +
                    " [" + l.getGiorno() + " " + l.getOraInizio() + "-" + l.getOraFine() + "]");

        }

        //TEST metodi di visualizzazione orario
    }
}
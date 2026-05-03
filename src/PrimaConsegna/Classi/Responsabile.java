package Classi;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;

public class Responsabile extends Docente{
    private List<Aula> elencoAule;
    private List<Insegnamento> elencoInsegnamenti;
    private Orario orarioGenerale;

    public Responsabile(String nome, String cognome, String mail, String password, String login, Orario orario){
        super(nome, cognome, mail, password, login, orario);
        this.elencoAule = new ArrayList<>();
        this.elencoInsegnamenti = new ArrayList<>();
        this.orarioGenerale = orario;
    }

    //Get e Set
    public List<Aula> getelencoAule(){
        return elencoAule;
    }
    public void setelencoAule(List<Aula> elencoAule){
        this.elencoAule = elencoAule;
    }

    public List<Insegnamento> getelencoInsegnamenti(){
        return elencoInsegnamenti;
    }
    public void setelencoInsegnamenti(List<Insegnamento> elencoInsegnamenti){
        this.elencoInsegnamenti = elencoInsegnamenti;
    }

    //Metodi
    public void definisciInsegnamento(String nome, int CFU, AnnoCorso anno, Docente titolare){
        Insegnamento nuovoInsegnamento = new Insegnamento(nome, CFU, anno, titolare);
        this.elencoInsegnamenti.add(nuovoInsegnamento);
        System.out.println("Insegnamento "+nome+" aggiunto con successo");
    }

    public void inserisciAula(String nomeAula){
        Aula nuovaAula = new Aula(nomeAula);
        this.elencoAule.add(nuovaAula);
    }

    public void creaLezione(Insegnamento ins, GiornoSettimana g, LocalTime inizio, LocalTime fine, Aula aula){
        Lezione nuovaLezione = new Lezione(g, inizio, fine, ins, aula);

        if(!orarioGenerale.haConflitti(nuovaLezione)){
            orarioGenerale.aggiungiLezione(nuovaLezione);
            System.out.println("Lezione aggiunta correttamente");
        } else{
            System.out.println("Lezione non aggiunta");
        }
    }

    public void visualizzaRichiestaSpostamento(){
        System.out.println("\n---ELENCO RICHIESTE SPOSTAMENTO---");
        for(RichiestaSpostamento r : orarioGenerale.getRichiesteSpostamento()){
            System.out.println("Lezione: "+ r.getLezione().getInsegnamento().getNome() +
                    "| Proposta: "+ r.getGiornoProposto() + "ore "+ r.getOraProposta() +
                    "| Stato: "+ r.getStato());
        }
    }

    public void visualizzaConflitti(){
        System.out.println("\n--- VERIFICA CONFLITTI ORARIO ---");
        boolean conflittiTrovati = false;
        List<Lezione> lezioni = orarioGenerale.getLezioni();

        for (int i = 0; i < lezioni.size(); i++) {
            for (int j = i + 1; j < lezioni.size(); j++) {
                Lezione l1 = lezioni.get(i);
                Lezione l2 = lezioni.get(j);

                if (l1.getGiorno().equals(l2.getGiorno())) {
                    boolean sovrapposizioneOraria = l1.getOraInizio().isBefore(l2.getOraFine()) &&
                            l1.getOraFine().isAfter(l2.getOraInizio());

                    if (sovrapposizioneOraria) {
                        if (l1.getAula().equals(l2.getAula())) {
                            System.out.println("CONFLITTO AULA [" + l1.getAula().getNome() + "]: " +
                                    l1.getInsegnamento().getNome() + " e " + l2.getInsegnamento().getNome());
                            conflittiTrovati = true;
                        }
                        if (l1.getInsegnamento().getDocenteTitolare().equals(l2.getInsegnamento().getDocenteTitolare())) {
                            System.out.println("CONFLITTO DOCENTE [" + l1.getInsegnamento().getDocenteTitolare().getCognome() + "]: " +
                                    l1.getInsegnamento().getNome() + " e " + l2.getInsegnamento().getNome());
                            conflittiTrovati = true;
                        }
                    }
                }
            }
        }
        if (!conflittiTrovati) System.out.println("Nessun conflitto rilevato.");
    }

    public void gestisciRichiestaSpostamento(RichiestaSpostamento richiesta, boolean approva){
        if (approva) {
            // Calcoliamo l'ora di fine basandoci sulla durata originale della lezione
            long durataMinuti = java.time.Duration.between(richiesta.getLezione().getOraInizio(), richiesta.getLezione().getOraFine()).toMinutes();
            LocalTime nuovaFine = richiesta.getOraProposta().plusMinutes(durataMinuti);

            // Creiamo una lezione temporanea per testare i conflitti
            Lezione ipotesi = new Lezione(richiesta.getGiornoProposto(), richiesta.getOraProposta(), nuovaFine,
                    richiesta.getLezione().getInsegnamento(), richiesta.getLezione().getAula());

            // Verifichiamo se lo spostamento è possibile (escludendo la lezione originale dal controllo)
            orarioGenerale.rimuoviLezione(richiesta.getLezione());

            if (!orarioGenerale.haConflitti(ipotesi)) {
                modificaOrario(richiesta.getLezione(), richiesta.getGiornoProposto(), richiesta.getOraProposta(), nuovaFine);
                richiesta.setStato(StatoRichiesta.approvata);
                System.out.println("Richiesta approvata e orario aggiornato.");
            } else {
                // Se c'è conflitto, rimettiamo la lezione originale e rifiutiamo o segnaliamo
                orarioGenerale.aggiungiLezione(richiesta.getLezione());
                System.out.println("Impossibile approvare: lo spostamento genera conflitti.");
            }
        } else {
            richiesta.setStato(StatoRichiesta.rifiutata);
            System.out.println("Richiesta rifiutata.");
        }
    }

    public void modificaOrario(Lezione lezione, GiornoSettimana g, LocalTime inizio, LocalTime fine) {
        lezione.setGiorno(g);
        lezione.setOraInizio(inizio);
        lezione.setOraFine(fine);
        // Se non era già presente (es. chiamata da gestisciRichiesta), la riaggiungiamo
        if (!orarioGenerale.getLezioni().contains(lezione)) {
            orarioGenerale.aggiungiLezione(lezione);
        }
    }
}

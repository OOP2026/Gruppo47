package Classi;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;

public class Responsabile extends Docente{
    private List<Aula> elencoAule;
    private List<Insegnamento> elencoInsegnamenti;
    private Orario orarioGenerale;

    public Responsabile(String nome, String cognome, String mail, String password, String login, Orario orario){
        super(nome, cognome, mail, password, login);
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
}

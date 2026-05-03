package Classi;
import java.time.LocalTime;

public class Lezione {
    private GiornoSettimana giorno;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private Insegnamento insegnamento;
    private Aula aula;

    public Lezione(GiornoSettimana giorno, LocalTime oraInizio, LocalTime oraFine, Insegnamento insegnamento, Aula aula) {
        this.giorno = giorno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.insegnamento = insegnamento;
        this.aula = aula;
    }

    //Get e Set
    public  GiornoSettimana getGiorno() {
        return giorno;
    }
    public void setGiorno(GiornoSettimana giorno) {
        this.giorno = giorno;
    }
    public LocalTime getOraInizio() {
        return oraInizio;
    }
    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }
    public LocalTime getOraFine() {
        return oraFine;
    }
    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
    }
    public Insegnamento getInsegnamento() {
        return insegnamento;
    }
    public void setInsegnamento(Insegnamento insegnamento) {
        this.insegnamento = insegnamento;
    }
    public Aula getAula() {
        return aula;
    }
    public void setAula(Aula aula) {
        this.aula = aula;
    }

}

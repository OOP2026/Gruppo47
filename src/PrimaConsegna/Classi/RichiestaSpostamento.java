package Classi;
import java.time.LocalTime;

public class RichiestaSpostamento {
    private GiornoSettimana giornoProposto;
    private LocalTime oraProposta;
    private StatoRichiesta stato;
    private Lezione lezione;

    public RichiestaSpostamento(GiornoSettimana giornoProposto, LocalTime oraProposta, StatoRichiesta stato, Lezione lezioneRiferimento) {
        this.giornoProposto = giornoProposto;
        this.oraProposta = oraProposta;
        this.stato = StatoRichiesta.in_attesa;
        this.lezione = lezione;
    }

    //Get e Set
    public GiornoSettimana getGiornoProposto() {
        return giornoProposto;
    }
    public void setGiornoProposto(GiornoSettimana giornoProposto) {
        this.giornoProposto = giornoProposto;
    }

    public LocalTime getOraProposta() {
        return oraProposta;
    }
    public void setOraProposta(LocalTime oraProposta) {
        this.oraProposta = oraProposta;
    }

    public StatoRichiesta getStato() {
        return stato;
    }
    public void setStato(StatoRichiesta stato) {
        this.stato = stato;

    }
    public Lezione getLezione() {
        return lezione;
    }
    public void setLezione(Lezione lezione) {
        this.lezione = lezione;
    }
}

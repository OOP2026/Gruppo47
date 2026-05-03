package Classi;
import java.time.LocalTime;

public class RichiestaSpostamento {
    private GiornoSettimana giornoProposto;
    private LocalTime oraInizioProposta;
    private LocalTime oraFineProposta;
    private StatoRichiesta stato;
    private Lezione lezione;

    public RichiestaSpostamento(GiornoSettimana giornoProposto, LocalTime oraInizioProposta,LocalTime oraFineProposta, StatoRichiesta stato, Lezione lezioneRiferimento) {
        this.giornoProposto = giornoProposto;
        this.oraInizioProposta = oraInizioProposta;

        this.stato = StatoRichiesta.in_attesa;
        this.lezione = lezioneRiferimento;
    }

    //Get e Set
    public GiornoSettimana getGiornoProposto() {
        return giornoProposto;
    }
    public void setGiornoProposto(GiornoSettimana giornoProposto) {
        this.giornoProposto = giornoProposto;
    }

    public LocalTime getOraProposta() {
        return oraInizioProposta;
    }
    public void setOraProposta(LocalTime oraProposta) {
        this.oraInizioProposta = oraProposta;
    }

    public LocalTime getOraFineProposta() {
        return oraFineProposta;
    }
    public void setOraFineProposta(LocalTime oraFineProposta) {
        this.oraFineProposta = oraFineProposta;
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

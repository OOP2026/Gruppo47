package Classi;
import java.util.List;
import java.util.ArrayList;

public class Orario {
    private List<Lezione> lezioni;

    public Orario() {
        this.lezioni = new ArrayList<>();
    }

    public List<Lezione> getLezioni() {
        return lezioni;
    }

    public void setLezioni(List<Lezione> lezioni) {
        this.lezioni = lezioni;
    }

    public boolean haConflitti(Lezione nuova) {
        for (Lezione esistente : lezioni) {
            // 1. Controllo stessa aula nello stesso giorno
            if (esistente.getGiorno().equals(nuova.getGiorno()) &&
                    esistente.getAula().equals(nuova.getAula())) {

                // Controllo sovrapposizione oraria con LocalTime
                if (nuova.getOraInizio().isBefore(esistente.getOraFine()) &&
                        nuova.getOraFine().isAfter(esistente.getOraInizio())) {
                    return true; // Conflitto Aula!
                }
            }

            // 2. Controllo stesso docente nello stesso giorno
            if (esistente.getGiorno().equals(nuova.getGiorno()) &&
                    esistente.getInsegnamento().getDocenteTitolare().equals(nuova.getInsegnamento().getDocenteTitolare())) {

                if (nuova.getOraInizio().isBefore(esistente.getOraFine()) &&
                        nuova.getOraFine().isAfter(esistente.getOraInizio())) {
                    return true; // Conflitto Docente!
                }
            }
        }
        return false;
    }

    public void aggiungiLezione(Lezione l) {
        this.lezioni.add(l);
    }
}

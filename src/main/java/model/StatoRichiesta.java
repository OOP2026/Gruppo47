package model;

/**
 * Rappresenta i possibili stati di avanzamento di una {@link RichiestaSpostamento}.
 * <p>
 * Questo enumerato viene utilizzato per tracciare e gestire il ciclo di vita
 * delle proposte di modifica dell'orario inoltrate dai docenti e valutate
 * dal {@link Responsabile}.
 * </p>
 */
public enum StatoRichiesta {

    /**
     * La richiesta è stata appena creata e inviata al sistema.
     * È in attesa di essere presa in carico e valutata dal responsabile.
     */
    in_attesa,

    /**
     * La richiesta è stata approvata.
     * Non sono stati rilevati conflitti e l'orario è stato aggiornato con successo.
     */
    approvata,

    /**
     * La richiesta è stata respinta (es. a causa di conflitti di orario insormontabili).
     * L'orario originale rimane inalterato.
     */
    rifiutata;
}
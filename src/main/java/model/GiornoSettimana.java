package model;

/**
 * Rappresenta i giorni lavorativi della settimana accademica.
 * <p>
 * Questo enumerato viene utilizzato per collocare temporalmente una {@link Lezione}
 * all'interno dell'{@link Orario} generale. Sono esclusi i weekend in quanto
 * non sono previste attività didattiche ordinarie.
 * </p>
 */
public enum GiornoSettimana {

    /**
     * Primo giorno della settimana accademica.
     */
    lunedi,

    /**
     * Secondo giorno della settimana accademica.
     */
    martedi,

    /**
     * Terzo giorno della settimana accademica.
     */
    mercoledi,

    /**
     * Quarto giorno della settimana accademica.
     */
    giovedi,

    /**
     * Quinto e ultimo giorno della settimana accademica.
     */
    venerdi;
}
package model;

/**
 * Rappresenta un'aula fisica o virtuale in cui si svolgono le lezioni.
 * <p>
 * Questa classe definisce lo spazio assegnato per un determinato evento
 * nel sistema di gestione dell'orario.
 * </p>
 */
public class Aula {

    private String nome;

    /**
     * Costruisce una nuova aula.
     *
     * @param nome Il nome o codice identificativo dell'aula (es. "Aula Magna", "Laboratorio 1").
     */
    public Aula(String nome) {
        this.nome = nome;
    }

    // --- GET E SET ---

    /**
     * Restituisce il nome dell'aula.
     *
     * @return Il nome identificativo dell'aula.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta o modifica il nome dell'aula.
     *
     * @param nome Il nuovo nome dell'aula.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
}
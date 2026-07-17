package model;

/**
 * Rappresenta un corso o materia di studio erogata all'interno dell'università.
 * <p>
 * Collega un'area tematica (nome e crediti) all'anno di corso di riferimento
 * e al {@link Docente} titolare incaricato di svolgere le lezioni.
 * </p>
 */
public class Insegnamento {
    private String nome;
    private int CFU;
    private AnnoCorso anno;
    private Docente docenteTitolare;

    /**
     * Costruisce un nuovo insegnamento.
     *
     * @param nome            Il nome per esteso della materia.
     * @param CFU             I Crediti Formativi Universitari associati all'insegnamento.
     * @param anno            L'anno di corso (es. primo, secondo) a cui è rivolta la materia.
     * @param docenteTitolare Il docente responsabile dell'insegnamento.
     */
    public Insegnamento(String nome, int CFU, AnnoCorso anno, Docente docenteTitolare) {
        this.nome = nome;
        this.CFU = CFU;
        this.anno = anno;
        this.docenteTitolare = docenteTitolare;
    }

    // --- GET E SET ---

    /**
     * Restituisce il nome della materia.
     *
     * @return Il nome dell'insegnamento.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta o modifica il nome dell'insegnamento.
     *
     * @param nome Il nuovo nome della materia.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce i Crediti Formativi Universitari (CFU) dell'insegnamento.
     *
     * @return Il numero di CFU.
     */
    public int getCFU() {
        return CFU;
    }

    /**
     * Imposta o modifica i CFU dell'insegnamento.
     *
     * @param CFU Il nuovo valore dei crediti formativi.
     */
    public void setCFU(int CFU) {
        this.CFU = CFU;
    }

    /**
     * Restituisce l'anno di corso associato all'insegnamento.
     *
     * @return L'anno di corso di erogazione.
     */
    public AnnoCorso getAnno() {
        return anno;
    }

    /**
     * Imposta o modifica l'anno di corso dell'insegnamento.
     *
     * @param anno Il nuovo anno di corso.
     */
    public void setAnno(AnnoCorso anno) {
        this.anno = anno;
    }

    /**
     * Restituisce il docente titolare della materia.
     *
     * @return Il docente responsabile.
     */
    public Docente getDocenteTitolare() {
        return docenteTitolare;
    }

    /**
     * Imposta o sostituisce il docente titolare dell'insegnamento.
     *
     * @param docenteTitolare Il nuovo docente responsabile.
     */
    public void setDocenteTitolare(Docente docenteTitolare) {
        this.docenteTitolare = docenteTitolare;
    }
}
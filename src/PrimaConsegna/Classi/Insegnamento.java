package Classi;

public class Insegnamento {
    private String nome;
    private int CFU;
    private AnnoCorso anno;
    private Docente docenteTitolare;

    public Insegnamento(String nome, int CFU, AnnoCorso anno, Docente docenteTitolare) {
        this.nome = nome;
        this.CFU = CFU;
        this.anno = anno;
        this.docenteTitolare = docenteTitolare;
    }

    //Get e Set
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public int getCFU() {
        return CFU;
    }
    public void setCFU(int CFU) {
        this.CFU = CFU;
    }
    public AnnoCorso getAnno() {
        return anno;
    }
    public void setAnno(AnnoCorso anno) {
        this.anno = anno;
    }
    public Docente getDocenteTitolare() {
        return docenteTitolare;
    }
    public void setDocenteTitolare(Docente docenteTitolare) {
        this.docenteTitolare = docenteTitolare;
    }
}

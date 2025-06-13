package util;

/** Autor: Marc Gil Moreno
 */

public class Anchor {
    private int fila;
    private int columna;
    private int crossCheck;
    private int maxLeft;

    public Anchor() {
        int fila  = 0;
        int columna = 0;
        int maxLeft = 0;
    }
    public Anchor(int fila, int columna, int crossCheck, int maxLeft) {
        this.fila = fila;
        this.columna = columna;
        this.crossCheck = crossCheck;
        this.maxLeft = maxLeft;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }
    public void setFila(int fila) {
        this.fila = fila;
    }
    public void setCrossCheck(int crossCheck) {
        this.crossCheck = crossCheck;
    }
    public void setMaxLeft(int maxLeft) {
        this.maxLeft = maxLeft;
    }

    public int getColumna() {
        return columna;
    }

    public int getFila() {
        return fila;
    }
    public int getCrossCheck() {
        return crossCheck;
    }
    public int getMaxLeft() {
        return maxLeft;
    }
}

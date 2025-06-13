package util;

/**
 * Classe Anchor.
 * Representa una posició d'ancoratge per a la generació de jugades al tauler d'Scrabble.
 * Gestiona la fila, columna, crossCheck i maxLeft.
 * Autor: Marc Gil Moreno
 */
public class Anchor {
    // ---------- ATRIBUTS ----------
    private int fila;
    private int columna;
    private int crossCheck;
    private int maxLeft;

    // ---------- CONSTRUCTORS ----------
    /**
     * Constructora per defecte. Inicialitza tots els valors a 0.
     */
    public Anchor() {
        int fila = 0;
        int columna = 0;
        int maxLeft = 0;
    }

    /**
     * Constructora amb paràmetres.
     * @param fila Fila de l'anchor.
     * @param columna Columna de l'anchor.
     * @param crossCheck Valor de crossCheck.
     * @param maxLeft Valor màxim a l'esquerra.
     */
    public Anchor(int fila, int columna, int crossCheck, int maxLeft) {
        this.fila = fila;
        this.columna = columna;
        this.crossCheck = crossCheck;
        this.maxLeft = maxLeft;
    }

    // ---------- GETTERS ----------
    /**
     * Retorna la columna de l'anchor.
     * @return Columna.
     */
    public int getColumna() {
        return columna;
    }

    /**
     * Retorna el crossCheck de l'anchor.
     * @return crossCheck.
     */
    public int getCrossCheck() {
        return crossCheck;
    }

    /**
     * Retorna la fila de l'anchor.
     * @return Fila.
     */
    public int getFila() {
        return fila;
    }

    /**
     * Retorna el maxLeft de l'anchor.
     * @return maxLeft.
     */
    public int getMaxLeft() {
        return maxLeft;
    }

    // ---------- SETTERS ----------
    /**
     * Assigna la columna de l'anchor.
     * @param columna Nova columna.
     */
    public void setColumna(int columna) {
        this.columna = columna;
    }

    /**
     * Assigna el crossCheck de l'anchor.
     * @param crossCheck Nou crossCheck.
     */
    public void setCrossCheck(int crossCheck) {
        this.crossCheck = crossCheck;
    }

    /**
     * Assigna la fila de l'anchor.
     * @param fila Nova fila.
     */
    public void setFila(int fila) {
        this.fila = fila;
    }

    /**
     * Assigna el maxLeft de l'anchor.
     * @param maxLeft Nou maxLeft.
     */
    public void setMaxLeft(int maxLeft) {
        this.maxLeft = maxLeft;
    }
}

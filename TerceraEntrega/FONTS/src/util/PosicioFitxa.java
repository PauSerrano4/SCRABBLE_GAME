package util;

/**
 * Classe PosicioFitxa.
 * Representa la posició (fila i columna) i la lletra col·locada al tauler, així com si és un comodí (joker).
 * @author Alexander de Jong
 */
public class PosicioFitxa {
    // ---------- ATRIBUTS ----------
    private final int fila;
    private final int col;
    private final char lletra;
    private final boolean joker;   // fals per defecte

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe PosicioFitxa.
     * @param fila Fila on es col·loca la fitxa.
     * @param col Columna on es col·loca la fitxa.
     * @param lletra Lletra col·locada.
     * @param joker Cert si la fitxa és un comodí.
     */
    public PosicioFitxa(int fila, int col, char lletra, boolean joker) {
        this.fila = fila;
        this.col  = col;
        this.lletra = lletra;
        this.joker = joker;
    }

    // ---------- GETTERS ----------
    /**
     * Retorna la columna de la posició.
     * @return Columna.
     */
    public int getCol() { return col; }

    /**
     * Retorna la fila de la posició.
     * @return Fila.
     */
    public int getFila() { return fila; }

    /**
     * Retorna la lletra col·locada.
     * @return Lletra.
     */
    public char getLletra() { return lletra; }

    /**
     * Indica si la fitxa és un comodí.
     * @return Cert si és comodí, fals altrament.
     */
    public boolean isJoker() { return joker; }
}

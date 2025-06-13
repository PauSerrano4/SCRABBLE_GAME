package util;

/**
 * Classe Play.
 * Representa una jugada vàlida generada per la IA.
 * Conté la paraula, l'anchor on comença, l'orientació i la seva puntuació.
 * Autor: Marc Gil Moreno
 */
public class Play {
    // ---------- ATRIBUTS ----------
    private final String word;
    private final int fila;
    private final int columna;
    private final boolean transposed;
    private final int score;

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe Play.
     *
     * @param word       Paraula jugada.
     * @param fila       Fila on comença la paraula.
     * @param columna    Columna on comença la paraula.
     * @param transposed Cert si és un moviment horitzontal (across), fals si vertical (down).
     * @param score      Puntuació calculada per aquesta jugada.
     */
    public Play(String word, int fila, int columna, boolean transposed, int score) {
        this.word = word;
        this.fila = fila;
        this.columna = columna;
        this.transposed = transposed;
        this.score = score;
    }

    // ---------- GETTERS ----------
    /**
     * Retorna la columna on comença la jugada.
     * @return Columna inicial.
     */
    public int getColumna() {
        return columna;
    }

    /**
     * Retorna la fila on comença la jugada.
     * @return Fila inicial.
     */
    public int getFila() {
        return fila;
    }

    /**
     * Retorna la puntuació de la jugada.
     * @return Puntuació.
     */
    public int getScore() {
        return score;
    }

    /**
     * Retorna la paraula jugada.
     * @return Paraula jugada.
     */
    public String getWord() {
        return word;
    }

    /**
     * Indica si la jugada és horitzontal.
     * @return Cert si és horitzontal, fals si és vertical.
     */
    public boolean isHorizontal() {
        return !transposed;
    }

    // ---------- MÈTODES ----------
    /**
     * Retorna una representació en format String de la jugada.
     * @return String amb la informació de la jugada.
     */
    @Override
    public String toString() {
        return String.format("Play[word=%s, fila=%s, columna=%s, horizontal=%s, score=%d]",
                word, fila, columna, !transposed, score);
    }
}

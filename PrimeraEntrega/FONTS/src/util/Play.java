package util;

/**Autor: Marc Gil Moreno
*/

/**
 * Representa una jugada válida generada por la IA.
 * Contiene la palabra, el anchor donde comienza, la orientación y su puntuación.
 */
public class Play {
    private final String word;
    private final int fila;
    private final int columna;
    private final boolean transposed;
    private final int score;

    /**
     * Constructor de Play.
     *
     * @param word       la palabra jugada
     * @param fila       fila donde comienza la palabra
     * @columna          columna donde comienza la palabra
     * @param transposed true si es un movimiento horizontal (across), false si vertical (down)
     * @param score      la puntuación calculada para esta jugada
     */
    public Play(String word, int fila, int columna, boolean transposed, int score) {
        this.word = word;
        this.fila = fila;
        this.columna = columna;
        this.transposed = transposed;
        this.score = score;
    }

    public String getWord() {
        return word;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    /**
     * transposed indica si hay que transponer o no el tablero,
     * y solo es necesario transponerlo si la palabra no es horizontal.
     * Por eso devuelve el negado
     * @return
     */
    public boolean isHorizontal() {
        return transposed;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return String.format("Play[word=%s, fila=%s, columna=%s, horizontal=%s, score=%d]",
                word, fila, columna, !transposed, score);
    }
}

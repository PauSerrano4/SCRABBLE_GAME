package util;

import java.io.Serializable;

/**
 * Classe Pair.
 * Classe auxiliar per facilitar la gestió de parelles d'elements.
 * @author Pau Serrano Sanz
 */
public class Pair<T, U> implements Serializable {

    // ---------- ATRIBUTS ----------
    public final T first;
    public final U second;

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe Pair.
     * @param first Primer element de la parella.
     * @param second Segon element de la parella.
     */
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    // ---------- MÈTODES ----------
    /**
     * Retorna una representació en format String de la parella.
     * @return String amb els dos elements de la parella.
     */
    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
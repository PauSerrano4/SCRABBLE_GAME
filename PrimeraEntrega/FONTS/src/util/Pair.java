package util;

/** 
 * Classe Pair.java
 * Classe auxiliar per facilitar la gestió de parelles d'elements.
 * @autor Pau Serrano Sanz | pau.serrano.sanz@estudiantat.upc.edu
 */

public class Pair<T, U> {
    public final T first;
    public final U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
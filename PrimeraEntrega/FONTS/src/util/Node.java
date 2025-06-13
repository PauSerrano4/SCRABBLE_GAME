/** Autor: Marc Gil Moreno
 */
package util;

import java.util.*;

public class Node {
    Map<Character, Node> fills = new TreeMap<>();
    boolean finalDeParaula = false;

    public Map<Character, Node> getFills() {
        return fills;
    }

    public boolean isFinalDeParaula() {
        return finalDeParaula;
    }

    public void setFinalDeParaula(boolean finalDeParaula) {
        this.finalDeParaula = finalDeParaula;
    }

    public Node getFill(char c) {
        return fills.get(c);
    }

    public void afegirFill(char c, Node node) {
        fills.put(c, node);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node other)) return false;
        return finalDeParaula == other.finalDeParaula && fills.equals(other.fills);
    }

    @Override
    public int hashCode() {
        return Objects.hash(finalDeParaula, fills);
    }
}
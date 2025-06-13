/** Autor: Marc Gil Moreno
 */
package util;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Classe Node.
 * Representa un node d'un DAWG (Directed Acyclic Word Graph) per gestionar paraules.
 * Autor: Marc Gil Moreno
 */
public class Node implements Serializable {

    // ---------- ATRIBUTS ----------
    Map<Character, Node> fills = new TreeMap<>();
    boolean finalDeParaula = false;

    // ---------- CONSTRUCTORS ----------
    // Constructor per defecte (implícit)

    // ---------- GETTERS ----------
    /**
     * Retorna el node fill associat a un caràcter.
     * @param c Caràcter pel qual es vol obtenir el fill.
     * @return Node fill corresponent o null si no existeix.
     */
    public Node getFill(char c) {
        return fills.get(c);
    }

    /**
     * Retorna el mapa de fills d'aquest node.
     * @return Map de caràcters a nodes fills.
     */
    public Map<Character, Node> getFills() {
        return fills;
    }

    /**
     * Indica si aquest node és final de paraula.
     * @return Cert si és final de paraula, fals altrament.
     */
    public boolean isFinalDeParaula() {
        return finalDeParaula;
    }

    // ---------- SETTERS ----------
    /**
     * Assigna si aquest node és final de paraula.
     * @param finalDeParaula Valor booleà a assignar.
     */
    public void setFinalDeParaula(boolean finalDeParaula) {
        this.finalDeParaula = finalDeParaula;
    }

    // ---------- MÈTODES PÚBLICS ----------
    /**
     * Afegeix un fill a aquest node associat a un caràcter.
     * @param c Caràcter pel qual s'afegeix el fill.
     * @param node Node fill a afegir.
     */
    public void afegirFill(char c, Node node) {
        fills.put(c, node);
    }

    /**
     * Comprova si aquest node és igual a un altre objecte.
     * @param obj Objecte a comparar.
     * @return Cert si són iguals, fals altrament.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node other)) return false;
        return finalDeParaula == other.finalDeParaula && fills.equals(other.fills);
    }

    /**
     * Retorna el codi hash d'aquest node.
     * @return Codi hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(finalDeParaula, fills);
    }
}
package model;

import util.Pair;
import java.util.*;
import util.Node;

/** 
 * Classe Diccionari.java
 * Conté tots els mots d'un diccionari d'Scrabble.
 * Implementat fent servir un DAWG (Directed Acyclic Word Graph) construït a partir d'una llista de paraules ordenades.
 * @autor Pau Serrano Sanz | pau.serrano.sanz@estudiantat.upc.edu
 */
public class Diccionari {
    private String idioma;
    private Set<String> paraules;
    private Map<String, Pair<Integer, Integer>> alfabet; // lletra -> (quantitat, puntuació)
    private Node arrel;
    private Map<Node, Node> registre;
    private String ultimaParaulaAfegida;

    //----------CONSTRUCTORES----------//
    /**
     * Constructora de la classe diccionari.
     * @param idioma Nom de l'idioma del diccionari.
     * @param paraulesOrdenades Llista ordenada lexicogràficament de paraules.
     * @param alfabet Mapa de lletres amb la seva quantitat i puntuació.
     */
    public Diccionari(String idioma, List<String> paraulesOrdenades, Map<String, Pair<Integer, Integer>> alfabet) {
        this.idioma = idioma;
        this.paraules = new TreeSet<>(paraulesOrdenades);
        this.arrel = new Node();
        this.registre = new HashMap<>();
        this.ultimaParaulaAfegida = "";
        this.alfabet = alfabet;
        construirDAWG(); 
    }

    /**
     * Construeix el DAWG a partir de les paraules ordenades.
     */
    private void construirDAWG() {
        for (String paraula : paraules) {
            afegirParaulaIncremental(paraula);
        }
        reemplaçarORegistrar(arrel);
    }

    //----------CONSULTORES----------//

    public String getIdioma() {
        return idioma;
    }

    /**
     * Retorna la llista de paraules del diccionari
     */
    public Set<String> getParaules() {
        return paraules;
    }

    /**
     * Retorna l'alfabet utilitzat al diccionari.
     * Cada entrada conté la lletra com a clau, i un parell (quantitat, puntuació).
     * @return Un mapa de lletres a (quantitat, puntuació).
     */
    public Map<String, Pair<Integer, Integer>> getAlfabet() {
        return alfabet;
    }

    /**
     * Retorna el node arrel del DAWG utilitzat pel diccionari.
     * @return El node arrel del graf acíclic de paraules.
     */
    public Node getArrel() {
        return arrel;
    }

    /**
     * Mostra totes les paraules del diccionari per consola.
     */
    public void mostraParaules() {
        List<String> resultat = new ArrayList<>();
        recorre(arrel, "", resultat);
        for (String paraula : resultat) {
            System.out.println(paraula);
        }
    }

    /**
     * Mostra per consola l'alfabet i les puntuacions/quantitats de cada lletra.
     */
    public void mostrarAlfabet() {
        System.out.println("Alfabet del diccionari (" + idioma + "):");
        for (Map.Entry<String, Pair<Integer, Integer>> entrada : alfabet.entrySet()) {
            String lletra = entrada.getKey();
            int quantitat = entrada.getValue().first;
            int puntuacio = entrada.getValue().second;
            System.out.println("Lletra: " + lletra + " | Quantitat: " + quantitat + " | Puntuació: " + puntuacio);
        }
    }

    /**
     * Comprova si una paraula existeix al diccionari.
     * @param paraula Paraula a validar.
     * @return true si la paraula existeix, false altrament.
     */
    public boolean validarParaula(String paraula) {
        Node node = arrel;
        for (char c : paraula.toCharArray()) {
            node = node.getFill(c);
            if (node == null) return false;
        }
        return node.isFinalDeParaula();
    }

    //----------MODIFICADORES----------//
    /**
     * Afegeix una nova paraula al diccionari. Ha d'estar en ordre lexicogràfic.
     * @param paraula Paraula nova a afegir.
     * @throws IllegalArgumentException si la paraula està desordenada.
     */
    public void afegirParaula(String paraula) {
        if (paraules.contains(paraula)) return;

        if (!ultimaParaulaAfegida.isEmpty() && paraula.compareTo(ultimaParaulaAfegida) < 0) {
            throw new IllegalArgumentException("Les paraules han d'estar ordenades alfabèticament.");
        }

        paraules.add(paraula);
        afegirParaulaIncremental(paraula);
    }

    /**
     * Elimina una paraula del diccionari.
     * @param paraula Paraula a eliminar.
     */
    public void eliminarParaula(String paraula) {
        if (!paraules.contains(paraula)) return;

        paraules.remove(paraula);

        if (paraula.equals(ultimaParaulaAfegida)) {
            this.ultimaParaulaAfegida = paraules.isEmpty() ? "" : ((TreeSet<String>) paraules).last();
        }

        reconstruirDAWG();
    }

    /**
     * Reconstrueix tot el DAWG des de zero a partir de la llista de paraules.
     */
    private void reconstruirDAWG() {
        this.arrel = new Node();
        this.registre = new HashMap<>();
        this.ultimaParaulaAfegida = "";
        construirDAWG();
    }

    /**
     * Afegeix una paraula de manera incremental al DAWG.
     * @param paraula Paraula a afegir.
     */
    private void afegirParaulaIncremental(String paraula) {
        int prefixComu = prefixComu(paraula, ultimaParaulaAfegida);
        Node nodeActual = arrel;

        for (int i = 0; i < prefixComu; i++) {
            nodeActual = nodeActual.getFill(paraula.charAt(i));
        }

        reemplaçarORegistrar(nodeActual);

        for (int i = prefixComu; i < paraula.length(); i++) {
            Node nou = new Node();
            nodeActual.afegirFill(paraula.charAt(i), nou);
            nodeActual = nou;
        }

        nodeActual.setFinalDeParaula(true);
        ultimaParaulaAfegida = paraula;
    }

    /**
     * Registra un node al DAWG o el reemplaça per un node ja existent equivalent.
     * @param node Node a processar.
     */
    private void reemplaçarORegistrar(Node node) {
        for (Map.Entry<Character, Node> entrada : node.getFills().entrySet()) {
            reemplaçarORegistrar(entrada.getValue());
        }

        for (Map.Entry<Character, Node> entrada : node.getFills().entrySet()) {
            Node fill = entrada.getValue();
            Node existent = registre.get(fill);
            if (existent != null) {
                entrada.setValue(existent);
            } else {
                registre.put(fill, fill);
            }
        }
    }

    /**
     * Recorre el DAWG i afegeix paraules completes a una llista de resultats.
     * @param node Node actual.
     * @param prefix Prefix actual construït.
     * @param resultat Llista on s'afegeixen les paraules trobades.
     */
    private void recorre(Node node, String prefix, List<String> resultat) {
        if (node.isFinalDeParaula()) resultat.add(prefix);
        for (Map.Entry<Character, Node> entrada : node.getFills().entrySet()) {
            recorre(entrada.getValue(), prefix + entrada.getKey(), resultat);
        }
    }

    /**
     * Calcula la longitud del prefix comú entre dues cadenes.
     * @param a Primera cadena.
     * @param b Segona cadena.
     * @return Nombre de caràcters iguals des de l'inici.
     */
    private int prefixComu(String a, String b) {
        int i = 0;
        while (i < a.length() && i < b.length() && a.charAt(i) == b.charAt(i)) i++;
        return i;
    }
}

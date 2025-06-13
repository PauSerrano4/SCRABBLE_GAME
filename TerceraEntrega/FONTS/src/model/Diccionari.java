package model;

import util.Pair;
import java.util.*;
import util.Node;
import java.io.Serializable;

/**
 * Classe Diccionari.java
 * Conté tots els mots d'un diccionari d'Scrabble.
 * Implementat fent servir un DAWG (Directed Acyclic Word Graph) construït a partir d'una llista de paraules ordenades.
 * @author Pau Serrano Sanz | pau.serrano.sanz@estudiantat.upc.edu
 */
public class Diccionari implements Serializable {

        // ---------- ATRIBUTS ----------
    /** Nom de l'idioma del diccionari (per exemple: "catalan", "castellano", "english") */
    private String idioma;
    /** Mapa de l'alfabet amb cada lletra i la seva quantitat i puntuació: lletra -> (quantitat, puntuació) */
    private Map<String, Pair<Integer, Integer>> alfabet;
    /** Node arrel del DAWG (Directed Acyclic Word Graph) utilitzat per emmagatzemar les paraules */
    private Node arrel;
    /** Registre de nodes compartits per optimitzar l'estructura del DAWG */
    private Map<Node, Node> registre;
    /** Última paraula afegida al diccionari, utilitzada per mantenir l'ordre lexicogràfic */
    private String ultimaParaulaAfegida;

        // ---------- CONSTRUCTORES ----------
    /**
     * Constructora de la classe diccionari.
     * @param idioma Nom de l'idioma del diccionari.
     * @param paraulesOrdenades Llista ordenada lexicogràficament de paraules.
     * @param alfabet Mapa de lletres amb la seva quantitat i puntuació.
     */
    public Diccionari(String idioma, List<String> paraulesOrdenades, Map<String, Pair<Integer, Integer>> alfabet) {
        this.idioma = idioma;
        this.arrel = new Node();
        this.registre = new HashMap<>();
        this.ultimaParaulaAfegida = "";
        this.alfabet = alfabet;
        construirDAWG(paraulesOrdenades);
    }

    // ---------- GETTERS ----------
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
     * Retorna l'idioma del diccionari.
     * @return Idioma del diccionari.
     */
    public String getIdioma() {
        return idioma;
    }

    /**
     * Retorna totes les paraules del diccionari ordenades alfabèticament.
     * @return Llista de paraules ordenades.
     */
    public List<String> getParaules() {
        List<String> resultat = new ArrayList<>();
        recorre(arrel, "", resultat);
        return resultat;
    }

    /**
     * Retorna el nom del diccionari (idioma).
     * @return Idioma del diccionari.
     */
    public String getNom() {
        return idioma;
    }

    // ---------- CONSULTORS ----------
    /**
     * Mostra per consola l'alfabet i les puntuacions/quantitats de cada lletra.
     * Funcio per a depuració i visualització de l'alfabet del diccionari.
     */
    public void mostrarAlfabet() {
        //System.out.println("Alfabet del diccionari (" + idioma + "):");
        for (Map.Entry<String, Pair<Integer, Integer>> entrada : alfabet.entrySet()) {
            String lletra = entrada.getKey();
            int quantitat = entrada.getValue().first;
            int puntuacio = entrada.getValue().second;
            //System.out.println("Lletra: " + lletra + " | Quantitat: " + quantitat + " | Puntuació: " + puntuacio);
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

    // ---------- MODIFICADORS ----------
    /**
     * Afegeix una nova paraula al diccionari. Ha d'estar en ordre lexicogràfic.
     * @param paraula Paraula nova a afegir.
     * @throws IllegalArgumentException si la paraula està desordenada.
     */
    public void afegirParaula(String paraula) {
        if (validarParaula(paraula)) return;
        // La paraula ja hi és, no cal fer res

        if (!ultimaParaulaAfegida.isEmpty() && paraula.compareTo(ultimaParaulaAfegida) < 0) {
            throw new IllegalArgumentException("Les paraules han d'estar ordenades alfabèticament.");
        }

        afegirParaulaIncremental(paraula);
    }

    // ---------- MÈTODES PRIVATS ----------
    /**
     * Construeix el DAWG a partir d'una llista de paraules ordenades.
     * @param paraulesOrdenades Llista de paraules ordenades.
     */
    private void construirDAWG(List<String> paraulesOrdenades) {
        for (String paraula : paraulesOrdenades) {
            afegirParaulaIncremental(paraula);
        }
        reemplaçarORegistrar(arrel);
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

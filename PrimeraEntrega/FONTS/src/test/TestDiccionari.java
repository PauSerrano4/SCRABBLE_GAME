import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import model.Diccionari;
import util.Pair;
import util.Node;


/**
 * Classe de testeig per a la classe Diccionari.Java
 * @autor Pau Serrano Sanz (pau.serrano.sanz@estudiantat.upc.edu)
 */

public class TestDiccionari {

    private Diccionari diccionari;
    private Map<String, Pair<Integer, Integer>> alfabet;

    @Before
    public void setUp() {
        List<String> paraules = Arrays.asList("abac", "abc", "acost", "acte");

        alfabet = new TreeMap<>();
        alfabet.put("a", new Pair<>(9, 1));
        alfabet.put("b", new Pair<>(2, 3));
        alfabet.put("c", new Pair<>(2, 3));
        alfabet.put("o", new Pair<>(2, 1));
        alfabet.put("s", new Pair<>(4, 1));
        alfabet.put("t", new Pair<>(5, 1));
        alfabet.put("e", new Pair<>(3, 1));

        diccionari = new Diccionari("català", paraules, alfabet);
    }

    @Test
    public void testIdioma() {
        assertEquals("català", diccionari.getIdioma());
    }

    @Test
    public void testValidarParaulaExistents() {
        assertTrue(diccionari.validarParaula("abac"));
        assertTrue(diccionari.validarParaula("abc"));
        assertTrue(diccionari.validarParaula("acost"));
        assertTrue(diccionari.validarParaula("acte"));
    }

    @Test
    public void testValidarParaulaInexistents() {
        assertFalse(diccionari.validarParaula("ab")); // prefix
        assertFalse(diccionari.validarParaula("aba")); // subprefix
        assertFalse(diccionari.validarParaula("zebra")); // fora alfabet
        assertFalse(diccionari.validarParaula("")); // buida
    }

    @Test
    public void testAfegirParaulaCorrectament() {
        diccionari.afegirParaula("actua");
        assertTrue(diccionari.validarParaula("actua"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAfegirParaulaDesordenadaLlençaExcepcio() {
        diccionari.afegirParaula("aa"); // abans de "acte"
    }

    @Test
    public void testAfegirParaulaDuplicadaNoFaRes() {
        int sizeInicial = diccionari.getParaules().size();
        diccionari.afegirParaula("abc"); // ja hi és
        assertEquals(sizeInicial, diccionari.getParaules().size());
    }

    @Test
    public void testEliminarParaulaExistents() {
        assertTrue(diccionari.validarParaula("abc"));
        diccionari.eliminarParaula("abc");
        assertFalse(diccionari.validarParaula("abc"));
    }

    @Test
    public void testEliminarParaulaInexistentNoTrenca() {
        diccionari.eliminarParaula("noexisteix");
        assertTrue(diccionari.validarParaula("abac")); // res es trenca
    }

    @Test
    public void testGetParaulesComprovaConjunt() {
        Set<String> paraules = diccionari.getParaules();
        assertEquals(4, paraules.size());
        assertTrue(paraules.contains("abac"));
        assertTrue(paraules.contains("abc"));
    }

    @Test
    public void testMostrarAlfabet() {
        Map<String, Pair<Integer, Integer>> alf = diccionari.getAlfabet();
        assertEquals(7, alf.size());
        assertEquals((Integer) 9, alf.get("a").first);
        assertEquals((Integer) 2, alf.get("b").first);
        assertEquals((Integer) 3, alf.get("c").second);
    }

    @Test
    public void testAfegirEliminarSuccessives() {
        diccionari.afegirParaula("actua");
        diccionari.afegirParaula("actuar");
        diccionari.afegirParaula("actuat");
        assertTrue(diccionari.validarParaula("actuat"));

        diccionari.eliminarParaula("actuar");
        assertFalse(diccionari.validarParaula("actuar"));
        assertTrue(diccionari.validarParaula("actuat")); // comprova no trencar altres
    }

    @Test
    public void testEliminarTotesLesParaules() {
        for (String paraula : new ArrayList<>(diccionari.getParaules())) {
            diccionari.eliminarParaula(paraula);
        }

        assertTrue(diccionari.getParaules().isEmpty());
        assertFalse(diccionari.validarParaula("abac"));
    }
}

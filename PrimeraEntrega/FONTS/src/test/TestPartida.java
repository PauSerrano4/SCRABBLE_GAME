import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import model.Diccionari;
import model.Fitxa;
import model.Jugador;
import model.Partida;
import util.Pair;
import model.Tauler;



/**
 * Tests per la classe Partida.java
 * @autor Pau Serrano Sanz (pau.serrano.sanz@estudiantat.upc.edu)
 */
public class TestPartida {

    private Partida partida;
    private Jugador jugador1;
    private Jugador jugador2;
    private Tauler tauler;
    private Diccionari diccionari;

    @Before
    public void setUp() {
        jugador1 = new Jugador("Bot1");
        jugador2 = new Jugador("Bot2");

        Map<String, Pair<Integer, Integer>> alfabet = new HashMap<>();
        alfabet.put("A", new Pair<>(3, 1));
        alfabet.put("B", new Pair<>(2, 3));
        alfabet.put("C", new Pair<>(1, 4));

        List<String> paraules = Arrays.asList("ab", "ba", "cab", "bac");

        diccionari = new Diccionari("catala", paraules, alfabet);
        tauler = new Tauler(15);

        partida = new Partida(1, List.of(jugador1, jugador2), tauler, diccionari);
    }

    @Test
    public void testCreacioCorrectaPartida() {
        assertEquals(1, partida.getIdPartida());
        assertEquals("Bot1", partida.getTornJugador());
        assertEquals("enCurs", partida.getEstat());
        assertEquals(tauler, partida.getTauler());
        assertEquals(diccionari, partida.getDiccionari());
        assertEquals(6, partida.getBossa().size()); // 3A + 2B + 1C
    }

    @Test
    public void testAvancarTorn() {
        assertEquals("Bot1", partida.getTornJugador());
        partida.avançarTorn();
        assertEquals("Bot2", partida.getTornJugador());
        partida.avançarTorn();
        assertEquals("Bot1", partida.getTornJugador()); // ha de tornar a començar
    }

    @Test
    public void testRepartirFitxesJugadorExistent() {
        partida.repartirFitxes("Bot1", 4);
        assertEquals(4, jugador1.getFitxes().size());
        assertEquals(2, partida.getBossa().size()); // 6 - 4 = 2
    }

    @Test
    public void testRepartirMesFitxesQueHiHa() {
        partida.repartirFitxes("Bot1", 10);
        assertEquals(6, jugador1.getFitxes().size()); // només n'hi ha 6
        assertEquals(0, partida.getBossa().size());
    }

    @Test
    public void testRepartirFitxesJugadorInexistent() {
        partida.repartirFitxes("NoExisteix", 3);
        assertEquals(6, partida.getBossa().size()); // no s'ha tret cap fitxa
        assertEquals(0, jugador1.getFitxes().size());
    }

    @Test
    public void testEstatsPartida() {
        partida.pausarPartida();
        assertEquals("pausada", partida.getEstat());

        partida.continuarPartida();
        assertEquals("enCurs", partida.getEstat());

        partida.finalitzarPartida();
        assertEquals("finalitzada", partida.getEstat());
    }

    @Test
    public void testGetJugador() {
        Jugador j1 = partida.getJugador("Bot1");
        Jugador j2 = partida.getJugador("Bot2");
        Jugador j3 = partida.getJugador("NoExisteix");

        assertNotNull(j1);
        assertEquals("Bot1", j1.getNom());

        assertNotNull(j2);
        assertEquals("Bot2", j2.getNom());

        assertNull(j3);
    }

    @Test
    public void testRepartirFitxesDespresDeFinalitzar() {
        partida.finalitzarPartida();
        partida.repartirFitxes("Bot1", 2);

        assertEquals(2, jugador1.getFitxes().size());
    }
}

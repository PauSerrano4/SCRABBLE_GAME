import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;
import model.Fitxa;
import model.Jugador;

/**
 * Classe de testeig per a la classe Jugador.java
 * @autor Pau Serrano Sanz (pau.serrano.sanz@estudiantat.upc.edu)
 */

public class TestJugador {

    private Jugador jugador;
    private Fitxa fitxaA;
    private Fitxa fitxaB;

    @Before
    public void setUp() {
        jugador = new Jugador("Pere");
        fitxaA = new Fitxa('A', 1);
        fitxaB = new Fitxa('B', 3);
        jugador.canviarFitxes(new ArrayList<>(List.of(fitxaA, fitxaB)));
    }

    @Test
    public void testNomJugador() {
        assertEquals("Pere", jugador.getNom());
    }

    @Test
    public void testPuntuacioInicial() {
        assertEquals(0, jugador.getPuntuacio());
    }

    @Test
    public void testActualitzarPuntuacio() {
        jugador.actualitzarPuntuacio(5);
        assertEquals(5, jugador.getPuntuacio());

        jugador.actualitzarPuntuacio(10);
        assertEquals(15, jugador.getPuntuacio());

    }

    @Test
    public void testAfegirFitxa() {
        Fitxa nova = new Fitxa('C', 2);
        jugador.afegirFitxa(nova);
        assertTrue(jugador.getFitxes().contains(nova));
        assertEquals(3, jugador.getFitxes().size());
    }

    @Test
    public void testAfegirFitxaDuplicada() {
        jugador.afegirFitxa(fitxaA); // fitxaA ja hi és
        assertEquals(3, jugador.getFitxes().size());
    }

    @Test
    public void testColocarFitxaCorrecta() {
        jugador.colocarFitxa(fitxaA);
        assertFalse(jugador.getFitxes().contains(fitxaA));
        assertEquals(1, jugador.getFitxes().size());
    }

    @Test
    public void testColocarFitxaInexistent() {
        Fitxa z = new Fitxa('Z', 10);
        jugador.colocarFitxa(z); // no hauria de fer res
        assertEquals(2, jugador.getFitxes().size());
        assertTrue(jugador.getFitxes().contains(fitxaA));
        assertTrue(jugador.getFitxes().contains(fitxaB));
    }

    @Test
    public void testReordenarFitxes() {
        List<Fitxa> abans = new ArrayList<>(jugador.getFitxes());
        jugador.reordenarFitxes();
        List<Fitxa> despres = jugador.getFitxes();

        // Mateix contingut, potser ordre diferent
        assertEquals(new HashSet<>(abans), new HashSet<>(despres));
    }

    @Test
    public void testReordenarFitxesAmbUnaSolaFitxa() {
        jugador.canviarFitxes(List.of(new Fitxa('D', 2)));
        jugador.reordenarFitxes();
        assertEquals(1, jugador.getFitxes().size());
        assertEquals('D', jugador.getFitxes().get(0).getLletra());
    }

    @Test
    public void testCanviarFitxes() {
        List<Fitxa> noves = List.of(new Fitxa('X', 8), new Fitxa('Y', 4));
        jugador.canviarFitxes(noves);
        assertEquals(noves, jugador.getFitxes());
    }

    @Test
    public void testCanviarFitxesBuides() {
        jugador.canviarFitxes(new ArrayList<>());
        assertTrue(jugador.getFitxes().isEmpty());
    }

    @Test
    public void testAfegirFitxaDespresDeCanviar() {
        jugador.canviarFitxes(new ArrayList<>());
        jugador.afegirFitxa(new Fitxa('M', 5));
        assertEquals(1, jugador.getFitxes().size());
    }
}

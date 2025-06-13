import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import model.Estadistiques;

import java.util.*;

/**
 * Classe de testeig de Estadistiques.java
 * @autor Ferran Blanchart Reyes (ferran.blanchart@estudiantat.upc.edu)
 */
public class TestEstadistiques {

    private Estadistiques est;

    @Before
    public void config() {
        est = new Estadistiques();
    }

    @Test
    public void testEstadistiques() {
        assertEquals((Integer) 0, est.getPartidesGuanyades());
        assertEquals((Integer) 0, est.getPartidesPerdudes());
        assertEquals(0f, est.getTotalPunts(),0.01f);
        assertEquals(0f, est.getMillorPuntuacio(),0.01f);
        assertEquals(0f, est.getPuntuacioMitjana(),0.01f);
        assertTrue(est.getHistorialPartides().isEmpty());
        assertTrue(est.getPartidesPausades().isEmpty());
    }

    @Test
    public void testActualitzarPartidaGuanyada() {
        est.actualitzarEstadistiques(false, "partida1", true, 100f);

        assertEquals((Integer) 1, est.getPartidesGuanyades());
        assertEquals((Integer) 0, est.getPartidesPerdudes());
        assertEquals(100f, est.getTotalPunts(),0.01f);
        assertEquals(100f, est.getMillorPuntuacio(),0.01f);
        assertEquals(100f, est.getPuntuacioMitjana(),0.01f);
        assertTrue(est.getHistorialPartides().contains("partida1"));
    }

    @Test
    public void testActualitzarPartidaPerduda() {
        est.actualitzarEstadistiques(false, "partida2", false, 80f);

        assertEquals((Integer) 0, est.getPartidesGuanyades());
        assertEquals((Integer) 1, est.getPartidesPerdudes());
        assertEquals(80f, est.getTotalPunts(),0.01f);
        assertEquals(80f, est.getMillorPuntuacio(),0.01f);
        assertEquals(80f, est.getPuntuacioMitjana(),0.01f);
        assertTrue(est.getHistorialPartides().contains("partida2"));
    }

    @Test
    public void testPartidaPausadaNoActualitzaEstadistiques() {
        est.actualitzarEstadistiques(true, "partida3", true, 999f);

        assertEquals((Integer) 0, est.getPartidesGuanyades());
        assertEquals((Integer) 0, est.getPartidesPerdudes());
        assertEquals(0f, est.getTotalPunts(),0.01f);
        assertEquals(0f, est.getMillorPuntuacio(),0.01f);
        assertEquals(0f, est.getPuntuacioMitjana(),0.01f);
        assertTrue(est.getPartidesPausades().contains("partida3"));
        assertFalse(est.getHistorialPartides().contains("partida3"));
    }

    @Test
    public void testMillorPuntuacioActualitzada() {
        est.actualitzarEstadistiques(false, "partida1", true, 50f);
        est.actualitzarEstadistiques(false, "partida2", true, 90f); 

        assertEquals((Integer) 2, est.getPartidesGuanyades());
        assertEquals(90f + 50f, est.getTotalPunts(),0.01f);
        assertEquals(90f, est.getMillorPuntuacio(),0.01f);
    }
}

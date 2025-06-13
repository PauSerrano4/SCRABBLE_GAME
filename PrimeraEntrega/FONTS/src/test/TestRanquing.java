import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import model.Ranquing;

/**
 * Classe de testeig de Ranquing.java
 * @autor Ferran Blanchart Reyes (ferran.blanchart@estudiantat.upc.edu)
 */

public class TestRanquing {

    private Ranquing ranquing;

    @Before
    public void setUp() {
        ranquing = new Ranquing();
    }

    @Test
    public void testAfegirUsuariNouAlRanquing() {
        ranquing.actualitzarRanquing("joan", "normal", 100f);
        Float punts = ranquing.getPuntsUsuari("joan", "normal");

        assertEquals(100f, punts, 0.01f);
    }

    @Test
    public void testAcumulacioDePunts() {
        ranquing.actualitzarRanquing("anna", "contrarellotge", 50f);
        ranquing.actualitzarRanquing("anna", "contrarellotge", 30f);

        Float punts = ranquing.getPuntsUsuari("anna", "contrarellotge");
        assertEquals(80f, punts, 0.01f);
    }

    @Test
    public void testEliminarUsuari() {
        ranquing.actualitzarRanquing("pepe", "dificil", 120f);
        ranquing.eliminarUsuari("pepe");

        Float punts = ranquing.getPuntsUsuari("pepe", "dificil");
        assertEquals(0f, punts, 0.01f); // S'espera 0f perquè s'ha eliminat
    }

    @Test
    public void testUsuariInexistent() {
        Float punts = ranquing.getPuntsUsuari("ines", "normal");
        assertEquals(0f, punts, 0.01f); // No hi és, hauria de retornar 0
    }
}

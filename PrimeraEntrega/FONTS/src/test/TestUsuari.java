import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import model.Usuari;
import model.Estadistiques;

/**
 * Classe de testeig de Usuari.java
 * @autor Ferran Blanchart Reyes (ferran.blanchart@estudiantat.upc.edu)
 */

public class TestUsuari {

    private Usuari usuari;

    @Before
    public void setUp() {
        usuari = new Usuari("joan", "1234");
    }

    @Test
    public void testConstructor() {
        assertEquals("joan", usuari.getNom());
        assertEquals("1234", usuari.getContrasenya());
        assertNotNull(usuari.getEstadistiques());
        assertFalse(usuari.esEliminat());
    }

    @Test
    public void testCanviarNom() {
        usuari.canviarNom("marta");
        assertEquals("marta", usuari.getNom());
    }

    @Test
    public void testCanviarContrasenya() {
        usuari.canviarContrasenya("12345");
        assertEquals("12345", usuari.getContrasenya());
    }

    @Test
    public void testEliminarUsuari() {
        usuari.eliminarUsuari();
        assertTrue(usuari.esEliminat());
        assertEquals("Usuari eliminat", usuari.getNom());
        assertNull(usuari.getEstadistiques());
        assertNull(usuari.getContrasenya());
    }

    @Test
    public void testNoPermetCanvisDespresDEliminar() {
        usuari.eliminarUsuari();
        usuari.canviarNom("nouNom");
        usuari.canviarContrasenya("novaContra");
        assertEquals("Usuari eliminat", usuari.getNom());
        assertNull(usuari.getContrasenya());
    }
}

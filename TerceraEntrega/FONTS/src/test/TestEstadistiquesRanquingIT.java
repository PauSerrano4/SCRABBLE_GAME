package test;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import model.*;
import util.*;

/**
 * Tests d'integració per estadístiques i rànquing d'usuaris.
 * Valida l'actualització correcta del rànquing després de les partides.
 * @author Alexander de Jong
 */
public class TestEstadistiquesRanquingIT extends TestBaseIT {

    /**
     * Test: actualització del rànquing després d'una partida completada.
     * Verifica que els punts del guanyador s'afegeixen correctament
     * al rànquing general i que les estadístiques es mantenen actualitzades.
     * @author Alexander de Jong
     */
    @Test
    public void testActualitzacioRanquing() throws Exception {
        // Desactivar temporalment el mode test per permetre actualització del rànquing
        ctrl.setModeTest(false);
        
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Fer una jugada simple
        forzarAtril(0, "CASA___");
        List<PosicioFitxa> jugada = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada);
        
        // Acabar partida
        ctrl.acabarPartida("jugador1");
        
        // Verificar que el rànquing s'ha actualitzat
        assertNotNull("Hauria d'haver rànquing", ctrl.getRanquing());
        assertTrue("Hauria d'haver punts al rànquing", 
                  ctrl.getRanquing().getPuntsUsuari("jugador1", "total") > 0);
        
        // Tornar a activar el mode test
        ctrl.setModeTest(true);
        
        // Eliminar usuaris creats en aquest test
        try {
            ctrl.eliminarUsuariComplet("jugador1");
        } catch (Exception e) {
            System.out.println("AVÍS: No s'ha pogut eliminar jugador1: " + e.getMessage());
        }
    }
}

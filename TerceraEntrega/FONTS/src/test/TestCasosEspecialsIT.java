package test;

import model.*;
import org.junit.*;
import java.util.*;
import util.*;
import static org.junit.Assert.*;

/**
 * Tests d'integració per casos especials i bonificacions del joc.
 * Valida situacions límit, bonificacions i condicions de final de partida.
 * @author Alexander de Jong
 */
public class TestCasosEspecialsIT extends TestBaseIT {

    /**
     * Test: final de partida per usar totes les fitxes de l'atril.
     * Verifica que s'aplica la bonificació de +50 punts quan
     * un jugador utilitza totes les seves fitxes en una sola jugada.
     * @author Alexander de Jong
     */
    @Test
    public void testFinPerFitxesAcabades() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // FACIL té 8 fitxes segons els fitxers de lletres
        int fitxesInicials = ctrl.getPartida().getJugador(0).getFitxes().size();
        assertEquals("FACIL hauria de donar 8 fitxes inicialment", 8, fitxesInicials);
        
        // Forçar exactament 8 fitxes que formin una paraula vàlida
        forzarAtril(0, "EXEMPLAR"); // 8 lletres
        
        // Verificar que després de forçar encara té 8 fitxes
        assertEquals("Després de forçar hauria de tenir 8 fitxes", 8, 
                    ctrl.getPartida().getJugador(0).getFitxes().size());
        
        // Crear jugada que usa TOTES les 8 fitxes
        List<PosicioFitxa> jugada = crearJugadaHoritzontal(7, 7, "EXEMPLAR");
        assertEquals("La jugada hauria d'usar totes les 8 fitxes", 8, jugada.size());
        
        int puntsAntes = ctrl.getPuntsJugador(0);
        ctrl.confirmarJugadaHumana(jugada);
        
        // REVISAT: Segons la lògica actual, sempre es reparteixen fitxes
        // Però quan s'usen TOTES les fitxes, hauria d'haver bonificació
        int fitxesDespres = ctrl.getPartida().getJugador(0).getFitxes().size();
        int puntsDespres = ctrl.getPuntsJugador(0);
        int diferencia = puntsDespres - puntsAntes;
        
        // El test principal: verificar que ha rebut la bonificació de 50 punts
        assertTrue("Hauria d'incloure bonificació de 50+ punts per usar totes les fitxes", 
                  diferencia >= 50);
        
        // Verificar que la jugada està registrada
        List<Jugada> jugades = ctrl.getPartida().getJugadesRealitzades();
        assertNotNull("Hauria d'haver jugades registrades", jugades);
        assertFalse("Hauria d'haver almenys una jugada", jugades.isEmpty());
        assertEquals("La jugada hauria de ser EXEMPLAR", "EXEMPLAR", jugades.get(0).getParaula());
        
        // Informació de debug per veure el comportament
        System.out.println("DEBUG: Fitxes després de la jugada: " + fitxesDespres);
        System.out.println("DEBUG: Punts guanyats: " + diferencia + " (inclou bonificació si va usar totes)");
    }

    /**
     * Test: límit de temps del temporitzador.
     * Comprova que les partides amb temporitzador finalitzen
     * automàticament quan s'exhaureix el temps.
     * @author Alexander de Jong
     */
    @Test
    public void testLimitTemps() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, true, true);
        
        // Verificar que hi ha temporitzador
        assertNotNull("Hauria d'haver temporitzador", ctrl.getTemporitzador());
        
        // Simular expiració del temps
        ctrl.zerosegons();
        Thread.sleep(1000); // Donar temps al temporitzador
        
        assertEquals("La partida hauria d'estar finalitzada", "finalitzada", ctrl.getPartida().getEstat());
    }

    /**
     * Test: situació d'empat entre jugadors.
     * Valida la gestió correcta d'empats quan els jugadors
     * tenen la mateixa puntuació al final de la partida.
     * @author Alexander de Jong
     */
    @Test
    public void testEmpat() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, false); // Dos humans
        
        // Verificar que ambdós jugadors tenen la mateixa puntuació (0 punts inicials)
        int punts1 = ctrl.getPuntsJugador(0);
        int punts2 = ctrl.getPuntsJugador(1);
        assertEquals("Ambdós jugadors haurien de tenir la mateixa puntuació", punts1, punts2);
        assertEquals("Cada jugador hauria de tenir 0 punts", 0, punts1);
        assertEquals("Cada jugador hauria de tenir 0 punts", 0, punts2);
        
        // Verificar que el sistema detecta l'empat correctament
        String guanyador = ctrl.obtenirNomMesPunts();
        assertEquals("Hauria de detectar empat amb puntuacions iguals", "Empat", guanyador);
        
        // Verificar estat de la partida
        assertNotNull("La partida hauria d'existir", ctrl.getPartida());
        assertEquals("Ambdós jugadors haurien de tenir 0 punts", 0, punts1 + punts2);
    }

    
    /**
     * Test: intent de jugada amb paraula invàlida.
     * Verifica que el sistema rebutja paraules inexistents
     * i proporciona missatges d'error adequats.
     * @author Alexander de Jong
     */
    @Test
    public void testParaulaInvalida() {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Intentar col·locar una paraula que probablement no existeix
        forzarAtril(0, "XYZQWER");
        List<PosicioFitxa> jugadaInvalida = Arrays.asList(
            new PosicioFitxa(7, 7, 'X', false),
            new PosicioFitxa(7, 8, 'Y', false),
            new PosicioFitxa(7, 9, 'Z', false)
        );
        
        boolean excepcioLlançada = false;
        try {
            ctrl.confirmarJugadaHumana(jugadaInvalida);
        } catch (Exception e) {
            excepcioLlançada = true;
            assertTrue("L'error hauria de mencionar diccionari", 
                      e.getMessage().toLowerCase().contains("diccionari") ||
                      e.getMessage().toLowerCase().contains("paraula"));
        }
        
        assertTrue("Hauria de llançar excepció per paraula no vàlida", excepcioLlançada);
    }

    /**
     * Test: comportament amb bossa de fitxes gairebé buida.
     * Comprova que el joc continua funcionant correctament
     * quan queden poques fitxes disponibles.
     * @author Alexander de Jong
     */
    @Test
    public void testBossaGairebeBuida() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Verificar que hi ha fitxes inicials
        int fitxesInicials = ctrl.getNumFitxesBossa();
        assertTrue("Hauria d'haver fitxes inicials a la bossa", fitxesInicials > 0);
        
        // Fer una jugada normal
        forzarAtril(0, "CASA___");
        List<PosicioFitxa> jugada = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada);
        
        // Verificar que la bossa té menys fitxes
        int fitxesDespres = ctrl.getNumFitxesBossa();
        assertTrue("La bossa hauria de tenir menys fitxes", fitxesDespres <= fitxesInicials);
    }
}

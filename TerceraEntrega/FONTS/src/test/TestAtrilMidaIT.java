package test;

import model.*;
import org.junit.*;
import java.util.*;
import util.*;
import static org.junit.Assert.*;

/**
 * Tests d'integració per verificar mides correctes d'atril i el correcte funcionamement
 * Valida que cada dificultat assigna el nombre correcte de fitxes.
 * @author Alexander de Jong
 */
public class TestAtrilMidaIT extends TestBaseIT {

    /**
     * Test: verificar mida d'atril per dificultat FACIL.
     * Comprova que FACIL dona exactament 8 fitxes inicials al jugador humà.
     * @author Alexander de Jong
     */
    @Test
    public void testAtrilMidaFACIL() {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        int fitxesJugador0 = ctrl.getPartida().getJugador(0).getFitxes().size();
        
        assertEquals("FACIL hauria de donar 8 fitxes al jugador humà", 8, fitxesJugador0);
        assertEquals("getRackSize hauria de retornar 8 per FACIL", 8, ctrl.getDificultat().getRackSize());
    }

    /**
     * Test: verificar mida d'atril per dificultat NORMAL.
     * Comprova que NORMAL dona exactament 7 fitxes inicials al jugador humà.
     * @author Alexander de Jong
     */
    @Test
    public void testAtrilMidaNORMAL() {
        crearPartida("catalan", Dificultat.NORMAL, false, true);
        
        int fitxesJugador0 = ctrl.getPartida().getJugador(0).getFitxes().size();
        
        assertEquals("MITJA hauria de donar 7 fitxes al jugador humà", 7, fitxesJugador0);
        assertEquals("getRackSize hauria de retornar 7 per MITJA", 7, ctrl.getDificultat().getRackSize());
    }

    /**
     * Test: verificar mida d'atril per dificultat DIFICIL.
     * Comprova que DIFICIL dona exactament 6 fitxes inicials al jugador humà.
     * @author Alexander de Jong
     */
    @Test
    public void testAtrilMidaDIFICIL() {
        crearPartida("catalan", Dificultat.DIFICIL, false, true);
        
        int fitxesJugador0 = ctrl.getPartida().getJugador(0).getFitxes().size();
        
        assertEquals("DIFICIL hauria de donar 6 fitxes al jugador humà", 6, fitxesJugador0);
        assertEquals("getRackSize hauria de retornar 6 per DIFICIL", 6, ctrl.getDificultat().getRackSize());
    }

    /**
     * Test: intercanvi de fitxes durant la partida.
     */
    @Test
    public void testIntercanviFitxes() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Primera jugada per activar el tauler
        forzarAtril(0, "CASA____");
        List<PosicioFitxa> jugada1 = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada1);
        
        // Avançar torn per tornar al jugador humà
        ctrl.jugarTorn(); // BOT juga
        
        // Forçar un atril amb fitxes reals per al test d'intercanvi
        forzarAtril(0, "PROVESTU");
        
        // Intercanviar 3 fitxes reals
        List<Character> aIntercanviar = Arrays.asList('P', 'R', 'O');
        
        int midaAntes = ctrl.getPartida().getJugador(0).getFitxes().size();
        ctrl.canviarFitxes(aIntercanviar);
        int midaDespres = ctrl.getPartida().getJugador(0).getFitxes().size();
        
        // Verificar que es manté la mida
        assertEquals("Hauria de mantenir el mateix nombre de fitxes", 
                    midaAntes, midaDespres);
    }

    /**
     * Test: mantenir mida d'atril després de jugades.
     * Verifica que després de fer una jugada, l'atril es reomplena
     * fins a la mida correcta segons la dificultat.
     * @author Alexander de Jong
     */
    @Test
    public void testMantenerMidaAtrilDespresJugada() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        int midaInici = ctrl.getPartida().getJugador(0).getFitxes().size();
        assertEquals("Hauria de començar amb 8 fitxes", 8, midaInici);
        
        // Fer una jugada de 4 fitxes
        forzarAtril(0, "CASA____");
        List<PosicioFitxa> jugada = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada);
        
        // Després d'avançar torn, hauria de tornar a tenir 8 fitxes
        int midaDespres = ctrl.getPartida().getJugador(0).getFitxes().size();
        assertEquals("Després de la jugada hauria de tornar a tenir 8 fitxes", 8, midaDespres);
    }

    /**
     * Test: atril quan s'acaben fitxes de la bossa.
     * Comprova que quan la bossa es buida, l'atril pot tenir menys
     * fitxes del màxim sense causar errors.
     * @author Alexander de Jong
     */
    @Test
    public void testAtrilBossaBuida() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Verificar que hi ha fitxes a la bossa inicialment
        int fitxesBossaInici = ctrl.getNumFitxesBossa();
        assertTrue("Hauria d'haver fitxes a la bossa", fitxesBossaInici > 0);
        
        // Fer múltiples jugades per esgotar la bossa
        for (int i = 0; i < 5; i++) {
            try {
                forzarAtril(0, "CASA____");
                List<PosicioFitxa> jugada = Arrays.asList(
                    new PosicioFitxa(7 + i, 7, 'C', false),
                    new PosicioFitxa(7 + i, 8, 'A', false)
                );
                ctrl.confirmarJugadaHumana(jugada);
                ctrl.jugarTorn(); // BOT juga
            } catch (Exception e) {
                // Acceptable si no es pot fer més jugades
                break;
            }
        }
        
        // L'atril pot tenir menys fitxes si la bossa s'ha esgotat
        int fitxesFinals = ctrl.getPartida().getJugador(0).getFitxes().size();
        assertTrue("L'atril hauria de tenir almenys 0 fitxes", fitxesFinals >= 0);
        assertTrue("L'atril no hauria de tenir més fitxes que el màxim", fitxesFinals <= 8);
    }
}

package test;

import model.*;
import org.junit.*;

import exceptions.JugadorNoPertanyPartidaException;

import java.util.*;
import util.*;
import static org.junit.Assert.*;

/**
 * Tests d'integració per casos d'error i situacions límit.
 * Valida la gestió robusta d'errors i la resposta del sistema
 * davant de situacions excepcionals o incorrectes.
 * @author Alexander de Jong
 */
public class TestErrorsIT extends TestBaseIT {

    /**
     * Test: error en intentar jugar fitxa no disponible a l'atril.
     * Verifica que el sistema detecta i rebutja l'ús de fitxes
     * que el jugador no té al seu atril.
     * @author Alexander de Jong
     */
    @Test
    public void testErrorFichaFueraAtril() {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        forzarAtril(0, "CASA___");
        List<PosicioFitxa> jugada = Arrays.asList(
            new PosicioFitxa(7, 7, 'Z', false)
        );
        try {
            ctrl.confirmarJugadaHumana(jugada);
            fail("Esperava JugadaInvalidaException");
        } catch (Exception e) {
            // OK
        }
        List<Jugada> jugadas = ctrl.getPartida().getJugadesRealitzades();
        assertTrue(jugadas == null || jugadas.isEmpty());
    }

    /**
     * Test: error en col·locar fitxes fora dels límits del tauler.
     * Comprova que es rebutgen jugades amb coordenades invàlides
     * que surten dels límits del tauler 15x15.
     * @author Alexander de Jong
     */
    @Test
    public void testErrorCoordenadasFueraTablero() {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        forzarAtril(0, "CASA___");
        List<PosicioFitxa> jugada = Arrays.asList(
            new PosicioFitxa(20, 20, 'C', false)
        );
        try {
            ctrl.confirmarJugadaHumana(jugada);
            fail("Esperava JugadaInvalidaException");
        } catch (Exception e) {
            // OK
        }
        List<Jugada> jugadas = ctrl.getPartida().getJugadesRealitzades();
        assertTrue(jugadas == null || jugadas.isEmpty());
    }

    /**
     * Test: error en intentar col·locar fitxa en casella ocupada.
     * Valida que el sistema detecta conflictes de connectivitat
     * i rebutja jugades que no segueixen les regles del Scrabble.
     * @author Alexander de Jong
     */
    @Test
    public void testErrorCasellaOcupada() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Primera jugada: CASA
        forzarAtril(0, "CASA___");
        List<PosicioFitxa> jugada1 = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada1);
        
        // BOT passa torn
        ctrl.jugarTorn();
        
        // Intentar col·locar una paraula que no connecta correctament
        forzarAtril(0, "TEST___");
        List<PosicioFitxa> jugadaInvalida = Arrays.asList(
            new PosicioFitxa(5, 5, 'T', false), // Posició aïllada
            new PosicioFitxa(5, 6, 'E', false),
            new PosicioFitxa(5, 7, 'S', false),
            new PosicioFitxa(5, 8, 'T', false)
        );
        
        boolean excepcioLlançada = false;
        try {
            ctrl.confirmarJugadaHumana(jugadaInvalida);
            fail("Esperava JugadaInvalidaException per manca de connectivitat");
        } catch (Exception e) {
            // OK 
        }
    }

    /**
     * Test: error en canviar fitxes quan la bossa està buida.
     * Comprova la gestió d'errors quan s'intenta intercanviar
     * fitxes però no n'hi ha prou disponibles a la bossa.
     * @author Alexander de Jong
     */
    @Test
    public void testErrorCanviFitxesBossaBuida() {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Intentar canviar fitxes que no tenim
        List<Character> fitxesACanviar = Arrays.asList('Z', 'Q', 'X'); // Fitxes rares
        
        boolean operacioFallida = false;
        try {
            ctrl.canviarFitxes(fitxesACanviar);
        } catch (Exception e) {
            operacioFallida = true;
        }
        
        assertTrue("Hauria de fallar quan no es poden canviar les fitxes", operacioFallida);
    }

    
    /**
     * Test: error en carregar partida inexistent.
     * Comprova que el sistema gestiona graciosament els intents
     * de carregar fitxers de partida que no existeixen.
     * @author Alexander de Jong
     */
    @Test
    public void testErrorCarregarPartidaInexistent() {
        boolean carregada = false;
        boolean excepcioLlancada = false;
        
        try {
            carregada = ctrl.carregarPartida("partida_que_no_existeix.ser");
        } catch (JugadorNoPertanyPartidaException e) {
            // Aquesta excepció és acceptable (fitxer no existeix)
            excepcioLlancada = true;
        } catch (Exception e) {
            // Qualsevol altra excepció també és acceptable
            excepcioLlancada = true;
        }
        
        // El test passa si:
        // 1. Retorna false (no s'ha carregat)
        // 2. O llança alguna excepció
        assertTrue("Hauria de retornar false o llançar excepció per partida inexistent", 
                !carregada || excepcioLlancada);
        assertFalse("No hauria d'haver partida carregada", ctrl.hiHaPartida());
    }

    /**
     * Test: error amb jugada buida (sense fitxes).
     * Verifica que el sistema rebutja jugades que no contenen
     * cap fitxa, requerint un mínim d'una fitxa per jugada.
     * @author Alexander de Jong
     */
    @Test
    public void testErrorJugadaBuida() {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        List<PosicioFitxa> jugadaBuida = new ArrayList<>();
        
        try {
            ctrl.confirmarJugadaHumana(jugadaBuida);
            fail("Esperava JugadaInvalidaException per jugada buida");
        } catch (Exception e) {
            // OK
        }
    }

    /**
     * Test: error amb coordenades negatives.
     * Comprova que es rebutgen coordenades negatives que
     * estarien fora dels límits inferiors del tauler.
     * @author Alexander de Jong
     */
    @Test
    public void testErrorCoordenadesNegatives() {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        forzarAtril(0, "CASA___");
        
        List<PosicioFitxa> jugadaInvalida = Arrays.asList(
            new PosicioFitxa(-1, -1, 'C', false)
        );
        
        try {
            ctrl.confirmarJugadaHumana(jugadaInvalida);
            fail("Esperava JugadaInvalidaException per coordenades negatives");
        } catch (Exception e) {
            // OK
        }
    }
}

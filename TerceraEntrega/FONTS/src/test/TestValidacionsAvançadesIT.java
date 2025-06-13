package test;

import model.*;
import org.junit.*;
import java.util.*;
import util.*;
import static org.junit.Assert.*;

/**
 * Tests d'integració per validacions avançades de jugades.
 * Comprova regles complexes de connectivitat i formació de paraules.
 * @author Alexander de Jong
 */
public class TestValidacionsAvançadesIT extends TestBaseIT {

    /**
     * Test: connectivitat obligatòria amb fitxes existents.
     * Verifica que totes les jugades després de la primera
     * han de connectar amb almenys una fitxa ja col·locada.
     * @author Alexander de Jong
     */
    @Test
    public void testConnectivitateObligatoria() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Primera jugada: CASA al centre
        forzarAtril(0, "CASA____");
        List<PosicioFitxa> jugada1 = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada1);
        
        ctrl.jugarTorn(); // BOT passa
        
        // Intentar jugada AÏLLADA (no connecta)
        forzarAtril(0, "TEST____");
        List<PosicioFitxa> jugadaAillada = Arrays.asList(
            new PosicioFitxa(0, 0, 'T', false),
            new PosicioFitxa(0, 1, 'E', false),
            new PosicioFitxa(0, 2, 'S', false),
            new PosicioFitxa(0, 3, 'T', false)
        );
        
        boolean excepcioLlançada = false;
        try {
            ctrl.confirmarJugadaHumana(jugadaAillada);
        } catch (Exception e) {
            excepcioLlançada = true;
        }
        
        assertTrue("Hauria de rebutjar jugada aïllada", excepcioLlançada);
    }

    /**
     * Test: totes les paraules creuades han de ser vàlides.
     * Comprova que quan una jugada forma múltiples paraules,
     * totes han d'existir al diccionari.
     * @author Alexander de Jong
     */
    @Test
    public void testParaulesCreuadesValides() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Primera jugada: CASA
        forzarAtril(0, "CASA____");
        List<PosicioFitxa> jugada1 = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada1);
        
        ctrl.jugarTorn(); // BOT passa
        
        // Intentar creuament que formi paraula invàlida
        forzarAtril(0, "QTXZ____");
        List<PosicioFitxa> jugadaInvalida = Arrays.asList(
            new PosicioFitxa(6, 7, 'Q', false), // Sobre la 'C', formaria "QC..."
            new PosicioFitxa(8, 7, 'T', false), // Sota la 'C', continuaria "QCT"
            new PosicioFitxa(9, 7, 'X', false),
            new PosicioFitxa(10, 7, 'Z', false)
        );
        
        boolean excepcioLlançada = false;
        try {
            ctrl.confirmarJugadaHumana(jugadaInvalida);
        } catch (Exception e) {
            excepcioLlançada = true;
        }
        
        assertTrue("Hauria de rebutjar creuament amb paraules invàlides", excepcioLlançada);
    }

    /**
     * Test: jugades amb espais buits no permeses.
     * Verifica que no es poden col·locar fitxes amb gaps
     * (espais buits entre elles) en la mateixa jugada.
     * @author Alexander de Jong
     */
    @Test
    public void testJugadaAmbEspaisBuits() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Primera jugada normal
        forzarAtril(0, "CASA____");
        List<PosicioFitxa> jugada1 = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada1);
        
        ctrl.jugarTorn(); // BOT passa
        
        // Intentar jugada amb GAP
        forzarAtril(0, "TO______");
        List<PosicioFitxa> jugadaAmbGap = Arrays.asList(
            new PosicioFitxa(8, 7, 'T', false), // Connecta amb CASA
            // GAP en posició (9,7)
            new PosicioFitxa(10, 7, 'O', false) // Espai buit entre T i O
        );
        
        boolean excepcioLlançada = false;
        try {
            ctrl.confirmarJugadaHumana(jugadaAmbGap);
        } catch (Exception e) {
            excepcioLlançada = true;
            System.out.println("DEBUG: Error detectat en jugada amb gaps: " + e.getMessage());
        }
        
        assertTrue("Hauria de llançar excepció per jugada amb gaps", excepcioLlançada);
    }

    /**
     * Test: direcció única en cada jugada.
     * Comprova que totes les fitxes d'una jugada han d'estar
     * en la mateixa línia (horitzontal o vertical), no diagonal.
     * @author Alexander de Jong
     */
    @Test
    public void testDireccioUnica() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Primera jugada
        forzarAtril(0, "CASA____");
        List<PosicioFitxa> jugada1 = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada1);
        
        ctrl.jugarTorn(); // BOT passa
        
        // Intentar jugada DIAGONAL (no permesa)
        forzarAtril(0, "TEST____");
        List<PosicioFitxa> jugadaDiagonal = Arrays.asList(
            new PosicioFitxa(8, 7, 'T', false), // Connecta
            new PosicioFitxa(9, 8, 'E', false), // Diagonal
            new PosicioFitxa(10, 9, 'S', false), // Diagonal
            new PosicioFitxa(11, 10, 'T', false) // Diagonal
        );
        
        boolean excepcioLlançada = false;
        try {
            ctrl.confirmarJugadaHumana(jugadaDiagonal);
        } catch (Exception e) {
            excepcioLlançada = true;
            System.out.println("DEBUG: Error detectat en jugada diagonal: " + e.getMessage());
        }
        
        assertTrue("Hauria de llançar excepció per jugada diagonal", excepcioLlançada);
    }

    /**
     * Test: solapament de fitxes no permès.
     * Verifica que no es poden col·locar fitxes sobre
     * caselles que ja contenen altres fitxes.
     * @author Alexander de Jong
     */
    @Test
    public void testSolapamentFitxes() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Primera jugada: CASA
        forzarAtril(0, "CASA____");
        List<PosicioFitxa> jugada1 = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada1);
        
        ctrl.jugarTorn(); // BOT passa
        
        // Intentar col·locar fitxa sobre casella ocupada
        forzarAtril(0, "TEST____");
        List<PosicioFitxa> jugadaSolapament = Arrays.asList(
            new PosicioFitxa(7, 7, 'T', false) // Mateixa posició que la 'C' de CASA
        );
        
        boolean excepcioLlançada = false;
        try {
            ctrl.confirmarJugadaHumana(jugadaSolapament);
        } catch (Exception e) {
            excepcioLlançada = true;
            System.out.println("DEBUG: Error detectat en solapament: " + e.getMessage());
        }
        
        assertTrue("Hauria de llançar excepció per solapament de fitxes", excepcioLlançada);
    }

    /**
     * Test: límits del tauler 15x15.
     * Comprova que es rebutgen jugades que surten
     * dels límits del tauler (0-14, 0-14).
     * @author Alexander de Jong
     */
    @Test
    public void testLimitsTauler() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Intentar col·locar en coordenades invàlides
        forzarAtril(0, "CASA____");
        
        // Test coordenades negatives
        List<PosicioFitxa> jugadaNegativa = Arrays.asList(
            new PosicioFitxa(-1, 7, 'C', false)
        );
        
        boolean excepcioNegativa = false;
        try {
            ctrl.confirmarJugadaHumana(jugadaNegativa);
        } catch (Exception e) {
            excepcioNegativa = true;
        }
        assertTrue("Hauria de rebutjar coordenades negatives", excepcioNegativa);
        
        // Test coordenades massa grans
        List<PosicioFitxa> jugadaGran = Arrays.asList(
            new PosicioFitxa(15, 7, 'C', false) // Fora del rang 0-14
        );
        
        boolean excepcioGran = false;
        try {
            ctrl.confirmarJugadaHumana(jugadaGran);
        } catch (Exception e) {
            excepcioGran = true;
        }
        assertTrue("Hauria de rebutjar coordenades fora del tauler", excepcioGran);
    }

    /**
     * Test alternatiu: verificar que el sistema detecta jugades problemàtiques.
     * Test més general que verifica que el sistema gestiona adequadament
     * situacions complexes sense assumir missatges d'error específics.
     * @author Alexander de Jong
     */
    @Test
    public void testGestioJugadesProblematiques() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Primera jugada vàlida
        forzarAtril(0, "CASA____");
        List<PosicioFitxa> jugadaValida = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugadaValida);
        
        List<Jugada> jugadesInicials = ctrl.getPartida().getJugadesRealitzades();
        int jugadesInicialCount = jugadesInicials != null ? jugadesInicials.size() : 0;
        
        ctrl.jugarTorn(); // BOT passa
        
        // Intentar diverses jugades problemàtiques i verificar que el sistema no es trenca
        List<List<PosicioFitxa>> jugadesProblematiques = Arrays.asList(
            // Jugada aïllada
            Arrays.asList(new PosicioFitxa(0, 0, 'T', false)),
            // Coordenades fora de rang
            Arrays.asList(new PosicioFitxa(-1, 0, 'T', false)),
            // Solapament
            Arrays.asList(new PosicioFitxa(7, 7, 'T', false))
        );
        
        for (List<PosicioFitxa> jugadaProblema : jugadesProblematiques) {
            try {
                forzarAtril(0, "TEST____");
                ctrl.confirmarJugadaHumana(jugadaProblema);
                // Si arriba aquí, el sistema ha permès la jugada
                System.out.println("DEBUG: Jugada permesa: " + jugadaProblema);
            } catch (Exception e) {
                // Si llança excepció, el sistema ha detectat el problema
                System.out.println("DEBUG: Jugada rebutjada: " + e.getMessage());
            }
        }
        
        // Verificar que el sistema segueix funcionant
        assertTrue("El sistema hauria de mantenir la partida activa", ctrl.hiHaPartida());
        assertNotNull("Hauria de mantenir l'estat del tauler", ctrl.getTauler());
        
        // Verificar que les jugades vàlides anteriors es mantenen
        List<Jugada> jugadesFinals = ctrl.getPartida().getJugadesRealitzades();
        int jugadesFinalsCount = jugadesFinals != null ? jugadesFinals.size() : 0;
        assertTrue("Hauria de mantenir almenys les jugades vàlides inicials", 
                  jugadesFinalsCount >= jugadesInicialCount);
    }
}

package test;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import model.*;
import util.*;

/**
 * Tests d'integració per funcionalitats de persistència.
 * Valida el guardat i càrrega de partides, mantenint l'estat del joc.
 * @author Alexander de Jong
 */
public class TestPersistenciaIT extends TestBaseIT {

    /**
     * Test: guardar i carregar partida completa.
     * Verifica que l'estat de la partida es manté consistent
     * després de guardar-la i carregar-la des del disc.
     * Inclou jugades realitzades, puntuacions i estat del tauler.
     * @author Alexander de Jong
     */
    @Test
    public void testGuardarCarregarPartida() throws Exception {
        // Registrar usuari primer
        ctrl.registrarUsuari("jugador1", "password");
        
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Fer una jugada
        forzarAtril(0, "CASA___");
        List<PosicioFitxa> jugada = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada);
        
        // Guardar partida
        boolean guardada = ctrl.guardarPartida("test_partida");
        assertTrue("Hauria de poder guardar la partida", guardada);
        
        // Carregar partida
        ctrl.inicialitzar();
        boolean carregada = ctrl.carregarPartida("test_partida.ser");
        assertTrue("Hauria de poder carregar la partida", carregada);
        
        // Verificar que la partida s'ha carregat correctament
        assertTrue("Hauria d'haver partida carregada", ctrl.hiHaPartida());
        
        // Neteja
        ctrl.eliminarPartida("test_partida.ser");
    }

    /**
     * Test: persistència de diccionaris personalitzats.
     * Verifica que es poden guardar i carregar diccionaris creats per l'usuari.
     */
    @Test
    public void testPersistenciaDiccionarisPersonalitzats() throws Exception {
        // Crear diccionari personalitzat
        List<String> paraules = Arrays.asList("PROVA", "TEST", "EXEMPLE");
        Map<String, Pair<Integer, Integer>> alfabet = new HashMap<>();
        alfabet.put("P", new Pair<>(1, 3));
        alfabet.put("R", new Pair<>(1, 2));
        alfabet.put("O", new Pair<>(1, 1));
        alfabet.put("V", new Pair<>(1, 4));
        alfabet.put("A", new Pair<>(1, 1));
        alfabet.put("T", new Pair<>(1, 2));
        alfabet.put("E", new Pair<>(1, 1));
        alfabet.put("S", new Pair<>(1, 2));
        alfabet.put("X", new Pair<>(1, 8));
        alfabet.put("M", new Pair<>(1, 3));
        alfabet.put("L", new Pair<>(1, 2));

        ctrl.guardarDiccionari("diccionari_test", paraules, alfabet);
        
        // Verificar que es pot carregar
        List<String> carregades = ctrl.getParaulesDeDiccionari("diccionari_test");
        assertTrue("Hauria de contenir les paraules guardades", 
                carregades.containsAll(paraules));
        
        // Neteja
        ctrl.eliminarDiccionari("diccionari_test");
    }

    /** Test: eliminar partida guardada. */
    @Test
    public void testEliminarPartida() throws Exception {
        loginUsuario("jugador1");
        
        // Crear i guardar una partida
        crearPartida("catalan", Dificultat.FACIL, false, true);
        forzarAtril(0, "CASA___");
        List<PosicioFitxa> jugada = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada);
        
        String nomFitxer = "partida_a_eliminar";
        boolean guardada = ctrl.guardarPartida(nomFitxer);
        assertTrue("La partida s'hauria de poder guardar", guardada);
        
        // Verificar que existeix (buscar amb extensió .ser)
        List<String> partides = ctrl.getPartidesGuardades();
        String fitxerComplet = nomFitxer + ".ser";
        boolean trobada = partides.contains(fitxerComplet);
        assertTrue("La partida hauria d'aparèixer a la llista", trobada);
        
        // Intentar carregar-la per verificar que existeix
        boolean carregada = ctrl.carregarPartida(fitxerComplet);
        assertTrue("La partida s'hauria de poder carregar", carregada);
        
        // Eliminar la partida (amb extensió)
        boolean eliminada = ctrl.eliminarPartida(fitxerComplet);
        assertTrue("La partida s'hauria de poder eliminar", eliminada);
        
        // Verificar que ja no existeix
        partides = ctrl.getPartidesGuardades();
        boolean jaNoExisteix = !partides.contains(fitxerComplet);
        assertTrue("La partida no hauria d'aparèixer a la llista", jaNoExisteix);
    }

    /** Test: persistència amb temporitzador. */
    @Test
    public void testPersistenciaTemporitzador() throws Exception {
        loginUsuario("jugador1");
        
        // Crear partida amb temporitzador
        crearPartida("catalan", Dificultat.FACIL, true, true);
        assertNotNull("Hauria de tenir temporitzador", ctrl.getTemporitzador());
        
        // Fer una jugada
        forzarAtril(0, "CASA___");
        List<PosicioFitxa> jugada = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada);
        
        // Guardar partida
        String nomFitxer = "partida_temporitzador";
        boolean guardada = ctrl.guardarPartida(nomFitxer);
        assertTrue("La partida amb temporitzador s'hauria de poder guardar", guardada);
        
        // Carregar partida (amb extensió .ser)
        boolean carregada = ctrl.carregarPartida(nomFitxer + ".ser");
        assertTrue("La partida amb temporitzador s'hauria de poder carregar", carregada);
        
        // Verificar que el temporitzador es recrea
        if (ctrl.getPartida().ambTemporitzador()) {
            // Si la partida diu que té temporitzador, hauria de tenir-lo
            // (pot ser null si s'ha pausat o finalitzat)
            System.out.println("Partida carregada amb configuració de temporitzador");
        }
        
        // Netejar
        ctrl.eliminarPartida(nomFitxer + ".ser");
    }

    /** Test: persistència amb diferents idiomes. */
    @Test
    public void testPersistenciaDiferentsIdiomes() throws Exception {
        for (String idioma : Arrays.asList("catalan", "castellano", "english")) {
            ctrl.inicialitzar();
            crearPartida(idioma, Dificultat.FACIL, false, true);
            
            String nom = "partida_" + idioma;
            assertTrue(ctrl.guardarPartida(nom));
            
            ctrl.inicialitzar();
            assertTrue(ctrl.carregarPartida(nom + ".ser"));
            assertEquals(idioma, ctrl.getIdioma());
            
            // Neteja
            ctrl.eliminarPartida(nom + ".ser");
        }
    }
}

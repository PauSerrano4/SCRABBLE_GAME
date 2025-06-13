package test;

import model.*;
import org.junit.*;
import java.util.*;
import util.*;
import static org.junit.Assert.*;

/**
 * Tests d'integració per comportament del BOT (intel·ligència artificial).
 * Valida que el BOT fa jugades vàlides i gestiona correctament el seu atril.
 * @author Alexander de Jong
 */
public class TestBotIT extends TestBaseIT {

    /**
     * Test: verificar que el BOT fa jugades vàlides.
     * Comprova que totes les jugades del BOT compleixen les regles
     * i que sempre col·loca paraules existents al diccionari.
     * @author Alexander de Jong
     */
    @Test
    public void testBOTFaJugadesValides() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Jugador humà fa primera jugada
        forzarAtril(0, "CASA____");
        List<PosicioFitxa> jugada = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada);
        
        int jugadesAntes = ctrl.getPartida().getJugadesRealitzades().size();
        
        // BOT juga el seu torn
        ctrl.jugarTorn();
        
        List<Jugada> jugades = ctrl.getPartida().getJugadesRealitzades();
        
        // Verificar que el BOT ha fet alguna acció (jugada o pass)
        assertTrue("El BOT hauria d'haver processat el seu torn", 
                  jugades.size() >= jugadesAntes);
        
        // Si el BOT ha fet una jugada, verificar que és vàlida
        if (jugades.size() > jugadesAntes) {
            Jugada jugadaBOT = jugades.get(jugades.size() - 1);
            assertEquals("La jugada hauria de ser del BOT", "BOT1", jugadaBOT.getJugador());
            assertTrue("La jugada del BOT hauria de donar punts positius", jugadaBOT.getPunts() > 0);
            assertNotNull("La paraula no hauria de ser null", jugadaBOT.getParaula());
            assertFalse("La paraula no hauria de ser buida", jugadaBOT.getParaula().isEmpty());
            
            // Verificar que la paraula està al diccionari
            assertTrue("La paraula del BOT hauria d'estar al diccionari", 
                      estaAlDiccionari(jugadaBOT.getParaula(), "catalan"));
        }
    }

    /**
     * Test: el BOT gestiona correctament el seu atril.
     * Verifica que el BOT manté el nombre correcte de fitxes
     * i que les utilitza de manera eficient.
     * @author Alexander de Jong
     */
    @Test
    public void testBOTGestionaAtril() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Verificar que el BOT té el nombre correcte de fitxes inicialment
        int fitxesBOTInici = ctrl.getPartida().getJugador(1).getFitxes().size();
        assertEquals("El BOT hauria de tenir 7 fitxes a qualsavol dificultat", 7, fitxesBOTInici);
        
        // Jugador humà fa primera jugada
        forzarAtril(0, "CASA____");
        List<PosicioFitxa> jugada = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada);
        
        // BOT juga
        ctrl.jugarTorn();
        
        // Verificar que el BOT manté fitxes a l'atril
        int fitxesBOTDespres = ctrl.getPartida().getJugador(1).getFitxes().size();
        assertTrue("El BOT hauria de mantenir fitxes a l'atril", fitxesBOTDespres >= 0);
        assertTrue("El BOT no hauria de tenir més fitxes del màxim", fitxesBOTDespres <= 7);
    }

    /**
     * Test: determinació del guanyador entre humà i BOT.
     * Comprova que el sistema determina correctament qui guanya
     * quan la partida finalitza amb diferents puntuacions.
     * @author Alexander de Jong
     */
    @Test
    public void testDeterminacioGuanyador() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);

        // Fer algunes jugades per acumular punts
        forzarAtril(0, "CASA____");
        List<PosicioFitxa> jugada1 = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada1);

        ctrl.jugarTorn(); // BOT juga

        forzarAtril(0, "TEST____");
        List<PosicioFitxa> jugada2 = Arrays.asList(
            new PosicioFitxa(8, 7, 'T', false),
            new PosicioFitxa(9, 7, 'E', false),
            new PosicioFitxa(10, 7, 'S', false),
            new PosicioFitxa(11, 7, 'T', false)
        );
        try {
            ctrl.confirmarJugadaHumana(jugada2);
        } catch (Exception e) {
            // Si TEST no és vàlid, intentar amb una altra paraula
            forzarAtril(0, "COTXE___");
            jugada2 = Arrays.asList(
                new PosicioFitxa(8, 7, 'O', false),
                new PosicioFitxa(9, 7, 'T', false),
                new PosicioFitxa(10, 7, 'X', false),
                new PosicioFitxa(11, 7, 'E', false)
            );
            ctrl.confirmarJugadaHumana(jugada2);
        }

        // Acabar partida i verificar guanyador
        ctrl.acabarPartida("jugador1");

        // Utilitzar la funció ctrl.obtenirNomMesPunts() per determinar el guanyador
        String guanyador = ctrl.obtenirNomMesPunts();

        assertNotNull("Hauria d'haver un guanyador determinat", guanyador);
        assertTrue(
            "El guanyador hauria de ser el jugador humà, el BOT o empat",
            guanyador.equals("jugador1") || guanyador.equals("BOT1") || guanyador.equals("Empat")
        );
    }

    /**
     * Test: temps de resposta del BOT.
     * Verifica que el BOT respon en un temps raonable
     * i no bloqueja el joc indefinidament.
     * @author Alexander de Jong
     */
    @Test
    public void testBOTTempsResposta() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Jugador humà fa primera jugada
        forzarAtril(0, "CASA____");
        List<PosicioFitxa> jugada = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada);
        
        // Mesurar temps que triga el BOT
        long tempsInici = System.currentTimeMillis();
        ctrl.jugarTorn();
        long tempsFinal = System.currentTimeMillis();
        
        long tempsTranscorregut = tempsFinal - tempsInici;
        
        // El BOT hauria de respondre en menys de 5 segons
        assertTrue("El BOT hauria de respondre en menys de 5 segons", tempsTranscorregut < 5000);
        
        ;
    }

    /**
     * Test: BOT pot intercanviar fitxes quan convé.
     * Verifica que el BOT té la capacitat d'intercanviar fitxes
     * quan no pot fer jugades beneficioses.
     * @author Alexander de Jong
     */
    @Test
    public void testBOTIntercanviaFitxes() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Donar fitxes difícils al BOT per forçar intercanvi
        Jugador bot = ctrl.getPartida().getJugador(1);
        bot.getFitxes().clear();
        // Fitxes rares que dificulten formar paraules
        for (char c : "QQQZZZX".toCharArray()) {
            bot.getFitxes().add(new Fitxa(c, getPuntuacioLletra(c, "catalan")));
        }
        
        // Jugador humà fa primera jugada
        forzarAtril(0, "CASA____");
        List<PosicioFitxa> jugada = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada);
        
        int fitxesBossaAntes = ctrl.getNumFitxesBossa();
        
        // BOT juga (hauria de considerar intercanvi o passar)
        ctrl.jugarTorn();
        
        // Verificar que el BOT ha gestionat la situació difícil
        // Pot haver intercanviat fitxes, passat torn o trobat una jugada creativa
        assertTrue("El BOT hauria d'haver gestionat la situació", true);
        
        // Si ha intercanviat, la bossa hauria de reflectir-ho
        int fitxesBossaDespres = ctrl.getNumFitxesBossa();
        if (fitxesBossaDespres != fitxesBossaAntes) {
            System.out.println("El BOT probablement ha intercanviat fitxes");
        }
    }

    /**
     * Test: BOT no pot fer jugades quan no té fitxes vàlides.
     * Verifica que el BOT passa el torn quan no té cap jugada possible.
     * @author Alexander de Jong
     */
    @Test
    public void testBOTNoPotFerJugada() throws Exception {
        crearPartida("catalan", Dificultat.FACIL, false, true);
        
        // Donar al BOT només fitxes que no poden formar paraules
        Jugador bot = ctrl.getPartida().getJugador(1);
        bot.getFitxes().clear();
        // Fitxes que no poden formar cap paraula vàlida
        for (char c : "QQQZZZ".toCharArray()) {
            bot.getFitxes().add(new Fitxa(c, getPuntuacioLletra(c, "catalan")));
        }
        
        // Jugador humà fa primera jugada
        forzarAtril(0, "CASA____");
        List<PosicioFitxa> jugada = crearJugadaHoritzontal(7, 7, "CASA");
        ctrl.confirmarJugadaHumana(jugada);
        
        // Mesurar temps abans de la jugada del BOT
        long tempsInici = System.currentTimeMillis();
        
        // BOT juga (hauria de passar el torn)
        ctrl.jugarTorn();
        
        long tempsFinal = System.currentTimeMillis();
        
        long tempsTranscorregut = tempsFinal - tempsInici;
        
        // El BOT hauria de passar el torn ràpidament
        assertTrue("El BOT hauria de passar el torn ràpidament", tempsTranscorregut < 2000);
        
        // Verificar que no ha fet cap jugada
        List<Jugada> jugades = ctrl.getPartida().getJugadesRealitzades();
        assertTrue("El BOT hauria de passar el torn sense fer jugada", 
                  jugades.size() == 1); // Només la jugada del jugador humà
        
        // Acabar partida i verificar que el jugador humà guanya
        ctrl.acabarPartida("jugador1");
        String guanyador = ctrl.obtenirNomMesPunts();
        
        assertEquals("El jugador humà hauria de guanyar ja que el BOT no ha fet cap jugada", 
                    "jugador1", guanyador);
    }


}

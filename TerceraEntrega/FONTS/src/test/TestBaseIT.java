package test;

import controller.ControladorDomini;
import model.*;
import util.*;
import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

/**
 * Classe base per a tests d'integració'Scrabble.
 * Conté helpers comuns i configuració compartida.
 * @author Alexander de Jong
 */
@Ignore("Classe base - no executar amb a test")
public abstract class TestBaseIT {

    protected ControladorDomini ctrl;

    // Diccionaris i alfabets carregats només una vegada per idioma
    protected static final Map<String, List<String>> diccionaris = new HashMap<>();
    protected static final Map<String, Map<String, util.Pair<Integer, Integer>>> alfabets = new HashMap<>();

    /**
     * Carrega diccionaris i alfabets una sola vegada per tots els tests.
     * Optimitza el temps d'execució evitant càrreges repetides.
     * @author Alexander de Jong
     */
    @BeforeClass
    public static void carregarDiccionarisUnCop() {
        // Carrega només una vegada per idioma
        String[] idiomes = {"catalan", "castellano", "english"};
        for (String idioma : idiomes) {
            try {
                persistencia.ControladorPersistencia cp = persistencia.ControladorPersistencia.getInstance();
                diccionaris.put(idioma, cp.importarParaulesDiccionari(idioma));
                alfabets.put(idioma, cp.importarAlfabet(idioma));
            } catch (Exception e) {
                throw new RuntimeException("Error carregant diccionari/alfabet per " + idioma, e);
            }
        }
    }

    /**
     * Configura l'entorn abans de cada test individual.
     * Inicialitza el controlador de domini i activa mode test.
     * @author Alexander de Jong
     */
    @Before
    public void setUp() {
        ctrl = ControladorDomini.getInstance();
        ctrl.inicialitzar();
        ctrl.setModeTest(true); // ACTIVAR MODE TEST
        
        // NO registrar usuari automàticament aquí
        // Cada test ha de gestionar els seus usuaris
    }

    /**
     * Neteja després de cada test.
     * Desactiva mode test per no afectar l'aplicació real.
     * @author Alexander de Jong
     */
    @After
    public void tearDown() {
        if (ctrl != null) {
            ctrl.setModeTest(false); // DESACTIVAR MODE TEST
        }
    }

    /**
     * Helper: força el atril del jugador indicat (0 o 1) amb les lletres donades (en ordre).
     * Utilitza valors reals de l'alfabet per assignar puntuacions correctes.
     * @param jugador Índex del jugador (0 o 1)
     * @param letras String amb les lletres a forçar
     * @author Alexander de Jong
     */
    protected void forzarAtril(int jugador, String letras) {
        // Accés directe al model per a proves
        Partida p = ctrl.getPartida();
        Jugador j = p.getJugadors().get(jugador);
        j.getFitxes().clear();

        // Obtener el idioma de la partida y el mapa de valores reales
        String idioma = ctrl.getIdioma();
        Map<String, util.Pair<Integer, Integer>> alfabet = alfabets.get(idioma);

        // Usar el tamaño del atril de la partida (no del jugador)
        int rackSize = ctrl.getDificultat().getRackSize();
        int count = 0;
        for (char c : letras.toCharArray()) {
            int valor = 1;
            if (c == '#') valor = 0;
            else if (alfabet != null) {
                util.Pair<Integer, Integer> info = alfabet.get(String.valueOf(c));
                if (info != null) valor = info.second;
            }
            j.getFitxes().add(new Fitxa(c, valor));
            count++;
        }
        // Rellenar amb lletres aleatòries si falten
        if (alfabet != null && count < rackSize) {
            for (String lletra : alfabet.keySet()) {
                if (lletra.equals("#")) continue;
                while (count < rackSize) {
                    util.Pair<Integer, Integer> info = alfabet.get(lletra);
                    if (info != null) {
                        j.getFitxes().add(new Fitxa(lletra.charAt(0), info.second));
                        count++;
                    }
                    break;
                }
                if (count >= rackSize) break;
            }
        }
        System.out.println("FORÇAT atril jugador " + jugador + ": " + letras + 
                          " (total: " + j.getFitxes().size() + " fitxes)");
    }

    /**
     * Helper: login o registre d'usuari per proves.
     * Gestiona automàticament registre si l'usuari no existeix.
     * @param nombre Nom de l'usuari
     * @author Alexander de Jong
     */
    protected void loginUsuario(String nombre) {
        loginUsuario(nombre, false);
    }

    /**
     * Helper: login o registre d'usuari per proves, con opción de segon usuario.
     * Permet gestionar el segon usuari per partides humà vs humà.
     * @param nombre Nom de l'usuari
     * @param segonUsuari Si és el segon usuari de la partida
     * @author Alexander de Jong
     */
    protected void loginUsuario(String nombre, boolean segonUsuari) {
        if (!segonUsuari) {
            if (!ctrl.registrarUsuari(nombre, "1234")) {
                try {
                    ctrl.login(nombre, "1234");
                } catch (Exception e) {
                    fail("No es pot fer login ni registrar: " + e.getMessage());
                }
            }
        } else {
            if (!ctrl.registraSegonUsuari(nombre, "1234")) {
                try {
                    ctrl.loginSegonUsuari(nombre, "1234");
                } catch (Exception e) {
                    fail("No es pot fer login ni registrar (segon usuari): " + e.getMessage());
                }
            }
        }
    }

    /**
     * Helper: crea partida via controlador amb paràmetres específics.
     * Gestiona automàticament el login dels usuaris necessaris.
     * @param idioma Idioma del diccionari
     * @param dif Dificultat de la partida
     * @param temporitzador Si la partida té temporitzador
     * @param vsMaquina Si juega contra la màquina
     * @author Alexander de Jong
     */
    protected void crearPartida(String idioma, Dificultat dif, boolean temporitzador, boolean vsMaquina) {
        loginUsuario("jugador1");
        if (!vsMaquina) {
            loginUsuario("jugador2", true);
        }
        // Només definir la partida una vegada, amb el mode correcte
        ModeJoc mode = temporitzador ? ModeJoc.CONTRARRELLOTGE : ModeJoc.CLASSIC;
        ctrl.definirPartida(
            mode,
            vsMaquina ? Contrincant.MAQUINA : Contrincant.JUGADOR,
            dif,
            idioma,
            vsMaquina ? null : "jugador2"
        );
    }

    /**
     * Helper: calcula els punts exactes d'una paraula sense multiplicadors de tauler.
     * Utilitza l'alfabet real de l'idioma especificat.
     * @param paraula Paraula a calcular
     * @param idioma Idioma per obtenir els valors de lletres
     * @return Puntuació sense multiplicadors
     * @author Alexander de Jong
     */
    protected int calcularPuntsParaulaSense(String paraula, String idioma) {
        Map<String, util.Pair<Integer, Integer>> alfabet = alfabets.get(idioma);
        if (alfabet == null) return 0;
        
        int punts = 0;
        for (char c : paraula.toCharArray()) {
            String lletra = String.valueOf(c);
            if (lletra.equals("#")) {
                punts += 0; // Comodí = 0 punts
            } else {
                util.Pair<Integer, Integer> info = alfabet.get(lletra);
                if (info != null) {
                    punts += info.second; // info.second és la puntuació
                } else {
                    punts += 1; // Valor per defecte si no es troba
                }
            }
        }
        return punts;
    }

    /**
     * Helper: calcula punts amb multiplicadors del tauler per primera jugada al centre.
     * Aplica el multiplicador de doble paraula del centre (7,7).
     * @param paraula Paraula de la primera jugada
     * @param idioma Idioma per calcular punts base
     * @return Puntuació amb multiplicador del centre
     * @author Alexander de Jong
     */
    protected int calcularPuntsPrimeraJugada(String paraula, String idioma) {
        int puntsSense = calcularPuntsParaulaSense(paraula, idioma);
        return puntsSense * 2; // Centre té multiplicador DP (doble paraula)
    }

    /**
     * Helper: obté la puntuació d'una lletra específica segons l'idioma.
     * @param lletra Lletra a consultar
     * @param idioma Idioma de l'alfabet
     * @return Puntuació de la lletra
     * @author Alexander de Jong
     */
    protected int getPuntuacioLletra(char lletra, String idioma) {
        Map<String, util.Pair<Integer, Integer>> alfabet = alfabets.get(idioma);
        if (alfabet == null) return 1;
        
        String str = String.valueOf(lletra);
        util.Pair<Integer, Integer> info = alfabet.get(str);
        return (info != null) ? info.second : 1;
    }

    /**
     * Helper: verifica si una paraula està al diccionari de l'idioma especificat.
     * @param paraula Paraula a verificar
     * @param idioma Idioma del diccionari
     * @return True si la paraula existeix
     * @author Alexander de Jong
     */
    protected boolean estaAlDiccionari(String paraula, String idioma) {
        List<String> dict = diccionaris.get(idioma);
        return dict != null && dict.contains(paraula.toUpperCase());
    }

    /**
     * Helper: crea una jugada simple horitzontal des d'una posició.
     * @param fila Fila inicial
     * @param colInici Columna inicial
     * @param paraula Paraula a col·locar
     * @return Llista de posicions de fitxes
     * @author Alexander de Jong
     */
    protected List<PosicioFitxa> crearJugadaHoritzontal(int fila, int colInici, String paraula) {
        List<PosicioFitxa> jugada = new ArrayList<>();
        for (int i = 0; i < paraula.length(); i++) {
            jugada.add(new PosicioFitxa(fila, colInici + i, paraula.charAt(i), false));
        }
        return jugada;
    }

    /**
     * Helper: crea una jugada simple vertical des d'una posició.
     * @param filaInici Fila inicial
     * @param col Columna
     * @param paraula Paraula a col·locar
     * @return Llista de posicions de fitxes
     * @author Alexander de Jong
     */
    protected List<PosicioFitxa> crearJugadaVertical(int filaInici, int col, String paraula) {
        List<PosicioFitxa> jugada = new ArrayList<>();
        for (int i = 0; i < paraula.length(); i++) {
            jugada.add(new PosicioFitxa(filaInici + i, col, paraula.charAt(i), false));
        }
        return jugada;
    }

    /**
     * Helper: obté mapa de puntuacions per idioma per facilitar tests.
     * @param idioma Idioma de l'alfabet
     * @return Mapa de caràcter a puntuació
     * @author Alexander de Jong
     */
    protected Map<Character, Integer> getPuntuacionsIdioma(String idioma) {
        Map<Character, Integer> puntuacions = new HashMap<>();
        Map<String, util.Pair<Integer, Integer>> alfabet = alfabets.get(idioma);
        
        if (alfabet != null) {
            for (Map.Entry<String, util.Pair<Integer, Integer>> entry : alfabet.entrySet()) {
                String lletra = entry.getKey();
                int puntuacio = entry.getValue().second;
                
                if (lletra.length() == 1) {
                    puntuacions.put(lletra.charAt(0), puntuacio);
                }
                // Per dígrafs com L·L, NY, etc. es podria afegir lògica especial
            }
        }
        
        return puntuacions;
    }

    /**
     * DOCUMENTACIÓ DELS MÈTODES DE CÀLCUL DE PUNTUACIONS:
     * 
     * 1. calcularPuntsParaulaSense(paraula, idioma):
     *    - Calcula punts base sense multiplicadors del tauler
     *    - Usa l'alfabet real carregat per l'idioma
     *    - Comodins (#) valen 0 punts
     *    - Exemple: "CASA" en català = C(2) + A(1) + S(1) + A(1) = 5 punts
     * 
     * 2. calcularPuntsPrimeraJugada(paraula, idioma):
     *    - Aplica multiplicador de doble paraula del centre (7,7)
     *    - Punts = calcularPuntsParaulaSense(paraula, idioma) * 2
     *    - Exemple: "CASA" = 5 * 2 = 10 punts
     * 
     * 3. getPuntuacioLletra(lletra, idioma):
     *    - Retorna puntuació individual d'una lletra
     *    - Consulta directament l'alfabet carregat
     * 
     * 4. Bonificacions especials:
     *    - Usar totes les fitxes de l'atril: +50 punts
     *    - Multiplicadors del tauler: DL, TL, DP, TP
     * 
     * @author Alexander de Jong
     */
}

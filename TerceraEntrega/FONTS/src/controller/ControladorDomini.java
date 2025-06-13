package controller;

import java.io.*;
import java.util.*;
import exceptions.*;
import model.*;
import util.*;
import persistencia.ControladorPersistencia;


/**
 * Classe ControladorDomini - Gestiona la lògica principal del joc d'Scrabble.
 * S'encarrega de la gestió de partides, usuaris, rànquings, diccionaris i la interacció amb la capa de persistència.
 */
public class ControladorDomini {

        // ---------- ATRIBUTS ----------
    /** Partida actual en curs */
    private Partida partida;
    /** Diccionari utilitzat per validar paraules */
    private Diccionari diccionari;
    /** Controlador de la intel·ligència artificial (BOT) */
    private ControladorMaquina maquina;
    /** Usuari principal que ha iniciat sessió */
    private Usuari usuariActiu;
    /** Segon usuari en partides humà vs humà */
    private Usuari usuari2;
    /** Sistema de rànquing global del joc */
    private Ranquing ranquing;
    /** Controlador de persistència per guardar/carregar dades */
    private ControladorPersistencia cp = new ControladorPersistencia();
    /** Dificultat de la partida actual */
    private Dificultat dificultat;
    /** Comptador de torns passats consecutius */
    private int passarTorn;
    /** Instància singleton del controlador de domini */
    private static ControladorDomini instance;
    /** Controlador específic per gestió d'usuaris */
    private CtrlUsuari ctrlUsuari;
    /** Indica si s'està executant en mode test */
    private boolean modeTest = false;

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe ControladorDomini.
     * Inicialitza el rànquing i l'estat intern.
     */
    public ControladorDomini() {
        this.ranquing = carregaRanquing();
        ctrlUsuari = new CtrlUsuari(cp);
        inicialitzar();
    }

    // ---------- GETTERS ----------
    /**
     * Retorna la instància singleton del controlador de domini.
     * @return Instància de ControladorDomini.
     */
    public static ControladorDomini getInstance() {
        if (instance == null) instance = new ControladorDomini();
        return instance;
    }

    /**
     * Retorna el rànquing actual.
     * @return Ranquing.
     */
    public Ranquing getRanquing() {
        return ranquing;
    }

    /**
     * Retorna la partida actual.
     * @return Partida.
     */
    public Partida getPartida() {
        return partida;
    }

    /**
     * Retorna el tauler de la partida.
     * @return Tauler.
     */
    public Tauler getTauler() {
        return partida.getTauler();
    }

    /**
     * Retorna el controlador d'usuaris.
     * @return CtrlUsuari.
     */
    public CtrlUsuari getCtrlUsuari() {
        return ctrlUsuari;
    }

    /**
     * Retorna el nom del jugador a l'índex donat.
     * @param index Índex del jugador.
     * @return Nom del jugador.
     */
    public String getNomJugador(int index) {
        return partida.getJugadors().get(index).getNom();
    }

    /**
     * Retorna els punts del jugador a l'índex donat.
     * @param index Índex del jugador.
     * @return Puntuació del jugador.
     */
    public int getPuntsJugador(int index) {
        return partida.getJugadors().get(index).getPuntuacio();
    }

    /**
     * Retorna les fitxes del jugador a l'índex donat en format String.
     * @param index Índex del jugador.
     * @return Fitxes del jugador.
     */
    public String getFitxesJugador(int index) {
        return partida.getJugadors().get(index).getFitxesString();
    }

    /**
     * Retorna el nombre de fitxes a la bossa.
     * @return Nombre de fitxes.
     */
    public int getNumFitxesBossa() {
        return partida.getBossa().size();
    }

    /**
     * Retorna la lletra d'una casella concreta.
     * @param fila Fila de la casella.
     * @param col Columna de la casella.
     * @return Lletra de la fitxa a la casella.
     */
    public char getLletraCasella(int fila, int col) {
        return getTauler().getCasella(fila, col).getFitxa().getLletra();
    }

    /**
     * Retorna el torn actual.
     * @return Índex del torn actual.
     */
    public int getTornActual() {
        return partida.getTornActual();
    }

    /**
     * Retorna el nom de l'idioma del diccionari.
     * @return Idioma.
     */
    public String getIdioma() {
        return diccionari.getIdioma();
    }

    /**
     * Retorna el temps restant del temporitzador.
     * @return Temps restant com a String.
     */
    public String getTempsRestant() {
        return partida.getTempsRestant();
    }

    /**
     * Retorna el temporitzador de la partida.
     * @return Temporitzador.
     */
    public Temporitzador getTemporitzador() {
        if (partida != null) return partida.getTemporitzador();
        return null;
    }


    /**
     * Retorna la llista de paraules d'un diccionari.
     * @param nomDiccionari Nom del diccionari.
     * @return Llista de paraules.
     */
    public List<String> getParaulesDeDiccionari(String nomDiccionari) {
        try {
            return cp.carregarDiccionari(nomDiccionari);
        } catch (IOException e) {
            //System.err.println("No s'ha pogut carregar el diccionari: " + nomDiccionari);
            return new ArrayList<>();
        }
    }

    /**
     * Retorna la llista de partides guardades.
     * @return Llista de noms de fitxers de partides.
     */
    public List<String> getPartidesGuardades() {
        try {
            return cp.listPartides();
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Retorna el torn del jugador actual.
     * @return Nom del jugador actual.
     */
    public String getTornJugador() {
        if (partida == null) return null;
        return partida.getTornJugador();
    }

    /**
     * Retorna el nom del jugador contrari al torn actual.
     * @return Nom del jugador contrari.
     */
    public String getTornJugadorContrari() {
        if (partida == null) return null;
        return partida.getTornJugadorContrari();
    }

    /**
     * Retorna la dificultat de la partida.
     * @return Dificultat.
     */
    public Dificultat getDificultat() {
        return dificultat;
    }

    /**
     * Retorna l'usuari 2 (en cas de partida a dos jugadors).
     * @return Usuari 2.
     */
    public Usuari getUsuari2() {
        return usuari2;
    }

    /**
     * Retorna l'usuari actiu.
     * @return Usuari actiu.
     */
    public Usuari getUsuariActiu() {
        return usuariActiu;
    }
    /**
     * Retorna els modes de joc disponibles.
     * @return Array de ModeJoc.
     */
    public ModeJoc[] getModesDisponibles() {
        return ModeJoc.getModesDisponibles();
    }

    /**
     * Retorna les dificultats disponibles.
     * @return Array de Dificultat.
     */
    public Dificultat[] getDificultatsDisponibles() {
        return Dificultat.getDificultatsDisponibles();
    }

    /**
     * Retorna els contrincants disponibles.
     * @return Array de Contrincant.
     */
    public Contrincant[] getContrincantsDisponibles() {
        return Contrincant.getContrincantsDisponibles();
    }

    /**
     * Retorna l'historial de partides.
     * @return Llista d'entrades de l'historial.
     */
    public List<String> carregarHistorial() {
        return cp.carregarHistorial();
    }

    // ---------- SETTERS ----------
    /**
     * Assigna l'usuari actiu.
     * @param u Usuari a assignar.
     */
    public void setUsuariActiu(Usuari u) {
        this.usuariActiu = u;
    }

    /**
     * Assigna l'usuari 2 (en cas de partida a dos jugadors).
     * @param u Usuari a assignar.
     */
    public void setUsuari2(Usuari u) {
        this.usuari2 = u;
    }

    /**
     * Assigna el mode de test.
     * @param modeTest Booleà que indica si s'està en mode test.
     */
    public void setModeTest(boolean modeTest) {
        this.modeTest = modeTest;
    }

   
    /**
     * Inicialitza l'estat intern del controlador.
     */
    public void inicialitzar() {
        // MILLORAT: Pausar partida actual abans d'inicialitzar
        if (this.partida != null) {
            try {
                this.partida.pausarPartida();
            } catch (Exception e) {
                // Ignorar errors de pausa si la partida ja està finalitzada
            }
        }
        
        this.partida = null;
        this.maquina = null;
        // NO inicialitzar usuariActiu aquí per mantenir la sessió
        // this.usuariActiu = null;
    }



    /**
     * Comprova si hi ha una partida inicialitzada.
     * @return Cert si hi ha partida, fals altrament.
     */
    public boolean hiHaPartida() {
        return this.partida != null;
    }

    /**
     * Realitza el login d'un usuari amb el controlador d'usuaris.
     * @param nomUsuari Nom de l'usuari.
     * @param contrasenya Contrasenya de l'usuari.
     * @throws LoginException Si hi ha un error iniciant sessió
     */
    public void login(String nomUsuari, String contrasenya) throws LoginException{
            ctrlUsuari.login(nomUsuari, contrasenya);
            usuariActiu = ctrlUsuari.getUsuariActiu();
    }

    /**
     * Registra un nou usuari utilitzant el controlador d'usuaris.
     * @param nomUsuari Nom del nou usuari.
     * @param contrasenya Contrasenya del nou usuari.
     * @return Cert si el registre és correcte, fals altrament.
     */
    public boolean registrarUsuari(String nomUsuari, String contrasenya) {
        if(nomUsuari == null || nomUsuari.isEmpty() || contrasenya == null || contrasenya.isEmpty()) {
            //System.err.println("Nom d'usuari o contrasenya no poden ser buits.");
            return false;
        }
        boolean si =  ctrlUsuari.registrar(nomUsuari, contrasenya);
        usuariActiu = ctrlUsuari.getUsuariActiu();
        
        return si;
    }

    /**
     * Realitza el login del segon usuari.
     * @param nomUsuari Nom del segon usuari.
     * @param contrasenya Contrasenya del segon usuari.
     */
    public void loginSegonUsuari(String nomUsuari, String contrasenya) throws LoginException{
        ctrlUsuari.loginsegonusuari(nomUsuari, contrasenya);
        usuari2 = ctrlUsuari.getUsuari2();
    }

    /**
     * Registra un nou segon usuari.
     * @param nomUsuari Nom del segon usuari.
     * @param contrasenya Contrasenya del segon usuari.
     * @return Cert si el registre és correcte, fals altrament.
     */
    public boolean registraSegonUsuari(String nomUsuari, String contrasenya) {
        boolean si = ctrlUsuari.registrarsegonusuari(nomUsuari, contrasenya);
        if (si) {
            usuari2 = ctrlUsuari.getUsuari2();
        }
        return si;
    }

    /**
     * Juga el torn actual
     * @return Booleà indicant si s'ha pogut jugar el torn correctament o no
     */
    public boolean jugarTorn(){
        passarTorn = 0;
        if (partida == null) {
            //System.out.println("Cap partida activa.");
            return false;
        }

        String jugador = partida.getTornJugador();

        try {
            if (!jugador.equals("BOT1") && !jugador.equals("BOT2")) {
                // Torn d'humà (no implementat aquí)
            } else {
                if (!maquina.jugarTorn(jugador)) passarTorn(1);
            }
    
            partida.avançarTorn();
    
            Jugador jugadorPartida = partida.getJugador(jugador);
            int fitxesARepartir;
            if (!jugador.equals("BOT1")) fitxesARepartir = partida.getNumFitxes() - jugadorPartida.getFitxes().size();
            else fitxesARepartir = 7 - jugadorPartida.getFitxes().size();
            partida.repartirFitxes(jugador, fitxesARepartir);
            return true;
        }
        catch(RepartirFitxesException ex) {
            //System.err.println("Error al jugar el torn de " + jugador + ": " + ex.getMessage());
            return false;
        }
        
    }

    /**
     * Passa el torn al següent jugador
     * @param i Valor (1 o 0) que s'ha de sumar per tindre un control de les vegades que es passa torn.
     * Si i = 0 vol dir que es passa torn de manera involuntària (Jugada invàlida, canvi de fitxes....)
     */
    public void passarTorn(int i) {
        partida.avançarTorn();
        passarTorn += i;
    }

    /**
     * Importa un diccionari i el seu alfabet des de fitxers.
     * @param idioma Idioma del diccionari.
     */
    public void importaDiccionari(String idioma) {
        // Només carrega si no està carregat o si l'idioma no coincideix
        
        if (this.diccionari != null && idioma.equalsIgnoreCase(this.diccionari.getIdioma())) {
            return;
        }
        try {
            
            Map<String, Pair<Integer, Integer>> alfabet = cp.importarAlfabet(idioma);
            List<String> paraules = cp.importarParaulesDiccionari(idioma);

            this.diccionari = new Diccionari(idioma, paraules, alfabet);
            this.diccionari.mostrarAlfabet();

        } catch (Exception e) {
            //System.err.println("Error important el diccionari: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Pausa la partida.
     * @return Cert si s'ha pausat correctament.
     */
    public boolean pausarPartida() {
        if (partida != null) {
            partida.pausarPartida();
            return true;
        }
        return false;
    }

    /**
     * Continua la partida després d'una pausa.
     * @return Cert si s'ha continuat correctament.
     */
    public boolean continuarPartida() {
        if (partida != null) {
            partida.continuarPartida();
            return true;
        }
        return false;
    }

    /**
     * Finalitza la partida.
     */
    public void finalitzarPartida() {
        if (partida != null) {
            partida.finalitzarPartida();
        }
    }

    /**
     * Guarda el rànquing al fitxer.
     */
    public void guardaRanquing() {
        cp.guardaRanquing(ranquing);
    }

    /**
     * Carrega el rànquing des del fitxer.
     * @return Ranquing carregat o nou si no existeix.
     */
    private Ranquing carregaRanquing() {
        return cp.carregaRanquing();
    }

    /**
     * Acaba la partida i actualitza estadístiques i rànquing.
     * @param guanyador Nom del guanyador.
     */
    public void acabarPartida(String guanyador) {
        if (partida == null) {
            //System.out.println("Cap partida activa.");
            return;
        }

        String jugador1 = partida.getJugadors().get(0).getNom();
        String jugador2 = partida.getJugadors().get(1).getNom();
        int punts1 = partida.getJugadors().get(0).getPuntuacio();
        int punts2 = partida.getJugadors().get(1).getPuntuacio();
        boolean perAbandonament = !(guanyador.equals(punts1 > punts2 ? jugador1 : jugador2));
        if(!modeTest)guardarHistorialPartida(jugador1, punts1, jugador2, punts2, perAbandonament, guanyador);

        List<Jugador> l = partida.getJugadors();
        Jugador jugadorGuanyador = null;
        for (Jugador j : l) {
            if (j.getNom().equals(guanyador)) {
                jugadorGuanyador = j;
                break;
            }
        }
        
        dificultat = partida.getDificultat();
        Float puntuacioGuanyador = jugadorGuanyador.getPuntuacio() * dificultat.getMultiplicador();
        
        if (guanyador.equals("BOT1") || guanyador.equals("BOT2") || modeTest) {
            // No actualitzem estadístiques per bots
        } else {
            
            Usuari usuariGuanyador = null;
            if (usuariActiu != null && usuariActiu.getNom().equals(guanyador)) {
                usuariGuanyador = usuariActiu;
            } else if (usuari2 != null && usuari2.getNom().equals(guanyador)) {
                usuariGuanyador = usuari2;
            }
            usuariGuanyador.getEstadistiques().actualitzarEstadistiques(partida.getIdPartida(), puntuacioGuanyador, partida.getDiccionari().getIdioma());
            usuariGuanyador.guardarEstadistiques();
        }
        if(!modeTest){
            ranquing.actualitzarRanquing(guanyador, "total", (float) puntuacioGuanyador);
            ranquing.actualitzarRanquing(guanyador, "dic:" + partida.getDiccionari().getIdioma(), (float) puntuacioGuanyador);
            if (partida.getContrarellotge()) ranquing.actualitzarRanquing(guanyador, "contrarellotge", (float) puntuacioGuanyador);
            if (ranquing.getPuntsUsuari(guanyador, "record") < puntuacioGuanyador) ranquing.actualitzarRanquing(guanyador, "record", (float) puntuacioGuanyador);
            if (ranquing.getPuntsUsuari(guanyador, "percentatge") == 0f) ranquing.actualitzarRanquing(guanyador, "percentatge", ((float) (puntuacioGuanyador)));
            else ranquing.actualitzarRanquing(guanyador, "percentatge", ((float) (puntuacioGuanyador + ranquing.getPuntsUsuari(guanyador, "percentatge")) / 2));

            partida.finalitzarPartida();
            guardaRanquing();
        }
    }

    /**
     * Guarda l'entrada d'una partida a l'historial.
     * @param jugador1 Nom del primer jugador.
     * @param punts1 Punts del primer jugador.
     * @param jugador2 Nom del segon jugador.
     * @param punts2 Punts del segon jugador.
     * @param perAbandonament Si la partida ha acabat per abandonament.
     * @param guanyador Nom del guanyador.
     */
    public void guardarHistorialPartida(String jugador1, int punts1, String jugador2, int punts2, boolean perAbandonament, String guanyador) {
        StringBuilder entrada = new StringBuilder();
        entrada.append("Guanyador: ").append(guanyador)
                .append(" (").append(perAbandonament ? "per abandonament" : "per punts").append(")\n");
        entrada.append(jugador1).append(": ").append(punts1).append(" punts, ")
                .append(jugador2).append(": ").append(punts2).append(" punts\n");
        entrada.append("Jugades:\n");

        List<Jugada> jugades = partida.getJugadesRealitzades();
        if (jugades == null) {
            entrada.append("No hi ha jugades registrades.\n");
            cp.guardarEntradaHistorial(entrada.toString());
            return;
        }
        else{
            for (Jugada jugada : jugades) {
                entrada.append(jugada.jugador).append(" → ")
                        .append(jugada.paraula).append(" (+").append(jugada.punts).append(")\n");
            }
        }

        cp.guardarEntradaHistorial(entrada.toString());
    }

    /**
     * Retorna el text amb les jugades que s'han jugat fins ara.
     * @return String amb les jugades realitzades.
     */
    public String getTextJugades() {
        if (partida == null) {
            return "No hi ha partida activa.\n";
        }

        StringBuilder textJugades = new StringBuilder();
        textJugades.append("Jugades:\n");

        List<Jugada> jugades = partida.getJugadesRealitzades();
        if (jugades == null || jugades.isEmpty()) {
            textJugades.append("No hi ha jugades registrades.\n");
        } else {
            for (Jugada jugada : jugades) {
                textJugades.append(jugada.jugador).append(" → ")
                        .append(jugada.paraula).append(" (+").append(jugada.punts).append(")\n");
            }
        }

        return textJugades.toString();
    }  

    /**
     * Reinicia el temporitzador a 1 segon.
     * Utilitzat per a proves 
     */
    public void zerosegons() {
        partida.getTemporitzador().setSegonsRestants(1);
        partida.finalitzarPartida();

    }

    /**
     * Confirma la jugada d'un jugador humà.
     * @param pos Llista de posicions de les fitxes jugades.
     * @throws JugadaInvalidaException
     */
    public void confirmarJugadaHumana(List<PosicioFitxa> pos) throws JugadaInvalidaException{
        partida.aplicarMovimentHumà(pos);
        partida.avançarTorn();
    }

    /**
     * Guarda el diccionari i l'alfabet en fitxers.
     * @param nom Nom del diccionari.
     * @param paraules Llista de paraules.
     * @param alfabet Mapa de lletres a (quantitat, puntuació).
     * @throws IOException Si hi ha un error d'escriptura.
     */
    public void guardarDiccionari(String nom, List<String> paraules, Map<String, Pair<Integer, Integer>> alfabet) throws IOException, DiccionariJaExisteixException {
        cp.guardarDiccionari(nom, paraules);
        cp.guardarAlfabet(nom, alfabet);
    }


    /**
     * Intercanvia fitxes de l'usuari actiu.
     * @param lletres Lletres que l'usuari vol canviar
     * @throws IntercanviFitxesException Si hi ha un error canviant fitxes
     */
    public void canviarFitxes(List<Character> lletres) throws IntercanviFitxesException {
        passarTorn = 0;
        partida.intercanviFitxes(lletres);
    }

    /**
     * Defineix una nova partida amb els paràmetres especificats.
     * @param mode
     * @param contrincant
     * @param dificultat
     * @param idioma
     * @param Usuari2
     * @return Booleà indicant si s'ha pogut definir la partida correctament o no
     */
    public boolean definirPartida(ModeJoc mode, Contrincant contrincant, Dificultat dificultat, String idioma, String Usuari2) {
        this.passarTorn = 0;
        Boolean ambTemporitzador;

        try {
            // MILLORAT: Assegurar que la partida anterior està netejada
            if (this.partida != null) {
                this.partida.pausarPartida();
                this.partida = null;
            }
            
            importaDiccionari(idioma);

            Jugador J1 = new Jugador(usuariActiu.getNom());
            Jugador J2;
            if (contrincant == Contrincant.MAQUINA) {
                J2 = new Jugador("BOT1");
            } else if (contrincant == Contrincant.JUGADOR) {
                J2 = new Jugador(Usuari2);
            } else {
                J2 = new Jugador("BOT1");
            }

            List<Jugador> jugadors = Arrays.asList(J1, J2);
            Tauler tauler = new Tauler(15);

            if (mode == ModeJoc.CONTRARRELLOTGE) ambTemporitzador = true;
            else ambTemporitzador = false;

            this.dificultat = dificultat; // <-- AFEGEIX AIXÒ

            partida = new Partida(1, jugadors, tauler, diccionari, ambTemporitzador, dificultat);

            partida.repartirFitxes(usuariActiu.getNom(), partida.getNumFitxes());
            if (contrincant == Contrincant.MAQUINA) partida.repartirFitxes("BOT1", 7);
            else if (contrincant == Contrincant.JUGADOR) partida.repartirFitxes(Usuari2, partida.getNumFitxes());

            maquina = new ControladorMaquina(partida);
            if (ambTemporitzador) {
                Temporitzador timer = new Temporitzador(20, () -> {
                    partida.finalitzarPartida();
                    acabarPartida(Usuari2);
                });
                partida.setTemporitzador(timer);
            }
            //System.out.println("Nova partida creada!");
            return true;
        }
        catch (RepartirFitxesException e) {
            //System.err.println("Error definint partida: " + e.getMessage());
            return false;
        }
    }

    /**
     * Guarda la partida actual amb el nom especificat.
     * @param fileName Nom del fitxer.
     * @return Cert si s'ha pogut guardar, fals altrament.
     */
    public boolean guardarPartida(String fileName) {
        if (partida == null) return false;
        partida.pausarPartida();
        try {
            cp.savePartida(partida, fileName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Carrega una partida des d'un fitxer.
     * @param fileName Nom del fitxer.
     * @return Cert si s'ha pogut carregar, fals altrament.
     * @throws JugadorNoPertanyPartidaException Si l'usuari actiu no pertany a la partida.
     */
    public boolean carregarPartida(String fileName) throws JugadorNoPertanyPartidaException {
        try {
            Partida p = cp.loadPartida(fileName);
            
            String nomUsuariActiu = usuariActiu.getNom();
            List<Jugador> jugadors = p.getJugadors();
            boolean pertanyAUsuari = false;
            
            for (Jugador jugador : jugadors) {
                if (jugador.getNom().equals(nomUsuariActiu)) {
                    pertanyAUsuari = true;
                    break;
                }
            }
            
            if (!pertanyAUsuari) {
                throw new JugadorNoPertanyPartidaException("L'usuari actiu no pertany a la partida carregada.");
                
            }
            this.diccionari = p.getDiccionari();


            if (p.ambTemporitzador()) {
                Temporitzador t = new Temporitzador(
                        p.getSegonsRestants(),
                        () -> p.finalitzarPartida()
                );
                p.setTemporitzador(t);
                
            }

            this.maquina = new ControladorMaquina(p);
            this.partida = p;
            return true;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una partida guardada.
     * @param fileName Nom del fitxer.
     * @return Cert si s'ha pogut eliminar, fals altrament.
     */
    public boolean eliminarPartida(String fileName) {
        try {
            return cp.deletePartida(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retorna la informació final de la partida.
     * @return Pair amb si la partida ha acabat i la llista de resultats.
     */
    public Pair<Boolean, List<Pair<String, Double>>> obtenirInfoFinal() {
        boolean finalDePartida = false;
        int numJugadors = partida.getJugadors().size();
        List<Jugador> jugadors = partida.getJugadors();

        if (passarTorn == numJugadors * 2) {
            finalDePartida = true;
        }
        if (getNumFitxesBossa() == 0) {
            for (Jugador j : jugadors) {
                if (j.getFitxes().isEmpty()) {
                    finalDePartida = true;
                    break;
                }
            }
        }

        double punts[] = new double[numJugadors];
        for (int i = 0; i < numJugadors; i++) punts[i] = jugadors.get(i).getPuntuacio();

        if (finalDePartida) {
            int penalitzacionsTotals = 0;
            int indexGuanyador = -1;
            for (int i = 0; i < numJugadors; i++) {
                Jugador j = jugadors.get(i);
                int penalitzacio = j.getFitxes().stream().mapToInt(Fitxa::getValor).sum();

                penalitzacionsTotals += penalitzacio;
                punts[i] -= penalitzacio;

                if (penalitzacio == 0 && getNumFitxesBossa() == 0) {
                    indexGuanyador = i;
                }
            }

            if (indexGuanyador != -1) {
                punts[indexGuanyador] += penalitzacionsTotals;
            }
        }

        double mult = partida.getDificultat().getMultiplicador();
        List<Pair<String, Double>> resultats = new ArrayList<>();
        for (int i = 0; i < numJugadors; i++) {
            String nom = jugadors.get(i).getNom();
            double puntsFinal = punts[i] * mult;
            resultats.add(new Pair<>(nom, puntsFinal));
        }

        return new Pair<>(finalDePartida, resultats);
    }

    /**
     * Retorna el nom del jugador amb més punts.
     * @return Nom del jugador guanyador o "Empat".
     */
    public String obtenirNomMesPunts() {
        if (partida == null) return null;

        int punts0 = getPuntsJugador(0);
        int punts1 = getPuntsJugador(1);

        if (punts0 > punts1) {
            return getNomJugador(0);
        } else if (punts1 > punts0) {
            return getNomJugador(1);
        } else {
            return "Empat";
        }
    }

    // ---------- MÈTODES PRIVATS PER ELIMINACIÓ D'USUARIS I DICCIONARIS ----------
    /**
     * Elimina un usuari del fitxer de persistència.
     * @param nomUsuari Nom de l'usuari a eliminar.
     */
    private void eliminarUsuariDeFitxer(String nomUsuari) {
        cp.eliminarUsuariDeFitxer(nomUsuari);
    }

    /**
     * Elimina les estadístiques d'un usuari.
     * @param nomUsuari Nom de l'usuari.
     */
    private void eliminarEstadistiquesUsuari(String nomUsuari) {
        cp.eliminarEstadistiquesUsuari(nomUsuari);
    }

    /**
     * Elimina el fitxer de rànquing d'un usuari.
     * @param nomUsuari Nom de l'usuari.
     */
    public void eliminarFitxerRanquingUsuari(String nomUsuari) {
        ranquing.eliminarUsuari(nomUsuari);
        cp.eliminarFitxerRanquingUsuari(ranquing);
    }

    /**
     * Elimina completament un usuari (fitxer, rànquing i estadístiques).
     * @param nomUsuari Nom de l'usuari.
     */
    public void eliminarUsuariComplet(String nomUsuari) {
        usuariActiu.eliminarUsuari();
        eliminarUsuariDeFitxer(nomUsuari);
        eliminarFitxerRanquingUsuari(nomUsuari);
        eliminarEstadistiquesUsuari(nomUsuari);
    }

    /**
     * Elimina un diccionari i el seu fitxer de lletres.
     * @param nomDiccionari Nom del diccionari.
     * @throws Exception Si hi ha un error d'eliminació.
     */
    public void eliminarDiccionari(String nomDiccionari) throws Exception {
        cp.eliminarDiccionari(nomDiccionari);

        ranquing.eliminarRanquingDeDiccionari(nomDiccionari);
        guardaRanquing();
    }

    /**
     * Retorna la llista de noms de diccionaris.
     * @return Llista de noms de diccionaris.
     */
    public List<String> getNomDiccionaris() {
        return cp.getNomDiccionaris();
    }

    /**
     * Canvia el nom de l'usuari actiu.
     * @param nouNom Nou nom d'usuari.
     * @return Cert si s'ha pogut canviar, fals altrament.
     */
    public boolean canviarNomUsuari(String nouNom) {
        if (usuariActiu == null) {
            
            //System.err.println("Error: Usuari actiu nul!");
            return false;
        }
        ctrlUsuari.setUsuariActiu(usuariActiu);

        return ctrlUsuari.canviarNomUsuari(nouNom);
    }

    /**
     * Canvia la contrasenya de l'usuari actiu.
     * @param novaPass Nova contrasenya.
     * @return Cert si s'ha pogut canviar, fals altrament.
     */
    public boolean canviarContrasenya(String novaPass) {
        return ctrlUsuari.canviarContrasenya(novaPass);
    }
}
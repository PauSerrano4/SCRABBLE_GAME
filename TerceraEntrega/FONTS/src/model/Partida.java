package model;

import java.util.*;
import util.*;
import exceptions.*;
import java.io.Serializable;

/**
 * Classe Partida - Representa una partida d'Scrabble.
 * Conté la informació del tauler, jugadors, diccionari, bossa de fitxes i estat de la partida.
 * @author Pau Serrano Sanz | pau.serrano.sanz@estudiantat.upc.edu
 */
public class Partida implements Serializable {

    // ---------- ATRIBUTS ----------
    /** Identificador únic de la partida */
    private int idPartida;
    /** Llista de jugadors participants a la partida */
    private List<Jugador> jugadors;
    /** Tauler de joc on es col·loquen les fitxes */
    private Tauler tauler;
    /** Diccionari utilitzat per validar les paraules */
    private Diccionari diccionari;
    /** Bossa de fitxes disponibles per repartir */
    private List<Fitxa> bossa;
    /** Estat de la partida: "enCurs", "pausada", "finalitzada" */
    private String estat;
    /** Índex del jugador al torn actual (0 per al primer jugador, 1 per al segon, etc.) */
    private int tornActual;
    /** Indica si la partida és a contrarellotge (true) o no (false) */
    private Boolean contrarellotge;
    /** Temporitzador per a la partida, si és a contrarellotge */
    private transient Temporitzador temporitzador;
    /** Indica si és la primera jugada de la partida */
    private boolean primeraJugada;
    /** Dificultat de la partida, que determina el nombre de fitxes per jugador i altres paràmetres */
    private Dificultat dificultat;
    /** Llista de jugades realitzades durant la partida */
    private List<Jugada> jugadesRealitzades; // Jugades realitzades en la partida

    // ---------- CONSTRUCTORES ----------
    /**
     * Constructora de la classe Partida.
     * @param idPartida Identificador únic de la partida
     * @param jugadors Llista de jugadors participants
     * @param tauler El tauler de joc
     * @param diccionari El diccionari utilitzat
     * @param contrarellotge Indica si la partida és a contrarellotge
     * @param dificultat Dificultat de la partida
     */
    public Partida(int idPartida, List<Jugador> jugadors, Tauler tauler, Diccionari diccionari, Boolean contrarellotge, Dificultat dificultat) {
        this.idPartida = idPartida;
        this.jugadors = jugadors;
        this.tauler = tauler;
        this.diccionari = diccionari;
        this.estat = "enCurs";
        this.tornActual = 0;
        this.contrarellotge = contrarellotge;
        this.primeraJugada = true;
        this.dificultat = dificultat;
        inicialitzarBossa();

        
    }

    // ---------- GETTERS ----------
    /**
     * Retorna la llista de fitxes a la bossa (només lectura).
     * @return Llista de Fitxa.
     */
    public List<Fitxa> getBossa() {
        return Collections.unmodifiableList(bossa);
    }

    /**
     * Retorna si la partida és a contrarellotge.
     * @return Cert si és a contrarellotge, fals altrament.
     */
    public Boolean getContrarellotge() {
        return contrarellotge;
    }

    /**
     * Retorna el diccionari utilitzat.
     * @return Diccionari.
     */
    public Diccionari getDiccionari() {
        return diccionari;
    }

    /**
     * Retorna la dificultat de la partida.
     * @return Dificultat.
     */
    public Dificultat getDificultat() {
        return dificultat;
    }

    /**
     * Retorna l'estat actual de la partida.
     * @return String amb l'estat ("enCurs", "pausada", "finalitzada").
     */
    public String getEstat() {
        return estat;
    }

    /**
     * Retorna l'identificador de la partida.
     * @return idPartida.
     */
    public int getIdPartida() {
        return idPartida;
    }

    /**
     * Retorna el jugador pel nom especificat.
     * @param nom Nom del jugador a buscar.
     * @return Jugador corresponent o null si no es troba.
     */
    public Jugador getJugador(String nom) {
        for (Jugador j : jugadors) {
            if (j.getNom().equals(nom)) return j;
        }
        return null;
    }

    /**
     * Retorna el jugador per índex.
     * @param i Índex del jugador.
     * @return Jugador.
     */
    public Jugador getJugador(int i) {
        return jugadors.get(i);
    }

    /**
     * Retorna el jugador actual (a qui li toca el torn).
     * @return Jugador actual.
     */
    public Jugador getJugadorActual() {
        return jugadors.get(tornActual);
    }

    /**
     * Retorna la llista de jugadors.
     * @return Llista de Jugador.
     */
    public List<Jugador> getJugadors() {
        return jugadors;
    }

    /**
     * Retorna la llista de jugades realitzades.
     * @return Llista de Jugada.
     */
    public List<Jugada> getJugadesRealitzades() {
        return jugadesRealitzades;
    }

    /**
     * Retorna el nombre de fitxes per jugador.
     * @return Nombre de fitxes.
     */
    public int getNumFitxes() {
        return dificultat.getRackSize();
    }

    /**
     * Retorna el tauler de la partida.
     * @return Tauler.
     */
    public Tauler getTauler() {
        return tauler;
    }

    /**
     * Retorna el temps restant del temporitzador en format llegible.
     * @return Temps restant com a String.
     */
    public String getTempsRestant() {
        if (temporitzador != null) return temporitzador.getTempsFormatejat();
        else return "∞";
    }

    /**
     * Retorna el temporitzador de la partida.
     * @return Temporitzador.
     */
    public Temporitzador getTemporitzador() {
        return temporitzador;
    }

    /**
     * Retorna el torn actual (índex).
     * @return Índex del torn actual.
     */
    public int getTornActual() {
        return tornActual;
    }

    /**
     * Retorna el nom del jugador al qual li toca jugar.
     * @return Nom del jugador actual.
     */
    public String getTornJugador() {
        return jugadors.get(tornActual).getNom();
    }

    /**
     * Retorna el nom del jugador contrari al torn actual.
     * @return Nom del jugador contrari.
     */
    public String getTornJugadorContrari() {
        return jugadors.get((tornActual + 1) % jugadors.size()).getNom();
    }

    /**
     * Retorna els segons restants del temporitzador.
     * @return Segons restants o -1 si no hi ha temporitzador.
     */
    public int getSegonsRestants() {
        if (temporitzador != null) return temporitzador.getSegonsRestants();
        else return -1;
    }

    // ---------- SETTERS ----------
    /**
     * Assigna un nou diccionari a la partida.
     * @param diccionari Diccionari a assignar.
     */
    public void setDiccionari(Diccionari diccionari) {
        this.diccionari = diccionari;
    }

    /**
     * Assigna un temporitzador a la partida.
     * @param temporitzador Temporitzador a assignar.
     */
    public void setTemporitzador(Temporitzador temporitzador) {
        this.temporitzador = temporitzador;
    }

    // ---------- MÈTODES PÚBLICS ----------
    /**
     * Inicialitza la bossa de fitxes amb les lletres i puntuacions indicades a l'alfabet del diccionari.
     * Omple i barreja aleatòriament la llista de fitxes.
     */
    public void inicialitzarBossa() {
        bossa = new ArrayList<>();
        Map<String, Pair<Integer, Integer>> alfabet = diccionari.getAlfabet();

        for (Map.Entry<String, Pair<Integer, Integer>> entrada : alfabet.entrySet()) {
            String lletra = entrada.getKey();
            int quantitat = entrada.getValue().first;
            int puntuacio = entrada.getValue().second;

            for (int i = 0; i < quantitat; i++) {
                bossa.add(new Fitxa(lletra.charAt(0), puntuacio));
            }
        }

        Collections.shuffle(bossa); // Barreja la bossa
    }

    /**
     * Mostra el tauler actual per consola.
     * Funció per debugging i visualització de l'estat del tauler.
     */
    public void mostrarTauler() {
        System.out.println(tauler.mostrarTauler());
    }

    /**
     * Reparteix fitxes a un jugador.
     * @param nomJugador Nom del jugador.
     * @param numFitxes Nombre de fitxes a repartir.
     * @throws RepartirFitxesException Si hi ha un error en repartir les fitxes, com jugador no trobat,
     */
    public void repartirFitxes(String nomJugador, int numFitxes) throws RepartirFitxesException {
        Jugador jugador = getJugador(nomJugador);

        if (jugador == null) {
            throw new RepartirFitxesException("Jugador no trobat: " + nomJugador);
        }
        if (numFitxes < 0) {
            throw new RepartirFitxesException(
                "Nombre de fitxes a repartir negatiu: " + numFitxes);
        }
        if (bossa.isEmpty()) {
            throw new RepartirFitxesException("No hi ha fitxes disponibles a la bossa.");
        }
        if (bossa.size() < numFitxes) {
            throw new RepartirFitxesException(
                "Només queden " + bossa.size() +
                " fitxes a la bossa, però se'n volen repartir " + numFitxes);
        }

        int fitxesRepartides = 0;
        Iterator<Fitxa> it = bossa.iterator();

        while (it.hasNext() && fitxesRepartides < numFitxes) {
            Fitxa f = it.next();
            jugador.afegirFitxa(f);
            it.remove();
            fitxesRepartides++;
        }

        //System.out.println("S'han repartit " + fitxesRepartides + " fitxes a " + nomJugador + ".");
    }

    /**
     * Afegeix una fitxa dins la bossa.
     * @param fitxa Fitxa que volem introduir dins la bossa.
     */
    public void fitxaABossa(Fitxa fitxa) {
        bossa.add(fitxa);
    }

    /**
     * Avança el torn al següent jugador.
     */
    public void avançarTorn() {
        this.tornActual = (this.tornActual + 1) % jugadors.size();
    }

    /**
     * Pausa la partida (canvia l'estat a "pausada" i para el temporitzador si cal).
     */
    public void pausarPartida() {
        this.estat = "pausada";
        if (temporitzador != null) temporitzador.parar();
    }

    /**
     * Continua la partida si estava pausada (canvia l'estat a "enCurs" i reinicia el temporitzador).
     */
    public void continuarPartida() {
        this.estat = "enCurs";
        if (temporitzador != null) temporitzador.iniciar();
    }

    /**
     * Canvia l'estat de la partida a "finalitzada".
     */
    public void finalitzarPartida() {
        this.estat = "finalitzada";
        if (temporitzador != null) temporitzador.parar();
    }

    /**
     * Retorna true si la partida té un temporitzador actiu.
     * @return true si té temporitzador, false altrament.
     */
    public boolean ambTemporitzador() {
        return contrarellotge;
    }

    /**
     * Retorna true si el torn actual és d'un bot.
     * @return true si juga la màquina, false altrament.
     */
    public boolean contraMaquina() {
        return (getTornJugador().startsWith("BOT"));
    }

    /**
     * Afegeix una jugada realitzada a la llista de jugades.
     * @param jugada Paraula jugada.
     * @param puntuacio Puntuació obtinguda.
     */
    public void afegirjugadaRealitzada(String jugada, int puntuacio) {
        if (jugadesRealitzades == null) {
            jugadesRealitzades = new ArrayList<>();
        }
        jugadesRealitzades.add(new Jugada(jugadors.get(tornActual).getNom(), jugada, puntuacio));
    }

    /**
     * Aplica el moviment d'un jugador humà.
     * @param jugada Llista de posicions de fitxes col·locades.
     * @throws JugadaInvalidaException Si la jugada no és vàlida.
     */
    public void aplicarMovimentHumà(List<PosicioFitxa> jugada) throws JugadaInvalidaException {

        // 0) Almenys una fitxa
        if (jugada.isEmpty())
            throw new JugadaInvalidaException("Has de col·locar almenys una fitxa");

        Jugador jugador = getJugadorActual();
        List<Fitxa> atril = jugador.getFitxes();
        List<Fitxa> fitxesNoves = new ArrayList<>();
        List<Pair<Integer,Integer>> posFixades = new ArrayList<>();
        boolean fitxesRetiradesAtril = false;
        boolean fitxesColocadesTauler = false;

        try {
            /* ========== VALIDACIONS INICIALS ========== */
            // Validar coordenades dins dels límits del tauler (0-14)
            for (PosicioFitxa p : jugada) {
                if (p.getFila() < 0 || p.getFila() >= 15 || p.getCol() < 0 || p.getCol() >= 15) {
                    throw new JugadaInvalidaException("Coordenades fora dels límits del tauler");
                }
                // Validar que no hi ha solapament amb fitxes existents
                if (tauler.getCasella(p.getFila(), p.getCol()).teFitxa()) {
                    throw new JugadaInvalidaException("No pots col·locar fitxes sobre caselles ocupades");
                }
            }

            /* ========== 1) Geometria: mateixa fila o columna + contigües ========== */
            List<Pair<Integer,Integer>> coords = new ArrayList<>();
            for (PosicioFitxa p : jugada)
                coords.add(new Pair<>(p.getFila(), p.getCol()));

            boolean mateixaFila = coords.stream().map(p -> p.first).distinct().count() == 1;
            boolean mateixaCol  = coords.stream().map(p -> p.second).distinct().count() == 1;
            
            // Validar que no és diagonal
            if (coords.size() > 1 && !mateixaFila && !mateixaCol) {
                throw new JugadaInvalidaException("Les fitxes han d'estar en la mateixa fila o columna");
            }
            
            // --- ORDENEM la jugada en l'ordre de lectura de la paraula ---------------
            List<PosicioFitxa> jugadaOrdenada = new ArrayList<>(jugada);
            jugadaOrdenada.sort(mateixaFila      // si totes són a la mateixa fila → ordre per columna
                ? Comparator.comparingInt(PosicioFitxa::getCol)
                : Comparator.comparingInt(PosicioFitxa::getFila)); // si mateixa columna → ordre per fila
            
            //System.out.println("DEBUG: coords = "+coords);   // ex: [[7,7], [7,9]]
            //System.out.println("DEBUG: mateixaFila = "+mateixaFila+", mateixaCol = "+mateixaCol);
            
            boolean unaFitxaNova = coords.size() == 1;
            boolean horitz = mateixaFila;
            
            /* --- deducir orientación si solo hay 1 ficha --- */
            if (unaFitxaNova) {
                int f = coords.get(0).first;
                int c = coords.get(0).second;

                boolean veinaAmunt  = f > 0           && tauler.getCasella(f-1, c).teFitxa();
                boolean veinaAvall  = f < tauler.getMida()-1 && tauler.getCasella(f+1, c).teFitxa();
                boolean veinaEsq    = c > 0           && tauler.getCasella(f, c-1).teFitxa();
                boolean veinaDreta  = c < tauler.getMida()-1 && tauler.getCasella(f, c+1).teFitxa();

                boolean contacteVertical   = veinaAmunt || veinaAvall;
                boolean contacteHoritzontal = veinaEsq  || veinaDreta;

                if (contacteVertical && !contacteHoritzontal)  horitz = false; // palabra vertical
                else if (contacteHoritzontal && !contacteVertical) horitz = true;  // palabra horizontal
                // Si toca en ambos sentidos (esquina), se mantiene horitz=true por defecto
            }

            if (!unaFitxaNova && !(mateixaFila ^ mateixaCol))
                throw new JugadaInvalidaException("Fila o columna única");

            coords.sort(mateixaFila
            ? Comparator.comparingInt(p -> p.second)
            : Comparator.comparingInt(p -> p.first));

            // Validar contiguïtat (no gaps)
            if (!tauler.sonContigues(coords, mateixaFila))
                throw new JugadaInvalidaException("Les fitxes han de ser contigües");
                
            // === REGLES DE CONECTIVITAT SCRABBLE ===================================
            if (tauler.estaBuit()) {
            // ➜ És la primera jugada; ha de passar pel centre
            boolean tocaCentre = coords.stream()
                .anyMatch(p -> p.first == 7 && p.second == 7);
                if (!tocaCentre)
                    throw new JugadaInvalidaException("La primera paraula ha d'ocupar la casella central");
            } else {
                // ➜ Partida en marxa; alguna fitxa nova ha de tocar una fitxa existent
                boolean connecta = coords.stream().anyMatch(p -> tauler.teVeinaOcupada(p.first, p.second));
                if (!connecta)
                    throw new JugadaInvalidaException("La paraula ha de connectar amb fitxes ja col·locades");
            }

            /* ========== 2) Treure fitxes de l'atril ========== */
            //System.out.println("DEBUG: Atril abans de treure fitxes: " + jugador.getFitxesString());
            
            for (PosicioFitxa p : jugadaOrdenada) {      // ← usem la llista ordenada
                char l = p.getLletra();
                Fitxa f = atril.stream()
                            .filter(ft -> ft.getLletra() == l)
                            .findFirst()
                            .orElseThrow(() ->
                    new JugadaInvalidaException("No tens la fitxa «" + l + "»"));
                atril.remove(f);
                fitxesNoves.add(f);                      // ordre coherent amb paraula
            }
            fitxesRetiradesAtril = true;
            //System.out.println("DEBUG: Fitxes retirades de l'atril: " + fitxesNoves.size());
            //System.out.println("DEBUG: Atril després de retirar fitxes: " + jugador.getFitxesString());

            /* ========== 3) Construir i colocar provisional ========== */
            int f0 = jugada.get(0).getFila();
            int c0 = jugada.get(0).getCol();
            if (!unaFitxaNova) horitz = mateixaFila;
            
            // Després de tenir f0, c0 i boolean horitz:
            int filaIni = f0;          // punt d'inici que passarem al Tauler
            int colIni  = c0;

            if (horitz) {                               // paraula horitzontal
                while (colIni - 1 >= 0 &&
                    tauler.getCasella(filaIni, colIni - 1).teFitxa()) {
                    colIni--;                           // camina a l'esquerra
                }
            } else {                                    // paraula vertical
                while (filaIni - 1 >= 0 &&
                    tauler.getCasella(filaIni - 1, colIni).teFitxa()) {
                    filaIni--;                          // camina cap amunt
                }
            }

            String motPrincipal = tauler.construirParaula(
                    filaIni, colIni, horitz, coords, fitxesNoves);

            String motPrincipalConvertit = DigrafMapper.desferConversioParaula(motPrincipal, diccionari.getIdioma());
            //System.out.println("DEBUG: motPrincipal = " + motPrincipalConvertit);

            Pair<List<Pair<Integer,Integer>>,Integer> res =
                tauler.colocarParaula(fitxesNoves, motPrincipal, filaIni, colIni, horitz);

            posFixades = res.first;
            int puntsMov = res.second;
            fitxesColocadesTauler = true;
            
            //System.out.println("DEBUG: Posicions fixades al tauler: " + posFixades.size());

            if (posFixades.isEmpty()) {                     // no encaixa
                throw new JugadaInvalidaException("La paraula no encaixa");
            }

            /* ========== 4) Validació diccionari (principal + creuades) ========== */
            List<String> paraulesFormades = tauler.getParaulesFormades(posFixades, horitz);
            //System.out.println("DEBUG: Paraules formades: " + paraulesFormades);
            
            for (String w : paraulesFormades) {
                //System.out.println("DEBUG: Validant paraula: " + w);
                if (contieneComodin(w)) {
                    if (!validarPalabraConComodin(w)) {
                        throw new JugadaInvalidaException("«" + w + "» no és al diccionari");
                    }
                    //else System.out.println("DEBUG: Paraula amb comodí vàlida: " + w);
                } else {
                    if (!diccionari.validarParaula(w)) {
                        String wConvertit = DigrafMapper.desferConversioParaula(w, diccionari.getIdioma());
                        throw new JugadaInvalidaException("«" + wConvertit + "» no és al diccionari");
                    }
                }
            }
            
            /* ========== 5) Confirmar, puntuar, reomplir ========== */
            tauler.confirmar(posFixades);                   // crema multiplicadors
            
            if(fitxesNoves.size() == dificultat.getRackSize()) {
                puntsMov += 50; //si utilitzes totes les lletres del atril hi ha bonificació extra 
            }

            jugador.actualitzarPuntuacio(puntsMov);
            try{
                repartirFitxes(jugador.getNom(), fitxesNoves.size());
            }
            catch(RepartirFitxesException ex) {
                ex.printStackTrace();
            }
            
            String motPrincipalConv = DigrafMapper.desferConversioParaula(motPrincipal, diccionari.getIdioma());
            afegirjugadaRealitzada(motPrincipalConv, puntsMov);
            primeraJugada = false;
            
            //System.out.println("DEBUG: Jugada completada amb èxit");
            
        } catch (JugadaInvalidaException e) {
            // REVERSIÓ COMPLETA QUAN FALLA LA JUGADA
            //System.out.println("DEBUG: Jugada invàlida, revertint canvis...");
            
            // 1) Revertir fitxes del tauler si s'han col·locat
            if (fitxesColocadesTauler && !posFixades.isEmpty()) {
                //System.out.println("DEBUG: Revertint " + posFixades.size() + " posicions del tauler");
                tauler.revertir2(posFixades);
            }
            
            // 2) Tornar fitxes a l'atril si s'han retirat
            if (fitxesRetiradesAtril && !fitxesNoves.isEmpty()) {
                //System.out.println("DEBUG: Tornant " + fitxesNoves.size() + " fitxes a l'atril");
                atril.addAll(fitxesNoves);
            }
            
            //System.out.println("DEBUG: Atril després de reversió: " + jugador.getFitxesString());
            //System.out.println("DEBUG: Error: " + e.getMessage());
            
            // Re-llançar l'excepció original
            throw e;
        }
    }

    /**
     * Intercanvia fitxes de l'atril del jugador actual amb la bossa.
     * @param lletres Llista de lletres a intercanviar.
     * @throws IntercanviFitxesException Si no hi ha prou fitxes a la bossa o si el jugador no té les lletres indicades.
     */
    public void intercanviFitxes(List<Character> lletres) throws IntercanviFitxesException {
        if (bossa.size() < lletres.size()) {
            throw new IntercanviFitxesException(
                "No hi ha prou fitxes a la bossa per intercanviar " + lletres.size());
        }
        Jugador j = getJugadorActual();
        if (j == null) {
            throw new IntercanviFitxesException("Jugador actual no trobat");
        }

        List<Fitxa> aTreure = new ArrayList<>();

        // 1) treure de l’atril
        for (char c : lletres) {
            Fitxa f = j.getFitxes().stream()
                    .filter(ft -> ft.getLletra() == c && !aTreure.contains(ft))
                    .findFirst()
                    .orElse(null);
            if (f == null) throw new IntercanviFitxesException("No tens la fitxa «" + c + "» al teu atril");
            aTreure.add(f);
        }
        j.getFitxes().removeAll(aTreure);

        // 2) posar-les a la bossa i agafar noves
        bossa.addAll(aTreure);
        Collections.shuffle(bossa);

        for (int i = 0; i < lletres.size(); i++) {
            j.afegirFitxa(bossa.remove(0));
        }

        avançarTorn();
    }


    // ---------- MÈTODES PRIVATS ----------
    /**
     * Comprova si una paraula conté un comodí.
     * @param paraula Paraula a comprovar.
     * @return true si conté comodí, false altrament.
     */
    private boolean contieneComodin(String paraula) {
        return paraula.contains("#");
    }

    /**
     * Valida una paraula amb comodí comprovant totes les possibles substitucions.
     * @param paraula Paraula amb comodí.
     * @return true si alguna substitució és vàlida, false altrament.
     */
    private boolean validarPalabraConComodin(String paraula) {
        for (char c = 'A'; c <= 'Z'; c++) {
            String possible = paraula.replace('#', c);
            if (diccionari.validarParaula(possible)) {
                return true;
            }
        }
        return false;
    }
}

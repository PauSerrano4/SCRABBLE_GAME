package controller;

import java.util.*;
import model.*;
import util.*;

/**
 * Controlador per la intel·ligència artificial (BOT) del joc Scrabble.
 * Implementa un algorisme avançat basat en cross-checks, anchors i navegació
 * per tries per generar jugades òptimes automàticament.
 * 
 * @author Marc Gil
 */
public class ControladorMaquina {
    
    // Atributs
    /** Partida actual on la màquina ha de jugar */
    public Partida partida;
    
    /** Jugador màquina actual que està jugant */
    private Jugador jugador;
    
    /** Diccionari utilitzat per validar paraules */
    private Diccionari diccionari;
    
    /** Tauler de joc actual */
    private Tauler tauler;
    
    /** Cross-checks per moviments horitzontals */
    private int[][] crossChecksAcross;
    
    /** Cross-checks per moviments verticals */
    private int[][] crossChecksDown;

    /**
     * Constructor del controlador de la màquina.
     * Inicialitza el controlador amb una partida específica i obté
     * referències al diccionari i tauler de la partida.
     * 
     * @param partida La partida actual on la màquina ha de jugar
     */
    public ControladorMaquina(Partida partida) {
        this.partida = partida;
        this.diccionari = partida.getDiccionari();
        this.tauler = partida.getTauler();
    }

    /**
     * Executa el torn de joc per un jugador màquina.
     * Assigna el jugador actual i inicia el procés de generació de jugades.
     * 
     * @param nomJugador Nom del jugador màquina que ha de jugar
     * @return retorna si ha pogut generar una jugada o no
     */
    public boolean jugarTorn(String nomJugador) {
        this.jugador = partida.getJugador(nomJugador);
        return generateMove();
    }

    /**
     * Genera i executa la millor jugada possible per la màquina.
     * Si és la primera jugada (centre buit), utilitza un algorisme específic.
     * Altrament, calcula cross-checks i genera jugades per totes les files i columnes.
     * Si no pot generar cap jugada, intercanvia fitxes aleatòriament.
     * @return Retorna si ha pogut generar una jugada o no
     */
    private boolean generateMove() {
        int mida = tauler.getMida();
        List<Play> totesJugades = new ArrayList<>();

        if (tauler.getCasella(7, 7) != null && !tauler.getCasella(7, 7).teFitxa()) {
            this.crossChecksAcross = new int[mida][mida];
            this.crossChecksDown = new int[mida][mida];
            int allLettersMask = (1 << 26) - 1;
            Anchor primerAnchor = new Anchor(7, 7, allLettersMask, 7);

            List<Casella> linea = new ArrayList<>();
            for (int i = 0; i < mida; i++) {
                linea.add(tauler.getCasella(7, i));
            }

            Node node = diccionari.getArrel();

            List<Fitxa> rack = new ArrayList<>(jugador.getFitxes());
            leftPart("", diccionari.getArrel(), primerAnchor, linea, totesJugades, primerAnchor.getMaxLeft(), 0, false, rack);
            leftPart("", diccionari.getArrel(), primerAnchor, linea, totesJugades, primerAnchor.getMaxLeft(), 0, true, rack);
        } 
        else {
            computeCrossChecks();
            for (int fila = 0; fila < mida; fila++) {
                totesJugades.addAll(generateMoveRow(fila, false));
            }
            for (int columna = 0; columna < mida; columna++) {
                totesJugades.addAll(generateMoveRow(columna, true));
            }
        }

        if (!totesJugades.isEmpty()) {
            Play millorJugada = totesJugades.get(0);
            for (Play jugada : totesJugades) {
                if (jugada.getScore() > millorJugada.getScore()) {
                    millorJugada = jugada;
                }
            }

            String paraula = millorJugada.getWord();
            int filaInicial = millorJugada.getFila();
            int columnaInicial = millorJugada.getColumna();
            boolean horitzontal = millorJugada.isHorizontal();
            int puntuacioJugada = millorJugada.getScore();

            List<Fitxa> fichasColocar = stringToFitxa(paraula, filaInicial, columnaInicial, horitzontal);
            Pair<List<Pair<Integer, Integer>>, Integer> resultat = tauler.colocarParaula(fichasColocar, paraula, filaInicial, columnaInicial, horitzontal);
            if (!resultat.first.isEmpty()) {
                for (Fitxa f : fichasColocar) {
                    jugador.colocarFitxa(f);
                }
                if (jugador.getFitxes() == null) {
                    puntuacioJugada += 50;
                }
                jugador.actualitzarPuntuacio(puntuacioJugada);
                partida.afegirjugadaRealitzada(paraula, puntuacioJugada);;
            }
        }
        else {
            int fichasEnBolsa = partida.getBossa().size(); 
            List<Fitxa> rack = new ArrayList<>(jugador.getFitxes());

            if (fichasEnBolsa > 0) {
                Random rnd = new Random();

                int numFitxesCanviar = rnd.nextInt(rack.size()) + 1;

                int fitesACanviar = Math.min(fichasEnBolsa, numFitxesCanviar);
                Collections.shuffle(rack);
                List<Fitxa> aIntercanviar = rack.subList(0, numFitxesCanviar);

                for(Fitxa f : aIntercanviar) {
                    jugador.eliminarFitxa(f);
                    partida.fitxaABossa(f);
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Genera jugades possibles per una fila o columna específica.
     * 
     * @param idx Índex de la fila o columna
     * @param transposed True si es tracta d'una columna (transposada), false per fila
     * @return Llista de jugades possibles per aquesta línia
     */
    private List<Play> generateMoveRow(int idx, boolean transposed) {
        List<Play> jugades = new ArrayList<>();
        List<Casella> linea = new ArrayList<>();
        int n = tauler.getMida();

        for (int i = 0; i < n; i++) {
            Casella c = transposed ? tauler.getCasella(i, idx) : tauler.getCasella(idx, i);
            linea.add(c);
        }

        List<Anchor> anchors = getAnchors(linea, idx, transposed);
        for (Anchor a : anchors) {
            List<Fitxa> atril = new ArrayList<>(jugador.getFitxes());
            leftPart("", diccionari.getArrel(), a, linea, jugades, a.getMaxLeft(), 0, transposed, atril);
        }

        Iterator<Play> it = jugades.iterator();
        while(it.hasNext()) {
            Play jugada = it.next();
            boolean todasValidas = true;

            List<Fitxa> fichasColocar = stringToFitxa(jugada.getWord(), jugada.getFila(), jugada.getColumna(), jugada.isHorizontal());
            for (int i = 0; i < fichasColocar.size(); i++) {
                Fitxa f = fichasColocar.get(i);
                int fila = jugada.isHorizontal() ? jugada.getFila() : jugada.getFila() + i;
                int columna = jugada.isHorizontal() ? jugada.getColumna() + i : jugada.getColumna();

                if (f == null) {
                    continue;
                }

                String cruzada = construirParaulaCreuada(fila, columna, jugada.isHorizontal(), f.getLletra());
                if(cruzada.length() > 1 && !diccionari.validarParaula(cruzada)) {
                    todasValidas = false;
                    break;
                } 
            }

            if(!todasValidas) {
                it.remove();
            }

        }

        return jugades;
    }

    /**
     * Identifica i retorna els anchors (punts d'ancoratge) d'una línia.
     * Un anchor és una casella buida adjacent a una fitxa col·locada.
     * 
     * @param linea Línia de caselles a analitzar
     * @param idx Índex de la fila o columna
     * @param transposed True si la línia és transposada (columna)
     * @return Llista d'anchors trobats
     */
    private List<Anchor> getAnchors(List<Casella> linea, int idx, boolean transposed) {
        List<Anchor> anchors = new ArrayList<>();
        int n = linea.size();

        for (int i = 0; i < n; i++) {
            Casella c = linea.get(i);
            if (!c.teFitxa()) {
                boolean esAnchor = false;
                if ((i > 0 && linea.get(i - 1).teFitxa()) || (i < n - 1 && linea.get(i + 1).teFitxa())) {
                    esAnchor = true;
                }

                int fila = transposed ? i : idx;
                int columna = transposed ? idx : i;

                if (esAnchor) {
                    int maxLeft = 0;
                    int j = i - 1;
                    int crossCheck = transposed ? crossChecksDown[fila][columna] : crossChecksAcross[fila][columna];

                    while (j > 0 && !linea.get(j).teFitxa() && !linea.get(j - 1).teFitxa()) {
                        j--;
                        maxLeft++;
                    }
                    if (j == 0 && !linea.get(0).teFitxa()) {
                        maxLeft++;
                    }
                    anchors.add(new Anchor(fila, columna, crossCheck, maxLeft));
                }
            }
        }
        return anchors;
    }

    /**
     * Calcula els cross-checks per totes les caselles buides del tauler.
     * Un cross-check determina quines lletres són vàlides en una posició
     * basant-se en les paraules perpendiculars que es formarien.
     */
    private void computeCrossChecks() {
        int n = tauler.getMida();
        this.crossChecksAcross = new int [n][n];
        this.crossChecksDown = new int [n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Casella c = tauler.getCasella(i, j);
                if (!c.teFitxa()) {
                    crossChecksAcross[i][j] = calcularCrossCheck(i, j, false);
                    crossChecksDown[i][j] = calcularCrossCheck(i, j, true);
                }
            }
        }
    }

    /**
     * Calcula el cross-check per una casella específica.
     * Determina quines lletres produirien paraules vàlides en direcció perpendicular.
     * 
     * @param fila Fila de la casella
     * @param columna Columna de la casella
     * @param transposed True per direcció vertical, false per horitzontal
     * @return Màscara de bits amb les lletres vàlides
     */
    private int calcularCrossCheck(int fila, int columna, boolean transposed) {
        String prefix = "", sufix = "";
        int n = tauler.getMida();

        if (transposed) {
            for (int r = fila - 1; r >= 0 && tauler.getCasella(r, columna).teFitxa(); r--) {
                prefix = tauler.getCasella(r, columna).getFitxa().getLletra() + prefix;
            }
            for (int r = fila + 1; r < n && tauler.getCasella(r, columna).teFitxa(); r++) {
                sufix = sufix + tauler.getCasella(r, columna).getFitxa().getLletra();
            }
        } 
        else {
            for (int c = columna - 1; c >= 0 && tauler.getCasella(fila, c).teFitxa(); c--) {
                prefix = tauler.getCasella(fila, c).getFitxa().getLletra() + prefix;
            }
            for (int c = columna + 1; c < n && tauler.getCasella(fila, c).teFitxa(); c++) {
                sufix = sufix + tauler.getCasella(fila, c).getFitxa().getLletra();
            }
        }

        int mask = 0;
        for (char L = 'A'; L <= 'Z'; L++) {
            if (diccionari.validarParaula(prefix + L + sufix)) {
                mask |= 1 << (L - 'A');
            }
        }
        return mask;
    }

    /**
     * Genera la part esquerra d'una paraula a partir d'un anchor.
     * Aquesta funció recursiva construeix prefixos vàlids abans de l'anchor.
     * 
     * @param soFar Paraula construïda fins ara
     * @param node Node actual del trie del diccionari
     * @param anchor Punt d'ancoratge actual
     * @param linea Línia de caselles
     * @param jugades Llista on afegir jugades vàlides
     * @param limit Límit màxim de lletres a l'esquerra
     * @param usedLeft Nombre de lletres usades a l'esquerra
     * @param transposed True si la direcció és transposada
     * @param rack Fitxes disponibles al rack
     */
    private void leftPart(String soFar, Node node, Anchor anchor, List<Casella> linea, List<Play> jugades, int limit, int usedLeft, boolean transposed, List<Fitxa> rack) {
        int pos = transposed ? anchor.getFila() : anchor.getColumna();

        extendRight(soFar, node, anchor, pos, usedLeft, linea, jugades, transposed, rack);

        if (limit > 0) {
            for (int i = 0; i < rack.size(); i++) {
                Fitxa f = rack.get(i);
                char L = f.getLletra();
                Node child = node.getFill(L);

                if (child != null) {
                    List<Fitxa> novaRack = new ArrayList<>(rack);
                    novaRack.remove(i);
                    String nouSoFar = L + soFar;
                    int newUsedLeft = usedLeft + 1;
                    int newLimit = limit - 1;

                    leftPart(nouSoFar, child, anchor, linea, jugades, newLimit, newUsedLeft, transposed, novaRack);
                }
            }
        }
    }

    /**
     * Estén una paraula cap a la dreta a partir d'una posició.
     * Aquesta funció recursiva col·loca fitxes cap a la dreta de l'anchor.
     * 
     * @param soFar Paraula construïda fins ara
     * @param node Node actual del trie del diccionari
     * @param anchor Punt d'ancoratge actual
     * @param pos Posició actual a la línia
     * @param usedLeft Nombre de lletres usades a l'esquerra
     * @param linea Línia de caselles
     * @param jugades Llista on afegir jugades vàlides
     * @param transposed True si la direcció és transposada
     * @param rack Fitxes disponibles al rack
     */
    private void extendRight(String soFar, Node node, Anchor anchor, int pos, int usedLeft, List<Casella> linea, List<Play> jugades, boolean transposed, List<Fitxa> rack) {
        if (pos >= linea.size()) return;

        Casella c = linea.get(pos);
        int fila = transposed ? pos : anchor.getFila();
        int col = transposed ? anchor.getColumna() : pos;

        if (!c.teFitxa()) {
            int placedRight = soFar.length() - usedLeft;
            if (placedRight > 0 && diccionari.validarParaula(soFar)) {
                int filaInici = transposed ? anchor.getFila() - usedLeft : anchor.getFila();
                int columnaInici = transposed ? anchor.getColumna() : anchor.getColumna() - usedLeft;
                int score = getPuntuacioJugada(soFar, filaInici, columnaInici, transposed);
                jugades.add(new Play(soFar, filaInici, columnaInici, transposed, score));
            }

            int cc;
            if (fila == anchor.getFila() && col == anchor.getColumna()) {
                cc = anchor.getCrossCheck();
            }
            else {
                cc = transposed ? crossChecksDown[fila][col] : crossChecksAcross[fila][col];
            }

            for (int i = 0; i < rack.size(); i++) {
                Fitxa f = rack.get(i);
                if (!f.esComodin()) {
                    char L = f.getLletra();
                    if (((cc >> (L - 'A')) & 1) == 1 && node.getFill(L) != null) {
                        List<Fitxa> novaRack = new ArrayList<>(rack);
                        novaRack.remove(i);
                        extendRight(soFar + L, node.getFill(L), anchor, pos + 1, usedLeft, linea, jugades, transposed, novaRack);
                    }
                } else {
                    for (char L = 'A'; L <= 'Z'; L++) {
                        if (((cc >> (L - 'A')) & 1) == 1 && node.getFill(L) != null) {
                            f.setLletra(L);
                            List<Fitxa> novaRack = new ArrayList<>(rack);
                            novaRack.remove(i);
                            extendRight(soFar + L, node.getFill(L), anchor, pos + 1, usedLeft, linea, jugades, transposed, novaRack);
                            f.setLletra('#');
                        }
                    }
                }
            }
        } else {
            char L = c.getFitxa().getLletra();
            if (node.getFill(L) != null) {
                extendRight(soFar + L, node.getFill(L), anchor, pos + 1, usedLeft, linea, jugades, transposed, rack);
            }
        }
    }

    /**
     * Calcula la puntuació total d'una jugada específica.
     * Inclou multiplicadors de caselles i puntuació de paraules creuades.
     * 
     * @param paraula Paraula a col·locar
     * @param fila Fila inicial
     * @param columna Columna inicial
     * @param transposed True si la paraula és vertical
     * @return Puntuació total de la jugada
     */
    private int getPuntuacioJugada(String paraula, int fila, int columna, boolean transposed) {
        int puntuacio = 0;
        int puntuacioParaulaCreuada = 0;
        int multiplicadorParaula = 1;
        Map<String, Pair<Integer, Integer>> alfabet = diccionari.getAlfabet();

        for (int i = 0; i < paraula.length(); i++) {
            char lletra = paraula.charAt(i);
            int f = transposed ? fila + i : fila;
            int c = transposed ? columna : columna + i;
            Casella casella = tauler.getCasella(f, c);
            if (casella == null) continue;

            int valorLletra = alfabet.get(Character.toString(lletra)).second;

            if (!casella.teFitxa()) {
                String mult = casella.getMultiplicador();
                switch (mult) {
                    case "DL" -> valorLletra *= 2;
                    case "TL" -> valorLletra *= 3;
                    case "DP" -> multiplicadorParaula *= 2;
                    case "TP" -> multiplicadorParaula *= 3;
                    case "C" -> multiplicadorParaula *= 2;
                }

                String paraulaCreuada = construirParaulaCreuada(f, c, !transposed, lletra);
                if (paraulaCreuada.length() > 1 && diccionari.validarParaula(paraulaCreuada)) {
                    puntuacioParaulaCreuada += calcularPuntuacioPalabra(paraulaCreuada, f, c, transposed);
                }
            }

            puntuacio += valorLletra;
        }

        puntuacio *= multiplicadorParaula;

        return puntuacio + puntuacioParaulaCreuada;
    }

    /**
     * Calcula la puntuació d'una paraula específica considerant multiplicadors.
     * 
     * @param paraula Paraula de la qual calcular la puntuació
     * @param fila Fila inicial de la paraula
     * @param columna Columna inicial de la paraula
     * @param transposed True si la paraula és vertical
     * @return Puntuació calculada de la paraula
     */
    private int calcularPuntuacioPalabra(String paraula, int fila, int columna, boolean transposed) {
        int puntuacio = 0;
        int multiplicadorParaula = 1;
        Map<String, Pair<Integer, Integer>> alfabet = diccionari.getAlfabet();

        for (int i = 0; i < paraula.length(); i++) {
            char lletra = paraula.charAt(i);
            int f = transposed ? fila + i : fila;
            int c = transposed ? columna : columna + i;
            Casella casella = tauler.getCasella(f, c);
            int valorLletra = alfabet.get(Character.toString(lletra)).second;

            if (!casella.teFitxa()) {
                String mult = casella.getMultiplicador();
                switch (mult) {
                    case "DL" -> valorLletra *= 2;
                    case "TL" -> valorLletra *= 3;
                    case "DP" -> multiplicadorParaula *= 2;
                    case "TP" -> multiplicadorParaula *= 3;
                }
            }

            puntuacio += valorLletra;
        }

        return puntuacio * multiplicadorParaula;
    }

    /**
     * Construeix la paraula creuada que passaria per (fila, columna)
     * si col·loquem 'letra' en aquesta casella, donat que la paraula
     * principal és horitzontal o vertical.
     *
     * @param fila Fila de la casella nova
     * @param columna Columna de la casella nova
     * @param horizontal True si la paraula principal és horitzontal
     * @param letra La lletra que estem provant en aquesta casella
     * @return La paraula perpendicular formada (incloent 'letra')
     */
    private String construirParaulaCreuada(int fila, int columna, boolean horizontal, char letra) {
        int n = tauler.getMida();
        int f = fila, c = columna;
        int dfBack = horizontal ? -1 : 0;
        int dcBack = horizontal ? 0 : -1;

        // Retrocede hasta el inicio de la palabra cruzada
        while (f + dfBack >= 0 && c + dcBack >= 0 && tauler.getCasella(f + dfBack, c + dcBack).teFitxa()) {
            f += dfBack;
            c += dcBack;
        }

        StringBuilder sb = new StringBuilder();
        int stepF = horizontal ? 1 : 0;
        int stepC = horizontal ? 0 : 1;
        boolean hayVecino = false;

        int ff = f, cc = c;
        while (ff < n && cc < n && tauler.getCasella(ff, cc).teFitxa()) {
            if (ff != fila || cc != columna) hayVecino = true;
            sb.append(tauler.getCasella(ff, cc).getFitxa().getLletra());
            ff += stepF;
            cc += stepC;
        }

        // Inserta la letra nueva en su sitio si la casilla está vacía
        int posNueva = (horizontal ? fila : columna) - (horizontal ? f : c);
        if (!tauler.getCasella(fila, columna).teFitxa()) {
            sb.insert(posNueva, letra);
            hayVecino = hayVecino || sb.length() > 1;
        }

        // Si solo hay una letra y no tiene vecino, no hay palabra cruzada real
        return hayVecino ? sb.toString() : "";
    }

    /**
     * Converteix una cadena de text en una llista de fitxes del rack del jugador.
     * Assigna fitxes del rack per formar la paraula, incloent comodins si cal.
     * 
     * @param paraula Paraula a formar
     * @param fila Fila inicial de col·locació
     * @param columna Columna inicial de col·locació
     * @param horitzontal True si la col·locació és horitzontal
     * @return Llista de fitxes necessàries per formar la paraula
     */
    private List<Fitxa> stringToFitxa(String paraula, int fila, int columna, boolean horitzontal) {
        List<Fitxa> fichasColocar = new ArrayList<>();
        List<Fitxa> atrilDisponible = new ArrayList<>(jugador.getFitxes());

        for (int i = 0; i < paraula.length(); i++) {
            int f = horitzontal ? fila : fila + i;
            int c = horitzontal ? columna + i : columna;
            Casella casella = tauler.getCasella(f, c);
            char lletra = paraula.charAt(i);

            if (casella.teFitxa()) continue;

            Fitxa enAtril = null;
            for (Fitxa ftx : atrilDisponible) {
                if (!ftx.esComodin() && ftx.getLletra() == lletra) {
                    enAtril = ftx;
                    break;
                }
            }

            if (enAtril == null) {
                for (Fitxa ftx : atrilDisponible) {
                    if (ftx.esComodin()) {
                        ftx.setLletra(lletra);
                        enAtril = ftx;
                        break;
                    }
                }
            }

            if (enAtril != null) {
                fichasColocar.add(enAtril);
                atrilDisponible.remove(enAtril);
            } else {
                return new ArrayList<>();
            }
        }

        return fichasColocar;
    }
}
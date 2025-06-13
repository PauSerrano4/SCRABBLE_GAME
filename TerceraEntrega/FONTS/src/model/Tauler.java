package model;

import util.Pair;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.Serializable;

/**
 * Classe Tauler - Representa el tauler de joc d'Scrabble.
 * Gestiona les caselles, la mida i les operacions principals sobre el tauler.
 * @author Alexander de Jong
 */
public class Tauler implements Serializable {

    // ---------- ATRIBUTS ----------
    /** Matriu de caselles que formen el tauler */
    private Casella[][] caselles;
    /** Mida del tauler (mida x mida) */
    private int mida;
    /** Indica si s'ha col·locat la primera paraula al tauler */
    private boolean primeraParaulaColocada = false; // Indica si s'ha col·locat la primera paraula

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe Tauler.
     * @param mida Mida del tauler (mida x mida)
     */
    public Tauler(int mida) {
        this.mida = mida;
        this.caselles = new Casella[mida][mida];
        inicialitzarTauler();
    }

    // ---------- GETTERS ----------
    /**
     * Obté una casella específica del tauler.
     * @param fila Número de fila
     * @param columna Número de columna
     * @return La casella en aquesta posició o null si les coordenades són invàlides
     */
    public Casella getCasella(int fila, int columna) {
        if (fila >= 0 && fila < mida && columna >= 0 && columna < mida) {
            return caselles[fila][columna];
        }
        return null;
    }

    /**
     * Obté la mida del tauler.
     * @return La mida del tauler
     */
    public int getMida() {
        return mida;
    }

    // ---------- SETTERS ----------
    /**
     * Substitueix una casella del tauler per una de nova.
     * @param fila Fila de la casella a substituir
     * @param columna Columna de la casella a substituir
     * @param novaCasella Nova casella a col·locar
     */
    public void substituirCasella(int fila, int columna, Casella novaCasella) {
        this.caselles[fila][columna] = novaCasella;
    }

    // ---------- MÈTODES PÚBLICS ----------
    /**
     * Col·loca una paraula al tauler i calcula la seva puntuació.
     * @param fitxesNoves Llista de fitxes a col·locar
     * @param paraula Paraula a col·locar
     * @param fila0 Fila inicial
     * @param col0 Columna inicial
     * @param horitzontal true si la paraula és horitzontal, false si és vertical
     * @return Un Pair que conté la llista de posicions on s'han col·locat les fitxes i la puntuació total.
     */
    public Pair<List<Pair<Integer,Integer>>, Integer> colocarParaula(List<Fitxa> fitxesNoves, String paraula, int fila0, int col0, boolean horitzontal) {
        List<Pair<Integer,Integer>> posNoves = new ArrayList<>();
        List<FitxaColocada> tmp = new ArrayList<>();
        int idxNova = 0;

        int puntsPrincipal = 0;
        int multParaula = 1;
        boolean tocaAntiga = false;

        for (int k = 0; k < paraula.length(); k++) {
            int f = horitzontal ? fila0 : fila0 + k;
            int c = horitzontal ? col0 + k : col0;
            Casella cas = getCasella(f, c);
            if (cas == null) { revertirTmp(tmp); return buit(); }

            char lletra = paraula.charAt(k);

            if (cas.teFitxa()) {
                if (cas.getFitxa().getLletra() != lletra) {
                    revertirTmp(tmp); return buit();
                }
                puntsPrincipal += cas.getFitxa().getValor();
                tocaAntiga = true;
            } else {
                if (idxNova >= fitxesNoves.size()) {
                    revertirTmp(tmp);
                    return buit();
                }
                Fitxa ftx = fitxesNoves.get(idxNova++);
                cas.colocarFitxa(ftx);
                tmp.add(new FitxaColocada(cas, ftx));
                posNoves.add(new Pair<>(f, c));

                if (!tocaAntiga) {
                    if ((f > 0 && getCasella(f - 1, c).teFitxa()) ||
                        (f < mida - 1 && getCasella(f + 1, c).teFitxa()) ||
                        (c > 0 && getCasella(f, c - 1).teFitxa()) ||
                        (c < mida - 1 && getCasella(f, c + 1).teFitxa()))
                        tocaAntiga = true;
                }

                int val = ftx.getValor();
                switch (cas.getMultiplicador()) {
                    case "DL": val *= 2; break;
                    case "TL": val *= 3; break;
                    case "DP": multParaula *= 2; break;
                    case "TP": multParaula *= 3; break;
                    case "C":
                        if (!primeraParaulaColocada && f == 7 && c == 7) multParaula *= 2;
                }
                puntsPrincipal += val;
            }
        }

        if (primeraParaulaColocada && !tocaAntiga) {
            revertirTmp(tmp); return buit();
        }

        int puntsCruzadas = 0;
        for (Pair<Integer,Integer> pos : posNoves) {
            String creu = construirCreuada(pos.first, pos.second, horitzontal);

            if (creu.length() <= 1) continue;

            int fStart = pos.first, cStart = pos.second;
            if (horitzontal) {
                while (fStart - 1 >= 0 && getCasella(fStart - 1, cStart).teFitxa())
                    fStart--;
            } else {
                while (cStart - 1 >= 0 && getCasella(fStart, cStart - 1).teFitxa())
                    cStart--;
            }

            int multPal = 1, punts = 0;
            for (int i = 0; i < creu.length(); i++) {
                int f = horitzontal ? fStart + i : pos.first;
                int c = horitzontal ? pos.second : cStart + i;
                Casella cas = getCasella(f, c);
                int val = cas.getFitxa().getValor();
                if (f == pos.first && c == pos.second) {
                    switch (cas.getMultiplicador()) {
                        case "DL": val *= 2; break;
                        case "TL": val *= 3; break;
                        case "DP": multPal *= 2; break;
                        case "TP": multPal *= 3; break;
                    }
                }
                punts += val;
            }
            puntsCruzadas += punts * multPal;
        }

        int total = puntsPrincipal * multParaula + puntsCruzadas;

        if (!primeraParaulaColocada && !posNoves.isEmpty())
            primeraParaulaColocada = true;

        return new Pair<>(posNoves, total);
    }

    /**
     * Confirma la jugada i crema els multiplicadors de les caselles utilitzades.
     * @param posFix Llista de posicions de les fitxes col·locades.
     */
    public void confirmar(List<Pair<Integer,Integer>> posFix) {
        for (Pair<Integer,Integer> p : posFix) {
            Casella cas = getCasella(p.first, p.second);
            cas.cremarMultiplicador();
        }
    }

    /**
     * Retorna la paraula principal formada a partir d'una posició i direcció.
     * @param f0 Fila inicial
     * @param c0 Columna inicial
     * @param hor true si horitzontal, false si vertical
     * @param coords Coordenades de les fitxes noves
     * @param noves Llista de fitxes noves
     * @return Paraula formada
     */
    public String construirParaula(int f0, int c0, boolean hor, List<Pair<Integer,Integer>> coords, List<Fitxa> noves) {
        while (f0 - (hor ? 0 : 1) >= 0 && c0 - (hor ? 1 : 0) >= 0 &&
            getCasella(f0 - (hor ? 0 : 1), c0 - (hor ? 1 : 0)).teFitxa()) {
            f0 -= hor ? 0 : 1; c0 -= hor ? 1 : 0;
        }
        StringBuilder sb = new StringBuilder();
        int idxNova = 0;
        while (f0 < mida && c0 < mida &&
            (getCasella(f0, c0).teFitxa() || idxNova < noves.size())) {
            if (getCasella(f0, c0).teFitxa())
                sb.append(getCasella(f0, c0).getFitxa().getLletra());
            else
                sb.append(noves.get(idxNova++).getLletra());
            f0 += hor ? 0 : 1; c0 += hor ? 1 : 0;
        }
        return sb.toString();
    }

    /**
     * Retorna la llista de paraules formades en una jugada (principal i creuades).
     * @param posFix Llista de posicions de les fitxes col·locades
     * @param jugadaHoriz true si la jugada és horitzontal
     * @return Llista de paraules formades
     */
    public List<String> getParaulesFormades(List<Pair<Integer,Integer>> posFix, boolean jugadaHoriz) {
        List<String> res = new ArrayList<>();
        res.add(construirParaula(
            posFix.get(0).first, posFix.get(0).second,
            jugadaHoriz, posFix, Collections.emptyList()));

        for (Pair<Integer,Integer> p : posFix) {
            String cruz = construirCreuada(p.first, p.second, jugadaHoriz);
            if (!cruz.isEmpty()) res.add(cruz);
        }
        return res;
    }

    /**
     * Mostra l'estat actual del tauler.
     * @return Representació visual del tauler
     */
    public String mostrarTauler() {
        StringBuilder sb = new StringBuilder();

        sb.append("   ");
        for (int j = 0; j < mida; j++) {
            sb.append(String.format("%2d ", j));
        }
        sb.append("\n");

        sb.append("   ");
        for (int j = 0; j < mida; j++) {
            sb.append("---");
        }
        sb.append("\n");

        for (int i = 0; i < mida; i++) {
            sb.append(String.format("%2d|", i));
            for (int j = 0; j < mida; j++) {
                Casella casella = getCasella(i, j);
                if (casella.teFitxa()) {
                    sb.append(" ").append(casella.getFitxa().getLletra()).append(" ");
                } else if (!casella.getMultiplicador().isEmpty()) {
                    sb.append(casella.getMultiplicador()).append(" ");
                } else {
                    sb.append("   ");
                }
            }
            sb.append("|\n");
        }

        sb.append("   ");
        for (int j = 0; j < mida; j++) {
            sb.append("---");
        }
        sb.append("\n");

        return sb.toString();
    }

    /**
     * Comprova si el tauler està buit.
     * @return true si no hi ha fitxes col·locades, false altrament
     */
    public boolean estaBuit() {
        for (int i = 0; i < mida; i++)
            for (int j = 0; j < mida; j++)
                if (caselles[i][j].teFitxa()) return false;
        return true;
    }

    /**
     * Comprova si una posició té una casella veïna ocupada.
     * @param fila Fila a comprovar
     * @param col Columna a comprovar
     * @return true si hi ha una casella veïna ocupada, false altrament
     */
    public boolean teVeinaOcupada(int fila, int col) {
        int[][] d = { {-1,0},{1,0},{0,-1},{0,1} };
        for (int[] v : d) {
            int f = fila + v[0], c = col + v[1];
            if (f >= 0 && f < mida && c >= 0 && c < mida && caselles[f][c].teFitxa())
                return true;
        }
        return false;
    }

    /**
     * Confirma si les posicions donades són contigües en la direcció indicada.
     * @param pos Llista de posicions
     * @param hor true si horitzontal, false si vertical
     * @return true si són contigües, false altrament
     */
    public boolean sonContigues(List<Pair<Integer,Integer>> pos, boolean hor) {
        for (int k = 1; k < pos.size(); k++) {
            int diff = hor ? pos.get(k).second - pos.get(k-1).second
                        : pos.get(k).first  - pos.get(k-1).first;
            if (diff == 1) continue;
            int fGap = hor ? pos.get(k-1).first  : pos.get(k-1).first  + 1;
            int cGap = hor ? pos.get(k-1).second + 1 : pos.get(k-1).second;
            if (!getCasella(fGap, cGap).teFitxa()) return false;
        }
        return true;
    }

    /**
     * Reemplaça les fitxes noves col·locades al tauler (desfà la jugada).
     * @param posFix Llista de posicions de les fitxes a treure
     */
    public void revertir2(List<Pair<Integer,Integer>> posFix) {
        for (Pair<Integer,Integer> p : posFix) {
            Casella cas = getCasella(p.first, p.second);
            if (cas != null && cas.teFitxa()) cas.treureFitxa();
        }
    }

    // ---------- MÈTODES PRIVATS ----------
    /**
     * Inicialitza el tauler amb les caselles i multiplicadors corresponents.
     */
    private void inicialitzarTauler() {
        for (int i = 0; i < mida; i++) {
            for (int j = 0; j < mida; j++) {
                String multiplicador = determinarMultiplicador(i, j);
                caselles[i][j] = new Casella(i, j, multiplicador);
            }
        }
    }

    /**
     * Determina el multiplicador d'una casella segons la seva posició.
     * @param fila Número de fila
     * @param columna Número de columna
     * @return El tipus de multiplicador per a aquesta casella
     */
    private String determinarMultiplicador(int fila, int columna) {
        int f = Math.min(fila, mida - 1 - fila);
        int c = Math.min(columna, mida - 1 - columna);

        if (mida == 15) {
            if (fila == 7 && columna == 7) {
                return "C";
            }
            if ((f == 0 && c == 0) || (f == 0 && c == 7) || (f == 7 && c == 0)) {
                return "TP";
            }
            if ((f == 1 && c == 1) || (f == 2 && c == 2) || (f == 3 && c == 3) || (f == 4 && c == 4)) {
                return "DP";
            }
            if ((f == 1 && c == 5) || (f == 5 && c == 1) || (f == 5 && c == 5)) {
                return "TL";
            }
            if ((f == 0 && c == 3) || (f == 2 && c == 6) || (f == 3 && c == 0) ||
                (f == 3 && c == 7) || (f == 6 && c == 2) || (f == 6 && c == 6) || (f == 7 && c == 3)) {
                return "DL";
            }
        }
        return "";
    }

    /**
     * Retorna la paraula perpendicular que passa per (fila,col).
     * Si no hi ha lletres veïnes, retorna "".
     * @param fila Fila de la casella
     * @param col Columna de la casella
     * @param jugadaHoriz true si la jugada principal és horitzontal
     * @return Paraula creuada formada o "" si no hi ha creuada real
     */
    private String construirCreuada(int fila, int col, boolean jugadaHoriz) {
        int f = fila, c = col;
        int dfBack = jugadaHoriz ? -1 : 0;
        int dcBack = jugadaHoriz ? 0  : -1;
        while (f + dfBack >= 0 &&
            c + dcBack >= 0 &&
            getCasella(f + dfBack, c + dcBack).teFitxa()) {
            f += dfBack;
            c += dcBack;
        }

        StringBuilder sb = new StringBuilder();
        int stepF = jugadaHoriz ? 1 : 0;
        int stepC = jugadaHoriz ? 0 : 1;
        boolean hiHaVeina = false;

        while (f < mida && c < mida && getCasella(f, c).teFitxa()) {
            if (f != fila || c != col) hiHaVeina = true;
            sb.append(getCasella(f, c).getFitxa().getLletra());
            f += stepF;
            c += stepC;
        }
        return hiHaVeina ? sb.toString() : "";
    }

    /**
     * Verifica si la paraula col·locada utilitza la casella central.
     * @param posicions Llista de posicions de les fitxes col·locades (Pair<Integer, Integer>).
     * @return true si alguna fitxa està a la casella central, false altrament.
     */
    private boolean haJugatAlCentre(List<Pair<Integer, Integer>> posicions) {
        int filaCentre = mida / 2;
        int columnaCentre = mida / 2;
        for (Pair<Integer, Integer> pos : posicions) {
            if (pos.first == filaCentre && pos.second == columnaCentre) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reverteix les fitxes col·locades temporalment en cas d'error.
     * @param t Llista de fitxes col·locades temporalment
     */
    private void revertirTmp(List<FitxaColocada> t) {
        for (FitxaColocada fc : t) fc.casella.treureFitxa();
    }

    /**
     * Retorna un Pair buit per indicar error en la jugada.
     * @return Pair buit (llista buida, puntuació 0)
     */
    private Pair<List<Pair<Integer,Integer>>,Integer> buit() {
        return new Pair<>(Collections.emptyList(), 0);
    }

    // ---------- CLASSES INTERNES ----------
    /**
     * Classe interna per representar una fitxa col·locada en una casella,
     * útil per revertir la col·locació en cas de fallada.
     */
    private static class FitxaColocada {
        Casella casella;
        Fitxa fitxa;

        public FitxaColocada(Casella casella, Fitxa fitxa) {
            this.casella = casella;
            this.fitxa = fitxa;
        }
    }
}
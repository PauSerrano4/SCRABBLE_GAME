package controller;

import java.util.*;
import model.*;
import util.*;

public class ControladorMaquina {
    public Partida partida;
    private Jugador jugador;
    private Diccionari diccionari;
    private Tauler tauler;
    private int[][] crossChecksAcross;
    private int[][] crossChecksDown;

    public ControladorMaquina(Partida partida) {
        this.partida = partida;
        this.diccionari = partida.getDiccionari();
        this.tauler = partida.getTauler();
    }

    public void jugarTorn(String nomJugador) {
        this.jugador = partida.getJugador(nomJugador);
        generateMove();
    }

    private void generateMove() {
        int mida = tauler.getMida();
        List<Play> totesJugades = new ArrayList<>();

        if (tauler.getCasella(7, 7) != null && !tauler.getCasella(7, 7).teFitxa()) {
            crossChecksAcross = new int[mida][mida];
            crossChecksDown = new int[mida][mida];
            int allLettersMask = (1 << 26) - 1;
            Anchor primerAnchor = new Anchor(7, 7, allLettersMask, 7);

            List<Casella> linea = new ArrayList<>();
            for (int i = 0; i < mida; i++) {
                linea.add(tauler.getCasella(7, i));
            }

            List<Fitxa> rack = new ArrayList<>(jugador.getFitxes());
            leftPart("", diccionari.getArrel(), primerAnchor, linea, totesJugades, primerAnchor.getMaxLeft() + 1, -1, false, rack);
        } else {
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

            List<Fitxa> fichasColocar = stringToFitxa(paraula, filaInicial, columnaInicial, horitzontal);
            Pair<List<Pair<Integer, Integer>>, Integer> resultat = tauler.colocarParaula(fichasColocar, filaInicial, columnaInicial, horitzontal);
            if (!resultat.first.isEmpty()) {
                for (Fitxa f : fichasColocar) {
                    jugador.colocarFitxa(f);
                }
                jugador.actualitzarPuntuacio(resultat.second);
            }
        }
    }

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
            leftPart("", diccionari.getArrel(), a, linea, jugades, a.getMaxLeft() + 1, -1, transposed, atril);
        }

        return jugades;
    }

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
                if ((fila > 0 && tauler.getCasella(fila - 1, columna).teFitxa()) ||
                    (fila < n - 1 && tauler.getCasella(fila + 1, columna).teFitxa())) {
                    esAnchor = true;
                }

                if (esAnchor) {
                    int maxLeft = 0;
                    int j = i - 1;
                    int crossCheck = transposed ? crossChecksDown[fila][columna] : crossChecksAcross[fila][columna];

                    while (j > 0 && !linea.get(j).teFitxa()) {
                        j--;
                        maxLeft++;
                    }
                    anchors.add(new Anchor(fila, columna, crossCheck, maxLeft));
                }
            }
        }

        return anchors;
    }

    private void computeCrossChecks() {
        int n = tauler.getMida();
        crossChecksAcross = new int[n][n];
        crossChecksDown = new int[n][n];

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

    private int calcularCrossCheck(int fila, int columna, boolean transposed) {
        String prefix = "", sufix = "";
        int n = tauler.getMida();

        if (transposed) {
            for (int c = columna - 1; c >= 0 && tauler.getCasella(fila, c).teFitxa(); c--) {
                prefix = tauler.getCasella(fila, c).getFitxa().getLletra() + prefix;
            }
            for (int c = columna + 1; c < n && tauler.getCasella(fila, c).teFitxa(); c++) {
                sufix = sufix + tauler.getCasella(fila, c).getFitxa().getLletra();
            }
        } else {
            for (int r = fila - 1; r >= 0 && tauler.getCasella(r, columna).teFitxa(); r--) {
                prefix = tauler.getCasella(r, columna).getFitxa().getLletra() + prefix;
            }
            for (int r = fila + 1; r < n && tauler.getCasella(r, columna).teFitxa(); r++) {
                sufix = sufix + tauler.getCasella(r, columna).getFitxa().getLletra();
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

    private void leftPart(String soFar, Node node, Anchor anchor, List<Casella> linea, List<Play> jugades, int limit, int usedLeft, boolean transposed, List<Fitxa> rack) {
        int pos = transposed ? anchor.getFila() : anchor.getColumna();
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

                    int anchorPos = transposed ? anchor.getFila() : anchor.getColumna();
                    int start = pos - newUsedLeft;
                    if (anchorPos >= start && anchorPos <= pos) {
                        extendRight(nouSoFar, child, anchor, pos, newUsedLeft, linea, jugades, transposed, novaRack);
                    }

                    leftPart(nouSoFar, child, anchor, linea, jugades, newLimit, newUsedLeft, transposed, novaRack);
                }
            }
        }
    }

    private void extendRight(String soFar, Node node, Anchor anchor, int pos, int usedLeft, List<Casella> linea, List<Play> jugades, boolean transposed, List<Fitxa> rack) {
        if (pos >= linea.size()) return;

        Casella c = linea.get(pos);
        int fila = transposed ? anchor.getFila() : pos;
        int col = transposed ? anchor.getColumna() : pos;

        if (!c.teFitxa()) {
            if (diccionari.validarParaula(soFar) && !soFar.isEmpty()) {
                int filaInici = transposed ? anchor.getFila() : anchor.getFila() - usedLeft;
                int columnaInici = transposed ? anchor.getColumna() - usedLeft : anchor.getColumna();
                int score = getPuntuacioJugada(soFar, filaInici, columnaInici, transposed);
                jugades.add(new Play(soFar, filaInici, columnaInici, transposed, score));
            }

            int cc = transposed ? crossChecksDown[fila][col] : crossChecksAcross[fila][col];
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

    private int getPuntuacioJugada(String paraula, int fila, int columna, boolean transposed) {
        int puntuacio = 0;
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
                }

                String paraulaCreuada = construirParaulaCreuada(f, c, transposed, lletra);
                if (paraulaCreuada.length() > 1 && diccionari.validarParaula(paraulaCreuada)) {
                    puntuacio += calcularPuntuacioPalabra(paraulaCreuada, f, c, transposed);
                }
            }

            puntuacio += valorLletra;
        }

        puntuacio *= multiplicadorParaula;

        if (jugador.getFitxes() == null) puntuacio += 50;

        return puntuacio;
    }

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

    private String construirParaulaCreuada(int fila, int columna, boolean transposed, char novaLletra) {
        StringBuilder prefix = new StringBuilder();
        StringBuilder sufix = new StringBuilder();

        int df = transposed ? 0 : -1;
        int dc = transposed ? -1 : 0;
        int f = fila + df;
        int c = columna + dc;

        while (f >= 0 && c >= 0 && f < tauler.getMida() && c < tauler.getMida() && tauler.getCasella(f, c).teFitxa()) {
            prefix.insert(0, tauler.getCasella(f, c).getFitxa().getLletra());
            f += df;
            c += dc;
        }

        df = transposed ? 0 : 1;
        dc = transposed ? 1 : 0;
        f = fila + df;
        c = columna + dc;

        while (f >= 0 && c >= 0 && f < tauler.getMida() && c < tauler.getMida() && tauler.getCasella(f, c).teFitxa()) {
            sufix.append(tauler.getCasella(f, c).getFitxa().getLletra());
            f += df;
            c += dc;
        }

        return prefix + Character.toString(novaLletra) + sufix;
    }

    private List<Fitxa> stringToFitxa(String paraula, int fila, int columna, boolean transposed) {
        List<Fitxa> fichasColocar = new ArrayList<>();
        List<Fitxa> atrilDisponible = new ArrayList<>(jugador.getFitxes());

        for (int i = 0; i < paraula.length(); i++) {
            int f = transposed ? fila + i : fila;
            int c = transposed ? columna : columna + i;
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

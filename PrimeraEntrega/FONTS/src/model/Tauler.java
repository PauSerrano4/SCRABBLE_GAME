package model;

import util.Pair;
import java.util.List;
import java.util.ArrayList;

/**
 * Clase Tauler - Representa el tablero de juego de Scrabble
 */
// autor : Alexander de Jong

public class Tauler {
    private Casella[][] caselles;
    private int mida;
    private boolean primeraParaulaColocada = false; // Nueva variable para rastrear la primera palabra

    /**
     * Constructor de la clase Tauler
     * @param mida Tamaño del tablero (mida x mida)
     */
    public Tauler(int mida) {
        this.mida = mida;
        this.caselles = new Casella[mida][mida];  // Inicialización de la matriz
        inicialitzarTauler();
    }

    /**
     * Inicializa el tablero con las casillas y multiplicadores correspondientes
     */
    private void inicialitzarTauler() {
        // Creamos todas las casillas
        for (int i = 0; i < mida; i++) {
            for (int j = 0; j < mida; j++) {
                String multiplicador = determinarMultiplicador(i, j);
                caselles[i][j] = new Casella(i, j, multiplicador);  // Asignación en la matriz
            }
        }
    }

    /**
     * Determina el multiplicador de una casilla según su posición
     * @param fila Número de fila
     * @param columna Número de columna
     * @return El tipo de multiplicador para esa casilla
     */
    private String determinarMultiplicador(int fila, int columna) {
        // Este método es específico para un tablero de Scrabble estándar
        // Se puede personalizar según las reglas específicas del juego

        // Tablero simétrico, normalizamos coordenadas
        int f = Math.min(fila, mida - 1 - fila);
        int c = Math.min(columna, mida - 1 - columna);

        // Para un tablero estándar de 15x15
        if (mida == 15) {
            // Casilla central
            if (fila == 7 && columna == 7) {
                return "C"; // Centro
            }

            // Palabra triple (TP)
            if ((f == 0 && c == 0) || (f == 0 && c == 7) || (f == 7 && c == 0)) {
                return "TP";
            }

            // Palabra doble (DP)
            if ((f == 1 && c == 1) || (f == 2 && c == 2) || (f == 3 && c == 3) || (f == 4 && c == 4)) {
                return "DP";
            }

            // Letra triple (TL)
            if ((f == 1 && c == 5) || (f == 5 && c == 1) || (f == 5 && c == 5)) {
                return "TL";
            }

            // Letra doble (DL)
            if ((f == 0 && c == 3) || (f == 2 && c == 6) || (f == 3 && c == 0) ||
                (f == 3 && c == 7) || (f == 6 && c == 2) || (f == 6 && c == 6) || (f == 7 && c == 3)) {
                return "DL";
            }
        }

        return "";
    }

    /**
     * Obtiene el tamaño del tablero
     * @return El tamaño del tablero
     */
    public int getMida() {
        return mida;
    }

    /**
     * Obtiene una casilla específica del tablero
     * @param fila Número de fila
     * @param columna Número de columna
     * @return La casilla en esa posición o null si las coordenadas son inválidas
     */
    public Casella getCasella(int fila, int columna) {
        if (fila >= 0 && fila < mida && columna >= 0 && columna < mida) {
            return caselles[fila][columna];  // Acceso directo en la matriz
        }
        return null;
    }

    /**
     * Coloca una palabra en el tablero y calcula su puntuación.
     * @param fitxes Lista de fichas a colocar
     * @param fila Fila inicial
     * @param columna Columna inicial
     * @param horitzontal true si la palabra es horizontal, false si es vertical
     * @return Un Pair que contiene la lista de posiciones (Pair<Integer, Integer>) donde se colocaron las fichas y la puntuación total.
     */
    public Pair<List<Pair<Integer, Integer>>, Integer> colocarParaula(List<Fitxa> fitxes, int fila, int columna, boolean horitzontal) {
        List<Pair<Integer, Integer>> posicions = new ArrayList<>();
        int puntuacioParaula = 0;
        int multiplicadorParaula = 1;
        boolean palabraValida = true;
        List<FitxaColocada> fitxesColocades = new ArrayList<>();

        // Verificar que hay espacio suficiente en el tablero
        if (horitzontal && columna + fitxes.size() > mida) {
            return new Pair<>(posicions, 0);
        }
        if (!horitzontal && fila + fitxes.size() > mida) {
            return new Pair<>(posicions, 0);
        }

        // Colocar las fichas y calcular la puntuación
        for (int i = 0; i < fitxes.size(); i++) {
            int f = horitzontal ? fila : fila + i;
            int c = horitzontal ? columna + i : columna;
            Casella casella = getCasella(f, c);

            if (casella != null && !casella.teFitxa()) {
                Fitxa fitxa = fitxes.get(i);
                if (casella.colocarFitxa(fitxa)) {
                    posicions.add(new Pair<>(f, c)); // Guardar la posición como un Pair
                    fitxesColocades.add(new FitxaColocada(casella, fitxa));
                } else {
                    palabraValida = false;
                    break;
                }
            } else {
                palabraValida = false;
                break;
            }
        }

        // Si no se colocaron todas las fichas, volver atrás
        if (!palabraValida) {
            for (FitxaColocada fc : fitxesColocades) {
                fc.casella.treureFitxa();
            }
            posicions.clear();
            return new Pair<>(posicions, 0);
        }

        // Calcular la puntuación de la palabra
        for (FitxaColocada fc : fitxesColocades) {
            int valorFitxa = fc.fitxa.getValor();
            String multiplicadorCasella = fc.casella.getMultiplicador();

            switch (multiplicadorCasella) {
                case "DL":
                    valorFitxa *= 2;
                    break;
                case "TL":
                    valorFitxa *= 3;
                    break;
                case "DP":
                    multiplicadorParaula *= 2;
                    break;
                case "TP":
                    multiplicadorParaula *= 3;
                    break;
                case "C": // Casilla Central - cuenta como palabra doble en la primera jugada
                    if (!primeraParaulaColocada && haJugatAlCentre(posicions)) {
                        multiplicadorParaula *= 2;
                    }
                    break;
            }
            puntuacioParaula += valorFitxa;
        }

        puntuacioParaula *= multiplicadorParaula;

        // Si la colocación fue exitosa y aún no se había colocado la primera palabra, marcarlo
        if (!primeraParaulaColocada && !posicions.isEmpty()) {
            primeraParaulaColocada = true;
        }

        return new Pair<>(posicions, puntuacioParaula);
    }

    /**
     * Verifica si la palabra colocada utiliza la casilla central.
     * @param posicions Lista de posiciones de las fichas colocadas (Pair<Integer, Integer>).
     * @return true si alguna ficha está en la casilla central, false en caso contrario.
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
     * Muestra el estado actual del tablero
     * @return Representación visual del tablero
     */
    public String mostrarTauler() {
        StringBuilder sb = new StringBuilder();

        // Encabezado de columnas
        sb.append("   ");
        for (int j = 0; j < mida; j++) {
            sb.append(String.format("%2d ", j));
        }
        sb.append("\n");

        // Línea divisoria
        sb.append("   ");
        for (int j = 0; j < mida; j++) {
            sb.append("---");
        }
        sb.append("\n");

        // Contenido del tablero
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

        // Línea divisoria
        sb.append("   ");
        for (int j = 0; j < mida; j++) {
            sb.append("---");
        }
        sb.append("\n");

        return sb.toString();
    }

    /**
     * Clase interna para representar una ficha colocada en una casilla,
     * útil para revertir la colocación en caso de fallo.
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
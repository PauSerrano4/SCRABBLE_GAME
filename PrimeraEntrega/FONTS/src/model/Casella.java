package model;

/**
 * Clase Casella - Representa una casilla del tablero de Scrabble
 */
//autor: alexander de jong 
public class Casella {
    private int fila;
    private int columna;
    private String multiplicador; // Puede ser "DL", "TL", "DP", "TP" o vacío
    private Fitxa fitxa;
    
    /**
     * Constructor de la clase Casella
     * @param fila Número de fila en el tablero
     * @param columna Número de columna en el tablero
     * @param multiplicador Tipo de multiplicador ("DL", "TL", "DP", "TP" o vacío)
     */
    public Casella(int fila, int columna, String multiplicador) {
        this.fila = fila;
        this.columna = columna;
        this.multiplicador = multiplicador;
        this.fitxa = null;
    }
    
    /**
     * Obtiene el número de fila
     * @return El número de fila
     */
    public int getFila() {
        return fila;
    }
    
    /**
     * Obtiene el número de columna
     * @return El número de columna
     */
    public int getColumna() {
        return columna;
    }
    
    /**
     * Obtiene el tipo de multiplicador
     * @return El tipo de multiplicador ("DL", "TL", "DP", "TP" o vacío)
     */
    public String getMultiplicador() {
        return multiplicador;
    }
    
    /**
     * Coloca una ficha en la casilla
     * @param fitxa La ficha a colocar
     * @return true si se pudo colocar, false si ya había una ficha
     */
    public boolean colocarFitxa(Fitxa fitxa) {
        if (this.fitxa == null) {
            this.fitxa = fitxa;
            return true;
        }
        return false;
    }
    
    /**
     * Verifica si la casilla tiene una ficha
     * @return true si hay una ficha, false en caso contrario
     */
    public boolean teFitxa() {
        return fitxa != null;
    }
    
    /**
     * Obtiene la ficha de la casilla
     * @return La ficha en la casilla o null si no hay ninguna
     */
    public Fitxa getFitxa() {
        return fitxa;
    }
    
    /**
     * Quita la ficha de la casilla
     * @return La ficha quitada o null si no había ninguna
     */
    public Fitxa treureFitxa() {
        Fitxa result = fitxa;
        fitxa = null;
        return result;
    }
    
}


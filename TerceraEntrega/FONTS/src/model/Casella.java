package model;

import java.io.Serializable;

/**
 * Classe Casella - Representa una casella del tauler d'Scrabble.
 * Gestiona la posició, el tipus de multiplicador i la fitxa col·locada a la casella.
 * @author alexander.de.jong
 */
public class Casella implements Serializable {

    // ---------- ATRIBUTS ----------
    /** Número de fila de la casella al tauler (0-14) */
    private int fila;
    /** Número de columna de la casella al tauler (0-14) */
    private int columna;
    /** Tipus de multiplicador de la casella: "DL" (doble lletra), "TL" (triple lletra), "DP" (doble paraula), "TP" (triple paraula) o buit */
    private String multiplicador;
    /** Fitxa col·locada a la casella, null si la casella està buida */
    private Fitxa fitxa;

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe Casella.
     * @param fila Número de fila al tauler.
     * @param columna Número de columna al tauler.
     * @param multiplicador Tipus de multiplicador ("DL", "TL", "DP", "TP" o buit).
     */
    public Casella(int fila, int columna, String multiplicador) {
        this.fila = fila;
        this.columna = columna;
        this.multiplicador = multiplicador;
        this.fitxa = null;
    }

    // ---------- GETTERS ----------
    /**
     * Retorna el número de columna.
     * @return El número de columna.
     */
    public int getColumna() {
        return columna;
    }

    /**
     * Retorna el número de fila.
     * @return El número de fila.
     */
    public int getFila() {
        return fila;
    }

    /**
     * Retorna la fitxa de la casella.
     * @return La fitxa a la casella o null si no n'hi ha cap.
     */
    public Fitxa getFitxa() {
        return fitxa;
    }

    /**
     * Retorna el tipus de multiplicador.
     * @return El tipus de multiplicador ("DL", "TL", "DP", "TP" o buit).
     */
    public String getMultiplicador() {
        return multiplicador;
    }

    // ---------- SETTERS ----------
    /**
     * Treu la fitxa de la casella.
     * @return La fitxa treta o null si no n'hi havia cap.
     */
    public Fitxa treureFitxa() {
        Fitxa resultat = fitxa;
        fitxa = null;
        return resultat;
    }

    /**
     * Col·loca una fitxa a la casella.
     * @param fitxa La fitxa a col·locar.
     * @return true si s'ha pogut col·locar, false si ja hi havia una fitxa.
     */
    public boolean colocarFitxa(Fitxa fitxa) {
        if (this.fitxa == null) {
            this.fitxa = fitxa;
            return true;
        }
        return false;
    }

    // ---------- MÈTODES ----------
    /**
     * Elimina el multiplicador de la casella (la deixa sense bonus).
     */
    public void cremarMultiplicador() {
        this.multiplicador = "";
    }

    /**
     * Comprova si la casella té una fitxa.
     * @return true si hi ha una fitxa, false en cas contrari.
     */
    public boolean teFitxa() {
        return fitxa != null;
    }
}


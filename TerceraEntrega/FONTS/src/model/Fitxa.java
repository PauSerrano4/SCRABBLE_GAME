package model;

import java.io.Serializable;

/**
 * Classe Fitxa - Representa una fitxa del joc d'Scrabble.
 * @author alexander.de.jong
 */
public class Fitxa implements Serializable {

    // ---------- ATRIBUTS ----------
    /** Lletra que representa la fitxa, pot ser un comodí representat per '#' */
    private char lletra;
    /** Valor de punts que té la fitxa, per exemple, 1 per 'A', 2 per 'B', etc. */
    private int valor;

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe Fitxa.
     * @param lletra La lletra que representa la fitxa.
     * @param valor El valor de punts que té la fitxa.
     */
    public Fitxa(char lletra, int valor) {
        this.lletra = lletra;
        this.valor = valor;
    }

    // ---------- GETTERS ----------
    /**
     * Retorna la lletra de la fitxa.
     * @return La lletra de la fitxa.
     */
    public char getLletra() {
        return lletra;
    }

    /**
     * Retorna el valor de punts de la fitxa.
     * @return El valor de punts de la fitxa.
     */
    public int getValor() {
        return valor;
    }

    // ---------- SETTERS ----------
    /**
     * Estableix la lletra de la fitxa.
     * @param lletra La nova lletra de la fitxa.
     */
    public void setLletra(char lletra) {
        this.lletra = lletra;
    }

    /**
     * Estableix el valor de punts de la fitxa.
     * @param valor El nou valor de punts.
     */
    public void setValor(int valor) {
        this.valor = valor;
    }

    // ---------- MÈTODES ----------
    /**
     * Indica si la fitxa és un comodí.
     * @return true si la fitxa és un comodí, false en cas contrari.
     */
    public boolean esComodin() {
        return this.getLletra() == '#';
    }
}


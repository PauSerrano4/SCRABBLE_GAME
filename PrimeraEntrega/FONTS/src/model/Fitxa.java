package model;
/**
 * Clase Fitxa - Representa una ficha del juego Scrabble
 */
public class Fitxa {
    private char lletra;
    private int valor;
    
    /**
     * Constructor de la clase Fitxa
     * @param lletra La letra que representa la ficha
     * @param valor El valor de puntos que tiene la ficha
     */
    public Fitxa(char lletra, int valor) {
        this.lletra = lletra;
        this.valor = valor;
    }
    
    /**
     * Obtiene la letra de la ficha
     * @return La letra de la ficha
     */
    public char getLletra() {
        return lletra;
    }
    
    /**
     * Obtiene el valor de puntos de la ficha
     * @return El valor de puntos de la ficha
     */
    public int getValor() {
        return valor;
    }
    
    /**
     * Establece el valor de puntos de la ficha
     * @param valor El nuevo valor de puntos
     */
    public void setValor(int valor) {
        this.valor = valor;
    }


    /**
     * Nos indica si la fitxa es comodin o no
     */
    public boolean esComodin() {
        if (this.getLletra() == '#') return true;
        return false;
    }
    public void setLletra(char lletra) {
        this.lletra = lletra;
    }
    
}


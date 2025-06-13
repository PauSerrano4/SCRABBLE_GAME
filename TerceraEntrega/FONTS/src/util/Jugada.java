package util;

/**
 * Classe que representa una jugada realitzada per un jugador a Scrabble.
 * Emmagatzema el nom del jugador, la paraula jugada i els punts obtinguts.
 * @author Alexander de Jong
 */
public class Jugada implements java.io.Serializable {

    // ---------- ATRIBUTS ----------
    /** Nom del jugador que ha fet la jugada */
    public final String jugador;
    /** Paraula jugada */
    public final String paraula;
    /** Punts obtinguts en la jugada */
    public final int punts;

    // ---------- CONSTRUCTOR ----------
    /**
     * Creadora de la classe Jugada.
     * @param jugador Nom del jugador
     * @param paraula Paraula jugada
     * @param punts Punts obtinguts
     */
    public Jugada(String jugador, String paraula, int punts) {
        this.jugador = jugador;
        this.paraula = paraula;
        this.punts = punts;
    }

    // ---------- GETTERS ----------
    /**
     * Retorna el nom del jugador que ha fet la jugada.
     * @return Nom del jugador
     */
    public String getJugador() {
        return jugador;
    }

    /**
     * Retorna la paraula jugada.
     * @return Paraula jugada
     */
    public String getParaula() {
        return paraula;
    }

    /**
     * Retorna els punts obtinguts en la jugada.
     * @return Punts de la jugada
     */
    public int getPunts() {
        return punts;
    }

    // ---------- MÈTODES ----------
    /**
     * Retorna una representació en String de la jugada.
     * @return String amb el format "jugador → paraula (+punts)"
     */
    @Override
    public String toString() {
        return jugador + " → " + paraula + " (+" + punts + ")";
    }
}



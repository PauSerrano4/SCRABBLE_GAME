package util;

import java.util.*;

/**
 * Enum que representa la dificultat d'una partida d'Scrabble.
 * Cada dificultat té un nom a mostrar, una mida de faristol i un multiplicador de puntuació.
 */
public enum Dificultat {
    FACIL("Fàcil", 8, 0.75f),
    NORMAL("Normal", 7, 1.0f),
    DIFICIL("Difícil", 6, 1.25f);

    // ---------- ATRIBUTS ----------
    private final String displayName;
    private final int rackSize;
    private final float multiplicador;

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de l'enum Dificultat.
     * @param displayName Nom a mostrar de la dificultat.
     * @param rackSize Mida del faristol.
     * @param multiplicador Multiplicador de puntuació.
     */
    Dificultat(String displayName, int rackSize, float multiplicador) {
        this.displayName = displayName;
        this.rackSize = rackSize;
        this.multiplicador = multiplicador;
    }

    // ---------- GETTERS ----------
    /**
     * Retorna el nom a mostrar de la dificultat.
     * @return Nom a mostrar.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Retorna el multiplicador de la dificultat.
     * @return Multiplicador.
     */
    public float getMultiplicador() {
        return multiplicador;
    }

    /**
     * Retorna la mida del faristol per a la dificultat.
     * @return Mida del faristol.
     */
    public int getRackSize() {
        return rackSize;
    }

    // ---------- MÈTODES ESTÀTICS ----------
    /**
     * Obté la dificultat a partir del seu nom a mostrar.
     * @param name Nom a mostrar.
     * @return Dificultat corresponent.
     * @throws IllegalArgumentException si no existeix cap dificultat amb aquest nom.
     */
    public static Dificultat fromDisplayName(String name) {
        for (Dificultat d : values()) {
            if (d.displayName.equals(name))
                return d;
        }
        throw new IllegalArgumentException("Dificultat desconeguda: " + name);
    }

    /**
     * Retorna totes les dificultats disponibles.
     * @return Array de dificultats disponibles.
     */
    public static Dificultat[] getDificultatsDisponibles() {
        return Dificultat.values();
    }
}
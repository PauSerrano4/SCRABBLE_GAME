package util;

import java.util.*;

/**
 * Enum que representa el mode de joc d'una partida d'Scrabble.
 * Pot ser Clàssic o Contrarrellotge.
 */
public enum ModeJoc {
    CLASSIC("Clàssic"),
    CONTRARRELLOTGE("Contrarrellotge");

    // ---------- ATRIBUTS ----------
    private final String displayName;

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de l'enum ModeJoc.
     * @param displayName Nom a mostrar del mode de joc.
     */
    ModeJoc(String displayName) {
        this.displayName = displayName;
    }

    // ---------- GETTERS ----------
    /**
     * Retorna el nom a mostrar del mode de joc.
     * @return Nom a mostrar.
     */
    public String getDisplayName() {
        return displayName;
    }

    // ---------- MÈTODES ESTÀTICS ----------
    /**
     * Obté el mode de joc a partir del seu nom a mostrar.
     * @param name Nom a mostrar.
     * @return ModeJoc corresponent.
     * @throws IllegalArgumentException si no existeix cap mode amb aquest nom.
     */
    public static ModeJoc fromDisplayName(String name) {
        for (ModeJoc m : values()) {
            if (m.displayName.equals(name)) 
                return m;
        }
        throw new IllegalArgumentException("Mode de joc desconegut: " + name);
    }

    /**
     * Retorna tots els modes de joc disponibles.
     * @return Array de modes de joc disponibles.
     */
    public static ModeJoc[] getModesDisponibles() {
        return ModeJoc.values();
    }
}
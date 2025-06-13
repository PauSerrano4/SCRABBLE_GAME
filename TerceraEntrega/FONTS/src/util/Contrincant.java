package util;

/**
 * Enum que representa el tipus de contrincant en una partida d'Scrabble.
 * Pot ser MAQUINA o JUGADOR.
 */
public enum Contrincant {
    MAQUINA("Maquina"),
    JUGADOR("Jugador");

    // ---------- ATRIBUTS ----------
    private final String displayName;

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de l'enum Contrincant.
     * @param displayName Nom a mostrar del contrincant.
     */
    Contrincant(String displayName) {
        this.displayName = displayName;
    }

    // ---------- GETTERS ----------
    /**
     * Retorna el nom a mostrar del contrincant.
     * @return Nom a mostrar.
     */
    public String getDisplayName() {
        return displayName;
    }

    // ---------- MÈTODES ESTÀTICS ----------
    /**
     * Obté el contrincant a partir del seu nom a mostrar.
     * @param name Nom a mostrar.
     * @return Contrincant corresponent.
     * @throws IllegalArgumentException si no existeix cap contrincant amb aquest nom.
     */
    public static Contrincant fromDisplayName(String name) {
        for (Contrincant c : values()) {
            if (c.displayName.equals(name)) 
                return c;
        }
        throw new IllegalArgumentException("Dificultat desconeguda: " + name);
    }

    /**
     * Retorna tots els contrincants disponibles.
     * @return Array de contrincants disponibles.
     */
    public static Contrincant[] getContrincantsDisponibles() {
        return Contrincant.values();
    }
}

package model;
import java.util.*;

/**
 * Classe que conté les estadístiques d'un usuari
 * @autor Ferran Blanchart Reyes | ferran.blanchart@estudiantat.upc.edu
 */
public class Estadistiques {

    /** Guarda les partides guanyades de l'usuari */
    private Integer partidesGuanyades;
    /** Guarda les partides perdudes de l'usuari */
    private Integer partidesPerdudes;
    /** Guarda el total de punts de l'usuari */
    private Float totaPunts;
    /** Guarda la puntuació més alta obtinguda en una partida de l'usuari */
    private Float millorPuntuacio;
    /** Guarda la puntuació mitjana per partida de l'usuari */
    private Float puntuacioMitjana;
    /** Guarda els ids de totes les partides finalitzades de l'usuari */
    private Set<String> historialPartides;
    /** GUarda els ids de totes les partides pausades de l'usuari */
    private Set<String> partidesPausades;

    //---------CONSTRUCTORES---------

    /** crea unes estaístiques buides per un usuari acabat de crear */
    public Estadistiques() {
        this.partidesGuanyades = 0;
        this.partidesPerdudes = 0;
        this.totaPunts = 0f;
        this.millorPuntuacio = 0f;
        this.puntuacioMitjana = 0f;
        this.historialPartides = new HashSet<>();
        this.partidesPausades = new HashSet<>();
    }

    //----------CONSULTORES----------

    /** retorna el nombre de partides guanyades de l'usuari */
    public Integer getPartidesGuanyades() {
        return partidesGuanyades;
    }

    /** retorna el nombre de partides perdudes de l'usuari */
    public Integer getPartidesPerdudes() {
        return partidesPerdudes;
    }

    /** retorna el total de punts de l'usuari */
    public Float getTotalPunts() {
        return totaPunts;
    }

    /** retorna la puntuació més alta obtinguda en una partida de l'usuari */
    public Float getMillorPuntuacio() {
        return millorPuntuacio;
    }

    /** retorna  la puntuació mitjana per partida de l'usuari*/
    public Float getPuntuacioMitjana() {
        return puntuacioMitjana;
    }

    /** els ids de totes les partides finalitzades de l'usuari */
    public Set<String> getHistorialPartides() {
        return historialPartides;
    }

    /** retorna els ids de totes les partides pausades de l'usuari */
    public Set<String> getPartidesPausades() {
        return partidesPausades;
    }

    //-----------MODIFICADORES----------

    /** actualitza les estadístiques de l'usuari en funció de l'estat en que es troba la partida indicada a la capcelera */
    public void actualitzarEstadistiques(boolean pausada, String idPartida, boolean guanyador, Float puntuacio) {
        if (pausada) {
            partidesPausades.add(idPartida);
        } else {
            historialPartides.add(idPartida);

            totaPunts += puntuacio;

            if (guanyador) partidesGuanyades += 1;
            else partidesPerdudes += 1;

            if (puntuacio > millorPuntuacio) millorPuntuacio = puntuacio;

            int totalPartides = partidesGuanyades + partidesPerdudes;
            puntuacioMitjana = totaPunts / totalPartides;
        }
    }
}

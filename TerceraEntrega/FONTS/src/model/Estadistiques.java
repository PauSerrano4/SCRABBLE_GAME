package model;

import java.util.*;
import util.Pair;
import java.io.Serializable;

/**
 * Classe que conté les estadístiques d'un usuari.
 * Gestiona el total de punts, millor puntuació, puntuació mitjana, historial de partides i estadístiques per diccionari.
 * @author Ferran Blanchart Reyes | ferran.blanchart@estudiantat.upc.edu
 */
public class Estadistiques implements Serializable {

    // ---------- ATRIBUTS ----------
    /** Guarda el total de punts de l'usuari */
    private Float totaPunts;
    /** Guarda la puntuació més alta obtinguda en una partida de l'usuari */
    private Float millorPuntuacio;
    /** Guarda la puntuació mitjana per partida de l'usuari */
    private Float puntuacioMitjana;
    /** Guarda els ids de totes les partides finalitzades de l'usuari */
    private Set<Integer> historialPartides;
    /** Guarda la suma total de puntuacions obtingudes per cada diccionari utilitzat */
    private Map<String, Float> estadistiquesPerDiccionari;

    // ---------- CONSTRUCTORS ----------
    /**
     * Crea unes estadístiques buides per un usuari acabat de crear.
     */
    public Estadistiques() {
        this.totaPunts = 0f;
        this.millorPuntuacio = 0f;
        this.puntuacioMitjana = 0f;
        this.historialPartides = new HashSet<>();
        this.estadistiquesPerDiccionari = new HashMap<>();
    }

    // ---------- GETTERS ----------
    /**
     * Retorna les estadístiques dels diccionaris.
     * @return Mapa amb les estadístiques per diccionari.
     */
    public Map<String, Float> getEstadistiquesPerDiccionari() {
        return estadistiquesPerDiccionari;
    }

    /**
     * Retorna l'historial d'ids de totes les partides finalitzades de l'usuari.
     * @return Conjunt d'ids de partides.
     */
    public Set<Integer> getHistorialPartides() {
        return historialPartides;
    }

    /**
     * Retorna la millor puntuació obtinguda en una partida de l'usuari.
     * @return Millor puntuació.
     */
    public Float getMillorPuntuacio() {
        return millorPuntuacio;
    }

    /**
     * Retorna la puntuació mitjana per partida de l'usuari.
     * @return Puntuació mitjana.
     */
    public Float getPuntuacioMitjana() {
        return puntuacioMitjana;
    }

    /**
     * Retorna el total de punts de l'usuari.
     * @return Total de punts.
     */
    public Float getTotalPunts() {
        return totaPunts;
    }

    // ---------- MODIFICADORS ----------
    /**
     * Actualitza les estadístiques de l'usuari en funció de l'estat en què es troba la partida indicada.
     * @param idPartida Identificador de la partida.
     * @param puntuacio Puntuació obtinguda.
     * @param diccionari Nom del diccionari utilitzat.
     */
    public void actualitzarEstadistiques(Integer idPartida, Float puntuacio, String diccionari) {
        historialPartides.add(idPartida);

        totaPunts += puntuacio;

        if (puntuacio > millorPuntuacio) millorPuntuacio = puntuacio;

        if (puntuacioMitjana == 0)  puntuacioMitjana = puntuacio;
        else puntuacioMitjana = (puntuacioMitjana + puntuacio) / 2;

        float puntsActuals = estadistiquesPerDiccionari.getOrDefault(diccionari, 0.0f);
        estadistiquesPerDiccionari.put(diccionari, puntsActuals + puntuacio);
    }
}

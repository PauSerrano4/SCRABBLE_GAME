package model;

import java.util.*;
import java.io.Serializable;

/**
 * Classe Jugador.java
 * Conté tota la informació d'un jugador d'una partida d'Scrabble.
 * @author Pau Serrano Sanz | pau.serrano.sanz@estudiantat.upc.edu
 */
public class Jugador implements Serializable {

    // ---------- ATRIBUTS ----------
    /** Nom del jugador */
    private String nom;
    /** Puntuació actual del jugador */
    private int puntuacio;
    /** Llista de fitxes que té el jugador a l'atril */
    private List<Fitxa> fitxes;

    // ---------- CONSTRUCTORS ----------
    /**
     * Creadora de la classe Jugador.
     * Inicialitza el nom i la puntuació a 0 i la llista de fitxes buida.
     * @param nom El nom del jugador.
     */
    public Jugador(String nom) {
        this.nom = nom;
        this.puntuacio = 0;
        this.fitxes = new ArrayList<>();
    }

    // ---------- GETTERS ----------
    /**
     * Retorna la llista de fitxes del jugador.
     * @return Llista de fitxes.
     */
    public List<Fitxa> getFitxes() {
        return fitxes;
    }

    /**
     * Retorna el nom del jugador.
     * @return Nom del jugador.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retorna la puntuació actual del jugador.
     * @return Puntuació.
     */
    public int getPuntuacio() {
        return puntuacio;
    }


    /**
     * Retorna una representació en String de les fitxes del jugador.
     * @return String amb les fitxes i els seus valors.
     */
    public String getFitxesString() {
        StringBuilder sb = new StringBuilder();
        for (Fitxa f : fitxes) {
            sb.append(f.getLletra()).append("(").append(f.getValor()).append(") ");
        }
        return sb.toString().trim();
    }

    // ---------- MODIFICADORS ----------
    /**
     * Actualitza la puntuació del jugador sumant-hi la quantitat indicada.
     * @param puntuacio Quantitat de punts a afegir.
     */
    public void actualitzarPuntuacio(int puntuacio) {
        this.puntuacio += puntuacio;
    }

    /**
     * Afegeix una fitxa a la llista de fitxes del jugador.
     * @param fitxa Fitxa a afegir.
     */
    public void afegirFitxa(Fitxa fitxa) {
        fitxes.add(fitxa);
    }

    /**
     * Substitueix totes les fitxes actuals del jugador per unes de noves.
     * @param novesFitxes Nova llista de fitxes.
     */
    public void canviarFitxes(List<Fitxa> novesFitxes) {
        this.fitxes = novesFitxes;
    }

    /**
     * Intenta col·locar una fitxa del jugador al tauler.
     * Si la fitxa és present en l’atril del jugador, s’elimina.
     * @param fitxa Fitxa a col·locar.
     */
    public void colocarFitxa(Fitxa fitxa) {
        if (fitxes.contains(fitxa)) {
            fitxes.remove(fitxa);
        } else {
            //System.out.println("El jugador no té aquesta fitxa.");
        }
    }

    /**
     * Elimina una fitxa de la llista de fitxes del jugador.
     * @param fitxa Fitxa a eliminar.
     */
    public void eliminarFitxa(Fitxa fitxa) {
        fitxes.remove(fitxa);
    }
}

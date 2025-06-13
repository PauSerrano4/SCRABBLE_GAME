package model;
import java.util.*;

/** 
 * Classe Jugador.java
 * Conté tota la informació d'un jugador d'una partida d'Scrabble.
 * @autor Pau Serrano Sanz | pau.serrano.sanz@estudiantat.upc.edu
 */

public class Jugador {
    private String nom;
    private int puntuacio;
    private List<Fitxa> fitxes;
    
    //----------CREADORES----------//

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
    
    //----------CONSULTORES----------//

    public String getNom() {
        return nom;
    }
    
    public int getPuntuacio() {
        return puntuacio;
    }

    public List<Fitxa> getFitxes() {
        return fitxes;
    }

    //----------MODIFICADORES----------//

    /**
     * Incrementa la puntuació del jugador.
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
     * Intenta col·locar una fitxa del jugador al tauler.
     * Si la fitxa és present en l’atril del jugador, s’elimina.
     * @param fitxa Fitxa a col·locar.
     */
    public void colocarFitxa(Fitxa fitxa) {
        if (fitxes.contains(fitxa)) {
            fitxes.remove(fitxa);
            System.out.println("Fitxa col·locada amb èxit.");
        } else {
            System.out.println("El jugador no té aquesta fitxa.");
        }
    }

    /**
     * Reordena aleatòriament les fitxes de l’atril del jugador.
     */
    public void reordenarFitxes() {
        Collections.shuffle(fitxes);
        System.out.println("Fitxes reordenades aleatòriament.");
    }

    /**
     * Substitueix totes les fitxes actuals del jugador per unes de noves.
     * @param novesFitxes Nova llista de fitxes.
     */
    public void canviarFitxes(List<Fitxa> novesFitxes) {
        this.fitxes = novesFitxes;
        System.out.println("Les fitxes han estat canviades.");
    }
}

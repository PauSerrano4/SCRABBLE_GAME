package model;

import java.util.*;
import util.Pair;

/**
 * Classe Partida - Representa una partida d'Scrabble.
 * Conté la informació del tauler, jugadors, diccionari, bossa de fitxes i estat de la partida.
 * @autor Pau Serrano Sanz | pau.serrano.sanz@estudiantat.upc.edu
 */
public class Partida {
    private int idPartida;
    private List<Jugador> jugadors;
    private Tauler tauler;
    private Diccionari diccionari;
    private List<Fitxa> bossa;
    private String estat;
    private int tornActual;

    //----------CONSTRUCTORES----------//
    /**
     * Constructora de la classe Partida
     * @param idPartida Identificador únic de la partida
     * @param jugadors Llista de jugadors participants
     * @param tauler El tauler de joc
     * @param diccionari El diccionari utilitzat
     */
    public Partida(int idPartida, List<Jugador> jugadors, Tauler tauler, Diccionari diccionari) {
        this.idPartida = idPartida;
        this.jugadors = jugadors;
        this.tauler = tauler;
        this.diccionari = diccionari;
        this.estat = "enCurs";
        this.tornActual = 0;
        inicialitzarBossa();
    }

    /**
     * Inicialitza la bossa de fitxes amb les lletres i puntuacions indicades a l'alfabet del diccionari.
     * Omple i barreja aleatòriament la llista de fitxes.
     */
    public void inicialitzarBossa() {
        bossa = new ArrayList<>();
        Map<String, Pair<Integer, Integer>> alfabet = diccionari.getAlfabet();

        for (Map.Entry<String, Pair<Integer, Integer>> entrada : alfabet.entrySet()) {
            String lletra = entrada.getKey();
            int quantitat = entrada.getValue().first;
            int puntuacio = entrada.getValue().second;

            for (int i = 0; i < quantitat; i++) {
                bossa.add(new Fitxa(lletra.charAt(0), puntuacio));
            }
        }

        Collections.shuffle(bossa); // Barreja la bossa
    }

    //---------- CONSULTORES ----------//

    /**
     * Retorna l'identificador de la partida
     * @return idPartida
     */
    public int getIdPartida() {
        return idPartida;
    }

    /**
     * Retorna la llista de jugadors
     * @return Llista de Jugador
     */
    public List<Jugador> getJugadors() {
        return jugadors;
    }

    /**
     * Retorna el tauler de la partida
     * @return Tauler
     */
    public Tauler getTauler() {
        return tauler;
    }

    /**
     * Retorna el diccionari utilitzat
     * @return Diccionari
     */
    public Diccionari getDiccionari() {
        return diccionari;
    }

    /**
     * Retorna la llista de fitxes a la bossa (només lectura)
     * @return Llista de Fitxa
     */
    public List<Fitxa> getBossa() {
        return Collections.unmodifiableList(bossa);
    }

    /**
     * Retorna l'estat actual de la partida
     * @return String amb l'estat ("enCurs", "pausada", "finalitzada")
     */
    public String getEstat() {
        return estat;
    }

    /**
     * Retorna el nom del jugador al qual li toca jugar
     * @return Nom del jugador actual
     */
    public String getTornJugador() {
        return jugadors.get(tornActual).getNom();
    }

    /**
     * Retorna l'objecte Jugador pel nom especificat
     * @param nom Nom del jugador a buscar
     * @return Jugador corresponent o null si no es troba
     */
    public Jugador getJugador(String nom) {
        for (Jugador j : jugadors) {
            if (j.getNom().equals(nom)) return j;
        }
        return null;
    }

    /**
     * Mostra el tauler actual per consola
     */
    public void mostrarTauler() {
        System.out.println(tauler.mostrarTauler());
    }

    //---------- MODIFICADORES ----------//

    /**
     * Canvia l'estat de la partida a "finalitzada"
     */
    public void finalitzarPartida() {
        this.estat = "finalitzada";
    }

    /**
     * Pausa la partida (canvia l'estat a "pausada")
     */
    public void pausarPartida() {
        this.estat = "pausada";
    }

    /**
     * Continua la partida si estava pausada (canvia l'estat a "enCurs")
     */
    public void continuarPartida() {
        this.estat = "enCurs";
    }

    /**
     * Avança el torn al següent jugador
     */
    public void avançarTorn() {
        this.tornActual = (this.tornActual + 1) % jugadors.size();
    }

    /**
     * Reparteix fitxes d'una bossa al jugador indicat, fins arribar a un màxim de numFitxes
     * o fins que no quedin fitxes disponibles.
     * @param nomJugador Nom del jugador que rep les fitxes
     * @param numFitxes Quantitat màxima de fitxes a repartir
     */
    public void repartirFitxes(String nomJugador, int numFitxes) {
        Jugador jugador = getJugador(nomJugador);

        if (jugador == null) {
            System.out.println("Jugador no trobat: " + nomJugador);
            return;
        }

        if (bossa.isEmpty()) {
            System.out.println("No hi ha fitxes disponibles a la bossa.");
            return;
        }

        int fitxesRepartides = 0;
        Iterator<Fitxa> it = bossa.iterator();

        while (it.hasNext() && fitxesRepartides < numFitxes) {
            Fitxa f = it.next();
            jugador.afegirFitxa(f);
            it.remove();
            fitxesRepartides++;
        }

        System.out.println("S'han repartit " + fitxesRepartides + " fitxes a " + nomJugador + ".");
    }
}

package util;

import java.util.Timer;
import java.util.TimerTask;

/** 
 * Classe util Temporitzador.java
 * S'encarrega de 
 * @author Pau Serrano Sanz | pau.serrano.sanz@estudiantat.upc.edu
 */

public class Temporitzador {
    private final int duracioInicialSegons;
    private int segonsRestants;
    private Timer timer;
    private boolean enMarxa;
    private Runnable callbackTempsEsgotat;
    private TemporitzadorListener listener;


    /**
     * Inicialitza un temporitzador amb una duració en minuts.
     *
     * @param minuts duració inicial
     * @param callbackTempsEsgotat acció a executar quan el temps s'acaba
     */
    public Temporitzador(int minuts, Runnable callbackTempsEsgotat) {
        this.duracioInicialSegons = minuts * 60;
        this.segonsRestants = duracioInicialSegons;
        this.callbackTempsEsgotat = callbackTempsEsgotat;
        this.enMarxa = false;
    }

    /**
     * Comença el compte enrere si no està en marxa.
     */
    public void iniciar() {
        if (enMarxa) return;

        timer = new Timer();
        enMarxa = true;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                segonsRestants--;
                if (listener != null) {
                    listener.onTick(getTempsFormatejat());  // Avisa la vista del temps actual
                }

                if (segonsRestants <= 0) {
                    parar();
                    if (callbackTempsEsgotat != null) callbackTempsEsgotat.run();
                    if (listener != null) listener.onFinal();  // Avisa que el temps s’ha acabat
                }
            }
        }, 1000, 1000);
    }

    /**
     * Atura el temporitzador si està actiu.
     */
    public void parar() {
        if (timer != null) {
            timer.cancel();
            enMarxa = false;
        }
    }

        
    /**
     * Reinicia el temporitzador a la duració inicial i el torna a començar.
     */
    public void reiniciar() {
        parar();
        segonsRestants = duracioInicialSegons;
        iniciar();
    }

    /**
     * Estableix manualment els segons restants.
     *
     * @param segons nous segons restants
     */
    public void setSegonsRestants(int segons) {
        this.segonsRestants = segons;
        if (listener != null) {
            listener.onTick(getTempsFormatejat());
        }
    }

    /**
     * Retorna els segons que queden.
     *
     * @return segons restants
     */
    public int getSegonsRestants() {
        return segonsRestants;
    }

    
    /**
     * Retorna el temps restant en format MM:SS.
     *
     * @return temps formatejat
     */
    public String getTempsFormatejat() {
        int minuts = segonsRestants / 60;
        int segons = segonsRestants % 60;
        return String.format("%02d:%02d", minuts, segons);
    }


    /**
     * Indica si el temporitzador està actiu.
     *
     * @return true si està en marxa
     */
    public boolean estaEnMarxa() {
        return enMarxa;
    }

    /**
     * Assigna el listener per actualitzacions de temps.
     *
     * @param listener objecte que escolta els canvis de temps
     */
    public void setListener(TemporitzadorListener listener) {
        this.listener = listener;
    }
}

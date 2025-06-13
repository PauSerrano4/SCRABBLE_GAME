package util;

/** 
 * Classe TemporitzadorListener.java
 * Interfície perquè el temporitzador pugui avisar la vista.
 * @author Pau Serrano Sanz | pau.serrano.sanz@estudiantat.upc.edu
 */

public interface TemporitzadorListener {
    
    /**
     * S'executa cada segon amb el temps restant formatat.
     *
     * @param tempsRestant temps actual en format "MM:SS"
     */
    void onTick(String tempsRestant);
    

    /**
     * S'executa quan s'acaba el temps del temporitzador.
     */
    void onFinal();             
}



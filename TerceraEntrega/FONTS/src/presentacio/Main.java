package presentacio;

import javax.swing.SwingUtilities;

/**
 * Classe Main - Punt d'entrada de l'aplicació Scrabble.
 */
public class Main {

    /**
     * Mètode principal. Inicialitza el controlador de presentació i la vista principal.
     * @param args Arguments de la línia de comandes (no utilitzats).
     */
    public static void main(String[] args) {
        ControladorPresentacio ctrl = ControladorPresentacio.getInstance();
        ctrl.inicialitzarVista();
    }
}


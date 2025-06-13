package exceptions;

/**
 * Excepció que indica que s'ha produït una jugada invàlida.
 */
public class JugadaInvalidaException extends Exception {
    /**
     * Crea una nova excepció amb el missatge especificat.
     * @param msg Descripció de l'error.
     */
    public JugadaInvalidaException(String msg) { super(msg); }
}

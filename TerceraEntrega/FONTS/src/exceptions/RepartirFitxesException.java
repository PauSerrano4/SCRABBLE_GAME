package exceptions;

/**
 * Excepció que indica un error en el procés de repartir fitxes..
 */
public class RepartirFitxesException extends Exception {

    /**
     * Crea una nova excepció amb el missatge especificat.
     * @param message Descripció de l'error.
     */
    public RepartirFitxesException(String message) {
        super(message);
    }

    /**
     * Crea una nova excepció amb el missatge i la causa original.
     * @param message Descripció de l'error.
     * @param cause   Causa original de l'excepció.
     */
    public RepartirFitxesException(String message, Throwable cause) {
        super(message, cause);
    }
}

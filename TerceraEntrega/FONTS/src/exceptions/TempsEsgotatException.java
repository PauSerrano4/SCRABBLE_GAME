package exceptions;


/**
 * Excepció que indica que s'ha esgotat el temps.
 */
public class TempsEsgotatException extends Exception {
    /**
     * Crea una nova excepció amb el missatge especificat.
     * @param msg Descripció de l'error.
     */
    public TempsEsgotatException(String msg) {
        super(msg);
    }
}
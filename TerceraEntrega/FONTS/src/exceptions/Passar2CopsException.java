package exceptions;

/**
 * Excepció que indica que s'ha intentat passar dues vegades .
 *

 
 *
 * @author alexd
 */
public class Passar2CopsException extends Exception {
    /**
     * Crea una nova excepció amb el missatge especificat.
     * @param msg Descripció de l'error.
     */
    public Passar2CopsException(String msg) {
        super(msg);
    }
}
package exceptions;


/**
 * Excepció que indica que ja existeix un diccionari amb el nom especificat.
 */
public class DiccionariJaExisteixException extends Exception {
    /**
     * Crea una nova excepció amb el missatge especificat.
     * @param msg Descripció de l'error.
     */
    public DiccionariJaExisteixException(String msg) {
        super(msg);
    }
}
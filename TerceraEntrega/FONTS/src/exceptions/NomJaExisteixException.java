package exceptions;

/**
 * Excepció que indica que un nom ja existeix.
 * Aquesta excepció es llença quan s'intenta crear o afegir un usuari amb un nom que ja està registrat.
 */
public class NomJaExisteixException extends Exception {
    /**
     * Crea una nova excepció amb el missatge especificat.
     * @param msg Descripció de l'error.
     */
    public NomJaExisteixException(String msg) {
        super(msg);
    }
}
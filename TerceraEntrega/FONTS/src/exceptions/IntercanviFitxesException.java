package exceptions;

/**
 * Excepció que indica que s'ha produït un error durant l'intercanvi de fitxes.
 * Aquesta excepció es llença quan es detecta una situació incorrecta o inesperada
 * en el procés d'intercanvi de fitxes dins de l'aplicació.
 */
public class IntercanviFitxesException extends Exception {
    /**
     * Crea una nova excepció amb el missatge especificat.
     * @param msg Descripció de l'error.
     */
    public IntercanviFitxesException(String msg) {
        super(msg);
    }
}

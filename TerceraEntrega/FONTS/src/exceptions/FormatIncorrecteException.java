package exceptions;

/**
 * Excepció que indica que el format d'un element és incorrecte.
 * Aquesta excepció es llença quan es detecta un error de format en les dades d'entrada.
 */
public class FormatIncorrecteException extends Exception {
    /**
     * Crea una nova excepció amb el missatge especificat.
     * @param msg Descripció de l'error.
     */
    public FormatIncorrecteException(String msg) {
        super(msg);
    }
}
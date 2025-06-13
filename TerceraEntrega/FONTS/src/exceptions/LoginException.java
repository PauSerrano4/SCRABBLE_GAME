package exceptions;


/**
 * Exepcio que es llança quan ha fallat fer el login o el registre de un usuari.
 */
public class LoginException extends Exception {
    /**
     * Crea una nova excepció amb el missatge especificat.
     * @param message Descripció de l'error.
     */
    public LoginException(String message) {
        super(message);
    }

    /**
     * Crea una nova excepció amb el missatge especificat i la causa original.
     * @param message Descripció de l'error.
     * @param cause Causa de l'error.
     */
    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
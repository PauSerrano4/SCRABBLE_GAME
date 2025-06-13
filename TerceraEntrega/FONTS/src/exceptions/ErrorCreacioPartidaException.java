package exceptions;


/**
 * Excepció que indica un error en la creació d'una partida.
 * Aquesta excepció es llença quan es produeix una incidència durant el procés de creació d'una nova partida.
 */
public class ErrorCreacioPartidaException extends Exception {
    /**
     * Crea una nova excepció amb el missatge especificat.
     * @param msg Descripció de l'error.
     */
    public ErrorCreacioPartidaException(String msg) {
        super(msg);
    }
}
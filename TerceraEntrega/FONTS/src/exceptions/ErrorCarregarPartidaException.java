package exceptions;

/**
 * Excepció que indica un error en carregar una partida.
 * Aquesta excepció es llença quan es produeix un problema durant el procés de càrrega d'una partida.
 */
public class ErrorCarregarPartidaException extends Exception {
    /**
     * Crea una nova excepció amb el missatge especificat.
     * @param msg Descripció de l'error.
     */
    public ErrorCarregarPartidaException(String msg) {
        super(msg);
    }
}
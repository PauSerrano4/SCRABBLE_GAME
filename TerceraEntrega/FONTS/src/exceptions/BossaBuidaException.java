package exceptions;


/**
 * Excepció que s'emet quan s'intenta accedir o operar amb una bossa buida.
 * Aquesta excepció indica que l'operació no es pot completar perquè la bossa no conté cap element.
 */
public class BossaBuidaException extends Exception {
    /**
     * Crea una nova excepció amb el missatge especificat.
     * @param msg Descripció de l'error.
     */
    public BossaBuidaException(String msg) {
        super(msg);
    }
}
package exceptions;

/**
 * Excepció llançada quan un usuari intenta carregar una partida 
 * que no li pertany o a la qual no té accés.
 * 
 * Aquesta excepció es llança per garantir la seguretat i privacitat
 * de les partides guardades, evitant que els usuaris puguin accedir
 * a partides d'altres jugadors.
 * 
 * @author Subgrup 11.1 - PROP FIB
 */
public class JugadorNoPertanyPartidaException extends Exception {
    
    /**
     * Crea una nova excepció amb un missatge per defecte.
     */
    public JugadorNoPertanyPartidaException() {
        super("L'usuari no té permisos per accedir a aquesta partida.");
    }
    
    /**
     * Crea una nova excepció amb un missatge personalitzat.
     * 
     * @param missatge Missatge descriptiu de l'error de seguretat
     */
    public JugadorNoPertanyPartidaException(String missatge) {
        super(missatge);
    }
    
    /**
     * Crea una nova excepció amb un missatge i una causa.
     * 
     * @param missatge Missatge descriptiu de l'error
     * @param causa Excepció que ha causat aquest error
     */
    public JugadorNoPertanyPartidaException(String missatge, Throwable causa) {
        super(missatge, causa);
    }
}
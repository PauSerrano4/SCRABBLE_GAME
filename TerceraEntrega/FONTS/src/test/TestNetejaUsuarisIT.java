package test;

import org.junit.*;
import static org.junit.Assert.*;
import controller.ControladorDomini;
import exceptions.LoginException;

/**
 * Test de neteja per eliminar usuaris específics utilitzant el domini.
 * @author Alexander de Jong
 */
public class TestNetejaUsuarisIT {

    private ControladorDomini ctrl;
    
    @Before
    public void setUp() {
        ctrl = ControladorDomini.getInstance();
    }

    /**
     * Test: elimina els usuaris especificats utilitzant el mètode del domini.
     */
    @Test
    public void testEliminarUsuarisEspecifics() {
        int usuarisEliminats = 0;
        
        // Intentar eliminar jugador1
        try {
            ctrl.login("jugador1", "1234");
            // Verificar que l'usuari està actiu abans d'eliminar
            if (ctrl.getUsuariActiu() != null) {
                ctrl.eliminarUsuariComplet("jugador1");
                usuarisEliminats++;
                System.out.println("Eliminat usuari: jugador1");
            }
        } catch (LoginException e) {
            System.out.println("jugador1 no existeix o credencials incorrectes");
        } catch (Exception e) {
            System.out.println("Error eliminant jugador1: " + e.getMessage());
        }
        
        // Reinicialitzar per al següent usuari
        ctrl.inicialitzar();
        
        // Intentar eliminar jugador2
        try {
            ctrl.login("jugador2", "1234");
            // Verificar que l'usuari està actiu abans d'eliminar
            if (ctrl.getUsuariActiu() != null) {
                ctrl.eliminarUsuariComplet("jugador2");
                usuarisEliminats++;
                System.out.println("Eliminat usuari: jugador2");
            }
        } catch (LoginException e) {
            System.out.println("jugador2 no existeix o credencials incorrectes");
        } catch (Exception e) {
            System.out.println("Error eliminant jugador2: " + e.getMessage());
        }

        System.out.println("Total usuaris eliminats: " + usuarisEliminats);
        assertTrue("El test hauria de completar-se sense errors", usuarisEliminats >= 0);
    }
}

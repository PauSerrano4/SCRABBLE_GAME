package test;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import model.*;
import util.*;

/**
 * Tests d'integració per funcionalitat d'usuaris.
 * Valida registre, login, canvis de nom/contrasenya i gestió multi-usuari.
 * @author Alexander de Jong
 */
public class TestUsuarisIT extends TestBaseIT {

    /**
     * Test: registre i login d'usuaris nous.
     * Verifica que el procés de registre funciona correctament
     * i que l'usuari queda actiu després del registre.
     * @author Alexander de Jong
     */
    @Test
    public void testRegistreILogin() {
        // Test de registre d'usuari nou amb nom únic
        String nomUsuari = "testUser_" + System.currentTimeMillis(); // Nom únic
        boolean registrat = ctrl.registrarUsuari(nomUsuari, "testPass");
        assertTrue("Hauria de poder registrar usuari nou", registrat);
        
        assertNotNull("Hauria d'haver usuari actiu després del registre", ctrl.getUsuariActiu());
        assertEquals(nomUsuari, ctrl.getUsuariActiu().getNom());
        // Netejar usuari després del test
        ctrl.eliminarUsuariComplet(nomUsuari); // Netejar usuari després del test
    }

    /**
     * Test: canvi de nom d'usuari actiu.
     * Verifica que es pot canviar el nom d'un usuari registrat
     * i que el canvi es persisteix correctament.
     * @author Alexander de Jong
     */
    @Test
    public void testCanviNomUsuari() {
        // Primer registrar un usuari amb nom únic
        String nomOriginal = "usuariOriginal_" + System.currentTimeMillis();
        boolean registrat = ctrl.registrarUsuari(nomOriginal, "password");
        assertTrue("Hauria de poder registrar usuari", registrat);
        
        // Canviar el nom
        String nouNom = "usuariNou_" + System.currentTimeMillis();
        boolean canviat = ctrl.canviarNomUsuari(nouNom);
        assertTrue("Hauria de poder canviar el nom", canviat);
        assertEquals(nouNom, ctrl.getUsuariActiu().getNom());
        // Netejar usuari després del test
        ctrl.eliminarUsuariComplet(nouNom); // Netejar usuari després del test
    }

    /**
     * Test: registre i login de múltiples usuaris sequencialment.
     * Valida que es poden gestionar múltiples usuaris independents
     * i que l'alternança entre ells funciona correctament.
     * @author Alexander de Jong
     */
    @Test
    public void testRegistreLoginMultiplesUsuaris() {
        // Registrar primer usuari amb nom únic
        String user1 = "user1_" + System.currentTimeMillis();
        boolean result1 = ctrl.registrarUsuari(user1, "pass1");
        assertTrue("Hauria de registrar user1", result1);
        assertEquals(user1, ctrl.getUsuariActiu().getNom());
        ctrl.eliminarUsuariComplet(user1); // Netejar usuari després del test
        
        // Sortir i registrar segon usuari
        ctrl.inicialitzar();
        String user2 = "user2_" + System.currentTimeMillis();
        boolean result2 = ctrl.registrarUsuari(user2, "pass2");
        assertTrue("Hauria de registrar user2", result2);
        assertEquals(user2, ctrl.getUsuariActiu().getNom());
        ctrl.eliminarUsuariComplet(user2); // Netejar usuari després del test
    }

    /**
     * Test: login amb credencials incorrectes.
     * Verifica que el sistema rebutja correctament els intents
     * de login amb usuaris inexistents o contrasenyes incorrectes.
     * @author Alexander de Jong
     */
    @Test
    public void testLoginCredencialsIncorrectes() {
        // Intentar login amb usuari inexistent
        boolean loginFallat = false;
        try {
            ctrl.login("usuariInexistent_" + System.currentTimeMillis(), "passwordIncorrecte");
        } catch (Exception e) {
            loginFallat = true;
            assertTrue("L'error hauria de mencionar credencials", 
                      e.getMessage().contains("incorrectes") || 
                      e.getMessage().contains("incorrect"));
        }
        assertTrue("Hauria de fallar el login amb credencials incorrectes", loginFallat);
    }

    /**
     * Test: canvi de contrasenya d'usuari.
     * Valida que es pot canviar la contrasenya i que la nova
     * contrasenya funciona per fer login posteriorment.
     * @author Alexander de Jong
     */
    @Test
    public void testCanviarContrasenya() {
        // Registrar usuari amb nom únic
        String nomUsuari = "userPassword_" + System.currentTimeMillis();
        boolean registrat = ctrl.registrarUsuari(nomUsuari, "oldPass");
        assertTrue("Hauria de poder registrar usuari", registrat);
        
        // Canviar contrasenya
        boolean canviat = ctrl.canviarContrasenya("newPass");
        assertTrue("Hauria de poder canviar la contrasenya", canviat);
        
        // Verificar que funciona la nova contrasenya
        ctrl.inicialitzar();
        try {
            ctrl.login(nomUsuari, "newPass");
            assertNotNull("Hauria de poder fer login amb nova contrasenya", ctrl.getUsuariActiu());
        } catch (Exception e) {
            fail("No hauria de fallar el login amb la nova contrasenya: " + e.getMessage());
        }
        ctrl.eliminarUsuariComplet(nomUsuari); // Netejar usuari després del test
    }
}

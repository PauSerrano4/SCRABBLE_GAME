package controller;

import persistencia.ControladorPersistencia;
import persistencia.GestorUsuari;
import model.Usuari;
import controller.ControladorDomini;
import exceptions.LoginException;

import java.io.IOException;
import java.util.Map;

/**
 * Classe CtrlUsuari - Gestiona la lògica d'autenticació i registre d'usuaris.
 * Permet iniciar sessió, registrar, canviar nom/contrasenya i gestionar l'usuari actiu.
 */
public class CtrlUsuari {

    // ---------- ATRIBUTS ----------
    /**
     * Gestor de persistència per accedir i modificar les dades dels usuaris.
     */
    private ControladorPersistencia gestor;

    /**
     * Usuari que ha iniciat sessió actualment.
     */
    private Usuari usuariActiu;

    /**
     * Segon usuari actiu (per a partides a dos jugadors).
     */
    private Usuari usuariActiu2;

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe CtrlUsuari.
     * Inicialitza el gestor d'usuaris
     * @param cp Controlador de persistència per accedir als usuaris.
     */
    public CtrlUsuari(ControladorPersistencia cp) {
        this.gestor = cp;
    }

    // ---------- GETTERS ----------
    /**
     * Retorna l'usuari actiu.
     * @return Usuari actiu.
     */
    public Usuari getUsuariActiu() {
        return usuariActiu;
    }

    /**
     * Retorna el segon usuari actiu (per a partides a dos jugadors).
     * @return Usuari actiu 2.
     */
    public Usuari getUsuari2() {
        return usuariActiu2;
    }

    // ---------- SETTERS ----------
    /**
     * Assigna l'usuari actiu.
     * @param u Usuari a assignar.
     */
    public void setUsuariActiu(Usuari u) {
        this.usuariActiu = u;
    }

    // ---------- MÈTODES PRINCIPALS ----------
    /**
     * Inicia sessió amb el nom d'usuari i contrasenya donats.
     * @param nomUsuari Nom d'usuari.
     * @param contrasenya Contrasenya.
     * @throws LoginException Si hi ha error iniciant sessió
     */
    public void login(String nomUsuari, String contrasenya) throws LoginException{
        Map<String, String> usuaris;
        try {
            usuaris = gestor.carregarUsuaris();
        } catch (IOException e) {
            // Error llegint fitxer d'usuaris: el llancem com a causa
            throw new LoginException("Error accedint al sistema d'usuaris.", e);
        }

        // Comprovem si l'usuari existeix i la contrasenya coincideix
        if (!usuaris.containsKey(nomUsuari) || !usuaris.get(nomUsuari).equals(contrasenya)) {
            throw new LoginException("Usuari o contrasenya incorrectes.");
        }

        // Si tot ok, guardem l'usuari actiu
        usuariActiu = new Usuari(nomUsuari, contrasenya, false);
        ControladorDomini.getInstance().setUsuariActiu(usuariActiu);
    }

    /**
     * Inicia sessió per al segon usuari (partida a dos jugadors).
     * @param nomUsuari Nom d'usuari.
     * @param contrasenya Contrasenya.
     * @throws LoginException Si hi ha error iniciant sessió
     */
    public void loginsegonusuari(String nomUsuari, String contrasenya) throws LoginException{
        Map<String, String> usuaris;
        try {
            usuaris = gestor.carregarUsuaris();
        } catch (IOException e) {
            // Error llegint fitxer d'usuaris: el llancem com a causa
            throw new LoginException("Error accedint al sistema d'usuaris.", e);
        }

        // Comprovem si l'usuari existeix i la contrasenya coincideix
        if (!usuaris.containsKey(nomUsuari) || !usuaris.get(nomUsuari).equals(contrasenya)) {
            throw new LoginException("Usuari o contrasenya incorrectes.");
        }

        // Si tot ok, guardem l'usuari actiu
        usuariActiu2 = new Usuari(nomUsuari, contrasenya, false);
        ControladorDomini.getInstance().setUsuari2(usuariActiu2);
    }

    /**
     * Registra un nou usuari amb nom i contrasenya.
     * @param nomUsuari Nom d'usuari.
     * @param contrasenya Contrasenya.
     * @return Cert si el registre és correcte, fals si ja existeix.
     */
    public boolean registrar(String nomUsuari, String contrasenya) {
        try {
            Map<String, String> usuaris = gestor.carregarUsuaris();
            if (usuaris.containsKey(nomUsuari)) return false;
            usuaris.put(nomUsuari, contrasenya);
            gestor.guardarUsuaris(usuaris);
            usuariActiu = new Usuari(nomUsuari, contrasenya, false);
            ControladorDomini.getInstance().setUsuariActiu(usuariActiu);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Registra el segon usuari (per a partides a dos jugadors).
     * @param nomUsuari Nom d'usuari.
     * @param contrasenya Contrasenya.
     * @return Cert si el registre és correcte, fals si ja existeix.
     */
    public boolean registrarsegonusuari(String nomUsuari, String contrasenya) {
        try {
            Map<String, String> usuaris = gestor.carregarUsuaris();
            if (usuaris.containsKey(nomUsuari)) return false;
            usuaris.put(nomUsuari, contrasenya);
            gestor.guardarUsuaris(usuaris);
            usuariActiu2 = new Usuari(nomUsuari, contrasenya, false);
            ControladorDomini.getInstance().setUsuari2(usuariActiu2);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Tanca la sessió de l'usuari actiu.
     */
    public void logout() {
        usuariActiu = null;
    }

    /**
     * Canvia el nom de l'usuari actiu.
     * @param nouNom Nou nom d'usuari.
     * @return Cert si s'ha pogut canviar, fals altrament.
     */
    public boolean canviarNomUsuari(String nouNom) {
        if (usuariActiu == null) return false;

        String nomAntic = usuariActiu.getNom();

        boolean canviat = gestor.canviarNomUsuari(usuariActiu, nouNom);
        if (!canviat) return false;

        usuariActiu.canviarNom(nouNom);

        ControladorDomini.getInstance().getRanquing().canviarNomUsuari(nomAntic, nouNom);
        ControladorDomini.getInstance().guardaRanquing();

        return true;
    }

    /**
     * Canvia la contrasenya de l'usuari actiu.
     * @param novaContrasenya Nova contrasenya.
     * @return Cert si s'ha pogut canviar, fals altrament.
     */
    public boolean canviarContrasenya(String novaContrasenya) {
        return gestor.canviarContrasenya(usuariActiu, novaContrasenya);
    }
}

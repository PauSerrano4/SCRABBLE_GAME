package presentacio;

import controller.ControladorDomini;
import exceptions.IntercanviFitxesException;
import exceptions.JugadaInvalidaException;

import javax.swing.*;
import util.*;
import model.*;
import java.util.*;
import java.io.IOException;
import java.io.File;
import exceptions.*;

/**
 * Classe ControladorPresentacio - Gestiona la lògica de presentació i la comunicació entre la UI i el domini.
 * S'encarrega de la gestió de vistes, usuaris, partides i la interacció amb el controlador de domini.
 */
public class ControladorPresentacio {

    // ---------- ATRIBUTS ----------
    private static ControladorPresentacio ctrlPresentacio;
    private ControladorDomini ctrlDomini;
    private VistaPartida vista;
    private VistaMenuPrincipal MenuPrincipal;
    private String usuari;
    private String usuari2;

    /**
     * Constructora de la classe ControladorPresentacio.
     * Inicialitza el controlador de domini.
     */
    public ControladorPresentacio() {
        ctrlDomini = new ControladorDomini();
    }

    // ---------- GETTERS ----------
    /**
     * Retorna la instància singleton del controlador de presentació.
     * @return Instància de ControladorPresentacio.
     */
    public static ControladorPresentacio getInstance() {
        if (ctrlPresentacio == null) {
            ctrlPresentacio = new ControladorPresentacio();
        }
        return ctrlPresentacio;
    }

    /**
     * Retorna els contrincants disponibles.
     * @return Array de Contrincant.
     */
    public Contrincant[] getContrincantsDisponibles() {
        return ctrlDomini.getContrincantsDisponibles();
    }

    /**
     * Retorna les dificultats disponibles.
     * @return Array de Dificultat.
     */
    public Dificultat[] getDificultatsDisponibles() {
        return ctrlDomini.getDificultatsDisponibles();
    }

    /**
     * Retorna la lletra d'una casella concreta.
     * @param fila Fila de la casella.
     * @param col Columna de la casella.
     * @return Caràcter de la casella.
     */
    public char getLletraCasella(int fila, int col) {
        return ctrlDomini.getLletraCasella(fila, col);
    }

    /**
     * Retorna els modes de joc disponibles.
     * @return Array de ModeJoc.
     */
    public ModeJoc[] getModesDisponibles() {
        return ctrlDomini.getModesDisponibles();
    }

    /**
     * Retorna el nombre de fitxes a la bossa.
     * @return Nombre de fitxes.
     */
    public int getNumFitxesBossa() {
        return ctrlDomini.getNumFitxesBossa();
    }

    

    /**
     * Retorna la llista de paraules d'un diccionari.
     * @param nomDiccionari Nom del diccionari.
     * @return Llista de paraules.
     */
    public List<String> getParaulesDiccionari(String nomDiccionari) {
        return ctrlDomini.getParaulesDeDiccionari(nomDiccionari);
    }

    /**
     * Retorna l'idioma actual.
     * @return Idioma.
     */
    public String getIdioma() {
        return ctrlDomini.getIdioma();
    }

    /**
     * Retorna la informació final de la partida.
     * @return Pair amb si la partida ha acabat i la llista de resultats.
     */
    public Pair<Boolean, List<Pair<String, Double>>> obtenirInfoFinal() {
        return ctrlDomini.obtenirInfoFinal();
    }

    /**
     * Retorna el temporitzador de la partida.
     * @return Temporitzador.
     */
    public Temporitzador getTemporitzador() {
        return ctrlDomini.getTemporitzador();
    }

    /**
     * Retorna l'historial de partides.
     * @return Llista d'entrades de l'historial.
     */
    public List<String> getHistorial() {
        return ctrlDomini.carregarHistorial();
    }

    /**
     * Retorna el controlador de domini.
     * @return Instància de ControladorDomini.
     */
    public ControladorDomini getCtrlDomini() {
        return ctrlDomini;
    }

    /**
     * Retorna la llista de partides guardades.
     * @return Llista de noms de fitxers de partides.
     */
    public List<String> listarPartidas() {
        return ctrlDomini.getPartidesGuardades();
    }

    // ---------- SETTERS ----------
    /**
     * Assigna la vista principal.
     * @param vp VistaMenuPrincipal.
     */
    public void setVista(VistaMenuPrincipal vp) {
        MenuPrincipal = vp;
    }

    /**
     * Assigna la vista de partida.
     * @param vp VistaPartida.
     */
    public void setVista2(VistaPartida vp) {
        vista = vp;
    }

    /**
     * Assigna l'usuari actiu i l'estableix al domini i al controlador d'usuaris.
     * @param nom nom usuari.
     */
    public void setUsuari(String nom) {
        this.usuari = nom;  
        
    }

    /**
     * Assigna el segon usuari (en cas de partida a dos jugadors).
     * @param nom nomusuari
     */
    public void setUsuari2(String nom) {
        this.usuari2 = nom;
    }

    // ---------- MÈTODES PRINCIPALS ----------
    /**
     * Inicialitza la vista de login.
     */
    public void inicialitzarVista() {
        VistaLogin login = new VistaLogin();
        login.setVisible(true);
    }

    /**
     * Defineix una partida amb els paràmetres especificats.
     * @param mode Mode de joc.
     * @param contrincant Tipus de contrincant.
     * @param dificultat Dificultat del contrincant.
     * @param idioma Idioma del diccionari.
     */
    public void definirPartida(ModeJoc mode, Contrincant contrincant, Dificultat dificultat, String idioma){
        if (contrincant == Contrincant.JUGADOR){
            VistaLoginSegonJugador vistaLogin = new VistaLoginSegonJugador(null);
            vistaLogin.mostrar();
        }
        
        boolean ok = ctrlDomini.definirPartida(mode, contrincant, dificultat, idioma, (contrincant == Contrincant.JUGADOR ? usuari2 : "BOT1"));
        if (!ok) {
            JOptionPane.showMessageDialog(
                vista,
                "No s'ha pogut crear la partida. Revisa les dades i torna-ho a provar.",
                "Error al crear partida",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // MILLORAT: Assegurar que es neteja la vista anterior abans de mostrar la nova
        mostraVistaJoc();   
        
        if (vista != null && ctrlDomini.getTemporitzador() != null && mode == ModeJoc.CONTRARRELLOTGE) {   
            ctrlDomini.getTemporitzador().setListener(vista);
            ctrlDomini.getTemporitzador().iniciar(); // <-- SOLO aquí
        }  
    }


    /**
     * Actualitza la vista de la partida i comprova si la partida ha finalitzat.
     */
    public void actualitzarVista() {
        vista.actualitzarTauler();

        Pair<Boolean, List<Pair<String, Double>>> resultats = obtenirInfoFinal();
        if (resultats.first) {
            String guanyador = ctrlDomini.obtenirNomMesPunts();
            mostrarResultatPartida(resultats.second, guanyador);
            ctrlDomini.acabarPartida(guanyador);
            vista.dispose();
        }

        vista.actualitzarEstatJugadors(
            ctrlDomini.getNomJugador(0),
            ctrlDomini.getPuntsJugador(0),
            ctrlDomini.getFitxesJugador(0),
            ctrlDomini.getNomJugador(1),
            ctrlDomini.getPuntsJugador(1),
            ctrlDomini.getFitxesJugador(1),
            ctrlDomini.getTornActual()
        );
        vista.actualitzarTemps(ctrlDomini.getTempsRestant());
    }

    /**
     * Passa el torn al següent jugador i actualitza la vista si hi ha bots.
     * @param i Índex del jugador al que es passa el torn.
     */
    public void passarTorn(int i) {
        ctrlDomini.passarTorn(i);
        jugarBotsIFerRefresh();
    }

    /**
     * Gestiona el bucle de bots consecutius i refresca la vista.
     */
    private void jugarBotsIFerRefresh() {
        while (ctrlDomini.getPartida().contraMaquina()) {
            ctrlDomini.jugarTorn();
        }
        actualitzarVista();
    }

    /**
     * Confirma la jugada humana i actualitza la vista.
     * @param posicions Llista de posicions de fitxes col·locades.
     */
    public void confirmarJugadaHumana(List<PosicioFitxa> posicions) throws JugadaInvalidaException{
        try {
            ctrlDomini.confirmarJugadaHumana(posicions);
            
            vista.buidarBufferTorn();
            actualitzarVista();
            while (ctrlDomini.getPartida().contraMaquina()) {
                ctrlDomini.jugarTorn();
                actualitzarVista();
            }
        } catch (JugadaInvalidaException e) {
            // Assegurar que la vista es neteja quan falla una jugada
            if (vista != null) {
                vista.buidarBufferTorn();
                vista.actualitzarTauler(); // Refresca el tauler per mostrar l'estat correcte
            }
            
            // Re-llançar l'excepció perquè la vista la pugui gestionar
            throw e;
        }
    }

    /**
     * Mostra la vista de la partida.
     */
    public void mostraVistaJoc() {
        if(vista == null){
            vista = new VistaPartida(ctrlPresentacio);
            setVista2(vista);
            vista.mostrar();
        }

        if (vista != null) {
            vista.actualitzarTauler();
            vista.actualitzarEstatJugadors(
               ctrlDomini.getNomJugador(0),
               ctrlDomini.getPuntsJugador(0),
               ctrlDomini.getFitxesJugador(0),
               ctrlDomini.getNomJugador(1),
               ctrlDomini.getPuntsJugador(1),
               ctrlDomini.getFitxesJugador(1),
               ctrlDomini.getTornActual()
           );
        }
        
    }

    
    /**
     * Mostra la vista de gestió de partida.
     */
    public void tornarGestioPartida() {
        // MILLORAT: Netejar vista de partida abans d'anar a gestió
        if (vista != null) {
            vista.dispose();
            vista = null;
        }
        
        VistaGestioPartida vgp = new VistaGestioPartida(ctrlPresentacio);
        vgp.mostrar();
    }

    /**
     * Mostra la vista d'estadístiques de l'usuari actiu.
     */
    public void mostrarEstadistiques() {
    new VistaEstadistiques();
    
}

    /**
     * Mostra la vista de rànquings.
     */
    public void mostrarVistaRanquings() {
        new VistaRanquings();
    }

    /**
     * Mostra la vista de diccionaris disponibles.
     */
    public void mostrarVistaDiccionaris() {
        List<String> disponibles = ctrlDomini.getNomDiccionaris();
        new VistaDiccionaris(disponibles);
    }

    /**
     * Mostra la vista de l'historial de partides.
     */
    public void mostrarVistaHistorial() {
        SwingUtilities.invokeLater(() -> {
            new VistaHistorial().setVisible(true);
        });
    }

    /**
     * Mostra la vista de resultat de la partida.
     * @param resultats Resultats de la partida.
     * @param guanyador Nom del guanyador.
     */
    public void mostrarResultatPartida(List<Pair<String, Double>> resultats, String guanyador) {
        VistaResultatPartida vistaResultatPartida = new VistaResultatPartida(resultats, guanyador, this);
        vistaResultatPartida.mostrar();
    }

    /**
     * Mostra el menú principal.
     */
    public void tornarMenuPrincipal() {
        // MILLORAT: Netejar completament abans de tornar al menú
        if (vista != null) {
            vista.dispose();
            vista = null;
        }
        
        // Pausar o finalitzar la partida actual si existeix
        if (ctrlDomini.hiHaPartida()) {
            ctrlDomini.pausarPartida();
        }
        
        VistaMenuPrincipal vistaMenu = new VistaMenuPrincipal();
        vistaMenu.mostrar();
        
        // Actualitzar la referència del menú principal
        MenuPrincipal = vistaMenu;
    }

    public void tempsAcabat(){
        String guanyador = ctrlDomini.obtenirNomMesPunts();
        ctrlDomini.acabarPartida(guanyador);
        mostrarResultatPartida(ctrlDomini.obtenirInfoFinal().second,guanyador );
        // MILLORAT: Netejar vista després d'abandonar
        if (vista != null) {
            vista.dispose();
            vista = null;
        }
    }

    /**
     * Torna al menú principal.
     */
    public void tornarAMenu() {
        // MILLORAT: Assegurar neteja abans de tornar al menú
        if (vista != null) {
            vista.dispose();
            vista = null;
        }
        
        if (MenuPrincipal != null) {
            MenuPrincipal.mostrar();
        } else {
            // Si no hi ha menú principal, crear-ne un de nou
            tornarMenuPrincipal();
        }
    }

    /**
     * Pausa la partida i mostra la vista de pausa.
     */
    public void pausarPartida() {
        ctrlDomini.pausarPartida();
        new VistaPausa(this).setVisible(true);
    }

    /**
     * Continua la partida després de la pausa.
     */
    public void continuarPartida() {
        ctrlDomini.continuarPartida();
        if (vista != null) {
            vista.mostrar();
        }
    }

    /**
     * Abandona la partida i mostra el resultat.
     */
    public void abandonarPartida() {
        String guanyador = ctrlDomini.getTornJugadorContrari();
        ctrlDomini.acabarPartida(guanyador);
        mostrarResultatPartida(ctrlDomini.obtenirInfoFinal().second, guanyador);
        
        // MILLORAT: Netejar vista després d'abandonar
        if (vista != null) {
            vista.dispose();
            vista = null;
        }
    }

    /**
     * Tanca la sessió de l'usuari actual i mostra la vista de login.
     */
    public void tancarSessio() {
        // MILLORAT: Netejar totes les vistes abans de tancar sessió
        if (vista != null) {
            vista.dispose();
            vista = null;
        }
        
        if (MenuPrincipal != null) {
            MenuPrincipal.dispose();
            MenuPrincipal = null;
        }
        
        // Pausar partida actual si existeix
        if (ctrlDomini.hiHaPartida()) {
            ctrlDomini.pausarPartida();
        }
        
        VistaLogin login = new VistaLogin();
        login.setVisible(true);
    }

    /**
     * Elimina el compte de l'usuari actiu.
     */
    public void eliminarCompte() {
        ctrlDomini.eliminarUsuariComplet(usuari);
        usuari = null;
    }
    
    /**
     * Retorna la llista de noms dels diccionaris disponibles.
     * @return Llista de noms de diccionaris.
     */
    public List<String> getNomDiccionaris() {
        return ctrlDomini.getNomDiccionaris();
    }

    /**
     * Guarda el diccionari i l'alfabet en fitxers.
     * @param nom Nom del diccionari.
     * @param paraules Llista de paraules.
     * @param alfabet Mapa de lletres a (quantitat, puntuació).
     * @throws IOException Si hi ha un error d'escriptura.
     */
    public void guardarFitxerDiccionari(String nom, List<String> paraules, Map<String, Pair<Integer, Integer>> alfabet) throws IOException, DiccionariJaExisteixException {
        ctrlDomini.guardarDiccionari(nom, paraules, alfabet);
    }

    /**
     * Guarda la partida actual amb el nom especificat.
     * @return Cert si s'ha pogut guardar, fals altrament.
     */
    public boolean guardarPartida() {
        String nomPartida = pedirNombrePartida();
        return ctrlDomini.guardarPartida(nomPartida);
    }

    /**
     * Carrega una partida des d'un fitxer.
     * @param fileName Nom del fitxer.
     */
    public void cargarPartida(String fileName) throws ErrorCarregarPartidaException {
        try {
            if (ctrlDomini.carregarPartida(fileName)) {
                JOptionPane.showMessageDialog(null, "Partida carregada correctament.");
                
                // MILLORAT: Netejar vista anterior abans de mostrar la nova partida
                if (vista != null) {
                    vista.dispose();
                    vista = null;
                }
                
                mostraVistaJoc();
                
                if (vista != null && ctrlDomini.getTemporitzador() != null) {
                    ctrlDomini.getTemporitzador().setListener(vista);
                    ctrlDomini.getTemporitzador().iniciar();
                }
            } else {
                throw new ErrorCarregarPartidaException("No s'ha pogut carregar la partida des del fitxer: " + fileName);
            }
        } catch (JugadorNoPertanyPartidaException e) {
            throw new ErrorCarregarPartidaException("Error: El jugador no pertany a aquesta partida - " + e.getMessage());
        }
    }

    /**
     * Llista les partides guardades.
     * @return Llista de noms de fitxers de partides.
     */
    public List<String> listarPartidasGuardades() {
        return ctrlDomini.getPartidesGuardades();
    }

    /**
     * Esborra una partida guardada.
     * @param fileName Nom del fitxer.
     * @return Cert si s'ha pogut esborrar, fals altrament.
     */
    public boolean borrarPartida(String fileName) {
        return ctrlDomini.eliminarPartida(fileName);
    }

    /**
     * Canvia el nom de l'usuari actiu.
     * @param nouNom Nou nom d'usuari.
     * @return Cert si s'ha pogut canviar, fals altrament.
     */
    public boolean canviarNomUsuari(String nouNom) {
        if (usuari == null) {
            
            //System.err.println("Error: Usuari actiu nul!");
            return false;
        }
        return ctrlDomini.canviarNomUsuari(nouNom);
        
    }

    /**
     * Canvia la contrasenya de l'usuari actiu.
     * @param novaPass Nova contrasenya.
     * @return Cert si s'ha pogut canviar, fals altrament.
     */
    public boolean canviarContrasenya(String novaPass) {
        return ctrlDomini.canviarContrasenya(novaPass);
    }

    /**
     * Comprova si la partida està inicialitzada.
     * @return Cert si la partida està inicialitzada, fals altrament.
     */
    public boolean partidaInicialitzada() {
        return ctrlDomini.hiHaPartida();
    }

    /**
     * Valida la contrasenya de l'usuari actiu.
     * @param contrasenya Contrasenya a validar.
     * @return Cert si la contrasenya és correcta, fals altrament.
     */
    public boolean validarContrasenya(String contrasenya) {
        return ctrlDomini.getUsuariActiu().getContrasenya().equals(contrasenya);
    }

    /**
     * Canvia les fitxes de l'usuari actiu.
     * @param lletres Llista de lletres a canviar.
     */
    public void canviarFitxes(List<Character> lletres) throws IntercanviFitxesException {
        ctrlDomini.canviarFitxes(lletres); //Pot llançar excepció
        vista.buidarBufferTorn();
        jugarBotsIFerRefresh();
    }

    /**
     * Mostra el diàleg per demanar el nom de la partida a guardar.
     * @return Nom de la partida.
     */
    public String pedirNombrePartida() {
        String nombre = null;
        while (nombre == null || nombre.trim().isEmpty()) {
            nombre = JOptionPane.showInputDialog(null, "Introdueix el nom per guardar la partida:",
                                                "Guardar Partida", JOptionPane.PLAIN_MESSAGE);
            if (nombre == null) {
                return null;
            }
            nombre = nombre.trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El nom no pot estar buit. Si us plau, introdueix un nom vàlid.",
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return nombre;
    }

    /**
     * Surt de l'aplicació.
     */
    public void sortir() {
        System.exit(0);
    }
}

package persistencia;

import util.Pair;
import exceptions.*;
import model.Partida;
import model.Ranquing;
import model.Usuari;
import controller.*;
import model.Diccionari;
import util.Temporitzador;
import util.DigrafMapper;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe ControladorPersistencia - Gestiona la persistència de dades del joc d'Scrabble.
 * Permet carregar i guardar diccionaris, alfabets, partides i historial, així com llistar i eliminar fitxers relacionats.
 * Implementa el patró Singleton per garantir una única instància.
 * @author Alexander de Jong
 */
public class ControladorPersistencia {

    // ---------- ATRIBUTS ----------
    private static final String DIR = "../DATA/partides/";
    private static final ControladorPersistencia INSTANCE = new ControladorPersistencia();
    private static final String RANQUING_FILE = "../DATA/ranquing.dat";
    private GestorDiccionari gestorDiccionari = new GestorDiccionari();
    private GestorPartida gestorPartida = new GestorPartida();
    private GestorUsuari gestorUsuari;
    private GestorRanquingEstadistiques gestorRanquingEstadistiques = new GestorRanquingEstadistiques();
    private GestorHistorial gestorHistorial = new GestorHistorial();
    
    
    // ---------- CONSTRUCTOR ----------
    /**
     * Constructor privat per implementar el patró Singleton.
     * Inicialitza els gestors de persistència.
     */
    public ControladorPersistencia() {
        try {
            gestorUsuari = new GestorUsuari();
        } catch (IOException e) {
            throw new RuntimeException("Error initializing GestorUsuari", e);
        }
    }

    // ---------- GETTERS ----------
    /**
     * Retorna la instància Singleton del controlador de persistència.
     * @return Instància única de controladorPersistencia.
     */
    public static ControladorPersistencia getInstance() {
        return INSTANCE;
    }
   // ---------------------- MÈTODES DE PERSISTÈNCIA D'USUARIS ----------

   /**
     * Carrega els usuaris i contrasenyes del fitxer de persistència.
     * @return Mapa amb nom d'usuari com a clau i contrasenya com a valor.
     * @throws IOException Si hi ha un error de lectura.
     */
    public Map<String, String> carregarUsuaris() throws IOException {
        return gestorUsuari.carregarUsuaris();
    }

    /**
     * Desa tots els usuaris i contrasenyes al fitxer de persistència.
     * @param usuaris Mapa amb nom d'usuari i contrasenya.
     * @throws IOException Si hi ha un error d'escriptura.
     */
    public void guardarUsuaris(Map<String, String> usuaris) throws IOException {
        gestorUsuari.guardarUsuaris(usuaris);
    }

    /**
     * Canvia el nom d'un usuari si el nou nom és vàlid i no existeix.
     * @param usuari Usuari a modificar.
     * @param nouNom Nou nom d'usuari.
     * @return Cert si s'ha pogut canviar, fals altrament.
     */
    public boolean canviarNomUsuari(Usuari usuari, String nouNom) {
        return gestorUsuari.canviarNomUsuari(usuari, nouNom);

    }

    /**
     * Canvia la contrasenya d'un usuari.
     * @param usuari Usuari a modificar.
     * @param novaContrasenya Nova contrasenya.
     * @return Cert si s'ha pogut canviar, fals altrament.
     */
    public boolean canviarContrasenya(Usuari usuari, String novaContrasenya) {
        return gestorUsuari.canviarContrasenya(usuari, novaContrasenya);
    }

     /**
     * Elimina un usuari del fitxer de persistència.
     * @param nomUsuari Nom de l'usuari a eliminar.
     */
    public void eliminarUsuariDeFitxer(String nomUsuari) {
        gestorUsuari.eliminarUsuariDeFitxer(nomUsuari);
    }
    

    // ---------- MÈTODES DE PERSISTÈNCIA DE DICCIONARIS I ALFABETS ----------
    /**
     * Carrega un diccionari des d'un fitxer de text.
     * @param nomDiccionari Nom del diccionari.
     * @return Llista de paraules del diccionari.
     * @throws IOException Si hi ha un error de lectura.
     */
    public List<String> carregarDiccionari(String nomDiccionari) throws IOException {
        return gestorDiccionari.carregarDiccionari(nomDiccionari);
    }

    /**
     * Carrega l'alfabet des d'un fitxer de text.
     * @param filepath Ruta del fitxer.
     * @return Mapa de lletres a (quantitat, puntuació).
     * @throws IOException Si hi ha un error de lectura.
     */
    public Map<String, Pair<Integer, Integer>> carregarAlfabet(String filepath) throws IOException {
        return gestorDiccionari.importarAlfabet(filepath);
    }

    /**
     * Desa un diccionari en un fitxer de text.
     * @param nom Nom del diccionari.
     * @param paraules Llista de paraules a desar.
     * @throws IOException Si hi ha un error d'escriptura.
     */
    public void guardarDiccionari(String nom, List<String> paraules) throws IOException, DiccionariJaExisteixException {
        File fitxer = new File("ruta/diccionaris/" + nom + ".txt");
        if (fitxer.exists()) {
            throw new DiccionariJaExisteixException("El diccionari '" + nom + "' ja existeix.");
        }
        gestorDiccionari.guardarDiccionari(nom, paraules);
    }

    /**
     * Desa l'alfabet en un fitxer de text.
     * @param nom Nom del fitxer
     * @param alfabet Mapa de lletres a (quantitat, puntuació).
     * @throws IOException Si hi ha un error d'escriptura.
     */
    public void guardarAlfabet(String nom, Map<String, Pair<Integer, Integer>> alfabet) throws IOException {
        gestorDiccionari.guardarAlfabet(nom, alfabet);
        
    }


    /**
     * Retorna una llista de noms de lletres (exclou fitxers d'usuaris i diccionaris).
     * @return Llista de noms de diccionaris.
     */
    public List<String> getNomesAlfabets() {
        return gestorDiccionari.getNomesAlfabets();
    }

    /**
     * Importa l'alfabet d'un fitxer i converteix les lletres segons l'idioma.
     * @param idioma Idioma del diccionari.
     * @return Mapa de lletres a (quantitat, puntuació).
     */
    public Map<String, Pair<Integer, Integer>> importarAlfabet(String idioma) {
        
        return gestorDiccionari.importarAlfabet(idioma);
    }

    /**
     * Importa paraules d'un diccionari i converteix dígrafs segons l'idioma.
     * @param idioma Idioma del diccionari.
     * @return Llista de paraules convertides.
     */
    public List<String> importarParaulesDiccionari(String idioma) {
        
        return gestorDiccionari.importarParaulesDiccionari(idioma);
    }

    /**
     * Elimina un diccionari i el seu fitxer de lletres.
     * @param nomDiccionari Nom del diccionari.
     * @throws Exception Si hi ha un error d'eliminació.
     */
    public void eliminarDiccionari(String nomDiccionari) throws Exception {
        gestorDiccionari.eliminarDiccionari(nomDiccionari);
        
    }

    /**
     * Retorna la llista de noms de diccionaris.
     * @return Llista de noms de diccionaris.
     */
    public List<String> getNomDiccionaris() {
        return gestorDiccionari.getNomDiccionaris();
    }

    // ---------- MÈTODES DE PERSISTÈNCIA DE PARTIDES ----------
    /**
     * Desa una partida serialitzada en un fitxer .ser.
     * @param partida Partida a desar.
     * @param filename Nom del fitxer.
     * @throws IOException Si hi ha un error d'escriptura.
     */
    public void savePartida(Partida partida, String filename) throws IOException {
        gestorPartida.savePartida(partida, filename);
    }

    /**
     * Carrega una partida des d'un fitxer .ser i re-inicialitza camps transients.
     * @param fileName Nom del fitxer.
     * @return Partida carregada.
     * @throws IOException Si hi ha un error de lectura.
     * @throws ClassNotFoundException Si la classe no es troba.
     */
    public Partida loadPartida(String fileName) throws IOException, ClassNotFoundException {
         return gestorPartida.loadPartida(fileName);
        
    }

    /**
     * Llista els fitxers .ser disponibles al directori de partides.
     * @return Llista de noms de fitxers de partides.
     * @throws IOException Si hi ha un error d'accés.
     */
    public List<String> listPartides() throws IOException {
        return gestorPartida.listPartides();
    }

    /**
     * Esborra el fitxer .ser corresponent a la partida.
     * @param fileName Nom del fitxer.
     * @return Cert si s'ha esborrat, fals altrament.
     * @throws IOException Si hi ha un error d'esborrat.
     */
    public boolean deletePartida(String fileName) throws IOException {
        return gestorPartida.deletePartida(fileName);
    }

    // ---------- MÈTODES D'HISTORIAL ----------
    /**
     * Desa una entrada a l'historial de partides.
     * @param entrada Text a afegir a l'historial.
     */
    public void guardarEntradaHistorial(String entrada){
        gestorHistorial.guardarEntradaHistorial(entrada);
    }

    /**
     * Carrega l'historial de partides des d'un fitxer de text.
     * @return Llista d'entrades de l'historial.
     */
    public List<String> carregarHistorial(){
        return gestorHistorial.carregarHistorial();
    }
    
    //----------------------- MÈTODES DE PERSISTÈNCIA D'ESTADÍSTIQUES I RÀNQUING ----------

    /**
     * Elimina les estadístiques d'un usuari.
     * @param nomUsuari Nom de l'usuari.
     */
    public void eliminarEstadistiquesUsuari(String nomUsuari) {
        try{
        gestorRanquingEstadistiques.eliminarEstadistiquesUsuari(nomUsuari);
        } catch (IOException e) {
            throw new RuntimeException("Error eliminant les estadístiques de l'usuari: " + nomUsuari, e);
        }
    }

    /**
     * Elimina el fitxer de rànquing d'un usuari.
     * @param ranquing Rànquing a eliminar.
     */
    public void eliminarFitxerRanquingUsuari(Ranquing ranquing)  {
        gestorRanquingEstadistiques.eliminarFitxerRanquingUsuari(ranquing);
    }

    /**
     * Guarda el rànquing al fitxer.
     * @param ranquing Rànquing a desar.
     */
    public void guardaRanquing(Ranquing ranquing)  {
        gestorRanquingEstadistiques.guardaRanquing(ranquing);
    }

    /**
     * Carrega el rànquing des del fitxer.
     * @return Ranquing carregat o nou si no existeix.
     */
    public Ranquing carregaRanquing()  {
        return gestorRanquingEstadistiques.carregaRanquing();
    }
    

}

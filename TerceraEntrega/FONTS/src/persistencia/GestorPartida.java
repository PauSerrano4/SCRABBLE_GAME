
package persistencia;

import util.Pair;
import model.Partida;
import model.Ranquing;
import controller.*;
import model.Diccionari;
import util.Temporitzador;
import util.DigrafMapper;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;


/**
 * GestorPartida és la classe encarregada de gestionar la persistència de les partides del joc.
 * Proporciona funcionalitats per desar, carregar, llistar i esborrar partides serialitzades
 * en fitxers al sistema de fitxers. Les partides es guarden en format binari (.ser) dins
 * d'un directori específic. 
 */
public class GestorPartida {

    private static final String DIR = "../DATA/partides/";

        /**
     * Desa una partida serialitzada en un fitxer .ser.
     * @param partida Partida a desar.
     * @param filename Nom del fitxer.
     * @throws IOException Si hi ha un error d'escriptura.
     */
    public void savePartida(Partida partida, String filename) throws IOException {
        Files.createDirectories(Path.of(DIR));
        String file = DIR + filename + ".ser";
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(file)))) {
            out.writeObject(partida);
        }
    }

    /**
     * Carrega una partida des d'un fitxer .ser i re-inicialitza camps transients.
     * @param fileName Nom del fitxer.
     * @return Partida carregada.
     * @throws IOException Si hi ha un error de lectura.
     * @throws ClassNotFoundException Si la classe no es troba.
     */
    public Partida loadPartida(String fileName)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(
                        new FileInputStream(DIR + fileName)))) {
            return (Partida) in.readObject();
        }
    }

    /**
     * Llista els fitxers .ser disponibles al directori de partides.
     * @return Llista de noms de fitxers de partides.
     * @throws IOException Si hi ha un error d'accés.
     */
    public List<String> listPartides() throws IOException {
        return Files.list(Path.of(DIR))
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(n -> n.endsWith(".ser"))
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Esborra el fitxer .ser corresponent a la partida.
     * @param fileName Nom del fitxer.
     * @return Cert si s'ha esborrat, fals altrament.
     * @throws IOException Si hi ha un error d'esborrat.
     */
    public boolean deletePartida(String fileName) throws IOException {
        return Files.deleteIfExists(Path.of(DIR, fileName));
    }
}

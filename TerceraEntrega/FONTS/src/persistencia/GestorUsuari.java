package persistencia;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import model.Usuari;

/**
 * Classe GestorUsuari - Gestiona la persistència dels usuaris del sistema.
 * Permet carregar, guardar i modificar usuaris i contrasenyes en un fitxer de text.
 */
public class GestorUsuari {

    // ---------- ATRIBUTS ----------
    private static final String FILE_PATH = "../DATA/usuaris.txt";

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe GestorUsuari.
     * Crea el fitxer i la carpeta de dades si no existeixen.
     * @throws IOException Si hi ha un error de creació de fitxer o carpeta.
     */
    public GestorUsuari() throws IOException {
        File file = new File(FILE_PATH);
        File parent = file.getParentFile();
        if (!parent.exists()) parent.mkdirs();   // crea la carpeta DATA/ si no existeix
        if (!file.exists()) file.createNewFile(); // crea l'arxiu si no existeix
    }

    // ---------- MÈTODES ----------
    /**
     * Carrega els usuaris i contrasenyes del fitxer de persistència.
     * @return Mapa amb nom d'usuari com a clau i contrasenya com a valor.
     * @throws IOException Si hi ha un error de lectura.
     */
    public Map<String, String> carregarUsuaris() throws IOException {
        Map<String, String> usuaris = new HashMap<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) return usuaris;

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String linia = sc.nextLine().trim();
                if (linia.contains(":")) {
                    String[] parts = linia.split(":");
                    if (parts.length == 2) {
                        usuaris.put(parts[0], parts[1]);
                    }
                }
            }
        }
        return usuaris;
    }

    /**
     * Desa tots els usuaris i contrasenyes al fitxer de persistència.
     * @param usuaris Mapa amb nom d'usuari i contrasenya.
     * @throws IOException Si hi ha un error d'escriptura.
     */
    public void guardarUsuaris(Map<String, String> usuaris) throws IOException {
        try (FileWriter fw = new FileWriter(FILE_PATH)) {
            for (Map.Entry<String, String> entry : usuaris.entrySet()) {
                fw.write(entry.getKey() + ":" + entry.getValue() + System.lineSeparator());
            }
        }
    }

    /**
     * Canvia el nom d'un usuari si el nou nom és vàlid i no existeix.
     * @param usuari Usuari a modificar.
     * @param nouNom Nou nom d'usuari.
     * @return Cert si s'ha pogut canviar, fals altrament.
     */
    public boolean canviarNomUsuari(Usuari usuari, String nouNom) {
        File fitxer = new File(FILE_PATH);
        Map<String, String> usuaris;

        try {
            usuaris = carregarUsuaris();

            // Comprovem que el nom no comenci per "BOT" (reservat pel joc de la màquina)
            if (nouNom.startsWith("BOT")) return false; // Es podria llençar una excepció

            // Comprovem si ja existeix aquest nom
            if (usuaris.containsKey(nouNom)) return false; // Es podria llençar una excepció

            String contrasenya = usuaris.remove(usuari.getNom()); // eliminem el nom antic
            if (contrasenya == null) return false;

            usuaris.put(nouNom, contrasenya); // afegim el nou nom amb la mateixa contrasenya
            guardarUsuaris(usuaris); // sobrescrivim el fitxer

            usuari.canviarNom(nouNom); // actualitzem a memòria
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Canvia la contrasenya d'un usuari.
     * @param usuari Usuari a modificar.
     * @param novaContrasenya Nova contrasenya.
     * @return Cert si s'ha pogut canviar, fals altrament.
     */
    public boolean canviarContrasenya(Usuari usuari, String novaContrasenya) {
        try {
            Map<String, String> usuaris = carregarUsuaris();

            if (!usuaris.containsKey(usuari.getNom())) return false;

            usuaris.put(usuari.getNom(), novaContrasenya);
            guardarUsuaris(usuaris);

            usuari.canviarContrasenya(novaContrasenya); // actualitzar a memòria també
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un usuari del fitxer de persistència.
     * @param nomUsuari Nom de l'usuari a eliminar.
     */
    public void eliminarUsuariDeFitxer(String nomUsuari) {
        File inputFile = new File("../DATA/usuaris.txt");
        File tempFile = new File("../DATA/usuaris_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.startsWith(nomUsuari + ":")) {
                    writer.write(linea);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Error eliminant usuari del fitxer: " + e.getMessage());
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }   
}
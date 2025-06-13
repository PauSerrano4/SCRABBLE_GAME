package persistencia;

import java.io.*;
import model.Ranquing;

    
    
/**
 * Classe GestorRanquingEstadistiques.
 * S'encarrega de gestionar la persistència del rànquing general i de les estadístiques individuals dels usuaris.
 * Proporciona funcionalitats per guardar, carregar i eliminar dades relacionades amb el rànquing i les estadístiques.
 */
public class GestorRanquingEstadistiques {
    private static final String RANQUING_FILE = "../DATA/ranquing.dat";

    public GestorRanquingEstadistiques() {
        
    }
    
    /**
     * Elimina les estadístiques d'un usuari.
     * @param nomUsuari Nom de l'usuari.
     * @throws IOException Si hi ha un error eliminant el fitxer.
     */
    public void eliminarEstadistiquesUsuari(String nomUsuari) throws IOException {
        File estadistiques = new File("../DATA/estadistiques_" + nomUsuari + ".ser");
        if (estadistiques.exists()) {
            if (!estadistiques.delete()) {
                throw new IOException("No s'ha pogut eliminar el fitxer d'estadístiques de l'usuari: " + nomUsuari);
            }
        }
    }

    /**
     * Elimina el fitxer de rànquing d'un usuari.
     * @param ranquing Rànquing a eliminar.
     */
    public void eliminarFitxerRanquingUsuari(Ranquing ranquing) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("../DATA/ranquing.dat"))) {
        oos.writeObject(ranquing);
    } catch (IOException e) {
        System.err.println("Error escrivint el rànquing al fitxer: " + e.getMessage());
    }
    }

    /**
     * Guarda el rànquing al fitxer.
     * @param ranquing Rànquing a guardar.
     */
    public void guardaRanquing(Ranquing ranquing) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RANQUING_FILE))) {
            oos.writeObject(ranquing);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega el rànquing des del fitxer.
     * @return Ranquing carregat o nou si no existeix.
     */
    public Ranquing carregaRanquing() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RANQUING_FILE))) {
            return (Ranquing) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new Ranquing();
        }
    }
}
package persistencia;

import util.Pair;
import util.DigrafMapper;
import java.io.*;
import java.util.*;

/**
 * Classe GestorDiccionari.
 * S'encarrega de gestionar la persistència dels diccionaris i alfabets del sistema.
 * Proporciona funcionalitats per carregar, guardar, eliminar i obtenir informació sobre diccionaris i alfabets,
 * així com importar-los per a la seva utilització interna.
 * Aquesta classe encapsula tota la lògica relacionada amb la gestió d'arxius de diccionaris i alfabets.
 * 
 */
public class GestorDiccionari {

    private Map<String,String> dictPaths;
    private Map<String,String> alphaPaths;

    /**
     * Constructor de la classe GestorDiccionari.
     * Inicialitza els mapes amb les rutes dels diccionaris i alfabets.
     */
    public GestorDiccionari() {
        inicialitzaRutes();
    }


    /**
     * Inicialitza els mapes de rutes dels arxius de diccionaris i lletres
     */
    private void inicialitzaRutes() {
        dictPaths  = new HashMap<>();
        alphaPaths = new HashMap<>();
        File carpeta = new File("../DATA/");
        for (File f : carpeta.listFiles((dir,name)->name.endsWith(".txt"))) {
            String nom = f.getName().replace(".txt","");
            if (nom.startsWith("letras")) {
                alphaPaths.put(nom.toLowerCase().replace("letras",""), f.getPath());
            } else if (!nom.equals("usuaris")) {
                dictPaths.put(nom.toLowerCase(), f.getPath());
            }
        }
    }

    /**
     * Retorna la ruta de l'arxiu de diccionari de l'idioma seleccionat
     * @param idioma
     * @return String amb la ruta de l'arxiu del diccionari
     */
    public String getDictPath(String idioma) {
        return dictPaths.get(idioma.toLowerCase());
    }
    
    /**
     * Retorna la ruta de l'arxiu de l'alfabet de l'idioma seleccionat
     * @param idioma
     * @return String amb la ruta de l'arxiu de l'alfabet
     */
    public String getAlfabetPath(String idioma) {
        return alphaPaths.get(idioma.toLowerCase());
    }
    
    /**
     * Carrega un diccionari des d'un fitxer.
     * El fitxer ha de tenir una paraula per línia.
     * @param nomDiccionari Nom del diccionari a carregar (sense extensió).
     * @return Llista de paraules del diccionari.
     * @throws IOException Si hi ha un error llegint el fitxer.
     */
    public List<String> carregarDiccionari(String nomDiccionari) throws IOException {
        String filepath = "../DATA/" + nomDiccionari + ".txt";
        List<String> paraules = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(filepath))) {
            while (sc.hasNextLine()) {
                String paraula = sc.nextLine().trim();
                if (!paraula.isEmpty()) paraules.add(paraula);
            }
        }
        return paraules;
    }

    /**
     * Carrega l'alfabet des d'un fitxer.
     * El fitxer ha de tenir el format: lletra quantitat puntuacio
     * @param filepath Ruta al fitxer de l'alfabet.
     * @return Un mapa amb les lletres com a claus i un parell (quantitat, puntuació) com a valors.
     * @throws IOException Si hi ha un error llegint el fitxer.
     */
    public Map<String, Pair<Integer, Integer>> carregarAlfabet(String filepath) throws IOException {
        Map<String, Pair<Integer, Integer>> alfabet = new HashMap<>();
        try (Scanner scanner = new Scanner(new File(filepath))) {
            while (scanner.hasNextLine()) {
                String linia = scanner.nextLine().trim();
                if (linia.isEmpty()) continue;

                String[] parts = linia.split("\\s+");
                if (parts.length != 3) {
                    System.out.println("Format alfabet Incorrecte");
                    throw new IOException("Format incorrecte a la línia: " + linia);
                }

                String lletra = parts[0];
                int quantitat = Integer.parseInt(parts[1]);
                int puntuacio = Integer.parseInt(parts[2]);
                alfabet.put(lletra, new Pair<>(quantitat, puntuacio));
            }
        }
        return alfabet;
    }

    /**
     * Guarda un diccionari en un fitxer.
     * Cada paraula es guarda en una línia separada.
     * @param nom Nom del diccionari (sense extensió).
     * @param paraules Llista de paraules a guardar.
     * @throws IOException Si hi ha un error escrivint al fitxer.
     */
    public void guardarDiccionari(String nom, List<String> paraules) throws IOException {
        String filepath = "../DATA/" + nom + ".txt";
        try (FileWriter fw = new FileWriter(filepath)) {
            for (String paraula : paraules) {
                fw.write(paraula + System.lineSeparator());
            }
        }
    }

    /**
     * Guarda l'alfabet en un fitxer.
     * Cada lletra es guarda amb la seva quantitat i puntuació en una línia separada.
     * @param nom Nom del diccionari (sense extensió).
     * @param alfabet Mapa amb les lletres, quantitats i puntuacions.
     * @throws IOException Si hi ha un error escrivint al fitxer.
     */
    public void guardarAlfabet(String nom, Map<String, Pair<Integer, Integer>> alfabet) throws IOException {
        
        String filepath = "../DATA/letras" + nom.toUpperCase() + ".txt";
        try (FileWriter fw = new FileWriter(filepath)) {
            for (Map.Entry<String, Pair<Integer, Integer>> entry : alfabet.entrySet()) {
                String lletra = entry.getKey();
                int quantitat = entry.getValue().first;
                int puntuacio = entry.getValue().second;
                fw.write(lletra + " " + quantitat + " " + puntuacio + System.lineSeparator());
            }
        }
    }
    
    /**
     * Elimina un diccionari i el seu alfabet associat.
     * @param nomDiccionari Nom del diccionari a eliminar (sense extensió).
     * @throws Exception Si hi ha un error eliminant els fitxers.
     */
    public void eliminarDiccionari(String nomDiccionari) throws Exception {
        File fitxer = new File("../DATA/" + nomDiccionari + ".txt");
        if (!fitxer.exists()) throw new FileNotFoundException("El diccionari no existeix.");
        if (!fitxer.delete()) throw new IOException("No s'ha pogut eliminar el diccionari.");

        String enMajuscules = nomDiccionari.toUpperCase();
        File fitxerLletres = new File("../DATA/letras" + enMajuscules + ".txt");
        if (fitxerLletres.exists()) {
            if (!fitxerLletres.delete()) throw new IOException("No s'ha pogut eliminar el fitxer de lletres.");
        }

        
    }

    /**
     * Retorna una llista amb els noms dels alfabets disponibles.
     * Els noms són els fitxers que comencen per "letras" i acaben amb ".txt".
     * @return Llista de noms d'alfabets.
     */
    public List<String> getNomesAlfabets() {
        File carpeta = new File("../DATA/");
        File[] fitxers = carpeta.listFiles((dir, name) -> name.endsWith(".txt") && name.startsWith("letras"));

        List<String> noms = new ArrayList<>();
        if (fitxers != null) {
            for (File f : fitxers) {
                noms.add(f.getName().replace(".txt", ""));
            }
        }
        return noms;
    }

    /**
     * Importa l'alfabet d'un idioma específic des d'un fitxer.
     * El fitxer ha de tenir el format: lletra quantitat puntuacio
     * @param idioma Idioma del qual es vol importar l'alfabet.
     * @return Un mapa amb les lletres com a claus i un parell (quantitat, puntuació) com a valors.
     */
    public Map<String, Pair<Integer, Integer>> importarAlfabet(String idioma) {
        
        // Busquem la ruta al fitxer corresponent
        idioma = idioma.toLowerCase();
        String alfabetFile   = getAlfabetPath(idioma);
        if (alfabetFile == null) {
            throw new IllegalArgumentException("Idioma desconegut: " + idioma);
        }

        // Importem el fitxer 
        Map<String, Pair<Integer, Integer>> alfabet = new HashMap<>();
        try {
            Scanner sc1 = new Scanner(new File(alfabetFile));
            while (sc1.hasNextLine()) {
                String linia = sc1.nextLine().trim();
                if (linia.isEmpty()) continue;

                String[] parts = linia.split("\\s+");
                if (parts.length == 3) {
                    String lletraOriginal = parts[0];
                    String lletraUnificada = DigrafMapper.convertirLletra(lletraOriginal, idioma);

                    int quantitat = Integer.parseInt(parts[1]);
                    int puntuacio = Integer.parseInt(parts[2]);

                    alfabet.put(lletraUnificada, new Pair<>(quantitat, puntuacio));
                }
            }
            sc1.close();
        } catch (Exception e) {
            System.err.println("Error important l'alfabet: " + e.getMessage());
            e.printStackTrace();
        }
        return alfabet;
    }

    /**
     * Importa les paraules d'un diccionari d'un idioma específic des d'un fitxer.
     * Cada paraula es guarda en una línia separada.
     * @param idioma Idioma del qual es vol importar el diccionari.
     * @return Llista de paraules del diccionari importat.
     */
    public List<String> importarParaulesDiccionari(String idioma) {
        
        // Busquem la ruta al fitxer corresponent
        idioma = idioma.toLowerCase();
        String diccionariFile = getDictPath(idioma);
        if (diccionariFile == null) {
            throw new IllegalArgumentException("Idioma desconegut: " + idioma);
        }

        // Importem el fitxer 
        List<String> paraules = new ArrayList<>();
        try {
            Scanner sc2 = new Scanner(new File(diccionariFile));
            while (sc2.hasNextLine()) {
                String paraula = sc2.nextLine().trim();
                if (!paraula.isEmpty()) {
                    String convertida = DigrafMapper.convertirParaula(paraula, idioma);
                    paraules.add(convertida);
                }
            }
            sc2.close();
        } catch (Exception e) {
            System.err.println("Error important paraules del diccionari: " + e.getMessage());
        }
        return paraules;
    }

    /**
     * Retorna una llista amb els noms dels diccionaris disponibles.
     * Els noms són els fitxers que acaben amb ".txt" i no comencen per "letras", "usuaris" o "historial".
     * @return Llista de noms de diccionaris.
     */
    public List<String> getNomDiccionaris() {
        File carpeta = new File("../DATA/");
        File[] arxius = carpeta.listFiles((dir, name) -> name.endsWith(".txt") && !name.startsWith("index") && !name.startsWith("letras") && !name.startsWith("usuaris") && !name.startsWith("historial"));
        List<String> noms = new ArrayList<>();
        if (arxius != null) {
            for (File f : arxius) {
                noms.add(f.getName().replace(".txt", ""));
            }
        }
        return noms;
    }

   

    
}

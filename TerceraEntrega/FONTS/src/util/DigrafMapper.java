package util;

import java.util.*;

/** 
 * Classe DigrafMapper.java
 * Classe auxiliar per la gestió de dígrafs dels diccionaris. 
 * S'encarrega de guardar tots els dígrafs de cada idioma del joc Scrabble i convertir-los en un format que facilita la gestió interna de les paraules.
 * @author Pau Serrano Sanz | pau.serrano.sanz@estudiantat.upc.edu
 */

public class DigrafMapper {

    private static final Map<String, Map<String, String>> digrafsPerIdioma = new HashMap<>();

    static {
        Map<String, String> catala = Map.of(
            "NY", "n",
            "L·L", "·"
        );
        Map<String, String> castella = Map.of(
            "CH", "c",
            "LL", "l",
            "RR", "r"
        );
        Map<String, String> angles = Map.of(); // No té dígrafs especials

        digrafsPerIdioma.put("catalan", catala);
        digrafsPerIdioma.put("castellano", castella);
        digrafsPerIdioma.put("english", angles);
    }

    /**
     * Converteix una paraula substituint els dígrafs propis d'un idioma per caràcters unificats.
     * <p>
     * Aquesta conversió permet gestionar internament les paraules de manera consistent,
     * especialment per representar lletres compostes com "NY" o "L·L".
     *
     * @param paraula la paraula original a convertir
     * @param idioma l’idioma al qual pertany la paraula (ex: "catalan", "castellano", "english")
     * @return la paraula amb els dígrafs convertits a caràcters unificats
     */
    public static String convertirParaula(String paraula, String idioma) {
        Map<String, String> digrafs = digrafsPerIdioma.getOrDefault(idioma, Map.of());
        for (Map.Entry<String, String> e : digrafs.entrySet()) {
            paraula = paraula.replace(e.getKey(), e.getValue());
        }
        return paraula;
    }


    /**
     * Aquesta funció pren una paraula que conté caràcters unificats (com '·' o 'n') utilitzats
     * internament per representar dígrafs, i retorna la forma original amb els dígrafs restaurats
     * segons l’idioma indicat (per exemple, '·' → "L·L", 'n' → "NY").
     *
     * @param paraula la paraula amb caràcters unificats
     * @param idioma l’idioma del diccionari (ex: "catalan", "castellano", "english")
     * @return la paraula original amb els dígrafs des-substituïts
     */
    public static String desferConversioParaula(String paraula, String idioma) {
        Map<String, String> digrafs = digrafsPerIdioma.getOrDefault(idioma, Map.of());
        for (Map.Entry<String, String> e : digrafs.entrySet()) {
            paraula = paraula.replace(e.getValue(), e.getKey());
        }
        return paraula;
    }

    /**
     * Aplica la conversió de dígrafs a una llista de paraules.
     *
     * @param paraules la llista de paraules originals
     * @param idioma l’idioma de les paraules
     * @return una nova llista amb les paraules convertides
     */
    public static List<String> convertirLlistaParaules(List<String> paraules, String idioma) {
        List<String> resultat = new ArrayList<>();
        for (String p : paraules) {
            resultat.add(convertirParaula(p, idioma));
        }
        return resultat;
    }


    /**
     * Converteix una lletra individual a la seva representació unificada si és un dígraf.
     *
     * @param lletra la lletra o dígraf a convertir
     * @param idioma l’idioma al qual pertany la lletra
     * @return la representació unificada del dígraf, o la lletra original si no és un dígraf
     */
    public static String convertirLletra(String lletra, String idioma) {
        Map<String, String> digrafs = digrafsPerIdioma.getOrDefault(idioma, Map.of());
        return digrafs.getOrDefault(lletra, lletra);
    }

    /**
     * Reverteix la conversió d’una lletra unificada a la seva forma original com a dígraf.
     *
     * @param lletraUnificada la lletra o símbol que representa un dígraf (ex: '·', 'n')
     * @param idioma l’idioma al qual pertany la lletra
     * @return la representació original del dígraf (ex: "L·L", "NY"), o la lletra si no és cap dígraf
     */
    public static String desferConversio(String lletraUnificada, String idioma) {
        Map<String, String> digrafs = digrafsPerIdioma.getOrDefault(idioma, Map.of());
        for (Map.Entry<String, String> e : digrafs.entrySet()) {
            if (e.getValue().equals(lletraUnificada)) return e.getKey();
        }
        return lletraUnificada;
    }

    
    /**
     * Retorna el mapa dels dígrafs definits per a un idioma concret.
     *
     * @param idioma l’idioma pel qual es vol obtenir el mapa de dígrafs
     * @return el mapa de dígrafs (original → unificat) per l’idioma especificat
     */
    public static Map<String, String> getDigrafsPerIdioma(String idioma) {
        return digrafsPerIdioma.getOrDefault(idioma, Map.of());
    }
}

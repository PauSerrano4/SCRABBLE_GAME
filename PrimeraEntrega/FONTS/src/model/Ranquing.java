package model;
import java.util.*;

/** 
 * Classe que conté la informació de tots els rànquings
 * @autor Ferran Blanchart Reyes | ferran.blanchart@estudiantat.upc.edu
 */

public class Ranquing {

    /** guarda els rànquings del sistema */
    private Map<String, Map<String, Float>> classificacio = new HashMap<>();

    //----------CONSULTORES----------

    /** retorna els punts que te un usuari en un rànquing concret */
    public Float getPuntsUsuari(String id, String tipus) {
        Map<String, Float> ranquingMode = classificacio.get(tipus);
        if (ranquingMode == null) return 0f;
        return ranquingMode.getOrDefault(id, 0f);
    }

    /** mostra el rànquing del tipus indicat a la seva capcelera */
    public void mostraRanquing(String tipus) {

        Map<String, Float> ranquingMode = classificacio.get(tipus);

        if (ranquingMode == null || ranquingMode.isEmpty()) {
            System.out.println("No hi ha dades per al mode: " + tipus);
            return;
        }

        List<Map.Entry<String, Float>> llista = new ArrayList<>(ranquingMode.entrySet());
        llista.sort((a, b) -> Float.compare(b.getValue(), a.getValue()));

        int pos = 1;
        for (int i = 0; i < Math.min(10, llista.size()); i++) {
            Map.Entry<String, Float> entrada = llista.get(i);
            System.out.println(pos + " - " + entrada.getKey() + " (" + entrada.getValue() + " punts)");
            pos++;
        }
    }

    //----------MODIFICADORES----------

    /** elimina l'usuari que té l'id indicat a la capcelera de tots els rànquings on apareix */
    public void eliminarUsuari(String id) {
        for (Map<String, Float> ranquingMode : classificacio.values()) {
            ranquingMode.remove(id);
        }
    }

    /** actualitza la putuacio de l'usuari amb l'id indicat a la capcelera al mode del tipus indicat
     * a la capcelera afegint-li la puntuació obtinguda i indicada a la capcelera
     */
    public void actualitzarRanquing(String id, String tipus, Float punts) {
        Map<String, Float> ranquingMode = classificacio.getOrDefault(tipus, new HashMap<>());

        Float puntuacioActual = ranquingMode.getOrDefault(id, 0f);
        ranquingMode.put(id, puntuacioActual + punts);

        classificacio.put(tipus, ranquingMode);
    }
}

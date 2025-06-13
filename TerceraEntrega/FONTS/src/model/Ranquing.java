package model;

import java.util.*;
import java.io.Serializable;

/**
 * Classe que conté la informació de tots els rànquings.
 * Gestiona la classificació i puntuació dels usuaris per diferents tipus de rànquing.
 * @author Ferran Blanchart Reyes | ferran.blanchart@estudiantat.upc.edu
 */
public class Ranquing implements Serializable {

    // ---------- ATRIBUTS ----------
    /** Guarda els rànquings ordenats per tipus */
    private Map<String, TreeSet<EntradaRanquing>> classificacio = new HashMap<>();

    /** Accés directe a cada entrada per poder modificar-la eficientment */
    private Map<String, Map<String, EntradaRanquing>> accesDirecte = new HashMap<>();

    // ---------- CLASSE INTERNA ----------
    /**
     * Classe interna per representar una entrada al rànquing.
     */
    private static class EntradaRanquing implements Comparable<EntradaRanquing>, Serializable {
        String idUsuari;
        Float punts;

        EntradaRanquing(String idUsuari, Float punts) {
            this.idUsuari = idUsuari;
            this.punts = punts;
        }

        @Override
        public int compareTo(EntradaRanquing other) {
            int comp = Float.compare(other.punts, this.punts); // ordenació descendent
            if (comp == 0) {
                return this.idUsuari.compareTo(other.idUsuari); // desempat per id
            }
            return comp;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EntradaRanquing)) return false;
            EntradaRanquing that = (EntradaRanquing) o;
            return Objects.equals(idUsuari, that.idUsuari);
        }

        @Override
        public int hashCode() {
            return Objects.hash(idUsuari);
        }
    }

    // ---------- GETTERS / CONSULTORS ----------

    /**
     * Retorna els punts que té un usuari en un rànquing concret.
     * @param id Identificador de l'usuari
     * @param tipus Tipus de rànquing
     * @return Punts de l'usuari en el rànquing especificat
     */
    public Float getPuntsUsuari(String id, String tipus) {
        Map<String, EntradaRanquing> directe = accesDirecte.get(tipus);
        if (directe == null) return 0f;
        EntradaRanquing entrada = directe.get(id);
        return entrada != null ? entrada.punts : 0;
    }

    /**
     * Mostra el rànquing del tipus indicat amb els 10 primers.
     * @param tipus Tipus de rànquing
     * @return Llista de les 10 primeres posicions del rànquing
     */
    public List<String> mostraRanquing(String tipus) {
        TreeSet<EntradaRanquing> ranquing = classificacio.get(tipus);
        List<String> resultat = new ArrayList<>();

        if (ranquing == null || ranquing.isEmpty()) return resultat;

        int pos = 1;
        for (EntradaRanquing entrada : ranquing) {
            if (pos > 10) break;
            resultat.add(pos + " - " + entrada.idUsuari + " (" + entrada.punts + " punts)");
            pos++;
        }
        return resultat;
    }

    // ---------- MODIFICADORS ----------

    /**
     * Elimina l'usuari amb l'id indicat de tots els rànquings.
     * @param id Identificador de l'usuari a eliminar
     */
    public void eliminarUsuari(String id) {
        for (String tipus : classificacio.keySet()) {
            Map<String, EntradaRanquing> directe = accesDirecte.get(tipus);
            if (directe != null && directe.containsKey(id)) {
                EntradaRanquing entrada = directe.remove(id);
                classificacio.get(tipus).remove(entrada);
            }
        }
    }

    /**
     * Actualitza la puntuació d'un usuari per un tipus de joc afegint-hi la nova puntuació.
     * @param id Identificador de l'usuari
     * @param tipus Tipus de rànquing
     * @param punts Puntuació a afegir o establir
     */
    public void actualitzarRanquing(String id, String tipus, Float punts) {
        classificacio.putIfAbsent(tipus, new TreeSet<>());
        accesDirecte.putIfAbsent(tipus, new HashMap<>());

        TreeSet<EntradaRanquing> ranquing = classificacio.get(tipus);
        Map<String, EntradaRanquing> directe = accesDirecte.get(tipus);

        EntradaRanquing entrada = directe.get(id);
        if (tipus.equals("percentatge") || tipus.equals("record")) {
            if (entrada != null) {
                ranquing.remove(entrada);
                entrada.punts = punts;
            } else {
                entrada = new EntradaRanquing(id, punts);
                directe.put(id, entrada);
            }
        }
        else {
            if (entrada != null) {
                ranquing.remove(entrada);
                entrada.punts += punts;
            } else {
                entrada = new EntradaRanquing(id, punts);
                directe.put(id, entrada);
            }
        }
        ranquing.add(entrada);
    }

    /**
     * Canvia el nom d'usuari en tots els rànquings.
     * @param nomAntic Nom antic de l'usuari
     * @param nomNou Nou nom de l'usuari
     */
    public void canviarNomUsuari(String nomAntic, String nomNou) {
        for (String tipus : classificacio.keySet()) {
            Map<String, EntradaRanquing> directe = accesDirecte.get(tipus);
            if (directe != null && directe.containsKey(nomAntic)) {
                EntradaRanquing entradaAntiga = directe.remove(nomAntic);
                classificacio.get(tipus).remove(entradaAntiga);

                EntradaRanquing novaEntrada = new EntradaRanquing(nomNou, entradaAntiga.punts);
                directe.put(nomNou, novaEntrada);
                classificacio.get(tipus).add(novaEntrada);
            }
        }
    }

    /**
     * Elimina el rànquing associat a un diccionari concret i actualitza la puntuació 
     * de cada jugador que estaba en aquell diccionari al ranquing de total de punts acumulats.
     * @param nomDiccionari Nom del diccionari
     */
    public void eliminarRanquingDeDiccionari(String nomDiccionari) {
        TreeSet<EntradaRanquing> ranquingDic = classificacio.get(nomDiccionari);
        Map<String, EntradaRanquing> directeDic = accesDirecte.get(nomDiccionari);

        if (ranquingDic != null && directeDic != null) {

            for (EntradaRanquing entrada : ranquingDic) {
                String usuari = entrada.idUsuari;
                float puntsArestar = entrada.punts;

                Map<String, EntradaRanquing> directeTotal = accesDirecte.get("total");
                TreeSet<EntradaRanquing> ranquingTotal = classificacio.get("total");

                if (directeTotal != null && directeTotal.containsKey(usuari)) {
                    EntradaRanquing entradaTotal = directeTotal.get(usuari);
                    ranquingTotal.remove(entradaTotal);
                    entradaTotal.punts -= puntsArestar;
                    if (entradaTotal.punts <= 0f) {
                        directeTotal.remove(usuari);
                    } else {
                        ranquingTotal.add(entradaTotal); 
                    }
                }
            }
        }

        classificacio.remove(nomDiccionari);
        accesDirecte.remove(nomDiccionari);
    }
}


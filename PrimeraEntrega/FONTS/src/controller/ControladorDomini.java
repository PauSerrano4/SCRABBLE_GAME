package controller;

import java.io.File;
import java.util.*;
import model.*;
import util.*;

public class ControladorDomini {

    public Partida partida;
    private ControladorMaquina maquina;
    public Diccionari diccionari;

    public ControladorDomini() {
        inicialitzar();
    }

    public void inicialitzar() { //inicialitzem les variables que necessitarem
        this.partida = null;
        this.maquina = null;
        this.diccionari = null;
    }

    public void inicialitzarPartida() {
        Jugador bot1 = new Jugador("BOT1");
        Jugador bot2 = new Jugador("BOT2");

        List<Jugador> jugadors = Arrays.asList(bot1, bot2);
        
        Tauler tauler = new Tauler(15);

        partida = new Partida(1, jugadors, tauler, diccionari);

        //hem de repartir les fitxes a cada jugador
        partida.repartirFitxes("BOT1", 7);
        partida.repartirFitxes("BOT2", 7);

        maquina = new ControladorMaquina(partida);  // ara és accessible sempre

        System.out.println("Nova partida creada!");
    }

    public void mostrarEstatPartida() {
        if (partida == null) {
            System.out.println("Cap partida inicialitzada.");
            return;
        }
        /*
        System.out.println("Torn del jugador: " + partida.getTornJugador());
        for (Jugador j : partida.getJugadors()) {
            System.out.println(j.getNom() + " - Punts: " + j.getPuntuacio() + " - Fitxes: " + j.getFitxes().size());
        }
        */
        for (Jugador j : partida.getJugadors()) {
            System.out.print(j.getNom() + " - Punts: " + j.getPuntuacio() + " - Fitxes: " + j.getFitxes().size() + " -> ");
            
            for (Fitxa f : j.getFitxes()) {
                System.out.print(f.getLletra() + "(" + f.getValor() + ") ");
            }
            System.out.println(); // salta de línia per al següent jugador
        }
        
        System.out.println("Fitxes a la bossa: " + partida.getBossa().size());
        System.out.println("Tauler: ");
        partida.mostrarTauler();
    }

    public void jugarTorn() {
        if (partida == null) {
            System.out.println("Cap partida activa.");
            return;
        }

        String jugador = partida.getTornJugador();

        // Fem jugar la màquina amb el nom del jugador
        maquina.jugarTorn(jugador);

        partida.avançarTorn();

        Jugador jugadorPartida = partida.getJugador(jugador);
        int fitxesARepartir = 7 - jugadorPartida.getFitxes().size();
        partida.repartirFitxes(jugador, fitxesARepartir);
    }

    public void importaDiccionari(String alfabetFile, String diccionariFile, String idioma) {
        Map<String, Pair<Integer, Integer>> alfabet = new HashMap<>();

        try {
            // Leer fichero de letras
            File fileLletres = new File(alfabetFile);
            Scanner sc1 = new Scanner(fileLletres);

            while (sc1.hasNextLine()) {
                String linia = sc1.nextLine().trim();
                if (linia.isEmpty()) continue; // ignorar comentarios
                String[] parts = linia.split("\\s+");

                if (parts.length == 3) {
                    String lletra = parts[0];
                    int quantitat = Integer.parseInt(parts[1]);
                    int puntuacio = Integer.parseInt(parts[2]);
                    alfabet.put(lletra, new Pair<>(quantitat, puntuacio));
                }
            }
            sc1.close();

            // Leer fichero de palabras
            File fileDiccionari = new File(diccionariFile);
            Scanner sc2 = new Scanner(fileDiccionari);

            List<String> paraules = new ArrayList<>();
            while (sc2.hasNextLine()) {
                String paraula = sc2.nextLine().trim();
                if (!paraula.isEmpty()) paraules.add(paraula);  // Asumimos que ya están en mayúsculas
            }
            sc2.close();

            // Crear Diccionari
            diccionari = new Diccionari(idioma, paraules, alfabet);
        } 
        catch (Exception e) {
            System.err.println("Error important el diccionari: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

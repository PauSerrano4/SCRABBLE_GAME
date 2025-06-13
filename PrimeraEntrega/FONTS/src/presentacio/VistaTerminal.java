package presentacio;

import java.util.*;

public class VistaTerminal {

    private ControladorPresentacio ctrlPresentacio;
    private Scanner scan;
    
    public VistaTerminal(ControladorPresentacio cp){
        ctrlPresentacio = cp;
        scan = new Scanner(System.in);
    };

    public void inicialitzaTerminal() { //primera funció que s'executará
        String comanda;

        System.out.println("\n ##################### BENVINGUT AL SCRABBLE ##################### \n");
        System.out.println("Escriu 'ajuda' per veure les comandes disponibles.");

        while (true) {
            System.out.print("> ");
            comanda = scan.nextLine().trim().toLowerCase();

            switch (comanda) {
                case "ajuda":
                    mostrarAjuda();
                    break;
                case "nova":
                    inicialitzarPartida();
                    break;
                case "mostrar":
                    ctrlPresentacio.mostrarEstatPartida();
                    break;
                case "torn":
                    ctrlPresentacio.jugarTorn();
                    break;
                case "sortir":
                    System.out.println("Sortint del joc...");
                    return;
                default:
                    System.out.println("Comanda no reconeguda.");
            }
        }
    }

    public void mostrarAjuda() {
        System.out.println("Comandes disponibles:");
        System.out.println(" - nova     : Inicia una nova partida");
        System.out.println(" - mostrar  : Mostra l'estat actual de la partida");
        System.out.println(" - torn     : Simula un torn de joc");
        System.out.println(" - sortir   : Tanca el programa");
    }

    public void inicialitzarPartida() {
        System.out.println("Anem a configurar la nova partida!");
        System.out.println("Amb quin idioma t'agradaria jugar?");
        System.out.println("1. Català");
        System.out.println("2. Español");
        System.out.println("3. English");

        int opcio = scan.nextInt();
    
        switch (opcio) {
            case 1 :
                ctrlPresentacio.importaDiccionari("../DATA/letrasCAT.txt", "../DATA/catalan.txt", "Català");
                break;
            
            case 2 :
                ctrlPresentacio.importaDiccionari("../DATA/letrasCAST.txt", "../DATA/castellano.txt", "Castellano");
                break;
            
            case 3 :
                ctrlPresentacio.importaDiccionari("../DATA/letrasENG.txt", "../DATA/english.txt", "English");
                break;
            
            default :
                System.out.println("Aquesta opció no és vàlida. Escull una altra!");
                inicialitzarPartida();
                break;
        };

        ctrlPresentacio.inicialitzarPartida();
    }
}

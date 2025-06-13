package persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe GestorHistorial - Gestiona l'historial de partides d'Scrabble.
 * Permet desar i carregar l'historial des d'un fitxer de text.
 * @author Alexander de Jong
 */
public class GestorHistorial {


    public GestorHistorial() {
        // Constructor buit, pot ser utilitzat per inicialitzar recursos si cal
    }

    /**
     * Desa una entrada a l'historial de partides.
     * @param entrada Text a afegir a l'historial.
     */
    public void guardarEntradaHistorial(String entrada)  {
        try (FileWriter fw = new FileWriter("../DATA/historial.txt", true)) {
            fw.write(entrada + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega l'historial de partides des d'un fitxer de text.
     * @return Llista d'entrades de l'historial.
     */
    public List<String> carregarHistorial()  {
        List<String> historial = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("../DATA/historial.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) historial.add(linea);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return historial;
    }
    
}

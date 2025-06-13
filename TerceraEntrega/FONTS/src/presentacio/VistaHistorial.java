package presentacio;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe VistaHistorial - Mostra l'historial de partides jugades.
 * Permet consultar el detall de cada partida seleccionada.
 */
public class VistaHistorial extends JFrame {

    // ---------- ATRIBUTS ----------
    private JList<String> llistaPartides;
    private JTextArea areaDetalls;
    private List<String> partides;  // cada string és una partida completa

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe VistaHistorial.
     * Inicialitza la finestra i carrega l'historial de partides.
     */
    public VistaHistorial() {
        setTitle("Historial de Partides");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Llegeix les línies del fitxer (una per línia)
        List<String> linies = ControladorPresentacio.getInstance().getHistorial();
        partides = agruparPartides(linies);

        // Mostrar només el títol (Guanyador: ...) a la llista
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String partida : partides) {
            String titol = partida.lines()
                                  .filter(l -> l.startsWith("Guanyador:"))
                                  .findFirst()
                                  .orElse("Partida");
            model.addElement(titol);
        }

        llistaPartides = new JList<>(model);
        JScrollPane scrollLlista = new JScrollPane(llistaPartides);
        scrollLlista.setPreferredSize(new Dimension(300, 500));
        add(scrollLlista, BorderLayout.WEST);

        areaDetalls = new JTextArea();
        areaDetalls.setEditable(false);
        areaDetalls.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollDetalls = new JScrollPane(areaDetalls);
        add(scrollDetalls, BorderLayout.CENTER);

        llistaPartides.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = llistaPartides.getSelectedIndex();
                if (index >= 0 && index < partides.size()) {
                    areaDetalls.setText(partides.get(index));
                }
            }
        });

        JButton tornar = new JButton("Tornar");
        tornar.addActionListener(e -> dispose());
        JPanel panellSud = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panellSud.add(tornar);
        add(panellSud, BorderLayout.SOUTH);
    }

    // ---------- MÈTODES PRIVATS ----------
    /**
     * Agrupa les línies llegides en partides segons el marcador "Guanyador:".
     * @param linies Llista de línies llegides de l'historial.
     * @return Llista de partides agrupades com a String.
     */
    private List<String> agruparPartides(List<String> linies) {
        List<String> agrupades = new ArrayList<>();
        StringBuilder partidaActual = new StringBuilder();

        for (String linia : linies) {
            if (linia.startsWith("Guanyador:")) {
                if (partidaActual.length() > 0) {
                    agrupades.add(partidaActual.toString().trim());
                    partidaActual.setLength(0);
                }
            }
            partidaActual.append(linia).append("\n");
        }
        if (partidaActual.length() > 0) {
            agrupades.add(partidaActual.toString().trim());
        }

        return agrupades;
    }
}

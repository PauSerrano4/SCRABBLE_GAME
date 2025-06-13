package presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Classe VistaDiccionaris - Permet consultar les paraules d'un diccionari seleccionat.
 */
public class VistaDiccionaris extends JFrame {

    // ---------- ATRIBUTS ----------
    private JComboBox<String> comboDiccionaris;
    private JTextArea areaParaules;

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe VistaDiccionaris.
     * Inicialitza la finestra i els components per consultar diccionaris.
     * @param diccionaris Llista de noms de diccionaris disponibles.
     */
    public VistaDiccionaris(List<String> diccionaris) {
        setTitle("Consulta de Diccionaris");
        setSize(600, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Selecciona un diccionari:"));

        comboDiccionaris = new JComboBox<>(diccionaris.toArray(new String[0]));
        topPanel.add(comboDiccionaris);

        JButton botoMostrar = new JButton("Mostrar");
        botoMostrar.addActionListener(this::mostrar);
        topPanel.add(botoMostrar);

        add(topPanel, BorderLayout.NORTH);

        areaParaules = new JTextArea();
        areaParaules.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaParaules);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // ---------- MÈTODES PRIVATS ----------
    /**
     * Mostra les paraules del diccionari seleccionat a l'àrea de text.
     * @param e Esdeveniment d'acció del botó Mostrar.
     */
    private void mostrar(ActionEvent e) {
        String diccionari = (String) comboDiccionaris.getSelectedItem();
        List<String> paraules = ControladorPresentacio.getInstance().getParaulesDiccionari(diccionari);
        areaParaules.setText("");
        for (String p : paraules) {
            areaParaules.append(p + "\n");
        }
    }
}

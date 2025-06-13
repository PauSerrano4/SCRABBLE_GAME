package presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import util.Pair;
import exceptions.*;

/**
 * Classe VistaCreacioDiccionari - Permet crear un nou diccionari personalitzat.
 * L'usuari pot introduir paraules i definir lletres amb quantitat i punts.
 */
public class VistaCreacioDiccionari extends JFrame {

    // ---------- ATRIBUTS ----------
    private JTextField campNom;
    private JTextArea areaParaules;
    private JTextArea areaLletres;

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe VistaCreacioDiccionari.
     * Inicialitza la finestra i els components per crear un nou diccionari.
     */
    public VistaCreacioDiccionari() {
        setTitle("Crear nou diccionari");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Nom del diccionari (sense .txt):"));
        campNom = new JTextField();
        panelSuperior.add(campNom, BorderLayout.CENTER);

        JPanel panelCentral = new JPanel(new GridLayout(2, 1, 10, 10));

        areaParaules = new JTextArea();
        areaParaules.setBorder(BorderFactory.createTitledBorder("Paraules (una per línia):"));
        JScrollPane scrollParaules = new JScrollPane(areaParaules);
        panelCentral.add(scrollParaules);

        areaLletres = new JTextArea();
        areaLletres.setBorder(BorderFactory.createTitledBorder("Lletres (format: LLETRA QUANTITAT PUNTS per línia):"));
        JScrollPane scrollLletres = new JScrollPane(areaLletres);
        panelCentral.add(scrollLletres);

        JButton botoGuardar = new JButton("Guardar fitxers");
        botoGuardar.addActionListener(this::guardarDiccionari);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(botoGuardar, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ---------- MÈTODES PRIVATS ----------
    /**
     * Guarda el nou diccionari i el fitxer de lletres segons les dades introduïdes.
     * @param e Esdeveniment d'acció del botó guardar.
     */
    private void guardarDiccionari(ActionEvent e) {
        String nom = campNom.getText().trim();
        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introdueix un nom vàlid.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File fitxerDiccionari = new File("../DATA/" + nom + ".txt");
        if (fitxerDiccionari.exists()) {
            JOptionPane.showMessageDialog(this, "Ja existeix un diccionari amb aquest nom.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> paraules = new ArrayList<>();
        for (String linia : areaParaules.getText().split("\\R")) {
            String paraula = linia.trim();
            if (!paraula.isEmpty()) paraules.add(paraula);
        }

        Map<String, Pair<Integer, Integer>> alfabet = new HashMap<>();
        for (String linia : areaLletres.getText().split("\\R")) {
            String[] parts = linia.trim().split("\\s+");
            if (parts.length != 3) {
                JOptionPane.showMessageDialog(this, "Error de format a la línia de lletres: " + linia, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                String lletra = parts[0];
                int quantitat = Integer.parseInt(parts[1]);
                int punts = Integer.parseInt(parts[2]);
                alfabet.put(lletra, new Pair<>(quantitat, punts));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error numèric a la línia de lletres: " + linia, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }


        try {
            ControladorPresentacio.getInstance().guardarFitxerDiccionari(nom, paraules, alfabet);
            JOptionPane.showMessageDialog(this, "Diccionari guardat correctament!");
            dispose();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error en guardar els fitxers: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DiccionariJaExisteixException ex) {
            JOptionPane.showMessageDialog(this, "Ja existeix un diccionari amb aquest nom.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperat: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

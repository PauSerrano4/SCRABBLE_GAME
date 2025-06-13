package presentacio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.util.List;
import util.Pair;

/**
 * Classe VistaResultatPartida - Mostra el resultat final d'una partida d'Scrabble.
 * Permet visualitzar les puntuacions dels jugadors i el guanyador.
 * @author Pau Serrano
 */
public class VistaResultatPartida extends JFrame {

    // ---------- ATRIBUTS ----------
    private JPanel panelPrincipal;
    private JTextArea areaResultats;
    private JButton botoTornarMenu;

    // ---------- CONSTRUCTOR ----------
    /**
     * Crea una nova vista per mostrar el resultat final de la partida.
     *
     * @param resultats   Llista de parelles (NomJugador, Puntuació)
     * @param guanyador   Nom del jugador guanyador
     * @param controlador Referència al controlador per accions posteriors
     */
    public VistaResultatPartida(List<Pair<String, Double>> resultats, String guanyador, ControladorPresentacio controlador) {
        setTitle("Resultat de la Partida");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(245, 250, 255));
        panelPrincipal.setBorder(new EmptyBorder(25, 35, 25, 35));
        setContentPane(panelPrincipal);

        // --------- CAPÇALERA AMB ICONA ---------
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelHeader.setOpaque(false);

        // Icona trofeu
        URL urlIcona = getClass().getResource("../../FONTS/src/presentacio/resources/icons/trofeu.png");
        if (urlIcona != null) {
            ImageIcon icon = new ImageIcon(urlIcona);
            Image scaled = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            JLabel lblIcona = new JLabel(new ImageIcon(scaled));
            panelHeader.add(lblIcona);
        }

        JLabel titol = new JLabel("Resultat Final");
        titol.setFont(new Font("Serif", Font.BOLD, 28));
        titol.setForeground(new Color(44, 62, 80));
        panelHeader.add(titol);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        // --------- RESULTATS ---------
        areaResultats = new JTextArea();
        areaResultats.setEditable(false);
        areaResultats.setFont(new Font("Monospaced", Font.BOLD, 18));
        areaResultats.setBackground(new Color(255, 255, 255));
        areaResultats.setForeground(new Color(52, 73, 94));
        areaResultats.setBorder(new EmptyBorder(20, 30, 20, 30));

        StringBuilder sb = new StringBuilder();
        for (Pair<String, Double> r : resultats) {
            sb.append(String.format("%-20s %5.0f punts\n", r.first, r.second));
        }
        sb.append("\nGuanyador: ").append(guanyador);
        areaResultats.setText(sb.toString());

        JScrollPane scroll = new JScrollPane(areaResultats);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(123, 104, 238), 2));
        panelPrincipal.add(scroll, BorderLayout.CENTER);

        // --------- BOTONS ---------
        JPanel panelBotons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotons.setOpaque(false);

        botoTornarMenu = new JButton("Tornar al menú");
        botoTornarMenu.setFont(new Font("SansSerif", Font.BOLD, 16));
        botoTornarMenu.setBackground(new Color(46, 204, 113));
        botoTornarMenu.setForeground(Color.WHITE);
        botoTornarMenu.setFocusPainted(false);
        botoTornarMenu.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        // Icona per al botó (opcional)
        URL urlMenu = getClass().getResource("../../FONTS/src/presentacio/resources/icons/menu.png");
        if (urlMenu != null) {
            ImageIcon iconMenu = new ImageIcon(urlMenu);
            Image scaledMenu = iconMenu.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
            botoTornarMenu.setIcon(new ImageIcon(scaledMenu));
        }

        botoTornarMenu.addActionListener(e -> {
            controlador.tornarAMenu();
            dispose();
        });

        panelBotons.add(botoTornarMenu);
        panelPrincipal.add(panelBotons, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ---------- MÈTODES ----------
    /**
     * Mostra la finestra de resultat de la partida.
     */
    public void mostrar() {
        setVisible(true);
    }
}

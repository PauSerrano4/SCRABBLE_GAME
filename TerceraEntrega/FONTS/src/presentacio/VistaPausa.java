package presentacio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

/**
 * VistaPausa és la finestra de pausa de la partida.
 * Permet a l'usuari continuar la partida, guardar-la, consultar les normes o tornar al menú principal.
 * @author Pau Serrano (pau.serrano.sanz@estudiantat.upc.edu)
 */
public class VistaPausa extends JFrame {

    /** Referència al controlador de presentació per gestionar les accions de la finestra. */
    private ControladorPresentacio controlador;

    /**
     * Creadora de la finestra de pausa.
     * @param controlador Controlador de presentació associat.
     */
    public VistaPausa(ControladorPresentacio controlador) {
        this.controlador = controlador;
        inicialitzar();
    }

    /**
     * Inicialitza la finestra de pausa amb la configuració visual, els botons i les accions corresponents.
     * La finestra inclou una capçalera amb icona i títol, i quatre botons principals:
     * - Continuar partida
     * - Guardar partida
     * - Consultar normes
     * - Tornar al menú principal
     */
    private void inicialitzar() {
        setTitle("Partida en pausa");
        setSize(650, 400); // Mida de la finestra
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(245, 250, 255));
        panelPrincipal.setBorder(new EmptyBorder(25, 35, 25, 35));
        setContentPane(panelPrincipal);

        // --------- CAPÇALERA AMB ICONA ---------
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelHeader.setOpaque(false);

        URL urlIcona = getClass().getResource("../../FONTS/src/presentacio/resources/icons/pausa.png");
        if (urlIcona != null) {
            ImageIcon icon = new ImageIcon(urlIcona);
            Image scaled = icon.getImage().getScaledInstance(38, 38, Image.SCALE_SMOOTH);
            JLabel lblIcona = new JLabel(new ImageIcon(scaled));
            panelHeader.add(lblIcona);
        }

        JLabel lblTitol = new JLabel("Partida en pausa");
        lblTitol.setFont(new Font("Serif", Font.BOLD, 28));
        lblTitol.setForeground(new Color(44, 62, 80));
        panelHeader.add(lblTitol);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        // --------- PANEL CENTRAL AMB BOTONS ---------
        JPanel panelBotons = new JPanel(new GridLayout(2, 2, 25, 25));
        panelBotons.setOpaque(false);
        panelBotons.setBorder(new EmptyBorder(30, 10, 10, 10));

        JButton botoContinuar = crearBotoAccio("Continuar partida", "../../FONTS/src/presentacio/resources/icons/jugar.png", new Color(46, 204, 113));
        JButton botoGuardar   = crearBotoAccio("Guardar partida", "../../FONTS/src/presentacio/resources/icons/guardar.png", new Color(52, 152, 219));
        JButton botoNormes    = crearBotoAccio("Consultar normes", "../../FONTS/src/presentacio/resources/icons/ajuda.png", new Color(255, 165, 0));
        JButton botoTornar    = crearBotoAccio("Tornar al menú principal", "../../FONTS/src/presentacio/resources/icons/logout.png", new Color(231, 76, 60));

        panelBotons.add(botoContinuar);
        panelBotons.add(botoGuardar);
        panelBotons.add(botoNormes);
        panelBotons.add(botoTornar);

        panelPrincipal.add(panelBotons, BorderLayout.CENTER);

        // --------- ACCIONS DELS BOTONS ---------
        botoTornar.addActionListener(e -> {
            int resposta = JOptionPane.showConfirmDialog(
                this,
                "Estàs segur que vols tornar al menú principal?",
                "Confirmar sortida",
                JOptionPane.YES_NO_OPTION
            );
            int guardar = JOptionPane.showConfirmDialog(
                this,
                "Vols guardar la partida abans de sortir?",
                "Guardar partida",
                JOptionPane.YES_NO_OPTION
            );
            if (guardar == JOptionPane.YES_OPTION) {
                guardarPartida();
            }
            if (resposta == JOptionPane.YES_OPTION) {
                controlador.tornarMenuPrincipal();
                dispose();
            }
        });

        botoContinuar.addActionListener(e -> {
            controlador.continuarPartida();
            dispose();
        });

        botoGuardar.addActionListener(e -> {
            guardarPartida();
        });

        botoNormes.addActionListener(e -> new VistaNormes());
    }

    /**
     * Crea un botó d'acció amb estil uniforme, icona i color personalitzat.
     * @param text Text del botó.
     * @param iconaPath Ruta relativa de la icona PNG.
     * @param color Color de fons del botó.
     * @return JButton configurat.
     */
    private JButton crearBotoAccio(String text, String iconaPath, Color color) {
        URL url = getClass().getResource(iconaPath);
        JButton boto;
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image scaled = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            boto = new JButton(text, new ImageIcon(scaled));
        } else {
            boto = new JButton(text);
        }
        boto.setFont(new Font("SansSerif", Font.BOLD, 16));
        boto.setBackground(color);
        boto.setForeground(Color.WHITE);
        boto.setFocusPainted(false);
        boto.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        return boto;
    }

    /**
     * Guarda la partida actual i mostra un missatge d'èxit o error.
     */
    private void guardarPartida() {
        if (controlador.guardarPartida()) {
            JOptionPane.showMessageDialog(this, "Partida guardada correctament.");
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar la partida.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

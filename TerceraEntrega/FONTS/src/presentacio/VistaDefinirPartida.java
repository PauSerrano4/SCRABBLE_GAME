package presentacio;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.net.URL;
import util.Dificultat;
import util.ModeJoc;
import util.Contrincant;


/**
 * VistaDefinirPartida és la classe que gestiona la interfície d'usuari
 * per definir els paràmetres d'una nova partida de Scrabble.
 * Permet seleccionar mode de joc, contrincant, dificultat i idioma.
 * @author Marc Gil Moreno
 */
public class VistaDefinirPartida extends JFrame implements ActionListener {
    private ControladorPresentacio ctrl;

    // components de selecció
    private Choice choiceMode;
    private Choice choiceContrincant;
    private Choice choiceDificultat;
    private Choice choiceIdioma;

    // botons
    private JButton btnTornar;
    private JButton btnJugar;

    public VistaDefinirPartida(ControladorPresentacio ctrl) {
        this.ctrl = ctrl;

        setTitle("SCRABBLE - DEFINIR PARTIDA");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(new Color(245, 250, 255));
        panelPrincipal.setBorder(new EmptyBorder(25, 35, 25, 35));
        setContentPane(panelPrincipal);

        // --- CAPÇALERA AMB ICONA ---
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelHeader.setOpaque(false);

        URL urlIcona = getClass().getResource("../../FONTS/src/presentacio/resources/icons/configuracio.png");
        if (urlIcona != null) {
            ImageIcon icon = new ImageIcon(urlIcona);
            Image scaled = icon.getImage().getScaledInstance(38, 38, Image.SCALE_SMOOTH);
            JLabel lblIcona = new JLabel(new ImageIcon(scaled));
            panelHeader.add(lblIcona);
        }

        JLabel lblTitol = new JLabel("Definir nova partida");
        lblTitol.setFont(new Font("Serif", Font.BOLD, 28));
        lblTitol.setForeground(new Color(44, 62, 80));
        panelHeader.add(lblTitol);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        // --- PANEL CENTRAL AMB FORMULARI ---
        JPanel panelCentre = new JPanel(new BorderLayout());
        panelCentre.setOpaque(false);

        JPanel panelForm = new JPanel(new GridLayout(4, 2, 20, 18));
        panelForm.setOpaque(false);
        panelForm.setBorder(new EmptyBorder(30, 60, 30, 60));

        JLabel lblMode = new JLabel("Mode de joc:");
        lblMode.setFont(new Font("SansSerif", Font.BOLD, 16));
        choiceMode = new Choice();
        for (ModeJoc m : ctrl.getModesDisponibles()) {
            choiceMode.add(m.getDisplayName());
        }
        panelForm.add(lblMode);
        panelForm.add(choiceMode);

        JLabel lblContrincant = new JLabel("Contrincant:");
        lblContrincant.setFont(new Font("SansSerif", Font.BOLD, 16));
        choiceContrincant = new Choice();
        for (Contrincant c : ctrl.getContrincantsDisponibles()) {
            choiceContrincant.add(c.getDisplayName());
        }
        panelForm.add(lblContrincant);
        panelForm.add(choiceContrincant);

        JLabel lblDificultat = new JLabel("Dificultat:");
        lblDificultat.setFont(new Font("SansSerif", Font.BOLD, 16));
        choiceDificultat = new Choice();
        for (Dificultat d : ctrl.getDificultatsDisponibles()) {
            choiceDificultat.add(d.getDisplayName());
        }
        panelForm.add(lblDificultat);
        panelForm.add(choiceDificultat);

        JLabel lblIdioma = new JLabel("Idioma:");
        lblIdioma.setFont(new Font("SansSerif", Font.BOLD, 16));
        choiceIdioma = new Choice();
        for (String idioma : ctrl.getNomDiccionaris()) {
            if (!idioma.toLowerCase().startsWith("letras")) choiceIdioma.add(idioma);
        }
        panelForm.add(lblIdioma);
        panelForm.add(choiceIdioma);

        JPanel panelFormWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        panelFormWrapper.setOpaque(false);
        panelFormWrapper.add(panelForm);

        panelCentre.add(panelFormWrapper, BorderLayout.CENTER);
        panelPrincipal.add(panelCentre, BorderLayout.CENTER);

        // --- BOTONS ---
        JPanel panelBotons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panelBotons.setOpaque(false);

        // Botó JUGAR amb icona de jugar
        btnJugar = new JButton("JUGAR");
        btnJugar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnJugar.setBackground(new Color(46, 204, 113));
        btnJugar.setForeground(Color.WHITE);
        URL urlJugar = getClass().getResource("../../FONTS/src/presentacio/resources/icons/jugar.png");
        if (urlJugar != null) {
            ImageIcon iconJugar = new ImageIcon(urlJugar);
            Image scaledJugar = iconJugar.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
            btnJugar.setIcon(new ImageIcon(scaledJugar));
        }

        // Botó TORNAR amb icona de desfer
        btnTornar = new JButton("TORNAR");
        btnTornar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnTornar.setBackground(new Color(231, 76, 60));
        btnTornar.setForeground(Color.WHITE);
        URL urlDesfer = getClass().getResource("../../FONTS/src/presentacio/resources/icons/desfer.png");
        if (urlDesfer != null) {
            ImageIcon iconDesfer = new ImageIcon(urlDesfer);
            Image scaledDesfer = iconDesfer.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
            btnTornar.setIcon(new ImageIcon(scaledDesfer));
        }

        panelBotons.add(btnTornar);
        panelBotons.add(btnJugar);

        panelPrincipal.add(panelBotons, BorderLayout.SOUTH);

        // --- LISTENERS ---
        btnJugar.addActionListener(this);
        btnTornar.addActionListener(this);

        // --- MOSTRAR ---
        setVisible(true);
    }

    /**
     * Mètode per mostrar la vista de definir partida.
     * Aquesta funció es pot cridar des del controlador per fer visible la finestra.
     */
    public void mostrar() {
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnTornar) {
            dispose();
            ctrl.tornarGestioPartida();
        } else if (src == btnJugar) {
            String modeSeleccionat = choiceMode.getSelectedItem();
            ModeJoc mode = ModeJoc.fromDisplayName(modeSeleccionat);

            String contrincantSeleccionat = choiceContrincant.getSelectedItem();
            Contrincant contrincant = Contrincant.fromDisplayName(contrincantSeleccionat);

            String dificultatSeleccionada  = choiceDificultat.getSelectedItem();
            Dificultat dificultat = Dificultat.fromDisplayName(dificultatSeleccionada);

            String idioma  = choiceIdioma.getSelectedItem();

            ctrl.definirPartida(mode, contrincant, dificultat, idioma);
            dispose();
            ctrl.mostraVistaJoc();
        }
    }
}
package presentacio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

/**
 * Classe VistaMenuPrincipal - Menú visual amb estil millorat per a Scrabble.
 */
public class VistaMenuPrincipal extends JFrame {

    private JButton btnLogout, btnAjuda, btnDiccionaris, btnJugar, btnEstadistiques, btnPerfil, btnHistorial, btnRankings;

    /**
     * Creadora de la vista del menú principal.
     * Inicialitza la interfície i els components necessaris.
     */
    public VistaMenuPrincipal() {
        setTitle("Menú Principal - Scrabble");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 248, 255));

        // ---------- PANEL SUPERIOR ----------
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(new EmptyBorder(20, 20, 20, 20));
        top.setBackground(new Color(70, 130, 180));

        JLabel lblTitle = new JLabel("SCRABBLE", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 32));
        top.add(lblTitle, BorderLayout.CENTER);

        btnLogout = crearBotoTop("Logout", "../../FONTS/src/presentacio/resources/icons/logout.png");
        btnAjuda = crearBotoTop("Ajuda", "../../FONTS/src/presentacio/resources/icons/ajuda.png");
        top.add(btnAjuda, BorderLayout.WEST);
        top.add(btnLogout, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        // ---------- PANEL CENTRAL ----------
        JPanel center = new JPanel(new GridLayout(2, 3, 30, 30));
        center.setBorder(new EmptyBorder(40, 80, 40, 80));
        center.setBackground(new Color(245, 250, 255));

        btnDiccionaris   = crearBoto("Diccionaris", "../../FONTS/src/presentacio/resources/icons/diccionari.png", new Color(123, 104, 238));
        btnJugar         = crearBoto("Jugar partida", "../../FONTS/src/presentacio/resources/icons/jugar.png", new Color(60, 179, 113));
        btnEstadistiques = crearBoto("Estadístiques", "../../FONTS/src/presentacio/resources/icons/estadistiques.png", new Color(255, 165, 0));
        btnPerfil        = crearBoto("Gestionar perfil", "../../FONTS/src/presentacio/resources/icons/perfil.png", new Color(100, 149, 237));
        btnHistorial     = crearBoto("Historial", "../../FONTS/src/presentacio/resources/icons/historial.png", new Color(72, 209, 204));
        btnRankings      = crearBoto("Rànquings", "../../FONTS/src/presentacio/resources/icons/ranquings.png", new Color(255, 105, 180));

        for (JButton b : new JButton[]{btnDiccionaris, btnJugar, btnEstadistiques, btnPerfil, btnHistorial, btnRankings}) {
            center.add(b);
        }

        add(center, BorderLayout.CENTER);

        // ---------- ACCIONS ----------
        btnLogout.addActionListener(e -> logout());
        btnAjuda.addActionListener(e -> new VistaNormes());
        btnDiccionaris.addActionListener(e -> mostrarDiccionaris());
        btnJugar.addActionListener(e -> iniciarPartida());
        btnEstadistiques.addActionListener(e -> mostrarEstadistiques());
        btnPerfil.addActionListener(e -> mostrarPerfil());
        btnHistorial.addActionListener(e -> mostrarHistorial());
        btnRankings.addActionListener(e -> mostrarRanquings());
    }

    // ---------- FACTORIA DE BOTONS ----------
    private JButton crearBoto(String text, String iconaPath, Color bg) {
        URL url = getClass().getResource(iconaPath);
        if (url == null) {
            //System.out.println("No s'ha trobat la icona: " + iconaPath);
            return new JButton(text); // fallback en cas d'error
        }
        ImageIcon icon = new ImageIcon(url);
        Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        JButton boto = new JButton(text, new ImageIcon(scaled));
        boto.setFont(new Font("SansSerif", Font.BOLD, 16));
        boto.setHorizontalTextPosition(SwingConstants.CENTER);
        boto.setVerticalTextPosition(SwingConstants.BOTTOM);
        boto.setFocusPainted(false);
        boto.setBackground(bg);
        boto.setForeground(Color.WHITE);
        boto.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return boto;
    }
    

    private JButton crearBotoTop(String text, String iconaPath) {
        URL url = getClass().getResource(iconaPath);
        if (url == null) {
            //System.err.println("No s'ha trobat la icona: " + iconaPath);
            return new JButton(text);
        }
        ImageIcon icon = new ImageIcon(url);
        Image scaled = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton boto = new JButton(text, new ImageIcon(scaled));

        boto.setFont(new Font("SansSerif", Font.PLAIN, 14));
        boto.setBackground(Color.WHITE);
        boto.setFocusPainted(false);
        return boto;
    }

    // ---------- MÈTODES ----------
    /**
     * Mostra la finestra del menú principal.
     */
    public void mostrar() {
        setVisible(true);
    }

    /**
     * Gestiona el tancament de sessió de l'usuari.
     */
    private void logout() {
        ControladorPresentacio.getInstance().tancarSessio();
        dispose();
    }

    /**
     * Mostra la vista de gestió de diccionaris.
     */
    private void mostrarDiccionaris() {
        new VistaGestioDiccionaris();
    }

    /**
     * Inicia la vista de gestió de partida.
     */
    private void iniciarPartida() {
        ControladorPresentacio ctrl = ControladorPresentacio.getInstance();
        VistaGestioPartida vgp = new VistaGestioPartida(ctrl);
        vgp.mostrar();
        dispose();
    }

    /**
     * Mostra la vista d'estadístiques de l'usuari.
     */
    private void mostrarEstadistiques() {
        ControladorPresentacio.getInstance().mostrarEstadistiques();
    }

    /**
     * Mostra la vista de gestió del perfil d'usuari.
     */
    private void mostrarPerfil() {
        ControladorPresentacio ctrl = ControladorPresentacio.getInstance();
        VistaGestioUsuari vg = new VistaGestioUsuari(ctrl);
        vg.setVisible(true);
    }

    /**
     * Mostra la vista de l'historial de partides.
     */
    private void mostrarHistorial() {
        ControladorPresentacio.getInstance().mostrarVistaHistorial();
    }

    /**
     * Mostra la vista de rànquings.
     */
    private void mostrarRanquings() {
        ControladorPresentacio.getInstance().mostrarVistaRanquings();
    }
}

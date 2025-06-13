package presentacio;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.net.URL;
import exceptions.*;

/**
 * Classe VistaGestioPartida - Permet gestionar l'inici o la continuació d'una partida.
 * Mostra opcions per continuar una partida guardada, començar-ne una de nova o tornar al menú principal.
 * @author Marc Gil Moreno
 */
public class VistaGestioPartida extends JFrame implements ActionListener {

    // ---------- ATRIBUTS ----------
    private JButton btnContinuar;
    private JButton btnNova;
    private JButton btnTornar;
    private ControladorPresentacio ctrl;

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe VistaGestioPartida.
     * Inicialitza la finestra i els components de gestió de partida.
     * @param ctrl Controlador de presentació associat.
     */
    public VistaGestioPartida(ControladorPresentacio ctrl) {
        this.ctrl = ctrl;

        setTitle("SCRABBLE - JUGAR");
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

        URL urlIcona = getClass().getResource("../../FONTS/src/presentacio/resources/icons/jugar.png");
        if (urlIcona != null) {
            ImageIcon icon = new ImageIcon(urlIcona);
            Image scaled = icon.getImage().getScaledInstance(38, 38, Image.SCALE_SMOOTH);
            JLabel lblIcona = new JLabel(new ImageIcon(scaled));
            panelHeader.add(lblIcona);
        }

        JLabel lblTitol = new JLabel("Gestió de Partida");
        lblTitol.setFont(new Font("Serif", Font.BOLD, 28));
        lblTitol.setForeground(new Color(44, 62, 80));
        panelHeader.add(lblTitol);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        // --- PANEL CENTRAL AMB DOS BOTONS GRANS ---
        JPanel panelCentre = new JPanel(new GridLayout(1, 2, 50, 0));
        panelCentre.setOpaque(false);
        panelCentre.setBorder(new EmptyBorder(40, 40, 40, 40));

        btnContinuar = crearBoto("Continuar partida", "../../FONTS/src/presentacio/resources/icons/continuar.png", new Color(52, 152, 219));
        btnNova      = crearBoto("Nova partida", "../../FONTS/src/presentacio/resources/icons/afegir.png", new Color(46, 204, 113));

        panelCentre.add(btnContinuar);
        panelCentre.add(btnNova);
        panelPrincipal.add(panelCentre, BorderLayout.CENTER);

        // --- BOTÓ DE TORNAR ---
        JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelSud.setOpaque(false);
        btnTornar = crearBoto("Tornar", "../../FONTS/src/presentacio/resources/icons/desfer.png", new Color(231, 76, 60));
        // Fes el botó més petit i estilitzat
        btnTornar.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnTornar.setPreferredSize(new Dimension(110, 38));
        panelSud.add(btnTornar);
        panelPrincipal.add(panelSud, BorderLayout.SOUTH);

        // --- LISTENERS ---
        btnContinuar.addActionListener(this);
        btnNova.addActionListener(this);
        btnTornar.addActionListener(this);

        // --- TANCAR FINESTRA ---
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // --- MOSTRAR ---
        setVisible(true);
    }

    // ---------- MÈTODES ----------
    /**
     * Fes visible la finestra.
     */
    public void mostrar() {
        setVisible(true);
    }

    /**
     * Gestiona les accions dels botons de la finestra.
     * @param e Esdeveniment d'acció.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnContinuar) {
            mostrarHistorial();
        }
        else if (src == btnNova) {
            VistaDefinirPartida vdp = new VistaDefinirPartida(ctrl);
            vdp.mostrar();
            dispose();
        }
        else if (src == btnTornar) {
            VistaMenuPrincipal vmp = new VistaMenuPrincipal();
            vmp.mostrar();
            dispose();
        }
    }

    /**
     * Mostra un diàleg amb la llista de partides guardades per continuar o esborrar.
     */
    private void mostrarHistorial() {
        ControladorPresentacio ctrl = ControladorPresentacio.getInstance();
        java.util.List<String> saves = ctrl.listarPartidas();

        // 1) Llista de partides
        java.awt.List lista = new java.awt.List(10, false);
        for (String s : saves) lista.add(s);

        // 2) Diàleg modal
        Dialog dlg = new Dialog(this, "Partides guardades", true);
        dlg.setLayout(new BorderLayout(5, 5));
        dlg.add(lista, BorderLayout.CENTER);

        // 3) Panell de botons: Carregar / Borrar / Cancel·lar
        Panel pnlBtns = new Panel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnCargar   = new JButton("Carregar");
        JButton btnBorrar   = new JButton("Borar");
        JButton btnCancelar = new JButton("Cancelar");

        btnCargar.setEnabled(false);
        btnBorrar.setEnabled(false);

        pnlBtns.add(btnCargar);
        pnlBtns.add(btnBorrar);
        pnlBtns.add(btnCancelar);
        dlg.add(pnlBtns, BorderLayout.SOUTH);

        // 4) Habilitar Carregar/Borrar només si hi ha selecció
        lista.addItemListener(ev -> {
            boolean hasSel = lista.getSelectedIndex() != -1;
            btnCargar.setEnabled(hasSel);
            btnBorrar.setEnabled(hasSel);
        });

        // 5) Acció Carregar
        btnCargar.addActionListener(ev -> {
            String sel = lista.getSelectedItem();
            if (sel != null) {
                try {
                    ctrl.cargarPartida(sel);
                }
                catch (Exception e) {
                    if (e instanceof ErrorCarregarPartidaException) {
                        JOptionPane.showMessageDialog(this, "Error al carregar la partida: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                dispose();
                dlg.dispose();
            }
        });

        // 6) Acció Borrar
        btnBorrar.addActionListener(ev -> {
            String sel = lista.getSelectedItem();
            if (sel != null) {
                borrarPartida(sel);
                lista.remove(sel);
            }
        });

        // 7) Acció Cancel·lar: tanca només el diàleg
        btnCancelar.addActionListener(ev -> dlg.dispose());

        // 8) Mostrar el diàleg
        dlg.setSize(400, 300);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void borrarPartida(String partida) {
        if (ctrl.borrarPartida(partida)) {
            JOptionPane.showMessageDialog(null, "Guardat eliminat correctament.");
        } else {
            JOptionPane.showMessageDialog(null, "Error al eliminar el guardat.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crea un botó amb estil, icona i color.
     */
    private JButton crearBoto(String text, String iconaPath, Color color) {
        JButton boto = new JButton(text);
        boto.setFont(new Font("SansSerif", Font.BOLD, 18));
        boto.setBackground(color);
        boto.setForeground(Color.WHITE);
        boto.setFocusable(false);
        boto.setPreferredSize(new Dimension(220, 60));
        try {
            URL url = getClass().getResource(iconaPath);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image scaled = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
                boto.setIcon(new ImageIcon(scaled));
            }
        } catch (Exception ignored) {}
        return boto;
    }
}
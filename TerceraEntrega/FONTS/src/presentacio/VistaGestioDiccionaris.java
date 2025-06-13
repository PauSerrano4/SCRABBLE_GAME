package presentacio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Classe VistaGestioDiccionari - Permet gestionar els diccionaris disponibles per jugar.
 * Inclou opcions per mostrar els diccionaris, inserir diccionaris i eliminar diccionaris.
 */
public class VistaGestioDiccionaris extends JFrame {
    // ---------- ATRIBUTS ----------
    private JList<String> llistaDiccionaris;

    // ---------- CONSTRUCTORS ----------
    /**
     * Creadora de la vista de gestió de diccionaris.
     * Inicialitza la interfície i els components necessaris.
     */
    public VistaGestioDiccionaris() {
        llistaDiccionaris = new JList<>();
        ControladorPresentacio cp = ControladorPresentacio.getInstance();

        setTitle("Gestió de Diccionaris");
        setSize(800, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout(12, 12));
        panelPrincipal.setBackground(new Color(245, 250, 255));
        panelPrincipal.setBorder(new EmptyBorder(18, 28, 18, 28));

        // Cabecera
        JLabel lblTitol = new JLabel("Gestió de Diccionaris", JLabel.LEFT);
        lblTitol.setFont(new Font("Serif", Font.BOLD, 26));
        lblTitol.setForeground(new Color(44, 62, 80));
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelHeader.setOpaque(false);
        panelHeader.add(lblTitol);
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        // Centro: lista de diccionarios
        actualitzarLlistaDiccionaris();
        JScrollPane scrollPane = new JScrollPane(llistaDiccionaris);
        scrollPane.setPreferredSize(new Dimension(320, 120));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220,220,220), 1));
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Botonera inferior
        JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.CENTER, 28, 0));
        panelSud.setOpaque(false);

        JButton botoMostrar = new JButton("Mostrar diccionaris");
        botoMostrar.setFont(new Font("SansSerif", Font.BOLD, 15));
        botoMostrar.setBackground(new Color(52, 152, 219));
        botoMostrar.setForeground(Color.WHITE);
        botoMostrar.setFocusable(false);
        botoMostrar.addActionListener(e -> ControladorPresentacio.getInstance().mostrarVistaDiccionaris());

        JButton botoInserir = new JButton("Inserir diccionari");
        botoInserir.setFont(new Font("SansSerif", Font.BOLD, 15));
        botoInserir.setBackground(new Color(46, 204, 113));
        botoInserir.setForeground(Color.WHITE);
        botoInserir.setFocusable(false);
        botoInserir.addActionListener(e -> new VistaCreacioDiccionari());

        JButton botoEliminar = new JButton("Eliminar diccionari");
        botoEliminar.setFont(new Font("SansSerif", Font.BOLD, 15));
        botoEliminar.setBackground(new Color(231, 76, 60));
        botoEliminar.setForeground(Color.WHITE);
        botoEliminar.setFocusable(false);
        botoEliminar.addActionListener(e -> {
            List<String> noms = cp.getCtrlDomini().getNomDiccionaris();
            if (noms.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hi ha diccionaris per eliminar.");
                return;
            }
            JList<String> llista = new JList<>(noms.toArray(new String[0]));
            JScrollPane scroll = new JScrollPane(llista);
            scroll.setPreferredSize(new Dimension(300, 150));

            int confirm = JOptionPane.showConfirmDialog(this, scroll,
                "Selecciona un diccionari per eliminar", JOptionPane.OK_CANCEL_OPTION);

            if (confirm == JOptionPane.OK_OPTION) {
                String seleccionat = llista.getSelectedValue();
                if (seleccionat != null) {
                    int segur = JOptionPane.showConfirmDialog(this,
                        "Estàs segur que vols eliminar el diccionari \"" + seleccionat + "\" i el seu rànquing?",
                        "Confirmació", JOptionPane.YES_NO_OPTION);
                    if (segur == JOptionPane.YES_OPTION) {
                        try {
                            cp.getCtrlDomini().eliminarDiccionari(seleccionat);
                            actualitzarLlistaDiccionaris();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Error eliminant: " + ex.getMessage());
                        }
                    }
                }
            }
        });

        panelSud.add(botoMostrar);
        panelSud.add(botoInserir);
        panelSud.add(botoEliminar);
        panelPrincipal.add(panelSud, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);

        panelPrincipal.revalidate();
        panelPrincipal.repaint();

        setVisible(true);
    }

    // ---------- MÈTODES PRIVATS ----------
    /**
     * Llista els diccionaris disponibles actuals i permet mostrar-los
     * al centre de la pantalla
     */
    private void actualitzarLlistaDiccionaris() {
        ControladorPresentacio cp = ControladorPresentacio.getInstance();
        List<String> noms = cp.getCtrlDomini().getNomDiccionaris();
        llistaDiccionaris.setListData(noms.toArray(new String[0]));
        llistaDiccionaris.setFont(new Font("SansSerif", Font.PLAIN, 14));
        llistaDiccionaris.setForeground(new Color(44, 62, 80));
        llistaDiccionaris.setBackground(Color.WHITE);
    }
}
package presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

/**
 * Classe VistaGestioUsuari - Permet gestionar el perfil de l'usuari.
 * Inclou opcions per canviar àlies, canviar contrasenya i eliminar el compte.
 */
public class VistaGestioUsuari extends JFrame {

    // ---------- ATRIBUTS ----------
    private ControladorPresentacio controlador;

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe VistaGestioUsuari.
     * Inicialitza la finestra i els components de gestió del perfil.
     * @param controlador Controlador de presentació associat.
     */
    public VistaGestioUsuari(ControladorPresentacio controlador) {
        this.controlador = controlador;
        inicialitzar();
    }

    // ---------- MÈTODES PRIVATS ----------
    /**
     * Inicialitza la interfície gràfica i els botons d'acció.
     */
    private void inicialitzar() {
        setTitle("Gestió del perfil");
        setSize(400, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelPrincipal = new JPanel(new BorderLayout(12, 12));
        panelPrincipal.setBackground(new Color(245, 250, 255));
        panelPrincipal.setBorder(new EmptyBorder(18, 28, 18, 28));
        setContentPane(panelPrincipal);

        JLabel lblTitol = new JLabel("Gestió del perfil", JLabel.LEFT);
        lblTitol.setFont(new Font("Serif", Font.BOLD, 24));
        lblTitol.setForeground(new Color(44, 62, 80));
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelHeader.setOpaque(false);
        panelHeader.add(lblTitol);
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        JPanel panelCentre = new JPanel(new GridLayout(3, 1, 14, 14));
        panelCentre.setOpaque(false);

        JButton botoCanviarAlies = new JButton("Canviar àlies");
        botoCanviarAlies.setFont(new Font("SansSerif", Font.BOLD, 15));
        botoCanviarAlies.setBackground(new Color(52, 152, 219));
        botoCanviarAlies.setForeground(Color.WHITE);
        botoCanviarAlies.setFocusable(false);

        JButton botoCanviarContrasenya = new JButton("Canviar contrasenya");
        botoCanviarContrasenya.setFont(new Font("SansSerif", Font.BOLD, 15));
        botoCanviarContrasenya.setBackground(new Color(46, 204, 113));
        botoCanviarContrasenya.setForeground(Color.WHITE);
        botoCanviarContrasenya.setFocusable(false);

        JButton botoEliminarCompte = new JButton("Eliminar compte");
        botoEliminarCompte.setFont(new Font("SansSerif", Font.BOLD, 15));
        botoEliminarCompte.setBackground(new Color(231, 76, 60));
        botoEliminarCompte.setForeground(Color.WHITE);
        botoEliminarCompte.setFocusable(false);

        panelCentre.add(botoCanviarAlies);
        panelCentre.add(botoCanviarContrasenya);
        panelCentre.add(botoEliminarCompte);

        panelPrincipal.add(panelCentre, BorderLayout.CENTER);

        botoCanviarAlies.addActionListener(e -> mostrarCanviNom());
        botoCanviarContrasenya.addActionListener(e -> mostrarCanviContrasenya());
        botoEliminarCompte.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                int resposta = JOptionPane.showConfirmDialog(
                        VistaGestioUsuari.this,
                        "Segur que vols eliminar el teu compte? Aquesta acció és irreversible.",
                        "Confirmació",
                        JOptionPane.YES_NO_OPTION
                );
                if (resposta == JOptionPane.YES_OPTION) {
                    String contrasenya = JOptionPane.showInputDialog(
                            VistaGestioUsuari.this,
                            "Introdueix la teva contrasenya per confirmar:"
                    );
                    if (contrasenya != null && controlador.validarContrasenya(contrasenya)) {
                        controlador.eliminarCompte();
                        JOptionPane.showMessageDialog(VistaGestioUsuari.this, "Compte eliminat correctament.");
                        VistaGestioUsuari.this.dispose();
                        System.exit(0);
                    } else {
                        JOptionPane.showMessageDialog(VistaGestioUsuari.this, "Contrasenya incorrecta. Operació cancel·lada.");
                    }
                }
            }
        });
    }

    /**
     * Mostra un diàleg per canviar el nom d'usuari.
     */
    private void mostrarCanviNom() {
        String nouNom = JOptionPane.showInputDialog(this, "Introdueix el nou nom d'usuari:");

        if (nouNom != null && !nouNom.trim().isEmpty()) {
            boolean canviat = controlador.canviarNomUsuari(nouNom.trim());
            if (canviat) {
                JOptionPane.showMessageDialog(this, "Nom canviat correctament.");
                //controlador.actualitzarVista();
            } else {
                JOptionPane.showMessageDialog(this, "Error: Aquest nom ja existeix o ha fallat el canvi.");
            }
        }
    }

    /**
     * Mostra un diàleg per canviar la contrasenya de l'usuari.
     */
    private void mostrarCanviContrasenya() {
        JPasswordField pass1 = new JPasswordField();
        JPasswordField pass2 = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Nova contrasenya:"));
        panel.add(pass1);
        panel.add(new JLabel("Repeteix la contrasenya:"));
        panel.add(pass2);

        int result = JOptionPane.showConfirmDialog(this, panel, "Canviar contrasenya", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String p1 = new String(pass1.getPassword()).trim();
            String p2 = new String(pass2.getPassword()).trim();

            if (!p1.isEmpty() && p1.equals(p2)) {
                boolean canviat = controlador.canviarContrasenya(p1);
                if (canviat) {
                    JOptionPane.showMessageDialog(this, "Contrasenya canviada correctament.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error en canviar la contrasenya.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Les contrasenyes no coincideixen o són buides.");
            }
        }
    }
}

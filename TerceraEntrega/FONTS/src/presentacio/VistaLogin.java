package presentacio;

import presentacio.ControladorPresentacio;
import controller.CtrlUsuari;
import exceptions.LoginException;

import javax.swing.*;
import java.awt.*;

/**
 * Classe VistaLogin - Vista per a la gestió de login i registre d'usuaris.
 * Permet iniciar sessió o registrar un nou usuari i accedir al menú principal.
 */
public class VistaLogin extends JFrame {

    // ---------- ATRIBUTS ----------
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;
    private JButton btnRegister;

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe VistaLogin.
     * Inicialitza la interfície gràfica i els components de login i registre.
     */
    public VistaLogin() {
       

        setTitle("Scrabble - Login");
        setSize(350, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        txtUser = new JTextField();
        txtPass = new JPasswordField();
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Registrar-se");

        JPanel panellCamps = new JPanel(new GridLayout(2, 2));
        panellCamps.add(new JLabel("Nom d'usuari:"));
        panellCamps.add(txtUser);
        panellCamps.add(new JLabel("Contrasenya:"));
        panellCamps.add(txtPass);

        JPanel panellBotons = new JPanel();
        panellBotons.add(btnLogin);
        panellBotons.add(btnRegister);

        add(panellCamps, BorderLayout.CENTER);
        add(panellBotons, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> login());
        btnRegister.addActionListener(e -> registrar());
    }

    // ---------- MÈTODES ----------
    /**
     * Gestiona l'inici de sessió de l'usuari.
     */
    private void login() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();
        ControladorPresentacio cp = ControladorPresentacio.getInstance();

        try {
            cp.getCtrlDomini().login(user, pass);
            JOptionPane.showMessageDialog(this, "Sessió iniciada correctament!");
            iniciarVistaPrincipal();
            cp.setUsuari(user);
        } catch(LoginException ex) {
            JOptionPane.showMessageDialog(this, "Usuari o contrasenya incorrectes:\n" + ex.getMessage(), "Error d'autenticació", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gestiona el registre d'un nou usuari.
     */
    private void registrar() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();
        ControladorPresentacio cp = ControladorPresentacio.getInstance();
        if (cp.getCtrlDomini().registrarUsuari(user, pass)) {
            JOptionPane.showMessageDialog(this, "Compte creat i sessió iniciada!");
            iniciarVistaPrincipal();
            cp.setUsuari(user);
        } else {
            JOptionPane.showMessageDialog(this, "Aquest usuari ja existeix.");
        }
    }

    /**
     * Inicia la vista principal del joc després d'un login o registre correcte.
     */
    private void iniciarVistaPrincipal() {
        ControladorPresentacio ctrl = ControladorPresentacio.getInstance();
        ctrl.setUsuari(ctrl.getCtrlDomini().getUsuariActiu().getNom());
        VistaMenuPrincipal vp = new VistaMenuPrincipal();
        ctrl.setVista(vp);
        vp.mostrar();
        dispose();
    }

    /**
     * Mostra la finestra de login.
     */
    public void mostrar() {
        setVisible(true);
    }
}
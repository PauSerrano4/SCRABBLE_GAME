package presentacio;

import controller.CtrlUsuari;
import exceptions.LoginException;

import javax.swing.*;
import java.awt.*;

/**
 * Classe VistaLoginSegonJugador - Diàleg per a l'autenticació o registre del segon jugador.
 * Permet iniciar sessió o registrar un nou usuari diferent del primer jugador.
 */
public class VistaLoginSegonJugador extends JDialog {

    // ---------- ATRIBUTS ----------
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;
    

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe VistaLoginSegonJugador.
     * Inicialitza la interfície gràfica i els components per al segon jugador.
     * @param parent Finestra pare per a la modalitat.
     */
    public VistaLoginSegonJugador(JFrame parent) {  
        super(parent, "Login Segon Jugador", true); // "true" = modal
        setTitle("Scrabble - Login Segon Jugador");
        setSize(350, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        

        txtUser = new JTextField();
        txtPass = new JPasswordField();
        btnLogin = new JButton("Iniciar Sessió");

        JPanel panellCamps = new JPanel(new GridLayout(2, 2));
        panellCamps.setBorder(BorderFactory.createTitledBorder("Segon jugador"));

        panellCamps.add(new JLabel("Nom d'usuari:"));
        panellCamps.add(txtUser);
        panellCamps.add(new JLabel("Contrasenya:"));
        panellCamps.add(txtPass);

        JPanel panellBotons = new JPanel();
        panellBotons.add(btnLogin);

        add(panellCamps, BorderLayout.CENTER);
        add(panellBotons, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> login());
    }

    // ---------- MÈTODES ----------
    /**
     * Gestiona l'inici de sessió o registre del segon jugador.
     * Si el nom coincideix amb el primer jugador, mostra un avís.
     * Si no existeix, ofereix registrar-lo.
     */
    private void login() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();
        ControladorPresentacio cp = ControladorPresentacio.getInstance();
        String usuariActiu = cp.getCtrlDomini().getUsuariActiu().getNom();

        if (user.equalsIgnoreCase(usuariActiu)) {
            JOptionPane.showMessageDialog(this, "El segon jugador ha de ser diferent del primer.");
            return;
        }

        try {
            cp.getCtrlDomini().loginSegonUsuari(user, pass);
            JOptionPane.showMessageDialog(this, "Segon jugador autenticat correctament!");
            cp.setUsuari2(user);
            
            dispose();
        } catch(LoginException ex) {
            int resposta = JOptionPane.showConfirmDialog(
                this,
                "Usuari no trobat o contrasenya incorrecta.\nVols registrar aquest usuari com a segon jugador?",
                "Registrar nou usuari",
                JOptionPane.YES_NO_OPTION
            );

            if (resposta == JOptionPane.YES_OPTION) {
                if (cp.getCtrlDomini().registraSegonUsuari(user, pass)) {
                    JOptionPane.showMessageDialog(this, "Usuari registrat correctament com a segon jugador!");
                    cp.setUsuari2(user);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Aquest nom d'usuari ja existeix o no és vàlid.");
                }
            }
        }
    }

    /**
     * Mostra el diàleg de login del segon jugador.
     */
    public void mostrar() {
        setVisible(true);
    }
}

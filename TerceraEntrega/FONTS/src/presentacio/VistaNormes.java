package presentacio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

/**
 * Classe VistaNormes - Mostra les normes i el manual d'usuari del joc Scrabble.
 */
public class VistaNormes extends JFrame {

    // ---------- ATRIBUTS ----------
    private JTextArea areaNormes;

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe VistaNormes.
     * Inicialitza la finestra i mostra les normes del joc.
     */
    public VistaNormes() {
        setTitle("Normes i Manual d'Usuari - Scrabble");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // --------- PANEL PRINCIPAL ---------
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(245, 250, 255));
        panelPrincipal.setBorder(new EmptyBorder(20, 30, 20, 30));
        setContentPane(panelPrincipal);

        // --------- CAPÇALERA AMB ICONA ---------
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelHeader.setOpaque(false);

        // Icona Scrabble + Títol "Ajuda"
        URL urlIcona = getClass().getResource("../../FONTS/src/presentacio/resources/icons/ajuda.png");
        if (urlIcona != null) {
            ImageIcon icon = new ImageIcon(urlIcona);
            Image scaled = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            JLabel lblIcona = new JLabel(new ImageIcon(scaled));
            panelHeader.add(lblIcona);
            // Títol "Ajuda"
            JLabel lblAjuda = new JLabel("Normes i Manual d'Usuari");
            lblAjuda.setFont(new Font("Serif", Font.BOLD, 26));
            lblAjuda.setForeground(new Color(44, 62, 80));
            panelHeader.add(lblAjuda);
        }

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        // --------- TEXT FORMATAT ---------
        areaNormes = new JTextArea();
        areaNormes.setEditable(false);
        areaNormes.setLineWrap(true);
        areaNormes.setWrapStyleWord(true);
        areaNormes.setFont(new Font("SansSerif", Font.PLAIN, 15));
        areaNormes.setBackground(new Color(255, 255, 255));
        areaNormes.setBorder(new EmptyBorder(20, 20, 20, 20));

        areaNormes.setText(
                "NORMATIVA SCRABBLE\n"
                + "────────────────────────────\n"
                + "1. El tauler és de 15x15 caselles. Cada jugador comença amb 7 fitxes.\n"
                + "2. La primera paraula ha de passar pel centre del tauler (casella central).\n"
                + "3. Les paraules es poden col·locar horitzontalment o verticalment, i han d'estar connectades a fitxes ja col·locades.\n"
                + "4. Només són vàlides les paraules que existeixen al diccionari seleccionat.\n"
                + "5. Es validen també les paraules perpendiculars formades en el torn.\n"
                + "6. Els multiplicadors (DL, TL, DP, TP) només s’apliquen la primera vegada que s’utilitzen.\n"
                + "7. Si un jugador utilitza totes les fitxes del faristol en un sol torn, rep una bonificació de 50 punts.\n"
                + "8. Es poden canviar fitxes si n’hi ha suficients a la bossa.\n"
                + "9. La partida acaba quan:\n"
                + "   - No queden fitxes a la bossa i un jugador es queda sense fitxes.\n"
                + "   - Cap jugador pot jugar en dos torns consecutius.\n"
                + "10. En mode contrarellotge, el temps limita la durada del torn i/o la partida.\n"
                + "\n"
                + "REGLES ADDICIONALS DEL PROJECTE\n"
                + "───────────────────────────────\n"
                + "- Hi ha tres dificultats: FÀCIL (8 fitxes), NORMAL (7 fitxes), DIFÍCIL (6 fitxes).\n"
                + "- El centre del tauler multiplica per 2 la paraula en la primera jugada.\n"
                + "- El joc permet jugar contra la màquina o en mode multijugador local.\n"
                + "- El bot fa jugades estratègiques per maximitzar punts.\n"
                + "- Es poden consultar les normes i el manual en qualsevol moment.\n"
                + "\n"
                + "MANUAL D'USUARI\n"
                + "───────────────\n"
                + "• Inici del joc: Selecciona \"Jugar partida\" al menú principal. Escull idioma, dificultat i mode de joc.\n"
                + "• Gestió de fitxes: Arrossega fitxes del faristol al tauler. Pots retirar fitxes provisionalment col·locades.\n"
                + "• Confirmar jugada: Prem el botó \"Confirmar\" per validar la paraula. Si no és vàlida, perds el torn.\n"
                + "• Passar torn: Pots passar el torn si no pots o no vols jugar.\n"
                + "• Canviar fitxes: Prem \"Canviar fitxes\" i selecciona les fitxes a intercanviar (si n’hi ha prou a la bossa).\n"
                + "• Pausar la partida: Pots pausar la partida i reprendre-la més tard.\n"
                + "• Guardar i carregar: Pots guardar la partida i continuar-la més endavant des del menú de gestió de partides.\n"
                + "• Consultar rànquings i estadístiques: Accedeix als rànquings i estadístiques des del menú principal.\n"
                + "• Tancar sessió: Pots tancar la sessió o eliminar el teu compte des del menú de perfil.\n"
                + "\n"
                + "Per a més informació, consulta el manual complet o contacta amb l’equip de desenvolupament.\n"
        );

        JScrollPane scroll = new JScrollPane(areaNormes);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(123, 104, 238), 2));
        panelPrincipal.add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }
}

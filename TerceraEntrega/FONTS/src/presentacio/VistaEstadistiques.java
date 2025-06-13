package presentacio;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import util.Pair;


/**
 * Classe VistaEstadistiques - Mostra les estadístiques de l'usuari.
 */
public class VistaEstadistiques extends JFrame {

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe VistaEstadistiques.
     * Inicialitza la finestra i mostra les estadístiques de l'usuari.
     */
    public VistaEstadistiques() {
        ControladorPresentacio cp = ControladorPresentacio.getInstance();
        setTitle("Estadístiques de l'Usuari");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panellContingut = new JPanel();
        panellContingut.setLayout(new BoxLayout(panellContingut, BoxLayout.Y_AXIS));

        panellContingut.add(creaTitolAmbIcona("Total de punts:", "../FONTS/src/presentacio/resources/icons/trofeu.png"));
        JTextArea areaTotal = new JTextArea("   " + cp.getCtrlDomini().getUsuariActiu().getEstadistiques().getTotalPunts() + "\n");
        areaTotal.setEditable(false);
        panellContingut.add(areaTotal);

        panellContingut.add(creaTitolAmbIcona("Puntuació mitjana:", "../FONTS/src/presentacio/resources/icons/percentatge.png"));
        JTextArea areaMitjana = new JTextArea("   " + cp.getCtrlDomini().getUsuariActiu().getEstadistiques().getPuntuacioMitjana( ) + "\n");
        areaMitjana.setEditable(false);
        panellContingut.add(areaMitjana);

        panellContingut.add(creaTitolAmbIcona("Millor puntuació:", "../FONTS/src/presentacio/resources/icons/record.png"));
        JTextArea areaMillor = new JTextArea("   " + cp.getCtrlDomini().getUsuariActiu().getEstadistiques().getMillorPuntuacio() + "\n");
        areaMillor.setEditable(false);
        panellContingut.add(areaMillor);

        panellContingut.add(creaTitolAmbIcona("Partides guanyades:", "../FONTS/src/presentacio/resources/icons/record.png"));
        JTextArea areaPartides = new JTextArea("   " + cp.getCtrlDomini().getUsuariActiu().getEstadistiques().getHistorialPartides().size() + "\n");
        areaPartides.setEditable(false);
        panellContingut.add(areaPartides);


        panellContingut.add(creaTitolAmbIcona("Millor puntuació acumulada per diccionaris:", "../FONTS/src/presentacio/resources/icons/diccionari (2).png"));

        Map<String, Float> perDiccionari = cp.getCtrlDomini().getUsuariActiu().getEstadistiques().getEstadistiquesPerDiccionari();
        for (Map.Entry<String, Float> entry : perDiccionari.entrySet()) {
            String idioma = entry.getKey();
            float punts = entry.getValue();
            panellContingut.add(creaTitolAmbIcona("Diccionari " + idioma + ": " + punts + " punts", "../FONTS/src/presentacio/resources/icons/diccionari.png" ));
        }

        add(new JScrollPane(panellContingut), BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Crea un titol amb la seva imatge corresponent.
     * @param titol titol
     * @param rutaIcona lloc on es troba la imatge corresponent.
     */
    private JPanel creaTitolAmbIcona(String titol, String rutaIcona) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        ImageIcon iconaOriginal = new ImageIcon(rutaIcona);

        Image imatgeReduida = iconaOriginal.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        ImageIcon iconaPetita = new ImageIcon(imatgeReduida);

        JLabel label = new JLabel(titol, iconaPetita, JLabel.LEFT);
        panel.add(label);

        return panel;
    }
}

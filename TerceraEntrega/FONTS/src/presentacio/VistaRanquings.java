package presentacio;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.io.File;

/**
 * Classe VistaRanquings - Mostra els rànquings de jugadors segons diferents criteris.
 */
public class VistaRanquings extends JFrame {

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe VistaRanquings.
     * Mostra els diferents rànquings de jugadors.
     */
    public VistaRanquings() {
        ControladorPresentacio cp = ControladorPresentacio.getInstance();
        setTitle("Rànquings de Jugadors");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel panellContingut = new JPanel();
        panellContingut.setLayout(new BoxLayout(panellContingut, BoxLayout.Y_AXIS));
        setLayout(new BorderLayout());

        panellContingut.add(creaTitolAmbIcona("Millor puntuació acumulada:", "../FONTS/src/presentacio/resources/icons/trofeu.png"));
        JTextArea areaTotal = new JTextArea();
        areaTotal.setEditable(false);
        afegirRanquing(areaTotal, cp.getCtrlDomini().getRanquing().mostraRanquing("total"));
        panellContingut.add(areaTotal);

        panellContingut.add(creaTitolAmbIcona("Record de punts en una partida:", "../FONTS/src/presentacio/resources/icons/record.png"));
        JTextArea areaRecord = new JTextArea();
        areaRecord.setEditable(false);
        afegirRanquing(areaRecord, cp.getCtrlDomini().getRanquing().mostraRanquing("record"));
        panellContingut.add(areaRecord);

        panellContingut.add(creaTitolAmbIcona("Millor percentatge de puntuació per partida:", "../FONTS/src/presentacio/resources/icons/percentatge.png"));
        JTextArea areaPercentatge = new JTextArea();
        areaPercentatge.setEditable(false);
        afegirRanquing(areaPercentatge, cp.getCtrlDomini().getRanquing().mostraRanquing("percentatge"));
        panellContingut.add(areaPercentatge);

        panellContingut.add(creaTitolAmbIcona("Millor puntuació acumulada amb contrarellotge:", "../FONTS/src/presentacio/resources/icons/crono.png"));
        JTextArea areaCron = new JTextArea();
        areaCron.setEditable(false);
        afegirRanquing(areaCron, cp.getCtrlDomini().getRanquing().mostraRanquing("contrarellotge"));
        panellContingut.add(areaCron);

        panellContingut.add(creaTitolAmbIcona("Millor puntuació acumulada per diccionaris:", "../FONTS/src/presentacio/resources/icons/diccionari (2).png"));

        List<String> nomsDiccionaris = cp.getCtrlDomini().getNomDiccionaris();
        if (nomsDiccionaris.isEmpty()) {
            JTextArea areaBuida = new JTextArea("   [Encara no hi ha diccionaris disponibles]\n");
            areaBuida.setEditable(false);
            panellContingut.add(areaBuida);
        } else {
            for (String nomDiccionari : nomsDiccionaris) {
                panellContingut.add(creaTitolAmbIcona("Diccionari " + nomDiccionari + ":", "../FONTS/src/presentacio/resources/icons/diccionari.png"));
                JTextArea areaDicc = new JTextArea();
                areaDicc.setEditable(false);
                afegirRanquing(areaDicc, cp.getCtrlDomini().getRanquing().mostraRanquing("dic:" + nomDiccionari));
                panellContingut.add(areaDicc);
            }       
        }

        add(new JScrollPane(panellContingut), BorderLayout.CENTER);
        setVisible(true);
    }

    // ---------- MÈTODES PRIVATS ----------
    /**
     * Afegeix una llista de rànquing a l'àrea de text.
     * @param area JTextArea on afegir el rànquing.
     * @param llista Llista de resultats a mostrar.
     */
    private void afegirRanquing(JTextArea area, List<String> llista) {
        if (llista.isEmpty()) {
            area.append("   [Encara no hi ha dades]\n");
        } else {
            for (String linia : llista) {
                area.append("   " + linia + "\n");
            }
        }
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

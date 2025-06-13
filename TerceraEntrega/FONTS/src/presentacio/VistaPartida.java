package presentacio;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import util.*;
import java.net.URL;
import java.awt.Image;




import model.Tauler;
import util.*;
import exceptions.*;

/**
 * Classe VistaPartida - Vista principal de la partida d'Scrabble.
 * Gestiona la visualització del tauler, el faristol, l'estat dels jugadors i les accions de joc.
 * @author Alexander de Jong i Pau Serrano
 */
public class VistaPartida extends JFrame implements TemporitzadorListener {

    // ---------- ATRIBUTS ----------
    private static final int MIDA = 15;
    private ControladorPresentacio controlador;
    private JPanel panelTauler;
    private JButton[][] caselles;

    // Panell estat jugadors
    private JLabel labelNom1, labelPunts1, labelFitxes1;
    private JLabel labelNom2, labelPunts2, labelFitxes2;
    private JLabel labelTorn;
    private JLabel labelTemps;
    private JLabel labelFitxesBossa;
    private JTextArea areaHistorial;
    private JScrollPane scrollHistorial;
    JPanel panelDret = new JPanel(new BorderLayout());

    // Panell faristol i fitxes
    private JPanel panelFaristol;
    private List<JButton> fitxesJugador;
    private Map<JButton, Pair<Integer, Integer>> origenFitxes;
    private final List<PosicioFitxa> fitxesTornActual = new ArrayList<>();
    private List<PosicioFitxa> posicionsColocades;

    // Botons d'acció
    private JButton botoConfirmar, botoRetirar;

    // ---------- CONSTRUCTOR ----------
    /**
     * Constructora de la classe VistaPartida.
     * Inicialitza la finestra i els components de la partida.
     * @param controlador Controlador de presentació associat.
     */
    public VistaPartida(ControladorPresentacio controlador) {
        this.controlador = controlador;
        inicialitzarComponents();
    }

    // ---------- MÈTODES D'INICIALITZACIÓ ----------
    /**
     * Inicialitza tots els components gràfics de la vista de partida.
     */
    private void inicialitzarComponents() {
        setTitle("Scrabble - Partida");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === TAULER ===
        panelTauler = new JPanel();
        panelTauler.setLayout(new GridLayout(MIDA, MIDA));
        caselles = new JButton[MIDA][MIDA];

        for (int i = 0; i < MIDA; i++) {
            for (int j = 0; j < MIDA; j++) {
                JButton botoCasella = new JButton();
                botoCasella.setFont(new Font("SansSerif", Font.BOLD, 10));
                caselles[i][j] = botoCasella;
                panelTauler.add(botoCasella);

                final int fila = i;
                final int col = j;

                botoCasella.setTransferHandler(new TransferHandler("text") {
                    @Override
                    public boolean canImport(TransferSupport support) {
                        return support.isDataFlavorSupported(DataFlavor.stringFlavor);
                    }

                    @Override
                    public boolean importData(TransferSupport support) {
                        try {
                            String actual = caselles[fila][col].getText();
                            if (!actual.isEmpty() &&
                                !actual.equals("C") &&
                                !actual.equals("DL") &&
                                !actual.equals("DP") &&
                                !actual.equals("TL") &&
                                !actual.equals("TP")) {
                                return false;
                            }

                            String text = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                            caselles[fila][col].setText(text);

                            int idxParentesi = text.indexOf('(');
                            String lletraOriginal = text.substring(0, idxParentesi);
                            String lletraUnificada = DigrafMapper.convertirLletra(lletraOriginal, controlador.getIdioma());
                            char lletra = lletraUnificada.charAt(0);

                            posicionsColocades.add(new PosicioFitxa(fila, col, lletra, false));

                            for (JButton btn : fitxesJugador) {
                                if (btn.getText().equals(text)) {
                                    panelFaristol.remove(btn);
                                    fitxesJugador.remove(btn);
                                    break;
                                }
                            }

                            panelFaristol.revalidate();
                            panelFaristol.repaint();

                            return true;

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            return false;
                        }
                    }
                });
            }
        }

        add(panelTauler, BorderLayout.CENTER);

        // === BOTONS INFERIORS ===
        JPanel panelBotons = new JPanel();

        JButton botoSortir = crearBotoAccio("Sortir", "../../FONTS/src/presentacio/resources/icons/logout.png", new Color(46, 204, 113));
        //JButton botoLogout = crearBotoAccio("Logout", "../../FONTS/src/presentacio/resources/icons/logout.png", new Color(46, 204, 113));
        JButton botoAbandonar = crearBotoAccio("Abandonar", "../../FONTS/src/presentacio/resources/icons/abandonar.png", new Color(231, 76, 60));

        panelBotons.add(botoSortir);
        panelBotons.add(botoAbandonar);
        //panelBotons.add(botoLogout);

        add(panelBotons, BorderLayout.SOUTH);

        botoSortir.addActionListener(e -> controlador.sortir());

        botoAbandonar.addActionListener(e -> {
            int resposta = JOptionPane.showConfirmDialog(this, "Estàs segur que vols abandonar la partida?", "Abandonar partida", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                controlador.abandonarPartida();
                dispose();
            }
        });

        // === PANEL ESTAT JUGADORS ===
        JPanel panelEstat = new JPanel();
        panelEstat.setLayout(new BoxLayout(panelEstat, BoxLayout.Y_AXIS));

        labelNom1 = new JLabel("Jugador 1: ");
        labelPunts1 = new JLabel("Punts: ");
        labelFitxes1 = new JLabel("Fitxes: ");

        labelNom2 = new JLabel("Jugador 2: ");
        labelPunts2 = new JLabel("Punts: ");
        labelFitxes2 = new JLabel("Fitxes: ");

        labelTorn = new JLabel("Torn de: ∞");
        labelTemps = new JLabel("Temps restant: ∞");
        labelFitxesBossa = new JLabel("Fitxes a la bossa: " + controlador.getNumFitxesBossa());
        labelTemps.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelFitxesBossa.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Alineació a l'esquerra per tots els elements
        labelNom1.setAlignmentX(LEFT_ALIGNMENT);
        labelPunts1.setAlignmentX(LEFT_ALIGNMENT);
        labelFitxes1.setAlignmentX(LEFT_ALIGNMENT);
        labelNom2.setAlignmentX(LEFT_ALIGNMENT);
        labelPunts2.setAlignmentX(LEFT_ALIGNMENT);
        labelFitxes2.setAlignmentX(LEFT_ALIGNMENT);
        labelTorn.setAlignmentX(LEFT_ALIGNMENT);
        labelFitxesBossa.setAlignmentX(LEFT_ALIGNMENT);

        // Panell temps amb icona i text, també alineat a l'esquerra
        JPanel panelTemps = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelTemps.setAlignmentX(LEFT_ALIGNMENT);
        labelTemps = new JLabel("Temps restant: ∞");
        labelTemps.setFont(new Font("SansSerif", Font.BOLD, 14));
        URL urlCrono = getClass().getResource("../../FONTS/src/presentacio/resources/icons/crono.png");
        if (urlCrono != null) {
            ImageIcon iconCrono = new ImageIcon(urlCrono);
            Image scaledCrono = iconCrono.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            JLabel labelIconaTemps = new JLabel(new ImageIcon(scaledCrono));
            panelTemps.add(labelIconaTemps);
        }
        panelTemps.add(labelTemps);

        // Afegir elements al panell d'estat
        panelEstat.add(labelNom1);
        panelEstat.add(labelPunts1);
        panelEstat.add(labelFitxes1);

        panelEstat.add(Box.createVerticalStrut(20));

        panelEstat.add(labelNom2);
        panelEstat.add(labelPunts2);
        panelEstat.add(labelFitxes2);

        panelEstat.add(Box.createVerticalStrut(20));
        panelEstat.add(labelTorn);

        panelEstat.add(Box.createVerticalStrut(20));
        panelEstat.add(panelTemps);

        panelEstat.add(Box.createVerticalStrut(20));
        panelEstat.add(labelFitxesBossa);

        // Just abans d’afegir labelFitxesBossa al panelEstat, substitueix això:
        // panelEstat.add(labelFitxesBossa);

        // Per aquest bloc:
        JPanel panelFitxesBossa = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelFitxesBossa.setAlignmentX(LEFT_ALIGNMENT);
        URL urlFitxa = getClass().getResource("../../FONTS/src/presentacio/resources/icons/fitxa.png");
        if (urlFitxa != null) {
            ImageIcon iconFitxa = new ImageIcon(urlFitxa);
            Image scaledFitxa = iconFitxa.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            JLabel labelIconaFitxa = new JLabel(new ImageIcon(scaledFitxa));
            panelFitxesBossa.add(labelIconaFitxa);
        }
        panelFitxesBossa.add(labelFitxesBossa);
        panelEstat.add(panelFitxesBossa);

        // Afegeix un espai vertical abans de l'historial
        panelEstat.add(Box.createVerticalStrut(20));

        // Historial de jugades (part inferior del panell dret)
        JPanel panelHistorial = new JPanel(new BorderLayout());
        panelHistorial.setBorder(BorderFactory.createTitledBorder("Historial de jugades"));
        
        areaHistorial = new JTextArea(15, 25);
        areaHistorial.setEditable(false);
        areaHistorial.setFont(new Font("Courier New", Font.PLAIN, 12));
        areaHistorial.setBackground(new Color(248, 248, 248));
        
        scrollHistorial = new JScrollPane(areaHistorial);
        scrollHistorial.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollHistorial.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        panelHistorial.add(scrollHistorial, BorderLayout.CENTER);
        
        // Botó per netejar historial (opcional)
        JButton botoNetejaaHistorial = new JButton("Netejar");
        botoNetejaaHistorial.setFont(new Font("SansSerif", Font.PLAIN, 10));
        botoNetejaaHistorial.addActionListener(e -> {
            areaHistorial.setText("");
            areaHistorial.append("--- Historial netejat ---\n");
        });
        panelHistorial.add(botoNetejaaHistorial, BorderLayout.SOUTH);

        // Assemblar panell dret
        panelDret.add(panelEstat, BorderLayout.NORTH);
        panelDret.add(panelHistorial, BorderLayout.CENTER);
        panelDret.setPreferredSize(new Dimension(300, 600));

        add(panelDret, BorderLayout.EAST);

        //Eliminat botó innecessari
        //botoLogout = crearBotoAccio("Tancar sessió", "/resources/icons/logout.png", new Color(231, 76, 60));
        //panelEstat.add(botoLogout);
        //botoLogout.addActionListener(e -> controlador.tancarSessio());

        JButton botoPausa = crearBotoAccio("Pausar", "../../FONTS/src/presentacio/resources/icons/pausa.png", new Color(192, 57, 43));
        botoPausa.addActionListener(e -> controlador.pausarPartida());
        panelBotons.add(botoPausa);

        // === PANELL INFERIOR AMB FARISTOL + BOTONS CONFIRMAR/RETIRAR ===
        JPanel panelInferior = new JPanel(new BorderLayout());

        panelFaristol = new JPanel(new FlowLayout());
        fitxesJugador = new ArrayList<>();
        origenFitxes = new HashMap<>();
        posicionsColocades = new ArrayList<>();

        JButton botoConfirmar = crearBotoAccio("Confirmar", "../../FONTS/src/presentacio/resources/icons/confirmar.png", new Color(46, 204, 113));
        JButton botoRetirar = crearBotoAccio("Retirar fitxes", "../../FONTS/src/presentacio/resources/icons/desfer.png", new Color(255, 165, 0));
        JButton botoPassar = crearBotoAccio("Passar torn", "../../FONTS/src/presentacio/resources/icons/passar.png", new Color(52, 152, 219));

        botoPassar.addActionListener(e -> controlador.passarTorn(1));
        botoRetirar.addActionListener(e -> retirarFitxes());
        botoConfirmar.addActionListener(e -> {
            confirmarJugadaHumana(new ArrayList<>(posicionsColocades));
            posicionsColocades.clear();
        });
        panelInferior.add(panelFaristol, BorderLayout.CENTER);

        JPanel panelAccions = new JPanel();
        panelAccions.add(botoPassar);
        panelAccions.add(botoConfirmar);
        panelAccions.add(botoRetirar);
        panelInferior.add(panelAccions, BorderLayout.EAST);

        // Botó per canviar fitxes
        JButton botoCanviar = crearBotoAccio("Canviar fitxes", "../../FONTS/src/presentacio/resources/icons/canviar.png", new Color(155, 89, 182));
        panelAccions.add(botoCanviar);
        botoCanviar.addActionListener(e -> obrirDialogCanvi());

        // Botó per reordenar fitxes
        JButton botoReordenar = crearBotoAccio("Reordenar fitxes", "../../FONTS/src/presentacio/resources/icons/reordenar.png", new Color(127, 140, 141));
        panelAccions.add(botoReordenar);
        botoReordenar.addActionListener(e -> reordenarFitxes());

        add(panelInferior, BorderLayout.NORTH);

        pack();
        setMinimumSize(new Dimension(800, 600));
        inicialitzarHistorial();
    }

    // Estil uniforme per botons principals
    private JButton crearBotoAccio(String text, String iconaPath, Color color) {
        URL url = getClass().getResource(iconaPath);
        if (url == null) {
            //System.err.println("No s'ha trobat la icona: " + iconaPath);
            return new JButton(text);
        }
        ImageIcon icon = new ImageIcon(url);
        Image scaled = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton boto = new JButton(text, new ImageIcon(scaled));

        boto.setFont(new Font("SansSerif", Font.BOLD, 14));
        boto.setBackground(color);
        boto.setForeground(Color.WHITE);
        boto.setFocusPainted(false);
        boto.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return boto;
    }


    // ---------- GETTERS ----------
    /**
     * Retorna la llista de posicions provisionalment col·locades.
     * @return Llista de PosicioFitxa.
     */
    public List<PosicioFitxa> getPosicionsColocades() {
        return posicionsColocades;
    }

    // ---------- MÈTODES PÚBLICS ----------
    /**
     * Mostra la finestra de la partida.
     */
    public void mostrar() {
        setVisible(true);
    }

    /**
     * Actualitza el  tauler gràficament segons l'estat del model.
     */
    public void actualitzarTauler() {
        
        for (int i = 0; i < MIDA; i++) {
            for (int j = 0; j < MIDA; j++) {
                String text = "";
                JButton boto = caselles[i][j];

                if (controlador.getCtrlDomini().getTauler().getCasella(i, j).teFitxa()) {
                    Character lletra = controlador.getCtrlDomini().getTauler().getCasella(i, j).getFitxa().getLletra();
                    String idioma = controlador.getIdioma();
                    text = DigrafMapper.desferConversio(String.valueOf(lletra), idioma);
                    boto.setBackground(Color.WHITE);
                } else {
                    String mult = controlador.getCtrlDomini().getTauler().getCasella(i, j).getMultiplicador();
                    if (mult != null && !mult.equals("")) {
                        text = mult;
                        switch (mult) {
                            case "TP":
                                boto.setBackground(new Color(255, 102, 102));
                                break;
                            case "DP":
                                boto.setBackground(new Color(255, 178, 102));
                                break;
                            case "TL":
                                boto.setBackground(new Color(102, 178, 255));
                                break;
                            case "DL":
                                boto.setBackground(new Color(153, 255, 255));
                                break;
                            default:
                                boto.setBackground(null);
                                break;
                        }
                    } else {
                        boto.setBackground(new Color(1, 200, 1));
                    }
                }
                boto.setText(text);
            }
        }
    }

    /**
     * Inicialitza l'historial amb un missatge de benvinguda.
     */
    private void inicialitzarHistorial() {
        areaHistorial.setText("HISTORIAL DE JUGADES \n");
        areaHistorial.append("Partida iniciada\n");
        areaHistorial.append("\n\n");
    }

    /**
     * Actualitza l'historial de jugades mostrant totes les jugades realitzades.
     */
    public void actualitzarHistorial() {
        String jugades = controlador.getCtrlDomini().getTextJugades();
        // Netejar i reescriure tot l'historial
        areaHistorial.setText(" HISTORIAL DE JUGADES \n");
        areaHistorial.append("Partida iniciada\n");
        areaHistorial.append("\n\n");
        
        
        areaHistorial.append(jugades);
        // Scroll automàtic al final
        areaHistorial.setCaretPosition(areaHistorial.getDocument().getLength());
    }

    /**
     * Actualitza l'historial amb el text complet rebut des de presentació.
     * Substitueix completament el contingut de l'historial.
     * @param textHistorial Text complet de l'historial formatejat
     */
    public void actualitzarHistorialComplet(String textHistorial) {
        areaHistorial.setText(textHistorial);
        
        // Scroll automàtic al final
        areaHistorial.setCaretPosition(areaHistorial.getDocument().getLength());
    }

    /**
     * Afegeix text a l'historial de manera incremental.
     * Permet afegir noves línies sense substituir tot el contingut.
     * @param nouText Text a afegir a l'historial
     */
    public void afegirTextHistorial(String nouText) {
        areaHistorial.append(nouText);
        
        // Scroll automàtic al final
        areaHistorial.setCaretPosition(areaHistorial.getDocument().getLength());
    }

    /**
     * Neteja l'historial i estableix text inicial.
     * @param textInicial Text inicial per mostrar (opcional)
     */
    public void netejarHistorial(String textInicial) {
        if (textInicial != null && !textInicial.isEmpty()) {
            areaHistorial.setText(textInicial);
        } else {
            inicialitzarHistorial();
        }
        
        // Scroll al principi
        areaHistorial.setCaretPosition(0);
    }

    /**
     * Actualitza l'estat dels jugadors a la vista.
     * @param nom1 Nom del jugador 1.
     * @param punts1 Punts del jugador 1.
     * @param fitxes1 Fitxes del jugador 1.
     * @param nom2 Nom del jugador 2.
     * @param punts2 Punts del jugador 2.
     * @param fitxes2 Fitxes del jugador 2.
     * @param tornActual Torn actual (0 o 1).
     */
    public void actualitzarEstatJugadors(String nom1, int punts1, String fitxes1, String nom2, int punts2, String fitxes2, int tornActual) {
        String idioma = controlador.getIdioma();

        labelNom1.setText("Jugador 1: " + nom1);
        labelPunts1.setText("Punts: " + punts1);
        labelFitxes1.setText("Fitxes: " + convertirFitxes(fitxes1, idioma));

        labelNom2.setText("Jugador 2: " + nom2);
        labelPunts2.setText("Punts: " + punts2);
        labelFitxes2.setText("Fitxes: " + convertirFitxes(fitxes2, idioma));

        if (tornActual == 0) labelTorn.setText("Torn de: " + nom1);
        else labelTorn.setText("Torn de: " + nom2);

        actualitzarFitxesBossa();

        if (tornActual == 0) {
            if (esJugadorHuma(nom1)) mostrarFitxesJugador(fitxes1, idioma);
            else buidarFaristol();
        } else {
            if (esJugadorHuma(nom2)) mostrarFitxesJugador(fitxes2, idioma);
            else buidarFaristol();
        }
        
        // Actualitzar historial automàticament
        actualitzarHistorial();
    }

    /**
     * Actualitza el temps restant a la vista.
     * @param temps Temps restant.
     */
    public void actualitzarTemps(String temps) {
        labelTemps.setText("Temps restant: " + temps);
    }

    /**
     * Actualitza el nombre de fitxes a la bossa a la vista.
     */
    public void actualitzarFitxesBossa() {
        labelFitxesBossa.setText("Fitxes a la bossa: " + controlador.getNumFitxesBossa());
    }

    /**
     * Mostra les fitxes del jugador humà al faristol.
     * @param fitxes Fitxes a mostrar.
     * @param idioma Idioma del joc.
     */
    public void mostrarFitxesJugador(String fitxes, String idioma) {
        if (fitxes == null || fitxes.isEmpty()) return;
        if (!esJugadorHuma(labelTorn.getText().replace("Torn de: ", ""))) return;
        panelFaristol.removeAll();
        fitxesJugador.clear();
        origenFitxes.clear();

        String[] tokens = fitxes.trim().split("\\s+");

        for (String token : tokens) {
            if (token.length() >= 3 && token.charAt(1) == '(' && token.endsWith(")")) {
                String lletraUnificada = token.substring(0, 1);
                String valor = token.substring(2, token.length() - 1);
                String lletraOriginal = DigrafMapper.desferConversio(lletraUnificada, idioma);

                JButton botoFitxa = new JButton(lletraOriginal + "(" + valor + ")");
                botoFitxa.setTransferHandler(new TransferHandler() {
                    @Override
                    protected Transferable createTransferable(JComponent c) {
                        JButton btn = (JButton) c;
                        return new StringSelection(btn.getText());
                    }

                    @Override
                    public int getSourceActions(JComponent c) {
                        return COPY;
                    }
                });

                botoFitxa.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            JComponent c = (JComponent) e.getSource();
                            TransferHandler handler = c.getTransferHandler();
                            handler.exportAsDrag(c, e, TransferHandler.COPY);
                        }
                    }
                });

                fitxesJugador.add(botoFitxa);
                panelFaristol.add(botoFitxa);
            }
        }

        panelFaristol.revalidate();
        panelFaristol.repaint();
    }

    /**
     * Obre el diàleg per canviar fitxes del faristol.
     */
    private void obrirDialogCanvi() {
        if (fitxesJugador.isEmpty()) return;

        JPanel pnl = new JPanel(new GridLayout(0, 5, 5, 5));
        Map<JCheckBox, Character> map = new HashMap<>();

        for (JButton btn : fitxesJugador) {
            String text = btn.getText();
            int idxParentesi = text.indexOf('(');
            String lletraOriginal = text.substring(0, idxParentesi);
            String lletraUnificada = DigrafMapper.convertirLletra(lletraOriginal, controlador.getIdioma());
            char lletra = lletraUnificada.charAt(0);

            JCheckBox cb = new JCheckBox(text);
            map.put(cb, lletra);
            pnl.add(cb);
        }

        int res = JOptionPane.showConfirmDialog(this, pnl,
                "Selecciona les fitxes a canviar", JOptionPane.OK_CANCEL_OPTION);

        if (res == JOptionPane.OK_OPTION) {
            List<Character> lletres = new ArrayList<>();
            for (Map.Entry<JCheckBox, Character> ent : map.entrySet())
                if (ent.getKey().isSelected()) lletres.add(ent.getValue());

            if (!lletres.isEmpty())
                canviarFitxes(lletres);
        }
    }

    /**
     * Reordena les fitxes del faristol visualment de manera aleatòria.
     */
    private void reordenarFitxes() {
        Collections.shuffle(fitxesJugador); // Reordena la llista
        panelFaristol.removeAll();          // Esborra els botons del panell

        for (JButton btn : fitxesJugador)   // Torna a afegir els botons en el nou ordre
            panelFaristol.add(btn);

        panelFaristol.revalidate();         // Actualitza la vista
        panelFaristol.repaint();
    }


    /**
     * Esborra totes les fitxes del panell faristol.
     */
    private void buidarFaristol() {
        panelFaristol.removeAll();
        fitxesJugador.clear();
        panelFaristol.revalidate();
        panelFaristol.repaint();
    }

    /**
     * Retira les fitxes provisionalment col·locades i les retorna al faristol.
     */
    private void retirarFitxes() {
        for (PosicioFitxa p : posicionsColocades) {
            String text = caselles[p.getFila()][p.getCol()].getText();
            if (!text.isEmpty()) {
                JButton botoFitxa = new JButton(text);
                botoFitxa.setTransferHandler(new TransferHandler() {
                    @Override
                    protected Transferable createTransferable(JComponent c) {
                        return new StringSelection(((JButton) c).getText());
                    }

                    @Override
                    public int getSourceActions(JComponent c) {
                        return COPY;
                    }
                });
                botoFitxa.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            JComponent c = (JComponent) e.getSource();
                            TransferHandler handler = c.getTransferHandler();
                            handler.exportAsDrag(c, e, TransferHandler.COPY);
                        }
                    }
                });

                fitxesJugador.add(botoFitxa);
                panelFaristol.add(botoFitxa);
            }
            JButton botoCasella = caselles[p.getFila()][p.getCol()];
            Color color = botoCasella.getBackground();

            if (color.equals(new Color(255, 102, 102)))      botoCasella.setText("TP");
            else if (color.equals(new Color(255, 178, 102))) botoCasella.setText("DP");
            else if (color.equals(new Color(102, 178, 255))) botoCasella.setText("TL");
            else if (color.equals(new Color(153, 255, 255))) botoCasella.setText("DL");
            else if (p.getFila() == 7 && p.getCol() == 7)    botoCasella.setText("C");
            else                                             botoCasella.setText("");
        }
        panelFaristol.revalidate();
        panelFaristol.repaint();
        posicionsColocades.clear();
        origenFitxes.clear();
    }

    /**
     * Converteix la cadena de fitxes per mostrar-les correctament segons l'idioma.
     * @param fitxes Cadena de fitxes.
     * @param idioma Idioma del joc.
     * @return Cadena convertida.
     */
    String convertirFitxes(String fitxes, String idioma) {
        StringBuilder resultat = new StringBuilder();
        String[] tokens = fitxes.trim().split("\\s+");
        for (String token : tokens) {
            if (token.length() >= 3 && token.charAt(1) == '(' && token.endsWith(")")) {
                String lletraUnificada = token.substring(0, 1);
                String valor = token.substring(2, token.length() - 1);
                String lletraOriginal = DigrafMapper.desferConversio(lletraUnificada, idioma);
                resultat.append(lletraOriginal).append("(").append(valor).append(") ");
            } else {
                resultat.append(token).append(" ");
            }
        }
        return resultat.toString().trim();
    }

    /**
     * S'invoca cada segon pel temporitzador per actualitzar el temps restant.
     * @param tempsRestant Temps restant en format String.
     */
    @Override
    public void onTick(String tempsRestant) {
        SwingUtilities.invokeLater(() -> actualitzarTemps(tempsRestant));
    }

    /**
     * S'invoca quan el temporitzador arriba a zero.
     */
    @Override
    public void onFinal() {
        SwingUtilities.invokeLater(() -> labelTemps.setText("Temps finalitzat!"));
        ControladorPresentacio cp = ControladorPresentacio.getInstance();
        cp.tempsAcabat();
    }

    /**
     * Esborra les marques de fitxes col·locades provisionalment.
     */
    public void buidarBufferTorn() {
        posicionsColocades.clear();
        origenFitxes.clear();
    }

    /**
     * Retorna true si el nom NO comença per "BOT" (jugador humà).
     * @param nom Nom del jugador.
     * @return Cert si és jugador humà.
     */
    private boolean esJugadorHuma(String nom) {
        return !(nom.startsWith("BOT"));
    }

    private void confirmarJugadaHumana(List<PosicioFitxa> posicions){
        try {
            controlador.confirmarJugadaHumana(posicions);

        } catch (JugadaInvalidaException e) {
            JOptionPane.showMessageDialog(this,
                "Jugada no vàlida: " + e.getMessage() + "\nEl teu torn s'ha perdut.");
            controlador.passarTorn(0);
        }
    }

    private void canviarFitxes(List<Character> lletres){
        try {
            controlador.canviarFitxes(lletres);
        } catch (IntercanviFitxesException ex) {
            JOptionPane.showMessageDialog(
                this,
                "No s'ha pogut intercanviar fitxes:\n" + ex.getMessage(),
                "Error intercanvi fitxes",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

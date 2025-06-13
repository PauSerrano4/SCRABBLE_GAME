package presentacio;

import controller.ControladorDomini;

public class ControladorPresentacio {

    private ControladorDomini ctrlDomini;
    private VistaTerminal vt;

    public ControladorPresentacio() {
        ctrlDomini = new ControladorDomini();
        vt = new VistaTerminal(this);

        vt.inicialitzaTerminal();
    }

    public void inicialitzarPartida() {
        ctrlDomini.inicialitzarPartida();
    }
    public void importaDiccionari(String alfabet, String diccionari, String idioma) {
        ctrlDomini.importaDiccionari(alfabet, diccionari, idioma);
    }

    public void mostrarEstatPartida() {
        ctrlDomini.mostrarEstatPartida();
    }

    public void jugarTorn() {
        ctrlDomini.jugarTorn();
    }
    
}
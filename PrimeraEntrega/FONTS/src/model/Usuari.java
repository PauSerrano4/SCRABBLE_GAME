package model;
import java.util.*;

/**
 * Classe que representa a un usuari del sistema
 * @autor Ferran Blanchart Reyes | ferran.blanchart@estudiantat.upc.edu
 */
public class Usuari {

    /** Guarda el nom escollit per l'usuari */
    private String nomUsuari;
    /** Guarda la contrasenya escollida per l'usuari */
    private String contrasenya;
    /** Guarda les estadístiques de l'usuari */
    private Estadistiques estadistiques;
    /** Bool que determina si l'usuari ha estat eliminat */
    private boolean eliminat;

    //----------CONSTRUCTORES----------

    /** crea un usuari amb el nom i contrassenya indicats */
    public Usuari(String nom, String contra) {
        this.nomUsuari = nom;
        this.contrasenya = contra;
        this.estadistiques = new Estadistiques();
        this.eliminat = false;
    }

    //----------CONSULTORES---------

    /** retorna el nom de l'usuari en cas de que no estigui eliminat */
    public String getNom() {
        return eliminat ? "Usuari eliminat" : nomUsuari;
    }

    /** retorna la contrasenya de l'usuari */
    public String getContrasenya() {
        return contrasenya;
    }

    /** retorna una classe Estadistiques amb les estadistiques de l'usuari */
    public Estadistiques getEstadistiques() {
        return estadistiques;
    }
    
    /** retorna un bool que es cert si l'usuari ha estat elimiant */
    public boolean esEliminat() {
        return eliminat;
    }

    //----------MODIFICADORES---------- 

    /** canvia el nom de l'usuari amb l'ndicat a la seva capcelera */
    public void canviarNom(String nouNom) {
        if (!eliminat) this.nomUsuari = nouNom;
    }

    /** canvia la contrasenya de l'usuari amb la indicada a la seva capcelera */
    public void canviarContrasenya(String novaContra) {
        if (!eliminat) this.contrasenya = novaContra;
    }

    /** posa tots els atributs de l'usuari a null i eliminat a true */
    public void eliminarUsuari() {
        this.nomUsuari = null;
        this.contrasenya = null;
        this.estadistiques = null;
        this.eliminat = true;
    }

    
}


package model;

import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Classe que representa a un usuari del sistema.
 * Gestiona el nom, contrasenya, estadístiques i estat d'eliminació de l'usuari.
 * @author Ferran Blanchart Reyes | ferran.blanchart@estudiantat.upc.edu
 */
public class Usuari implements Serializable {

    /** Guarda el nom escollit per l'usuari */
    private String nomUsuari;
    /** Guarda la contrasenya escollida per l'usuari */
    private String contrasenya;
    /** Guarda les estadístiques de l'usuari */
    private Estadistiques estadistiques;
    /** Bool que determina si l'usuari ha estat eliminat */
    private boolean eliminat;
    /** Bool que determina si es tracta de l'usuari màquina */
    private boolean esMaquina;

    //----------CONSTRUCTORS----------

    /**
     * Crea un usuari amb el nom i contrasenya indicats.
     * @param nom Nom de l'usuari
     * @param contra Contrasenya de l'usuari
     * @param maquina Indica si és usuari màquina
     */
    public Usuari(String nom, String contra, boolean maquina) {
        this.nomUsuari = nom;
        this.contrasenya = contra;
        carregarEstadistiques();
        this.eliminat = false;
        this.esMaquina = maquina;
    }

    /**
     * Crea i retorna un usuari màquina.
     * @return Usuari màquina
     */
    public static Usuari crearMaquina() {
        return new Usuari("MAQUINA", "", true);
    }

    //----------GETTERS & SETTERS----------

    /**
     * Retorna la contrasenya de l'usuari.
     * @return Contrasenya
     */
    public String getContrasenya() {
        return contrasenya;
    }

    /**
     * Retorna les estadístiques de l'usuari.
     * @return Estadistiques
     */
    public Estadistiques getEstadistiques() {
        return estadistiques;
    }

    /**
     * Retorna el nom de l'usuari en cas de que no estigui eliminat.
     * @return Nom de l'usuari o "Usuari eliminat" si està eliminat
     */
    public String getNom() {
        return eliminat ? "Usuari eliminat" : nomUsuari;
    }

    /**
     * Indica si l'usuari ha estat eliminat.
     * @return Cert si eliminat, fals altrament
     */
    public boolean esEliminat() {
        return eliminat;
    }

    /**
     * Indica si es tracta de l'usuari màquina.
     * @return Cert si és màquina, fals altrament
     */
    public boolean esMaquina() {
        return esMaquina;
    }

    //----------MODIFICADORES----------

    /**
     * Canvia el nom de l'usuari pel nou nom indicat i el seu respectiu fitxer amb les seves estadístiques.
     * @param nouNom Nou nom d'usuari
     */
    public void canviarNom(String nouNom) {
        if (!eliminat) {
            String oldFileName = "../DATA/estadistiques_" + this.nomUsuari + ".ser";
            String newFileName = "../DATA/estadistiques_" + nouNom + ".ser";
            File oldFile = new File(oldFileName);
            File newFile = new File(newFileName);

            if (oldFile.exists()) oldFile.renameTo(newFile);
            this.nomUsuari = nouNom;
        }
    }

    /**
     * Canvia la contrasenya de l'usuari per la indicada.
     * @param novaContra Nova contrasenya
     */
    public void canviarContrasenya(String novaContra) {
        if (!eliminat) this.contrasenya = novaContra;
    }

    /**
     * Elimina l'usuari posant tots els atributs a null i eliminat a true.
     */
    public void eliminarUsuari() {
        this.nomUsuari = null;
        this.contrasenya = null;
        this.estadistiques = null;
        this.eliminat = true;
    }

    /**
     * Desa les estadístiques de l'usuari a un fitxer.
     */
    public void guardarEstadistiques() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("../DATA/estadistiques_" + nomUsuari + ".ser"))) {
            oos.writeObject(estadistiques);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega les estadístiques de l'usuari des d'un fitxer, o crea noves si no existeix.
     */
    public void carregarEstadistiques() {
        File fitxer = new File("../DATA/estadistiques_" + nomUsuari + ".ser");
        if (fitxer.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fitxer))) {
                estadistiques = (Estadistiques) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            estadistiques = new Estadistiques(); // nova si no existeix
        }
    }
}


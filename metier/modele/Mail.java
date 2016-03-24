package fr.insalyon.dasi.metier.modele;

import java.text.SimpleDateFormat;
import java.util.List;

public abstract class Mail {
    protected static SimpleDateFormat dt = new SimpleDateFormat("dd MMMM yyyy");
    
    public Mail() {
    }

    public Mail(Adherent expediteur, String sujet, String corps, Adherent dest) {
        this.expediteur = expediteur;
        this.sujet = sujet;
        this.corps = corps;
        this.dest = dest;
    }
    
    private Adherent expediteur;
    
    private Adherent dest;

    public Adherent getDest() {
        return dest;
    }
    
    public void setDest(Adherent dest) {
        this.dest = dest;
    }

    public Adherent getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(Adherent expediteur) {
        this.expediteur = expediteur;
    }

    private String sujet;

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }
    
    protected String corps;

    public String getCorps() {
        return corps;
    }

    public void setCorps(String corps) {
        this.corps = corps;
    }

}

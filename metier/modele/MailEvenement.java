/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.metier.modele;

import java.text.SimpleDateFormat;
import java.util.List;

public class MailEvenement extends Mail {
    
    public MailEvenement() {
    }

    public MailEvenement(Adherent expediteur, Adherent dest, Evenement event) {
        super(expediteur,
            "Nouvel Evenement Collect'IF",
            "Bonjour " + dest.getPrenom() + ",\r\n" +
                    "Comme vous l'aviez souhaité, COLLECT'IF organise une événement de " +
                    event.getActivite().getDenomination() + " le " + dt.format(event.getDate())  +".\r\n" +
                    "Vous trouverez ci-dessous les détails de cet événement." +
                    "Associativement vôtre,\r\n" +
                    "Le responsable de l'Association\r\n" +
                    "Evénement : " + event.getActivite().getDenomination() + "\r\n" +
                    "Date : " + event.getDate() + "\r\n" +
                    "Lieu : " + event.getLieu() + "\r\n" +
                    "TODO : calcul distance" + "\r\n",
            dest);
            
        if (event.getActivite().isParEquipe()) {
            for (Equipe equipe : event.getEquipes()) {
                if (equipe.getListeAdherents().contains(dest)) {
                    corps += "Vous jouerez avec :\r\n";
                    for (Adherent adh : equipe.getListeAdherents()) {
                        if (!adh.equals(dest)) {
                            corps += adh.getPrenom() + " " + adh.getNom() + "\r\n";
                        }
                    }
                }
                else {
                    corps += "Vous jouerez contre :\r\n";
                    for (Adherent adh : equipe.getListeAdherents()) {
                        corps += adh.getPrenom() + " " + adh.getNom() + "\r\n";
                    }
                }
            }
        }
    }
}

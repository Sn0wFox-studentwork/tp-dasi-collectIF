package fr.insalyon.dasi.metier.services;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import fr.insalyon.dasi.metier.modele.Adherent;
import fr.insalyon.dasi.metier.modele.Lieu;
import fr.insalyon.dasi.metier.modele.Mail;
import java.util.List;

public class ServiceTechnique {
    final static String MA_CLE = "AIzaSyDcVVJjfmxsNdbdUYeg9MjQoJJ6THPuap4";
    final static GeoApiContext MON_CONTEXTE = new GeoApiContext().setApiKey(MA_CLE);
    
    public static void sendMail(List<Adherent> destinataires, Mail mail)
    {
        for(Adherent adh: destinataires)
        {
            mail.setDest(adh);
            System.out.println("Expediteur : " + mail.getExpediteur().getMail());
            System.out.println("Pour : " + adh.getMail());
            System.out.println("Sujet : " + mail.getSujet());
            System.out.println("Corps : \n");
            System.out.println(mail.getCorps());
        }
    }
    
    public static LatLng getGeolocalisation(String adresse)
    {
        try {
            GeocodingResult[] results = GeocodingApi.geocode(MON_CONTEXTE, adresse).await();
            return results[0].geometry.location;
        } catch (Exception ex) {
        }
        return null;
    }
    
    public static double distance(String ad1, String ad2)
    // Contrat :    Les latitudes et longitudes de l1 et l2 ne doivent pas être nulles
    {
        LatLng coords1 = getGeolocalisation(ad1);
        LatLng coords2 = getGeolocalisation(ad2);
        return Math.sqrt(Math.pow(coords1.lat - coords2.lat, 2) + Math.pow(coords1.lat - coords2.lng, 2));
    }
    
}

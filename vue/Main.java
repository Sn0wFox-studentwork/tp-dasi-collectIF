package fr.insalyon.dasi.vue;

import fr.insalyon.dasi.dao.ActiviteDao;
import fr.insalyon.dasi.dao.AdherentDao;
import fr.insalyon.dasi.dao.EvenementDao;
import fr.insalyon.dasi.dao.JpaUtil;
import fr.insalyon.dasi.dao.LieuDao;
import fr.insalyon.dasi.metier.modele.Activite;
import fr.insalyon.dasi.metier.modele.Adherent;
import fr.insalyon.dasi.metier.modele.Equipe;
import fr.insalyon.dasi.metier.modele.Evenement;
import fr.insalyon.dasi.metier.modele.Lieu;
import fr.insalyon.dasi.metier.services.ServiceMetier;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
	
    public static void printAllTables()
    {
        JpaUtil.creerEntityManager();
        AdherentDao add = new AdherentDao();
        ActiviteDao aco = new ActiviteDao();
        LieuDao lio = new LieuDao();
        EvenementDao evo = new EvenementDao();

        try {
                List<Adherent> lad = add.findAll();
                System.out.println("Liste des adhérents (" + lad.size() + ")");
                for (Adherent ad : lad) {
                    System.out.println(ad);
                }
                List<Activite> lac = aco.findAll();
                System.out.println("Liste des activités (" + lac.size() + ")");
                for (Activite ac : lac) {
                    System.out.println(ac);
                }
                List<Lieu> lli = lio.findAll();
                System.out.println("Liste des lieux (" + lli.size() + ")");
                for (Lieu li : lli) {
                    System.out.println(li);
                }
                List<Evenement> lev = evo.findAll();
                System.out.println("Liste des événements (" + lev.size() + ")");
                for (Evenement ev : lev) {
                    System.out.println(ev);
                }
            } catch (Throwable ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

        JpaUtil.fermerEntityManager();
    }

    public static void testCascadeAll()
    {
        JpaUtil.creerEntityManager();
        EvenementDao evo = new EvenementDao();

        JpaUtil.ouvrirTransaction();
        try {
            Activite act = new Activite("Foot", true, 4);
            Lieu l = new Lieu("Stade INSA", "Desc", "20 Av");
            Adherent adh1 = new Adherent("A", null, null, null);
            Adherent adh2 = new Adherent("B", null, null, null);
            List<Adherent> adh1s = new ArrayList<Adherent>(2);
            adh1s.add(adh1);
            adh1s.add(adh2);
            Adherent adh3 = new Adherent("C", null, null, null);
            Adherent adh4 = new Adherent("D", null, null, null);
            List<Adherent> adh2s = new ArrayList<Adherent>(2);
            adh2s.add(adh3);
            adh2s.add(adh4);
            List<Equipe> eq = new ArrayList<Equipe>(2);
            Equipe eq1 = new Equipe(adh1s);
            Equipe eq2 = new Equipe(adh2s);
            eq.add(eq1);
            eq.add(eq2);
            Evenement ev = new Evenement(act, l);
            ev.setEquipes(eq);
            evo.create(ev);

            // Results
            List<Evenement> res = evo.findByAdherent(adh4);
            System.out.println("ATTENTION : " + res.size());
        } catch (Throwable ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        JpaUtil.validerTransaction();

        JpaUtil.fermerEntityManager();
    }

    public static void testCreateOrFillEvent()
    {
        // Evénement demandé par l'utilisateur
        Activite act = new Activite("Chill", false, 4);
        Lieu l = new Lieu("Pelouse Humanites", "Desc", "20 Av");
        Evenement ev = new Evenement(act, l);

        // Adhérents demandant cet événement
        Adherent adh1 = new Adherent("A", null, null, null);
        Adherent adh2 = new Adherent("B", null, null, null);

        // Simulation de remplissage formulaire par les deux adhérents
        ServiceMetier.createOrFillEvent(ev, adh1);
        ServiceMetier.createOrFillEvent(ev, adh2);

        // Visualisation de la table des événements
        JpaUtil.creerEntityManager();
        EvenementDao evo = new EvenementDao();
        try {
            List<Evenement> events = evo.findByActivite(act);
            for(Evenement event: events)
            {
                    System.out.println(event);
            }
        } catch (Throwable ex) {
                System.out.print("Cannot print list of all events found by activity.");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        JpaUtil.fermerEntityManager();
    }
        
    public static void testCascadeAdh() {
        Activite act = new Activite("Chill", false, 4);
        Evenement ev = new Evenement(act, null);

        // Adhérents demandant cet événement
        Adherent adh1 = new Adherent("A", null, null, null);
        //Adherent adh2 = new Adherent("B", null, null, null);

        // Simulation de remplissage formulaire par les deux adhérents
        ServiceMetier.createOrFillEvent(ev, adh1);
        //ServiceMetier.createOrFillEvent(ev, adh2);

        // Visualisation de la table des événements
        JpaUtil.creerEntityManager();
        EvenementDao evo = new EvenementDao();
        AdherentDao ado = new AdherentDao();
        JpaUtil.ouvrirTransaction();
        try {
            ado.delete(ado.findAll().get(0));
            JpaUtil.validerTransaction();
        } catch (Throwable ex) {
                System.out.print("Cannot print list of all events found by activity.");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        JpaUtil.fermerEntityManager();
    }
    
    public static void testLieuxDispos()
    {
        JpaUtil.creerEntityManager();
        EvenementDao evo = new EvenementDao();
        LieuDao lao = new LieuDao();

        JpaUtil.ouvrirTransaction();
        try {
            SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
            
            Activite act = new Activite("Foot", true, 4);
            Lieu l1 = new Lieu("Stade INSA", "Desc", "20 Av");
            l1.setTypeLieu(Lieu.TypeLieu.GYMNASE);
            Lieu l2 = new Lieu("Stade CPE", "Desc", "20 Av");
            l2.setTypeLieu(Lieu.TypeLieu.GYMNASE);
            
            Evenement ev1 = new Evenement(act, l1);
            Evenement ev2 = new Evenement(act, l2);
            ev1.setDate(dt.parse("21-03-2016"));
            ev2.setDate(dt.parse("21-03-2016"));
            
            Adherent adh1 = new Adherent("A", null, null, null);
            Adherent adh2 = new Adherent("B", null, null, null);
            List<Adherent> adh1s = new ArrayList<Adherent>(2);
            adh1s.add(adh1);
            adh1s.add(adh2);
            Adherent adh3 = new Adherent("C", null, null, null);
            Adherent adh4 = new Adherent("D", null, null, null);
            List<Adherent> adh2s = new ArrayList<Adherent>(2);
            adh2s.add(adh3);
            adh2s.add(adh4);
            List<Equipe> eq = new ArrayList<Equipe>(2);
            Equipe eq1 = new Equipe(adh1s);
            Equipe eq2 = new Equipe(adh2s);
            eq.add(eq1);
            eq.add(eq2);
            ev1.setEquipes(eq);
            ev2.setEquipes(eq);
            evo.create(ev1);
            evo.create(ev2);

            // Results
            List<Lieu> res = lao.findLieuxDispoByType(dt.parse("22-03-2016"), Lieu.TypeLieu.GYMNASE);
            System.out.println(res);
        } catch (Throwable ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        JpaUtil.validerTransaction();

        JpaUtil.fermerEntityManager();
    }
    
    public static void main(String[] args)
    {
    	//printAllTables();
    	//testCascadeAll();
    	//testCreateOrFillEvent();
        //testCascadeAdh();
        //testLieuxDispos();
    	
    	KeyboardView.menu();
    }
}

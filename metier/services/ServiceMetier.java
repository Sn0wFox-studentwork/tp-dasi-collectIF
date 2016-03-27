package fr.insalyon.dasi.metier.services;

import fr.insalyon.dasi.dao.AdherentDao;
import fr.insalyon.dasi.dao.EvenementDao;
import fr.insalyon.dasi.dao.JpaUtil;
import fr.insalyon.dasi.dao.LieuDao;
import fr.insalyon.dasi.metier.modele.Adherent;
import fr.insalyon.dasi.metier.modele.Evenement;
import fr.insalyon.dasi.metier.modele.Lieu;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceMetier {
    
    // Liés à l'OM : EVENEMENT
    public static void createOrFillEvent(Evenement event, Adherent adh) {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        EvenementDao eventDao = new EvenementDao();
        try {
            List<Evenement> le = eventDao.findEventsByState(Evenement.EventState.UNFILLED);
            boolean toCreate = true;
            for(Evenement ev: le)
            {
                if(ev.getActivite().equals(event.getActivite()))
                {
                    toCreate = false;
                    ev.ajouterParticipant(adh);
                    ev = eventDao.update(ev);
                    break;
                }
            }
            if(toCreate)
            {
                event.ajouterParticipant(adh);
                eventDao.create(event);
            }
            
            JpaUtil.validerTransaction();
        } catch (Throwable ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JpaUtil.fermerEntityManager();
        }
    }
    
    public static boolean isEventComplete(Evenement event) {
        return event.getEtat() == Evenement.EventState.COMPLETE;
    }
    
    public static boolean isEventTerminated(Evenement event) {
        return event.getEtat() == Evenement.EventState.TERMINATED;
    }
    
    public static void ajouterParticipant(Evenement event, Adherent adh) {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        
        EvenementDao eventDao = new EvenementDao();
        try {
            event.ajouterParticipant(adh);
            if(event.getNbParticipant() == event.getActivite().getNbParticipants()) {
                event.setEtat(Evenement.EventState.FILLED);
            }
            eventDao.update(event);
            JpaUtil.validerTransaction();
        } catch (Throwable ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JpaUtil.fermerEntityManager();
        }
    }
    
    public static List<Evenement> getUnfilledEvents() {
        JpaUtil.creerEntityManager();
        
        List<Evenement> li = new ArrayList<Evenement>();
        EvenementDao eventDao = new EvenementDao();
        try {
            li = eventDao.findEventsByState(Evenement.EventState.UNFILLED);
        } catch (Throwable ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JpaUtil.fermerEntityManager();
        }
        return li;
    }
    
    public static List<Evenement> getEventsToFill() {
        JpaUtil.creerEntityManager();
        
        List<Evenement> li = new ArrayList<Evenement>();
        EvenementDao eventDao = new EvenementDao();
        try {
            li = eventDao.findEventsByState(Evenement.EventState.FILLED);
        } catch (Throwable ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JpaUtil.fermerEntityManager();
        }
        return li;
    }
    
    public static void ajouterLieu(Evenement event, Lieu lieu) {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        
        EvenementDao eventDao = new EvenementDao();
        event.setLieu(lieu);
        try {
            if(event.getEtat() == Evenement.EventState.FILLED) {
                event.setEtat(Evenement.EventState.COMPLETE);
            }
            eventDao.update(event);
            JpaUtil.validerTransaction();
        } catch (Throwable ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JpaUtil.fermerEntityManager();
        }
    }
    
    // Liés à l'OM : Lieu
    public static List<Lieu> getLieuxDispo(Date date, Lieu.TypeLieu type) {
        JpaUtil.creerEntityManager();
        
        List<Lieu> ll = new ArrayList<Lieu>();
        LieuDao lieuDao = new LieuDao();
        try {
             ll = lieuDao.findLieuxDispoByType(date, type);
        } catch (Throwable ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JpaUtil.fermerEntityManager();
        }
        return ll;
    }
    
    // Liés à l'OM : ADHERENT
    public static Adherent authentificate(Adherent adh)
    // Renvoie null si l'authentification n'a pas marché
    {
    	AdherentDao adhDao = new AdherentDao();
    	List<Adherent> adherent = null;
    	try {
    		// Cette liste ne devrait contenir qu'un seul élément
			adherent = adhDao.findByMail(adh.getMail());
		} catch (Throwable ex) {
			Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
		}
    	for(Adherent a: adherent)
    	{
    		if(a.getMdp().equals(adh.getMdp()))
    		{
    			return a;
    		}
    	}
    	return null;
    }
    
    public static boolean ajouterAdherent(Adherent adh)
    {
        boolean res = true;
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        
        AdherentDao adhDao = new AdherentDao();
        try {
            adhDao.create(adh);
            JpaUtil.validerTransaction();
        } catch (Throwable ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
            res = false;
        } finally {
            JpaUtil.fermerEntityManager();
            return res;
        }
    }
    
    public static void supprimerAdherent(Adherent adh)
    {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        
        AdherentDao adhDao = new AdherentDao();
        try {
            adhDao.delete(adh);
            JpaUtil.validerTransaction();
        } catch (Throwable ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JpaUtil.fermerEntityManager();
        }
    }
    
    public static void changerMdp(Adherent adh, String mdp)
    {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        
        AdherentDao adhDao = new AdherentDao();
        try {
            adh.setMdp(mdp);
            adhDao.update(adh);
            JpaUtil.validerTransaction();
        } catch (Throwable ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JpaUtil.fermerEntityManager();
        }
    }
    
    public static List<Evenement> getHistoriqueDemandes(Adherent adh)
    {
        JpaUtil.creerEntityManager();
        
        List<Evenement> li = new ArrayList<Evenement>();
        EvenementDao eventDao = new EvenementDao();
        try {
             li = eventDao.findByAdherent(adh);
        } catch (Throwable ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JpaUtil.fermerEntityManager();
        }
        return li;
    }
    
}

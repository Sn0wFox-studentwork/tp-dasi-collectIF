package fr.insalyon.dasi.metier.services;

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
import fr.insalyon.dasi.metier.modele.MailEvenement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceMetier {
    
	// Liés à l'OM : ACTIVITE
	public static List<Activite> getActivitesStartingBy(String beginning) {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        
        List<Activite> activites = null;
        ActiviteDao actDao = new ActiviteDao();
        try {
        	activites = actDao.findStartingBy(beginning);
		} catch (Throwable ex) {
			Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JpaUtil.fermerEntityManager();
        }
        
        return activites;
    }
	//TODO : A TESTER
	
	public static Activite getActiviteById(int id) {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        
        Activite activite = null;
        ActiviteDao actDao = new ActiviteDao();
        try {
        	activite = actDao.findById(id);
		} catch (Throwable ex) {
			Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JpaUtil.fermerEntityManager();
        }
        
        return activite;
    }
	//TODO : A TESTER
	
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
                if(ev.getActivite().equals(event.getActivite())
                		&& ev.getDate().equals(event.getDate()))
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
    
    @Deprecated
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
    
    @Deprecated
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
    
    public static List<Evenement> getEventsToComplete() {
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
    
    public static Evenement getEvenementById(int id) {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        
        Evenement event = null;
        EvenementDao eventDao = new EvenementDao();
        try {
        	event = eventDao.findById(id);
		} catch (Throwable ex) {
			Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JpaUtil.fermerEntityManager();
        }
        
        return event;
    }
	//TODO : A TESTER
    
    public static void ajouterLieu(Evenement event, Lieu lieu) {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        
        EvenementDao eventDao = new EvenementDao();
        event.setLieu(lieu);
        try {
            if(event.getEtat() == Evenement.EventState.FILLED) {
                event.setEtat(Evenement.EventState.COMPLETE);
                eventDao.update(event);
                JpaUtil.validerTransaction();
                Adherent exp = new Adherent();
                exp.setMail("collectif@collectif.org");
                MailEvenement mail = new MailEvenement();
                mail.setExpediteur(exp);
                mail.setEvenement(event);
                List<Equipe> equipes = event.getEquipes();
                ArrayList<Adherent> dest = new ArrayList<Adherent>();
                for(Equipe eq: equipes)
                {
                	dest.addAll(eq.getListeAdherents());
                }
                
                ServiceTechnique.sendMail(dest, mail);
            }
            else
            {
            	JpaUtil.annulerTransaction();
            	Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE,
            			"The event " + event + " was not in a stable state.");
            }
            
        } catch (Throwable ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JpaUtil.fermerEntityManager();
        }
    }
    
    // Liés à l'OM : Lieu
    public static List<Lieu> getLieuxDispo(Date date) {
        JpaUtil.creerEntityManager();
        
        List<Lieu> ll = new ArrayList<Lieu>();
        LieuDao lieuDao = new LieuDao();
        try {
             ll = lieuDao.findLieuxDispoByDate(date);
        } catch (Throwable ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JpaUtil.fermerEntityManager();
        }
        return ll;
    }
    
    public static Lieu getLieuById(int id) {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        
        Lieu lieu = null;
        LieuDao lieuDao = new LieuDao();
        try {
        	lieu = lieuDao.findById(id);
		} catch (Throwable ex) {
			Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            JpaUtil.fermerEntityManager();
        }
        
        return lieu;
    }
	//TODO : A TESTER
    
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
        	List<Adherent> la = adhDao.findByMail(adh.getMail());
        	if(la != null)
        	{
        		if(!la.isEmpty())
        		{
        			JpaUtil.annulerTransaction();
        			return false;
        		}
        	}
            adhDao.create(adh);
            JpaUtil.validerTransaction();
        } catch (Throwable ex) {
            Logger.getLogger(ServiceMetier.class.getName()).log(Level.SEVERE, null, ex);
            res = false;
        } finally {
            JpaUtil.fermerEntityManager();
        }
        return res;
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

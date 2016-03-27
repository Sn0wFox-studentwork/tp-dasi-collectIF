package fr.insalyon.dasi.dao;

import fr.insalyon.dasi.metier.modele.Activite;
import fr.insalyon.dasi.metier.modele.Adherent;
import fr.insalyon.dasi.metier.modele.Evenement;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class EvenementDao {
    
     public void create(Evenement event) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            em.persist(event);
        }
        catch(Exception e) {
            throw e;
        }
    }
     
     public Evenement update(Evenement event) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            event = em.merge(event);
        }
        catch(Exception e){
            throw e;
        }
        return event;
     }
     
     public Evenement findById(int id) throws Throwable {
         EntityManager em = JpaUtil.obtenirEntityManager();
         Evenement event = null;
         try {
             event = em.find(Evenement.class, id);
         }
         catch(Exception e) {
             throw e;
         }
         return event;
     }
     
	 public List<Evenement> findAll() throws Throwable {
		EntityManager em = JpaUtil.obtenirEntityManager();
		List<Evenement> events = null;
		try {
		    Query q = em.createQuery("SELECT e FROM Evenement e");
		    events = (List<Evenement>) q.getResultList();
		}
		catch(Exception e) {
		    throw e;
		}
		return events;
	 }
     
     public List<Evenement> findEventsByState(Evenement.EventState state) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Evenement> events = null;
        try {
            Query q = em.createQuery("SELECT e FROM Evenement e WHERE e.etat=:state");
            events = (List<Evenement>) q.setParameter("state", state).getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        return events;
     }
     
     public List<Evenement> findByAdherent(Adherent adh) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Evenement> events = null;
        try {
            Query q = em.createQuery("SELECT e FROM Evenement e WHERE :id IN (SELECT a FROM e.equipes.listeAdherents.id a)");
            events = (List<Evenement>) q.setParameter("id", adh.getId()).getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        return events;
     }
     
     public List<Evenement> findByActivite(Activite act) throws Throwable {
    	 EntityManager em = JpaUtil.obtenirEntityManager();
         List<Evenement> events = null;
         try {
             Query q = em.createQuery("SELECT e FROM Evenement e WHERE e.activite =:activite");
             events = (List<Evenement>) q.setParameter("activite", act).getResultList();
         }
         catch(Exception e) {
             throw e;
         }
         return events;
      }
}

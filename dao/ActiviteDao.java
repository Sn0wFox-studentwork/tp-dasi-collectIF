package fr.insalyon.dasi.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import fr.insalyon.dasi.metier.modele.Activite;

public class ActiviteDao {
    
    public void create(Activite activite) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            em.persist(activite);
        }
        catch(Exception e) {
            throw e;
        }
    }
    
    public Activite update(Activite activite) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            activite = em.merge(activite);
        }
        catch(Exception e){
            throw e;
        }
        return activite;
    }
    
    public Activite findById(int id) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Activite activite = null;
        try {
            activite = em.find(Activite.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        return activite;
    }
    
    public List<Activite> findAll() throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Activite> activites = null;
        try {
            Query q = em.createQuery("SELECT a FROM Activite a");
            activites = (List<Activite>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return activites;
    }
    
    // TODO: a tester
    public List<Activite> findStartingBy(String beginning) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Activite> activites = null;
        try {
            Query q = em.createQuery("SELECT a FROM Activite a WHERE a.denomination LIKE :beginning%");
            activites = (List<Activite>) q.setParameter("beginning", beginning).getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return activites;
    }
}

package fr.insalyon.dasi.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import fr.insalyon.dasi.metier.modele.Lieu;
import java.util.Date;
import javax.persistence.TemporalType;

public class LieuDao {
    
    public void create(Lieu lieu) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            em.persist(lieu);
        }
        catch(Exception e) {
            throw e;
        }
    }
    
    public Lieu update(Lieu lieu) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            lieu = em.merge(lieu);
        }
        catch(Exception e){
            throw e;
        }
        return lieu;
    }
    
    public Lieu findById(int id) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        Lieu lieu = null;
        try {
            lieu = em.find(Lieu.class, id);
        }
        catch(Exception e) {
            throw e;
        }
        return lieu;
    }
    
    public List<Lieu> findAll() throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Lieu> lieux = null;
        try {
            Query q = em.createQuery("SELECT l FROM Lieu l");
            lieux = (List<Lieu>) q.getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return lieux;
    }
    
    public Long getCount() throws Throwable {
        Long nbLieux;
        EntityManager em = JpaUtil.obtenirEntityManager();
        try {
            Query q = em.createQuery("SELECT COUNT(l) FROM Lieu l");
            nbLieux = (Long) q.getSingleResult();
        }
        catch(Exception e) {
            throw e;
        }
        return nbLieux;
    }

    public List<Lieu> findLieuxDispoByType(Date date, Lieu.TypeLieu type) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Lieu> lieux;
        try {
            Query q = em.createQuery("SELECT e.lieu FROM Evenement e WHERE e.date != :dte AND e.lieu.typeLieu = :tlieu");
            lieux = (List<Lieu>) q.setParameter("tlieu", type).setParameter("dte", date).getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        
        return lieux;
    }
}

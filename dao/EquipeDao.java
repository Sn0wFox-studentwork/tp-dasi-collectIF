package fr.insalyon.dasi.dao;

import fr.insalyon.dasi.metier.modele.Activite;
import fr.insalyon.dasi.metier.modele.Adherent;
import fr.insalyon.dasi.metier.modele.Equipe;
import fr.insalyon.dasi.metier.modele.Evenement;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class EquipeDao {
     
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
     
     public List<Equipe> findByAdherent(Adherent adh) throws Throwable {
        EntityManager em = JpaUtil.obtenirEntityManager();
        List<Equipe> equipes = null;
        try {
            Query q = em.createQuery("SELECT e FROM Equipe e WHERE :id IN (SELECT a FROM e.listeAdherents.id a)");
            equipes = (List<Equipe>) q.setParameter("id", adh.getId()).getResultList();
        }
        catch(Exception e) {
            throw e;
        }
        return equipes;
     }
}

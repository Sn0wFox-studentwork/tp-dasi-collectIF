package fr.insalyon.dasi.metier.modele;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Evenement {
    
    // TODO : Migrer vers une simple string ?
    public enum EventState
    {
        UNFILLED, FILLED, COMPLETE, TERMINATED 
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @ManyToOne(cascade = CascadeType.ALL)
    private Activite activite;
    
    @ManyToOne(cascade = CascadeType.ALL)
    private Lieu lieu;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Equipe> equipes;
    
    @Temporal(TemporalType.DATE)
    private Date date;
    
    @Enumerated(EnumType.STRING)
    private EventState etat;
    
    // Constructeurs
    public Evenement() {
        this.etat = EventState.UNFILLED;
    }
    
    public Evenement(Activite a, Lieu l) {
        this();
        this.activite = a;
        this.lieu = l;
    }
    
    // Methodes publiques    
    public boolean ajouterParticipant(Adherent adh) {
    	int nbPartMax = activite.getNbParticipants();
        if(etat == EventState.UNFILLED) {
        	if(activite.isParEquipe()) {
                if(equipes == null) {
                    equipes = new ArrayList<Equipe>();
                    equipes.add(new Equipe());
                    equipes.get(0).getListeAdherents().add(adh);
                } else {
                    boolean newEquipe = true;
                    for(Equipe e : equipes)
                    {
                        if(e.getListeAdherents().size() < nbPartMax/(equipes.size()+1))
                        {
                            e.getListeAdherents().add(adh);
                            newEquipe = false;
                            break;
                        }
                    }
                    if(newEquipe) {
                        equipes.add(new Equipe());
                        equipes.get(equipes.size()-1).getListeAdherents().add(adh);
                    }
                }
            } else {
                if(equipes == null) {
                    equipes = new ArrayList<Equipe>();
                    equipes.add(new Equipe());
                }
                equipes.get(0).getListeAdherents().add(adh);
            }
        	int nbPart = 0;
            for(Equipe eq: equipes)
            {
            	nbPart += eq.getListeAdherents().size();
            }
            if(nbPartMax == nbPart)
            {
            	etat = EventState.FILLED;
            }
            return true;
        }
        return false;
    }
    
    public int getNbParticipant() {
        if(equipes == null) {
            return 0;
        }
        int somme = 0;
        for(Equipe e: equipes) {
            somme += e.getListeAdherents().size();
        }
        return somme;
    }
    
    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setActivite(Activite activite) {
        this.activite = activite;
    }

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }

    public void setEquipes(List<Equipe> equipes) {
        this.equipes = equipes;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }

    public void setEtat(EventState etat) {
        this.etat = etat;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public Activite getActivite() {
        return activite;
    }

    public Lieu getLieu() {
        return lieu;
    }

    public List<Equipe> getEquipes() {
        return equipes;
    }
    
    public Date getDate() {
        return date;
    }
    
    public EventState getEtat() {
        return etat;
    }
    
    @Override
    public String toString() {
        return "Evenement{" + "id=" + id + ", Activite=" + activite + ", Lieu=" + lieu + ", Date=" + date + ", Etat=" + etat + '}';
    }
}

package fr.insalyon.dasi.metier.modele;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.eclipse.persistence.annotations.CascadeOnDelete;

@Entity
public class Equipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @CascadeOnDelete
    private List<Adherent> listeAdherents;

    public List<Adherent> getListeAdherents() {
        return listeAdherents;
    }

    public Equipe() {
        listeAdherents = new ArrayList<Adherent>();
    }
    
    public Equipe(List<Adherent> listeAdherents) {
        this.listeAdherents = listeAdherents;
    }
    
}

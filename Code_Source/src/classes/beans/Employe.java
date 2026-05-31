
package classes.beans;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
/**
 *
 * @author er.rahmouni
 */
@Entity
@Table(name="employe")
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_emp")
    private int id;
    @Column(name="matricule")
    private String matricule;
    @Column(name="nom")
    private String nom;
    @Column(name="prenom")
    private String prenom;
    @Column(name="poste")
    private String poste;
    @Column(name="salaire")
    private double salaire;
    @Column(name="statut")
    private String statut;
    @ManyToOne
    @JoinColumn(name="id_dept", nullable=false)
    private Departement departement;
    
    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
    
   

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    
    
    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }
    
    

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }
    
   
    public String getStatut() {
        return statut;
    }

    
    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Employe() {
    }

    public Employe(String matricule, String nom, String prenom, String poste, double salaire, String statut, Departement departement) {
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.poste = poste;
        this.salaire = salaire;
        this.statut = statut;
        this.departement = departement;
    }
    
    @Override
    public String toString() {
        return nom + " " + prenom;
    }
    
    
}


package classes.beans;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
/**
 *
 * @author er.rahmouni
 */
@Entity
@Table(name="absence")
public class Absence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_abs")
    private int id;
    @Column(name="date_debut")
    
    private java.sql.Date  dateDebut;
    @Column(name="date_fin")
    
    private java.sql.Date dateFin;
    @Column(name="motif")
    private String motif;
    @ManyToOne
    @JoinColumn(name="id_emp" , nullable=false)
    private Employe employe;
    @Column(name="nb_jours")
    private Integer nbJours;
    public int getId() {
        return id;
    }

    
    public void setId(int id) {
        this.id = id;
    }
    
    

    
    public java.sql.Date getDateDebut() {
        return dateDebut;
    }

    
    public void setDateDebut(java.sql.Date dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    

   
    public java.sql.Date getDateFin() {
        return dateFin;
    }

   
    public void setDateFin(java.sql.Date dateFin) {
        this.dateFin = dateFin;
    }
    
    

    public String getMotif() {
        return motif;
    }

    
    public void setMotif(String motif) {
        this.motif = motif;
    }
    
   
    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public int getNbJours() {
        return nbJours;
    }

    public void setNbJours(int nbJours) {
        this.nbJours = nbJours;
    }
    
    public Absence() {
    }

    public Absence(java.sql.Date dateDebut, java.sql.Date dateFin, String motif, Employe employe) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.employe = employe;
        
    }

    @Override
    public String toString() {
        return "Absence{" + "motif=" + motif + ", employe=" + employe + '}';
    }
    


}

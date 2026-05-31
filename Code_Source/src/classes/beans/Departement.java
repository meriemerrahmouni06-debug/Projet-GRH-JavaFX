
package classes.beans;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
/**
 *
 * @author er.rahmouni
 */
@Entity
@Table(name="departement")
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_dept")
    private int id;
    @Column(name="nom_dept")
    private String nomDept;
    @Column(name="bureau")
    private String bureau;
    @Column(name="mission")
    private String mission;

    /**
     * Get the value of bureau
     *
     * @return the value of bureau
     */
    public String getBureau() {
        return bureau;
    }

    /**
     * Set the value of bureau
     *
     * @param bureau new value of bureau
     */
    public void setBureau(String bureau) {
        this.bureau = bureau;
    }


    /**
     * Get the value of nomDept
     *
     * @return the value of nomDept
     */
    public String getNomDept() {
        return nomDept;
    }

    /**
     * Set the value of nomDept
     *
     * @param nomDept new value of nomDept
     */
    public void setNomDept(String nomDept) {
        this.nomDept = nomDept;
    }

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(int id) {
        this.id = id;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }
    
    public Departement() {
    }

    public Departement(String nomDept, String bureau, String mission) {
        this.nomDept = nomDept;
        this.bureau = bureau;
        this.mission = mission;
    }

    @Override
    public String toString() {
        return nomDept;
    }
    
}

package grh;

import classes.beans.Departement;
import classes.beans.Employe;
import fichiershibernate.NewHibernateUtil;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class FormulaireEmployeController implements Initializable {

    @FXML private TextField txtMatricule;
    @FXML private TextField txtNomEmp;
    @FXML private TextField txtPrenomEmp;
    @FXML private TextField txtPosteEmp;
    @FXML private TextField txtSalaireEmp;
    @FXML private TextField txtStatutEmp;
    @FXML private ComboBox<Departement> cbDepartementEmp;
    @FXML private Button buttonAjouter;
    @FXML private Button buttonAnnuler;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chargerDepartements();
    }
    private void chargerDepartements() {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        try {
            List<Departement> liste =
                    session.createQuery("from Departement").list();
            cbDepartementEmp.setItems(FXCollections.observableArrayList(liste));
            System.out.println("✔ Départements chargés dans le ComboBox (" + liste.size() + ")");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
        if (session != null && session.isOpen()) {
            session.close();
        }
    } catch (Exception ignored) {}
        }
    }
    @FXML
    private void onEnregistrerNouveauEmploye(ActionEvent event) {
        if (txtMatricule.getText().isEmpty() || txtNomEmp.getText().isEmpty()
                || txtPrenomEmp.getText().isEmpty() || txtPosteEmp.getText().isEmpty()
                || txtSalaireEmp.getText().isEmpty() || txtStatutEmp.getText().isEmpty()
                || cbDepartementEmp.getValue() == null) {
            System.out.println("ERREUR : Veuillez remplir tous les champs !");
            return;
        }
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            Employe emp = new Employe();
            emp.setMatricule(txtMatricule.getText().trim());
            emp.setNom(txtNomEmp.getText().trim());
            emp.setPrenom(txtPrenomEmp.getText().trim());
            emp.setPoste(txtPosteEmp.getText().trim());
            emp.setSalaire(Double.parseDouble(txtSalaireEmp.getText().trim()));
            emp.setStatut(txtStatutEmp.getText().trim());
            emp.setDepartement(cbDepartementEmp.getValue());
           tx = session.beginTransaction();
session.save(emp);
tx.commit();
System.out.println("✔ Employé '" + emp.getNom() + "' sauvegardé !");
if (parentController != null) {
    parentController.chargerEmployes();
}
Stage stage = (Stage) buttonAjouter.getScene().getWindow();
stage.close();
        } catch (NumberFormatException e) {
            System.out.println("ERREUR : Le salaire doit être un nombre (ex: 5000.0) !");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("ERREUR Hibernate : " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
        if (session != null && session.isOpen()) {
            session.close();
        }
            }catch (Exception ignored) {}
    }    
    }

    @FXML
    private void onFermerFormulaire(ActionEvent event) {
        Stage stage = (Stage) buttonAnnuler.getScene().getWindow();
        stage.close();
    }
private FXMLDocumentController parentController;
public void setParentController(FXMLDocumentController parent) {
    this.parentController = parent;
}
}

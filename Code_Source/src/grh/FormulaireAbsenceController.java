package grh;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import fichiershibernate.NewHibernateUtil;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
public class FormulaireAbsenceController {

    @FXML private TextField txtIdEmployeAbs;
    @FXML private DatePicker pickerDateDebut;
    @FXML private DatePicker pickerDateFin;
    @FXML private TextArea txtMotifAbsence;
    @FXML private Button btnAnnulerAbsence;
    private FXMLDocumentController parentController;
    public void setParentController(FXMLDocumentController parent) {
        this.parentController = parent;
    }
    @FXML
    private void enregistrerNouvelleAbsence(ActionEvent event) {

        if (txtIdEmployeAbs.getText().isEmpty()
                || pickerDateDebut.getValue() == null
                || pickerDateFin.getValue() == null
                || txtMotifAbsence.getText().isEmpty()) {
            System.out.println("ERREUR : Veuillez remplir tous les champs !");
            return;
        }
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
           
            String matricule = txtIdEmployeAbs.getText().trim();
            org.hibernate.Query q = session.createQuery(
                "from Employe where matricule = :mat");
            q.setParameter("mat", matricule);
            java.util.List liste = q.list();

            if (liste.isEmpty()) {
                System.out.println("ERREUR : Aucun employé trouvé avec la matricule '" + matricule + "'");
                return;
            }
            classes.beans.Employe employe = (classes.beans.Employe) liste.get(0);

            java.sql.Date dateDebut = java.sql.Date.valueOf(pickerDateDebut.getValue());
            java.sql.Date dateFin   = java.sql.Date.valueOf(pickerDateFin.getValue());
            String motif            = txtMotifAbsence.getText().trim();

            long nbJours = pickerDateDebut.getValue()
                    .until(pickerDateFin.getValue(), java.time.temporal.ChronoUnit.DAYS) + 1;

            classes.beans.Absence abs = new classes.beans.Absence();
            abs.setEmploye(employe);
            abs.setDateDebut(dateDebut);
            abs.setDateFin(dateFin);
            abs.setMotif(motif);
            abs.setNbJours((int) nbJours);

            tx = session.beginTransaction();
            session.save(abs);
            tx.commit();
            
            System.out.println("✔ Absence sauvegardée en base de données !");
            if (parentController != null) {
                parentController.chargerAbsences();
            }
            viderChamps();
            Stage stage = (Stage) btnAnnulerAbsence.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("ERREUR Hibernate : " + e.getMessage());
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
    private void fermerFormulaire(ActionEvent event) {
        Stage stage = (Stage) btnAnnulerAbsence.getScene().getWindow();
        stage.close();
    }
    private void viderChamps() {
        txtIdEmployeAbs.clear();
        pickerDateDebut.setValue(null);
        pickerDateFin.setValue(null);
        txtMotifAbsence.clear();
    }
}
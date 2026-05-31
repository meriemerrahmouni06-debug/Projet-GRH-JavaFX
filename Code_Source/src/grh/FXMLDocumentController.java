package grh;

import classes.beans.Employe;
import classes.beans.Absence;
import classes.beans.Departement;
import fichiershibernate.NewHibernateUtil;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class FXMLDocumentController implements Initializable {
    //  TABLE ABSENCE
    @FXML private TableView<Absence> tableAbsence;
    @FXML private TextField txtChercherAbsence;
    @FXML private Button btnAjouterAbsence;
    @FXML private Button btnSupprimerAbsence;
    @FXML private TableColumn<Absence, String> colIdEmploye;
    @FXML private TableColumn<Absence, LocalDate> colDateDebut;
    @FXML private TableColumn<Absence, LocalDate> colDateFin;
    @FXML private TableColumn<Absence, String> colMotif;
    @FXML private TableColumn<Absence, Integer> colNbJours;

    //  TABLE DEPARTEMENT
    @FXML private TableView<Departement> tableDepartement;
    @FXML private TableColumn<Departement, Integer> colIdDepartement;
    @FXML private TableColumn<Departement, String> colNomDepartement;
    @FXML private TableColumn<Departement, String> colBureau;
    @FXML private TableColumn<Departement, String> colMission;
    @FXML private TextField txtBureau;
    @FXML private TextField txtMission;
    @FXML private TextField txtChercherDepartement;
    @FXML private TextField txtIdDepartement;
    @FXML private TextField txtNomDepartement;

    //  TABLE EMPLOYE
    @FXML private TableView<Employe> tableEmploye;
    @FXML private TableColumn<Employe, String> colMatricule;
    @FXML private TableColumn<Employe, String> colNomEmploye;
    @FXML private TableColumn<Employe, String> colPrenomEmploye;
    @FXML private TableColumn<Employe, String> colPosteEmploye;
    @FXML private TableColumn<Employe, Double> colSalaireEmploye;
    @FXML private TableColumn<Employe, String> colStatutEmploye;
    @FXML private TableColumn<Employe, String> colDepEmploye;
    @FXML private TextField txtChercherEmploye;

    //   lier les colonnes + charger les données
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // -- Colonnes Département
        if (colIdDepartement != null)
            colIdDepartement.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colNomDepartement != null)
            colNomDepartement.setCellValueFactory(new PropertyValueFactory<>("nomDept"));
        if (colBureau != null)
            colBureau.setCellValueFactory(new PropertyValueFactory<>("bureau"));
        if (colMission != null)
            colMission.setCellValueFactory(new PropertyValueFactory<>("mission"));
        
        // -- Colonnes Employé
        if (colMatricule != null)
            colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        if (colNomEmploye != null)
            colNomEmploye.setCellValueFactory(new PropertyValueFactory<>("nom"));
        if (colPrenomEmploye != null)
            colPrenomEmploye.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        if (colPosteEmploye != null)
            colPosteEmploye.setCellValueFactory(new PropertyValueFactory<>("poste"));
        if (colSalaireEmploye != null)
            colSalaireEmploye.setCellValueFactory(new PropertyValueFactory<>("salaire"));
        if (colStatutEmploye != null)
            colStatutEmploye.setCellValueFactory(new PropertyValueFactory<>("statut"));
        
        colDepEmploye.setCellValueFactory(cellData -> {
    Employe emp = cellData.getValue();
    String nomDept = (emp.getDepartement() != null) ? emp.getDepartement().getNomDept() : "";
    return new javafx.beans.property.SimpleStringProperty(nomDept);
});
        // Colonnes Absence
if (colDateDebut != null)
    colDateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
if (colDateFin != null)
    colDateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
if (colMotif != null)
    colMotif.setCellValueFactory(new PropertyValueFactory<>("motif"));
if (colNbJours != null)
    colNbJours.setCellValueFactory(new PropertyValueFactory<>("nbJours"));

if (colIdEmploye != null)
    colIdEmploye.setCellValueFactory(cellData -> {
        Absence abs = cellData.getValue();
        String mat = (abs.getEmploye() != null) ? abs.getEmploye().getMatricule() : "";
        return new javafx.beans.property.SimpleStringProperty(mat);
    });
if (tableAbsence != null)
    tableAbsence.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        chargerDepartements();
    if (txtChercherDepartement != null) {
    txtChercherDepartement.textProperty().addListener((obs, oldVal, newVal) -> {
        chercherDepartement(newVal.trim().toLowerCase());
    });
}
        chargerEmployes();
if (txtChercherEmploye != null) {
    txtChercherEmploye.textProperty().addListener((obs, oldVal, newVal) -> {
        String recherche = newVal.trim().toLowerCase();
        if (recherche.isEmpty()) {
            chargerEmployes();
            return;
        }
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        try {
            org.hibernate.Query q = session.createQuery(
                "from Employe where lower(nom) like :mot " +
                "or lower(prenom) like :mot " +
                "or lower(matricule) like :mot");
            q.setParameter("mot", "%" + recherche + "%");
            List<Employe> liste = q.list();
            tableEmploye.setItems(FXCollections.observableArrayList(liste));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
        if (session != null && session.isOpen()) {
            session.close();
        }
    } catch (Exception ignored) {}
        }
    });
}
       chargerAbsences();
if (txtChercherAbsence != null) {
    txtChercherAbsence.textProperty().addListener((obs, oldVal, newVal) -> {
        String recherche = newVal.trim().toLowerCase();
        if (recherche.isEmpty()) {
            chargerAbsences();
            return;
        }
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        try {
            org.hibernate.Query q = session.createQuery(
 "SELECT DISTINCT a FROM Absence a JOIN FETCH a.employe e JOIN FETCH e.departement " +
    "WHERE lower(e.matricule) like :mot");
            q.setParameter("mot", "%" + recherche + "%");
            List<Absence> liste = q.list();
            tableAbsence.setItems(FXCollections.observableArrayList(liste));
            System.out.println("Recherche matricule '" + recherche + "' → " + liste.size() + " résultat(s)");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
             try {
        if (session != null && session.isOpen()) {
            session.close();
        }
    } catch (Exception ignored) {}
        }
    });
}
    }
    public void chargerDepartements() {
    Session session = NewHibernateUtil.getSessionFactory().openSession();
    try {
        session.clear();
        List<Departement> liste = session.createQuery("from Departement").list();
        tableDepartement.setItems(FXCollections.observableArrayList(liste));
        System.out.println("✔ Départements chargés : " + liste.size());
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
public void chargerEmployes() {
    NewHibernateUtil.reset();
    Session session = NewHibernateUtil.getSessionFactory().openSession();
    try {
        session.clear();
        List<Employe> liste = session.createQuery(
            "SELECT DISTINCT e FROM Employe e JOIN FETCH e.departement").list();
        final ObservableList<Employe> data = FXCollections.observableArrayList(liste);
        javafx.application.Platform.runLater(() -> {
            tableEmploye.setItems(data);
            tableEmploye.refresh();
        });
        System.out.println("✔ Employés chargés : " + liste.size());
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
public void chargerAbsences() {
    NewHibernateUtil.reset();
    Session session = NewHibernateUtil.getSessionFactory().openSession();
    try {
        session.clear();
        List<Absence> liste = session.createQuery(
            "SELECT DISTINCT a FROM Absence a " +
            "JOIN FETCH a.employe e " +
            "LEFT JOIN FETCH e.departement").list();
        final ObservableList<Absence> data = FXCollections.observableArrayList(liste);
        javafx.application.Platform.runLater(() -> {
            tableAbsence.setItems(data);
            tableAbsence.refresh();
        });
        System.out.println("✔ Absences chargées : " + liste.size());
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
    private void ajouterDepartement(ActionEvent event) {
        String nomDep  = txtNomDepartement.getText().trim();
        String bureau  = txtBureau != null ? txtBureau.getText().trim() : "";
        String mission = txtMission != null ? txtMission.getText().trim() : "";

        if (nomDep.isEmpty()) {
            System.out.println("ERREUR : Le nom du département est obligatoire !");
            return;
        }
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            Departement dep = new Departement(nomDep, bureau, mission);
            tx = session.beginTransaction();
            session.save(dep);
            tx.commit();
            System.out.println("✔ Département '" + nomDep + "' sauvegardé en BDD !");
            chargerDepartements();
            txtNomDepartement.clear();
            if (txtBureau != null) txtBureau.clear();
            if (txtMission != null) txtMission.clear();

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
private void supprimerDepartement(ActionEvent event) {
    Departement depSelectionne = tableDepartement.getSelectionModel().getSelectedItem();
    if (depSelectionne == null) {
        System.out.println("ERREUR : Veuillez sélectionner un département !");
        return;
    }
    Session session = NewHibernateUtil.getSessionFactory().openSession();
    Transaction tx = null;
    try {
        tx = session.beginTransaction();
        session.createQuery(
            "DELETE FROM Absence WHERE employe.id IN " +
            "(SELECT e.id FROM Employe e WHERE e.departement.id = :idDept)")
               .setParameter("idDept", depSelectionne.getId())
               .executeUpdate();
        session.createQuery(
            "DELETE FROM Employe WHERE departement.id = :idDept")
               .setParameter("idDept", depSelectionne.getId())
               .executeUpdate();
        session.createQuery(
            "DELETE FROM Departement WHERE id = :id")
               .setParameter("id", depSelectionne.getId())
               .executeUpdate();
        tx.commit();
        System.out.println("✔ Département et ses employés supprimés !");
        chargerDepartements();
        chargerEmployes();
        chargerAbsences();
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
    private void annulerSaisieDepartement(ActionEvent event) {
        txtNomDepartement.clear();
        if (txtBureau != null) txtBureau.clear();
        if (txtMission != null) txtMission.clear();
        System.out.println("Formulaire département réinitialisé.");
    }
    @FXML
    private void ouvrirFormulaireAjouterEmp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FormulaireEmploye.fxml"));
            Parent root = loader.load();
            FormulaireEmployeController ctrl = loader.getController();
            ctrl.setParentController(this);
            Stage stage = new Stage();
            stage.setTitle("Ajouter un nouvel employé");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            chargerEmployes();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERREUR ouverture FormulaireEmploye.fxml : " + e.getMessage());
        }
    }
    @FXML
    private void supprimerEmploye(ActionEvent event) {
        Employe empSelectionne = tableEmploye.getSelectionModel().getSelectedItem();
        if (empSelectionne == null) {
            System.out.println("ERREUR : Veuillez sélectionner un employé !");
            return;
        }
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
       try{
tx = session.beginTransaction();
session.createQuery("DELETE FROM Absence WHERE employe.id = :id")
       .setParameter("id", empSelectionne.getId())
       .executeUpdate();
session.createQuery("DELETE FROM Employe WHERE id = :id")
       .setParameter("id", empSelectionne.getId())
       .executeUpdate();
tx.commit();
System.out.println("✔ Employé supprimé !");
chargerEmployes();
chargerAbsences();}
catch (Exception e) {
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
    private void chercherEmploye(ActionEvent event) {
        String recherche = txtChercherEmploye.getText().trim().toLowerCase();
        if (recherche.isEmpty()) { chargerEmployes(); return; }
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        try {
            org.hibernate.Query q = session.createQuery(
                    "from Employe where lower(nom) like :mot or lower(prenom) like :mot or lower(matricule) like :mot");
                q.setParameter("mot", "%" + recherche + "%");
                List<Employe> liste = q.list();
            tableEmploye.setItems(FXCollections.observableArrayList(liste));
            System.out.println("Recherche '" + recherche + "' → " + liste.size() + " résultat(s)");
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
private void ouvrirFormulaireAjouterAbs(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FormulaireAbsence.fxml"));
        Parent root = loader.load();
        FormulaireAbsenceController ctrl = loader.getController();
        ctrl.setParentController(this);
        Stage stage = new Stage();
        stage.setTitle("Ajouter une Absence");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
        chargerAbsences();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
@FXML
    private void ouvrirFormulaireSupprimerAbs(ActionEvent event) {
        Absence absSelectionne = tableAbsence.getSelectionModel().getSelectedItem();
        if (absSelectionne == null) {
            System.out.println("ERREUR : Veuillez sélectionner une absence !");
            return;
        }
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Absence absASupprimer =
                    (Absence) session.get(Absence.class, absSelectionne.getId());
            if (absASupprimer != null) {
                session.delete(absASupprimer);
            }
            tx.commit();
            System.out.println("✔ Absence supprimée de la BDD !");
            chargerAbsences();
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
    private void onEnregistrerNouveauEmploye(ActionEvent event) { }
    @FXML
    private void onFermerFormulaire(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    private void enregistrerNouvelleAbsence(ActionEvent event) {
    }
    @FXML
    private void fermerFormulaire(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
private void chercherDepartement(String recherche) {
    if (recherche.isEmpty()) {
        chargerDepartements();
        return;
    }
    Session session = NewHibernateUtil.getSessionFactory().openSession();
    try {
        org.hibernate.Query q = session.createQuery(
            "from Departement where lower(nomDept) like :mot " +
            "or lower(bureau) like :mot " +
            "or lower(mission) like :mot");
        q.setParameter("mot", "%" + recherche + "%");
        List<Departement> liste = q.list();
        tableDepartement.setItems(FXCollections.observableArrayList(liste));
        System.out.println("Recherche '" + recherche + "' → " + liste.size() + " résultat(s)");
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
}

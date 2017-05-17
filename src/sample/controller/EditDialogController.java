package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.objects.Person;
import sample.utils.DialogManager;

import java.net.URL;
import java.util.ResourceBundle;

public class EditDialogController implements Initializable {
    @FXML
    private Button btnOK;

    @FXML
    private Button btnCancel;

    @FXML
    private TextField txtFIO;

    @FXML
    private TextField txtPhone;

    private Person person;

    private ResourceBundle resourceBundle;

    public Person getPerson() {
        return person;
    }

    public void actionClose(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.hide();
    }

    public void actionSave(ActionEvent actionEvent) {
        if (!checkValues()){
            return;
        }
        person.setPhone(txtPhone.getText());
        person.setFio(txtFIO.getText());
        actionClose(actionEvent);
    }

    private boolean checkValues(){
        if (txtFIO.getText().trim().length()==0
    ||
                txtPhone.getText().trim().length()==0){
            DialogManager.showInfoDialog(resourceBundle.getString("error"), resourceBundle.getString("fill_field"));
            return false;
        }
        return true;
    }


    public void setPerson(Person person){
        this.person = person;

        txtFIO.setText(person.getFio());
        txtPhone.setText(person.getPhone());

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resourceBundle;

    }
}

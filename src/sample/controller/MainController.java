package sample.controller;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import sample.db.DBConnections;
import sample.interfaces.impls.CollectionAddressBook;
import sample.objects.Person;
import sample.utils.DialogManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements Initializable{

    private CollectionAddressBook addressBookImpl = new CollectionAddressBook();

    private Stage mainStage;

    @FXML
    private Button addButton;


    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;


    @FXML
    private CustomTextField textSearch;

    @FXML
    private Button searchButton;

    @FXML
    private TableView tableAddressBook;

    @FXML
    private Label labelCount;

    @FXML
    private TextField txt_name;

    @FXML
    private TableColumn<Person, String> columnFIO;

    @FXML
    private TableColumn<Person, String> columnPhone;

    private Connection connection = null;

    private PreparedStatement preparedStatement = null;

    private Parent fxmlEdit;
    private FXMLLoader fxmlLoader = new FXMLLoader();
    private EditDialogController editDialogController;
    private Stage editDialogStage;
    private ResourceBundle resourceBundle;
    private ObservableList<Person> backupList;
    private ObservableList<Person> personList =  FXCollections.observableArrayList();

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    private void fillDate() {
        addressBookImpl.fillTestData();

        backupList = FXCollections.observableArrayList();
        backupList.addAll(addressBookImpl.getPersonList());
        tableAddressBook.setItems(addressBookImpl.getPersonList());
    }

    private void  initListeners(){
        addressBookImpl.getPersonList().addListener(new ListChangeListener<Person>() {
            @Override
            public void onChanged(Change<? extends Person> c) {
                updateCountLabel();
            }
        });

        tableAddressBook.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2){
                    editDialogController.setPerson((Person) tableAddressBook.getSelectionModel().getSelectedItem());
                    showDialog();
                }
            }
        });
    }

    private void initLoader(){
        try {
            fxmlLoader.setLocation(getClass().getResource("../fxml/edit.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("sample.bundles.Locale",new Locale("en")));
            fxmlEdit = fxmlLoader.load();
            editDialogController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCountLabel(){
        labelCount.setText(resourceBundle.getString("count") + ": "+ addressBookImpl.getPersonList().size());
    }


    private void showDialog(){
        if (editDialogStage == null){
            editDialogStage = new Stage();
            editDialogStage.setTitle(resourceBundle.getString("edit_record"));
            editDialogStage.setMinHeight(150);
            editDialogStage.setMinWidth(300);
            editDialogStage.setResizable(false);
            editDialogStage.setScene(new Scene(fxmlEdit));
            editDialogStage.initModality(Modality.WINDOW_MODAL);
            editDialogStage.initOwner(mainStage);
        }

        editDialogStage.showAndWait();

//        editDialogStage.show();

    }

    public void actionButtonPressed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (!(source instanceof Button)){
            return;
        }

        Button clickedButton = (Button) source;

        Person selectedPerson = (Person) tableAddressBook.getSelectionModel().getSelectedItem();

//        Window parentWindow = ((Node) actionEvent.getSource()).getScene().getWindow();

//        editDialogController.setPerson(selectedPerson);

        switch (clickedButton.getId()){
            case "addButton":
                editDialogController.setPerson(new Person());
                showDialog();
                addressBookImpl.add(editDialogController.getPerson());
                break;
            case "editButton":
//                editDialogController.setPerson((Person)tableAddressBook.getSelectionModel().getSelectedItem());
//                if (!personIsSelected(selectedPerson)){
//                        return;
//                }
                editDialogController.setPerson(selectedPerson);
                showDialog();
                break;
            case "deleteButton":
//                addressBookImpl.delete((Person) tableAddressBook.getSelectionModel().getSelectedItem());
//                if (!personIsSelected(selectedPerson)){
//                    return;
//                }
                addressBookImpl.delete(selectedPerson);
                break;
        }
    }

    private boolean personIsSelected(Person selectedPerson){
        if (selectedPerson==null){
            DialogManager.showInfoDialog(resourceBundle.getString("error"), resourceBundle.getString("select_person"));
        }
        return false;

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;

        //        выделение нескльких записей
//        tableAddressBook.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        columnFIO.setCellValueFactory(new PropertyValueFactory<Person, String>("fio"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<Person, String>("phone"));

        initListeners();

        fillDate();

        initLoader();

        connection = DBConnections.connection();



        try {
            setupClearButtonField(textSearch);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupClearButtonField(CustomTextField customTextField)  throws Exception {
        Method m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class, ObjectProperty.class);
        m.setAccessible(true);
        m.invoke(null, customTextField, customTextField.rightProperty());
    }

    @FXML
    private void actionSearch(ActionEvent actionEvent){
        addressBookImpl.getPersonList().clear();

        for (Person person : backupList){

            if (person.getFio().toLowerCase().contains(textSearch.getText().toLowerCase())
                    ||
                    person.getPhone().toLowerCase().contains(textSearch.getText().toLowerCase())){

                addressBookImpl.getPersonList().add(person);

            }
        }
    }


    public void handleSelect(ActionEvent actionEvent) {

    }
//    @FXML
//    public void handleSelect(ActionEvent actionEvent) {
//
////        int version = Integer.parseInt(txt_name.getText());
//        Statement statement = null;
//        PreparedStatement preparedStatement = null;
//
//        try {
//
////            preparedStatement = connection.prepareStatement("SELECT * FROM VERSION WHERE VERSION > ?");
////            preparedStatement.setInt(1, Integer.parseInt((String.valueOf(columnFIO))));
////            ResultSet resultSet1 = preparedStatement.executeQuery();
////
////            while (resultSet1.next()){
////                System.out.println("number # "+ resultSet1.getRow()+
////                        "\n Number in database " + resultSet1.getInt("version"));
////            }
//
//
//            statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT NAME_USER FROM GASTELLO.DOK WHERE NAME_USER LIKE 'NAS' ");
//
//            while (resultSet.next()){
//                String name = resultSet.getString("NAME_USER");
//                System.out.println("name: " + name);
//            }
//
//            System.out.println("data select successful!");
//
//        } catch (SQLException e) {
//            Logger.getLogger(DBConnections.class.getName()).log(Level.SEVERE, null, e);
//        }finally {
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}

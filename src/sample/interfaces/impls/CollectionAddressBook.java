package sample.interfaces.impls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.db.DBConnections;
import sample.interfaces.AddressBook;
import sample.objects.Person;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CollectionAddressBook implements AddressBook {

    private ObservableList<Person> personList =  FXCollections.observableArrayList();

    private Connection connection = null;

    @Override
    public void add(Person person) {
        personList.add(person);
    }

    @Override
    public void update(Person person) {

    }

    @Override
    public void delete(Person person) {
        personList.remove(person);
    }

    public ObservableList<Person> getPersonList() {
        return personList;
    }

    public void print(){
        int number = 0;
        System.out.println();
        for (Person person : personList){
            number++;
            System.out.println(number+") FIO = " + person.getFio() + "; phone = " + person.getPhone());

        }

    }

    public void fillTestData(){
//        personList.add(new Person("uvan", "124124"));
//        personList.add(new Person("uv25an", "124121324"));
//        personList.add(new Person("u54van", "12419824"));
//        personList.add(new Person("uva3n", "124313234"));
//        personList.add(new Person("uva32n", "12497124"));
//        personList.add(new Person("uv124an", "1234124"));
//        personList.add(new Person("uva3125n", "120409124"));

        //        int version = Integer.parseInt(txt_name.getText());
        Statement statement = null;
        PreparedStatement preparedStatement = null;

        connection = DBConnections.connection();
        try {

//            preparedStatement = connection.prepareStatement("SELECT * FROM VERSION WHERE VERSION > ?");
//            preparedStatement.setInt(1, Integer.parseInt((String.valueOf(columnFIO))));
//            ResultSet resultSet1 = preparedStatement.executeQuery();
//
//            while (resultSet1.next()){
//                System.out.println("number # "+ resultSet1.getRow()+
//                        "\n Number in database " + resultSet1.getInt("version"));
//            }


            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT NAME_USER FROM GASTELLO.DOK WHERE NAME_USER LIKE 'NAS' ");

            while (resultSet.next()){
                String name = resultSet.getString("NAME_USER");
                personList.addAll(new Person(name, "1234"));
//                System.out.println("name: " + name);
            }


            System.out.println("data select successful!");

        } catch (SQLException e) {
            Logger.getLogger(DBConnections.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}

package sample.interfaces.impls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.interfaces.AddressBook;
import sample.objects.Person;

public class CollectionAddressBook implements AddressBook {

    private ObservableList<Person> personList =  FXCollections.observableArrayList();

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
        personList.add(new Person("uvan", "124124"));
        personList.add(new Person("uv25an", "124121324"));
        personList.add(new Person("u54van", "12419824"));
        personList.add(new Person("uva3n", "124313234"));
        personList.add(new Person("uva32n", "12497124"));
        personList.add(new Person("uv124an", "1234124"));
        personList.add(new Person("uva3125n", "120409124"));

    }
}

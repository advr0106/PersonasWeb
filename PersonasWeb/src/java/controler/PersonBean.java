/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Person;
import model.PersonDetail;
import org.primefaces.model.StreamedContent;
import persist.PersonDAOHibernate;
import util.ExportReport;

/**
 *
 * @author alex
 */
@ManagedBean
@SessionScoped
public class PersonBean {

    private Person person;
    private PersonDAOHibernate personDAO;
    private ArrayList<Person> persons;
    private ExportReport exportReport;

    public String init() {
        exportReport = ExportReport.getInstance();
        personDAO = PersonDAOHibernate.getInstance();
        this.newPerson();
        this.refillList();
        return null;
    }
    public String saveContact(){
        if(person.getId()==null){
            personDAO.saveContact(person);
        }else{
            personDAO.updateContact(person);
        }
        return "/faces/webapp/viewContacts.xhtml?faces-redirect=true";
    }
    public void deleteContacts(String userId){
        personDAO.deleteContact(personDAO.findById(userId));
        this.refillList();
    }
    public void newPerson(){
        person=new Person();
    }
    private void refillList(){
        persons=(ArrayList<Person>) personDAO.findAll();
    }
    private void findPerson(String userId){
        this.setPerson(personDAO.findById(userId));
    }
    public StreamedContent getStreamContextPdf(){
        String nameFile="report2";
        HashMap hash =new HashMap();
        return exportReport.exportPdf((List<Object>)(Object)persons,nameFile,hash);
    }
    public StreamedContent getStreamContextExcel(){
        String nameFile="report2";
        HashMap hash =new HashMap();
        return exportReport.exportExcel((List<Object>)(Object)persons,nameFile,hash);
    }
    public List<PersonDetail> getPersonDetails(){
        return Collections.singletonList(person.getPersonDetail());
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public PersonDAOHibernate getPersonDAO() {
        return personDAO;
    }

    public void setPersonDAO(PersonDAOHibernate personDAO) {
        this.personDAO = personDAO;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public ExportReport getExportReport() {
        return exportReport;
    }

    public void setExportReport(ExportReport exportReport) {
        this.exportReport = exportReport;
    }
    
}

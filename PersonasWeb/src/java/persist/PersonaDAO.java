/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persist;

import model.Person;

/**
 *
 * @author alexv
 */
public interface PersonaDAO extends GenericDAO<Person, String>{
    public Person findByUuid(String uuid);
}

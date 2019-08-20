/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persist;

import java.io.Serializable;
import java.util.List;
/**
 *
 * @author alexv
 * @param <T>
 * @param <ID>
 * 
 */
public interface GenericDAO<T, ID extends Serializable> {
   T findById(ID id);
   List<T> findAll();
   T makePersistent(T entity);
}

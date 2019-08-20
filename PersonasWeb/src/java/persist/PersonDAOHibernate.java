/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persist;

import java.util.List;
import model.Person;
import model.Status;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import persistence.HibernateUtil;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author alexv
 */
public class PersonDAOHibernate implements PersonaDAO {

    private Session session;
    private Transaction tx;
    private static PersonDAOHibernate instance;
    private static final int STATUS_ENABLED = 1;
    private static final int STATUS_DISABLED = 2;

    public static PersonDAOHibernate getInstance() {
        if (instance == null) {
            instance = new PersonDAOHibernate();
        }
        return instance;
    }

    public void saveContact(Person contact) throws HibernateException {
        contact.setStatus(new Status(STATUS_DISABLED));
        makePersistent(contact);
    }

    public void updateContact(Person contact) throws HibernateException {
        try {
            session.update(contact);
            tx.commit();
        } catch (HibernateException he) {
            handleException(he);
            throw he;
        }
    }

    public void deleteContact(Person contact) throws HibernateException {
        try {
            contact.setStatus(new Status(STATUS_DISABLED));
            session.saveOrUpdate(contact);
            tx.commit();
        } catch (HibernateException he) {
            handleException(he);
            throw he;
        }
    }

    private void startOp() throws HibernateException {
        session = HibernateUtil.getSessionFactory().openSession();
        tx = session.beginTransaction();
    }

    private void handleException(HibernateException he) throws HibernateException {
        tx.rollback();
        throw new HibernateException("Ocurrio un error en la capa de acceso de datos", he);
    }

    @Override
    public Person findByUuid(String uuid) {
        startOp();
        Criteria criteria = session.createCriteria(Person.class);
        criteria.add(Restrictions.eq("id", uuid));
        return (Person) criteria.uniqueResult();
    }

    @Override
    public Person findById(String id) {
        startOp();
        Criteria criteria=session.createCriteria(Person.class);
        criteria.add(Restrictions.eq("id",id));
        
        return (Person) criteria.uniqueResult();
    }

    @Override
    public List<Person> findAll() {
        try {

            startOp();
            Criteria criteria = session.createCriteria(Person.class,"p");
            criteria.createAlias("status", "s");
            criteria.setReadOnly(true);
            criteria.add(Restrictions.eq("s.id", STATUS_ENABLED));
            return criteria.list();
        } catch (HibernateException he) {
            handleException(he);
            throw he;
        } finally {
            session.close();
        }
    }

    @Override
    public Person makePersistent(Person entity) {
        try {
            startOp();
            session.saveOrUpdate(entity);
            tx.commit();
            return entity;
        } catch (HibernateException he) {
            handleException(he);
            throw he;
        }finally{
            session.close();
        }
    }
}

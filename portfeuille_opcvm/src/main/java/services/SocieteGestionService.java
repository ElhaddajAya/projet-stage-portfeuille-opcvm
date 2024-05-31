package services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import entities.Client;
import entities.Portefeuille;
import entities.SocieteGestion;
import utils.HibernateUtil;

@ManagedBean
public class SocieteGestionService {

	private SessionFactory sessionFactory;

	@PostConstruct
	public void init() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

	/************ CRUD methods ************/
    public List<SocieteGestion> getAllSocieteGestion() {
    	try (Session session = sessionFactory.openSession()) {
            Query<SocieteGestion> query = session.createQuery("from SocieteGestion", SocieteGestion.class);
            return query.list();
        }
    }
    
    public void addSocieteGestion(SocieteGestion sg) {
    	try (Session session = sessionFactory.openSession()) {
    		session.beginTransaction();
    		session.save(sg);
    		session.getTransaction().commit();
    	}
    }
    
    public void deleteSociete(long id) {
		SocieteGestion societe= null;
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			societe = session.get(SocieteGestion.class, id);
			session.delete(societe);
			session.getTransaction().commit();
		}
	}
    
	public void updateSocieteGestion(SocieteGestion sg) {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			session.update(sg);
			session.getTransaction().commit();
		}
	}

	public SocieteGestion getSocieteGestionById(long sgId) {
		try (Session session = sessionFactory.openSession()) {
			SocieteGestion sg = session.get(SocieteGestion.class, sgId);	        
			return session.get(SocieteGestion.class, sgId);
		}
	}
	
    public List<Portefeuille> getPortefeuillesBySocieteGestion(long sgId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Portefeuille> query = session.createQuery("from Portefeuille where societeGestion.id = :sgId", Portefeuille.class);
            query.setParameter("sgId", sgId);
            return query.list();
        }
    }

}

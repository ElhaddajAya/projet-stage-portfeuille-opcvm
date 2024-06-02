package services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import entities.Court;
import entities.Portefeuille;
import utils.HibernateUtil;

@ManagedBean
public class CoursService {

	private SessionFactory sessionFactory;

	@PostConstruct
	public void init() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	/********* CRUD *********/
	public List<Portefeuille> getAllPortefeuilles() {
		try (Session session = sessionFactory.openSession()) {
	        Query<Portefeuille> query = session.createQuery("from Portefeuille", Portefeuille.class);
			return query.list();
		}
	}
	
	public void addCours(Court cours) {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			session.save(cours);
			session.getTransaction().commit();
		}
	}

	public List<Court> getAllCours() {
		try (Session session = sessionFactory.openSession()) {
			String hql = "SELECT c1 FROM Court c1 WHERE c1.date = (" +
                    	"SELECT MAX(c2.date) FROM Court c2 WHERE c2.portefeuille.id = c1.portefeuille.id)";
	       Query<Court> query = session.createQuery(hql, Court.class);
	       return query.list();
		}
	}
	
	public void deleteCours(Long id) {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			Court c = session.find(Court.class, id);
			
			session.delete(c);
			session.getTransaction().commit();
		}
	}
	
}

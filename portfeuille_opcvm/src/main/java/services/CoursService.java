package services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import entities.Cours;
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
	
	public void addCours(Cours cours) {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			session.save(cours);
			session.getTransaction().commit();
		}
	}

	public List<Cours> getAllCours() {
		try (Session session = sessionFactory.openSession()) {
			String hql = "SELECT c1 FROM Cours c1 WHERE c1.date = (" +
                    	"SELECT MAX(c2.date) FROM Cours c2 WHERE c2.portefeuille.id = c1.portefeuille.id)";
	       Query<Cours> query = session.createQuery(hql, Cours.class);
	       return query.list();
		}
	}
	
	public void deleteCours(Long id) {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			Cours c = session.find(Cours.class, id);
			
			session.delete(c);
			session.getTransaction().commit();
		}
	}
	
}

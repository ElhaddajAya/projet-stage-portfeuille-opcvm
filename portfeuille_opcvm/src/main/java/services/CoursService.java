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
			Query<Court> query = session.createQuery("from Court", Court.class);
			return query.list();
		}
	}
	
}

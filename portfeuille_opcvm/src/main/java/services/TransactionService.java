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
import entities.Transaction;
import utils.HibernateUtil;

@ManagedBean
public class TransactionService {

	private SessionFactory sessionFactory;

	@PostConstruct
	public void init() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	/*** CRUD methods *****/
	public List<Client> getAllClients() {
		try (Session session = sessionFactory.openSession()) {
			Query<Client> query = session.createQuery("from Client", Client.class);
			return query.list();
		}
	}

	public List<Transaction> getAllTransactions() {
		try (Session session = sessionFactory.openSession()) {
			Query<Transaction> query = session.createQuery("from Transaction", Transaction.class);
			return query.list();
		}
	}

	public void addTransaction(Transaction tr) {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			session.save(tr);
			session.getTransaction().commit();
		}
	}

	public String getPortefeuilleLibelleById(Long portefeuilleId) {
		try (Session session = sessionFactory.openSession()) {
			Portefeuille p = session.get(Portefeuille.class, portefeuilleId);

			return p.getLibelle();
		}
	}

	public List<Transaction> getAllTransactionsForPortefeuille(Long id) {
		try (Session session = sessionFactory.openSession()) {
			Query<Transaction> query = session.createQuery("from Transaction where portefeuille.id = :portefeuilleId",
					Transaction.class);
			query.setParameter("portefeuilleId", id);
			return query.list();
		}
	}

	public int getTotalPartsForPortefeuille(Long id) {
		try (Session session = sessionFactory.openSession()) {
			Query<Long> query = session.createQuery(
					"select sum(nbrPart) from Transaction where portefeuille.id = :portefeuilleId", Long.class);
			query.setParameter("portefeuilleId", id);
			Long totalParts = query.uniqueResult();
			return totalParts != null ? totalParts.intValue() : 0;
		}
	}

	public void updateTransaction(Transaction t) {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			session.update(t);
			session.getTransaction().commit();
		}
	}

	public List<Portefeuille> getAllPortefeuilles() {
		try (Session session = sessionFactory.openSession()) {
			Query<Portefeuille> query = session.createQuery("from Portefeuille", Portefeuille.class);
			return query.list();
		}
	}

	public void deleteTransaction(Transaction tr) {
		try (Session session = sessionFactory.openSession()) {
	        session.beginTransaction();
	        
	        /* mettre à jour le nbr de parts total dans le portefeuille concerné */
//	        Portefeuille p = tr.getPortefeuille();
//	        p.setNbrPart(p.getNbrPart() - tr.getNbrPart());
//	        session.update(p);
	        
	        session.delete(tr);
	        session.getTransaction().commit();
    	}
	}

	public Client getClientById(Long clientId) {
		try (Session session = sessionFactory.openSession()) {
			// Initialize transactions collection
			Client c = session.get(Client.class, clientId);
	        Hibernate.initialize(c.getTransactions());
	        
			return session.get(Client.class, clientId);
		}
	}

}
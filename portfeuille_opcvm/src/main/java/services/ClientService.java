package services;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import entities.Client;
import entities.Court;
import entities.Portefeuille;
import entities.Transaction;
import utils.HibernateUtil;

@ManagedBean
public class ClientService {

	private SessionFactory sessionFactory;

	@PostConstruct
	public void init() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	/*** CRUD methods *****/
	public void addClient(Client clt) {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			session.save(clt);
			session.getTransaction().commit();
		}
	}

	public void deleteClient(long id) {
		Client client = null;
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			client = session.get(Client.class, id);

			// Dissociate transactions from portefeuille
			Query<Transaction> query = session.createQuery("FROM Transaction WHERE client = :client",
					Transaction.class);
			query.setParameter("client", client);	
			List<Transaction> transactions = query.list();
	        for (Transaction transaction : transactions) {
	            // Mettre à jour le nombre de parts du portefeuille
	            Portefeuille portefeuille = transaction.getPortefeuille();
	            portefeuille.setNbrPart(portefeuille.getNbrPart() - transaction.getNbrPart());
	            session.update(portefeuille); // Mettre à jour le portefeuille dans la base de données

	            // Supprimer la transaction
	            session.delete(transaction);
	        }

			session.delete(client);
			session.getTransaction().commit();
		}
	}

	public void updateClient(Client client) {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			session.update(client);
			session.getTransaction().commit();
		}
	}

	public Client getClientById(long clientId) {
		try (Session session = sessionFactory.openSession()) {
			// Initialize transactions collection
			Client c = session.get(Client.class, clientId);
	        Hibernate.initialize(c.getTransactions());
	        
			return session.get(Client.class, clientId);
		}
	}

	public List<Client> getAllClients() {
		try (Session session = sessionFactory.openSession()) {
			Query<Client> query = session.createQuery("from Client", Client.class);
			return query.list();
		}
	}

	public List<Transaction> getDistinctTransactionsForClient(Long clientId) {
		try (Session session = sessionFactory.openSession()) {
			Query<Transaction> query = session.createQuery("from Transaction where client.id = :clientId", Transaction.class);
			query.setParameter("clientId", clientId);
			return query.list();
		}
	}	
}
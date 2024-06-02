package services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import entities.Client;
import entities.Cours;
import entities.Portefeuille;
import entities.SocieteGestion;
import entities.Transaction;
import utils.HibernateUtil;

@ManagedBean
public class PortefeuilleService {

	private SessionFactory sessionFactory;

	@PostConstruct
	public void init() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	/*** CRUD methods *****/
	public void addPortefeuille(Portefeuille ptf) {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			session.save(ptf);
			session.getTransaction().commit();
		}
	}

	public List<Portefeuille> getAllPortefeuilles() {
		try (Session session = sessionFactory.openSession()) {
			Query<Portefeuille> query = session.createQuery("from Portefeuille", Portefeuille.class);
			return query.list();
		}
	}

	public List<Client> getAllClients() {
		try (Session session = sessionFactory.openSession()) {
			Query<Client> query = session.createQuery("from Client", Client.class);
			return query.list();
		}
	}

	public List<SocieteGestion> getAllSocieteGestion() {
		try (Session session = sessionFactory.openSession()) {
			Query<SocieteGestion> query = session.createQuery("from SocieteGestion", SocieteGestion.class);
			return query.list();
		}
	}

    public List<Cours> getCourtsByPortefeuille(Portefeuille portefeuille) {
        try (Session session = sessionFactory.openSession()) {
            Query<Cours> query = session.createQuery("from Cours where portefeuille.id = :portefeuilleId", Cours.class);
            query.setParameter("portefeuilleId", portefeuille.getId());
            return query.list();
        }
    }

    public List<Cours> getAllCourts() {
        try (Session session = sessionFactory.openSession()) {
            Query<Cours> query = session.createQuery("from Cours", Cours.class);
            return query.list();
        }
    }

	public Portefeuille getPortefeuilleById(Long portefeuilleId) {
	    try (Session session = sessionFactory.openSession()) {
	        Portefeuille portefeuille = session.get(Portefeuille.class, portefeuilleId);
	        // Initialize transactions collection
	        Hibernate.initialize(portefeuille.getTransactions());
	        // Initialize cours collection
	        Hibernate.initialize(portefeuille.getCours());
	        return portefeuille;
	    }
	}

	public Portefeuille getPortefeuilleByLibelle(String libelle) {
		try (Session session = sessionFactory.openSession()) {
			Query<Portefeuille> query = session
					.createQuery("from Portefeuille where lower(libelle) = :libelle", Portefeuille.class)
					.setParameter("libelle", libelle);

			return query.getSingleResult();
		}
	}

	public void updatePortefeuille(Portefeuille portefeuille) {
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			session.update(portefeuille);
			session.getTransaction().commit();
		}
	}
	
	public void deletePortefeuille(Portefeuille ptf) {
	    try (Session session = sessionFactory.openSession()) {
	        session.beginTransaction();

	        // Charger le portefeuille géré par la session
	        Portefeuille managedPortefeuille = session.get(Portefeuille.class, ptf.getId());

	        // Vérifier que le portefeuille existe et est géré par la session
	        if (managedPortefeuille != null) {
	            // Dissocier et supprimer les transactions associées
	            for (Transaction transaction : managedPortefeuille.getTransactions()) {
	                transaction.setPortefeuille(null);  // Dissocier le portefeuille
	                session.delete(transaction);  // Supprimer la transaction
	            }

	            // Supprimer le portefeuille
	            session.delete(managedPortefeuille);
	        }

	        session.getTransaction().commit();
	    }
	}

	public List<Transaction> getAllTransactionsForPortefeuille(Long ptfId) {
		try (Session session = sessionFactory.openSession()) {
			Query<Transaction> query = session.createQuery("SELECT t FROM Transaction t WHERE t.portefeuille.id = :portefeuilleId", Transaction.class);
			query.setParameter("portefeuilleId", ptfId);
			return query.list();
		}
	}
    
	public List<Client> getDistinctClientsForPortefeuille(Long portefeuilleId) {
	    try (Session session = sessionFactory.openSession()) {
	        String hql = "SELECT DISTINCT t.client FROM Transaction t WHERE t.portefeuille.id = :portefeuilleId";
	        Query<Client> query = session.createQuery(hql, Client.class);
	        query.setParameter("portefeuilleId", portefeuilleId);
	        return query.list();
	    }
	}
	
	public List<Client> getClientTransactionInfoForPortefeuille(Long portefeuilleId) {
	    try (Session session = sessionFactory.openSession()) {
	        String hql = "SELECT t.client, SUM(t.nbrPart), SUM(t.montant) " +
	                     "FROM Transaction t WHERE t.portefeuille.id = :portefeuilleId " +
	                     "GROUP BY t.client";
	        Query<Object[]> query = session.createQuery(hql, Object[].class);
	        query.setParameter("portefeuilleId", portefeuilleId);

	        List<Object[]> results = query.list();
	        List<Client> clients = new ArrayList<>();
	        for (Object[] result : results) {
	            Client client = (Client) result[0];
	            Long totalPartsLong = (Long) result[1];
	            Double totalAmountDouble = (Double) result[2];
	            
	            int totalParts = totalPartsLong != null ? totalPartsLong.intValue() : 0;
	            double totalAmount = totalAmountDouble != null ? totalAmountDouble : 0.0;

	            client.setTotalParts(totalParts);
	            client.setTotalAmount(totalAmount);
	            clients.add(client);
	        }
	        return clients;
	    }
	}
	
    public Cours getLatestCourtForPortefeuille(Long portefeuilleId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Cours> query = session.createQuery(
                "FROM Cours WHERE portefeuille.id = :portefeuilleId ORDER BY date DESC", 
                Cours.class
            );
            query.setParameter("portefeuilleId", portefeuilleId);
            query.setMaxResults(1);
            return query.uniqueResult();
        }
    }
    
    public void addCourt(Cours court) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(court);
            session.getTransaction().commit();
        }
    }
	
    public Double getLatestCoutForPortefeuille(Long portefeuilleId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Double> query = session.createQuery(
                "select c.cout from Cours c where c.portefeuille.id = :portefeuilleId order by c.date desc", Double.class);
            query.setParameter("portefeuilleId", portefeuilleId);
            query.setMaxResults(1);
            return query.uniqueResult();
        }
    }
    
}

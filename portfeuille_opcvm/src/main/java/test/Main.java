package test;

import org.hibernate.Session;
import org.hibernate.Transaction;

import utils.HibernateUtil;

public class Main {
	public static void main(String[] args) {
        // Obtain a Hibernate session from the utility class
        Session session = HibernateUtil.getSessionFactory().openSession();

        // Begin a transaction
        Transaction transaction = session.beginTransaction();

        // Create the table by executing the mapping of the entities
        session.createQuery("from Client").list(); // This operation triggers the creation of the table if it doesn't exist
        session.createQuery("from SocieteGestion").list();
        session.createQuery("from Portefeuille").list();
        session.createQuery("from Transaction").list();
        session.createQuery("from Court").list();

        // Commit the transaction
        transaction.commit();

        // Close the Hibernate session
        session.close();

        // Shutdown the Hibernate session factory
        HibernateUtil.shutdown();
    }
}

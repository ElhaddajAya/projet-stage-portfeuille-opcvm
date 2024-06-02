package utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import entities.Client;
import entities.Cours;
import entities.Portefeuille;
import entities.SocieteGestion;
import entities.Transaction;

public class HibernateUtil {
	private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure()
            		.addAnnotatedClass(Client.class)
            		.addAnnotatedClass(SocieteGestion.class)
            		.addAnnotatedClass(Portefeuille.class)
            		.addAnnotatedClass(Transaction.class)
            		.addAnnotatedClass(Cours.class)
            		.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}

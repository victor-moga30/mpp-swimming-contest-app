package ro.mpp2026.repository.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ro.mpp2026.model.Child;
import ro.mpp2026.model.Event;
import ro.mpp2026.model.Registration;
import ro.mpp2026.model.User;

public class HibernateUtils {
    private static SessionFactory sessionFactory;

    private HibernateUtils() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration()
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Child.class)
                    .addAnnotatedClass(Event.class)
                    .addAnnotatedClass(Registration.class)
                    .buildSessionFactory();
        }

        return sessionFactory;
    }

    public static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }
}
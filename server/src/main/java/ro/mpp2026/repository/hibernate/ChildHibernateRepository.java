package ro.mpp2026.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ro.mpp2026.model.Child;
import ro.mpp2026.repository.ChildRepository;

public class ChildHibernateRepository implements ChildRepository {
    private final SessionFactory sessionFactory;

    public ChildHibernateRepository() {
        sessionFactory = HibernateUtils.getSessionFactory();
    }

    @Override
    public Child findByCnp(String cnp) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "from Child c where c.cnp = :cnp",
                            Child.class)
                    .setParameter("cnp", cnp)
                    .uniqueResult();
        }
    }

    @Override
    public Child findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Child.class, id);
        }
    }

    @Override
    public void save(Child child) {
        sessionFactory.inTransaction(session -> session.persist(child));
    }
}
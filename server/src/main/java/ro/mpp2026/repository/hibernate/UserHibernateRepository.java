package ro.mpp2026.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ro.mpp2026.model.User;
import ro.mpp2026.repository.UserRepository;

public class UserHibernateRepository implements UserRepository {
    private final SessionFactory sessionFactory;

    public UserHibernateRepository() {
        sessionFactory = HibernateUtils.getSessionFactory();
    }

    @Override
    public User findByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "from ContestUser u where u.username = :username",
                            User.class)
                    .setParameter("username", username)
                    .uniqueResult();
        }
    }

    @Override
    public User findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(User.class, id);
        }
    }

    @Override
    public void save(User user) {
        sessionFactory.inTransaction(session -> session.persist(user));
    }
}
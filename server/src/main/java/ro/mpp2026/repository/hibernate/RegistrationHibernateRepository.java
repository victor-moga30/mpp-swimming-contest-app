package ro.mpp2026.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ro.mpp2026.model.Registration;
import ro.mpp2026.repository.RegistrationRepository;

import java.util.List;

public class RegistrationHibernateRepository implements RegistrationRepository {
    private final SessionFactory sessionFactory;

    public RegistrationHibernateRepository() {
        sessionFactory = HibernateUtils.getSessionFactory();
    }

    @Override
    public List<Registration> findByChildId(long childId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "from Registration r where r.childId = :childId",
                            Registration.class)
                    .setParameter("childId", childId)
                    .getResultList();
        }
    }

    @Override
    public List<Registration> findByEventId(long eventId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "from Registration r where r.eventId = :eventId",
                            Registration.class)
                    .setParameter("eventId", eventId)
                    .getResultList();
        }
    }

    @Override
    public void save(Registration registration) {
        sessionFactory.inTransaction(session -> session.persist(registration));
    }

    @Override
    public void delete(Registration registration) {
        sessionFactory.inTransaction(session -> {
            Registration existing = session.createQuery(
                            "from Registration r where r.childId = :childId and r.eventId = :eventId",
                            Registration.class)
                    .setParameter("childId", registration.getChildId())
                    .setParameter("eventId", registration.getEventId())
                    .uniqueResult();

            if (existing != null) {
                session.remove(existing);
            }
        });
    }

    @Override
    public Registration findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Registration.class, id);
        }
    }

    @Override
    public List<Long> findEventIdsByChildId(Long childId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "select r.eventId from Registration r where r.childId = :childId",
                            Long.class)
                    .setParameter("childId", childId)
                    .getResultList();
        }
    }
}
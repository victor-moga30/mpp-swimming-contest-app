package ro.mpp2026.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ro.mpp2026.model.Event;
import ro.mpp2026.repository.EventRepository;

import java.util.List;

public class EventHibernateRepository implements EventRepository {
    private final SessionFactory sessionFactory;

    public EventHibernateRepository() {
        sessionFactory = HibernateUtils.getSessionFactory();
    }

    @Override
    public Event findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Event.class, id);
        }
    }

    @Override
    public void save(Event event) {
        sessionFactory.inTransaction(session -> session.persist(event));
    }

    @Override
    public List<Event> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Event", Event.class).getResultList();
        }
    }
}
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
    public Event update(Event event) {
        final Event[] updatedEvent = new Event[1];

        sessionFactory.inTransaction(session -> {
            Event existing = session.find(Event.class, event.getId());

            if (existing == null) {
                updatedEvent[0] = null;
                return;
            }

            existing.setName(event.getName());
            existing.setDistance(event.getDistance());
            existing.setMinAge(event.getMinAge());
            existing.setMaxAge(event.getMaxAge());

            updatedEvent[0] = session.merge(existing);
        });

        return updatedEvent[0];
    }

    @Override
    public void deleteById(Long id) {
        sessionFactory.inTransaction(session -> {
            Event existing = session.find(Event.class, id);

            if (existing != null) {
                session.remove(existing);
            }
        });
    }

    @Override
    public List<Event> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "from Event e order by e.id",
                    Event.class
            ).getResultList();
        }
    }

    @Override
    public List<Event> findByDistance(int distance) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "from Event e where e.distance = :distance order by e.id",
                            Event.class)
                    .setParameter("distance", distance)
                    .getResultList();
        }
    }
}
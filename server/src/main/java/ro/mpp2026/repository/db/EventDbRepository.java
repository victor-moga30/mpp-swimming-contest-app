package ro.mpp2026.repository.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.model.Event;
import ro.mpp2026.repository.EventRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDbRepository implements EventRepository {
    private static final Logger logger = LogManager.getLogger(EventDbRepository.class);
    private final JdbcUtils jdbcUtils;

    public EventDbRepository(JdbcUtils jdbcUtils) {
        logger.info("Initializing EventDbRepository");
        this.jdbcUtils = jdbcUtils;
    }

    @Override
    public Event findById(Long id) {
        logger.info("Entering findById with id={}", id);
        String sql = "SELECT id, name, distance, min_age, max_age FROM events WHERE id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Event event = new Event(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("distance"),
                            resultSet.getInt("min_age"),
                            resultSet.getInt("max_age")
                    );
                    logger.info("Event found with id={}: {}", id, event);
                    return event;
                }
            }
        } catch (SQLException e) {
            logger.error("Error in findById for event id={}", id, e);
            throw new RuntimeException("Error finding event by id", e);
        }

        logger.info("No event found with id={}", id);
        return null;
    }

    @Override
    public List<Event> findAll() {
        logger.info("Entering findAll");
        List<Event> events = new ArrayList<>();
        String sql = "SELECT id, name, distance, min_age, max_age FROM events";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Event event = new Event(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("distance"),
                        resultSet.getInt("min_age"),
                        resultSet.getInt("max_age")
                );
                events.add(event);
            }

            logger.info("findAll completed, {} events loaded", events.size());
        } catch (SQLException e) {
            logger.error("Error in findAll", e);
            throw new RuntimeException("Error finding all events", e);
        }

        return events;
    }

    @Override
    public void save(Event event) {
        throw new UnsupportedOperationException("Save not supported for events");
    }
}
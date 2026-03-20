package ro.mpp2026.repository.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.model.Registration;
import ro.mpp2026.repository.RegistrationRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDbRepository implements RegistrationRepository {
    private static final Logger logger = LogManager.getLogger(RegistrationDbRepository.class);
    private final JdbcUtils jdbcUtils;

    public RegistrationDbRepository(JdbcUtils jdbcUtils) {
        logger.info("Initializing RegistrationDbRepository");
        this.jdbcUtils = jdbcUtils;
    }

    @Override
    public List<Registration> findByChildId(long childId) {
        logger.info("Entering findByChildId with childId={}", childId);
        List<Registration> registrations = new ArrayList<>();
        String sql = "SELECT id, child_id, event_id FROM registrations WHERE child_id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, childId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Registration registration = new Registration(
                            resultSet.getLong("id"),
                            resultSet.getLong("child_id"),
                            resultSet.getLong("event_id")
                    );
                    registrations.add(registration);
                }
            }

            logger.info("findByChildId completed, {} registrations found", registrations.size());
        } catch (SQLException e) {
            logger.error("Error in findByChildId for childId={}", childId, e);
            throw new RuntimeException("Error finding registrations by child id", e);
        }

        return registrations;
    }

    @Override
    public List<Registration> findByEventId(long eventId) {
        logger.info("Entering findByEventId with eventId={}", eventId);
        List<Registration> registrations = new ArrayList<>();
        String sql = "SELECT id, child_id, event_id FROM registrations WHERE event_id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Registration registration = new Registration(
                            resultSet.getLong("id"),
                            resultSet.getLong("child_id"),
                            resultSet.getLong("event_id")
                    );
                    registrations.add(registration);
                }
            }

            logger.info("findByEventId completed, {} registrations found", registrations.size());
        } catch (SQLException e) {
            logger.error("Error in findByEventId for eventId={}", eventId, e);
            throw new RuntimeException("Error finding registrations by event id", e);
        }

        return registrations;
    }

    @Override
    public void save(Registration registration) {
        logger.info("Entering save with registration={}", registration);
        String sql = "INSERT INTO registrations(child_id, event_id) VALUES (?, ?)";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, registration.getChildId());
            statement.setLong(2, registration.getEventId());

            int rows = statement.executeUpdate();
            logger.info("Registration saved successfully, affected rows={}", rows);
        } catch (SQLException e) {
            logger.error("Error saving registration={}", registration, e);
            throw new RuntimeException("Error saving registration", e);
        }
    }

    @Override
    public void delete(Registration registration) {
        logger.info("Entering delete with registration={}", registration);
        String sql = "DELETE FROM registrations WHERE child_id = ? AND event_id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, registration.getChildId());
            statement.setLong(2, registration.getEventId());

            int rows = statement.executeUpdate();
            logger.info("Registration deleted successfully, affected rows={}", rows);
        } catch (SQLException e) {
            logger.error("Error deleting registration={}", registration, e);
            throw new RuntimeException("Error deleting registration", e);
        }
    }
}
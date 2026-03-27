package ro.mpp2026.repository.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.model.Child;
import ro.mpp2026.repository.ChildRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChildDbRepository implements ChildRepository {
    private static final Logger logger = LogManager.getLogger(ChildDbRepository.class);
    private final JdbcUtils jdbcUtils;

    public ChildDbRepository(JdbcUtils jdbcUtils) {
        logger.info("Initializing ChildDbRepository");
        this.jdbcUtils = jdbcUtils;
    }

    @Override
    public Child findByCnp(String cnp) {
        logger.info("Entering findByCnp with cnp={}", cnp);
        String sql = "SELECT id, name, cnp, age FROM children WHERE cnp = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cnp);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Child child = new Child(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("cnp"),
                            resultSet.getInt("age")
                    );
                    logger.info("Child found with cnp={}: {}", cnp, child);
                    return child;
                }
            }
        } catch (SQLException e) {
            logger.error("Error in findByCnp for cnp={}", cnp, e);
            throw new RuntimeException("Error finding child by cnp", e);
        }

        logger.info("No child found with cnp={}", cnp);
        return null;
    }

    @Override
    public Child findById(Long id) {
        logger.info("Entering findById with id={}", id);
        String sql = "SELECT id, name, cnp, age FROM children WHERE id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Child child = new Child(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("cnp"),
                            resultSet.getInt("age")
                    );
                    logger.info("Child found with id={}: {}", id, child);
                    return child;
                }
            }
        } catch (SQLException e) {
            logger.error("Error in findById for id={}", id, e);
            throw new RuntimeException("Error finding child by id", e);
        }

        logger.info("No child found with id={}", id);
        return null;
    }

    @Override
    public void save(Child child) {
        logger.info("Entering save with child={}", child);
        String sql = "INSERT INTO children(name, cnp, age) VALUES (?, ?, ?)";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, child.getName());
            statement.setString(2, child.getCnp());
            statement.setInt(3, child.getAge());

            int rows = statement.executeUpdate();
            logger.info("Child saved successfully, affected rows={}", rows);
        } catch (SQLException e) {
            logger.error("Error saving child={}", child, e);
            throw new RuntimeException("Error saving child", e);
        }
    }
}
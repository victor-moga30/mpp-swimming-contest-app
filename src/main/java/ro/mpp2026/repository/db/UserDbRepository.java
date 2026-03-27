package ro.mpp2026.repository.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.model.User;
import ro.mpp2026.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDbRepository implements UserRepository {
    private static final Logger logger = LogManager.getLogger(UserDbRepository.class);
    private final JdbcUtils jdbcUtils;

    public UserDbRepository(JdbcUtils jdbcUtils) {
        logger.info("Initializing UserDbRepository");
        this.jdbcUtils = jdbcUtils;
    }

    @Override
    public User findByUsername(String username) {
        logger.info("Entering findByUsername with username={}", username);
        String sql = "SELECT id, username, password_hash, office FROM users WHERE username = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User(
                            resultSet.getLong("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password_hash"),
                            resultSet.getString("office")
                    );
                    logger.info("User found with username={}: {}", username, user);
                    return user;
                }
            }
        } catch (SQLException e) {
            logger.error("Error in findByUsername for username={}", username, e);
            throw new RuntimeException("Error finding user by username", e);
        }

        logger.info("No user found with username={}", username);
        return null;
    }

    @Override
    public User findById(Long id) {
        logger.info("Entering findById with id={}", id);
        String sql = "SELECT id, username, password_hash, office FROM users WHERE id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User(
                            resultSet.getLong("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password_hash"),
                            resultSet.getString("office")
                    );
                    logger.info("User found with id={}: {}", id, user);
                    return user;
                }
            }
        } catch (SQLException e) {
            logger.error("Error in findById for id={}", id, e);
            throw new RuntimeException("Error finding user by id", e);
        }

        logger.info("No user found with id={}", id);
        return null;
    }

    @Override
    public void save(User user) {
        throw new UnsupportedOperationException("Save not supported for users");
    }
}
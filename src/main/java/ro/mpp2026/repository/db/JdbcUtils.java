package ro.mpp2026.repository.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class JdbcUtils {
    private static final Logger logger = LogManager.getLogger(JdbcUtils.class);
    private final Properties properties = new Properties();

    public JdbcUtils() {
        logger.info("Initializing JdbcUtils");

        try (InputStream input = JdbcUtils.class.getClassLoader().getResourceAsStream("bd.config")) {
            if (input == null) {
                logger.error("Configuration file bd.config not found");
                throw new RuntimeException("Configuration file bd.config not found");
            }

            properties.load(input);
            logger.info("Database configuration loaded successfully");
        } catch (Exception e) {
            logger.error("Error loading database configuration", e);
            throw new RuntimeException("Error loading database configuration", e);
        }
    }

    public Connection getConnection() {
        logger.info("Getting database connection");

        try {
            String url = properties.getProperty("jdbc.url");
            Connection connection = DriverManager.getConnection(url);
            logger.info("Database connection established successfully");
            return connection;
        } catch (Exception e) {
            logger.error("Error obtaining database connection", e);
            throw new RuntimeException("Error obtaining database connection", e);
        }
    }
}
package ro.mpp2026.server;

import ro.mpp2026.network.objectprotocol.ContestClientObjectWorker;
import ro.mpp2026.repository.hibernate.ChildHibernateRepository;
import ro.mpp2026.repository.hibernate.EventHibernateRepository;
import ro.mpp2026.repository.hibernate.HibernateUtils;
import ro.mpp2026.repository.hibernate.RegistrationHibernateRepository;
import ro.mpp2026.repository.hibernate.UserHibernateRepository;
import ro.mpp2026.service.ContestService;
import ro.mpp2026.service.IContestServices;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class StartObjectServer {
    public static void main(String[] args) {
        Properties properties = new Properties();

        try (InputStream inputStream = StartObjectServer.class.getClassLoader().getResourceAsStream("server.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (Exception ignored) {
        }

        int port = Integer.parseInt(properties.getProperty("server.port", "55556"));

        IContestServices service = new ContestService(
                new UserHibernateRepository(),
                new ChildHibernateRepository(),
                new EventHibernateRepository(),
                new RegistrationHibernateRepository()
        );

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serverul a pornit pe portul " + port);

            while (true) {
                Socket client = serverSocket.accept();
                ContestClientObjectWorker worker = new ContestClientObjectWorker(service, client);
                Thread thread = new Thread(worker);
                thread.start();
            }
        } catch (Exception e) {
            System.out.println("Eroare la server: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtils.close();
        }
    }
}
package ro.mpp2026.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ro.mpp2026.repository.hibernate.ChildHibernateRepository;
import ro.mpp2026.repository.hibernate.EventHibernateRepository;
import ro.mpp2026.repository.hibernate.HibernateUtils;
import ro.mpp2026.repository.hibernate.RegistrationHibernateRepository;
import ro.mpp2026.repository.hibernate.UserHibernateRepository;
import ro.mpp2026.service.ContestService;
import ro.mpp2026.service.IContestServices;

public class StartGrpcServer {
    public static void main(String[] args) {
        int port = 55556;

        IContestServices service = new ContestService(
                new UserHibernateRepository(),
                new ChildHibernateRepository(),
                new EventHibernateRepository(),
                new RegistrationHibernateRepository()
        );

        try {
            Server server = ServerBuilder
                    .forPort(port)
                    .addService(new ContestGrpcService(service))
                    .build()
                    .start();

            System.out.println("Java gRPC server started on port " + port);
            server.awaitTermination();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtils.close();
        }
    }
}
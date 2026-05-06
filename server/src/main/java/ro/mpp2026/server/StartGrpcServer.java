package ro.mpp2026.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ro.mpp2026.repository.db.ChildDbRepository;
import ro.mpp2026.repository.db.EventDbRepository;
import ro.mpp2026.repository.db.JdbcUtils;
import ro.mpp2026.repository.db.RegistrationDbRepository;
import ro.mpp2026.repository.db.UserDbRepository;
import ro.mpp2026.service.ContestService;
import ro.mpp2026.service.IContestServices;

public class StartGrpcServer {
    public static void main(String[] args) {
        int port = 55556;

        JdbcUtils jdbcUtils = new JdbcUtils();

        IContestServices service = new ContestService(
                new UserDbRepository(jdbcUtils),
                new ChildDbRepository(jdbcUtils),
                new EventDbRepository(jdbcUtils),
                new RegistrationDbRepository(jdbcUtils)
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
        }
    }
}
package exchange_rates;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ExchangeRate {
    private int port = 50052;
    private Server server;

    private void start() throws IOException {
        server = ServerBuilder.forPort(port).addService(new ExchangeRateImpl()).build().start();
        System.out.println("Exchanges rates server started on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("shutting down gRPC server since JVM is shutting down");
            ExchangeRate.this.stop();
            System.err.println("Server shut down");
        }));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final ExchangeRate server = new ExchangeRate();
        server.start();
        server.blockUntilShutdown();
    }
}

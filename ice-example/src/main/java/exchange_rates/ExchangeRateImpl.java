package exchange_rates;

import io.grpc.stub.StreamObserver;
import proto_gen.CurrencyType;
import proto_gen.ExchangeRateOpArguments;
import proto_gen.ExchangeRateOpResult;
import proto_gen.ExchangeRateServiceGrpc;

import java.util.HashMap;
import java.util.Map;

public class ExchangeRateImpl extends ExchangeRateServiceGrpc.ExchangeRateServiceImplBase {
    private Map<proto_gen.CurrencyType, Double> rates = new HashMap<>();

    public ExchangeRateImpl() {
        rates.put(CurrencyType.GBP, 5.03);
        rates.put(CurrencyType.EUR, 4.28);
        Simulator simulator = new Simulator(this.rates);
        simulator.start();
    }

    @Override
    public void getExchangeRate(ExchangeRateOpArguments request, StreamObserver<ExchangeRateOpResult> responseObserver) {
        CurrencyType currencyType = request.getArg();
        responseObserver.onNext(proto_gen.ExchangeRateOpResult.newBuilder().setRes(this.rates.get(currencyType)).build());
        responseObserver.onCompleted();
    }

    private class Simulator extends Thread {
        private Map<proto_gen.CurrencyType, Double> rates;

        public Simulator(Map<proto_gen.CurrencyType, Double> rates) {
            this.rates = rates;
        }

        private void changeExchangesRates() {
            for (double currencyRate : this.rates.values()) {
                currencyRate += 0.01;
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                changeExchangesRates();
                System.out.println(rates);
            }
        }
    }
}

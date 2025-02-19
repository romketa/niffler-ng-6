package guru.qa.niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.CalculateRequest;
import guru.qa.niffler.grpc.CalculateResponse;
import guru.qa.niffler.grpc.Currency;
import guru.qa.niffler.grpc.CurrencyResponse;
import guru.qa.niffler.grpc.CurrencyValues;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CurrencyGrpcTest extends BaseGrpcTest {

  @Test
  void allCurrenciesShouldReturned() {
    final CurrencyResponse response = blockingStub.getAllCurrencies(Empty.getDefaultInstance());
    final List<Currency> allCurrenciesList = response.getAllCurrenciesList();
    Assertions.assertEquals(4, allCurrenciesList.size());
  }

  @Test
  void rateShouldBeCalculatedRubToUsd() {
    CalculateRequest calculateRequest = CalculateRequest.newBuilder()
        .setSpendCurrency(CurrencyValues.RUB)
        .setDesiredCurrency(CurrencyValues.USD)
        .setAmount(1000.0)
        .build();
    final CalculateResponse response = blockingStub.calculateRate(calculateRequest);
    double amount = response.getCalculatedAmount();
    Assertions.assertEquals(15, amount, 0.01);
  }

  @Test
  void rateShouldBeCalculatedRubToEur() {
    CalculateRequest calculateRequest = CalculateRequest.newBuilder()
        .setSpendCurrency(CurrencyValues.RUB)
        .setDesiredCurrency(CurrencyValues.EUR)
        .setAmount(1000.0)
        .build();
    final CalculateResponse response = blockingStub.calculateRate(calculateRequest);
    double amount = response.getCalculatedAmount();
    Assertions.assertEquals(13.89, amount, 0.01);
  }

  @Test
  void rateShouldBeCalculatedRubToKzt() {
    CalculateRequest calculateRequest = CalculateRequest.newBuilder()
        .setSpendCurrency(CurrencyValues.RUB)
        .setDesiredCurrency(CurrencyValues.KZT)
        .setAmount(1000.0)
        .build();
    final CalculateResponse response = blockingStub.calculateRate(calculateRequest);
    double amount = response.getCalculatedAmount();
    Assertions.assertEquals(7142.86, amount, 0.01);
  }
}

package guru.qa.niffler.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import java.io.IOException;
import java.util.List;
import lombok.SneakyThrows;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


public class SpendApiClient {

  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(Config.getInstance().spendUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  public SpendJson createSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.addSpend(spend)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(201, response.code());
    return response.body();
  }

  public SpendJson getSpendById(String id, String username) {
    final Response<SpendJson> response;
    try {
      response = spendApi.getSpendById(id, username)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public List<SpendJson> getAllSpend(String username, CurrencyValues filterCurrency, String from,
      String to) {
    final Response<List<SpendJson>> response;
    try {
      response = spendApi.getAllSpend(username, filterCurrency, from, to)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public SpendJson editSpend(SpendJson spendJson) {
    final Response<SpendJson> response;
    try {
      response = spendApi.editSpend(spendJson)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public void deleteSpend(String username, List<String> ids) {
    final Response<Void> response;
    try {
      response = spendApi.deleteSpend(username, ids)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
  }

  public CategoryJson getCategories(String username, boolean excludeArchived) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.getCategories(username, excludeArchived)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public CategoryJson addCategories(CategoryJson category) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.addCategories(category)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public CategoryJson updateCategory(CategoryJson category) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.updateCategory(category)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }
}

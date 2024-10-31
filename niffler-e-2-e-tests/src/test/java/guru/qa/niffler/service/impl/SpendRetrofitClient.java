package guru.qa.niffler.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import retrofit2.Response;

public class SpendRetrofitClient implements SpendClient {

  private static final Config CFG = Config.getInstance();
  private final SpendApiClient spendApi = new SpendApiClient();

  @Override
  public SpendJson createSpend(SpendJson spend) {
    return spendApi.createSpend(spend);
  }

  @Override
  public CategoryJson createCategory(CategoryJson category) {
    return spendApi.addCategories(category);
  }

  @Override
  public void removeCategory(CategoryJson category) {
    throw new UnsupportedOperationException("Can't remove category by API");
  }

  @Override
  public CategoryJson findOrCreateCategoryByUsernameAndName(String username, String name) {
    throw new UnsupportedOperationException("Can't find or create category by API yet");
  }
}

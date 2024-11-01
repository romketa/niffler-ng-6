package guru.qa.niffler.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SpendRetrofitClient implements SpendClient {

  private static final Config CFG = Config.getInstance();
  private final SpendApiClient spendApi = new SpendApiClient();

  @Override
  @Nonnull
  public SpendJson createSpend(SpendJson spend) {
    return spendApi.createSpend(spend);
  }

  @Override
  @Nonnull
  public CategoryJson createCategory(CategoryJson category) {
    return spendApi.createCategory(category);
  }

  @Override
  public void removeCategory(CategoryJson category) {
    throw new UnsupportedOperationException("Can't remove category by API");
  }

  @Override
  @Nonnull
  public CategoryJson findOrCreateCategoryByUsernameAndName(String username, String name) {
    throw new UnsupportedOperationException("Can't find or create category by API yet");
  }
}

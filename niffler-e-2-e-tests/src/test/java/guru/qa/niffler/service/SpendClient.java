package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface SpendClient {

  @Nonnull
  SpendJson createSpend(SpendJson spend);

  @Nonnull
  CategoryJson createCategory(CategoryJson category);

  void removeCategory(CategoryJson category);

  @Nonnull
  CategoryJson findOrCreateCategoryByUsernameAndName(String username, String name);

  @Nonnull
  List<CategoryJson> getCategories(String username);

  @Nonnull
  List<SpendJson> getSpends(String username);
}

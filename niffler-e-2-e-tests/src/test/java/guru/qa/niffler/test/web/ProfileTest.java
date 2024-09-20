package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.User;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

public class ProfileTest {

  private static final Config CFG = Config.getInstance();
  private final User user = new User("moon", "moon123");

  @Category(
      username = "moon",
      archived = true
  )
  @Test
  void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.getUsername(), user.getPassword());
    Selenide.open(CFG.profileUrl(), ProfilePage.class)
        .verifyThatArchivedCategoryWasAdded(category.name());
  }

  @Category(
      username = "moon",
      archived = false
  )
  @Test
  void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.getUsername(), user.getPassword());
    Selenide.open(CFG.profileUrl(), ProfilePage.class)
        .verifyThatActiveCategoryWasAdded(category.name());
  }
}

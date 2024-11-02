package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileTest {

  private static final Config CFG = Config.getInstance();

  @User(
      username = "moon",
      categories = @Category(
          archived = true
      )
  )
  @Test
  void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("moon", "moon123");
    Selenide.open(CFG.profileUrl(), ProfilePage.class)
        .showArchivedCategories()
        .verifyThatCategoryDisplayed(category.name());
  }

  @User(
      username = "moon",
      categories = @Category(
          archived = false
      )
  )
  @Test
  void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("moon", "moon123");
    Selenide.open(CFG.profileUrl(), ProfilePage.class)
        .verifyThatCategoryDisplayed(category.name());
  }

  @User
  @Test
  void userShouldEditProfile(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .header()
        .toProfilePage()
        .setName("Name")
        .checkName("Name");
  }
}

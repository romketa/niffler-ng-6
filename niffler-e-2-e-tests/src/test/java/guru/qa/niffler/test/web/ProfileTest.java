package guru.qa.niffler.test.web;

import static utils.RandomDataUtils.randomName;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileTest {

  private static final Config CFG = Config.getInstance();

  @User(
      categories = @Category(
          archived = true
      )
  )
  @ApiLogin
  @Test
  void archivedCategoryShouldPresentInCategoriesList(UserJson user) {
    final String categoryName = user.testData().categoryDescriptions()[0];

    Selenide.open(ProfilePage.URL, ProfilePage.class)
        .checkArchivedCategoryExists(categoryName);
  }

  @User(
      categories = @Category(
          archived = false
      )
  )
  @ApiLogin
  @Test
  void activeCategoryShouldPresentInCategoriesList(UserJson user) {
    final String categoryName = user.testData().categoryDescriptions()[0];

    Selenide.open(ProfilePage.URL, ProfilePage.class)
        .checkCategoryExists(categoryName);
  }

  @User
  @ApiLogin
  @Test
  @ScreenShotTest("img/expected-cat.png")
  void shouldUpdateProfileWithAllFieldsSet(UserJson user, BufferedImage expectedAvatar)
      throws IOException {
    final String newName = randomName();

    ProfilePage profilePage = Selenide.open(ProfilePage.URL, ProfilePage.class)
        .uploadPhotoFromClasspath("img/cat.jpeg")
        .setName(newName)
        .submitProfile()
        .checkAlert("Profile successfully updated");

    Selenide.refresh();

    profilePage.checkName(newName)
        .checkPhotoExist()
        .checkAvatarImg(expectedAvatar);
  }

  @User
  @ApiLogin
  @Test
  void shouldUpdateProfileWithOnlyRequiredFields(UserJson user) {
    final String newName = randomName();

    ProfilePage profilePage = Selenide.open(ProfilePage.URL, ProfilePage.class)
        .setName(newName)
        .submitProfile()
        .checkAlert("Profile successfully updated");

    Selenide.refresh();

    profilePage.checkName(newName);
  }

  @Test
  @User
  public void sendInvitationAlertTest(UserJson user) {
    Selenide.open(MainPage.URL, MainPage.class)
        .getHeader()
        .toAllPeoplesPage()
        .sendInvitation("moon")
        .verifyAlertSentInvitation("moon");
  }
}

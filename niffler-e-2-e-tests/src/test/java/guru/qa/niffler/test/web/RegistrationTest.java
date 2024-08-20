package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;

@WebTest
public class RegistrationTest {

  private static final Config CFG = Config.getInstance();
  private static final Faker fakeData = new Faker();

  @Test
  void shouldRegisterNewUser() {
    final User user = new User(fakeData.pokemon().name(), "12345");

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccount()
        .fillRegisterPage(user.getUsername(), user.getPassword(), user.getPassword())
        .submitRegistration()
        .signInAfterRegistration()
        .login(user.getUsername(), user.getPassword())
        .verifyThatLoginWasSuccessful();
  }

  @Test
  void shouldNotRegisterWithExistedUserName() {
    final User user = new User("moon", "12345");

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccount()
        .fillRegisterPage(user.getUsername(), user.getPassword(), user.getPassword())
        .submitRegistration();
    String errorMessage = String.format("Username `%s` already exists", user.getUsername());
    new RegisterPage().verifyErrorMessage(errorMessage);
  }

  @Test
  void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
    final User user = new User(fakeData.pokemon().name(), "12345");

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccount()
        .fillRegisterPage(user.getUsername(), user.getPassword(), "2345")
        .submitRegistration();

    String errorMessage = "Passwords should be equal";
    new RegisterPage().verifyErrorMessage(errorMessage);
  }

  @Test
  void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
    final User user = new User("unexpectedLogin", "12345");

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.getUsername(), user.getPassword());

    new LoginPage().verifyThatUserStayedOnLoginPageAfterUnsuccessfulLogin();
  }
}

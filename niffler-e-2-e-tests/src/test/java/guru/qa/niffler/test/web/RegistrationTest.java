package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;
import utils.RandomDataUtils;

@WebTest
public class RegistrationTest {

  private static final Config CFG = Config.getInstance();


  @Test
  void shouldRegisterNewUser() {

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccount()
        .fillRegisterPage(RandomDataUtils.randomUsername(), "12345", "12345")
        .submitRegistration()
        .signInAfterRegistration()
        .login(RandomDataUtils.randomUsername(), "12345")
        .verifyThatLoginWasSuccessful();
  }

  @Test
  void shouldNotRegisterWithExistedUserName() {

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccount()
        .fillRegisterPage(RandomDataUtils.randomUsername(), "12345", "12345")
        .submitRegistration();
    String errorMessage = String.format("Username `%s` already exists", "12345");
    new RegisterPage().verifyErrorMessage(errorMessage);
  }

  @Test
  void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccount()
        .fillRegisterPage(RandomDataUtils.randomUsername(), "12345", "2345")
        .submitRegistration();

    String errorMessage = "Passwords should be equal";
    new RegisterPage().verifyErrorMessage(errorMessage);
  }

  @Test
  void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(RandomDataUtils.randomUsername(), "12345");

    new LoginPage().verifyThatUserStayedOnLoginPageAfterUnsuccessfulLogin();
  }
}

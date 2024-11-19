package guru.qa.niffler.test.web;

import static utils.RandomDataUtils.randomUsername;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;
import utils.RandomDataUtils;

@WebTest
public class RegistrationTest {

  private static final Config CFG = Config.getInstance();

  @Test
  void shouldRegisterNewUser() {
    String username = randomUsername();
    String password = "12345";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccount()
        .fillRegisterPage(username, password, password)
        .successSubmit()
        .fillLoginPage(username, password)
        .submit(new MainPage())
        .verifyThatLoginWasSuccessful();
  }

  @Test
  void shouldNotRegisterWithExistedUserName() {
    String username = "moon";
    String password = "12345";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccount()
        .fillRegisterPage(username, password, password)
        .submit();
    String errorMessage = String.format("Username `%s` already exists", username);
    new RegisterPage().checkAlertMessage(errorMessage);
  }

  @Test
  void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
    String username = randomUsername();
    String password = "12345";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .createNewAccount()
        .fillRegisterPage(username, password, "2345")
        .errorSubmit();

    String errorMessage = "Passwords should be equal";
    new RegisterPage().checkAlertMessage(errorMessage);
  }

  @Test
  void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
    String username = randomUsername();
    String password = "12345";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(username, password)
        .submit(new LoginPage())
        .verifyThatUserStayedOnLoginPageAfterUnsuccessfulLogin();
  }
}

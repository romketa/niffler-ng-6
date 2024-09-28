package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
public class RegistrationTest {

  private static final Config CFG = Config.getInstance();

  @Test
  void shouldRegisterNewUser() {
    String newUsername = randomUsername();
    String password = "12345";
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
    String newUsername = randomUsername();
    String password = "12345";

    LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
    loginPage.doRegister()
        .fillRegisterPage(newUsername, password, "bad password submit")
        .submit();
    loginPage.checkError("Passwords should be equal");
  }
}

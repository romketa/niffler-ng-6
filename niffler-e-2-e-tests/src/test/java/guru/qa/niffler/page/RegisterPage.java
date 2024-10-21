package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage {

  private static final String USERNAME_INPUT_LOC = "#username";
  private static final String PASSWORD_INPUT_LOC = "#password";
  private static final String PASSWORD_SUBMIT_LOC = "#passwordSubmit";
  private static final String SIGN_UP_BTN_LOC = "button[type='submit']";
  private static final String ERROR_MESSAGE_LOC = ".form__error";
  private static final String SING_IN_LOC = ".form_sign-in";


  public RegisterPage fillRegisterPage(String username, String password, String passwordSubmit) {
    $(USERNAME_INPUT_LOC).setValue(username);
    $(PASSWORD_INPUT_LOC).setValue(password);
    $(PASSWORD_SUBMIT_LOC).setValue(passwordSubmit);
    return this;
  }

  public RegisterPage submitRegistration() {
    $(SIGN_UP_BTN_LOC).click();
    return this;
  }

  public LoginPage signInAfterRegistration() {
    $(SING_IN_LOC).click();
    return new LoginPage();
  }

  public void verifyErrorMessage(String errorMessage) {
    $(ERROR_MESSAGE_LOC).shouldHave(exactText(errorMessage));
  @Nonnull
  public RegisterPage fillRegisterPage(String login, String password, String passwordSubmit) {
    usernameInput.setValue(login);
    passwordInput.setValue(password);
    passwordSubmitInput.setValue(passwordSubmit);
    return this;
  }

  @Nonnull
  public LoginPage successSubmit() {
    submit();
    proceedLoginButton.click();
    return new LoginPage();
  }

  public void submit() {
    submitButton.click();
  }

  @Nonnull
  public RegisterPage checkAlertMessage(String errorMessage) {
    errorContainer.shouldHave(text(errorMessage));
    return this;
  }
}

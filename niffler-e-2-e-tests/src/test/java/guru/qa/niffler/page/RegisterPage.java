package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;

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
  }
}

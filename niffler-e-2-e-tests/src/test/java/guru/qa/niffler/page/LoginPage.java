package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LoginPage {

  private static final String USERNAME_INPUT_LOC = "input[name='username']";
  private static final String PASSWORD_INPUT_LOC = "input[name='password']";
  private static final String SUBMIT_BTN_LOC = "button[type='submit']";
  private static final String CREATE_NEW_ACC = ".form__register";
  private static final String TITLE_EL = "h1";
  private static final String ERROR_MESSAGE = ".form__error";
  private final SelenideElement errorContainer = $(".form__error");

  @Nonnull
  public MainPage login(String username, String password) {
    $(USERNAME_INPUT_LOC).setValue(username);
    $(PASSWORD_INPUT_LOC).setValue(password);
    $(SUBMIT_BTN_LOC).click();
    return new MainPage();
  }

  @Nonnull
  public MainPage successLogin(String username, String password) {
    login(username, password);
    return new MainPage();
  }

  @Nonnull
  public RegisterPage createNewAccount() {
    $(CREATE_NEW_ACC).shouldBe(visible).click();
    return new RegisterPage();
  }

  public void verifyThatUserStayedOnLoginPageAfterUnsuccessfulLogin() {
    $(TITLE_EL).shouldHave(exactText("Log in"));
    $(ERROR_MESSAGE).shouldBe(visible);
  }

  @Nonnull
  public LoginPage checkError(String error) {
    errorContainer.shouldHave(text(error));
    return this;
  }
}

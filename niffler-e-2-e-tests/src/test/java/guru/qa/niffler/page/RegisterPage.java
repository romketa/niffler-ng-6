package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage extends BasePage<RegisterPage>  {

  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement proceedLoginButton = $(".form_sign-in");
  private final SelenideElement errorContainer = $(".form__error");

  @Step("Check that page is loaded")
  @Override
  @Nonnull
  public RegisterPage checkThatPageLoaded() {
    usernameInput.should(visible);
    passwordInput.should(visible);
    passwordSubmitInput.should(visible);
    return this;
  }

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

  @Step("Submit register")
  @Nonnull
  public RegisterPage errorSubmit() {
    submitButton.click();
    return this;
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

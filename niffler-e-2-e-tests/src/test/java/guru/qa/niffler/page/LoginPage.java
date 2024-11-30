package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage>  {

  public static final String URL = Config.getInstance().authUrl() + "login";
  private final SelenideElement usernameInput;
  private final SelenideElement passwordInput;
  private final SelenideElement submitBtn;
  private final SelenideElement createNewAcc;
  private final SelenideElement titleEl;
  private final SelenideElement errorMessage;

  public LoginPage(SelenideDriver driver) {
    super(driver);
    this.usernameInput = driver.$("input[name='username']");
    this.passwordInput = driver.$("input[name='password']");
    this.submitBtn = driver.$("button[type='submit']");
    this.createNewAcc = driver.$(".form__register");
    this.titleEl = driver.$("h1");
    this.errorMessage = driver.$(".form__error");
  }

  public LoginPage() {
    this.usernameInput = Selenide.$("input[name='username']");
    this.passwordInput = Selenide.$("input[name='password']");
    this.submitBtn = Selenide.$("button[type='submit']");
    this.createNewAcc = Selenide.$(".form__register");
    this.titleEl = Selenide.$("h1");
    this.errorMessage = Selenide.$(".form__error");
  }

  @Step("Fill login page with credentials: username: {0}, password: {1}")
  @Nonnull
  public LoginPage fillLoginPage(String login, String password) {
    setUsername(login);
    setPassword(password);
    return this;
  }

  @Step("Set username: {0}")
  @Nonnull
  public LoginPage setUsername(String username) {
    usernameInput.setValue(username);
    return this;
  }

  @Step("Set password: {0}")
  @Nonnull
  public LoginPage setPassword(String password) {
    passwordInput.setValue(password);
    return this;
  }

  @Step("Submit login")
  @Nonnull
  public <T extends BasePage<?>> T submit(T expectedPage) {
    submitBtn.click();
    return expectedPage;
  }

  @Nonnull
  public RegisterPage createNewAccount() {
    createNewAcc.shouldBe(visible).click();
    return new RegisterPage();
  }

  public void verifyThatUserStayedOnLoginPageAfterUnsuccessfulLogin() {
    titleEl.shouldHave(exactText("Log in"));
    errorMessage.shouldBe(visible);
  }

  @Nonnull
  public LoginPage checkError(String error) {
    errorMessage.shouldHave(text(error));
    return this;
  }

  @Step("Check that page is loaded")
  @Override
  @Nonnull
  public LoginPage checkThatPageLoaded() {
    usernameInput.should(visible);
    passwordInput.should(visible);
    return this;
  }
}

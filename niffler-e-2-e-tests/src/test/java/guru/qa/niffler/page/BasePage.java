package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Header;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage<?>> {

  protected static final Config CFG = Config.getInstance();
  protected final SelenideElement alert;
  private final ElementsCollection formErrors;

  protected BasePage(SelenideDriver driver) {
    this.alert = driver.$(".MuiSnackbar-root");
    this.formErrors = driver.$$("p.Mui-error, .input__helper-text");
  }

  public BasePage() {
    this.alert = Selenide.$(".MuiSnackbar-root");
    this.formErrors = Selenide.$$("p.Mui-error, .input__helper-text");
  }

  public abstract T checkThatPageLoaded();

  @SuppressWarnings("unchecked")
  public T checkAlert(String message) {
    alert.shouldHave(text(message));
    return (T) this;
  }
}

package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class Header {

  private final SelenideElement self = $("#root header");

  public void checkHeaderText() {
    self.$("h1").shouldHave(text("Niffler"));
  }
}

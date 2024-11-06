package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.$;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SearchField extends BaseComponent<SearchField>{


  public SearchField() {
    super($("input[aria-label='search']"));
  }


  @Step("Perform search for query {query}")
  @Nonnull
  public SearchField search(String value) {
    self.setValue(value).pressEnter();
    return this;
  }

  @Step("Try to clear search field")
  @Nonnull
  public SearchField clearIfNotEmpty() {
    self.clear();
    return this;
  }

}

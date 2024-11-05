package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SearchField extends BaseComponent{

  private final SelenideElement searchInput = $("input[aria-label='search']");

  @Nonnull
  public SearchField doSearch(String value) {
    searchInput.setValue(value).pressEnter();
    return this;
  }

  @Nonnull
  public SearchField clearIfNotEmpty() {
    searchInput.clear();
    return this;
  }

}

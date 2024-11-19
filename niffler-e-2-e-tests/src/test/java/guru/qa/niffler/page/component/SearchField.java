package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Selenide.$;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SearchField extends BaseComponent<SearchField>{


  public SearchField() {
    super($("input[aria-label='search']"));
  }


  @Nonnull
  public SearchField search(String value) {
    self.setValue(value).pressEnter();
    return this;
  }

  @Nonnull
  public SearchField clearIfNotEmpty() {
    self.clear();
    return this;
  }

}

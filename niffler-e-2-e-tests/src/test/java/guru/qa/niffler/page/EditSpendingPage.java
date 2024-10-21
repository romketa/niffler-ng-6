package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage {

  private static final String DESC_INPUT_LOC = "#description";
  private static final String SAVE_BTN_LOC = "#save";

  private final Calendar calendar = new Calendar($(".SpendingCalendar"));

  @Nonnull
  public EditSpendingPage setNewSpendingDescription(String description) {
    $(DESC_INPUT_LOC).clear();
    $(DESC_INPUT_LOC).setValue(description);
    return this;
  }

  public void save() {
    $(SAVE_BTN_LOC).click();
  }
}

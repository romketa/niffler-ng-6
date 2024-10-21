package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;
import java.util.Date;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EditSpendingPage {

  private static final String DESC_INPUT_LOC = "#description";
  private static final String SAVE_BTN_LOC = "#save";
  private final Calendar calendar = new Calendar($(".MuiPickersLayout-root"));
  private final SelenideElement calendarPick = $("button[aria-label='Choose date']");
  private final SelenideElement amountEl = $("#amount");
  private final SelenideElement catNameEl = $("#category");
  private final SelenideElement descriptionEl = $("#desscription");

  @Nonnull
  public EditSpendingPage setNewSpendingDescription(String description) {
    $(DESC_INPUT_LOC).clear();
    $(DESC_INPUT_LOC).setValue(description);
    return this;
  }

  @Nonnull
  public EditSpendingPage addAmount(String amount) {
    amountEl.setValue(amount);
    return this;
  }

  @Nonnull
  public EditSpendingPage addCategoryName(String categoryName) {
    catNameEl.setValue(categoryName);
    return this;
  }

  @Nonnull
  public EditSpendingPage addDate(Date date) {
    calendarPick.click();
    calendar.selectDateInCalendar(date);
    return this;
  }

  @Nonnull
  public EditSpendingPage addDescription(String desc) {
    descriptionEl.setValue(desc);
    return this;
  }


  public void save() {
    $(SAVE_BTN_LOC).click();
  }
}

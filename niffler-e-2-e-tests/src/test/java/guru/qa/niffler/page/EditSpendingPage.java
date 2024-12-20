package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;
import java.util.Date;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EditSpendingPage extends BasePage<EditSpendingPage> {

  public static final String URL = CFG.frontUrl() + "spending";
  private final SelenideElement descInput = $("#description");
  private final SelenideElement saveBtn = $("#save");
  private final Calendar calendar = new Calendar($(".MuiPickersLayout-root"));
  private final SelenideElement calendarPick = $("button[aria-label^='Choose date']");
  private final SelenideElement amountInput = $("#amount");
  private final SelenideElement categoryInput = $("#category");
  private final SelenideElement descriptionEl = $("#description");

  @Override
  @Nonnull
  public EditSpendingPage checkThatPageLoaded() {
    amountInput.should(visible);
    return this;
  }

  @Nonnull
  public EditSpendingPage setNewSpendingDescription(String description) {
    descInput.clear();
    descInput.setValue(description);
    return this;
  }

  @Nonnull
  public EditSpendingPage addAmount(String amount) {
    amountInput.setValue(amount);
    return this;
  }

  @Nonnull
  public EditSpendingPage addCategoryName(String categoryName) {
    categoryInput.setValue(categoryName);
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
    $(saveBtn).click();
  }
}

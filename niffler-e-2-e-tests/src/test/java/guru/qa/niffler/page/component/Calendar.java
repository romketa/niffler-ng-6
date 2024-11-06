package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class Calendar extends BaseComponent<Calendar>{

  private final SelenideElement yearArrowDropDown = self.find("svg[data-testid='ArrowDropDownIcon']");
  private final ElementsCollection year = self.findAll(".MuiPickersYear-root");
  private final SelenideElement month = self.find(".MuiPickersCalendarHeader-label");
  private final SelenideElement monthSelectRight = self.find("svg[data-testid='ArrowRightIcon']");
  private final SelenideElement monthSelectLeft = self.find("svg[data-testid='ArrowLeftIcon']");
  private final ElementsCollection datePicker = self.findAll(".MuiPickersDay-root");

  public Calendar(SelenideElement self) {
    super(self);
  }

  @Nonnull
  public Calendar selectDateInCalendar(Date date) {
    LocalDate lDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
    selectYear(lDate);
    selectMonth(lDate);
    selectDate(lDate.toEpochDay());
    return this;
  }

  private void selectDate(Long unixDate) {
    datePicker
        .filterBy(enabled)
        .filterBy(attribute("data-timestamp", String.valueOf(unixDate)))
        .first()
        .click();
  }

  private void selectMonth(LocalDate lDate) {
    Month month = lDate.getMonth();
    LocalDate today = LocalDate.now();
    while (!month.equals(today.getMonth())) {
      if (lDate.isBefore(today)) {
        monthSelectLeft.click();
      } else {
        monthSelectRight.click();
      }
    }
  }

  private void selectYear(LocalDate lDate) {
    int year = lDate.getYear();
    if (year != LocalDate.now().getYear()) {
      yearArrowDropDown.click();
      this.year
          .filterBy(text(String.valueOf(year)))
          .first()
          .click();
    }
  }
}

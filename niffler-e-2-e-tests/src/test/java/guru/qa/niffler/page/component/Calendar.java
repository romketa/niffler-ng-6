package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;

import com.codeborne.selenide.SelenideElement;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class Calendar {

  private final SelenideElement self;
  private static final String YEAR_ARROW_DROP_DOWN_LOC = "svg[data-testid='ArrowDropDownIcon']";
  private static final String YEAR_LOC = ".MuiPickersYear-root";
  private static final String MONTH_LOC = ".MuiPickersCalendarHeader-label";
  private static final String MONTH_SELECT_RIGHT_LOC = "svg[data-testid='ArrowRightIcon']";
  private static final String MONTH_SELECT_LEFT_LOC = "svg[data-testid='ArrowLeftIcon']";
  private static final String DATE_PICKER_LOC = ".MuiPickersDay-root";


  public Calendar(SelenideElement self) {
    this.self = self;
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
    self.$$(DATE_PICKER_LOC)
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
        self.$(MONTH_SELECT_LEFT_LOC).click();
      } else {
        self.$(MONTH_SELECT_RIGHT_LOC).click();
      }
    }
  }

  private void selectYear(LocalDate lDate) {
    int year = lDate.getYear();
    if (year != LocalDate.now().getYear()) {
      self.$(YEAR_ARROW_DROP_DOWN_LOC).click();
      self.$$(YEAR_LOC)
          .filterBy(text(String.valueOf(year)))
          .first()
          .click();
    }
  }
}

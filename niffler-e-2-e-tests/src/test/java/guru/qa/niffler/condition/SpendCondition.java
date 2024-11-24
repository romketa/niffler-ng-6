package guru.qa.niffler.condition;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.SpendJson;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.WebElement;

@ParametersAreNonnullByDefault
public class SpendCondition {

  @Nonnull
  public static WebElementsCondition spends(@Nonnull List<SpendJson> expectedSpends) {
    return new WebElementsCondition() {

      private final String expectedSpendsValues = expectedSpends.stream().map(
          spend -> spend.category() + " " + spend.description() + " " + spend.spendDate() + " "
              + spend.amount()).toList().toString();

      @NotNull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (expectedSpends.isEmpty()) {
          throw new IllegalArgumentException("No expected colors given");
        }
        if (expectedSpends.size() != elements.size()) {
          final String message = String.format("List size mismatch (expected: %s, actual: %s)",
              expectedSpends.size(), elements.size());
          return rejected(message, elements);
        }

        boolean passed = true;
        final List<String> actualSpendsList = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
          final WebElement categoryEl = elements.get(i).findElements(new ByCssSelector("td"))
              .get(1);
          final WebElement amountEl = elements.get(i).findElements(new ByCssSelector("td")).get(2);
          final WebElement descEl = elements.get(i).findElements(new ByCssSelector("td")).get(3);
          final WebElement dateEl = elements.get(i).findElements(new ByCssSelector("td")).get(4);

          final String categoryToCheck = expectedSpends.get(i).category().name();
          final double amountToCheck = expectedSpends.get(i).amount();
          final String descToCheck = expectedSpends.get(i).description();
          final String dateToCheck = formatDate(expectedSpends.get(i).spendDate());

          final String category = categoryEl.getText();
          final double amount = Double.parseDouble(amountEl.getText().replaceAll("\\D", ""));
          final String desc = descEl.getText();
          final String date = dateEl.getText();
          actualSpendsList.add(category + " " + desc + " " + date + " " + amount);
          if (passed) {
            passed = categoryToCheck.equals(category)
                & amountToCheck == amount
                & descToCheck.equals(desc)
                & dateToCheck.equals(date);
          }
        }

        if (!passed) {
          final String actualValues = actualSpendsList.toString();
          final String message = String.format(
              "List Bubbles mismatch (expected: %s , actual: %s )", expectedSpendsValues,
              actualValues
          );
          return rejected(message, actualValues);
        }
        return accepted();
      }

      @Override
      public String toString() {
        return expectedSpendsValues;
      }
    };
  }

  private static String formatDate(Date date) {
    LocalDate lDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
    return lDate.format(dtf);
  }

}

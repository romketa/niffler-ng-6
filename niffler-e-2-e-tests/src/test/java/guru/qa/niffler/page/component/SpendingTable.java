package guru.qa.niffler.page.component;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.data.DataFilterValues;
import guru.qa.niffler.page.EditSpendingPage;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SpendingTable {

  private final SelenideElement periodSelector = $("#period");
  private static final String PERIOD_VALUE = "li[data-value='%s']";
  private final SelenideElement editSpend = $("button[aria-label='Edit spending']");
  private final SelenideElement tableSpending = $("table[aria-labelledby='tableTitle']");
  private static final String CHECK_BOX_LOC = "input[type='checkbox']";

  @Nonnull
  public SpendingTable selectPeriod(DataFilterValues period) {
    periodSelector.click();
    $(String.format(PERIOD_VALUE, period.getDataVal())).click();
    return this;
  }

  @Nonnull
  public EditSpendingPage editSpending(String description) {
    searchSpendingByDescription(description);
    editSpend.click();
    return new EditSpendingPage();
  }

  @Nonnull
  public SpendingTable deleteSpending(String description) {
    searchSpendingByDescription(description);
    getSpendingElByDesc(description).$(CHECK_BOX_LOC).click();
    return this;
  }

  @Nonnull
  public SpendingTable searchSpendingByDescription(String description) {
    new SearchField().doSearch(description);
    return this;
  }

  @Nonnull
  public SpendingTable checkTableContains(String... expectedSpends) {
    tableSpending.$$("tr").shouldHave(texts(expectedSpends));
    return this;
  }

  @Nonnull
  public SpendingTable checkTableSize(int expectedSize) {
    tableSpending.$$("tr").shouldHave(size(expectedSize));
    return this;
  }

  private SelenideElement getSpendingElByDesc(String description) {
    return tableSpending.$$("tr")
        .filterBy(Condition.text(description))
        .first();
  }

}

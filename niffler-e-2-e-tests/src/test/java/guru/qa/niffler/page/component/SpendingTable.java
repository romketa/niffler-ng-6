package guru.qa.niffler.page.component;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.SpendCondition;
import guru.qa.niffler.data.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.EditSpendingPage;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SpendingTable extends BaseComponent<SpendingTable>{

  private final SelenideElement periodSelector = $("#period");
  private final SelenideElement editSpend = $("button[aria-label='Edit spending']");
  private final SelenideElement tableSpending = $("table[aria-labelledby='tableTitle']");
  private final SelenideElement deleteBtn = self.$("#delete");
  private final SelenideElement popup = $("div[role='dialog']");

  public SpendingTable() {
    super($("#spendings"));
  }

  @Nonnull
  public SpendingTable selectPeriod(DataFilterValues period) {
    periodSelector.click();
    $(String.format("li[data-value='%s']", period.name())).click();
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
    getSpendingElByDesc(description).$("input[type='checkbox']").click();
    deleteBtn.click();
    popup.$(byText("Delete")).click(usingJavaScript());
    return this;
  }

  @Nonnull
  public SpendingTable searchSpendingByDescription(String description) {
    new SearchField().search(description);
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

  @Nonnull
  public SpendingTable checkSpends(List<SpendJson> spends) {
    tableSpending.$$("tbody tr").should(SpendCondition.spends(spends));
    return this;
  }

  private SelenideElement getSpendingElByDesc(String description) {
    return tableSpending.$$("tr")
        .filterBy(Condition.text(description))
        .first();
  }

}

package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {

  private final String TABLE_ROWS = "#spendings tbody";
  private static final String STATISTICS_BLOCK_LOC = "//div[h2[contains(text(), 'Statistics')]]";
  private static final String HISTORY_OF_SPENDING_LOC = "//div[h2[contains(text(), 'History of Spendings')]]";
  private static final String NEW_SPENDING_BTN_LOC = "//a[contains(text(), 'New spending')]";

  public EditSpendingPage editSpending(String spendingDescription) {
    $(TABLE_ROWS).$$("tr").find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    $(TABLE_ROWS).$$("tr").find(text(spendingDescription)).should(visible);
  }

  public MainPage verifyThatLoginWasSuccessful() {
    $x(STATISTICS_BLOCK_LOC).shouldBe(visible);
    $x(HISTORY_OF_SPENDING_LOC).shouldBe(visible);
    $x(NEW_SPENDING_BTN_LOC).shouldBe(visible, enabled);
    return this;
  }
}

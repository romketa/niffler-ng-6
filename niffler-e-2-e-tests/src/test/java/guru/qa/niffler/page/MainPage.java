package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.enabled;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import com.mifmif.common.regex.Main;
import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

import guru.qa.niffler.page.component.Header;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {

  private final SelenideElement header = $("#root header");
  private final SelenideElement headerMenu = $("ul[role='menu']");
  private final String TABLE_ROWS = "#spendings tbody";
  private static final String STATISTICS_BLOCK_LOC = "//div[h2[contains(text(), 'Statistics')]]";
  private static final String HISTORY_OF_SPENDING_LOC = "//div[h2[contains(text(), 'History of Spendings')]]";
  private static final String NEW_SPENDING_BTN_LOC = "//a[contains(text(), 'New spending')]";
  private static final String SEARCH_INPUT_LOC = "input[aria-label='search']";
  private static final String STAT_COMP_LOC = "#stat";
  private static final String SPENDING_TABLE_LOC = "#spendings";

  @Nonnull
  @Step("Going to friends page people page")
  public FriendsPage friendsPage() {
    header.$("button").click();
    headerMenu.$$("li").find(text("Friends")).click();
    return new FriendsPage();
  }

  @Nonnull
  @Step("Going to all people page")
  public PeoplePage allPeoplesPage() {
    header.$("button").click();
    headerMenu.$$("li").find(text("All People")).click();
    return new PeoplePage();
    }

  @Step("Search spending {spendingDescription}")
  public void searchForSpending(String spendingDescription) {
    $(SEARCH_INPUT_LOC).setValue(spendingDescription).pressEnter();
  }

  @Nonnull
  @Step("Edit spending {spendingDescription}")
  public EditSpendingPage editSpending(String spendingDescription) {
    searchForSpending(spendingDescription);
    $(TABLE_ROWS).$$("tr").find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  @Step("Check that table contains spending {spendingDescription}")
  public MainPage checkThatTableContainsSpending(String spendingDescription) {
    searchForSpending(spendingDescription);
    $(TABLE_ROWS).$$("tr").find(text(spendingDescription)).should(visible);
    return this;
  }

  @Nonnull
  @Step("Verify that login was successful")
  public MainPage verifyThatLoginWasSuccessful() {
    $x(STATISTICS_BLOCK_LOC).shouldBe(visible);
    $x(HISTORY_OF_SPENDING_LOC).shouldBe(visible);
    $x(NEW_SPENDING_BTN_LOC).shouldBe(visible, enabled);
    return this;
  }

  @Nonnull
  @Step("Check that page is loaded")
  public MainPage checkThatPageLoaded() {
    $(STAT_COMP_LOC).should(visible).shouldHave(text("Statistics"));
    $(SPENDING_TABLE_LOC).should(visible).shouldHave(text("History of Spendings"));
    return this;
  }

  @Nonnull
  @Step("Verify that alert of created spending is shown")
  public MainPage verifyAlertCreatedSpending() {
    checkAlert("New spending is successfully created");
    return this;
  }

  @Nonnull
  public Header header() {
    return new Header();
  }
}

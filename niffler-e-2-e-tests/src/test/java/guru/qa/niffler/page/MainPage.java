package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.enabled;

import com.codeborne.selenide.SelenideElement;

import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

import guru.qa.niffler.page.component.Header;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {

  private final SelenideElement header = $("#root header");
  private final SelenideElement headerMenu = $("ul[role='menu']");
  private final SelenideElement tableRows = $("#spendings tbody");
  private SelenideElement statisticsBlock = $x("//div[h2[contains(text(), 'Statistics')]]");
  private final SelenideElement historyOfSpending = $x("//div[h2[contains(text(), 'History of Spendings')]]");
  private final SelenideElement newSpendingBtn = $x("//a[contains(text(), 'New spending')]");
  private final SelenideElement searchInput = $("input[aria-label='search']");
  private final SelenideElement statComp = $("#stat");
  private final SelenideElement spendingTable = $("#spendings");

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
    searchInput.setValue(spendingDescription).pressEnter();
  }

  @Nonnull
  @Step("Edit spending {spendingDescription}")
  public EditSpendingPage editSpending(String spendingDescription) {
    searchForSpending(spendingDescription);
    tableRows.$$("tr").find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  @Step("Check that table contains spending {spendingDescription}")
  public MainPage checkThatTableContainsSpending(String spendingDescription) {
    searchForSpending(spendingDescription);
    tableRows.$$("tr").find(text(spendingDescription)).should(visible);
    return this;
  }

  @Nonnull
  @Step("Verify that login was successful")
  public MainPage verifyThatLoginWasSuccessful() {
    statisticsBlock.shouldBe(visible);
    historyOfSpending.shouldBe(visible);
    newSpendingBtn.shouldBe(visible, enabled);
    return this;
  }

  @Nonnull
  @Step("Check that page is loaded")
  public MainPage checkThatPageLoaded() {
    statComp.should(visible).shouldHave(text("Statistics"));
    spendingTable.should(visible).shouldHave(text("History of Spendings"));
    return this;
  }

  @Nonnull
  @Step("Verify that alert of created spending is shown")
  public MainPage verifyAlertCreatedSpending() {
    checkAlert("New spending is successfully created");
    return this;
  }

  @Nonnull
  public Header getHeader() {
    return new Header();
  }
}

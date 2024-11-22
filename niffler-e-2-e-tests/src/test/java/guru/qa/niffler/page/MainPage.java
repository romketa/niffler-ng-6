package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import io.qameta.allure.Step;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import utils.ScreenDiffResult;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {

  private final SelenideElement tableRows = $("#spendings tbody");
  private SelenideElement statisticsBlock = $x("//div[h2[contains(text(), 'Statistics')]]");
  private final SelenideElement historyOfSpending = $x(
      "//div[h2[contains(text(), 'History of Spendings')]]");
  private final SelenideElement newSpendingBtn = $x("//a[contains(text(), 'New spending')]");
  private final SelenideElement searchInput = $("input[aria-label='search']");
  private final SelenideElement statComp = $("#stat");
  private final SelenideElement statImg = $("canvas[role='img']");
  private final SelenideElement statCell = $("#legend-container");
  protected final Header header = new Header();
  protected final SpendingTable spendingTable = new SpendingTable();
  private final StatComponent statComponent = new StatComponent();

  @Nonnull
  public Header getHeader() {
    return header;
  }

  @Nonnull
  public StatComponent getStatComponent() {
    statComponent.getSelf().scrollIntoView(true);
    return statComponent;
  }

  @Nonnull
  public SpendingTable getSpendingTable() {
    return spendingTable;
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
    statComp.shouldBe(visible).shouldHave(text("Statistics"));
    spendingTable.getSelf().shouldBe(visible).shouldHave(text("History of Spendings"));
    return this;
  }

  @Nonnull
  @Step("Verify that alert of created spending is shown")
  public MainPage verifyAlertCreatedSpending() {
    checkAlert("New spending is successfully created");
    return this;
  }

  @Nonnull
  @Step("Verify statistic image")
  public MainPage checkStatImg(BufferedImage expected) throws IOException {
    BufferedImage actual = ImageIO.read(Objects.requireNonNull(statImg.screenshot()));
    assertFalse(new ScreenDiffResult(
        actual,
        expected
    ));
    return this;
  }

  @Step("Check that statistic cell have text {text}")
  public MainPage checkStatCell(String... text) {
    for (String s : text) {
      statCell.shouldHave(exactText(s));
    }
    return this;
  }
}

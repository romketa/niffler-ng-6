package guru.qa.niffler.page;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FriendsPage extends BasePage<FriendsPage> {

  private final SelenideElement peopleTab = $("a[href='/people/friends']");
  private final SelenideElement allTab = $("a[href='/people/all']");
  private final SelenideElement requestTable = $("#requests");
  private final SelenideElement allTable = $("#all");
  private final SelenideElement friendsTable = $("#friends");
  private final SelenideElement accept = $(byText("Accept"));
  private final SelenideElement decline = $(byText("Decline"));


  @Nonnull
  public FriendsPage checkExistingFriends(String... expectedUsernames) {
    friendsTable.$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
    return this;
  }

  @Nonnull
  public FriendsPage checkNotExistingFriends() {
    friendsTable.$$("tr").shouldHave(size(0));
    return this;
  }

  @Nonnull
  public FriendsPage checkExistingInvitations(String... expectedUsernames) {
    requestTable.$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
    return this;
  }

  @Nonnull
  public FriendsPage selectAllPeopleTab() {
    allTab.click();
    return this;
  }

  @Nonnull
  public FriendsPage checkExistingOutcomeInvitations(String... expectedUsernames) {
    allTable
        .$$("tr")
        .filterBy(text("Waiting"))
        .shouldHave(textsInAnyOrder(expectedUsernames));
    return this;
  }

  @Nonnull
  public FriendsPage acceptFriend() {
    accept.click();
    return this;
  }

  @Nonnull
  public FriendsPage declineFriend() {
    decline.click();
    return this;
  }

  @Step("Check that the page is loaded")
  @Override
  @Nonnull
  public FriendsPage checkThatPageLoaded() {
    peopleTab.shouldBe(Condition.visible);
    allTab.shouldBe(Condition.visible);
    return this;
  }
}

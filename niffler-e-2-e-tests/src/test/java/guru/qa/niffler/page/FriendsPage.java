package guru.qa.niffler.page;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
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

  public static final String URL = CFG.frontUrl() + "people/friends";
  private final SelenideElement peopleTab = $("a[href='/people/friends']");
  private final SelenideElement allTab = $("a[href='/people/all']");
  private final SelenideElement requestsTable = $("#requests");
  private final SelenideElement allTable = $("#all");
  private final SelenideElement friendsTable = $("#friends");
  private final SelenideElement popup = $("div[role='dialog']");


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
    requestsTable.$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
    return this;
  }

  @Step("Check that income invitations count is equal to {expectedCount}")
  @Nonnull
  public FriendsPage checkExistingInvitationsCount(int expectedCount) {
    requestsTable.$$("tr").shouldHave(size(expectedCount));
    return this;
  }

  @Step("Check that friends count is equal to {expectedCount}")
  @Nonnull
  public FriendsPage checkExistingFriendsCount(int expectedCount) {
    friendsTable.$$("tr").shouldHave(size(expectedCount));
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

  @Step("Accept invitation from user: {username}")
  @Nonnull
  public FriendsPage acceptFriendInvitationFromUser(String username) {
    SelenideElement friendRow = requestsTable.$$("tr").find(text(username));
    friendRow.$(byText("Accept")).click();
    return this;
  }

  @Step("Decline invitation from user: {username}")
  @Nonnull
  public FriendsPage declineFriendInvitationFromUser(String username) {
    SelenideElement friendRow = requestsTable.$$("tr").find(text(username));
    friendRow.$(byText("Decline")).click();
    popup.$(byText("Decline")).click(usingJavaScript());
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

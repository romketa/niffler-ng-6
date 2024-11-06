package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class PeoplePage extends BasePage<PeoplePage> {

  private final SelenideElement friendsTab = $("a[href='/people/friends']");
  private final SelenideElement allTab = $("a[href='/people/all']");
  private final SelenideElement peopleTab = $("#all");
  private final SelenideElement search = $("input[aria-label='search']");

  @Step("Check that the page is loaded")
  @Override
  @Nonnull
  public PeoplePage checkThatPageLoaded() {
    peopleTab.shouldBe(Condition.visible);
    allTab.shouldBe(Condition.visible);
    return this;
  }

  @Nonnull
  @Step("Check that invitation sent to user {username}")
  public PeoplePage checkInvitationSentToUser(@Nonnull String username) {
    SelenideElement friendRow = friendsTab.$$("tr").find(text(username));
    friendRow.shouldHave(text("Waiting..."));
    return this;
  }

  @Step("Send invitation to user {username}")
  public PeoplePage sendInvitation(@Nonnull String username) {
    new SearchField().doSearch(username);
    friendsTab.$$("tr").find(text(username)).$("button")
        .click();
    return this;
  }

  @Nonnull
  @Step("Verify that alert of sent invitation to user {username} is shown")
  public PeoplePage verifyAlertSentInvitation(@Nonnull String username) {
    checkAlert(String.format("Invitation sent to %s", username));
    return this;
  }

  @Nonnull
  @Step("Verify that user {username} exist in people list")
  public PeoplePage verifyThatUserExistInPeopleList(@Nonnull String username) {
    peopleTab.$$("tr").find(text(username)).shouldBe(exist);
    return this;
  }

  @Nonnull
  @Step("Filter by username {username}")
  public PeoplePage filterByUsername(@Nonnull String username) {
    search.setValue(username).pressEnter();
    return this;
  }

}

package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class PeoplePage extends BasePage<PeoplePage> {

  private static final String PEOPLE_TAB_LOC = "a[href='/people/friends']";
  private static final String ALL_TAB_LOC = "a[href='/people/all']";
  private static final String PEOPLE_TABLE_LOC = "#all";
  private static final String SEARCH_LOC = "input[aria-label='search']";

  @Nonnull
  @Step("Check that invitation sent to user {username}")
  public PeoplePage checkInvitationSentToUser(@Nonnull String username) {
    SelenideElement friendRow = $(PEOPLE_TABLE_LOC).$$("tr").find(text(username));
    friendRow.shouldHave(text("Waiting..."));
    return this;
  }

  @Step("Send invitation to user {username}")
  public PeoplePage sendInvitation(@Nonnull String username) {
    new SearchField().doSearch(username);
    $(PEOPLE_TABLE_LOC).$$("tr").find(text(username)).$("button")
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
    $(PEOPLE_TAB_LOC).$$("tr").find(text(username)).shouldBe(exist);
    return this;
  }

  @Nonnull
  @Step("Filter by username {username}")
  public PeoplePage filterByUsername(@Nonnull String username) {
    $(SEARCH_LOC).setValue(username).pressEnter();
    return this;
  }

}

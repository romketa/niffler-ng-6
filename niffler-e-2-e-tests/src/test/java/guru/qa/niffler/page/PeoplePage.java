package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class PeoplePage {

  private static final String PEOPLE_TAB_LOC = "a[href='/people/friends']";
  private static final String ALL_TAB_LOC = "a[href='/people/all']";
  private static final String PEOPLE_TABLE_LOC = "#all";
  private static final String SEARCH_LOC = "input[aria-label='search']";

  @Nonnull
  public PeoplePage checkInvitationSentToUser(String username) {
    SelenideElement friendRow = $(PEOPLE_TABLE_LOC).$$("tr").find(text(username));
    friendRow.shouldHave(text("Waiting..."));
    return this;
  }

  @Nonnull
  public PeoplePage verifyThatUserExistInPeopleList(String username) {
    $(PEOPLE_TAB_LOC).$$("tr").find(text(username)).shouldBe(exist);
    return this;
  }

  @Nonnull
  public PeoplePage filterByUsername(String username) {
    $(SEARCH_LOC).setValue(username).pressEnter();
    return this;
  }

}

package guru.qa.niffler.page;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;

public class FriendsPage {

  private static final String PEOPLE_TAB = "a[href='/people/friends']";
  private static final String ALL_TAB = "a[href='/people/all']";
  private static final String REQUEST_TABLE = "#requests";
  private static final String ALL_TABLE = "#all";
  private static final String FRIENDS_TABLE = "#friends";

  public FriendsPage checkExistingFriends(String... expectedUsernames) {
    $(FRIENDS_TABLE).$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
    return this;
  }

  public FriendsPage checkNotExistingFriends() {
    $(FRIENDS_TABLE).$$("tr").shouldHave(size(0));
    return this;
  }

  public FriendsPage checkExistingInvitations(String... expectedUsernames) {
    $(REQUEST_TABLE).$$("tr").shouldHave(textsInAnyOrder(expectedUsernames));
    return this;
  }

  public FriendsPage selectAllPeopleTab() {
    $(ALL_TAB).click();
    return this;
  }

  public FriendsPage checkExistingOutcomeInvitations(String... expectedUsernames) {
    $(ALL_TABLE)
        .$$("tr")
        .filterBy(text("Waiting"))
        .shouldHave(textsInAnyOrder(expectedUsernames));
    return this;
  }
}

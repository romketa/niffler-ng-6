package guru.qa.niffler.test.web;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.page.FriendsPage.URL;

import com.codeborne.selenide.Selenide;
import com.mifmif.common.regex.Main;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.PeoplePage;
import org.junit.jupiter.api.Test;

@WebTest
public class FriendsTest {

  private static final Config CFG = Config.getInstance();

  @User(friends = 1)
  @ApiLogin
  @Test
  public void friendShouldBePresentInFriendsTable(UserJson user) {
    final String friendUsername = user.testData().friendsUsernames()[0];

    Selenide.open(URL, FriendsPage.class)
        .checkExistingFriends(friendUsername);
  }

  @User
  @ApiLogin
  @Test
  public void friendsTableShouldBeEmptyForNewUser() {
    Selenide.open(URL, FriendsPage.class)
        .checkNotExistingFriends();
  }

  @User(incomeInvitations = 1)
  @ApiLogin
  @Test
  public void incomeInvitationBePresentInFriendsTable(UserJson user) {
    final String incomeInvitationFromUser = user.testData().incomeInvitationsUsernames()[0];

    Selenide.open(URL, FriendsPage.class)
        .checkExistingInvitations(incomeInvitationFromUser);
  }

  @User(outcomeInvitations = 1)
  @ApiLogin
  @Test
  public void outcomeInvitationBePresentInAllPeopleTable(UserJson user) {
    final String outcomeInvitationFromUser = user.testData().outcomeInvitationsUsernames()[0];

    Selenide.open(URL, FriendsPage.class)
        .selectAllPeopleTab()
        .checkExistingOutcomeInvitations(outcomeInvitationFromUser);
  }

  @User(incomeInvitations = 1)
  @ApiLogin
  @Test
  void userShouldAcceptFriendInvitation(UserJson user) {
    final String userToAccept = user.testData().incomeInvitationsUsernames()[0];

    FriendsPage friendsPage = open(URL, FriendsPage.class)
        .checkExistingInvitationsCount(1)
        .acceptFriendInvitationFromUser(userToAccept);

    Selenide.refresh();

    friendsPage.checkExistingInvitationsCount(0)
        .checkExistingFriendsCount(1)
        .checkExistingFriends(userToAccept);
  }

  @User(incomeInvitations = 1)
  @Test
  void shouldDeclineInvitation(UserJson user) {
    final String userToDecline = user.testData().incomeInvitationsUsernames()[0];

    FriendsPage friendsPage = Selenide.open(URL, FriendsPage.class)
        .checkExistingInvitationsCount(1)
        .declineFriendInvitationFromUser(userToDecline);

    Selenide.refresh();

    friendsPage.checkExistingInvitationsCount(0)
        .checkExistingFriendsCount(0);

    open(PeoplePage.URL, PeoplePage.class)
        .checkExistingUser(userToDecline);
  }
}

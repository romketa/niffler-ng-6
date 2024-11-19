package guru.qa.niffler.test.web;

import static com.codeborne.selenide.Selenide.open;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
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
  @Test
  public void friendShouldBePresentInFriendsTable(UserJson user) {
    final String friendUsername = user.testData().friendsUsernames()[0];

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .toFriendsPage()
        .checkExistingFriends(friendUsername);
  }

  @User
  @Test
  public void friendsTableShouldBeEmptyForNewUser(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .toFriendsPage()
        .checkNotExistingFriends();
  }

  @User(incomeInvitations = 1)
  @Test
  public void incomeInvitationBePresentInFriendsTable(UserJson user) {
    final String incomeInvitationFromUser = user.testData().incomeInvitationsUsernames()[0];

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .toFriendsPage()
        .checkExistingInvitations(incomeInvitationFromUser);
  }

  @User(outcomeInvitations = 1)
  @Test
  public void outcomeInvitationBePresentInAllPeopleTable(UserJson user) {
    final String outcomeInvitationFromUser = user.testData().outcomeInvitationsUsernames()[0];

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .toFriendsPage()
        .selectAllPeopleTab()
        .checkExistingOutcomeInvitations(outcomeInvitationFromUser);
  }

  @User(incomeInvitations = 1)
  @Test
  void userShouldAcceptFriendInvitation(UserJson user) {
    final String userToAccept = user.testData().incomeInvitationsUsernames()[0];

    FriendsPage friendsPage = open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .toFriendsPage()
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

    FriendsPage friendsPage = Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .toFriendsPage()
        .checkExistingInvitationsCount(1)
        .declineFriendInvitationFromUser(userToDecline);

    Selenide.refresh();

    friendsPage.checkExistingInvitationsCount(0)
        .checkExistingFriendsCount(0);

    open(PeoplePage.URL, PeoplePage.class)
        .checkExistingUser(userToDecline);
  }
}

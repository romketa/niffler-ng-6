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

  @Test
  public void friendShouldBePresentInFriendsTable(
      @UserType(Type.WITH_FRIEND) StaticUser staticUser) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(staticUser.username(), staticUser.password());
    Selenide.open(CFG.friendsUrl(), FriendsPage.class)
        .checkExistingFriends(staticUser.friend());
  }

  @Test
  public void friendsTableShouldBeEmptyForNewUser(@UserType(Type.EMPTY) StaticUser staticUser) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(staticUser.username(), staticUser.password());
    Selenide.open(CFG.friendsUrl(), FriendsPage.class)
        .checkNotExistingFriends();
  }

  @Test
  public void incomeInvitationBePresentInFriendsTable(
      @UserType(Type.WITH_INCOME_REQUEST) StaticUser staticUser) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(staticUser.username(), staticUser.password());
    Selenide.open(CFG.friendsUrl(), FriendsPage.class)
        .checkExistingInvitations(staticUser.income());
  }

  @Test
  public void outcomeInvitationBePresentInAllPeopleTable(
      @UserType(Type.WITH_OUTCOME_REQUEST) StaticUser staticUser) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(staticUser.username(), staticUser.password());
    Selenide.open(CFG.friendsUrl(), FriendsPage.class)
        .selectAllPeopleTab()
        .checkExistingOutcomeInvitations(staticUser.outcome());
  }

  @User(
      incomeInvitations = 1
  )
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

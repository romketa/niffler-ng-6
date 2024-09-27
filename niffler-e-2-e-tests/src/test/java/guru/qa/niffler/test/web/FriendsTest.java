package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class FriendsTest {

  private static final Config CFG = Config.getInstance();


  @Test
  public void friendShouldBePresentInFriendsTable(@UserType(Type.WITH_FRIEND) StaticUser staticUser) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(staticUser.username(), staticUser.password());
    Selenide.open(CFG.friendsUrl(), FriendsPage.class)
        .checkExistingFriends(staticUser.friend());
  }

  @Test
  public void friendsTableShouldBeEmptyForNewUser(@UserType(Type.EMPTY) StaticUser staticUser) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(staticUser.username(), staticUser.password());
    Selenide.open(CFG.friendsUrl(), FriendsPage.class)
        .checkNotExistingFriends();
  }

  @Test
  public void incomeInvitationBePresentInFriendsTable(@UserType(Type.WITH_INCOME_REQUEST) StaticUser staticUser) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(staticUser.username(), staticUser.password());
    Selenide.open(CFG.friendsUrl(), FriendsPage.class)
        .checkExistingInvitations(staticUser.income());
  }

  @Test
  public void outcomeInvitationBePresentInAllPeopleTable(@UserType(Type.WITH_OUTCOME_REQUEST) StaticUser staticUser) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(staticUser.username(), staticUser.password());
    Selenide.open(CFG.friendsUrl(), FriendsPage.class)
        .selectAllPeopleTab()
        .checkExistingOutcomeInvitations(staticUser.outcome());
  }
}

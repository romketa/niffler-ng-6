package guru.qa.niffler.service.impl;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.api.UserApiClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import utils.RandomDataUtils;

@ParametersAreNonnullByDefault
public class UsersRestClient implements UsersClient {

  UserApiClient userApiClient = new UserApiClient();

  AuthApiClient authApiClient = new AuthApiClient();

  @Override
  @Nonnull
  @Step("Create a new user {username}")
  public UserJson createUser(String username, String password) {
    authApiClient.requestRegisterForm();
    authApiClient.register(username, password, password, ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN"));
    Stopwatch sw = Stopwatch.createStarted();
    long maxWaitTime = 5000L;
    while(sw.elapsed(TimeUnit.MILLISECONDS) < maxWaitTime) {
      UserJson user = userApiClient.currentUser(username);
      if(user != null && user.id() != null) {
        return user;
      } else {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          throw new RuntimeException("Error occurred while executing request of getting current user");
        }
      }
    }
    throw new AssertionError("User were not found for presented time");
  }

  @Override
  @Nonnull
  @Step("Create {count} income invitations for user {targetUser}")
  public List<UserJson> createIncomeInvitations(UserJson targetUser, int count) {
    List<UserJson> users = new ArrayList<>();
    int limit = 0;
    while (count > limit) {
      final String username = RandomDataUtils.randomUsername();
      final UserJson user = createUser(username, "12345");
      userApiClient.sendInvitation(user.username(), targetUser.username());
      users.add(user);
      limit++;
    }
    return users;
  }

  @Override
  @Nonnull
  @Step("Create {count} outcome invitations for user {targetUser}")
  public List<UserJson> createOutcomeInvitations(UserJson targetUser, int count) {
    List<UserJson> users = new ArrayList<>();
    int limit = 0;
    while (count > limit) {
      final String username = RandomDataUtils.randomUsername();
      final UserJson user = createUser(username, "12345");
      userApiClient.sendInvitation(targetUser.username(), user.username());
      users.add(user);
      limit++;
    }
    return users;
  }

  @Override
  @Nonnull
  @Step("Create {count} friends for {targetUser} user")
  public List<UserJson> createFriends(UserJson targetUser, int count) {
    List<UserJson> users = new ArrayList<>();
    int limit = 0;
    while (count > limit) {
      final String username = RandomDataUtils.randomUsername();
      final UserJson user = createUser(username, "12345");
      userApiClient.sendInvitation(targetUser.username(), user.username());
      userApiClient.sendInvitation(user.username(), targetUser.username());
      users.add(user);
      limit++;
    }
    return users;
  }
}

package guru.qa.niffler.service.impl;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.api.UserApiClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import utils.RandomDataUtils;

@ParametersAreNonnullByDefault
public class UsersRestClient implements UsersClient {

  private static final String defaultPassword = "12345";
  UserApiClient userApiClient = new UserApiClient();
  AuthApiClient authApiClient = new AuthApiClient();

  @Override
  @Nonnull
  @Step("Create a new user {username}")
  public UserJson createUser(String username, String password) {
    authApiClient.requestRegisterForm();
    authApiClient.register(username, password, password,
        ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN"));
    Stopwatch sw = Stopwatch.createStarted();
    long maxWaitTime = 5000L;
    while (sw.elapsed(TimeUnit.MILLISECONDS) < maxWaitTime) {
      UserJson user = userApiClient.currentUser(username);
      if (user != null && user.id() != null) {
        return user.addTestData(
            new TestData(
                password
            )
        );
      } else {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          throw new RuntimeException(
              "Error occurred while executing request of getting current user");
        }
      }
    }
    throw new AssertionError("User were not found for presented time");
  }

  @Override
  @Nonnull
  @Step("Create {count} income invitations for user {targetUser}")
  public void addIncomeInvitations(UserJson targetUser, int count) {
    int limit = 0;
    while (count > limit) {
      final String username = RandomDataUtils.randomUsername();
      final UserJson newUser = createUser(username, defaultPassword);
      userApiClient.sendInvitation(newUser.username(), targetUser.username());
      targetUser.testData().incomeInvitations().add(newUser);
      limit++;
    }
  }

  @Override
  @Nonnull
  @Step("Create {count} outcome invitations for user {targetUser}")
  public void addOutcomeInvitations(UserJson targetUser, int count) {
    int limit = 0;
    while (count > limit) {
      final String username = RandomDataUtils.randomUsername();
      final UserJson newUser = createUser(username, defaultPassword);
      userApiClient.sendInvitation(targetUser.username(), newUser.username());
      targetUser.testData().outcomeInvitations().add(newUser);
      limit++;
    }
  }

  @Override
  @Nonnull
  @Step("Create {count} friends for {targetUser} user")
  public void addFriends(UserJson targetUser, int count) {
    int limit = 0;
    while (count > limit) {
      final String username = RandomDataUtils.randomUsername();
      final UserJson newUser = createUser(username, defaultPassword);
      userApiClient.sendInvitation(newUser.username(), targetUser.username());
      userApiClient.acceptInvitation(targetUser.username(), newUser.username());
      targetUser.testData()
          .friends()
          .add(newUser);
      limit++;
    }
  }

  @Nonnull
  @Override
  public List<UserJson> findAll(String username, String searchQuery) {
    return userApiClient.findAll(username, searchQuery);
  }

  @Override
  @Nonnull
  public List<UserJson> getFriends(String username, String searchQuery) {
    return Objects.requireNonNull(userApiClient.getFriends(username, searchQuery));
  }
}

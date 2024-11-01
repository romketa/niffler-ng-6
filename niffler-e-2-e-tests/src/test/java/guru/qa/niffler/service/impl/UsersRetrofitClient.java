package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.UserApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import utils.RandomDataUtils;

@ParametersAreNonnullByDefault
public class UsersRetrofitClient implements UsersClient {

  UserApiClient userApiClient = new UserApiClient();

  @Override
  @Nonnull
  public UserJson createUser(String username, String password) {
    throw new UnsupportedOperationException("Can't create user by API");
  }

  @Override
  @Nonnull
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

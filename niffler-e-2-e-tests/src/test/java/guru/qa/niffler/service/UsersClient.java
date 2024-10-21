package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UsersClient {
  @Nonnull
  UserJson createUser(String username, String password);
  List<UserJson> createIncomeInvitations(UserJson targetUser, int count);
  List<UserJson> createOutcomeInvitations(UserJson targetUser, int count);
  List<UserJson> createFriends(UserJson targetUser, int count);

}

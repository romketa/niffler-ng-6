package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UsersClient {

  @Nonnull
  UserJson createUser(String username, String password);

  @Nonnull
  void addIncomeInvitations(UserJson targetUser, int count);

  @Nonnull
  void addOutcomeInvitations(UserJson targetUser, int count);

  @Nonnull
  void addFriends(UserJson targetUser, int count);

}

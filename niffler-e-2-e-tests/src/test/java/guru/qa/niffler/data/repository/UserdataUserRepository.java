package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userdata.UserEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UserdataUserRepository {

  @Nonnull
  UserEntity create(UserEntity user);

  @Nonnull
  UserEntity update(UserEntity user);

  @Nonnull
  Optional<UserEntity> findById(UUID id);

  @Nonnull
  Optional<UserEntity> findByUsername(String username);

  void sendInvitation(UserEntity requester, UserEntity addressee);

  void addFriend(UserEntity requester, UserEntity addressee);

  void remove(UserEntity user);
}

package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserDaoSpringJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class UserdataUserRepositorySpringJdbc implements UserdataUserRepository {

  private final UserDao userDao = new UserDaoSpringJdbc();

  @Override
  @Nonnull
  public UserEntity create(UserEntity user) {
    return userDao.createUser(user);
  }

  @Override
  @Nonnull
  public UserEntity update(UserEntity user) {
    return userDao.update(user);
  }

  @Override
  @Nonnull
  public Optional<UserEntity> findById(UUID id) {
    return userDao.findById(id);
  }

  @Override
  @Nonnull
  public Optional<UserEntity> findByUsername(String username) {
    return userDao.findByUsername(username);
  }

  @Override
  public void sendInvitation(UserEntity requester, UserEntity addressee) {
    requester.addFriends(FriendshipStatus.PENDING, addressee);
    userDao.update(requester);
  }

  @Override
  public void addFriend(UserEntity requester, UserEntity addressee) {
    requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
    addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
    userDao.update(requester);
    userDao.update(addressee);
  }

  @Override
  public void remove(UserEntity user) {
    throw new UnsupportedOperationException();
  }
}

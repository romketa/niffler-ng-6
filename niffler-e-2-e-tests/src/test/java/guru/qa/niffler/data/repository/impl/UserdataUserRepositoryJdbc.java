package guru.qa.niffler.data.repository.impl;

import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.PENDING;
import static guru.qa.niffler.data.jdbc.Connections.holder;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

  private final UserdataUserDao userDao = new UserDaoJdbc();
  private final Config CFG = Config.getInstance();

  @Override
  @Nonnull
  public UserEntity create(UserEntity user) {
    return userDao.create(user);
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
  @Nonnull
  public UserEntity update(UserEntity user) {
    throw new UnsupportedOperationException();
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

  @Override
  public void sendInvitation(UserEntity requester, UserEntity addressee) {
    String sql = "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?, ?)";
    try (PreparedStatement requesterPs = holder(CFG.userdataJdbcUrl()).connection()
        .prepareStatement(sql);) {
      requesterPs.setObject(1, requester.getId());
      requesterPs.setObject(2, addressee.getId());
      requesterPs.setString(3, PENDING.name());
      requesterPs.setDate(4, new java.sql.Date(System.currentTimeMillis()));
      requesterPs.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}

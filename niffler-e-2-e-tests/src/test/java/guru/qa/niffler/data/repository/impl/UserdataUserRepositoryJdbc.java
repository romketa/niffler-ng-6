package guru.qa.niffler.data.repository.impl;

import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.ACCEPTED;
import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.PENDING;
import static guru.qa.niffler.data.tpl.Connections.holder;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

  private final UserDao userDao = new UserDaoJdbc();
  private final Config CFG = Config.getInstance();

  @Override
  public UserEntity create(UserEntity user) {
    return userDao.createUser(user);
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    return userDao.findById(id);
  }

  @Override
  public void addFriend(UserEntity requester, UserEntity addressee) {
    String sql = "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?, ?)";
    try (PreparedStatement requesterPs = holder(CFG.userdataJdbcUrl()).connection()
        .prepareStatement(sql); PreparedStatement addresseePs = holder(
        CFG.userdataJdbcUrl()).connection().prepareStatement(sql)) {
      requesterPs.setObject(1, requester.getId());
      requesterPs.setObject(2, addressee.getId());
      requesterPs.setString(3, ACCEPTED.name());
      requesterPs.setDate(4, new java.sql.Date(System.currentTimeMillis()));
      requesterPs.executeUpdate();

      addresseePs.setObject(1, addressee.getId());
      addresseePs.setObject(2, requester.getId());
      addresseePs.setString(3, ACCEPTED.name());
      addresseePs.setDate(4, new java.sql.Date(System.currentTimeMillis()));
      addresseePs.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addInvitation(UserEntity requester, UserEntity addressee) {
    String sql = "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?, ?)";
    try (PreparedStatement requesterPs = holder(CFG.userdataJdbcUrl()).connection()
        .prepareStatement(sql); ) {
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

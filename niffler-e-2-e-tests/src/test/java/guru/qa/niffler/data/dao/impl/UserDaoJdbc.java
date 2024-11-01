package guru.qa.niffler.data.dao.impl;

import static guru.qa.niffler.data.jdbc.Connections.holder;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserDaoJdbc implements UserDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public UserEntity createUser(UserEntity user) {
    String sql =
        "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name)"
            + " VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(sql,
        Statement.RETURN_GENERATED_KEYS)) {
      setUserParams(ps, user);
      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }
      user.setId(generatedKey);
      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    String sql = "SELECT * FROM \"user\" WHERE id = ?";
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(sql)) {
      ps.setObject(1, id);
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          UserEntity ue = new UserEntity();
          pullUserEntity(ue, rs);
          return Optional.of(ue);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    String sql = "SELECT * FROM \"user\" WHERE username = ?";
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(sql)) {
      ps.setString(1, username);
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          UserEntity ue = new UserEntity();
          pullUserEntity(ue, rs);
          return Optional.of(ue);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void remove(UserEntity user) {
    String sql = "DELETE FROM \"user\" WHERE id = ?";
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(sql)) {

      ps.setObject(1, user.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public UserEntity update(UserEntity user) {
    try (PreparedStatement usersPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
        """
              UPDATE "user"
                SET currency    = ?,
                    firstname   = ?,
                    surname     = ?,
                    photo       = ?,
                    photo_small = ?
                WHERE id = ?
            """);

        PreparedStatement friendsPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
            """
                INSERT INTO friendship (requester_id, addressee_id, status)
                VALUES (?, ?, ?)
                ON CONFLICT (requester_id, addressee_id)
                    DO UPDATE SET status = ?
                """)
    ) {
      usersPs.setString(1, user.getCurrency().name());
      usersPs.setString(2, user.getFirstname());
      usersPs.setString(3, user.getSurname());
      usersPs.setBytes(4, user.getPhoto());
      usersPs.setBytes(5, user.getPhotoSmall());
      usersPs.setObject(6, user.getId());
      usersPs.executeUpdate();

      for (FriendshipEntity fe : user.getFriendshipRequests()) {
        friendsPs.setObject(1, user.getId());
        friendsPs.setObject(2, fe.getAddressee().getId());
        friendsPs.setString(3, fe.getStatus().name());
        friendsPs.setString(4, fe.getStatus().name());
        friendsPs.addBatch();
        friendsPs.clearParameters();
      }
      friendsPs.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  @Override
  public List<UserEntity> findAll() {
    String sql = "SELECT * FROM \"user\"";
    try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(sql)) {

      ps.execute();
      List<UserEntity> userEntities = new ArrayList<>();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          UserEntity ue = new UserEntity();
          pullUserEntity(ue, rs);
        }
        return userEntities;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void setUserParams(PreparedStatement ps, UserEntity user) throws SQLException {
    ps.setString(1, user.getUsername());
    ps.setString(2, user.getCurrency().name());
    ps.setString(3, user.getFirstname());
    ps.setString(4, user.getSurname());
    ps.setBytes(5, user.getPhoto());
    ps.setBytes(6, user.getPhotoSmall());
    ps.setString(7, user.getFullname());
  }

  private void pullUserEntity(UserEntity ue, ResultSet rs) throws SQLException {
    ue.setId(rs.getObject("id", UUID.class));
    ue.setUsername(rs.getString("username"));
    ue.setCurrency(rs.getObject("currency", CurrencyValues.class));
    ue.setFirstname(rs.getString("firstname"));
    ue.setSurname(rs.getString("surname"));
    ue.setPhoto(rs.getBytes("photo"));
    ue.setPhotoSmall(rs.getBytes("small_photo"));
    ue.setFullname(rs.getString("full_name"));
  }
}

package guru.qa.niffler.data.dao.impl;

import static guru.qa.niffler.data.jdbc.Connections.holder;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static guru.qa.niffler.data.jdbc.Connections.holder;

@ParametersAreNonnullByDefault
public class AuthUserDaoJdbc implements AuthUserDao {

  private static final Config CFG = Config.getInstance();
  private final String url = CFG.authJdbcUrl();

  @SuppressWarnings("resource")
  @Nonnull
  @Override
  public AuthUserEntity create(AuthUserEntity authUser) {
    String sql =
        "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement ps = holder(url).connection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, authUser.getUsername());
      ps.setString(2, authUser.getPassword());
      ps.setBoolean(3, authUser.getEnabled());
      ps.setBoolean(4, authUser.getAccountNonExpired());
      ps.setBoolean(5, authUser.getAccountNonLocked());
      ps.setBoolean(6, authUser.getCredentialsNonExpired());

      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }
      authUser.setId(generatedKey);
      return authUser;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("resource")
  @Nonnull
  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    String sql = "SELECT * FROM \"user\"  WHERE id = ?";
    try (PreparedStatement ps = holder(url).connection().prepareStatement(sql)) {
      ps.setObject(1, id);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          AuthUserEntity au = new AuthUserEntity();
          pullAuthUserEntities(au, rs);
          return Optional.of(au);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("resource")
  @Nonnull
  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    try (PreparedStatement ps = holder(url).connection().prepareStatement("""
                SELECT * FROM "user" WHERE username = ?
        """)) {
      ps.setString(1, username);

      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          return Optional.ofNullable(
              AuthUserEntityRowMapper.instance.mapRow(rs, rs.getRow())
          );
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("resource")
  @Nonnull
  @Override
  public List<AuthUserEntity> findAll() {
    String sql = "SELECT * FROM \"category\"";
    try (PreparedStatement ps = holder(url).connection().prepareStatement(sql)) {

      ps.execute();
      List<AuthUserEntity> authUserEntities = new ArrayList<>();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          AuthUserEntity au = new AuthUserEntity();
          pullAuthUserEntities(au, rs);
          authUserEntities.add(au);
        }
        return authUserEntities;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void pullAuthUserEntities(AuthUserEntity au, ResultSet rs) throws SQLException {
    au.setId(rs.getObject("id", UUID.class));
    au.setUsername(rs.getString("username"));
    au.setPassword(rs.getString("password"));
    au.setEnabled(rs.getBoolean("enabled"));
    au.setAccountNonExpired(rs.getBoolean("account_non_expired"));
    au.setAccountNonLocked(rs.getBoolean("account_non_locked"));
    au.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
  }
}

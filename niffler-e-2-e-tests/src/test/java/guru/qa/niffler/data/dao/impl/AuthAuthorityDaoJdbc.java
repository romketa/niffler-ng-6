package guru.qa.niffler.data.dao.impl;

import static guru.qa.niffler.data.jdbc.Connections.holder;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthorityEntityRowMapper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.jetbrains.annotations.NotNull;

import static guru.qa.niffler.data.jdbc.Connections.holder;

@ParametersAreNonnullByDefault
public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

  private static final Config CFG = Config.getInstance();
  private final String url = CFG.authJdbcUrl();

  @SuppressWarnings("resource")
  @Override
  public void create(AuthorityEntity... authority) {
    String sql = "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)";
    try (PreparedStatement ps = holder(url).connection().prepareStatement(sql)) {
      for (AuthorityEntity entity : authority) {
        ps.setObject(1, entity.getUser().getId());
        ps.setString(2, entity.getAuthority().name());

        ps.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("resource")
  @Nonnull
  @Override
  public List<AuthorityEntity> findAll() {
    String sql = "SELECT * FROM \"authority\"";
    try (PreparedStatement ps = holder(url).connection().prepareStatement(sql)) {

      ps.execute();
      List<AuthorityEntity> authorityEntities = new ArrayList<>();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          AuthorityEntity ce = new AuthorityEntity();
          ce.setId(rs.getObject("id", UUID.class));
          ce.setAuthority(rs.getObject("authority", Authority.class));
          AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
          ce.setUser(authUserDao.findById(rs.getObject("userId", UUID.class)).orElseThrow());
          authorityEntities.add(ce);
        }
        return authorityEntities;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("resource")
  @Nonnull
  @Override
  public List<AuthorityEntity> findAllByUserId(UUID userId) {
    try (PreparedStatement ps = holder(url).connection().prepareStatement(
        "SELECT * FROM authority where user_id = ?")) {
      ps.setObject(1, userId);
      ps.execute();
      List<AuthorityEntity> result = new ArrayList<>();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          result.add(AuthorityEntityRowMapper.instance.mapRow(rs, rs.getRow()));
        }
      }
      return result;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}

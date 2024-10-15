package guru.qa.niffler.data.dao.impl;

import static guru.qa.niffler.data.tpl.Connections.holder;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public void create(AuthorityEntity... authority) {
    String sql = "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)";
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(sql)) {
      for (AuthorityEntity entity : authority) {
        ps.setObject(1, entity.getUser().getId());
        ps.setString(2, entity.getAuthority().name());

        ps.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthorityEntity> findAll() {
    String sql = "SELECT * FROM \"authority\"";
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(sql)) {

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
}

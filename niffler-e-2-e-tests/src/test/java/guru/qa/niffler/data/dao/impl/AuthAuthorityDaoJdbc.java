package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

  private static final Config CFG = Config.getInstance();

  private final Connection connection;

  public AuthAuthorityDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void create(AuthorityEntity... authority) {
    String sql = "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      for (AuthorityEntity entity : authority) {
        ps.setObject(1, entity.getUserId());
        ps.setString(2, entity.getAuthority().name());

        ps.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}

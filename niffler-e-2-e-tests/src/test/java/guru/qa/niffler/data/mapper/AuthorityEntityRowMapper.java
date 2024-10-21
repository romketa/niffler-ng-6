package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class AuthorityEntityRowMapper implements RowMapper<AuthorityEntity> {

  public static final AuthorityEntityRowMapper instance = new AuthorityEntityRowMapper();

  private AuthorityEntityRowMapper() {
  }

  @Override
  @Nonnull
  public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    AuthorityEntity result = new AuthorityEntity();
    result.setId(rs.getObject("id", UUID.class));
    result.setAuthority(rs.getObject("authority", Authority.class));
    AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    result.setUser(authUserDao.findById(rs.getObject("userId", UUID.class)).orElseThrow());
    return result;
  }
}

package guru.qa.niffler.data.extractor;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nullable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class AuthUserEntityExtractor implements ResultSetExtractor<AuthUserEntity> {

  public static final AuthUserEntityExtractor instance = new AuthUserEntityExtractor();

  private AuthUserEntityExtractor() {
  }

  @Override
  @Nullable
  public AuthUserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {

    Map<UUID, AuthUserEntity> userMap = new ConcurrentHashMap<>();
    UUID userId = null;
    while (rs.next()) {
      userId = rs.getObject("id", UUID.class);

      AuthUserEntity user = userMap.get(userId);
      if (user == null) {
        AuthUserEntity newUser = new AuthUserEntity();
        newUser.setId(userId);
        newUser.setUsername(rs.getString("username"));
        newUser.setEnabled(rs.getBoolean("enabled"));
        newUser.setAccountNonExpired(rs.getBoolean("account_non_expired"));
        newUser.setAccountNonLocked(rs.getBoolean("account_non_locked"));
        newUser.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
        userMap.put(userId, newUser);
        user = newUser;
      }

      AuthorityEntity authority = new AuthorityEntity(rs.getObject("authority_id", UUID.class));
      authority.setAuthority(Authority.valueOf(rs.getString("authority")));
      user.addAuthorities(authority);
    }
    return userId != null ? userMap.get(userId) : null;
  }
}


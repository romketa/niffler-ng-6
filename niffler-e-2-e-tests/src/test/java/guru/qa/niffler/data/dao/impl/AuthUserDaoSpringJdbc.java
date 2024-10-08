package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class AuthUserDaoSpringJdbc implements AuthUserDao {

  private final DataSource dataSource;

  public AuthUserDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) "
              +
              "VALUES (?,?,?,?,?,?)",
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getPassword());
      ps.setBoolean(3, user.isEnabled());
      ps.setBoolean(4, user.isAccountNonExpired());
      ps.setBoolean(5, user.isAccountNonLocked());
      ps.setBoolean(6, user.isCredentialsNonExpired());
      return ps;
    }, kh);

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    user.setId(generatedKey);
    return user;
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT * FROM \"user\" WHERE id = ?",
            AuthUserEntityRowMapper.instance,
            id
        )
    );
  }

  @Override
  public List<AuthUserEntity> findAll() {
    String sql = "SELECT * FROM \"user\"";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(sql, AuthUserEntityRowMapper.instance);
  }
}
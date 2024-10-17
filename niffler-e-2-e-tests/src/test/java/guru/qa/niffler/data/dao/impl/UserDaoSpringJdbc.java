package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UserEntityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class UserDaoSpringJdbc implements UserDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public UserEntity createUser(UserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) "
              +
              "VALUES (?,?,?,?,?,?,?)",
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getCurrency().name());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getSurname());
      ps.setBytes(5, user.getPhoto());
      ps.setBytes(6, user.getPhotoSmall());
      ps.setString(7, user.getFullname());
      return ps;
    }, kh);

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    user.setId(generatedKey);
    return user;
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT * FROM \"user\" WHERE id = ?",
            UserEntityRowMapper.instance,
            id
        )
    );
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    String sql = "SELECT * FROM \"user\" WHERE id = ?";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            sql,
            UserEntityRowMapper.instance,
            username
        )
    );
  }

  @Override
  public void delete(UserEntity user) {
    String sql = "DELETE FROM \"user\" WHERE id = ?";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    jdbcTemplate.update(sql, user.getId());
  }

  @Override
  public List<UserEntity> findAll() {
    String sql = "SELECT * FROM \"user\"";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    return jdbcTemplate.query(sql, UserEntityRowMapper.instance);
  }
}
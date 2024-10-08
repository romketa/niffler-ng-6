package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.SpendEntityRowMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class SpendDaoSpringJdbc implements SpendDao {


  private final DataSource dataSource;

  public SpendDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public SpendEntity create(SpendEntity spend) {
    String sql = "INSERT INTO \"spend\" (username, spend_date, currency, amount, description, category_id) VALUES (?, ?, ?, ?, ?, ?)";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      setSpendParams(ps, spend);
      return ps;
    }, kh);

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    spend.setId(generatedKey);
    return spend;
  }


  @Override
  public Optional<SpendEntity> findSpendById(UUID id) {
    String sql = "SELECT * FROM \"spend\" WHERE id = ?";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            sql,
            SpendEntityRowMapper.instance,
            id
        )
    );
  }

  @Override
  public List<SpendEntity> findAllByUsername(String username) {
    String sql = "SELECT * FROM \"spend\" WHERE username = ?";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(
        sql,
        SpendEntityRowMapper.instance,
        username
    );
  }

  @Override
  public void deleteSpend(SpendEntity spend) {
    String sql = "DELETE FROM \"spend\" WHERE id = ?";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(sql, spend.getId());
  }

  @Override
  public List<SpendEntity> findAll() {
    String sql = "SELECT * FROM \"spend\"";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(sql, SpendEntityRowMapper.instance);
  }

  private void setSpendParams(PreparedStatement ps, SpendEntity spend) throws SQLException {
    ps.setString(1, spend.getUsername());
    ps.setDate(2, spend.getSpendDate());
    ps.setString(3, spend.getCurrency().name());
    ps.setDouble(4, spend.getAmount());
    ps.setString(5, spend.getDescription());
    ps.setObject(6, spend.getCategory().getId());
  }
}
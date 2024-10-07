package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.mapper.CategoryEntityRowMapper;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class CategoryDaoSpringJdbc implements CategoryDao {

  private final DataSource dataSource;

  public CategoryDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public CategoryEntity create(CategoryEntity category) {
    String sql = "INSERT INTO \"category\" (username, name, archived) VALUES (?, ?, ?)";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, category.getUsername());
      ps.setString(2, category.getName());
      ps.setBoolean(3, category.isArchived());
      return ps;
    }, kh);

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    category.setId(generatedKey);
    return category;
  }

  @Override
  public CategoryEntity update(CategoryEntity category) {
    String sql = "UPDATE \"category\" SET name = ?, username = ?, archived = ? where id = ?";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(sql);
      ps.setString(1, category.getName());
      ps.setString(2, category.getUsername());
      ps.setBoolean(3, category.isArchived());
      ps.setObject(4, category.getId());
      return ps;
    });
    return category;
  }

  @Override
  public Optional<CategoryEntity> findCategoryById(UUID id) {
    String sql = "SELECT * FROM \"category\" WHERE id = ?";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            sql,
            CategoryEntityRowMapper.instance,
            id
        )
    );
  }

  @Override
  public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username,
      String categoryName) {
    String sql = "SELECT * FROM \"category\" WHERE username = ? and name = ?";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            sql,
            CategoryEntityRowMapper.instance,
            username,
            categoryName
        )
    );
  }

  @Override
  public List<CategoryEntity> findAllByUsername(String username) {
    String sql = "SELECT * FROM \"category\" WHERE username = ?";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(sql, CategoryEntityRowMapper.instance, username);
  }

  @Override
  public void deleteCategory(CategoryEntity category) {
    String sql = "DELETE FROM \"category\" WHERE id = ?";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(sql, category.getId());
  }

  @Override
  public List<CategoryEntity> findAll() {
    String sql = "SELECT * FROM \"category\"";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(sql, CategoryEntityRowMapper.instance);
  }
}

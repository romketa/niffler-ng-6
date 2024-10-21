package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.jdbc.DataSources;
import guru.qa.niffler.data.mapper.CategoryEntityRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

@ParametersAreNonnullByDefault
public class CategoryDaoSpringJdbc implements CategoryDao {

  private static final Config CFG = Config.getInstance();

  @Nonnull
  @Override
  public CategoryEntity create(CategoryEntity category) {
    String sql = "INSERT INTO \"category\" (username, name, archived) VALUES (?, ?, ?)";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
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

  @Nonnull
  @Override
  public Optional<CategoryEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(
              "SELECT * FROM category WHERE id = ?",
              CategoryEntityRowMapper.instance,
              id
          )
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Nonnull
  @Override
  public CategoryEntity update(CategoryEntity category) {
    String sql = "UPDATE \"category\" SET name = ?, username = ?, archived = ? where id = ?";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
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

  @Nonnull
  @Override
  public Optional<CategoryEntity> findCategoryById(UUID id) {
    String sql = "SELECT * FROM \"category\" WHERE id = ?";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            sql,
            CategoryEntityRowMapper.instance,
            id
        )
    );
  }

  @Nonnull
  @Override
  public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username,
      String categoryName) {
    String sql = "SELECT * FROM \"category\" WHERE username = ? and name = ?";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            sql,
            CategoryEntityRowMapper.instance,
            username,
            categoryName
        )
    );
  }

  @Nonnull
  @Override
  public List<CategoryEntity> findAllByUsername(String username) {
    String sql = "SELECT * FROM \"category\" WHERE username = ?";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    return jdbcTemplate.query(sql, CategoryEntityRowMapper.instance, username);
  }

  @Nonnull
  @Override
  public void deleteCategory(CategoryEntity category) {
    String sql = "DELETE FROM \"category\" WHERE id = ?";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    jdbcTemplate.update(sql, category.getId());
  }

  @Nonnull
  @Override
  public List<CategoryEntity> findAll() {
    String sql = "SELECT * FROM \"category\"";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
    return jdbcTemplate.query(sql, CategoryEntityRowMapper.instance);
  }
}

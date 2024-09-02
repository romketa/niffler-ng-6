package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoJdbc implements CategoryDao {

  private static final Config CFG = Config.getInstance();

  private final Connection connection;

  public CategoryDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public CategoryEntity create(CategoryEntity category) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO category (username, name, archived) " +
            "VALUES (?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, category.getUsername());
      ps.setString(2, category.getName());
      ps.setBoolean(3, category.isArchived());

      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }
      category.setId(generatedKey);
      return category;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public CategoryEntity update(CategoryEntity category) {
    String sql = "UPDATE category SET name = ?, username = ?, archived = ? where id = ?";
    
      try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, category.getName());
        ps.setString(2, category.getUsername());
        ps.setBoolean(3, category.isArchived());
        ps.setObject(4, category.getId());

        ps.executeUpdate();
        if (ps.getUpdateCount() >= 1) {
          return category;
        } else {
          throw new SQLException("Didn't update anything by presented id");
        }
      
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<CategoryEntity> findCategoryById(UUID id) {
    String sql = "SELECT * FROM category WHERE id = ?";
    try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setObject(1, id);
        ps.execute();
        try (ResultSet rs = ps.getResultSet()) {
          if (rs.next()) {
            CategoryEntity ce = new CategoryEntity();
            pullCategoryEntity(ce, rs);
            return Optional.of(ce);
          } else {
            return Optional.empty();
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username,
      String categoryName) {
    String sql = "SELECT * FROM category WHERE username = ? and name = ?";
    try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, username);
        ps.setString(2, categoryName);

        ps.execute();

        try (ResultSet rs = ps.getResultSet()) {
          if (rs.next()) {
            CategoryEntity ce = new CategoryEntity();
            pullCategoryEntity(ce, rs);
            return Optional.of(ce);
          } else {
            return Optional.empty();
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<CategoryEntity> findAllByUsername(String username) {
    String sql = "SELECT * FROM category WHERE username = ?";
    try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, username);

        ps.execute();
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        try (ResultSet rs = ps.getResultSet()) {
          while (rs.next()) {
            CategoryEntity ce = new CategoryEntity();
            pullCategoryEntity(ce, rs);
            categoryEntities.add(ce);
          }
          return categoryEntities;
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteCategory(CategoryEntity category) {
    String sql = "DELETE FROM category WHERE id = ?";
    try (Connection connection = Databases.connection(CFG.spendJdbcUrl());
        PreparedStatement ps = connection.prepareStatement(sql)) {

      ps.setObject(1, category.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void pullCategoryEntity(CategoryEntity ce, ResultSet rs) throws SQLException {
    ce.setId(rs.getObject("id", UUID.class));
    ce.setUsername(rs.getString("username"));
    ce.setName(rs.getString("name"));
    ce.setArchived(rs.getBoolean("archived"));
  }
}

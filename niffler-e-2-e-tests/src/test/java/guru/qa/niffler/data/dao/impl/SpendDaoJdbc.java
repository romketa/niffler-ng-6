package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public SpendEntity create(SpendEntity spend) {
    String sql = "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(sql,
          Statement.RETURN_GENERATED_KEYS)) {

        setSpendParams(ps, spend);
        ps.executeUpdate();

        final UUID generatedKey;
        try (ResultSet rs = ps.getGeneratedKeys()) {
          if (rs.next()) {
            generatedKey = rs.getObject("id", UUID.class);
          } else {
            throw new SQLException("Can`t find id in ResultSet");
          }
        }
        spend.setId(generatedKey);
        return spend;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<SpendEntity> findSpendById(UUID id) {
    String sql = "SELECT * FROM spend WHERE id = ?";
    try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setObject(1, id);
        ps.execute();

        try (ResultSet rs = ps.getResultSet()) {
          if (rs.next()) {
            SpendEntity se = createSpendEntity(rs);
            return Optional.of(se);
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
  public List<SpendEntity> findAllByUsername(String username) {
    String sql = "SELECT * FROM spend WHERE username = ?";
    try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, username);

        ps.execute();
        List<SpendEntity> spendEntities = new ArrayList<>();
        try (ResultSet rs = ps.getResultSet()) {
          while (rs.next()) {
            SpendEntity se = createSpendEntity(rs);
            spendEntities.add(se);
          }
          return spendEntities;
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteSpend(SpendEntity spendEntity) {
    String sql = "DELETE FROM spend WHERE id = ?";
    try (Connection connection = Databases.connection(CFG.spendJdbcUrl());
        PreparedStatement ps = connection.prepareStatement(sql)) {

      ps.setObject(1, spendEntity.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private SpendEntity createSpendEntity(ResultSet rs) throws SQLException {
    SpendEntity se = new SpendEntity();
    se.setId(rs.getObject("id", UUID.class));
    se.setUsername(rs.getString("username"));
    se.setCurrency(rs.getObject("currency", CurrencyValues.class));
    se.setSpendDate(rs.getDate("spend_date"));
    se.setAmount(rs.getDouble("amount"));
    se.setDescription(rs.getString("description"));
    UUID categoryId = rs.getObject("category_id", UUID.class);
    if (categoryId != null) {
      CategoryEntity category = new CategoryEntity();
      category.setId(categoryId);
      se.setCategory(category);
    }
    return se;
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

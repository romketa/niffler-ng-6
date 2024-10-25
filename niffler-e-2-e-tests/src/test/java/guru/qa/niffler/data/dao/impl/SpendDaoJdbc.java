package guru.qa.niffler.data.dao.impl;

import static guru.qa.niffler.data.jdbc.Connections.holder;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.SpendEntityRowMapper;
import guru.qa.niffler.model.CurrencyValues;
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
    String sql = "INSERT INTO \"spend\" (username, spend_date, currency, amount, description, category_id) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(sql,
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
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<SpendEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
        "SELECT * FROM spend WHERE id = ?"
    )) {
      ps.setObject(1, id);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          return Optional.ofNullable(
              SpendEntityRowMapper.instance.mapRow(rs, rs.getRow())
          );
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<SpendEntity> findSpendById(UUID id) {
    String sql = "SELECT * FROM \"spend\" WHERE id = ?";
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(sql)) {
      ps.setObject(1, id);
      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          SpendEntity se = pullSpendEntity(rs);
          return Optional.of(se);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<SpendEntity> findAllByUsername(String username) {
    String sql = "SELECT * FROM \"spend\" WHERE username = ?";
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(sql)) {
      ps.setString(1, username);

      ps.execute();
      List<SpendEntity> spendEntities = new ArrayList<>();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          SpendEntity se = pullSpendEntity(rs);
          spendEntities.add(se);
        }
        return spendEntities;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteSpend(SpendEntity spendEntity) {
    String sql = "DELETE FROM \"spend\" WHERE id = ?";
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(sql)) {

      ps.setObject(1, spendEntity.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<SpendEntity> findAll() {
    String sql = "SELECT * FROM \"spend\"";
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(sql)) {

      ps.execute();
      List<SpendEntity> spendEntities = new ArrayList<>();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          SpendEntity se = new SpendEntity();
          pullSpendEntity(rs);
          spendEntities.add(se);
        }
        return spendEntities;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public SpendEntity update(SpendEntity spend) {
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
        """
              UPDATE "spend"
                SET spend_date  = ?,
                    currency    = ?,
                    amount      = ?,
                    description = ?
                WHERE id = ?
            """);
    ) {
      ps.setDate(1, new java.sql.Date(spend.getSpendDate().getTime()));
      ps.setString(2, spend.getCurrency().name());
      ps.setDouble(3, spend.getAmount());
      ps.setString(4, spend.getDescription());
      ps.setObject(5, spend.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return spend;
  }

  private SpendEntity pullSpendEntity(ResultSet rs) throws SQLException {
    SpendEntity se = new SpendEntity();
    se.setId(rs.getObject("id", UUID.class));
    se.setUsername(rs.getString("username"));
    se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
    se.setSpendDate(rs.getDate("spend_date"));
    se.setAmount(rs.getDouble("amount"));
    se.setDescription(rs.getString("description"));
    se.setCategory(new CategoryDaoJdbc().findCategoryById(rs.getObject("category_id", UUID.class))
        .orElseThrow(() -> new SQLException("There is no such category in Category table")));
    return se;
  }

  private void setSpendParams(PreparedStatement ps, SpendEntity spend) throws SQLException {
    ps.setString(1, spend.getUsername());
    ps.setDate(2, new java.sql.Date(spend.getSpendDate().getTime()));
    ps.setString(3, spend.getCurrency().name());
    ps.setDouble(4, spend.getAmount());
    ps.setString(5, spend.getDescription());
    ps.setObject(6, spend.getCategory().getId());
  }
}

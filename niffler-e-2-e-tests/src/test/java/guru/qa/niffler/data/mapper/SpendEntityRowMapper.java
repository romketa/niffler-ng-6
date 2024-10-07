package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class SpendEntityRowMapper implements RowMapper<SpendEntity> {

  public static final SpendEntityRowMapper instance = new SpendEntityRowMapper();

  private SpendEntityRowMapper() {
  }

  @Override
  public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    SpendEntity result = new SpendEntity();
    result.setId(rs.getObject("id", UUID.class));
    result.setUsername(rs.getString("username"));
    result.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
    result.setSpendDate(rs.getDate("spend_date"));
    result.setAmount(rs.getDouble("amount"));
    result.setDescription(rs.getString("description"));
//    result.setCategory(new CategoryDaoJdbc(connection).findCategoryById(rs.getObject("category_id", UUID.class));
    return result;
  }


//    se.setCategory(new CategoryDaoJdbc(connection).findCategoryById(rs.getObject("category_id", UUID.class))
//        .orElseThrow(() -> new SQLException("There is no such category in Category table")));
}

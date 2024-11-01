package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class CategoryEntityRowMapper implements RowMapper<CategoryEntity> {

  public static final CategoryEntityRowMapper instance = new CategoryEntityRowMapper();

  private CategoryEntityRowMapper() {
  }

  @Override
  public CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    CategoryEntity result = new CategoryEntity();
    result.setId(rs.getObject("id", UUID.class));
    result.setUsername(rs.getString("username"));
    result.setName(rs.getString("name"));
    result.setArchived(rs.getBoolean("archived"));
    return result;
  }
}

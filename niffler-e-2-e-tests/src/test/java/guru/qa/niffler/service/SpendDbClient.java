package guru.qa.niffler.service;

import static guru.qa.niffler.data.Databases.TransactionIsolation.TRANSACTION_READ_COMMITTED;
import static guru.qa.niffler.data.Databases.transaction;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

public class SpendDbClient {

  private static final Config CFG = Config.getInstance();

  public SpendJson createSpend(SpendJson spend) {
    return transaction(connection -> {
          SpendEntity spendEntity = SpendEntity.fromJson(spend);
          if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                .create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
          }
          return SpendJson.fromEntity(
              new SpendDaoJdbc(connection).create(spendEntity)
          );
        },
        CFG.spendJdbcUrl()
    );
  }

  public CategoryJson createCategory(CategoryJson category) {
    return transaction(connection -> {
          CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
          return CategoryJson.fromEntity(
              new CategoryDaoJdbc(connection).create(categoryEntity)
          );
        },
        CFG.spendJdbcUrl());
  }

  public void deleteCategory(CategoryJson category) {
    transaction(connection -> {
          CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
          CategoryJson.fromEntity(
              new CategoryDaoJdbc(connection).create(categoryEntity)
          );
        },
        CFG.spendJdbcUrl());
  }

  public CategoryJson findOrCreateCategoryByUsernameAndName(String username, String name) {
    return transaction(connection -> {
          return new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username, name)
              .map(CategoryJson::fromEntity)
              .orElse(new CategoryJson(null, name, username, false));
        },
        CFG.spendJdbcUrl());
  }
}

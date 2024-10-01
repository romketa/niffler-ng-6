package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class SpendDbClient {

  private final SpendDao spendDao = new SpendDaoJdbc();
  private final CategoryDao categoryDao = new CategoryDaoJdbc();

  public SpendJson createSpend(SpendJson spend) {
    SpendEntity spendEntity = SpendEntity.fromJson(spend);
    if (spendEntity.getCategory().getId() == null) {
      CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
      spendEntity.setCategory(categoryEntity);
    }
    return SpendJson.fromEntity(
        spendDao.create(spendEntity)
    );
  }

  public CategoryJson createCategory(CategoryJson category) {
    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
    return CategoryJson.fromEntity(
        categoryDao.create(categoryEntity)
    );
  }

  public CategoryJson updateCategory(CategoryJson category) {
    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
    return CategoryJson.fromEntity(
        categoryDao.update(categoryEntity)
    );
  }

  public CategoryJson findCategoryByUsernameAndName(String username, String name) {
    Optional<CategoryEntity> ce = categoryDao.findCategoryByUsernameAndCategoryName(username, name);
    AtomicReference<CategoryJson> ar = new AtomicReference<>();
    ce.ifPresentOrElse(
        entity -> ar.set(CategoryJson.fromEntity(entity)),
        () -> ar.set(new CategoryJson(null, username, name, false)));
    return ar.get();
  }
}

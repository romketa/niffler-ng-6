package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface CategoryDao {

  @Nonnull
  CategoryEntity create(CategoryEntity category);

  @Nonnull
  Optional<CategoryEntity> findById(UUID id);

  @Nonnull
  CategoryEntity update(CategoryEntity category);

  @Nonnull
  Optional<CategoryEntity> findCategoryById(UUID id);

  @Nonnull
  Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName);

  @Nonnull
  List<CategoryEntity> findAllByUsername(String username);

  void deleteCategory(CategoryEntity category);

  @Nonnull
  List<CategoryEntity> findAll();
}

package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendDbClient;
import java.util.UUID;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
import utils.RandomDataUtils;

public class CategoryExtension implements AfterTestExecutionCallback, BeforeEachCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      CategoryExtension.class);

  private final SpendDbClient spendDbClient = new SpendDbClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .filter(user -> user.categories().length > 0)
        .ifPresent(anno -> {
          UUID uuid = UUID.randomUUID();
          Category categoryAnno = anno.categories()[0];
          String title =
              categoryAnno.title().isEmpty() ? RandomDataUtils.randomCategoryName()
                  : categoryAnno.title();
          CategoryJson category = new CategoryJson(
              uuid,
              title,
              anno.username(),
              false
          );
          CategoryJson createdCategory = spendDbClient.createCategory(category);
          if (categoryAnno.archived()) {
            CategoryJson archivedCategory = new CategoryJson(
                createdCategory.id(),
                createdCategory.name(),
                createdCategory.username(),
                true
            );
            createdCategory = spendDbClient.updateCategory(archivedCategory);
          }
          context.getStore(NAMESPACE).put(
              context.getUniqueId(),
              createdCategory
          );
        });
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {

    CategoryJson categoryJson = context.getStore(NAMESPACE)
        .get(context.getUniqueId(), CategoryJson.class);
    if (categoryJson != null && categoryJson.archived()) {
      categoryJson = new CategoryJson(
          categoryJson.id(),
          categoryJson.name(),
          categoryJson.username(),
          true
      );
      spendDbClient.updateCategory(categoryJson);
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
  }

  @Override
  public CategoryJson resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE)
        .get(extensionContext.getUniqueId(), CategoryJson.class);
  }
}

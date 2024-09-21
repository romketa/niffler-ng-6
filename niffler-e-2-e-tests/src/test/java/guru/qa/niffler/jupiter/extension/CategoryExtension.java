package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import java.util.UUID;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements AfterTestExecutionCallback, BeforeEachCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      CategoryExtension.class);

  private final SpendApiClient spendApiClient = new SpendApiClient();
  private static final Faker fakeData = new Faker();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
        .ifPresent(anno -> {
          UUID uuid = UUID.randomUUID();
          String title = anno.title().isEmpty() ? fakeData.app().name() : anno.title();
          CategoryJson category = new CategoryJson(
              uuid,
              title,
              anno.username(),
              false
          );
          CategoryJson createdCategory = spendApiClient.addCategories(category);
          if (anno.archived()) {
            CategoryJson archivedCategory = new CategoryJson(
                createdCategory.id(),
                createdCategory.name(),
                createdCategory.username(),
                true
            );
            createdCategory = spendApiClient.updateCategory(archivedCategory);
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
    if (categoryJson.archived()) {
      categoryJson = new CategoryJson(
          categoryJson.id(),
          categoryJson.name(),
          categoryJson.username(),
          true
      );
      spendApiClient.updateCategory(categoryJson);
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

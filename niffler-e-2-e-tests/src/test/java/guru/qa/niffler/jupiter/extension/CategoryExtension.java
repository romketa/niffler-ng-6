package guru.qa.niffler.jupiter.extension;

import static utils.RandomDataUtils.randomCategoryName;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.SpendDbClient;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
public class CategoryExtension implements
    BeforeEachCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      CategoryExtension.class);

  private final SpendDbClient spendDbClient = new SpendDbClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(userAnno -> {
          if (ArrayUtils.isNotEmpty(userAnno.categories())) {
            List<CategoryJson> result = new ArrayList<>();

            UserJson user = context.getStore(UserExtension.NAMESPACE)
                .get(context.getUniqueId(), UserJson.class);

            for (Category categoryAnno : userAnno.categories()) {
              final String categoryName = "".equals(categoryAnno.name())
                  ? randomCategoryName()
                  : categoryAnno.name();

              CategoryJson category = new CategoryJson(
                  null,
                  categoryName,
                  user != null ? user.username() : userAnno.username(),
                  categoryAnno.archived()
              );

              CategoryJson createdCategory = spendDbClient.createCategory(category);
              result.add(createdCategory);
            }

            if (user != null) {
              user.testData().categories().addAll(result);
            } else {
              context.getStore(NAMESPACE).put(
                  context.getUniqueId(),
                  result
              );
            }
          }
        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson[].class);
  }

  @Override
  @SuppressWarnings("unchecked")
  public CategoryJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return (CategoryJson[]) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class)
        .toArray();
  }
}

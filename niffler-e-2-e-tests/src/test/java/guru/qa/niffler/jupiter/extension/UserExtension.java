package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.impl.UsersRetrofitClient;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
import utils.RandomDataUtils;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      UserExtension.class);
  private static final String defaultPassword = "12345";

  private final UsersClient userClient = new UsersRetrofitClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(userAnno -> {
          if ("".equals(userAnno.username())) {
            final String username = RandomDataUtils.randomUsername();
            UserJson testUser = userClient.createUser(username, defaultPassword);
            List<UserJson> income = userClient.createIncomeInvitations(testUser, userAnno.income());
            List<UserJson> outcome = userClient.createIncomeInvitations(testUser,
                userAnno.income());
            List<UserJson> friends = userClient.createFriends(testUser, userAnno.friends());
            context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                testUser.addTestData(
                    new TestData(
                        defaultPassword,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        income.stream().map(UserJson::username).toList(),
                        outcome.stream().map(UserJson::username).toList(),
                        friends.stream().map(UserJson::username).toList()
                    )
                )
            );
          }
        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
  }

  @Override
  public UserJson resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), UserJson.class);
  }
}

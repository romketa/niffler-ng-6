package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.User;
import io.qameta.allure.Allure;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class UsersQueueExtension implements
    BeforeTestExecutionCallback,
    AfterTestExecutionCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      UsersQueueExtension.class);

  public record StaticUser(User user, String friend, String income, String outcome) {

  }

  private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_FRIENDS_USERS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_INCOME_REQUESTS_USERS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_OUTCOME_REQUESTS_USERS = new ConcurrentLinkedQueue<>();

  static {
    EMPTY_USERS.add(new StaticUser(new User("Venonat", "12345"), null, null, null));
    WITH_FRIENDS_USERS.add(new StaticUser(new User("moon", "moon123"), "larisa", null, null));
    WITH_INCOME_REQUESTS_USERS.add(
        new StaticUser(new User("larisa", "larisa123"), null, "vadim123", null));
    WITH_OUTCOME_REQUESTS_USERS.add(
        new StaticUser(new User("vadim123", "vadim123"), null, null, "larisa"));
  }

  @Target(ElementType.PARAMETER)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface UserType {

    Type value() default Type.EMPTY;

    enum Type {
      EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
    }
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) {
    Arrays.stream(context.getRequiredTestMethod().getParameters())
        .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
        .map(parameter -> parameter.getAnnotation(UserType.class))
        .forEach(ut -> {
              Optional<StaticUser> user = Optional.empty();
              StopWatch sw = StopWatch.createStarted();
              while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                switch (ut.value()) {
                  case EMPTY -> user = Optional.ofNullable(EMPTY_USERS.poll());
                  case WITH_FRIEND -> user = Optional.ofNullable(WITH_FRIENDS_USERS.poll());
                  case WITH_INCOME_REQUEST ->
                      user = Optional.ofNullable(WITH_INCOME_REQUESTS_USERS.poll());
                  case WITH_OUTCOME_REQUEST ->
                      user = Optional.ofNullable(WITH_OUTCOME_REQUESTS_USERS.poll());
                }
              }
              Allure.getLifecycle().updateTestCase(testCase ->
                  testCase.setStart(new Date().getTime())
              );
              user.ifPresentOrElse(
                  u ->
                      ((Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                          .getOrComputeIfAbsent(
                              context.getUniqueId(),
                              key -> new HashMap<>()
                          )).put(ut, u),
                  () -> {
                    throw new IllegalStateException("Can`t obtain user after 30s.");
                  }
              );
            }
        );
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    Map<UserType, StaticUser> utMap = context.getStore(NAMESPACE)
        .get(context.getUniqueId(), Map.class);

    for (Map.Entry<UserType, StaticUser> el : utMap.entrySet()) {
      switch (el.getKey().value()) {
        case EMPTY -> EMPTY_USERS.add(el.getValue());
        case WITH_FRIEND -> WITH_FRIENDS_USERS.add(el.getValue());
        case WITH_INCOME_REQUEST -> WITH_INCOME_REQUESTS_USERS.add(el.getValue());
        case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUESTS_USERS.add(el.getValue());
      }
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
        && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
  }

  @Override
  public StaticUser resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    UserType userType = parameterContext.getParameter().getAnnotation(UserType.class);
    Map<UserType, StaticUser> usersMap = extensionContext.getStore(NAMESPACE)
        .get(extensionContext.getUniqueId(), Map.class);
    return usersMap != null ? usersMap.get(userType) : null;
  }
}

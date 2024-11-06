package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.impl.UsersDbClient;
import guru.qa.niffler.service.impl.UsersRestClient;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;

public class UsersClientExtension implements TestInstancePostProcessor {
  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
    for (Field field : testInstance.getClass().getDeclaredFields()) {
      if (field.getType().isAssignableFrom(UsersClient.class)) {
        field.setAccessible(true);
        if("db".equals(System.getenv("users.client"))) {
          field.set(testInstance, new UsersDbClient());
        } else {
          field.set(testInstance, new UsersRestClient());
        }
      }
    }
  }
}

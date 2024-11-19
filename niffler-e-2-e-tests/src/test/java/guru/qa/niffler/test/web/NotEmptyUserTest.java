package guru.qa.niffler.test.web;

import static org.junit.jupiter.api.Assertions.assertFalse;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.impl.UsersRestClient;
import java.util.List;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@Order(Integer.MAX_VALUE)
public class NotEmptyUserTest {

  UsersClient usersClient = new UsersRestClient();

  @User
  @Test
  void usersListShouldBeNotEmpty(UserJson user) {
    List<UserJson> response = usersClient.findAll(user.username(), null);
    assertFalse(response.isEmpty(), "User list should be not empty");
  }
}

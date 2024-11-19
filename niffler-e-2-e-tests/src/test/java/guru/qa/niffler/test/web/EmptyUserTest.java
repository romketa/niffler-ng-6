package guru.qa.niffler.test.web;

import static org.junit.jupiter.api.Assertions.assertTrue;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.impl.UsersRestClient;
import java.util.List;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@Order(1)
public class EmptyUserTest {

  UsersClient usersClient = new UsersRestClient();

  @Test
  @User
  void usersListShouldBeEmpty(UserJson user) {
    List<UserJson> response = usersClient.findAll(user.username(), null);
    assertTrue(response.isEmpty(), "User list should be empty");
  }

}

package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.Test;


public class JdbcTest {

  UserDbClient userDbClient = new UserDbClient();

  @Test
  void txSpendTest() {
    SpendDbClient spendDbClient = new SpendDbClient();

    SpendJson spend = spendDbClient.createSpend(
        new SpendJson(
            null,
            new Date(),
            new CategoryJson(
                null,
                "cat-name-tx-2",
                "duck",
                false
            ),
            CurrencyValues.RUB,
            1000.0,
            "spend-name-tx",
            null
        )
    );

    System.out.println(spend);
  }

  // в этом тесте по идее пользователь должен создаться по первой транзакции, во второй нет и не добавиться в user data
// т.к. встречается ошибка при выполнении второй транзакции
//  почитал доку, был уверен что поведение именно такое, НО сколько бы я эксперементировал, у меня выполняется первая транзкция, фейлится вторая
//  НО записи не добавляются ни в одну ни во вторую таблицу, непонимаю.
  @Test
  void userChainedTx() {
    UserJson user = userDbClient.createUserChainedTx(new UserJson(
        null,
        "valentin-9",
        null,
        null,
        null,
        CurrencyValues.RUB,
        null,
        null,
        null
    ));
    System.out.println("Created user - " + user);
  }

  @Test
  void userJdbcTxTest() {
    UserJson user = userDbClient.createUserJdbcTx(
        new UserJson(
            null,
            "valentin-7",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );
    System.out.println("Created user - " + user);
  }

  @Test
  void userSpringJdbcTxTest() {
    UserJson userMark = new UserJson(
        null,
        "markeloff-1",
        "mark",
        "awper",
        "mark awper",
        CurrencyValues.RUB,
        null,
        null,
        null
    );

    userDbClient.createUserSpringJdbcTx(userMark);
    System.out.println("Created user - " + userMark);
  }

  @Test
  void userSpringJdbcWithoutTxTest() {
    UserJson user = userDbClient.createUserSpringJdbcWithoutTx(
        new UserJson(
            null,
            "valentin-2",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );
    System.out.println("Created user - " + user);
  }

  @Test
  void userJdbcWithoutTxTest() {
    UserJson user = userDbClient.createUserJdbcWithoutTx(
        new UserJson(
            null,
            "valentin-3",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );
    System.out.println("Created user - " + user);
  }

  @Test
  void userJdbcAddFriendTest() {
    UserJson user = userDbClient.createUserJdbcTx(
        new UserJson(
            null,
            "user-1",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );

    UserJson friend = userDbClient.createUserJdbcTx(
        new UserJson(
            null,
            "friend-1",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );

    UserJson income = userDbClient.createUserJdbcTx(
        new UserJson(
            null,
            "income-1",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );

    UserJson outcome = userDbClient.createUserJdbcTx(
        new UserJson(
            null,
            "outcome-1",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );

    userDbClient.addInvitation(income, user);
    userDbClient.addInvitation(user, outcome);
    userDbClient.addFriends(user, friend);
  }

  @Test
  void userJdbcAddInvitationTest() {

  }
}

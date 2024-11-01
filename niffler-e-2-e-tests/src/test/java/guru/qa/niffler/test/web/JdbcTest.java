package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.SpendDbClient;
import guru.qa.niffler.service.OldUserDbClient;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


public class JdbcTest {

  OldUserDbClient oldUserDbClient = new OldUserDbClient();

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
    UserJson user = oldUserDbClient.createUserChainedTx(new UserJson(
        null,
        "valentin-9",
        null,
        null,
        null,
        CurrencyValues.RUB,
        null,
        null,
        null,
        null
    ));
    System.out.println("Created user - " + user);
  }

  @Test
  void userJdbcTxTest() {
    UserJson user = oldUserDbClient.createUserJdbcTx(
        new UserJson(
            null,
            "valentin-7",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null,
            null
        )
    );
    System.out.println("Created user - " + user);
  }

  @Test
  void userSpringJdbcTxTest() {
    UserJson user = oldUsersDbClient.createUserSpringJdbcTx(
        "mark-1",
        "12345"
    );
    System.out.println("Created user - " + user.username());
  }

  @Test
  void userSpringJdbcWithoutTxTest() {
    UserJson user = oldUserDbClient.createUserSpringJdbcWithoutTx(
        new UserJson(
            null,
            "valentin-2",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null,
            null
        )
    );
    System.out.println("Created user - " + user);
  }

  @Test
  void userJdbcWithoutTxTest() {
    UserJson user = oldUserDbClient.createUserJdbcWithoutTx(
        new UserJson(
            null,
            "valentin-3",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null,
            null
        )
    );
    System.out.println("Created user - " + user);
  }

  @Test
  void userJdbcAddFriendTest() {
    UserJson user = oldUserDbClient.createUserJdbcTx(
        new UserJson(
            null,
            "user-1",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null,
            null
        )
    );

    UserJson friend = oldUserDbClient.createUserJdbcTx(
        new UserJson(
            null,
            "friend-1",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null,
            null
        )
    );

    UserJson income = oldUserDbClient.createUserJdbcTx(
        new UserJson(
            null,
            "income-1",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null,
            null
        )
    );

    UserJson outcome = oldUserDbClient.createUserJdbcTx(
        new UserJson(
            null,
            "outcome-1",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null,
            null
        )
    );

    oldUserDbClient.addInvitation(income, user);
    oldUserDbClient.addInvitation(user, outcome);
    oldUserDbClient.addFriends(user, friend);
  }

  static OldUserDbClient oldUsersDbClient = new OldUserDbClient();

  @ValueSource(strings = {
      "valentin-10"
  })
  @ParameterizedTest
  void springJdbcTest(String uname) {

    UserJson user = oldUsersDbClient.createUserSpringJdbcTx(
        uname,
        "12345"
    );

    oldUsersDbClient.addIncomeInvitation(user, 1);
    oldUsersDbClient.addOutcomeInvitation(user, 1);
  }
}

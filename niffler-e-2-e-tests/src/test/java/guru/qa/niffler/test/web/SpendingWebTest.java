package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Bubble;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import java.util.Date;
import javax.annotation.Nonnull;
import org.junit.jupiter.api.Test;

@WebTest
public class SpendingWebTest {

  private static final Config CFG = Config.getInstance();

  @Test
  @ApiLogin
  @User
  void newSpendingAlertTest() {
    Selenide.open(EditSpendingPage.URL, EditSpendingPage.class)
        .addAmount("100")
        .addCategoryName("Category name")
        .addDate(new Date())
        .addDescription("Description").save();

    new MainPage().checkThatTableContainsSpending("Category name").verifyAlertCreatedSpending();
  }

  @User(spendings = @Spending(category = "Some category", description = "Keep", amount = 990))
  @ApiLogin
  @Test
  void categoryDescriptionShouldBeChangedFromTable() {
    final String newDescription = "some desc 1";

    Selenide.open(MainPage.URL, MainPage.class)
        .editSpending("Some category")
        .setNewSpendingDescription(newDescription).save();

    new MainPage().checkThatTableContainsSpending(newDescription);
  }

  @User
  @ApiLogin
  @Test
  void userShouldAddNewSpending() {
    Selenide.open(EditSpendingPage.URL, EditSpendingPage.class)
        .addAmount("100")
        .addCategoryName("Category name")
        .addDate(new Date())
        .addDescription("Description")
        .save();

    new MainPage().checkThatTableContainsSpending("Category name");
  }

  @User(
      spendings = {
          @Spending(
              category = "Обучение",
              description = "Обучение Advanced 2.0",
              amount = 79990
          ),
          @Spending(
              category = "Цифровое пианино",
              description = "Casio CW-100",
              amount = 50000
          )
      }
  )
  @ApiLogin
  @Test
  void checkStatComponentTest()
      throws InterruptedException {
    StatComponent statComponent = Selenide.open(MainPage.URL, MainPage.class)
        .getStatComponent();

    Thread.sleep(3000);

    Bubble bubbleStudy = new Bubble(Color.yellow, "Обучение 79990 ₽");
    Bubble bubblePiano = new Bubble(Color.green, "Цифровое пианино 50000 ₽");

    statComponent.checkFixedStatBubbles(bubbleStudy, bubblePiano);
  }

  @User(
      spendings = {
          @Spending(
              category = "Обучение",
              description = "Обучение Advanced 2.0",
              amount = 79990
          ),
          @Spending(
              category = "Цифровое пианино",
              description = "Casio CW-100",
              amount = 50000
          )
      }
  )
  @ApiLogin
  @Test
  void checkStatComponentAnyOrderTest()
      throws InterruptedException {
    StatComponent statComponent = Selenide.open(MainPage.URL, MainPage.class)
        .getStatComponent();

    Thread.sleep(3000);

    Bubble bubbleStudy = new Bubble(Color.yellow, "Обучение 79990 ₽");
    Bubble bubblePiano = new Bubble(Color.green, "Цифровое пианино 50000 ₽");

    statComponent.checkStatBubblesAnyOrder(bubblePiano, bubbleStudy);
  }

  @User(
      spendings = {
          @Spending(
              category = "Обучение",
              description = "Обучение Advanced 2.0",
              amount = 79990
          ),
          @Spending(
              category = "Цифровое пианино",
              description = "Casio CW-100",
              amount = 50000
          ),
          @Spending(
              category = "Гитара",
              description = "Fender CS60",
              amount = 30000
          )
      }
  )
  @ApiLogin
  @Test
  void checkContainsStatComponentTest()
      throws InterruptedException {
    StatComponent statComponent = Selenide.open(MainPage.URL, MainPage.class)
        .getStatComponent();

    Thread.sleep(3000);

    Bubble bubbleStudy = new Bubble(Color.yellow, "Обучение 79990 ₽");

    statComponent.checkStatBubblesContains(bubbleStudy);
  }

  @User(
      spendings = {
          @Spending(
              category = "Обучение",
              description = "Обучение Advanced 2.0",
              amount = 79990
          )
          ,
          @Spending(
              category = "Цифровое пианино",
              description = "Casio CW-100",
              amount = 50000
          ),
          @Spending(
              category = "Гитара",
              description = "Fender CS60",
              amount = 30000
          )
      }
  )
  @ApiLogin
  @Test
  void checkSpendsTest(UserJson user)
      throws InterruptedException {
    SpendingTable spendingTable = Selenide.open(MainPage.URL, MainPage.class)
        .getSpendingTable();

    Thread.sleep(3000);

    spendingTable.checkSpends(user.testData().spends());
  }

  @User(
      spendings = @Spending(
          category = "Обучение",
          description = "Обучение Advanced 2.0",
          amount = 79990
      )
  )
  @ApiLogin
  @Test
  @ScreenShotTest(value = "img/clear-stat.png",
      rewriteExpected = true)
  void deleteSpendingTest(@Nonnull UserJson user) {
    Selenide.open(MainPage.URL, MainPage.class)
        .getSpendingTable()
        .deleteSpending("Обучение Advanced 2.0")
        .checkTableSize(0);
  }
}


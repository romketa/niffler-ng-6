package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.condition.StatConditions.Bubble;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.StatComponent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import javax.annotation.Nonnull;
import org.junit.jupiter.api.Test;

@WebTest
public class SpendingWebTest {

  private static final Config CFG = Config.getInstance();

  @Test
  @User
  void newSpendingAlertTest(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password()).submit(new MainPage())
        .getHeader().addSpendingPage().addAmount("100").addCategoryName("Category name")
        .addDate(new Date()).addDescription("Description").save();

    new MainPage().checkThatTableContainsSpending("Category name").verifyAlertCreatedSpending();
  }

  @User(spendings = @Spending(category = "Some category", description = "Keep", amount = 990))
  @Test
  void categoryDescriptionShouldBeChangedFromTable(UserJson user) {
    final String newDescription = "some desc 1";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password()).submit(new MainPage())
        .editSpending("Some category").setNewSpendingDescription(newDescription).save();

    new MainPage().checkThatTableContainsSpending(newDescription);
  }

  @User
  @Test
  void userShouldAddNewSpending(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password()).submit(new MainPage())
        .getHeader().addSpendingPage().addAmount("100").addCategoryName("Category name")
        .addDate(new Date()).addDescription("Description").save();

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
  @Test
  void checkStatComponentTest(UserJson user)
      throws InterruptedException {
    StatComponent statComponent = Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
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
  @Test
  void checkStatComponentAnyOrderTest(UserJson user)
      throws InterruptedException {
    StatComponent statComponent = Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
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
  @Test
  void checkContainsStatComponentTest(UserJson user)
      throws InterruptedException {
    StatComponent statComponent = Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getStatComponent();

    Thread.sleep(3000);

    Bubble bubbleStudy = new Bubble(Color.yellow, "Обучение 79990 ₽");

    statComponent.checkStatBubblesContains(bubbleStudy);
  }

  @User(
      spendings = @Spending(
          category = "Обучение",
          description = "Обучение Advanced 2.0",
          amount = 79990
      )
  )
  @Test
  @ScreenShotTest(value = "img/clear-stat.png",
      rewriteExpected = true)
  void deleteSpendingTest(@Nonnull UserJson user, BufferedImage clearStat) throws IOException {
    Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getSpendingTable()
        .deleteSpending("Обучение Advanced 2.0")
        .checkTableSize(0);
  }
}


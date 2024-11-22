package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.StatComponent;
import javax.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import org.junit.jupiter.api.Test;
import utils.ScreenDiffResult;

import static org.junit.jupiter.api.Assertions.assertFalse;

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
      spendings = @Spending(
          category = "Обучение",
          description = "Обучение Advanced 2.0",
          amount = 79990
      )
  )
  @ScreenShotTest("img/expected-stat.png")
  void checkStatComponentTest(UserJson user, BufferedImage expectedStat, BufferedImage expected) throws IOException, InterruptedException {
    StatComponent statComponent = Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getStatComponent();

    Thread.sleep(3000);

    assertFalse(new ScreenDiffResult(
        expected,
        statComponent.chartScreenshot()
    ), "Screen comparison failure");

    statComponent.checkBubbles(Color.yellow);
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


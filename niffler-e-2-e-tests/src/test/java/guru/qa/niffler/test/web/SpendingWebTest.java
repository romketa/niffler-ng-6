package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import java.util.Date;
import org.junit.jupiter.api.Test;

@WebTest
public class SpendingWebTest {

  private static final Config CFG = Config.getInstance();

  @Test
  @User
  void newSpendingAlertTest(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .addSpendingPage()
        .addAmount("100")
        .addCategoryName("Category name")
        .addDate(new Date())
        .addDescription("Description")
        .save();

    new MainPage().checkThatTableContainsSpending("Category name")
        .verifyAlertCreatedSpending();
  }

  @User(
      spendings = @Spending(
          category = "Some category",
          description = "Keep",
          amount = 990
      )
  )
  @Test
  void categoryDescriptionShouldBeChangedFromTable(UserJson user) {
    final String newDescription = "some desc 1";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .editSpending("Some category")
        .setNewSpendingDescription(newDescription)
        .save();

    new MainPage().checkThatTableContainsSpending(newDescription);
  }

  @User
  @Test
  void userShouldAddNewSpending(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .getHeader()
        .addSpendingPage()
        .addAmount("100")
        .addCategoryName("Category name")
        .addDate(new Date())
        .addDescription("Description")
        .save();

    new MainPage().checkThatTableContainsSpending("Category name");
  }
}


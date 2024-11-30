package guru.qa.niffler.test.web;

import static utils.RandomDataUtils.randomUsername;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Browser;
import guru.qa.niffler.jupiter.BrowserArgumentConverter;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;


@WebTest
public class LoginTest {

  private static final Config CFG = Config.getInstance();

  @User
  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .checkThatPageLoaded();
  }

  @Test
  void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .fillLoginPage(randomUsername(), "BAD")
        .submit(new LoginPage())
        .checkError("Неверные учетные данные пользователя");
  }


}

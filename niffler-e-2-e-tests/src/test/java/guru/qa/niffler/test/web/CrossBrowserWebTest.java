package guru.qa.niffler.test.web;

import static utils.RandomDataUtils.randomUsername;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.data.Browser;
import guru.qa.niffler.jupiter.BrowserArgumentConverter;
import guru.qa.niffler.jupiter.annotation.CrossBrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;

@CrossBrowserExtension
public class CrossBrowserWebTest {

  @ParameterizedTest
  @EnumSource(Browser.class)
  void successLoginTest(@ConvertWith(BrowserArgumentConverter.class) SelenideDriver driver) {
    driver.open(LoginPage.URL);
    new LoginPage(driver)
        .fillLoginPage(randomUsername(), "password")
        .submit(new LoginPage(driver));
  }
}

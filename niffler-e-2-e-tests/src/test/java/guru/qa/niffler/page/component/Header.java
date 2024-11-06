package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.ProfilePage;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class Header extends BaseComponent<Header> {

  private final SelenideElement menu = $("button[aria-label='Menu']");
  private final SelenideElement newSpending = self.find("a[href='/spending']");
  private final SelenideElement toMainPage = self.find("a[href='/main']");
  private final ElementsCollection menuItem = self.findAll("li[role='menuitem']");

  public Header() {
    super($("#root header"));
  }


  public void checkHeaderText() {
    self.$("h1").shouldHave(text("Niffler"));
  }

  @Nonnull
  public FriendsPage toFriendsPage() {
    menu.click();
    menuItem.get(1).click();
    return new FriendsPage();
  }

  @Nonnull
  public PeoplePage toAllPeoplesPage() {
    menu.click();
    menuItem.get(2).click();
    return new PeoplePage();
  }

  @Nonnull
  public ProfilePage toProfilePage() {
    menu.click();
    menuItem.first().click();
    return new ProfilePage();
  }

  @Nonnull
  public LoginPage signOut() {
    menu.click();
    menuItem.get(3).click();
    return new LoginPage();
  }

  @Nonnull
  public EditSpendingPage addSpendingPage() {
    newSpending.click();
    return new EditSpendingPage();
  }

  @Nonnull
  public MainPage toMainPage() {
    toMainPage.click();
    return new MainPage();
  }
}

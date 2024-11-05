package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

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
public class Header {

  private final SelenideElement self = $("#root header");
  private final SelenideElement menu = $("button[aria-label='Menu']");
  private static final String NEW_SPENDING_LOC = "a[href='/spending']";
  private static final String TO_MAIN_PAGE_LOC = "a[href='/main']";
  private static final String MENU_ITEM_LOC = "li[role='menuitem']";

  public void checkHeaderText() {
    self.$("h1").shouldHave(text("Niffler"));
  }

  @Nonnull
  public FriendsPage toFriendsPage() {
    menu.click();
    self.$$(MENU_ITEM_LOC).get(1).click();
    return new FriendsPage();
  }

  @Nonnull
  public PeoplePage toAllPeoplesPage() {
    menu.click();
    self.$$(MENU_ITEM_LOC).get(2).click();
    return new PeoplePage();
  }

  @Nonnull
  public ProfilePage toProfilePage() {
    menu.click();
    self.$$(MENU_ITEM_LOC).first().click();
    return new ProfilePage();
  }

  @Nonnull
  public LoginPage signOut() {
    menu.click();
    self.$$(MENU_ITEM_LOC).get(3).click();
    return new LoginPage();
  }

  @Nonnull
  public EditSpendingPage addSpendingPage() {
    self.$(NEW_SPENDING_LOC).click();
    return new EditSpendingPage();
  }

  @Nonnull
  public MainPage toMainPage() {
    self.$(TO_MAIN_PAGE_LOC).click();
    return new MainPage();
  }
}

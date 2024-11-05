package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ProfilePage {

  private static final String SHOW_ARCHIVED_LOC = "//span[contains(text(), 'archived')]/preceding-sibling::span";
  private static final String CATEGORY_LOC = "//div[span[contains(text(), '%s')]]";
  private static final String SEND_CATEGORY_TO_ARCHIVE_BTN_LOC = "button[aria-label='Archive category']";
  private final SelenideElement nameEl = $("#name");
  private final SelenideElement saveBtn = $("button[type='submit']");



  public void verifyThatCategoryDisplayed(String categoryName) {
    SelenideElement category = $x(String.format(CATEGORY_LOC, categoryName));
    category.shouldBe(visible);;
  }

  @Nonnull
  public ProfilePage showArchivedCategories() {
    $x(SHOW_ARCHIVED_LOC).click();
    return this;
  }

  @Nonnull
  public ProfilePage setName(String name) {
    nameEl.setValue(name);
    saveBtn.click();
    return this;
  }

  public void checkName(String name) {
    nameEl.shouldBe(exactText(name));
  }
}

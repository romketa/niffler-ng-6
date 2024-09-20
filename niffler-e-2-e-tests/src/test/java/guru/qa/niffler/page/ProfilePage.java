package guru.qa.niffler.page;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;

public class ProfilePage {

  private static final String SHOW_ARCHIVED_LOC = "//span[contains(text(), 'archived')]/preceding-sibling::span";
  private static final String CATEGORY_LOC = "//div[span[contains(text(), '%s')]]";
  private static final String SEND_CATEGORY_TO_ARCHIVE_BTN_LOC = "button[aria-label='Archive category']";


  public void verifyThatActiveCategoryWasAdded(String categoryName) {
    SelenideElement category = $x(String.format(CATEGORY_LOC, categoryName));
    category.shouldBe(visible);
    category.sibling(0).$(SEND_CATEGORY_TO_ARCHIVE_BTN_LOC)
        .shouldBe(attribute("aria-label", "Archive category"));
  }

  public void verifyThatArchivedCategoryWasAdded(String categoryName) {
    $x(SHOW_ARCHIVED_LOC).click();
    SelenideElement category = $x(String.format(CATEGORY_LOC, categoryName));
    category.shouldBe(visible);
    category.sibling(0).shouldBe(attribute("aria-label", "Unarchive category"));
  }
}

package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$;

public class EditSpendingPage {

  private static final String DESC_INPUT_LOC = "#description";
  private static final String SAVE_BTN_LOC = "#save";

  public EditSpendingPage setNewSpendingDescription(String description) {
    $(DESC_INPUT_LOC).clear();
    $(DESC_INPUT_LOC).setValue(description);
    return this;
  }

  public void save() {
    $(SAVE_BTN_LOC).click();
  }
}

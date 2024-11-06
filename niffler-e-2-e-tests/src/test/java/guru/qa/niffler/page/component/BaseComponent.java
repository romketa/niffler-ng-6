package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

public class BaseComponent<T extends BaseComponent<?>> {


  protected SelenideElement self;

  public BaseComponent(SelenideElement self) {
    this.self = self;
  }
}

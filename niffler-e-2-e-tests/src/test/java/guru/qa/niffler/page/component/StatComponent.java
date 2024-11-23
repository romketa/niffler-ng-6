package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.StatConditions;
import guru.qa.niffler.condition.Bubble;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.StatConditions.statBubbles;
import static java.util.Objects.requireNonNull;

public class StatComponent extends BaseComponent<StatComponent> {
  public StatComponent() {
    super($("#stat"));
  }

  private final ElementsCollection bubbles = self.$("#legend-container").$$("li");
  private final SelenideElement chart = $("canvas[role='img']");

  @Step("Get screenshot of stat chart")
  @Nonnull
  public BufferedImage chartScreenshot() throws IOException {
    return ImageIO.read(requireNonNull(chart.screenshot()));
  }

  @Step("Check that stat bubbles have fixed Bubbles {expectedBubbles}")
  @Nonnull
  public StatComponent checkFixedStatBubbles(Bubble... expectedBubbles) {
    bubbles.should(StatConditions.statBubbles(expectedBubbles));
    return this;
  }

  @Step("Check that stat bubbles have Bubbles {expectedBubbles} in any order")
  @Nonnull
  public StatComponent checkStatBubblesAnyOrder(Bubble... expectedBubbles) {
    bubbles.should(StatConditions.statBubblesInAnyOrder(expectedBubbles));
    return this;
  }

  @Step("Check that stat bubble(s) contains Bubbles {expectedBubbles}")
  @Nonnull
  public StatComponent checkStatBubblesContains(Bubble... expectedBubbles) {
    bubbles.should(StatConditions.statBubblesContains(expectedBubbles));
    return this;
  }
}

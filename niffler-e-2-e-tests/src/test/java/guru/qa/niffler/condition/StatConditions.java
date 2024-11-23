package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

@ParametersAreNonnullByDefault
public class StatConditions {

  @Nonnull
  public static WebElementCondition statBubbles(Color expectedColor) {
    return new WebElementCondition("color " + expectedColor.rgb) {
      @NotNull
      @Override
      public CheckResult check(Driver driver, WebElement element) {
        final String rgba = element.getCssValue("background-color");
        return new CheckResult(
            expectedColor.rgb.equals(rgba),
            rgba
        );
      }
    };
  }

  public record Bubble(Color color, String text) {}

  @Nonnull
  public static WebElementsCondition statBubbles(@Nonnull Bubble... expectedBubbles) {
    return new WebElementsCondition() {

      private final String expectedRgba = Arrays.stream(expectedBubbles).map(c -> c.color.rgb).toList().toString();
      private final String expectedText = Arrays.stream(expectedBubbles).map(c -> c.text).toList().toString();

      @NotNull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (ArrayUtils.isEmpty(expectedBubbles)) {
          throw new IllegalArgumentException("No expected colors given");
        }
        if (expectedBubbles.length != elements.size()) {
          final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedBubbles.length, elements.size());
          return rejected(message, elements);
        }

        boolean passed = true;
        final Map<String, String> actualBubbleList = new HashMap<>();
        for (int i = 0; i < elements.size(); i++) {
          final WebElement elementToCheck = elements.get(i);
          final Color colorToCheck = expectedBubbles[i].color;
          final String textToCheck = expectedBubbles[i].text;
          final String rgba = elementToCheck.getCssValue("background-color");
          final String text = elementToCheck.getText();
          actualBubbleList.put(rgba, text);
          if (passed) {
            passed = colorToCheck.rgb.equals(rgba) & textToCheck.equals(text);
          }
        }

        if (!passed) {
          final String actualRgba = actualBubbleList.keySet().toString();
          final String actualText = actualBubbleList.values().toString();
          final String message = String.format(
              "List Bubbles mismatch (expected: %s %s, actual: %s %s)", expectedRgba, expectedText, actualRgba, actualText
          );
          return rejected(message, actualRgba);
        }
        return accepted();
      }

      @Override
      public String toString() {
        return expectedRgba;
      }
    };
  }

  public static WebElementsCondition statBubblesInAnyOrder(@Nonnull Bubble... expectedBubbles) {
    return new WebElementsCondition() {

      private final String expectedValues = Arrays.stream(expectedBubbles).map(c -> c.color.rgb + " " + c.text).toList().toString();

      @NotNull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (ArrayUtils.isEmpty(expectedBubbles)) {
          throw new IllegalArgumentException("No expected colors given");
        }
        if (expectedBubbles.length != elements.size()) {
          final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedBubbles.length, elements.size());
          return rejected(message, elements);
        }

        boolean passed = true;

        final Map<String, String> actualBubbleList = new HashMap<>();
        final List<Bubble> expectedBubbleList = Arrays.asList(expectedBubbles);
        final Map<String, Bubble> expectedBubbleMap = new HashMap<>();
        expectedBubbleList.forEach(bubble -> expectedBubbleMap.put(bubble.color.rgb, bubble));

        for (WebElement elementToCheck : elements) {
          final String rgba = elementToCheck.getCssValue("background-color");
          final String text = elementToCheck.getText();
          final Color colorToCheck = expectedBubbleMap.get(rgba).color;
          final String textToCheck = expectedBubbleMap.get(rgba).text;
          actualBubbleList.put(rgba, text);
          if (passed) {
            passed = colorToCheck.rgb.equals(rgba) & textToCheck.equals(text);
          }
        }

        if (!passed) {
          final String actualValues = actualBubbleList.keySet().toString();
          final String message = String.format(
              "List Bubbles mismatch (expected: %s , actual: %s )", expectedValues, actualValues
          );
          return rejected(message, actualValues);
        }
        return accepted();
      }

      @Override
      public String toString() {
        return expectedValues;
      }
    };
  }

  public static WebElementsCondition statBubblesContains(@Nonnull Bubble... expectedBubbles) {
    return new WebElementsCondition() {

      private final String expectedValues = Arrays.stream(expectedBubbles).map(c -> c.color.rgb + " " + c.text).toList().toString();

      @NotNull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (ArrayUtils.isEmpty(expectedBubbles)) {
          throw new IllegalArgumentException("No expected colors given");
        }

        boolean passed = true;
        final Map<String, String> actualBubbleList = new HashMap<>();

        for (Bubble bubble : expectedBubbles) {
          final WebElement elToCheck = elements.stream()
              .filter(el -> el.getCssValue("background-color").equals(bubble.color.rgb))
              .findFirst()
              .orElseThrow(() -> new IllegalArgumentException("Unknown color"));
          final Color colorToCheck = bubble.color;
          final String textToCheck = bubble.text;
          final String rgba = elToCheck.getCssValue("background-color");
          final String text = elToCheck.getText();
          actualBubbleList.put(rgba, text);

          if (passed) {
            passed = colorToCheck.rgb.equals(rgba) & textToCheck.equals(text);
          }
        }

        if (!passed) {
          final String actualValues = actualBubbleList.keySet().toString();
          final String message = String.format(
              "List Bubbles mismatch (expected: %s , actual: %s )", expectedValues, actualValues
          );
          return rejected(message, actualValues);
        }
        return accepted();
      }

      @Override
      public String toString() {
        return expectedValues;
      }
    };
  }
}

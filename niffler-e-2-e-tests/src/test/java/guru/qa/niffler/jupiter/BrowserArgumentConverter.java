package guru.qa.niffler.jupiter;

import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.data.Browser;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import utils.SelenideUtils;

public class BrowserArgumentConverter implements ArgumentConverter {

  @Override
  public Object convert(Object source, ParameterContext context)
      throws ArgumentConversionException {
    if (source instanceof Browser browser) {
      SelenideConfig selenideConfig = switch (browser) {
        case CHROME -> SelenideUtils.chromeConfig;
        case FIREFOX -> SelenideUtils.firefoxConfig;
      };
      return new SelenideDriver(selenideConfig);
    }
    return new ArgumentConversionException("Cannot convert source: " + source);
  }
}

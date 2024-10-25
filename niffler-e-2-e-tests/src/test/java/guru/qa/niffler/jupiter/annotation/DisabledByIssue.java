package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.IssueExtension;
import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DisabledByIssue {
  String category() default "";

  String description();

  double amount();

  CurrencyValues currency() default CurrencyValues.RUB;
}

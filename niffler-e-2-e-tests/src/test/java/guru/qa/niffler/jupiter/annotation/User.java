package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.SpendingExtension;
import guru.qa.niffler.jupiter.extension.UserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({UserExtension.class, CategoryExtension.class, SpendingExtension.class
})
public @interface User {

  String username() default "";

  Category[] categories() default {};

  Spending[] spendings() default {};
}

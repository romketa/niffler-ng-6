package utils;

import com.github.javafaker.Faker;

import javax.annotation.Nonnull;

public class RandomDataUtils {

  private static final Faker fakeData = new Faker();

  @Nonnull
  public static String randomUsername() {
    return fakeData.pokemon().name();
  }

  @Nonnull
  public static String randomName() {
    return fakeData.name().firstName();
  }

  @Nonnull
  public static String randomSurname() {
    return fakeData.name().lastName();
  }

  @Nonnull
  public static String randomCategoryName() {
    return fakeData.animal().name();
  }

  @Nonnull
  public static String randomSentence(int wordsCount) {
    return fakeData.lorem().sentence(wordsCount);
  }
}

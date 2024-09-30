package utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {

  private static final Faker fakeData = new Faker();

  public static String randomUsername() {
    return fakeData.pokemon().name();
  }

  public static String randomName() {
    return fakeData.name().firstName();
  }

  public static String randomSurname() {
    return fakeData.name().lastName();
  }

  public static String randomCategoryName() {
    return fakeData.beer().name();
  }

  public static String randomSentence(int wordsCount) {
    return fakeData.lorem().sentence(wordsCount);
  }
}

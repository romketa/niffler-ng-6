package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String ghUrl();

  String frontUrl();

  String spendUrl();

  String ghUrl();
  
  String registerUrl();

  String profileUrl();
}

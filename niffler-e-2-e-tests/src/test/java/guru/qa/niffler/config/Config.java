package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String frontUrl();

  String spendUrl();

  String ghUrl();
  
  String registerUrl();

  String profileUrl();

  String friendsUrl();

  String authUrl();

  String authJdbcUrl();

  String gatewayUrl();

  String userdataUrl();

  String userdataJdbcUrl();

  String spendJdbcUrl();

  String currencyJdbcUrl();
}

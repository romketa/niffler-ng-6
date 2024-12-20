package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.api.core.CodeInterceptor;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import lombok.SneakyThrows;
import utils.OAuthUtils;


public class AuthRestClient extends RestClient {

  private static final Config CFG = Config.getInstance();
  private final AuthApiClient authApi = new AuthApiClient();

  public AuthRestClient() {
    super(CFG.authUrl(), true, new CodeInterceptor());
  }

  @SneakyThrows
  public String login(String username, String password) {
    final String codeVerifier = OAuthUtils.generateCodeVerifier();
    final String codeChallenge = OAuthUtils.generateCodeChallenge(codeVerifier);
    authApi.authorize(codeChallenge);
    authApi.login(username, password);
    return authApi.token(ApiLoginExtension.getCode(), codeVerifier);
  }
}

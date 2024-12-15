package guru.qa.niffler.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class AuthApiClient extends RestClient {

  private final AuthApi authApi;
  private final static String CODE = "code";
  private final static String CLIENT_ID = "client";
  private final static String SCOPE = "openid";
  private final static String CODE_CHALLENGE_METHOD = "S256";
  private final static String GRANT_TYPE = "authorization_code";
  private final static Config CONFIG = Config.getInstance();

  public AuthApiClient() {
    super(CFG.authUrl());
    this.authApi = retrofit.create(AuthApi.class);
  }

  @Step("Request registration form")
  public void requestRegisterForm() {
    final Response<Void> response;
    try {
      response = authApi.requestRegisterForm()
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
  }

  @Step("Authorize")
  public void authorize(String codeChallenge) {
    final Response<ResponseBody> response;
    try {
      response = authApi.authorize(CODE, CLIENT_ID, SCOPE,
          CONFIG.authorizedUrl(), codeChallenge,
          CODE_CHALLENGE_METHOD).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
  }

  @Step("Login")
  public void login(String username, String password) {
    final Response<Void> response;
    try {
      response = authApi.login(username, password,
              ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN"))
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
  }

  @Step("Get token")
  public String token(String codeChallenge, String codeVerifier) {
    final Response<JsonNode> response;
    try {
      response = authApi.token(codeChallenge, CODE_CHALLENGE_METHOD, codeVerifier,
          GRANT_TYPE, CLIENT_ID).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body().get("id_token").toString();
  }
}

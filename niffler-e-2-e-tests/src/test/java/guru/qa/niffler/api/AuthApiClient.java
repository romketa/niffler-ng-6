package guru.qa.niffler.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.core.CodeInterceptor;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;
import java.io.IOException;
import retrofit2.Response;

public class AuthApiClient extends RestClient {

  private final AuthApi authApi;
  private final static String CODE = "code";
  private final static String CLIENT_ID = "client";
  private final static String SCOPE = "openid";
  private final static String CODE_CHALLENGE_METHOD = "S256";
  private final static String GRANT_TYPE = "authorization_code";
  private final static Config CONFIG = Config.getInstance();
  private final static String REDIRECT_URL = CFG.frontUrl() + "authorized";

  public AuthApiClient() {
    super(CFG.authUrl(), true, new CodeInterceptor());
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

  @Step("Register")
  public void register(String username, String password, String passwordSubmit, String csrf) {
    final Response<Void> response;
    try {
      response = authApi.register(username, password, passwordSubmit, csrf)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(201, response.code());
  }

  @Step("Authorize")
  public void authorize(String codeChallenge) {
    final Response<Void> response;
    try {
      response = authApi.authorize(
          CODE,
          CLIENT_ID,
          SCOPE,
          REDIRECT_URL,
          codeChallenge,
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
      response = authApi.token(
          codeChallenge,
          REDIRECT_URL,
          CLIENT_ID,
          codeVerifier,
          GRANT_TYPE).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body().get("id_token").toString();
  }
}

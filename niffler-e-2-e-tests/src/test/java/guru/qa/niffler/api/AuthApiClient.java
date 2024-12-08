package guru.qa.niffler.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import io.qameta.allure.Step;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class AuthApiClient extends RestClient {

  private final AuthApi authApi;

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
      response = authApi.authorize("code", "client", "openid",
          "http://127.0.0.1:3000/authorized", codeChallenge,
          "S256").execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
  }

  @Step("Login")
  public void login() {
    final Response<Void> response;
    try {
      response = authApi.login("moon", "moon123",
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
      response = authApi.token(codeChallenge, "http://127.0.0.1:3000/authorized", codeVerifier,
          "authorization_code", "client").execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body().get("id_token").toString();
  }
}

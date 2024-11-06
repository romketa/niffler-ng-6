package guru.qa.niffler.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nonnull;
import retrofit2.Call;
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


  @Step("User registration")
  public void register(@Nonnull String username, @Nonnull String password, @Nonnull String passwordSubmit, @Nonnull String csrf) {
    final Response<Void> response;
    try {
      response = authApi.register(username, password, passwordSubmit, csrf)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(201, response.code());
  }
}

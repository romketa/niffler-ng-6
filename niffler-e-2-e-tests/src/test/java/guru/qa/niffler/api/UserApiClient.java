package guru.qa.niffler.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@ParametersAreNonnullByDefault
public class UserApiClient extends RestClient {
  private final UserApi usersApi;

  public UserApiClient() {
    super(CFG.userdataUrl());
    this.usersApi = retrofit.create(UserApi.class);
  }

  @Nullable
  public UserJson currentUser(String username) {
    final Response<UserJson> response;
    try {
      response = usersApi.currentUser(username)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Nullable
  public List<UserJson> getAllUsers(String username, String searchQuery) {
    final Response<List<UserJson>> response;
    try {
      response = usersApi.allUsers(username, searchQuery)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Nullable
  public UserJson updateUser(UserJson user) {
    final Response<UserJson> response;
    try {
      response = usersApi.updateUserInfo(user)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Nullable
  public UserJson sendInvitation(String username, String targetUsername) {
    final Response<UserJson> response;
    try {
      response = usersApi.sendInvitation(username, targetUsername)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Nullable
  public UserJson acceptInvitation(String username, String targetUsername) {
    final Response<UserJson> response;
    try {
      response = usersApi.acceptInvitation(username, targetUsername)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Nullable
  public UserJson declineInvitation(String username, String targetUsername) {
    final Response<UserJson> response;
    try {
      response = usersApi.declineInvitation(username, targetUsername)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Nullable
  public List<UserJson> getFriends(String username, String searchQuery) {
    final Response<List<UserJson>> response;
    try {
      response = usersApi.friends(username, searchQuery)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public void removeFriend(String username, String targetUsername) {
    final Response<Void> response;
    try {
      response = usersApi.removeFriend(username, targetUsername)
          .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
  }


}

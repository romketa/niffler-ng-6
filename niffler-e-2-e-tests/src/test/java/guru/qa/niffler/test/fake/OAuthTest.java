package guru.qa.niffler.test.fake;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import guru.qa.niffler.api.AuthApiClient;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;
import utils.OAuthUtils;

public class OAuthTest {

  private AuthApiClient apiClient = new AuthApiClient();

  @Test
  void oauthTest() throws IOException, NoSuchAlgorithmException {
    String codeVerifier = OAuthUtils.generateCodeVerifier();
    String codeChallenge = OAuthUtils.generateCodeChallenge(codeVerifier);

    apiClient.authorize(codeChallenge);
    apiClient.login("moon", "moon123");
    String token = apiClient.token(codeChallenge, codeVerifier);
    assertNotNull(token);
  }
}

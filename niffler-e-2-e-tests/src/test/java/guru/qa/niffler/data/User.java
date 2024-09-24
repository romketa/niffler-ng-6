package guru.qa.niffler.data;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {

  String username;
  String password;

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  public String toString() {
    return username;
  }
}

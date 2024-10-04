package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.CategoryJson;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;

@Getter
@Setter
public class AuthUserEntity implements Serializable {

  private UUID id;
  private String username;
  private String password;
  private boolean enabled;
  private boolean accountNonExpired;
  private boolean accountNonLocked;
  private boolean credentialsNonExpired;

}

package guru.qa.niffler.service;

import static guru.qa.niffler.data.Databases.xaTransaction;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases.XaFunction;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.spend.UserEntity;
import guru.qa.niffler.model.UserJson;
import java.util.Arrays;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserDbClient {

  private static final Config CFG = Config.getInstance();

  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public UserJson createUser(UserJson user) {
    return UserJson.fromEntity(
        xaTransaction(
            new XaFunction<>(connection -> {
              AuthUserEntity aue = new AuthUserEntity();
              aue.setUsername(user.username());
              aue.setPassword(pe.encode("12345"));
              aue.setEnabled(true);
              aue.setAccountNonLocked(true);
              aue.setAccountNonExpired(true);
              aue.setCredentialsNonExpired(true);
              aue = new AuthUserDaoJdbc(connection).create(aue);
              AuthUserEntity finalAue = aue;
              new AuthAuthorityDaoJdbc(connection).create(
                  Arrays.stream(Authority.values())
                      .map(a -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setUserId(finalAue.getId());
                        ae.setAuthority(a);
                        return ae;
                      }).toArray(AuthorityEntity[]::new));
              return null;
            }, CFG.authJdbcUrl()
            ),
            new XaFunction<>(connection -> {
              UserEntity ue = new UserEntity();
              ue.setUsername(user.username());
              ue.setFullname(user.fullname());
              ue.setCurrency(user.currency());
              ue.setSurname(user.surname());
              ue.setFirstname(user.firstname());
              return new UserDaoJdbc(connection).createUser(ue);
            }, CFG.userdataJdbcUrl())
        )
    );
  }
}

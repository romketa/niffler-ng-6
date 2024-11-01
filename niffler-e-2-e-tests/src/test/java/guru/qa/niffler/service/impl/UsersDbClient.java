package guru.qa.niffler.service.impl;

import static utils.RandomDataUtils.randomUsername;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import utils.RandomDataUtils;

@ParametersAreNonnullByDefault
public class UsersDbClient implements UsersClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
  private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
  private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();

  private final XaTransactionTemplate xaJdbcTxTemplate = new XaTransactionTemplate(
      CFG.userdataJdbcUrl(), CFG.authJdbcUrl());


  @Override
  @Nonnull
  public UserJson createUser(String username, String password) {
    return Objects.requireNonNull(xaJdbcTxTemplate.execute(
        () -> UserJson.fromEntity(createNewUser(username, password), null)));
  }

  @Override
  @Nonnull
  public List<UserJson> createIncomeInvitations(UserJson targetUser, int count) {
    List<UserJson> income = new ArrayList<>();
    if (count > 0) {
      UserEntity targetEntity = userdataUserRepository.findById(targetUser.id()).orElseThrow();

      for (int i = 0; i < count; i++) {
        xaJdbcTxTemplate.execute(() -> {
          final String username = randomUsername();
          final UserEntity user = createNewUser(username, "12345");
          userdataUserRepository.sendInvitation(
              user,
              targetEntity
          );
          income.add(UserJson.fromEntity(user, null));
          return null;
        });
      }
    }
    return income;
  }

  @Override
  @Nonnull
  public List<UserJson> createOutcomeInvitations(UserJson targetUser, int count) {
    List<UserJson> outcome = new ArrayList<>();
    if (count > 0) {
      UserEntity targetEntity = userdataUserRepository.findById(targetUser.id()).orElseThrow();

      for (int i = 0; i < count; i++) {
        xaJdbcTxTemplate.execute(() -> {
          final String username = randomUsername();
          final UserEntity user = createNewUser(username, "12345");
          userdataUserRepository.sendInvitation(
              targetEntity,
              user
          );
          outcome.add(UserJson.fromEntity(user, null));
          return null;
        });
      }
    }
    return outcome;
  }

  @Override
  @Nonnull
  public List<UserJson> createFriends(UserJson targetUser, int count) {
    List<UserJson> friends = new ArrayList<>();
    if (count > 0) {
      UserEntity targetEntity = userdataUserRepository.findById(targetUser.id()).orElseThrow();

      for (int i = 0; i < count; i++) {
        xaJdbcTxTemplate.execute(() -> {
          final String username = randomUsername();
          final UserEntity user = createNewUser(username, "12345");
          userdataUserRepository.addFriend(
              targetEntity,
              user
          );
          friends.add(UserJson.fromEntity(user, null));
          return null;
        });
      }
    }
    return friends;
  }

  private UserEntity createNewUser(String username, String password) {
    AuthUserEntity authUser = authUserEntity(username, password);
    authUserRepository.create(authUser);
    return userdataUserRepository.create(userEntity(username));
  }

  private UserEntity userEntity(String username) {
    UserEntity ue = new UserEntity();
    ue.setUsername(username);
    ue.setCurrency(CurrencyValues.RUB);
    return ue;
  }

  private AuthUserEntity authUserEntity(String username, String password) {
    AuthUserEntity authUser = new AuthUserEntity();
    authUser.setUsername(username);
    authUser.setPassword(pe.encode(password));
    authUser.setEnabled(true);
    authUser.setAccountNonExpired(true);
    authUser.setAccountNonLocked(true);
    authUser.setCredentialsNonExpired(true);
    authUser.setAuthorities(Arrays.stream(Authority.values()).map(e -> {
      AuthorityEntity ae = new AuthorityEntity();
      ae.setUser(authUser);
      ae.setAuthority(e);
      return ae;
    }).toList());
    return authUser;
  }

}

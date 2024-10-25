package guru.qa.niffler.service;

import static guru.qa.niffler.data.jdbc.DataSources.dataSource;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import java.util.Arrays;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;
import utils.RandomDataUtils;

public class OldUserDbClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  //  Spring JDBC DAOs
  private final AuthUserDao authUserDaoSpringJdbc = new AuthUserDaoSpringJdbc();
  private final AuthAuthorityDao authAuthorityDaoSpringJdbc = new AuthAuthorityDaoSpringJdbc();
  private final UserDao userDaoSpringJdbc = new UserDaoSpringJdbc();
  private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();

  //  JDBC DAOs
  private final AuthUserDao authUserDaoJdbc = new AuthUserDaoJdbc();
  private final AuthAuthorityDao authAuthorityDaoJdbc = new AuthAuthorityDaoJdbc();
  private final UserDao userDaoJdbc = new UserDaoJdbc();
  private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryJdbc();

  private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
      CFG.userdataJdbcUrl()
  );

  private final XaTransactionTemplate xaJdbcTxTemplate = new XaTransactionTemplate(
      CFG.userdataJdbcUrl(),
      CFG.authJdbcUrl()
  );

  TransactionTemplate txTemplate = new TransactionTemplate(
      new ChainedTransactionManager(
          new JdbcTransactionManager(dataSource(CFG.authJdbcUrl())),
          new JdbcTransactionManager(dataSource(CFG.userdataJdbcUrl()))
      )
  );

  public UserJson createUserChainedTx(UserJson user) {
    return txTemplate.execute(status -> {

      AuthUserEntity authUser = new AuthUserEntity();
      authUser.setUsername(user.username());
      authUser.setPassword(pe.encode("12345"));
      authUser.setEnabled(true);
      authUser.setAccountNonExpired(true);
      authUser.setAccountNonLocked(true);
      authUser.setCredentialsNonExpired(true);

      AuthUserEntity createdAuthUser = authUserDaoSpringJdbc.create(authUser);

      AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
          e -> {
            AuthorityEntity ae = new AuthorityEntity();
            ae.setUser(createdAuthUser);
            ae.setAuthority(e);
            return ae;
          }
      ).toArray(AuthorityEntity[]::new);

      authAuthorityDaoSpringJdbc.create(authorityEntities);

      UserEntity createdUser = UserEntity.fromJson(user);
      return UserJson.fromEntity(userDaoSpringJdbc.createUser(createdUser), null);
    });
  }


  //  Spring JDBC transactions
  public UserJson createUserSpringJdbcTx(String username, String password) {
    return xaJdbcTxTemplate.execute(() -> {
      AuthUserEntity authUser = new AuthUserEntity();
      authUser.setUsername(username);
      authUser.setPassword(pe.encode(password));
      authUser.setEnabled(true);
      authUser.setAccountNonExpired(true);
      authUser.setAccountNonLocked(true);
      authUser.setCredentialsNonExpired(true);

      AuthUserEntity createdAuthUser = authUserDaoSpringJdbc.create(authUser);

      AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
          e -> {
            AuthorityEntity ae = new AuthorityEntity();
            ae.setUser(createdAuthUser);
            ae.setAuthority(e);
            return ae;
          }
      ).toArray(AuthorityEntity[]::new);

      authAuthorityDaoSpringJdbc.create(authorityEntities);

      return UserJson.fromEntity(
          userDaoSpringJdbc.createUser(
              userEntity(username)
          ), null);
    });
  }

  //  Spring JDBC without transactions
  public UserJson createUserSpringJdbcWithoutTx(UserJson user) {

    AuthUserEntity authUser = new AuthUserEntity();
    authUser.setUsername(user.username());
    authUser.setPassword(pe.encode("12345"));
    authUser.setEnabled(true);
    authUser.setAccountNonExpired(true);
    authUser.setAccountNonLocked(true);
    authUser.setCredentialsNonExpired(true);

    AuthUserEntity createdAuthUser = authUserDaoSpringJdbc.create(authUser);

    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
        e -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setUser(createdAuthUser);
          ae.setAuthority(e);
          return ae;
        }
    ).toArray(AuthorityEntity[]::new);

    authAuthorityDaoSpringJdbc.create(authorityEntities);

    return UserJson.fromEntity(
        userDaoSpringJdbc.createUser(
            UserEntity.fromJson(user)
        ), null
    );
  }

  // JDBC transactions
  public UserJson createUserJdbcTx(UserJson user) {
    return xaJdbcTxTemplate.execute(() -> {
      AuthUserEntity authUser = new AuthUserEntity();
      authUser.setUsername(user.username());
      authUser.setPassword(pe.encode("12345"));
      authUser.setEnabled(true);
      authUser.setAccountNonExpired(true);
      authUser.setAccountNonLocked(true);
      authUser.setCredentialsNonExpired(true);

      AuthUserEntity createdAuthUser = authUserDaoJdbc.create(authUser);

      AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
          e -> {
            AuthorityEntity ae = new AuthorityEntity();
            ae.setUser(createdAuthUser);
            ae.setAuthority(e);
            return ae;
          }
      ).toArray(AuthorityEntity[]::new);

      authAuthorityDaoJdbc.create(authorityEntities);

      return UserJson.fromEntity(
          userDaoJdbc.createUser(
              UserEntity.fromJson(user)
          ), null
      );
    });
  }

  // JDBC without transactions
  public UserJson createUserJdbcWithoutTx(UserJson user) {

    AuthUserEntity authUser = new AuthUserEntity();
    authUser.setUsername(user.username());
    authUser.setPassword(pe.encode("12345"));
    authUser.setEnabled(true);
    authUser.setAccountNonExpired(true);
    authUser.setAccountNonLocked(true);
    authUser.setCredentialsNonExpired(true);

    AuthUserEntity createdAuthUser = authUserDaoJdbc.create(authUser);

    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
        e -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setUser(createdAuthUser);
          ae.setAuthority(e);
          return ae;
        }
    ).toArray(AuthorityEntity[]::new);

    authAuthorityDaoJdbc.create(authorityEntities);

    return UserJson.fromEntity(
        userDaoJdbc.createUser(
            UserEntity.fromJson(user)
        ), null
    );
  }

  @SuppressWarnings("unchecked")
  public void addFriends(UserJson userRequester, UserJson userAddressee) {
    xaJdbcTxTemplate.execute(() -> {
      userdataUserRepository.addFriend(UserEntity.fromJson(userRequester),
          UserEntity.fromJson(userAddressee));
      return null;
    });
  }

  @SuppressWarnings("unchecked")
  public void addInvitation(UserJson userRequester, UserJson userAddressee) {
    xaJdbcTxTemplate.execute(() -> {
      userdataUserRepository.sendInvitation(UserEntity.fromJson(userRequester),
          UserEntity.fromJson(userAddressee));
      return null;
    });
  }

  public void addIncomeInvitation(UserJson targetUser, int count) {
    if (count > 0) {
      UserEntity targetEntity = userdataUserRepository.findById(
          targetUser.id()
      ).orElseThrow();

      for (int i = 0; i < count; i++) {
        xaJdbcTxTemplate.execute(() -> {
              String username = RandomDataUtils.randomUsername();
              AuthUserEntity authUser = authUserEntity(username, "12345");
              authUserRepository.create(authUser);
              UserEntity adressee = userdataUserRepository.create(userEntity(username));
              userdataUserRepository.sendInvitation(targetEntity, adressee);
              return null;
            }
        );
      }
    }
  }

  public void addOutcomeInvitation(UserJson targetUser, int count) {
    if (count > 0) {
      UserEntity targetEntity = userdataUserRepository.findById(
          targetUser.id()
      ).orElseThrow();

      for (int i = 0; i < count; i++) {
        xaJdbcTxTemplate.execute(() -> {
              String username = RandomDataUtils.randomUsername();
              AuthUserEntity authUser = authUserEntity(username, "12345");
              authUserRepository.create(authUser);
              UserEntity adressee = userdataUserRepository.create(userEntity(username));
              userdataUserRepository.sendInvitation(adressee, targetEntity);
              return null;
            }
        );
      }
    }
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
    authUser.setAuthorities(
        Arrays.stream(Authority.values()).map(
            e -> {
              AuthorityEntity ae = new AuthorityEntity();
              ae.setUser(authUser);
              ae.setAuthority(e);
              return ae;
            }
        ).toList()
    );
    return authUser;
  }
}

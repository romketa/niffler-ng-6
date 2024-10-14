package guru.qa.niffler.service;

import static guru.qa.niffler.data.tpl.DataSources.dataSource;

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
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import java.util.Arrays;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

public class UserDbClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  //  Spring JDBC DAOs
  private final AuthUserDao authUserDaoSpringJdbc = new AuthUserDaoSpringJdbc();
  private final AuthAuthorityDao authAuthorityDaoSpringJdbc = new AuthAuthorityDaoSpringJdbc();
  private final UserDao userDaoSpringJdbc = new UserDaoSpringJdbc();

  //  JDBC DAOs
  private final AuthUserDao authUserDaoJdbc = new AuthUserDaoJdbc();
  private final AuthAuthorityDao authAuthorityDaoJdbc = new AuthAuthorityDaoJdbc();
  private final UserDao userDaoJdbc = new UserDaoJdbc();

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
            ae.setUserId(createdAuthUser.getId());
            ae.setAuthority(e);
            return ae;
          }
      ).toArray(AuthorityEntity[]::new);

      authAuthorityDaoSpringJdbc.create(authorityEntities);

      UserEntity createdUser = UserEntity.fromJson(user);
      createdUser.setUsername(null);

      return UserJson.fromEntity(userDaoSpringJdbc.createUser(createdUser), null);
    });
  }


  //  Spring JDBC transactions
  public UserJson createUserSpringJdbcTx(UserJson user) {
    return xaJdbcTxTemplate.execute(() -> {
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
            ae.setUserId(createdAuthUser.getId());
            ae.setAuthority(e);
            return ae;
          }
      ).toArray(AuthorityEntity[]::new);

      authAuthorityDaoSpringJdbc.create(authorityEntities);

      return UserJson.fromEntity(
          userDaoSpringJdbc.createUser(
              UserEntity.fromJson(user)
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
          ae.setUserId(createdAuthUser.getId());
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
            ae.setUserId(createdAuthUser.getId());
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
          ae.setUserId(createdAuthUser.getId());
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
}

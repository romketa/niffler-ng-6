package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.UserEntity;
<<<<<<<< HEAD:niffler-e-2-e-tests/src/test/java/guru/qa/niffler/data/dao/UserDao.java
========

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
>>>>>>>> bdb30cb (7.2 framework di (#51)):niffler-e-2-e-tests/src/test/java/guru/qa/niffler/data/dao/UserdataUserDao.java
import java.util.List;
import java.util.Optional;
import java.util.UUID;

<<<<<<<< HEAD:niffler-e-2-e-tests/src/test/java/guru/qa/niffler/data/dao/UserDao.java
public interface UserDao {

  UserEntity createUser(UserEntity user);
========
@ParametersAreNonnullByDefault
public interface UserdataUserDao {

  @Nonnull
  UserEntity create(UserEntity user);

  @Nonnull
  UserEntity update(UserEntity user);
>>>>>>>> bdb30cb (7.2 framework di (#51)):niffler-e-2-e-tests/src/test/java/guru/qa/niffler/data/dao/UserdataUserDao.java

  @Nonnull
  Optional<UserEntity> findById(UUID id);

  @Nonnull
  Optional<UserEntity> findByUsername(String username);

<<<<<<<< HEAD:niffler-e-2-e-tests/src/test/java/guru/qa/niffler/data/dao/UserDao.java
  void delete(UserEntity user);

========
  @Nonnull
>>>>>>>> bdb30cb (7.2 framework di (#51)):niffler-e-2-e-tests/src/test/java/guru/qa/niffler/data/dao/UserdataUserDao.java
  List<UserEntity> findAll();

  UserEntity update(UserEntity user);
}

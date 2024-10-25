package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;

import guru.qa.niffler.data.repository.AuthUserRepository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jdbc.Connections.holder;

public class AuthUserRepositoryJdbc implements AuthUserRepository {

  private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
  private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc();

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    authUserDao.create(user);
    authAuthorityDao.create(user.getAuthorities().toArray(new AuthorityEntity[0]));
    return user;
  }

  @Override
  public AuthUserEntity update(AuthUserEntity user) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    Optional<AuthUserEntity> userEntity = authUserDao.findById(id);
    userEntity
        .ifPresent(authUserEntity ->
            authUserEntity.addAuthorities(
                authAuthorityDao.findAllByUserId(authUserEntity.getId()).toArray(new AuthorityEntity[0])
            )
        );
    return userEntity;
  }

  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    Optional<AuthUserEntity> userEntity = authUserDao.findByUsername(username);
    userEntity.ifPresent(authUserEntity ->
        authUserEntity.addAuthorities(
            authAuthorityDao.findAllByUserId(authUserEntity.getId()).toArray(new AuthorityEntity[0])
        )
    );
    return userEntity;
  }

  @Override
  public void remove(AuthUserEntity user) {
    throw new UnsupportedOperationException();
  }
}

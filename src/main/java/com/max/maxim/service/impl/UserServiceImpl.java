package com.max.maxim.service.impl;

import com.max.maxim.bean.condition.UserUpdateCondition;
import com.max.maxim.bean.dto.UserUpdateDto;
import com.max.maxim.bean.vo.DummyEntity;
import com.max.maxim.bean.vo.ResultEntity;
import com.max.maxim.bean.vo.UserEntity;
import com.max.maxim.dao.UserDao;
import com.max.maxim.enums.ResultEnum;
import com.max.maxim.enums.user.RoleEnum;
import com.max.maxim.service.UserService;
import com.max.maxim.util.ResultUtil;
import jakarta.annotation.Resource;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

  @Resource
  private UserDao userDao;

  @Resource
  private StringEncryptor encryptor;

  @Override
  public void insertOne(UserEntity user) {
    user.setUserId(UUID.randomUUID().toString().substring(0, 20));
    user.setPassword(encryptor.encrypt(user.getPassword()));
    SimpleGrantedAuthority sga = new SimpleGrantedAuthority(RoleEnum.USER.name());
    Set<GrantedAuthority> gas = Set.of(sga);
    user.setAuthorities(gas);
    userDao.insertOne(user);
    log.info("A new user {} is successfully inserted into db.", user);
  }

  @Override
  public ResultEntity<DummyEntity> update(UserUpdateDto user) {
    ResultEntity<DummyEntity> resultEntity = this.validateUser(user);
    if (!ObjectUtils.nullSafeEquals(resultEntity.getCode(), ResultEnum.SUCCESS.getCode())) {
      return resultEntity;
    }

    userDao.updateOne(this.populateToCondition(user));
    log.info("update user[{}] password success.", user.getUsername());
    return ResultUtil.success();

  }

  private UserUpdateCondition populateToCondition(UserUpdateDto user) {
    return UserUpdateCondition.builder().username(user.getUsername())
        .newPassword(encryptor.encrypt(user.getNewPassword())).build();
  }

  @Override
  public void deleteOne(String userId) {

  }

  @Override
  public UserEntity queryOne(String userId) {
    return null;
  }

  @Override
  public UserEntity loadUserByUsername(String username) {
    UserEntity userEntity = userDao.loadUserByUsername(username);
    if (ObjectUtils.isEmpty(userEntity)) {
      log.warn("no such user [{}] exists.", username);
      return null;
    }
    return userEntity;
  }

  private <T> ResultEntity<T> validateUser(UserUpdateDto user) {
    String username = user.getUsername();
    UserEntity dbUser = this.loadUserByUsername(username);
    if (ObjectUtils.isEmpty(dbUser)) {
      log.error("username not found in db.");
      return ResultUtil.error(ResultEnum.USER_NOT_FOUND_IN_DB);
    }
    String dbPasswd = dbUser.getPassword();
    String oldPassword = user.getOldPassword();
    if (ObjectUtils.isEmpty(oldPassword)) {
      log.warn("input password is null.");
      return ResultUtil.error(ResultEnum.PASSWORD_IS_NULL);
    }
    dbPasswd = encryptor.decrypt(dbPasswd);
    if (!ObjectUtils.nullSafeEquals(dbPasswd, oldPassword)) {
      log.warn("input old password not match db password.");
      return ResultUtil.error(ResultEnum.PASSWORD_NOT_MATCHED);
    }
    log.info("verification ok, userUpdateDto:[{}]", user);
    return ResultUtil.success();
  }
}

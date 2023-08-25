package com.max.maxim.service.impl;

import com.max.maxim.bean.vo.UserEntity;
import com.max.maxim.dao.UserDao;
import com.max.maxim.enums.user.RoleEnum;
import com.max.maxim.service.UserService;
import jakarta.annotation.Resource;
import java.util.List;
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
    List<GrantedAuthority> gas = List.of(sga);
    user.setAuthorities(gas);
    userDao.insertOne(user);
    log.info("A new user {} is successfully inserted into db.", user);
  }

  @Override
  public void update(UserEntity user) {

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
}

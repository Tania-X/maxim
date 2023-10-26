package com.max.maxim.dao;

import com.max.maxim.bean.condition.UserUpdateCondition;
import com.max.maxim.bean.vo.UserEntity;
import org.apache.ibatis.annotations.Param;

public interface UserDao {

  UserEntity loadUserByUsername(@Param("username") String username);

  void insertOne(UserEntity user);

  void updateOne(UserUpdateCondition user);
}

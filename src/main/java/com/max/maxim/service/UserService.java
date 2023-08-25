package com.max.maxim.service;

import com.max.maxim.bean.vo.UserEntity;

public interface UserService {

  /**
   * insert a new user into db
   *
   * @param user user
   */
  void insertOne(UserEntity user);

  /**
   * update an existing user into db
   *
   * @param user user
   */
  void update(UserEntity user);

  /**
   * delete an existing user from db by user ID
   *
   * @param userId the unique marker for a user in db
   */
  void deleteOne(String userId);

  /**
   * query an existing user from db by user ID
   *
   * @param userId the unique marker for a user in db
   * @return the queried user if any
   */
  UserEntity queryOne(String userId);

  /**
   * query a user from db by username, integrated by Spring Security for user authentication
   *
   * @param username username input by a user
   * @return a user entity if any
   */
  UserEntity loadUserByUsername(String username);

}

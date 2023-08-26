package com.max.maxim.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthoritiesStringConvertHandler extends BaseTypeHandler<Collection<GrantedAuthority>> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Collection<GrantedAuthority> parameter,
      JdbcType jdbcType) throws SQLException {
    ps.setString(i, listToAuthoritiesConverter(parameter));
  }

  private String listToAuthoritiesConverter(Collection<GrantedAuthority> parameters) {
    final String DELIMITER = ",";
    StringBuilder res = new StringBuilder();
    Iterator<GrantedAuthority> iterator = parameters.iterator();
    while (iterator.hasNext()) {
      res.append(iterator.next().getAuthority());
      if (iterator.hasNext()) {
        res.append(DELIMITER);
      }
    }
    return res.toString();
  }

  @Override
  public Collection<GrantedAuthority> getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String res = rs.getString(columnName);
    return authoritiesToListConverter(res);
  }

  @Override
  public Collection<GrantedAuthority> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String res = rs.getString(columnIndex);
    return authoritiesToListConverter(res);
  }

  @Override
  public Collection<GrantedAuthority> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String res = cs.getString(columnIndex);
    return authoritiesToListConverter(res);
  }

  private Collection<GrantedAuthority> authoritiesToListConverter(String res) {
    final String DELIMITER = ",";
    Collection<GrantedAuthority> gas = new HashSet<>();
    String[] resList = res.split(DELIMITER);
    for (String ga : resList) {
      gas.add(new SimpleGrantedAuthority(ga));
    }
    return gas;
  }
}

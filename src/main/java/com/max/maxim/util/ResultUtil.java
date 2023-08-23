package com.max.maxim.util;

import com.max.maxim.bean.vo.ResultEntity;

public class ResultUtil {

  public static <T> ResultEntity<T> success() {
    return ResultEntity.<T>builder().code(200).build();
  }

  public static <T> ResultEntity<T> error() {
    return ResultEntity.<T>builder().code(500).build();
  }

  public static <T> ResultEntity<T> success(T data) {
    ResultEntity<T> successResult = success();
    successResult.setData(data);
    return successResult;
  }

  public static <T> ResultEntity<T> error(Integer code, String message) {
    ResultEntity<T> errorResult = error();
    errorResult.setCode(code);
    errorResult.setMessage(message);
    return errorResult;
  }

}

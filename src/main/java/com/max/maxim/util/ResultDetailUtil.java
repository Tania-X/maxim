package com.max.maxim.util;

import lombok.Builder;
import lombok.Data;

public class ResultDetailUtil {

  @Data
  @Builder
  private static class Detail {

    /**
     * detail content
     */
    private String detail;

    /**
     * result code for frontend
     */
    private Integer code;
    /**
     * detail description
     */
    private String desc;
  }

  public static Detail of(String detail, String desc, Integer code) {
    return Detail.builder().detail(detail).desc(desc).code(code).build();
  }

}

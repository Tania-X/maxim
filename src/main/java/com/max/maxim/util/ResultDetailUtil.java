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
     * detail description
     */
    private String desc;
  }

  public static Detail of(String detail, String desc) {
    return Detail.builder().detail(detail).desc(desc).build();
  }

}

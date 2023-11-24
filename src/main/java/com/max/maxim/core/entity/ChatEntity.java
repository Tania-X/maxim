package com.max.maxim.core.entity;

import com.max.maxim.core.enums.MsgType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ChatEntity {

  private MsgType msgType;

  private Object message;

  private LocalDateTime sendDatetime;

}

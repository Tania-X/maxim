package com.max.maxim.core.client.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.StandardCharsets;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientSimpleDialHandler extends ChannelInboundHandlerAdapter {

  private final ObjectMapper objectMapper = new ObjectMapper();


  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
//    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//    Assert.notNull(requestAttributes, "no user exists currently.");
//    HttpServletRequest request = requestAttributes.getRequest();
//    HttpSession session = request.getSession();
//    String username = (String) session.getAttribute(MaximConstant.SESSION_USERNAME);
//    log.info("user [{}] logs in.", username);
    String username = "max";
    log.info("user [{}] logs in.", username);
//    byte[] bytes = objectMapper.writeValueAsBytes(Identifier.builder().username(username).build());
//    ByteBuf buf = Unpooled.copiedBuffer(bytes);
//    ctx.writeAndFlush(buf);
    super.channelActive(ctx);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf buf = (ByteBuf) msg;
    byte[] bytes = new byte[buf.readableBytes()];
    buf.readBytes(bytes);
    String result = new String(bytes, StandardCharsets.UTF_8);
    System.out.println("result = " + result);
    super.channelRead(ctx, msg);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    super.channelReadComplete(ctx);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    super.exceptionCaught(ctx, cause);
  }
}

package com.max.maxim.core.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.max.maxim.core.client.handler.ClientSimpleDialHandler;
import com.max.maxim.service.UserService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import jakarta.annotation.Resource;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerSimpleDialHandler extends ChannelInboundHandlerAdapter {

  @Resource
  private UserService userService;

  // todo 默认ip与用户名的一致性。后续可以添加一张ip-username的动态表，记录实时的对应关系
  private final ConcurrentMap<String, Channel> ip2clientChannelMap = new ConcurrentHashMap<>();

  private final ConcurrentMap<String, Channel> username2clientChannelMap = new ConcurrentHashMap<>();

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    InetSocketAddress clientAddress = (InetSocketAddress) ctx.channel().remoteAddress();
    String clientHost = clientAddress.getAddress().getHostAddress();
    int clientPort = clientAddress.getPort();
    log.info("a new client[host: {}, port: {}] joined the net.", clientHost, clientPort);
    // todo 暂时不对clientHost进行“混淆”，方便调试
    ip2clientChannelMap.put(clientHost, ctx.channel());
    super.channelActive(ctx);
//    ctx.channel()
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    System.out.println("channel read");
    // todo 使用objectMapper将msg解析成为SimpleDialEntity后决定将内容发给谁
    // todo 先保存到redis，监听心跳，若在线则直接发送
    // todo 持久化到数据库，标记是否为已发送，否则等用户登录时发送
//    if (msg instanceof Identifier identifier) {
//      String username = identifier.getUsername();
//      username2clientChannelMap.put(username, ctx.channel());
//      log.info("a user [{}] send a message for identification.", username);
//      return;
//    }
    ByteBuf buf = (ByteBuf) msg;
    byte[] req = new byte[buf.readableBytes()];
    buf.readBytes(req);
//    Identifier identifier = objectMapper.readValue(req, ClientSimpleDialHandler.Identifier.class);
//    String username = identifier.getUsername();
//
//    ctx.write(resp);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ctx.close();
  }
}

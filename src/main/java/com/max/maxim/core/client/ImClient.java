package com.max.maxim.core.client;

import com.max.maxim.core.client.handler.ClientSimpleDialHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class ImClient {

  public void connect(String host, int port) throws Exception {

    // config thread group of the client port
    EventLoopGroup group = new NioEventLoopGroup();
    Bootstrap b = new Bootstrap();
    b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(
        new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
//            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
//            ch.pipeline().addLast(new StringDecoder());
            ch.pipeline().addLast(new ClientSimpleDialHandler());
          }
        });

    ChannelFuture future = b.connect(host, port).sync();
    future.channel().closeFuture().sync();

  }
}

package com.max.maxim.core.server;

import com.max.maxim.core.server.handler.ServerSimpleDialHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class ImServer {

  public void bind(String host, int port) throws Exception {
    // config thread groups of the server port
    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, 1024)
          .childHandler(new ChildChannelHandler());
      // bind the port and wait for sync
      ChannelFuture future = b.bind(host, port).sync();
      future.channel().closeFuture().sync();
    } finally {
      // gracefully exit and release thread resource
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }


  }

  private static class ChildChannelHandler extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
      ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
      ch.pipeline().addLast(new StringDecoder());
      ch.pipeline().addLast(new ServerSimpleDialHandler());
    }
  }
}

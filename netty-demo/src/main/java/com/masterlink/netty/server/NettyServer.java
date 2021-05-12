package com.masterlink.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.SimpleTimeZone;

/**
 * @author Klein
 * @Classname NettyServer
 * @Description TODO
 * @Date 2021-05-11 19:08
 * @Created by Klein
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //创建一个NIOEventLoopGroup的两个实例对象
        //他们就是两个线程池 默认的线程数就是CPU核心数*2
       EventLoopGroup bossGroup= new NioEventLoopGroup();  //接收请求
       EventLoopGroup workerGroup=new NioEventLoopGroup();  //处理请求

        //2，创建服务启动辅助类  装配一些使用的组件
        ServerBootstrap bootstrap=new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup)
                //指定服务器端接收套接字通道NioServerSocketChannel
                .channel(NioServerSocketChannel.class)
                //设置业务职责链路 也就是一个个channelHandler组成进行处理
                .childHandler(new ChannelInitializer<NioSocketChannel>(){

                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                          ChannelPipeline pipeline= channel.pipeline();
                            //将一个一个的channelhandler添加到责任链上，在请求进来或者响应出去的时候
                            // 都会经过链上channelHandler进行处理
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new SimpleChannelInboundHandler<String>(){

                            //真正的数据逻辑处理
                            @Override
                            protected void channelRead0(ChannelHandlerContext chContext, String msg) throws Exception {
                                System.out.println(msg);
                            }
                        });

                    }
                });

                //bind监听端口
                //sync: 用于阻塞当前的thread，一直到端口绑定操作完成
                ChannelFuture channelFuture=bootstrap.bind(8000).sync();

                System.out.println("tcp server start success...");

                //应用程序会阻塞登到知道服务的channel关闭
                channelFuture.channel().closeFuture().sync();

    }
}

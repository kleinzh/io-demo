package com.masterlink.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Date;

/**
 * @author Klein
 * @Classname NettyClient
 * @Description TODO
 * @Date 2021-05-12 14:35
 * @Created by Klein
 */
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {

        //1.创建NioEventLoopGroup得实例对象
        EventLoopGroup group=new NioEventLoopGroup();

        //2.创建bootstrap对象
        Bootstrap bootstrap=new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new StringEncoder());
                    }
                });

        Channel channel = bootstrap.connect("127.0.0.1", 8000).channel();

        while (true) {
            channel.writeAndFlush(new Date()+": Hello");
            Thread.sleep(2000);
        }


    }
}

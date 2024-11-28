package cn.ether.im.push.server;

import cn.ether.im.push.ImPushServer;
import cn.ether.im.push.server.codec.ImProtocEncoder;
import cn.ether.im.push.server.codec.WebSocketMessageDecoder;
import cn.ether.im.push.server.codec.WebSocketMessageEncoder;
import cn.ether.im.push.server.handler.HandshakeCompleteChannelHandler;
import cn.ether.im.push.server.handler.ImChannelHandler;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.util.Properties;

import static cn.ether.im.common.constants.ImConstants.READER_IDLE_TIME_SEC;

/**
 * * @Author: Martin
 * * @Date    2024/9/3 14:28
 * * @Description
 **/
@Slf4j
@Component
@ConditionalOnProperty(prefix = "websocket", value = "enable", havingValue = "true", matchIfMissing = true)
public class WebSocketImServer implements ImPushServer {

    private boolean ready = false;

    @Value("${websocket.port}")
    private int port;

    @Value("${websocket.name}")
    private String name;

    private NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);

    private NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;


    /**
     * @return
     */
    @Override
    public boolean ready() {
        return ready;
    }

    /**
     *
     */
    @Override
    public void start() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 256)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true) // 禁用nagle算法,避免小数据被缓存
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    /**
                     * This method will be called once the {@link Channel} was registered. After the method returns this instance
                     * will be removed from the {@link ChannelPipeline} of the {@link Channel}.
                     *
                     * @param ch the {@link Channel} which was registered.
                     * @throws Exception is thrown if an error occurs. In that case it will be handled by
                     *                   {@link #exceptionCaught(ChannelHandlerContext, Throwable)} which will by default close
                     *                   the {@link Channel}.
                     */
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                // 读空闲时间为 4分钟，小于一般运营商的NAT超时时间。
                                .addLast(new IdleStateHandler(READER_IDLE_TIME_SEC, 0, 0))
                                .addLast("http-codec", new HttpServerCodec())
                                .addLast("aggregator", new HttpObjectAggregator(65535))
                                .addLast("http-chunked", new ChunkedWriteHandler())
                                .addLast(new WebSocketServerProtocolHandler("/im"))
                                .addLast(new WebSocketMessageDecoder())
                                .addLast(new ImProtocEncoder())
                                .addLast(new WebSocketMessageEncoder())
                                .addLast("im-identity", new HandshakeCompleteChannelHandler()) // 位置不能放在前面
                                .addLast(new ImChannelHandler());
                    }
                });

        registerNamingService(name, port);
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            this.ready = true;
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    log.info("WebSocketServer.start|websocket server 初始化完成,端口：{}", port);
                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将Netty服务注册进Nacos
     *
     * @param nettyName 服务名称
     * @param nettyPort 服务端口号
     */
    private void registerNamingService(String nettyName, Integer nettyPort) {
        try {
            Properties properties = new Properties();
            properties.setProperty(PropertyKeyConst.SERVER_ADDR, nacosDiscoveryProperties.getServerAddr());
            properties.setProperty(PropertyKeyConst.NAMESPACE, nacosDiscoveryProperties.getNamespace());
            NamingService namingService = NamingFactory.createNamingService(properties);
            InetAddress address = InetAddress.getLocalHost();
            namingService.registerInstance(nettyName, address.getHostAddress(), nettyPort);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     */
    @Override
    public void shutdown() {
        if (bossGroup != null && !bossGroup.isShuttingDown()) {
            bossGroup.shutdownGracefully();
        }

        if (workerGroup != null && !workerGroup.isShuttingDown()) {
            workerGroup.shutdownGracefully();
        }
    }
}

package cn.ether.im.push.server.handler;

import cn.ether.im.client.common.model.ImInfo;
import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.model.protoc.ImProtoType;
import cn.ether.im.common.model.protoc.ImProtoc;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.util.JwtUtils;
import cn.ether.im.common.util.SpringContextHolder;
import cn.ether.im.push.handler.ImUserContextHandler;
import cn.ether.im.push.util.ChannelHandlerContextUtil;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/21 21:26
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Slf4j
public class HandshakeCompleteChannelHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 传递给下一个handler
        ctx.fireChannelRead(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            HttpHeaders headers = handshakeComplete.requestHeaders();
            String token = headers.get(ImConstants.IM_TOKEN);
            if (StringUtils.isEmpty(token)) {
                ctx.channel().close();
            }
            boolean pass = checkToken(ctx, token);

            if (!pass) {
                ctx.writeAndFlush(new ImInfo("Token校验失败"));
                ctx.channel().close();
                return;
            }
            String protocType = headers.get("protoc_type");

            ImProtoc imProtoc = ImProtoc.defaultProtoc;
            if (StringUtils.isNotEmpty(protocType) && Objects.equals(ImProtoType.PROTOC_BUFFER.getCode(), Integer.parseInt(protocType))) {
                imProtoc = new ImProtoc(ImProtoType.PROTOC_BUFFER);
            }
            ChannelHandlerContextUtil.setAttr(ctx, "protoc", imProtoc);
            ctx.writeAndFlush(imProtoc);
        }
        super.userEventTriggered(ctx, evt);
    }


    private boolean checkToken(ChannelHandlerContext ctx, String token) {
        Boolean signed = JwtUtils.checkSign(token, ImConstants.TOKEN_SECRET);
        if (!signed) {
            log.info("Token is invalid，token：{}", token);
            return false;
        }
        ImUserTerminal imUserTerminal = JSON.parseObject(JwtUtils.getInfo(token), ImUserTerminal.class);
        if (imUserTerminal == null) {
            return false;
        }
        ImUserContextHandler userHandler = SpringContextHolder.getBean(ImUserContextHandler.class);
        userHandler.onUserLogin(imUserTerminal, ctx);
        return true;
    }


}

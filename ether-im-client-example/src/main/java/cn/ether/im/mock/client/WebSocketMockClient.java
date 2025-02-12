package cn.ether.im.mock.client;

import cn.ether.im.client.common.enums.ImInfoType;
import cn.ether.im.client.common.model.ImInfo;
import cn.ether.im.client.common.proto.ImProtoDecoder;
import cn.ether.im.client.common.proto.ImProtoEncoder;
import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.model.info.ImHeartbeatInfo;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImGroupMessage;
import cn.ether.im.common.model.message.ImMessageReceived;
import cn.ether.im.common.model.message.ImSingleMessage;
import cn.ether.im.common.model.protoc.ImProtoType;
import cn.ether.im.common.util.JwtUtils;
import cn.ether.im.common.util.ThreadPoolUtils;
import cn.ether.im.mock.user.MockUser;
import cn.ether.im.proto.binary.ImBinary;
import cn.ether.im.proto.text.ImTextProto;
import com.alibaba.fastjson.JSON;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/27 12:50
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Slf4j
public class WebSocketMockClient extends WebSocketClient {

    private MockUser mockUser;

    private ImProtoType protocType = ImProtoType.JSON;

    public WebSocketMockClient(MockUser mockUser, URI serverUri) {
        this(serverUri);
        this.mockUser = mockUser;
        String jsonString = JSON.toJSONString(mockUser);
        String token = JwtUtils.sign(mockUser.getUserId(), jsonString, 3600 * 24 * 7, ImConstants.TOKEN_SECRET);
        this.addHeader("token", token);
        this.addHeader("protoc_type", String.valueOf(protocType.getCode()));
    }

    public WebSocketMockClient(MockUser mockUser, URI serverUri, ImProtoType protocType) {
        this(serverUri);
        this.mockUser = mockUser;
        this.protocType = protocType;
        String jsonString = JSON.toJSONString(mockUser);
        String token = JwtUtils.sign(mockUser.getUserId(), jsonString, 3600 * 24 * 7, ImConstants.TOKEN_SECRET);
        log.info("【{}】token：{}", mockUser, token);
        this.addHeader("token", token);
        this.addHeader("protoc_type", String.valueOf(protocType.getCode()));
    }

    public WebSocketMockClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        log.info("【{}】已连接", mockUser);
    }

    @Override
    public void onMessage(String message) {
        log.info("【{}】收到文本消息:{}", mockUser, message);
        if (protocType.name().equals(message)) {
            return;
        }
        ImTextProto textProto = JSON.parseObject(message, ImTextProto.class);
        ImInfo imInfo = ImProtoDecoder.decodeToImInfo(textProto);

        processMessage(imInfo);
    }

    private void processMessage(ImInfo imInfo) {
        if (imInfo.getType() == ImInfoType.HEART_BEAT) {
            log.info("【{}】发送心跳", mockUser);
            sendMessage(new ImInfo<>(ImInfoType.HEART_BEAT, new ImHeartbeatInfo()));
        } else if (imInfo.getType() == ImInfoType.SINGLE) {
            ImSingleMessage info = (ImSingleMessage) imInfo.getInfo();
            sendMessageReceivedNotice(info);
        } else if (imInfo.getType() == ImInfoType.GROUP) {
            ImGroupMessage info = (ImGroupMessage) imInfo.getInfo();
            sendMessageReceivedNotice(info);
        }
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        byte[] array = bytes.array();
        try {
            ImInfo imInfo = ImProtoDecoder.decodeToImInfo(array);
            log.info("【{}】解析二进制消息:{}", mockUser, JSON.toJSONString(imInfo));
            processMessage(imInfo);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(String text) {
        boolean open = myReconnect();
        if (open) {
            super.send(text);
        }
    }

    /**
     * 在连接断开时尝试重连
     *
     * @return
     */
    public boolean myReconnect() {
        if (this.isOpen()) {
            return true;
        }
        try {
            Thread.sleep(3000);
            log.info("【{}】重连服务器...", mockUser);
            if (this.getReadyState().equals(ReadyState.NOT_YET_CONNECTED)) {
                super.connectBlocking();
                return true;
            } else if (this.getReadyState().equals(ReadyState.CLOSING)
                    || this.getReadyState().equals(ReadyState.CLOSED)) {
                try {
                    this.reconnectBlocking();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        } catch (Exception e) {
            log.error("reconnect error ", e);
        }
        log.info("【{}】重连服务器状态：{}", mockUser, this.isOpen());
        return this.isOpen();
    }

    private void sendMessageReceivedNotice(ImChatMessage imMessage) {
        ImMessageReceived imMessageReceived = new ImMessageReceived();
        imMessageReceived.setMessageId(imMessage.messageId());
        ImInfo message = new ImInfo<>(ImInfoType.MESSAGE_RECEIVED, imMessageReceived);
        sendMessage(message);
        log.info("【{}】已发送接受消息通知,MessageId:{}", mockUser, imMessage.messageId());
    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("【{}】onClose|{},code:{},reason:{}", mockUser, JSON.toJSONString(mockUser), code, reason);
        ThreadPoolUtils.execute(() -> {
            try {
                myReconnect();
            } catch (Exception e) {
                log.error("【{}】重连失败", mockUser, e);
            }
        });

    }

    @Override
    public void onError(Exception ex) {
        log.info("【{}】onError", mockUser, ex);
    }


    private void sendMessage(ImInfo message) {
        if (protocType == ImProtoType.JSON) {
            ImTextProto imTextProto = ImProtoEncoder.encodeToText(message);
            this.send(JSON.toJSONString(imTextProto));
        } else {
            ImBinary.ImBinaryProto imBinaryProto = ImProtoEncoder.encodeToBinary(message);
            this.send(imBinaryProto.toByteArray());
        }
    }
}

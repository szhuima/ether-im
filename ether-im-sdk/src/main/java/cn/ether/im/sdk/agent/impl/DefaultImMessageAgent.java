package cn.ether.im.sdk.agent.impl;

import cn.ether.im.common.cache.ImUserContextCache;
import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImMessageSendResult;
import cn.ether.im.common.enums.ImMessageType;
import cn.ether.im.common.model.info.ImTopicInfo;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImGroupMessage;
import cn.ether.im.common.model.message.ImSingleMessage;
import cn.ether.im.common.mq.ImMessageMQSender;
import cn.ether.im.sdk.agent.ImMessageAgent;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 15:23
 * * @Description
 **/
@Slf4j
@Component
public class DefaultImMessageAgent implements ImMessageAgent {

    @Resource
    private ImUserContextCache userContextCache;

    @Resource
    private ImMessageMQSender messageSender;

    @Override
    public ImMessageSendResult sendChatMessage(ImChatMessage chatMessage) {
        ImMessageType messageType = chatMessage.messageType();
        if (messageType == ImMessageType.SINGLE) {
            return sendSingleMessage(chatMessage);
        } else if (messageType == ImMessageType.GROUP) {
            return sendGroupMessage(chatMessage);
        } else {
            throw new RuntimeException("不支持的消息类型");
        }
    }


    private ImMessageSendResult sendSingleMessage(ImChatMessage chatMessage) {
        ImTopicInfo<ImChatMessage> topicInfo = new ImTopicInfo<>();
        topicInfo.setTopic(ImConstants.IM_CHAT_MESSAGE_TOPIC);
        String serverId = userContextCache.connectedServerId(chatMessage.getReceiverId());
        if (serverId == null) {
            return ImMessageSendResult.RECEIVER_NOT_ONLINE;
        }
        topicInfo.setTag(ImConstants.IM_CHAT_MESSAGE_TAG_PREFIX + serverId);
        topicInfo.setMessage(chatMessage);
        boolean ok = false;
        try {
            ok = messageSender.send(topicInfo);
            if (!ok) {
                return ImMessageSendResult.SENT_FAIL;
            }
        } catch (Exception e) {
            return ImMessageSendResult.SENT_FAIL;
        }
        return ImMessageSendResult.SENT;
    }

    @Override
    public ImMessageSendResult batchSendSingleMessage(List<ImSingleMessage> singleMessages) throws Exception {
        String receiverId = singleMessages.get(0).getReceiverId();
        List<String> serverIds = userContextCache.connectedServerIds(receiverId);
        if (serverIds.isEmpty()) {
            return ImMessageSendResult.RECEIVER_NOT_ONLINE;
        }
        List<ImTopicInfo> list = singleMessages.stream().map((message) -> {
            ImTopicInfo<ImSingleMessage> topicInfo = new ImTopicInfo<>();
            topicInfo.setTopic(ImConstants.IM_CHAT_MESSAGE_TOPIC);
            topicInfo.setTag(ImConstants.IM_CHAT_MESSAGE_TAG_PREFIX + serverIds.get(0));
            topicInfo.setMessage(message);
            return topicInfo;
        }).collect(Collectors.toList());
        return messageSender.batchSend(list) ? ImMessageSendResult.SENT : ImMessageSendResult.SENT_FAIL;
    }

    @SneakyThrows
    private ImMessageSendResult sendGroupMessage(ImChatMessage chatMessage) {
        List<String> receiverIds = chatMessage.getGroupReceiverIds();
        List<String> onlineUserIds = userContextCache.onlineUserIds(receiverIds);
        if (onlineUserIds.isEmpty()) {
            return ImMessageSendResult.RECEIVER_NOT_ONLINE;
        }
        List<ImTopicInfo> collect = receiverIds.stream().map((receiverId) -> {
            ImTopicInfo<ImChatMessage> topicInfo = new ImTopicInfo<>();
            topicInfo.setTopic(ImConstants.IM_GROUP_MESSAGE_TOPIC);
            List<String> serverIds = userContextCache.connectedServerIds(receiverId);
            if (serverIds.isEmpty()) {
                return null;
            }
            topicInfo.setTag(ImConstants.IM_CHAT_MESSAGE_TAG_PREFIX + serverIds.get(0));
            topicInfo.setMessage(chatMessage);
            return topicInfo;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        Map<String, List<ImTopicInfo>> groupedList = collect.stream().collect(Collectors.groupingBy(ImTopicInfo::getTag));

        List<ImTopicInfo> readyToSendList = new LinkedList<>();

        groupedList.forEach((k, v) -> {
            if (CollectionUtil.isNotEmpty(v)) {
                readyToSendList.add(v.get(0));
            }
        });
        if (CollectionUtil.isNotEmpty(readyToSendList)) {
            if (log.isDebugEnabled()) {
                log.debug("readyToSendList:{}", JSON.toJSONString(readyToSendList));
            }
            return messageSender.batchSend(readyToSendList) ? ImMessageSendResult.SENT : ImMessageSendResult.SENT_FAIL;
        }
        return ImMessageSendResult.SENT;
    }

    @Override
    public ImMessageSendResult batchSendGroupMessage(List<ImGroupMessage> groupMessages) throws Exception {
        return null;
    }

}

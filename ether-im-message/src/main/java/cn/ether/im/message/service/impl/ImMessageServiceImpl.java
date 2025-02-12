package cn.ether.im.message.service.impl;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImMessageContentType;
import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.model.info.ImTopicInfo;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImSingleMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.mq.ImMessageMQSender;
import cn.ether.im.common.util.SnowflakeUtil;
import cn.ether.im.message.mapper.ImSingleMessageETMapper;
import cn.ether.im.message.model.dto.MessageSendReq;
import cn.ether.im.message.model.entity.ImSingleMessageET;
import cn.ether.im.message.model.session.SessionContext;
import cn.ether.im.message.service.ImMessageService;
import cn.ether.im.sdk.agent.ImMessageAgent;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static cn.ether.im.common.enums.ImChatMessageStatus.*;

/**
 * @author jack
 * @createDate 2024-10-05 19:55:33
 */
@Slf4j
@Service
public class ImMessageServiceImpl extends ServiceImpl<ImSingleMessageETMapper, ImSingleMessageET>
        implements ImMessageService {

    @Resource
    private SnowflakeUtil snowflakeUtil;

    @Resource
    private ImMessageMQSender messageMQSender;

    @Resource
    private ImMessageAgent messageAgent;


    @Override
    public ImChatMessage convertSendReqToCoreModel(MessageSendReq sendReq) {
        ImUserTerminal loggedUser = SessionContext.loggedUser();
        Long messageId = snowflakeUtil.nextId();
        ImChatMessage chatMessage = new ImChatMessage();
        chatMessage.setMessageId(messageId);
        chatMessage.setContent(sendReq.getContent());
        chatMessage.setContentType(ImMessageContentType.valueOf(sendReq.getContentType()));
        chatMessage.setSenderId(loggedUser.getUserId());
        chatMessage.setSenderTerminal(loggedUser.getTerminalType());
        chatMessage.setReceiverId(sendReq.getReceiverId());
        chatMessage.setReceiverType(sendReq.getReceiverType());
        chatMessage.setSendTime(System.currentTimeMillis());
        return chatMessage;
    }

    @Override
    public ImSingleMessageET convertCoreModelToEntity(ImChatMessage chatMessage) {
        ImSingleMessageET singleMessageET = new ImSingleMessageET();
        BeanUtil.copyProperties(chatMessage, singleMessageET);
        singleMessageET.setStatus(INIT.name());
        singleMessageET.setCreateTime(new Date());
        return singleMessageET;
    }

    @Override
    @Transactional
    public boolean persistCoreModel(ImChatMessage chatMessage) {
        ImSingleMessageET singleMessageET = convertCoreModelToEntity(chatMessage);
        this.save(singleMessageET);
        return true;
    }

    @Override
    public ImSingleMessage convertEntityToCoreModel(ImSingleMessageET singleMessageET) {
        ImSingleMessage singleMessage = new ImSingleMessage();
        BeanUtil.copyProperties(singleMessageET, singleMessage);
        return singleMessage;
    }

    @Override
    public void sendMessage(ImChatMessage chatMessage) {
        ImTopicInfo topicInfo = new ImTopicInfo(chatMessage, ImConstants.IM_TX_MESSAGE_TOPIC);
        messageMQSender.sendMessageInTransaction(topicInfo, null);
    }

    @Override
    public void resendUnReceivedMessage(String userId, ImTerminalType terminalType) {
        List<ImSingleMessageET> list = this.lambdaQuery().eq(ImSingleMessageET::getReceiverId, userId)
                .in(ImSingleMessageET::getStatus, INIT, RECEIVER_NOT_ONLINE, SENT, SENT_FAIL)
                .list();

        List<ImSingleMessage> singleMessages = list.stream().map(this::convertEntityToCoreModel)
                .peek(singleMessage -> {
                    singleMessage.setLimitTerminals(Arrays.asList(new ImUserTerminal(userId, terminalType)));
                }).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(singleMessages)) {
            return;
        }
        try {
            messageAgent.batchSendSingleMessage(singleMessages);
        } catch (Exception e) {
            log.error("重发消息失败,userId:{}", userId, e);
        }

    }


    @Override
    public void sendMessageReadNotice(String messageId) {

    }

    @Override
    public void sendMessageWithDrawnNotice(String messageId) {

    }


}





package cn.ether.im.message.single.service.impl;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImMessageContentType;
import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.model.info.ImTopicInfo;
import cn.ether.im.common.model.message.ImSingleMessage;
import cn.ether.im.common.model.user.ImUserTerminal;
import cn.ether.im.common.mq.ImMessageMQSender;
import cn.ether.im.common.util.SnowflakeUtil;
import cn.ether.im.message.single.mapper.ImSingleMessageETMapper;
import cn.ether.im.message.single.model.dto.MessageSendReq;
import cn.ether.im.message.single.model.entity.ImSingleMessageET;
import cn.ether.im.message.single.model.session.SessionContext;
import cn.ether.im.message.single.service.ImMessageService;
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
 * @description 针对表【im_single_message(单聊信息表)】的数据库操作Service实现
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
    public ImSingleMessage convertSendReqToCoreModel(MessageSendReq sendReq) {
        ImUserTerminal loggedUser = SessionContext.loggedUser();
        Long messageId = snowflakeUtil.nextId();
        ImSingleMessage singleMessage = new ImSingleMessage();
        singleMessage.setMessageId(messageId);
        singleMessage.setContent(sendReq.getContent());
        singleMessage.setContentType(ImMessageContentType.valueOf(sendReq.getContentType()));
        singleMessage.setSenderId(loggedUser.getUserId());
        singleMessage.setSenderTerminal(loggedUser.getTerminalType());
        singleMessage.setReceiverId(sendReq.getReceiverId());
        singleMessage.setSendTime(System.currentTimeMillis());
        return singleMessage;
    }

    @Override
    public ImSingleMessageET convertCoreModelToEntity(ImSingleMessage singleMessage) {
        ImSingleMessageET singleMessageET = new ImSingleMessageET();
        BeanUtil.copyProperties(singleMessage, singleMessageET);
        singleMessageET.setStatus(INIT.name());
        singleMessageET.setCreateTime(new Date());
        return singleMessageET;
    }

    @Override
    @Transactional
    public boolean persistCoreModel(ImSingleMessage singleMessage) {
        ImSingleMessageET singleMessageET = convertCoreModelToEntity(singleMessage);
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
    public void sendSingleMessage(ImSingleMessage singleMessage) {
        ImTopicInfo topicInfo = new ImTopicInfo(singleMessage, ImConstants.IM_SINGLE_TX_MESSAGE_TOPIC);
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





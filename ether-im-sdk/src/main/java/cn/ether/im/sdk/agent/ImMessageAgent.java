package cn.ether.im.sdk.agent;

import cn.ether.im.common.enums.ImMessageSendResult;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImGroupMessage;
import cn.ether.im.common.model.message.ImSingleMessage;

import java.util.List;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 15:22
 * * @Description
 **/
public interface ImMessageAgent {

    /**
     * 发送对话消息
     * @param chatMessage
     * @return
     */
    ImMessageSendResult sendChatMessage(ImChatMessage chatMessage);

    /**
     * 批量发送单聊消息，接受人是同一个人
     *
     * @param singleMessages
     * @return
     */
    ImMessageSendResult batchSendSingleMessage(List<ImSingleMessage> singleMessages) throws Exception;


    /**
     * 批量发送群消息，接受人是同一个人
     *
     * @param groupMessages
     * @return
     */
    ImMessageSendResult batchSendGroupMessage(List<ImGroupMessage> groupMessages) throws Exception;
}

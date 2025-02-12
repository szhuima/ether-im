package cn.ether.im.message.service;

import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.common.model.message.ImSingleMessage;
import cn.ether.im.message.model.dto.MessageSendReq;
import cn.ether.im.message.model.entity.ImSingleMessageET;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author jack
 * @createDate 2024-10-05 19:55:33
 */
public interface ImMessageService extends IService<ImSingleMessageET> {

    /**
     * 将发送请求转为消息模型
     *
     * @param sendReq
     * @return
     */
    ImChatMessage convertSendReqToCoreModel(MessageSendReq sendReq);

    /**
     * 将单聊消息模型转为实体
     *
     * @param chatMessage
     * @return
     */
    ImSingleMessageET convertCoreModelToEntity(ImChatMessage chatMessage);

    /**
     * 持久化模型
     *
     * @param chatMessage
     */
    boolean persistCoreModel(ImChatMessage chatMessage);


    /**
     * 将实体转为模型
     *
     * @param singleMessageET
     * @return
     */
    ImSingleMessage convertEntityToCoreModel(ImSingleMessageET singleMessageET);

    /**
     * 发送消息
     *
     * @param chatMessage
     * @return
     * @throws Exception
     */
    void sendMessage(ImChatMessage chatMessage) throws Exception;


    /**
     * 重发收到的消息
     *
     * @param userId       接收用户ID
     * @param terminalType 接收终端类型
     */
    void resendUnReceivedMessage(String userId, ImTerminalType terminalType);

    /**
     * 发送消息已读通知
     *
     * @param messageId
     */
    void sendMessageReadNotice(String messageId);

    /**
     * 发送消息已撤回通知
     *
     * @param messageId
     */
    void sendMessageWithDrawnNotice(String messageId);


}

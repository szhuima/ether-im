package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.ImMessageContentType;
import cn.ether.im.common.enums.ImMessageType;
import cn.ether.im.common.enums.ImTerminalType;
import cn.ether.im.common.model.user.ImUserTerminal;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 对话消息模型，指代用户之间的对话消息
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/5 12:06
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Getter
@Setter
public class ImChatMessage implements ImMessageV2 {


    /**
     * 消息内容
     */
    protected String content;
    /**
     * 消息内容类型
     */
    protected ImMessageContentType contentType;
    /**
     * 发送时间戳
     */
    protected Long sendTime;
    /**
     * 消息ID
     */
    private Long messageId;
    /**
     * 发送者 ID
     */
    private String senderId;

    /**
     * 发送者终端类型
     */
    private ImTerminalType senderTerminal;

    /**
     * 接收者Id,可以是用户ID，也可以是群ID
     */
    private String receiverId;

    /**
     * 接受者类型 0-个人 1-群
     */
    private Integer receiverType;

    /**
     * 接受者ID集合，用于群消息
     */
    private List<String> groupReceiverIds;

    /**
     * 用于限制接受终端，如果为空，则不用限制。
     */
    private List<ImUserTerminal> limitTerminals;

    @Override
    public Long messageId() {
        return messageId;
    }

    @Override
    public ImMessageType messageType() {
        if (1 == receiverType) {
            return ImMessageType.GROUP;
        } else {
            return ImMessageType.SINGLE;
        }
    }
}

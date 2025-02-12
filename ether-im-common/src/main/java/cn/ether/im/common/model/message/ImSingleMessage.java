package cn.ether.im.common.model.message;

import cn.ether.im.common.enums.ImMessageType;
import lombok.Data;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/4 21:52
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ImSingleMessage extends ImChatMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Long messageId() {
        return this.getMessageId();
    }

    @Override
    public ImMessageType messageType() {
        return ImMessageType.SINGLE;
    }

}

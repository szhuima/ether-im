package cn.ether.im.message.transaction;

import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.message.model.entity.ImMessageEntity;
import cn.ether.im.message.service.ImMessageService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/10/5 12:31
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Slf4j
@Component
@RocketMQTransactionListener(rocketMQTemplateBeanName = "rocketMQTemplate")
public class ImMessageTransactionListener implements RocketMQLocalTransactionListener {

    @Resource
    private ImMessageService messageService;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String messageString = new String((byte[]) msg.getPayload());
        try {
            ImChatMessage chatMessage = JSON.parseObject(messageString, ImChatMessage.class);
            messageService.persistCoreModel(chatMessage);
        } catch (Exception e) {
            log.error("executeLocalTransaction|messageString:{}", messageString, e);
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        return RocketMQLocalTransactionState.COMMIT;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        String messageId = (String) msg.getHeaders().get("rocketmq_KEYS");
        ImMessageEntity messageEntity = messageService.getById(messageId);
        if (messageEntity != null) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}

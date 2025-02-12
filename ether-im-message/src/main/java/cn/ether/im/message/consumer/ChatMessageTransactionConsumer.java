/**
 * Copyright 2022-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ether.im.message.consumer;

import cn.ether.im.common.constants.ImConstants;
import cn.ether.im.common.enums.ImChatMessageStatus;
import cn.ether.im.common.enums.ImMessageSendResult;
import cn.ether.im.common.model.message.ImChatMessage;
import cn.ether.im.message.model.entity.ImMessageEntity;
import cn.ether.im.message.service.ImMessageService;
import cn.ether.im.sdk.agent.ImMessageAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "IM-CHAT-MESSAGE-TX-CONSUMERS",
        topic = ImConstants.IM_TX_MESSAGE_TOPIC, consumeMode = ConsumeMode.CONCURRENTLY)
public class ChatMessageTransactionConsumer
        implements RocketMQListener<ImChatMessage>, RocketMQPushConsumerLifecycleListener {

    @Resource
    private ImMessageAgent messageAgent;

    @Resource
    private ImMessageService messageService;

    @Override
    public void onMessage(ImChatMessage message) {
        log.info("监听到【对话消息-事物消息】|{}", message);
        ImMessageSendResult imMessageSendResult = messageAgent.sendChatMessage(message);
        log.info("消息发送结果：{}", imMessageSendResult);
        messageService.lambdaUpdate().eq(ImMessageEntity::getId, message.getMessageId())
                .eq(ImMessageEntity::getStatus, ImChatMessageStatus.INIT.name())
                .set(ImMessageEntity::getStatus, imMessageSendResult)
                .update();

        // 给自己所有终端发送消息
        message.setReceiverId(message.getSenderId());
        messageAgent.sendChatMessage(message);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
    }
}

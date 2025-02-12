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
package cn.ether.im.common.constants;


public class ImConstants {


    public static final String USER_KEY = "USER";


    public static final Integer READER_IDLE_TIME_SEC = 4 * 60;


    /**
     * Redis Key的分隔符
     */
    public static final String REDIS_KEY_SPLIT = ":";

    public static final String UN_RECEIVED_MSG_PREFIX = "un_received_msg:";


    public static final String GROUP_MEMBERS_PREFIX = "group:member:";


    /**
     * 在线状态过期时间
     */
    public static final long ONLINE_TIMEOUT_SECONDS = 3600;


    /**
     * 用户ID所连接的IM-server的ID
     */
    public final static String IM_USER_SERVER_ID = "im:user:server_id";

    /**
     * 对话消息Topic
     */
    public final static String IM_CHAT_MESSAGE_TOPIC = "im-chat-message-topic";

    /**
     * 群对话消息Topic
     */
    public final static String IM_GROUP_MESSAGE_TOPIC = "im-group-message-topic";


    /**
     * 单聊事物消息Topic
     */
    public final static String IM_TX_MESSAGE_TOPIC = "im-tx-message-topic";

    /**
     * 群聊事物消息Topic
     */
    public final static String IM_GROUP_TX_MESSAGE_TOPIC = "im-group-tx-message-topic";

    /**
     * 对话消息Topic
     */
    public final static String IM_CHAT_MESSAGE_TAG_PREFIX = "tag-server-id-";


    public final static String IM_MESSAGE_PUSH_CONSUMER_GROUP = "im-push-consumer-group";


    /**
     * IM用户登陆事件主题
     */
    public final static String IM_LOGIN_EVENT_TOPIC = "IM-LOGIN-EVENT-TOPIC";

    /**
     * IM单聊消息已接收事件主题
     */
    public final static String IM_SINGLE_MESSAGE_RECEIVED_EVENT_TOPIC = "im-single-message-received-event-topic";

    /**
     * IM群聊消息已接收事件主题
     */
    public final static String IM_GROUP_MESSAGE_RECEIVED_EVENT_TOPIC = "im-group-message-received-event-topic";

    public final static String IM_EVENT_TOPIC = "im-event-topic";

    /**
     * 心跳次数
     */
    public static final String HEARTBEAT_TIMES = "hb_times";

    /**
     * 服务器发送心跳最大次数，超过则断开连接
     */
    public static final Integer HEARTBEAT_MAX_TIMES = 3;

    public static final String MQ_TOPIC_SPLIT = "-";

    /**
     * 消息已读通知主题
     */
    public static final String IM_MESSAGE_READ_TOPIC = "im_message_read_topic";

    /**
     * 消息撤回通知主题
     */
    public static final String IM_MESSAGE_WITH_DRAWN_TOPIC = "im_message_with_drawn_topic";

    /**
     * 消息可撤回时间 单位秒
     */
    public static final Integer IM_MESSAGE_WITH_DRAWN_SEC = 120;
    public static final String IM_TOKEN = "token";
    public static final String TOKEN_SECRET = "marding";
    public static final String IM_EVENT_CONSUMER_GROUP = "IM-EVENT_CONSUMERS";

}
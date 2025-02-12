package cn.ether.im.message.single.model.dto;

import cn.ether.im.common.enums.ImMessageType;
import lombok.Data;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/22 02:10
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ChatMessageQueryReq {

    private String userId;

    private String receiverId;

    private ImMessageType messageType;

    private Long minMessageId;

    private Long maxMessageId;

    private Integer maxSize = 100;

    private Long startTime;

    private Long endTime;

}

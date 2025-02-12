package cn.ether.im.message.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 群聊消息发送请求对象
 * * @Author: Martin
 * * @Date    2024/9/15 15:00
 * * @Description
 **/
@Schema(description = "发送群聊消息请求对象")
@Data
public class GroupMessageSendReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "会话ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long conversationId;

    /**
     * 接收者ID 群聊是群ID
     */
    @Schema(name = "receiverId", description = "接收者ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String receiverId;

    /**
     * 消息内容
     */
    @Schema(name = "content", description = "消息内容", example = "测试群消息-01")
    private String content;

    /**
     * 消息内容类型: 文字 图片 文件 语音 视频
     */
    @Schema(name = "contentType", description = "消息内容类型: 文字 图片 文件 语音 视频", example = "TEXT|IMAGE|VIDEO|AUDIO|FILE")
    private String contentType;

}

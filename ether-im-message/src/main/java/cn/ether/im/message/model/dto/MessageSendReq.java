package cn.ether.im.message.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息发送请求对象
 * * @Author: Martin
 * * @Date    2024/9/15 15:00
 * * @Description
 **/
@Schema(description = "发送消息请求对象")
@Data
public class MessageSendReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "会话ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long conversationId;

    /**
     * 接收者ID
     */
    @Schema(name = "receiverId", description = "接收者ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "kefu-01")
    private String receiverId;

    @Schema(name = "receiverType", description = "接受者类型: 0-个人 1-群", example = "0")
    private Integer receiverType;


    /**
     * 消息内容
     */
    @Schema(name = "content", description = "消息内容", example = "测试消息-01")
    private String content;

    /**
     * 消息内容类型: 文字 图片 文件 语音 视频
     */
    @Schema(name = "contentType", description = "消息内容类型: 文字 图片 文件 语音 视频", example = "TEXT|IMAGE|VIDEO|AUDIO|FILE")
    private String contentType;

}

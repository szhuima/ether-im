package cn.ether.im.message.model.vo;

import cn.ether.im.common.enums.ImMessageContentType;
import cn.ether.im.common.enums.ImMessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/22 20:24
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Schema(title = "对话消息视图模型", description = "对话消息视图模型")
@Data
public class ImChatMessageVo {

    /**
     * 消息内容
     */
    @Schema(name = "消息内容", description = "消息内容", example = "测试消息-01")
    protected String content;
    /**
     * 消息内容类型
     */
    @Schema(name = "消息内容类型", description = "消息内容类型", example = "TEXT｜IMAGE｜VIDEO｜AUDIO｜FILE")
    protected ImMessageContentType contentType;
    /**
     * 发送时间戳
     */
    @Schema(name = "发送时间戳", description = "Long类型 毫秒", example = "1724333660412")
    protected Long sendTime;
    /**
     * 发送者ID
     */
    private String senderId;
    /**
     * 消息类型
     */
    @Schema(name = "对话消息类型", description = "", example = "PERSONAL｜GROUP")
    private ImMessageType type;
    /**
     * 消息ID
     */
    @Schema(name = "消息ID", description = "消息ID", format = "long", example = "1837845461734658048")
    private Long id;


}

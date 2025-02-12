package cn.ether.im.message.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息表
 *
 * @TableName im_message
 */
@TableName(value = "im_message")
@Data
public class ImMessageEntity implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 消息ID
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 发送者ID
     */
    @TableField(value = "sender_id")
    private String senderId;
    /**
     * 发送者终端
     */
    @TableField(value = "sender_terminal")
    private String senderTerminal;
    /**
     * 接收者ID
     */
    @TableField(value = "receiver_id")
    private String receiverId;
    /**
     * 接收者类型,0-个人 1-群组
     */
    @TableField(value = "receiver_type")
    private Integer receiverType;
    /**
     * 消息内容
     */
    @TableField(value = "content")
    private String content;
    /**
     * 消息类型
     */
    @TableField(value = "content_type")
    private String contentType;
    /**
     * 消息状态
     */
    @TableField(value = "status")
    private String status;
    /**
     * 发送时间
     */
    @TableField(value = "send_time")
    private Long sendTime;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
}
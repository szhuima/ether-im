package cn.ether.im.message.single.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 单聊离线消息表
 *
 * @TableName im_single_offline_message
 */
@TableName(value = "im_single_offline_message")
@Data
public class ImSingleOfflineMessage implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 消息ID
     */
    @TableId(value = "message_id")
    private Long messageId;
    /**
     * 接收者ID
     */
    @TableField(value = "receiver_id")
    private String receiverId;
}
package cn.ether.im.message.model.session;

import cn.ether.im.common.enums.ImTerminalType;
import lombok.Data;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/22 01:05
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Data
public class ImUserSession {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 终端类型
     */
    private ImTerminalType terminalType;

}

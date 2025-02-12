package cn.ether.im.message.mapper;

import cn.ether.im.message.model.entity.ImSingleOfflineMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jack
 * @description 针对表【im_single_offline_message(单聊离线消息表)】的数据库操作Mapper
 * @createDate 2024-10-05 23:03:56
 * @Entity cn.ether.im.message.single.model.entity.ImSingleOfflineMessage
 */
@Mapper
public interface ImSingleOfflineMessageMapper extends BaseMapper<ImSingleOfflineMessage> {

}





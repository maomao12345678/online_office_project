package com.xxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxx.server.pojo.MailLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 邮件日志 Mapper 接口
 * </p>
 *
 * @author Bing
 * @since 2021-01-13
 */
@Mapper
public interface MailLogMapper extends BaseMapper<MailLog> {

}

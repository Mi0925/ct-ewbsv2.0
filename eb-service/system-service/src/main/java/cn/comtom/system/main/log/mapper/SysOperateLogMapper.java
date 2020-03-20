package cn.comtom.system.main.log.mapper;

import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.log.entity.dbo.SysOperateLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysOperateLogMapper extends SystemMapper<SysOperateLog,String> {
}

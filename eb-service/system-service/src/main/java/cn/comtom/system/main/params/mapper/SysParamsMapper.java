package cn.comtom.system.main.params.mapper;

import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.params.entity.dbo.SysParams;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SysParamsMapper extends SystemMapper<SysParams,String> {
}

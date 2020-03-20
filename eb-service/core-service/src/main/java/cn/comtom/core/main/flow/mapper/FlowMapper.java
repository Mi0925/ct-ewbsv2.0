package cn.comtom.core.main.flow.mapper;

import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.flow.entity.dbo.DispatchFlow;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FlowMapper extends CoreMapper<DispatchFlow,String> {
}

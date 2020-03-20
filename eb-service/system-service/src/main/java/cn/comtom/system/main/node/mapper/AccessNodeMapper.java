package cn.comtom.system.main.node.mapper;

import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.node.entity.dbo.AccessNode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AccessNodeMapper extends SystemMapper<AccessNode,String> {
}

package cn.comtom.core.main.access.mapper;

import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.access.entity.dbo.Access;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AccessMapper extends CoreMapper<Access,String> {
}

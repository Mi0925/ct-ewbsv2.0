package cn.comtom.core.main.omd.mapper;

import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.omd.entity.dbo.OmdRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OmdRequestMapper extends CoreMapper<OmdRequest,String> {
}

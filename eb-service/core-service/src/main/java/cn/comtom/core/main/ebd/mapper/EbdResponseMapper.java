package cn.comtom.core.main.ebd.mapper;

import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebd.entity.dbo.EbdResponse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EbdResponseMapper extends CoreMapper<EbdResponse,String> {
}

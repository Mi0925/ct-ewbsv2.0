package cn.comtom.core.main.ebm.mapper;

import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebm.entity.dbo.EbmDispatchInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EbmDispatchInfoMapper extends CoreMapper<EbmDispatchInfo,String> {
}

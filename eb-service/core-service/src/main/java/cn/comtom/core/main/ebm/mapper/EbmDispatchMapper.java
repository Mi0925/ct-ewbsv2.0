package cn.comtom.core.main.ebm.mapper;

import cn.comtom.core.fw.BaseMapper;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebm.entity.dbo.EbmDispatch;
import cn.comtom.core.main.ebm.entity.dbo.EbmDispatchInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EbmDispatchMapper extends CoreMapper<EbmDispatch,String>,BaseMapper<cn.comtom.domain.core.ebm.info.EbmDispatchAndEbdFileInfo,String> {
}

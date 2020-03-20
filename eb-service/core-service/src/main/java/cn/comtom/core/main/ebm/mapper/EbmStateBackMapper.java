package cn.comtom.core.main.ebm.mapper;

import cn.comtom.core.fw.BaseMapper;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebm.entity.dbo.Ebm;
import cn.comtom.core.main.ebm.entity.dbo.EbmStateBack;
import cn.comtom.domain.core.ebm.info.EbmStateBackInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EbmStateBackMapper extends CoreMapper<EbmStateBack,String> ,BaseMapper<EbmStateBackInfo,String> {
}

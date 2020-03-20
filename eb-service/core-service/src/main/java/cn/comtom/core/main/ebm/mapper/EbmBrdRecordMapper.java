package cn.comtom.core.main.ebm.mapper;

import cn.comtom.core.fw.BaseMapper;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebm.entity.dbo.EbmBrdRecord;
import cn.comtom.domain.core.ebd.info.EbdInfo;
import cn.comtom.domain.core.ebm.info.EbmBrdRecordInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EbmBrdRecordMapper extends CoreMapper<EbmBrdRecord,String>,BaseMapper<EbmBrdRecordInfo,String> {
}

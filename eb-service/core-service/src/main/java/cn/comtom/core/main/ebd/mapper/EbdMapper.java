package cn.comtom.core.main.ebd.mapper;

import cn.comtom.core.fw.BaseMapper;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebd.entity.dbo.Ebd;
import cn.comtom.domain.core.access.info.AccessRecordInfo;
import cn.comtom.domain.core.ebd.info.EbdInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EbdMapper extends CoreMapper<Ebd,String> ,BaseMapper<EbdInfo,String> {
}

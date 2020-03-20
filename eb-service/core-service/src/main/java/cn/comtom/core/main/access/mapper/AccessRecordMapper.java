package cn.comtom.core.main.access.mapper;

import cn.comtom.core.fw.BaseMapper;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.access.entity.dbo.AccessRecord;
import cn.comtom.domain.core.access.info.AccessRecordInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AccessRecordMapper extends CoreMapper<AccessRecord,String>,BaseMapper<AccessRecordInfo,String> {


}

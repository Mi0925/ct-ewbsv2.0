package cn.comtom.core.main.scheme.mapper;

import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.scheme.entity.dbo.SchemeEbr;
import cn.comtom.domain.core.scheme.info.SchemeEbrInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SchemeEbrMapper extends CoreMapper<SchemeEbr,String> {
    List<SchemeEbrInfo> getSchemeEbrListBySchemeId(String schemeId);
}

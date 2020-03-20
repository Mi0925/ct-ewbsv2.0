package cn.comtom.core.main.scheme.mapper;

import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.scheme.entity.dbo.Scheme;
import cn.comtom.domain.core.scheme.info.SchemePageInfo;
import cn.comtom.domain.core.scheme.request.SchemeQueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SchemeMapper extends CoreMapper<Scheme,String> {
    List<SchemePageInfo> getSchemePageInfo(SchemeQueryRequest request);
}

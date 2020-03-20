package cn.comtom.reso.main.ebr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.request.EbrPlatformPageRequest;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.EbrPlatform;

@Mapper
@Repository
public interface EbrPlatformMapper extends ResoMapper<EbrPlatform,String> {
    EbrInfo getEbrInfoById(String ebrId);

    List<EbrPlatformInfo> getEbrPlatfromInfoListWithXML(EbrPlatformPageRequest request);
    
    int getAllCount(Map<String,String> params);
    
    int getNormalCount(Map<String,String> params);
}

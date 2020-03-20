package cn.comtom.reso.main.ebr.mapper;

import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.ebr.request.EbrQueryRequest;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.VEbr;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface VEbrMapper extends ResoMapper<VEbr,String> {
    List<EbrInfo> listForInfoSrc(EbrQueryRequest queryRequest);
}

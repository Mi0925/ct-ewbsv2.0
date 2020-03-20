package cn.comtom.reso.main.ebr.mapper;

import cn.comtom.domain.reso.ebr.info.EbrGisInfo;
import cn.comtom.domain.reso.ebr.info.EbrstatusCount;
import cn.comtom.domain.reso.ebr.request.EbrGisPageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface VEbrGisMapper {

    List<EbrGisInfo> getEbrGisByPage(EbrGisPageRequest request);

    List<EbrstatusCount> getEbrGisStatusCount(EbrGisPageRequest request);
}

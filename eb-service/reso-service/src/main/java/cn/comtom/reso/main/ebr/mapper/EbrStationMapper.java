package cn.comtom.reso.main.ebr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.ebr.request.EbrStationPageRequest;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.EbrStation;

@Mapper
@Repository
public interface EbrStationMapper extends ResoMapper<EbrStation,String> {

	List<EbrStationInfo> findListByStationIds(List<String> stationIds);

	List<EbrStationInfo> getListWithXml(EbrStationPageRequest request);

}

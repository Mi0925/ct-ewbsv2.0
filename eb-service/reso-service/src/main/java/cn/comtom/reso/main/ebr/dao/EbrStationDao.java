package cn.comtom.reso.main.ebr.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.ebr.request.EbrStationPageRequest;
import cn.comtom.domain.reso.ebr.request.StationWhereRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.EbrStation;
import cn.comtom.reso.main.ebr.mapper.EbrStationMapper;
import cn.comtom.tools.utils.DateUtil;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;


@Repository
public class EbrStationDao extends BaseDao<EbrStation,String> {

    @Autowired
    private EbrStationMapper mapper;


    @Override
    public ResoMapper<EbrStation, String> getMapper() {
        return mapper;
    }

    public List<EbrStation> findStationListByWhere(StationWhereRequest request) {
	   Weekend<EbrStation> weekend = Weekend.of(EbrStation.class);
       WeekendCriteria<EbrStation,Object> weekendCriteria = weekend.weekendCriteria();
       if(request.getSyncFlag() != null){
           weekendCriteria.andEqualTo(EbrStation::getSyncFlag,request.getSyncFlag());
       }
       if(request.getStatusSyncFlag() != null){
           weekendCriteria.andEqualTo(EbrStation::getStatusSyncFlag,request.getStatusSyncFlag());
       }
       if(StringUtils.isNotBlank(request.getRptStartTime())){
           weekendCriteria.andGreaterThan(EbrStation::getUpdateTime,DateUtil.stringToDate(request.getRptStartTime()) );
       }
       if(StringUtils.isNotBlank(request.getRptEndTime())){
           weekendCriteria.andLessThan(EbrStation::getUpdateTime,DateUtil.stringToDate(request.getRptEndTime()));
       }
       return mapper.selectByExample(weekend);
    }

	public List<EbrStationInfo> findListByStationIds(List<String> stationIds) {

		return mapper.findListByStationIds(stationIds);
	}

	public List<EbrStationInfo> getList(EbrStationPageRequest request) {
		return mapper.getListWithXml(request);
	}

	public int saveStation(EbrStation station) {
        return mapper.insertSelective(station);
    }
}

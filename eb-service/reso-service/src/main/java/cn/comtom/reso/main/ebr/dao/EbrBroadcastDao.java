package cn.comtom.reso.main.ebr.dao;

import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.request.BroadcastPageRequest;
import cn.comtom.domain.reso.ebr.request.BroadcastWhereRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.EbrBroadcast;
import cn.comtom.reso.main.ebr.mapper.EbrBroadcastMapper;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;


@Repository
public class EbrBroadcastDao extends BaseDao<EbrBroadcast,String> {

    @Autowired
    private EbrBroadcastMapper mapper;


    @Override
    public ResoMapper<EbrBroadcast, String> getMapper() {
        return mapper;
    }


    public List<EbrBroadcast> getMatchBroadcast(String areaCode, String relatedPsEbrId) {
        Weekend<EbrBroadcast> weekend = Weekend.of(EbrBroadcast.class);
        WeekendCriteria<EbrBroadcast,Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria.andEqualTo(EbrBroadcast::getAreaCode,areaCode);
        weekendCriteria.andEqualTo(EbrBroadcast::getRelatedPsEbrId,relatedPsEbrId);
        weekendCriteria.andEqualTo(EbrBroadcast::getBsState,Constants.EBR_STATE_RUNNING);
        return mapper.selectByExample(weekend);
    }

    public List<EbrBroadcastInfo> getList(BroadcastPageRequest request) {
        return mapper.getListWithXML(request);
    }

    public List<EbrBroadcast> findBroadcastListByWhere(BroadcastWhereRequest request) {
        Weekend<EbrBroadcast> weekend = Weekend.of(EbrBroadcast.class);
        WeekendCriteria<EbrBroadcast,Object> weekendCriteria = weekend.weekendCriteria();
        if(request.getSyncFlag() != null){
            weekendCriteria.andEqualTo(EbrBroadcast::getSyncFlag,request.getSyncFlag());
        }
        if(request.getStatusSyncFlag() != null){
            weekendCriteria.andEqualTo(EbrBroadcast::getStatusSyncFlag,request.getStatusSyncFlag());
        }
        if(request.getRptStartTime() != null ){
            weekendCriteria.andGreaterThan(EbrBroadcast::getUpdateTime,DateUtil.stringToDate(request.getRptStartTime()) );
        }
        if(request.getRptEndTime() != null ){
            weekendCriteria.andLessThan(EbrBroadcast::getUpdateTime,DateUtil.stringToDate(request.getRptEndTime()));
        }
        return mapper.selectByExample(weekend);
    }
}

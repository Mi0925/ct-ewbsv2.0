package cn.comtom.reso.main.ebr.dao;

import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.request.EbrPlatformPageRequest;
import cn.comtom.domain.reso.ebr.request.PlatformWhereRequest;
import cn.comtom.reso.fw.BaseDao;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.EbrPlatform;
import cn.comtom.reso.main.ebr.mapper.EbrPlatformMapper;
import cn.comtom.tools.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;
import java.util.Map;

@Repository
public class EbrPlatformDao extends BaseDao<EbrPlatform,String> {

    @Autowired
    private EbrPlatformMapper mapper;


    @Override
    public ResoMapper<EbrPlatform, String> getMapper() {
        return mapper;
    }

    public List<EbrPlatform> findPlatformListByWhere(PlatformWhereRequest request) {
        Weekend<EbrPlatform> weekend = Weekend.of(EbrPlatform.class);
        WeekendCriteria<EbrPlatform,Object> weekendCriteria = weekend.weekendCriteria();
        if(request.getSyncFlag() != null){
            weekendCriteria.andEqualTo(EbrPlatform::getSyncFlag,request.getSyncFlag());
        }
        if(request.getStatusSyncFlag() != null){
            weekendCriteria.andEqualTo(EbrPlatform::getStatusSyncFlag,request.getStatusSyncFlag());
        }
        if(request.getRptStartTime() != null ){
            weekendCriteria.andGreaterThan(EbrPlatform::getUpdateTime,DateUtil.stringToDate(request.getRptStartTime()) );
        }
        if(request.getRptEndTime() != null ){
            weekendCriteria.andLessThan(EbrPlatform::getUpdateTime,DateUtil.stringToDate(request.getRptEndTime()));
        }
        return mapper.selectByExample(weekend);
    }

    public EbrInfo getEbrInfoById(String ebrId) {
        return mapper.getEbrInfoById(ebrId);
    }

    public List<EbrPlatformInfo> list(EbrPlatformPageRequest request) {
        return mapper.getEbrPlatfromInfoListWithXML(request);
    }
    
    public int getAllCount(Map<String,String> params) {
    	
    	return mapper.getAllCount(params);
    }
    
    public int getNormalCount(Map<String,String> params) {
    	
    	return mapper.getNormalCount(params);
    }
}

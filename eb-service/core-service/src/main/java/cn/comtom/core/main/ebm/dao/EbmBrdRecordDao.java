package cn.comtom.core.main.ebm.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebm.entity.dbo.EbmBrdRecord;
import cn.comtom.core.main.ebm.mapper.EbmBrdRecordMapper;
import cn.comtom.domain.core.ebm.info.EbmBrdRecordInfo;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordPageRequest;
import cn.comtom.domain.core.ebm.request.EbmBrdRecordWhereRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;
import java.util.Map;

@Repository
public class EbmBrdRecordDao extends BaseDao<EbmBrdRecord,String> {

    @Autowired
    private EbmBrdRecordMapper mapper;


    @Override
    public CoreMapper<EbmBrdRecord, String> getMapper() {
        return mapper;
    }


    public List<EbmBrdRecord> getEbmBrdRecordListByEbmId(String ebmId) {
        Weekend<EbmBrdRecord> weekend = Weekend.of(EbmBrdRecord.class);
        WeekendCriteria<EbmBrdRecord,Object> weekendCriteria = weekend.weekendCriteria();
        weekendCriteria.andEqualTo(EbmBrdRecord::getEbmId,ebmId);
        return mapper.selectByExample(weekend);
    }

    public List<EbmBrdRecord> findEbmBrdRecordListByWhere(EbmBrdRecordWhereRequest request) {
        Weekend<EbmBrdRecord> weekend = Weekend.of(EbmBrdRecord.class);
        WeekendCriteria<EbmBrdRecord,Object> weekendCriteria = weekend.weekendCriteria();
        if(request.getSyncFlag() != null){
            weekendCriteria.andEqualTo(EbmBrdRecord::getSyncFlag,request.getSyncFlag());
        }
        if(request.getRptStartTime() != null ){
            weekendCriteria.andGreaterThan(EbmBrdRecord::getUpdateTime,request.getRptStartTime());
        }
        if(request.getRptEndTime() != null ){
            weekendCriteria.andLessThan(EbmBrdRecord::getUpdateTime,request.getRptEndTime());
        }
        return mapper.selectByExample(weekend);
    }

    public EbmBrdRecord getEbmBrdByEbmIdAndResourceId(String ebmId, String resourceId) {
        EbmBrdRecord ebmBrdRecord = new EbmBrdRecord();
        ebmBrdRecord.setEbmId(ebmId);
        ebmBrdRecord.setResourceId(resourceId);
        return mapper.selectOne(ebmBrdRecord);
    }

    public List<EbmBrdRecordInfo> page(Map<String,Object> map) {
        return mapper.queryList(map);
    }
}

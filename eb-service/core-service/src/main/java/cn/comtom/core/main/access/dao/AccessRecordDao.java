package cn.comtom.core.main.access.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.access.entity.dbo.AccessRecord;
import cn.comtom.core.main.access.mapper.AccessRecordMapper;
import cn.comtom.domain.core.access.info.AccessRecordInfo;
import cn.comtom.domain.core.access.request.AccessRecordPageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;
import java.util.Map;

@Repository
public class AccessRecordDao extends BaseDao<AccessRecord,String> {

    @Autowired
    private AccessRecordMapper mapper;

    @Override
    public CoreMapper<AccessRecord, String> getMapper() {
        return mapper;
    }

    public List<AccessRecordInfo> list(Map<String,Object> mapRequest) {
        return  mapper.queryList(mapRequest);
    }
}

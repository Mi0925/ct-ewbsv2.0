package cn.comtom.core.main.access.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.access.entity.dbo.Access;
import cn.comtom.core.main.access.mapper.AccessMapper;
import cn.comtom.domain.core.access.request.AccessInfoPageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class AccessDao extends BaseDao<Access,String> {

    @Autowired
    private AccessMapper mapper;

    @Override
    public CoreMapper<Access, String> getMapper() {
        return mapper;
    }

    public List<Access> list(AccessInfoPageRequest request) {
        Weekend<Access> weekend = Weekend.of(Access.class);
        WeekendCriteria<Access, Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(request.getNodeId())){
            weekendCriteria.andEqualTo(Access::getNodeId,request.getNodeId());
        }
        if(StringUtils.isNotBlank(request.getEventType())){
            weekendCriteria.andEqualTo(Access::getEventType,request.getEventType());
        }
        if(StringUtils.isNotBlank(request.getInfoLevel())){
            weekendCriteria.andEqualTo(Access::getInfoLevel,request.getInfoLevel());
        }
        if(StringUtils.isNotBlank(request.getInfoType())){
            weekendCriteria.andEqualTo(Access::getInfoType,request.getInfoType());
        }
        if(StringUtils.isNotBlank(request.getRelatedEbmId())){
            weekendCriteria.andEqualTo(Access::getRelatedEbmId,request.getRelatedEbmId());
        }
        if(StringUtils.isNotBlank(request.getSeverity())){
            weekendCriteria.andEqualTo(Access::getSeverity,request.getSeverity());
        }
        if(StringUtils.isNotBlank(request.getInfoTitle())){
            weekendCriteria.andLike(Access::getInfoTitle,"%".concat(request.getInfoTitle()).concat("%"));
        }

        return mapper.selectByExample(weekend);
    }
}

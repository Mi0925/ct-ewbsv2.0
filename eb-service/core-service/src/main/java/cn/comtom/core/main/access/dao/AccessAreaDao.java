package cn.comtom.core.main.access.dao;

import cn.comtom.core.fw.BaseDao;
import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.access.entity.dbo.AccessArea;
import cn.comtom.core.main.access.mapper.AccessAreaMapper;
import cn.comtom.domain.core.access.request.AccessAreaReq;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class AccessAreaDao extends BaseDao<AccessArea,String> {

    @Autowired
    private AccessAreaMapper accessAreaMapper;

    @Override
    public CoreMapper<AccessArea, String> getMapper() {
        return accessAreaMapper;
    }

    public List<AccessArea> list(AccessAreaReq req) {
        Weekend<AccessArea> weekend = Weekend.of(AccessArea.class);
        WeekendCriteria<AccessArea, Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(req.getAreaCode())){
            weekendCriteria.andEqualTo(AccessArea::getAreaCode,req.getAreaCode());
        }
        if(StringUtils.isNotBlank(req.getInfoId())){
            weekendCriteria.andEqualTo(AccessArea::getInfoId,req.getInfoId());
        }
        return accessAreaMapper.selectByExample(weekend);
    }

    public List<AccessArea> getByInfoId(String infoId) {
        Weekend<AccessArea> weekend = Weekend.of(AccessArea.class);
        WeekendCriteria<AccessArea,Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(infoId)){
            weekendCriteria.andEqualTo(AccessArea::getInfoId,infoId);
        }
        return accessAreaMapper.selectByExample(weekend);
    }

}

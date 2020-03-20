package cn.comtom.system.main.params.dao;

import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.params.entity.dbo.SysParams;
import cn.comtom.system.main.params.mapper.SysParamsMapper;
import cn.comtom.domain.system.sysparam.request.SysParamPageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class SysParamDao extends BaseDao<SysParams,String> {

    @Autowired
    private SysParamsMapper mapper;

    @Override
    public SystemMapper<SysParams, String> getMapper() {
        return mapper;
    }

    public List<SysParams> pageList(SysParamPageRequest request) {
        Weekend<SysParams> weekend = Weekend.of(SysParams.class);
        WeekendCriteria<SysParams, Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(request.getParamName())){
            weekendCriteria.andLike(SysParams::getParamName,"%".concat(request.getParamName()).concat("%"));
        }

        if(StringUtils.isNotBlank(request.getId())){
            weekendCriteria.andEqualTo(SysParams::getId,request.getId());
        }
        if(StringUtils.isNotBlank(request.getParamKey())){
            weekendCriteria.andEqualTo(SysParams::getParamKey,request.getParamKey());
        }
        if(StringUtils.isNotBlank(request.getParamValue())){
            weekendCriteria.andEqualTo(SysParams::getParamValue,request.getParamValue());
        }
        if(request.getParamType()!=null){
            weekendCriteria.andEqualTo(SysParams::getParamType,request.getParamType());
        }

        return mapper.selectByExample(weekend);
    }

    public SysParams getByKey(String paramKey) {
        SysParams sysParams = new SysParams();
        sysParams.setParamKey(paramKey);
        return mapper.selectOne(sysParams);
    }
}

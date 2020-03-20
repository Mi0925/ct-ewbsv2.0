package cn.comtom.system.main.resource.dao;

import cn.comtom.domain.system.sysresource.request.SysResourceQueryRequest;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.resource.entity.dbo.SysResource;
import cn.comtom.system.main.resource.mapper.SysResourceMapper;
import cn.comtom.tools.constants.SymbolConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class SysResourceDao extends BaseDao<SysResource,String> {

    @Autowired
    private SysResourceMapper sysResourceMapper;

    @Override
    public SystemMapper<SysResource, String> getMapper() {
        return sysResourceMapper;
    }


    public List<SysResource> list(SysResourceQueryRequest request) {
        Weekend<SysResource> weekend = Weekend.of(SysResource.class);
        WeekendCriteria<SysResource,Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(request.getPid())){
            weekendCriteria.andEqualTo(SysResource::getPid,request.getPid());
        }
        if(StringUtils.isNotBlank(request.getName())){
            weekendCriteria.andLike(SysResource::getName,SymbolConstants.MATH_CHARACTER_PERCENT.concat(request.getName()).concat(SymbolConstants.MATH_CHARACTER_PERCENT));
        }
        if(StringUtils.isNotBlank(request.getPerms())){
            weekendCriteria.andEqualTo(SysResource::getPerms,request.getPerms());
        }
        if(StringUtils.isNotBlank(request.getType())){
            weekendCriteria.andEqualTo(SysResource::getType,request.getType());
        }

        return sysResourceMapper.selectByExample(weekend);
    }

    public List<SysResource> getByPid(String pid) {
        SysResource sysResource = new SysResource();
        sysResource.setPid(pid);
        return sysResourceMapper.select(sysResource);
    }
}

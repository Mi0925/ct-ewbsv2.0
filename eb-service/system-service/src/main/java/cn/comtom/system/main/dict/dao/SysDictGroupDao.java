package cn.comtom.system.main.dict.dao;

import cn.comtom.domain.system.dict.info.SysDictGroupInfo;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.dict.entity.dbo.SysDictGroup;
import cn.comtom.system.main.dict.mapper.SysDictGroupMapper;
import cn.comtom.tools.constants.SymbolConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class SysDictGroupDao extends BaseDao<SysDictGroup,String> {

    @Autowired
    private SysDictGroupMapper sysDictGroupMapper;

    @Override
    public SystemMapper<SysDictGroup, String> getMapper() {
        return sysDictGroupMapper;
    }


    public List<SysDictGroup> list(SysDictGroupInfo sysDictGroupInfo) {
        Weekend<SysDictGroup> weekend = Weekend.of(SysDictGroup.class);
        WeekendCriteria<SysDictGroup,Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(sysDictGroupInfo.getDictGroupId())){
            weekendCriteria.andEqualTo(SysDictGroup::getDictGroupId,sysDictGroupInfo.getDictGroupId());
        }
        if(StringUtils.isNotBlank(sysDictGroupInfo.getDictGroupStatus())){
            weekendCriteria.andEqualTo(SysDictGroup::getDictGroupStatus,sysDictGroupInfo.getDictGroupStatus());
        }
        if(StringUtils.isNotBlank(sysDictGroupInfo.getDictGroupName())){
            weekendCriteria.andLike(SysDictGroup::getDictGroupName,
                    SymbolConstants.MATH_CHARACTER_PERCENT.concat(sysDictGroupInfo.getDictGroupName()).concat(SymbolConstants.MATH_CHARACTER_PERCENT));
        }
        return sysDictGroupMapper.selectByExample(weekend);
    }
}

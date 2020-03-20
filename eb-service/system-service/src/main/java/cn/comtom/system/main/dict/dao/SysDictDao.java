package cn.comtom.system.main.dict.dao;

import cn.comtom.domain.system.dict.info.SysDictInfo;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.dict.entity.dbo.SysDict;
import cn.comtom.system.main.dict.mapper.SysDictMapper;
import cn.comtom.tools.constants.SymbolConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;

@Repository
public class SysDictDao extends BaseDao<SysDict,String> {

    @Autowired
    private SysDictMapper sysDictMapper;

    @Override
    public SystemMapper<SysDict, String> getMapper() {
        return sysDictMapper;
    }


    public List<SysDict> list(SysDictInfo sysDictInfo) {
        Weekend<SysDict> weekend = Weekend.of(SysDict.class);
        WeekendCriteria<SysDict,Object> weekendCriteria = weekend.weekendCriteria();
        if(StringUtils.isNotBlank(sysDictInfo.getDictGroupCode())){
            weekendCriteria.andEqualTo(SysDict::getDictGroupCode,sysDictInfo.getDictGroupCode());
        }
        if(StringUtils.isNotBlank(sysDictInfo.getDictKey())){
            weekendCriteria.andEqualTo(SysDict::getDictKey,sysDictInfo.getDictKey());
        }
        if(StringUtils.isNotBlank(sysDictInfo.getDictStatus())){
            weekendCriteria.andEqualTo(SysDict::getDictStatus,sysDictInfo.getDictStatus());
        }
        if(StringUtils.isNotBlank(sysDictInfo.getDictValue())){
            weekendCriteria.andLike(SysDict::getDictValue,
                    SymbolConstants.MATH_CHARACTER_PERCENT.concat(sysDictInfo.getDictValue()).concat(SymbolConstants.MATH_CHARACTER_PERCENT));
        }
        return sysDictMapper.selectByExample(weekend);
    }
}

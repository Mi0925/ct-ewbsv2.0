package cn.comtom.system.main.dict.mapper;

import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.dict.entity.dbo.SysDictGroup;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户持久化接口
 * @author baichun
 * @Date 2018-10-29 9:33
 */
@Repository
@Mapper
public interface SysDictGroupMapper extends SystemMapper<SysDictGroup,String> {

}

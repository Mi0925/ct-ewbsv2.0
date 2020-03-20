package cn.comtom.system.main.role.mapper;

import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.role.entity.dbo.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户持久化接口
 * @author guomao
 * @Date 2018-10-29 9:33
 */
@Repository
@Mapper
public interface SysRoleMapper extends SystemMapper<SysRole,String> {

}

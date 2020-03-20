package cn.comtom.core.main.access.mapper;

import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.access.entity.dbo.AccessFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AccessFileMapper extends CoreMapper<AccessFile,String> {
}

package cn.comtom.core.main.ebd.mapper;

import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.ebd.entity.dbo.EbdFiles;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EbdFilesMapper extends CoreMapper<EbdFiles,String> {
}

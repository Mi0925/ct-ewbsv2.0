package cn.comtom.reso.main.file.mapper;

import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.file.entity.dbo.OriginFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OriginFileMapper extends ResoMapper<OriginFile,String> {
}

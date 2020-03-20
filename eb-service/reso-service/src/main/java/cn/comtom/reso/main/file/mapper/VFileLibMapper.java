package cn.comtom.reso.main.file.mapper;

import cn.comtom.domain.reso.file.info.VFileLibInfo;
import cn.comtom.domain.reso.file.request.VFileLibPageRequest;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.file.entity.dbo.VFileLib;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface VFileLibMapper extends ResoMapper<VFileLib,String> {

    List<VFileLibInfo> getListFromXml(VFileLibPageRequest request);
}

package cn.comtom.reso.main.ebr.mapper;

import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.EbrIdCreator;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EbrIdCreatorMapper extends ResoMapper<EbrIdCreator,String> {
}

package cn.comtom.reso.main.ebr.mapper;

import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.request.BroadcastPageRequest;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.EbrBroadcast;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EbrBroadcastMapper extends ResoMapper<EbrBroadcast,String> {
    List<EbrBroadcastInfo> getListWithXML(BroadcastPageRequest request);
}

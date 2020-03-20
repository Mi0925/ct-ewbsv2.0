package cn.comtom.reso.main.ebr.mapper;

import cn.comtom.domain.reso.ebr.info.EbrTerminalInfo;
import cn.comtom.domain.reso.ebr.request.EbrTerminalPageRequest;
import cn.comtom.reso.fw.ResoMapper;
import cn.comtom.reso.main.ebr.entity.dbo.EbrTerminal;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EbrTerminalMapper extends ResoMapper<EbrTerminal,String> {

    List<EbrTerminalInfo> getListWithXml(EbrTerminalPageRequest request);

    List<EbrTerminalInfo> findListByTremIds(List<String> list);
}

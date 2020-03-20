package cn.comtom.core.main.program.mapper;

import cn.comtom.core.fw.CoreMapper;
import cn.comtom.core.main.program.entity.dbo.ProgramUnit;
import cn.comtom.domain.core.program.info.ProgramUnitInfo;
import cn.comtom.domain.core.program.request.ProgramUnitPageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ProgramUnitMapper extends CoreMapper<ProgramUnit,String> {

    List<ProgramUnitInfo> getProgramUnitInfoList(ProgramUnitPageRequest request);
}

package cn.comtom.core.main.program.service;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.program.entity.dbo.ProgramUnit;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.program.info.ProgramUnitInfo;
import cn.comtom.domain.core.program.request.EbmCreateRequest;
import cn.comtom.domain.core.program.request.ProgramUnitPageRequest;

import java.util.List;

public interface IProgramUnitService extends BaseService<ProgramUnit,String> {

    List<ProgramUnitInfo> getProgramUnitInfoList(ProgramUnitPageRequest request);

    List<EbmInfo> createEbms(EbmCreateRequest request);

    void cancel(List<ProgramUnitInfo> programUnitInfos);
}

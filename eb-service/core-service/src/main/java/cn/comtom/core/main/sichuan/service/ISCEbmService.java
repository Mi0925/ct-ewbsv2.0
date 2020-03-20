package cn.comtom.core.main.sichuan.service;

import java.util.List;

import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.program.entity.dbo.ProgramUnit;
import cn.comtom.core.main.sichuan.entity.EBM;
import cn.comtom.domain.core.program.info.ProgramUnitInfo;
import cn.comtom.domain.core.program.request.ProgramUnitPageRequest;

public interface ISCEbmService extends BaseService<ProgramUnit,String> {

    List<ProgramUnitInfo> getProgramUnitInfoList(ProgramUnitPageRequest request);

    void createEbms(EBM ebm);

    void cancel(List<ProgramUnitInfo> programUnitInfos);
}

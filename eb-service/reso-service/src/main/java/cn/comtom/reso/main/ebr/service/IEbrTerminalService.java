package cn.comtom.reso.main.ebr.service;

import cn.comtom.domain.reso.ebr.info.EbrTerminalInfo;
import cn.comtom.domain.reso.ebr.request.EbrTerminalPageRequest;
import cn.comtom.domain.reso.ebr.request.TerminalConditionRequest;
import cn.comtom.domain.reso.ebr.request.TerminalWhereRequest;
import cn.comtom.reso.fw.BaseService;
import cn.comtom.reso.main.ebr.entity.dbo.EbrTerminal;

import java.util.List;

public interface IEbrTerminalService extends BaseService<EbrTerminal,String> {

    List<EbrTerminalInfo> getList(EbrTerminalPageRequest request);

    List<EbrTerminalInfo> findListByTremIds(List<String> devTrmIds);

    List<EbrTerminalInfo> findTerminalListByWhere(TerminalWhereRequest request);

	Integer countTerminalByCondition(TerminalConditionRequest terminalConditionRequest);
}

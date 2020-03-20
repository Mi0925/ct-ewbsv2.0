package cn.comtom.core.main.ebm.service;


import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.ebm.entity.dbo.Ebm;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.ebm.request.EbmPageRequest;

import java.util.List;

public interface IEbmService extends BaseService<Ebm,String> {
    List<EbmInfo> getDispatchEbm(String ebmState);

    List<EbmInfo> list(EbmPageRequest request);

	Boolean cancelEbmPlay(String ebmId);

	Ebm selectByRelatedEbmId(String ebmid);

}

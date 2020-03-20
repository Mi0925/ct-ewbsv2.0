package cn.comtom.core.main.ebm.service;


import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.ebm.entity.dbo.EbmDispatch;
import cn.comtom.domain.core.ebm.info.EbmDispatchAndEbdFileInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfo;
import cn.comtom.domain.core.ebm.request.EbmDispatchPageRequest;

import java.util.List;

public interface IEbmDispatchService extends BaseService<EbmDispatch,String> {
    List<EbmDispatchInfo> getEbmDispatchByEbmId(String ebmId);

    List<EbmDispatchInfo> getEbmDispatchByEbdId(String ebdId);

    List<EbmDispatchInfo> getEbmDispatchList(EbmDispatchPageRequest req);

    List<EbmDispatchInfo> getEbmDispatchByMatchedEbdId(String matchedEbdId);

    List<EbmDispatchAndEbdFileInfo> getEbmDispatchAndEbdFileByEbmId(String ebmId);
}

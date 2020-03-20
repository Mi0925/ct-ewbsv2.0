package cn.comtom.core.main.ebm.service;


import cn.comtom.core.fw.BaseService;
import cn.comtom.core.main.ebm.entity.dbo.EbmDispatchInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfoInfo;

import java.util.List;

public interface IEbmDispatchInfoService extends BaseService<EbmDispatchInfo,String> {
    List<EbmDispatchInfoInfo> getByEbmId(String ebmId);
}
